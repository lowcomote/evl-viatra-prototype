package org.eclipse.epsilon.eol.viatra.mapping.data;

import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.IEolVisitor;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;

public class DefaultExpression extends Expression{

	@Override
	public Object execute(IEolContext context) throws EolRuntimeException {
		return null;
	}

	@Override
	public void accept(IEolVisitor visitor) {
	}

}
