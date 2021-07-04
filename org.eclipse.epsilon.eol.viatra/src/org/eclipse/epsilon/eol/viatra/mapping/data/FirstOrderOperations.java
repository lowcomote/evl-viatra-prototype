package org.eclipse.epsilon.eol.viatra.mapping.data;

public class FirstOrderOperations {

	public static final String SELECT = "select";
	public static final String SELECT_ONE = "selectOne";
	public static final String EXISTS = "exists";
	public static final String COUNT = "count";
	public static final String REJECT = "reject";
	public static final String NONE = "none";
	public static final String ONE = "one";
	public static final String N_MATCH = "nMatch";
	public static final String AT_LEAST_N_MATCH = "atLeastNMatch";
	public static final String AT_MOST_N_MATCH = "atMostNMatch";
	public static final String FOR_ALL = "forAll";

	private FirstOrderOperations() {
	}

	public static boolean isOptimisableOperation(String name) {
		switch (name) {
		case SELECT:
		case SELECT_ONE:
		case EXISTS:
		case COUNT:
		case REJECT:
		case NONE:
		case ONE:
		case N_MATCH:
		case AT_LEAST_N_MATCH:
		case AT_MOST_N_MATCH:
		case FOR_ALL:
			return true;
		}
		return false;
	}

}
