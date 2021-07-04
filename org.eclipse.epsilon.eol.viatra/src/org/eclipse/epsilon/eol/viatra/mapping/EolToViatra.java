package org.eclipse.epsilon.eol.viatra.mapping;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.AndOperatorExpression;
import org.eclipse.epsilon.eol.dom.AssignmentStatement;
import org.eclipse.epsilon.eol.dom.ExecutableBlock;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.ExpressionStatement;
import org.eclipse.epsilon.eol.dom.FeatureCallExpression;
import org.eclipse.epsilon.eol.dom.FirstOrderOperationCallExpression;
import org.eclipse.epsilon.eol.dom.ForStatement;
import org.eclipse.epsilon.eol.dom.IfStatement;
import org.eclipse.epsilon.eol.dom.IntegerLiteral;
import org.eclipse.epsilon.eol.dom.LiteralExpression;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.NegativeOperatorExpression;
import org.eclipse.epsilon.eol.dom.OperationCallExpression;
import org.eclipse.epsilon.eol.dom.OperatorExpression;
import org.eclipse.epsilon.eol.dom.OrOperatorExpression;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.dom.Statement;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.dom.StringLiteral;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalyser;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalysisContext;
import org.eclipse.epsilon.eol.types.EolModelElementType;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.epsilon.eol.viatra.IRunViatra;
import org.eclipse.epsilon.eol.viatra.mapping.data.EolCondition;
import org.eclipse.epsilon.eol.viatra.mapping.data.EolOperations;
import org.eclipse.epsilon.eol.viatra.mapping.data.FirstOrderOperations;
import org.eclipse.epsilon.eol.viatra.mapping.data.PatternParameter;
import org.eclipse.epsilon.eol.viatra.mapping.data.RunViatraCallParameters;
import org.eclipse.epsilon.eol.viatra.mapping.data.ViatraOperations;
import org.eclipse.epsilon.eol.viatra.mapping.utils.ExpressionUtil;

public class EolToViatra {

	protected String metamodelNsuri = "";
	protected String modelElementTypeName = "";

	protected List<EolCondition> conditions = new LinkedList<>();
	protected List<PatternParameter> patternParameters = new LinkedList<>();

	protected boolean isOptimisable = true;
	protected boolean injectPrintln = false;
	protected String printParameter = "";

	protected String viatraMethodName = "";
	protected String firstOrderMethodName = "";

	protected int patternNameSequenceNumber = 0;
	protected int expectedNumberOfMatches = 0;
	
	protected EolStaticAnalyser staticAnalyser;

	public void map(IModel model, IEolModule module, EolStaticAnalysisContext context, EolStaticAnalyser staticAnalyser) {
		this.staticAnalyser = staticAnalyser;
		for (ModelDeclaration declaration : module.getModelDelcarations()) {
			if (declaration.getModel() == model) {
				metamodelNsuri = declaration.getModelDeclarationParameter("nsuri").getValue();
			}
		}
		List<Statement> statements = module.getMain().getStatements();
		optimiseStatementBlock(model, module, statements, context);
	}

	public void optimiseStatementBlock(IModel model, IEolModule module, List<Statement> statements,
			EolStaticAnalysisContext context) {
		for (Statement statement : statements) {
			if (statement instanceof ForStatement) {
				optimiseAST(model, Arrays.asList(statement.getChildren().get(1)), context);
				List<Statement> childStatements = ((ForStatement) statement).getBodyStatementBlock().getStatements();
				optimiseStatementBlock(model, module, childStatements, context);
			}

			else if (statement instanceof IfStatement) {
				StatementBlock thenBlock = ((IfStatement) statement).getThenStatementBlock();
				if (thenBlock != null) {
					List<Statement> thenStatements = thenBlock.getStatements();
					optimiseStatementBlock(model, module, thenStatements, context);
				}
				StatementBlock elseBlock = ((IfStatement) statement).getElseStatementBlock();
				if (elseBlock != null) {
					List<Statement> elseStatements = ((IfStatement) statement).getElseStatementBlock().getStatements();
					optimiseStatementBlock(model, module, elseStatements, context);
				}
			}

			else if (statement instanceof AssignmentStatement) {
				List<ModuleElement> targetAsts = Arrays.asList(statement.getChildren().get(0));
				optimiseAST(model, targetAsts, context);

				List<ModuleElement> valueAsts = Arrays.asList(statement.getChildren().get(1));
				optimiseAST(model, valueAsts, context);
			} else {
				List<ModuleElement> asts = statement.getChildren();
				optimiseAST(model, asts, context);
			}
		}
	}

