package org.eclipse.epsilon.eol.viatra.mapping.data;

import java.util.Objects;

import org.eclipse.epsilon.eol.dom.Expression;

public class PatternParameter {

	private String name;
	private String type;
	private Expression value;

	public PatternParameter(String name, String type, Expression value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Expression getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, type, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PatternParameter)) {
			return false;
		}
		PatternParameter other = (PatternParameter) obj;
		return Objects.equals(name, other.name) && Objects.equals(type, other.type)
				&& Objects.equals(value, other.value);
	}

}
