package org.eclipse.epsilon.eol.viatra.mapping.data;

public class ViatraOperations {

	public static final String ALL_MATCHES = "allMatches";
	public static final String ONE_ARBITRARY_MATCH = "oneArbitraryMatch";

	public static final String HAS_MATCH = "hasMatch";
	public static final String HAS_MATCH_IS_FALSE = "hasMatchIsFalse";

	public static final String COUNT_MATCHES = "countMatches";
	public static final String COUNT_MATCHES_EQUALS = "countMatchesEquals";
	public static final String COUNT_MATCHES_AT_LEAST = "countMatchesAtLeast";
	public static final String COUNT_MATCHES_AT_MOST = "countMatchesAtMost";
	
	private ViatraOperations() {}

	public static boolean isSupportedOperation(String name) {
		switch (name) {
		case ALL_MATCHES:
		case ONE_ARBITRARY_MATCH:
		case HAS_MATCH:
		case HAS_MATCH_IS_FALSE:
		case COUNT_MATCHES:
		case COUNT_MATCHES_EQUALS:
		case COUNT_MATCHES_AT_LEAST:
		case COUNT_MATCHES_AT_MOST:
			return true;
		}
		return false;
	}

}
