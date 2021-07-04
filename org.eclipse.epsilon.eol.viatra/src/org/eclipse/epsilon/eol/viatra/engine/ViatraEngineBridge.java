package org.eclipse.epsilon.eol.viatra.engine;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.eol.viatra.IRunViatra;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;
import org.eclipse.epsilon.eol.viatra.mapping.data.ViatraOperations;
import org.eclipse.epsilon.eol.viatra.mapping.utils.ExpressionUtil;
import org.eclipse.viatra.query.patternlanguage.emf.specification.GenericQuerySpecification;
import org.eclipse.viatra.query.runtime.api.AdvancedViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.GenericPatternMatch;
import org.eclipse.viatra.query.runtime.api.GenericPatternMatcher;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngineOptions;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.base.api.BaseIndexOptions;
import org.eclipse.viatra.query.runtime.emf.EMFScope;
import org.eclipse.viatra.query.runtime.localsearch.matcher.integration.LocalSearchHints;
import org.eclipse.viatra.query.runtime.matchers.backend.QueryEvaluationHint;

public class ViatraEngineBridge implements IRunViatra, AutoCloseable {

	private static final Logger LOGGER = Logger.getLogger(ViatraEmfModel.class.getName());

	private ParsedPatternCache patternCache;
	private AdvancedViatraQueryEngine engine;

	public ViatraEngineBridge(Resource resource, LocalSearchEngineOptions localSearchEngineOptions) {
		LOGGER.info("Initializing Viatra");
		patternCache = new ParsedPatternCache();

		EMFScope scope = initializeScope(resource);
		engine = createQueryEngine(scope, localSearchEngineOptions);
	}
	
	private AdvancedViatraQueryEngine createQueryEngine(EMFScope scope, LocalSearchEngineOptions localSearchEngineOptions) {
		if (localSearchEngineOptions.isLocalSearchEnabled()) {
			QueryEvaluationHint localSearchHint = null;
			
			if (localSearchEngineOptions.useBaseIndex()) {
				localSearchHint = LocalSearchHints.getDefault().build();
			} else {
				localSearchHint = LocalSearchHints.getDefaultNoBase().build();
			}
			
			ViatraQueryEngineOptions options = ViatraQueryEngineOptions
					.defineOptions()
					.withDefaultHint(localSearchHint)
					.withDefaultBackend(localSearchHint.getQueryBackendFactory())
					.build();
			return AdvancedViatraQueryEngine.createUnmanagedEngine(scope, options);
		} else {
			return AdvancedViatraQueryEngine.createUnmanagedEngine(scope);
		}
	}

	private EMFScope initializeScope(Resource resource) {
		// enable dynamic EMF mode, because EClasses are loaded differently in EOL
		BaseIndexOptions options = new BaseIndexOptions().withDynamicEMFMode(true);
		EMFScope scope = new EMFScope(resource, options);
		return scope;
	}

	private IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> parsePattern(
			String patternDefinition, String patternName) {
		LOGGER.info("Parsing patterns");
		long start = System.currentTimeMillis();

		IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> querySpecification = patternCache
				.parsePattern(patternDefinition, patternName);

		long finish = System.currentTimeMillis();
		LOGGER.info(String.format("Took: %d ms", finish - start));

		return querySpecification;
	}

	private ViatraQueryMatcher<? extends IPatternMatch> getMatcher(
			IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> querySpecification) {
		LOGGER.info(String.format("Getting matcher for %s", querySpecification.getSimpleName()));
		long start = System.currentTimeMillis();

		ViatraQueryMatcher<? extends IPatternMatch> matcher = querySpecification.getMatcher(engine);

		long finish = System.currentTimeMillis();
		LOGGER.info(String.format("Took: %d ms", finish - start));
		return matcher;
	}