	public void optimiseAST(IModel model, List<ModuleElement> asts, EolStaticAnalysisContext context) {
		isOptimisable = true;
		injectPrintln = false;
		printParameter = "";
		viatraMethodName = "";
		firstOrderMethodName = "";

		for (ModuleElement ast : asts) {
			NameExpression target = new NameExpression(model.getName());
			NameExpression operation = new NameExpression(IRunViatra.METHOD_NAME);
			RunViatraCallParameters methodCallParameters = translateToViatraPattern(model, ast, context);

			if (methodCallParameters != null) {
				OperationCallExpression rewrittenQuery = new OperationCallExpression(target, operation,
						methodCallParameters.getPatterns(), methodCallParameters.getMainPattern(),
						methodCallParameters.getMethodName(), methodCallParameters.getNumberOfMatches(),
						methodCallParameters.getFirstPatternParameter(),
						methodCallParameters.getSecondPatternParameter(),
						methodCallParameters.getThirdPatternParameter());

				if (injectPrintln) {
					if (printParameter.equals("")) {
						rewrittenQuery = new OperationCallExpression(rewrittenQuery,
								new NameExpression(EolOperations.PRINTLN));
					} else {
						rewrittenQuery = new OperationCallExpression(rewrittenQuery,
								new NameExpression(EolOperations.PRINTLN), new StringLiteral(printParameter));
					}
				}

				if (ast.getParent() instanceof ExpressionStatement) {
					((ExpressionStatement) ast.getParent()).setExpression(rewrittenQuery);
				} else if (ast.getParent() instanceof AssignmentStatement) {
					((AssignmentStatement) ast.getParent()).setValueExpression(rewrittenQuery);
				} else if (ast.getParent() instanceof ForStatement) {
					((ForStatement) ast.getParent()).setIteratedExpression(rewrittenQuery);
				} else if (ast.getParent() instanceof ExecutableBlock<?>) {
					((ExecutableBlock<?>) ast.getParent()).setBody(rewrittenQuery);
				} else {
					((OperationCallExpression) ast.getParent()).setTargetExpression(rewrittenQuery);
				}
			}
		}

	}

