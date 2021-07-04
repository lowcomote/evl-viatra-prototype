package org.eclipse.epsilon.eol.viatra.mapping.data;

import java.util.Objects;

public class EolCondition {

	private String name;
	private String operator;
	private Object value;

	public EolCondition(String name, String operator, Object value) {
		this.name = name;
		this.operator = operator;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getOperator() {
		return operator;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, operator, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EolCondition)) {
			return false;
		}
		EolCondition other = (EolCondition) obj;
		return Objects.equals(name, other.name) && Objects.equals(operator, other.operator)
				&& Objects.equals(value, other.value);
	}

}