	private Object collectResults(ViatraQueryMatcher<? extends IPatternMatch> matcher, String methodName,
			int expectedNumberOfMatches, Object firstPatternParameter, Object secondPatternParameter,
			Object thirdPatternParameter) {
		LOGGER.info(String.format("Calling %s on %s", methodName, matcher.getPatternName()));

		Object result = null;
		long start = System.currentTimeMillis();

		boolean hasBoundParameters = firstPatternParameter != null || secondPatternParameter != null
				|| thirdPatternParameter != null;
		if (hasBoundParameters) {
			result = getResultFromPartialMatches(matcher, methodName, expectedNumberOfMatches, firstPatternParameter,
					secondPatternParameter, thirdPatternParameter);
		} else {
			switch (methodName) {
			case ViatraOperations.ALL_MATCHES: {
				result = matcher.getAllMatches().stream().map(match -> match.get(0)).collect(Collectors.toSet());
				break;
			}
			case ViatraOperations.ONE_ARBITRARY_MATCH: {
				result = matcher.getOneArbitraryMatch().map(match -> match.get(0)).orElse(null);
				break;
			}
			case ViatraOperations.HAS_MATCH: {
				result = matcher.hasMatch();
				break;
			}
			case ViatraOperations.HAS_MATCH_IS_FALSE: {
				result = matcher.hasMatch() == false;
				break;
			}
			case ViatraOperations.COUNT_MATCHES: {
				result = matcher.countMatches();
				break;
			}
			case ViatraOperations.COUNT_MATCHES_EQUALS: {
				result = matcher.countMatches() == expectedNumberOfMatches;
				break;
			}
			case ViatraOperations.COUNT_MATCHES_AT_LEAST: {
				result = matcher.countMatches() >= expectedNumberOfMatches;
				break;
			}
			case ViatraOperations.COUNT_MATCHES_AT_MOST: {
				result = matcher.countMatches() <= expectedNumberOfMatches;
				break;
			}
			default:
				throw new IllegalArgumentException("Unsupported operation: " + methodName);
			}
		}

		long finish = System.currentTimeMillis();
		LOGGER.info(String.format("Took: %d ms", finish - start));
		return result;
	}

	private Object getResultFromPartialMatches(ViatraQueryMatcher<? extends IPatternMatch> matcher, String methodName,
			int expectedNumberOfMatches, Object firstPatternParameter, Object secondPatternParameter,
			Object thirdPatternParameter) {
		GenericQuerySpecification querySpecification = (GenericQuerySpecification) matcher.getSpecification();

		firstPatternParameter = convertToDoubleIfFloat(firstPatternParameter);
		secondPatternParameter = convertToDoubleIfFloat(secondPatternParameter);
		thirdPatternParameter = convertToDoubleIfFloat(thirdPatternParameter);

		GenericPatternMatch boundMatch = GenericPatternMatch.newMatch(querySpecification, null, firstPatternParameter,
				secondPatternParameter, thirdPatternParameter);

		GenericPatternMatcher genericMatcher = (GenericPatternMatcher) matcher;

		switch (methodName) {
		case ViatraOperations.ALL_MATCHES: {
			return genericMatcher.getAllMatches(boundMatch).stream().map(match -> match.get(0))
					.collect(Collectors.toSet());
		}
		case ViatraOperations.ONE_ARBITRARY_MATCH: {
			return genericMatcher.getOneArbitraryMatch(boundMatch).map(match -> match.get(0)).orElse(null);
		}
		case ViatraOperations.HAS_MATCH: {
			return genericMatcher.hasMatch(boundMatch);
		}
		case ViatraOperations.HAS_MATCH_IS_FALSE: {
			return genericMatcher.hasMatch(boundMatch) == false;
		}
		case ViatraOperations.COUNT_MATCHES: {
			return genericMatcher.countMatches(boundMatch);
		}
		case ViatraOperations.COUNT_MATCHES_EQUALS: {
			return genericMatcher.countMatches(boundMatch) == expectedNumberOfMatches;
		}
		case ViatraOperations.COUNT_MATCHES_AT_LEAST: {
			return genericMatcher.countMatches(boundMatch) >= expectedNumberOfMatches;
		}
		case ViatraOperations.COUNT_MATCHES_AT_MOST: {
			return genericMatcher.countMatches(boundMatch) <= expectedNumberOfMatches;
		}
		default:
			throw new IllegalArgumentException("Unsupported operation: " + methodName);
		}
	}

	private Object convertToDoubleIfFloat(Object value) {
		// "Real" types are Float at runtime. However in the models Double is mostly
		// used instead of Float.
		if (value instanceof Float && "Double".equals(ExpressionUtil.EOL_REAL_SUBSTITUTE_TYPE)) {
			value = Double.valueOf(value.toString());
		}
		return value;
	}

	@Override
	public Object runViatra(String patternDefinition, String patternName, String methodName,
			int expectedNumberOfMatches, Object firstPatternParameter, Object secondPatternParameter,
			Object thirdPatternParameter) {
		IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> querySpecification = parsePattern(
				patternDefinition, patternName);
		ViatraQueryMatcher<? extends IPatternMatch> matcher = getMatcher(querySpecification);
		Object result = collectResults(matcher, methodName, expectedNumberOfMatches, firstPatternParameter,
				secondPatternParameter, thirdPatternParameter);
		return result;
	}

	@Override
	public void close() {
		engine.dispose();
		patternCache.clear();
	}

}