	public RunViatraCallParameters translateToViatraPattern(IModel model, ModuleElement ast,
			EolStaticAnalysisContext context) {
		modelElementTypeName = "";
		conditions = new LinkedList<>();
		patternParameters = new LinkedList<>();

		if (ast instanceof OperationCallExpression
				&& !(((OperationCallExpression) ast).getTargetExpression() instanceof NameExpression)) {

			for (ModuleElement astChild : ast.getChildren()) {
				if (astChild instanceof OperationCallExpression) {
					astToPattern(model, (OperationCallExpression) astChild, context);
				}
				if (astChild instanceof PropertyCallExpression) {
					astToPattern(model, (PropertyCallExpression) astChild, context);
				}
				if (astChild instanceof FirstOrderOperationCallExpression) {
					astToPattern(model, (FirstOrderOperationCallExpression) astChild, context);
				}
			}
			astToPattern(model, (OperationCallExpression) ast, context);
		}

		if (ast instanceof PropertyCallExpression) {
			if (!(((PropertyCallExpression) ast).getTargetExpression() instanceof NameExpression)) {

				for (ModuleElement astChild : ast.getChildren()) {
					if (astChild instanceof OperationCallExpression) {
						astToPattern(model, (OperationCallExpression) astChild, context);
					}
					if (astChild instanceof PropertyCallExpression) {
						astToPattern(model, (PropertyCallExpression) astChild, context);
					}
				}
			}
			astToPattern(model, (PropertyCallExpression) ast, context);
		}

		if (ast instanceof FirstOrderOperationCallExpression) {
			if (!(((FirstOrderOperationCallExpression) ast).getTargetExpression() instanceof NameExpression)) {

				for (ModuleElement astChild : ast.getChildren()) {
					if (astChild instanceof OperationCallExpression) {
						astToPattern(model, (OperationCallExpression) astChild, context);
					}
					if (astChild instanceof PropertyCallExpression) {
						astToPattern(model, (PropertyCallExpression) astChild, context);
					}
					if (astChild instanceof FirstOrderOperationCallExpression) {
						astToPattern(model, (FirstOrderOperationCallExpression) astChild, context);
					}
				}
			}
			astToPattern(model, (FirstOrderOperationCallExpression) ast, context);
		}

		if (isOptimisable && !modelElementTypeName.isBlank() && !conditions.isEmpty()) {
			StringBuilder patternBuilder = new StringBuilder();
			String header = String.format("import \"%s\"\n", metamodelNsuri);
			patternBuilder.append(header);

			patternNameSequenceNumber++;
			String patternName = String.format("%s%d", model.getName(), patternNameSequenceNumber);
			String modelElementName = modelElementTypeName.toLowerCase();
			String patternOpening = generatePatternHeader(patternName, modelElementName, modelElementTypeName);
			patternBuilder.append(patternOpening);

			if (FirstOrderOperations.REJECT.equals(firstOrderMethodName)
					|| FirstOrderOperations.FOR_ALL.equals(firstOrderMethodName)) {
				String internalPatternName = String.format("%sinternal", patternName);

				// negated pattern call to internal pattern
				String callParameters = modelElementName;
				if (!patternParameters.isEmpty()) {
					String patternParameterNames = patternParameters.stream().map(PatternParameter::getName)
							.collect(Collectors.joining(", "));
					callParameters = String.join(", ", modelElementName, patternParameterNames);
				}

				String negFindInternalMatches = String.format("neg find %s(%s);\n", internalPatternName,
						callParameters);
				patternBuilder.append(negFindInternalMatches);
				patternBuilder.append("}\n");

				// helper internal pattern with positive conditions
				String internalPatternOpening = generatePatternHeader(internalPatternName, modelElementName,
						modelElementTypeName);
				patternBuilder.append(internalPatternOpening);
			}

			String patternBody = generatePatternBodyWithConditions(modelElementName);
			patternBuilder.append(patternBody);

			String patternClosing = "}";
			patternBuilder.append(patternClosing);

			String pattern = patternBuilder.toString();
			List<Expression> parameterExpressions = patternParameters.stream().map(PatternParameter::getValue)
					.collect(Collectors.toList());
			return new RunViatraCallParameters(pattern, patternName, viatraMethodName, expectedNumberOfMatches,
					parameterExpressions);
		}

		return null;
	}

	private String generatePatternBodyWithConditions(String modelElementName) {
		StringBuilder patternBuilder = new StringBuilder();
		for (int i = 0; i < conditions.size(); i++) {
			EolCondition condition = conditions.get(i);
			String conditionName = condition.getName();
			String operator = condition.getOperator();
			Object parameter = condition.getValue();

			String viatraCondition = "";
			String conditionVariable = String.format("%s%d", conditionName, i);
			String checkExpressionBody = String.format("%s%s%s", conditionVariable, operator, parameter);
			switch (operator) {
			case "==":
			case "=":
				viatraCondition = String.format("%s.%s(%s,%s);\n", modelElementTypeName, conditionName,
						modelElementName, parameter);
				break;
			case "<>":
				viatraCondition = String.format("neg %s.%s(%s,%s);\n", modelElementTypeName, conditionName,
						modelElementName, parameter);
				break;
			default:
				viatraCondition = String.format("%s.%s(%s,%s); check(%s);\n", modelElementTypeName, conditionName,
						modelElementName, conditionVariable, checkExpressionBody);
			}

			patternBuilder.append(viatraCondition);
		}

		return patternBuilder.toString();
	}

	private String generatePatternHeader(String patternName, String modelElementName, String modelElementTypeName) {
		if (patternParameters.isEmpty()) {
			return String.format("pattern %s (%s: %s){\n", patternName, modelElementName, modelElementTypeName);
		} else {
			StringBuilder patternHeaderBuilder = new StringBuilder();
			String headerPrefix = String.format("pattern %s (%s: %s", patternName, modelElementName,
					modelElementTypeName);
			patternHeaderBuilder.append(headerPrefix);

			patternParameters.stream().forEach(parameter -> {
				patternHeaderBuilder.append(", ");

				String name = parameter.getName();
				String type = parameter.getType();

				String parameterDeclaration;
				if (type == null) {
					parameterDeclaration = String.format("in %s", name);
				} else {
					parameterDeclaration = String.format("in %s: %s", name, type);
				}

				patternHeaderBuilder.append(parameterDeclaration);
			});

			String headerClosing = "){\n";
			patternHeaderBuilder.append(headerClosing);
			return patternHeaderBuilder.toString();
		}
	}

