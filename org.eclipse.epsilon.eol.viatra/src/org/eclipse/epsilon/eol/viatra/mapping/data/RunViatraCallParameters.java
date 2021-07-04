package org.eclipse.epsilon.eol.viatra.mapping.data;

import java.util.List;

import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.IntegerLiteral;
import org.eclipse.epsilon.eol.dom.StringLiteral;

public class RunViatraCallParameters {

	private String patterns;
	private String mainPattern;
	private String methodName;
	private int expectedNumberOfMatches;

	private Expression firstPatternParameter = new DefaultExpression();
	private Expression secondPatternParameter = new DefaultExpression();
	private Expression thirdPatternParameter = new DefaultExpression();

	public RunViatraCallParameters(String patternDefinition, String patternName, String methodName,
			int expectedNumberOfMatches, List<Expression> parameters) {
		this.patterns = patternDefinition;
		this.mainPattern = patternName;
		this.methodName = methodName;
		this.expectedNumberOfMatches = expectedNumberOfMatches;

		if (parameters.size() > 0) {
			this.firstPatternParameter = parameters.get(0);
			if (parameters.size() > 1) {
				this.secondPatternParameter = parameters.get(1);
				if (parameters.size() > 2) {
					this.thirdPatternParameter = parameters.get(2);
					if (parameters.size() > 3) {
						throw new IllegalArgumentException(
								"Query may have at most 3 parameters. Got: " + parameters.size());
					}
				}
			}
		}

	}

	public StringLiteral getPatterns() {
		return new StringLiteral(patterns);
	}

	public StringLiteral getMainPattern() {
		return new StringLiteral(mainPattern);
	}

	public StringLiteral getMethodName() {
		return new StringLiteral(methodName);
	}

	public IntegerLiteral getNumberOfMatches() {
		return new IntegerLiteral(expectedNumberOfMatches);
	}

	public Expression getFirstPatternParameter() {
		return firstPatternParameter;
	}

	public Expression getSecondPatternParameter() {
		return secondPatternParameter;
	}

	public Expression getThirdPatternParameter() {
		return thirdPatternParameter;
	}

}
