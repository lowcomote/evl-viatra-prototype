package org.eclipse.epsilon.eol.viatra;

public interface IRunViatra {
	
	public String METHOD_NAME = "runViatra";
	
	public Object runViatra(String patternDefintion, String patternName, String methodName, int expectedNumberOfMatches, Object firstPatternParameter, Object secondPatternParameter, Object thirdPatternParameter);

}