	public void astToPattern(IModel model, OperationCallExpression ast, EolStaticAnalysisContext context) {
		if (!(ast.getTargetExpression() instanceof NameExpression) || (ast.getChildren() != null)) {
			String operationName = ast.getName();
			// ast.getChildren had already been transformed
			if (!EolOperations.PRINTLN.equals(operationName) && !EolOperations.SIZE.equals(operationName)) {
				for (ModuleElement astChild : ast.getChildren()) {
					if (astChild instanceof OperationCallExpression) {
						astToPattern(model, (OperationCallExpression) astChild, context);
					}
					if (astChild instanceof PropertyCallExpression) {
						astToPattern(model, (PropertyCallExpression) astChild, context);
					}
					if (astChild instanceof FirstOrderOperationCallExpression) {
						astToPattern(model, (FirstOrderOperationCallExpression) astChild, context);
					}
				}
			}

			if (EolOperations.PRINTLN.equals(operationName)) {
				injectPrintln = true;
				List<Expression> parameterExpressions = ast.getParameterExpressions();
				if (!(parameterExpressions.isEmpty())) {
					printParameter = ((StringLiteral) parameterExpressions.get(0)).getValue();
				}
			}
			if (EolOperations.SIZE.equals(operationName)) {
				viatraMethodName = ViatraOperations.COUNT_MATCHES;
			}
		}
	}

	public void astToPattern(IModel model, FirstOrderOperationCallExpression ast, EolStaticAnalysisContext context) {
		if (!(ast.getTargetExpression() instanceof NameExpression) || (ast.getChildren() != null)) {
			for (ModuleElement astChild : ast.getChildren()) {
				if (astChild instanceof OperationCallExpression)
					astToPattern(model, (OperationCallExpression) astChild, context);

				if (astChild instanceof PropertyCallExpression)
					astToPattern(model, (PropertyCallExpression) astChild, context);
			}

			if (FirstOrderOperations.isOptimisableOperation(ast.getName())) {
				Expression parameterExpression = ast.getExpressions().get(0);
				if (parameterExpression instanceof OperatorExpression) {
					OperatorExpression operatorExpression = (OperatorExpression) parameterExpression;
					collectConditionsInExpression(operatorExpression);
				}
				firstOrderMethodName = ast.getName();
				switch (firstOrderMethodName) {
				case FirstOrderOperations.SELECT:
				case FirstOrderOperations.REJECT:
					viatraMethodName = ViatraOperations.ALL_MATCHES;
					break;
				case FirstOrderOperations.SELECT_ONE:
					viatraMethodName = ViatraOperations.ONE_ARBITRARY_MATCH;
					break;
				case FirstOrderOperations.EXISTS:
					viatraMethodName = ViatraOperations.HAS_MATCH;
					break;
				case FirstOrderOperations.NONE:
					viatraMethodName = ViatraOperations.COUNT_MATCHES_EQUALS;
					expectedNumberOfMatches = 0;
					break;
				case FirstOrderOperations.ONE:
					viatraMethodName = ViatraOperations.COUNT_MATCHES_EQUALS;
					expectedNumberOfMatches = 1;
					break;
				case FirstOrderOperations.N_MATCH:
					viatraMethodName = ViatraOperations.COUNT_MATCHES_EQUALS;
					expectedNumberOfMatches = ((IntegerLiteral) ast.getExpressions().get(1)).getValue().intValue();
					firstOrderMethodName = String.format("%s-%d", firstOrderMethodName, expectedNumberOfMatches);
					break;
				case FirstOrderOperations.AT_LEAST_N_MATCH:
					viatraMethodName = ViatraOperations.COUNT_MATCHES_AT_LEAST;
					expectedNumberOfMatches = ((IntegerLiteral) ast.getExpressions().get(1)).getValue().intValue();
					firstOrderMethodName = String.format("%s-%d", firstOrderMethodName, expectedNumberOfMatches);
					break;
				case FirstOrderOperations.AT_MOST_N_MATCH:
					viatraMethodName = ViatraOperations.COUNT_MATCHES_AT_MOST;
					expectedNumberOfMatches = ((IntegerLiteral) ast.getExpressions().get(1)).getValue().intValue();
					firstOrderMethodName = String.format("%s-%d", firstOrderMethodName, expectedNumberOfMatches);
					break;
				case FirstOrderOperations.COUNT:
					viatraMethodName = ViatraOperations.COUNT_MATCHES;
					break;
				case FirstOrderOperations.FOR_ALL:
					viatraMethodName = ViatraOperations.HAS_MATCH_IS_FALSE;
					break;
				}
			}
		}
	}

