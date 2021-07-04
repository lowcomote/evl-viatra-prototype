package org.eclipse.epsilon.eol.viatra.mapping.utils;

import java.util.Set;

import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.FeatureCallExpression;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalyser;
import org.eclipse.epsilon.eol.types.EolAnyType;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.eol.types.EolType;

public class ExpressionUtil {
	
	public static final String EOL_REAL_SUBSTITUTE_TYPE = "Double";
	protected static final Set<String> JAVA_TYPES = Set.of("String", "Integer", "Double", "Float", "Real", "Boolean");
	
	private ExpressionUtil(){}
	
	public static String getName(Expression expression) {
		if (expression instanceof FeatureCallExpression) {
			return ((FeatureCallExpression) expression).getName();
		} else if(expression instanceof NameExpression) {
			return ((NameExpression) expression).getName();
		}
		return null;
	}
	
	public static String getResolvedTypeName(Expression expression, EolStaticAnalyser staticAnalyser) {
		EolType resolvedType = staticAnalyser.getResolvedType(expression);
		if (resolvedType == null) {
			throw new IllegalArgumentException("Resolved type of expression is null: " + expression);
		}
		
		String typeName;
		if (resolvedType instanceof EolAnyType) {
			typeName = null;
		} else if(resolvedType instanceof EolModelElementType) {
			typeName = ((EolModelElementType) resolvedType).getTypeName();
		} else {
			typeName = resolvedType.getName();
			
			if(JAVA_TYPES.contains(typeName)) {
				// "Real" is not a valid type in Java, therefore it should be substituted.
				if ("Real".equals(typeName)) {
					typeName = EOL_REAL_SUBSTITUTE_TYPE;
				}
				typeName = String.format("java %s", typeName);
			}
		}
		
		return typeName;
	}

}
