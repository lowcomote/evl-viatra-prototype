package org.eclipse.epsilon.eol.viatra.engine;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParser;
import org.eclipse.viatra.query.patternlanguage.emf.util.PatternParserBuilder;
import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;

public class ParsedPatternCache {

	private PatternParser patternParser;
	private Map<String, IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>>> querySpecificationByPatternName;

	public ParsedPatternCache() {
		// initialize Xtext for pattern language
		new EMFPatternLanguageStandaloneSetup().createStandaloneInjector();
		this.patternParser = PatternParserBuilder.instance().withClassLoader(getClass().getClassLoader()).build();
		this.querySpecificationByPatternName = new HashMap<>();
	}

	public IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> parsePattern(
			String patternDefinition, String patternName) {
		IQuerySpecification<? extends ViatraQueryMatcher<? extends IPatternMatch>> querySpecification = querySpecificationByPatternName
				.get(patternName);
		if (querySpecification != null) {
			return querySpecification;
		}
		
		querySpecification = patternParser.parse(patternDefinition).getQuerySpecification(patternName).orElse(null);
		if(querySpecification == null) {
			throw new IllegalArgumentException(String.format("Pattern %s was incorrectly parsed.", patternName));
		}
		querySpecificationByPatternName.put(patternName, querySpecification);
		
		return querySpecification;
	}
	
	public void clear() {
		querySpecificationByPatternName.clear();
	}

}