	private void collectConditionsInExpression(Expression expression) {
		if (expression instanceof AndOperatorExpression) {
			AndOperatorExpression andOperator = (AndOperatorExpression) expression;
			andOperator.getOperands().forEach(operator -> collectConditionsInExpression(operator));
		} else if (expression instanceof OrOperatorExpression) {
			throw new IllegalArgumentException("Or operator is not supported.");
		} else if (expression instanceof NegativeOperatorExpression) {
			throw new IllegalArgumentException("Negation operator is not supported.");
		} else if (expression instanceof OperatorExpression) {
			OperatorExpression operatorExpression = (OperatorExpression) expression;
			Expression firstOperand = operatorExpression.getFirstOperand();

			if (firstOperand instanceof PropertyCallExpression) {
				String conditionName = ((PropertyCallExpression) operatorExpression.getFirstOperand()).getName();
				String conditionOperator = operatorExpression.getOperator();
				Object conditionValue = null;

				Expression secondOperand = operatorExpression.getSecondOperand();
				if (secondOperand instanceof LiteralExpression<?>) {
					Object value = ((LiteralExpression<?>) secondOperand).getValue();
					if (value instanceof String) {
						conditionValue = String.format("\"%s\"", value);
					} else {
						conditionValue = value;
					}
				} else if (secondOperand instanceof FeatureCallExpression || secondOperand instanceof NameExpression) {
					String name = ExpressionUtil.getName(secondOperand);
					int index = conditions.size();
					String uniqueParameterName = String.format("%s%d", name, index);
					conditionValue = uniqueParameterName;

					String typeName = ExpressionUtil.getResolvedTypeName(secondOperand, staticAnalyser);
					PatternParameter parameter = new PatternParameter(uniqueParameterName, typeName, secondOperand);
					patternParameters.add(parameter);
				} else {
					throw new IllegalArgumentException("Unsupported operand: " + secondOperand);
				}

				EolCondition condition = new EolCondition(conditionName, conditionOperator, conditionValue);
				conditions.add(condition);
			} else {
				throw new IllegalArgumentException("Unsupported operand: " + firstOperand);
			}
		} else {
			throw new IllegalArgumentException("Unsupported expression: " + expression);
		}
	}

	public void astToPattern(IModel model, PropertyCallExpression ast, EolStaticAnalysisContext context) {
		if (!(ast.getTargetExpression() instanceof NameExpression) || (ast.getChildren() != null)) {
			for (ModuleElement astChild : ast.getChildren()) {
				if (astChild instanceof OperationCallExpression) {
					astToPattern(model, (OperationCallExpression) astChild, context);
				}
				if (astChild instanceof PropertyCallExpression) {
					astToPattern(model, (PropertyCallExpression) astChild, context);
				}
			}
			String propertyCallName = ast.getName();
			if ("all".equals(propertyCallName) || "allInstances".equals(propertyCallName)) {
				EolType resolvedType = staticAnalyser.getResolvedType(ast.getTargetExpression());
				if (resolvedType instanceof EolModelElementType) {

					IModel modelOfAllInstances = null;
					try {
						modelOfAllInstances = getModel(((EolModelElementType) resolvedType));
					} catch (EolModelElementTypeNotFoundException e) {
					}

					if (modelOfAllInstances == model) {
						modelElementTypeName = ((EolModelElementType) resolvedType).getTypeName();
					} else {
						isOptimisable = false;
					}
				}
			}
		}
	}
	
	public IModel getModel(EolModelElementType modelElementType) throws EolModelElementTypeNotFoundException {
		for (ModelDeclaration modelDeclaration : staticAnalyser.getContext().getModelDeclarations()) {
			if (modelElementType.getModelName().isEmpty() || modelDeclaration.getNameExpression().getName().equals(modelElementType.getModelName())) {
				return modelDeclaration.getModel();
			}
		}
		return null;
	}

}
