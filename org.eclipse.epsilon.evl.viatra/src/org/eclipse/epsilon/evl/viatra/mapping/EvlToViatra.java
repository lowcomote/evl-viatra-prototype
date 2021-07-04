package org.eclipse.epsilon.evl.viatra.mapping;

import java.util.List;

import org.eclipse.epsilon.eol.dom.IExecutableModuleElement;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.dom.Statement;
import org.eclipse.epsilon.eol.dom.StatementBlock;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalysisContext;
import org.eclipse.epsilon.eol.viatra.mapping.EolToViatra;
import org.eclipse.epsilon.erl.dom.Post;
import org.eclipse.epsilon.erl.dom.Pre;
import org.eclipse.epsilon.evl.IEvlModule;
import org.eclipse.epsilon.evl.dom.Constraint;
import org.eclipse.epsilon.evl.staticanalyser.EvlStaticAnalyser;

public class EvlToViatra extends EolToViatra {

	public void map(IModel model, IEvlModule module, EolStaticAnalysisContext context, EvlStaticAnalyser staticAnalyser) {
		this.staticAnalyser = staticAnalyser;
		for (ModelDeclaration declaration : module.getModelDelcarations()) {
			if (declaration.getModel() == model) {
				metamodelNsuri = declaration.getModelDeclarationParameter("nsuri").getValue();
			}
		}
		for (Pre pre : module.getPre()) {
			List<Statement> statements = pre.getBody().getStatements();
			optimiseStatementBlock(model, module, statements, context);
		}

		for (Constraint constraint : module.getConstraints()) {
			if (constraint.getGuardBlock() != null) {
				IExecutableModuleElement guardBody = constraint.getGuardBlock().getBody();

				if (guardBody instanceof StatementBlock) {
					List<Statement> guardStatements = ((StatementBlock) guardBody).getStatements();
					optimiseStatementBlock(model, module, guardStatements, context);
				} else {
					optimiseAST(model, guardBody.getParent().getChildren(), context);
				}
			}
			IExecutableModuleElement body = constraint.getCheckBlock().getBody();
			if (body instanceof StatementBlock) {
				List<Statement> bodyStatements = ((StatementBlock) body).getStatements();
				optimiseStatementBlock(model, module, bodyStatements, context);
			} else {
				optimiseAST(model, body.getParent().getChildren(), context);
			}
		}

		for (Post post : module.getPost()) {
			List<Statement> statements = post.getBody().getStatements();
			optimiseStatementBlock(model, module, statements, context);
		}

		for (Operation operation : module.getDeclaredOperations()) {
			List<Statement> statements = operation.getBody().getStatements();
			optimiseStatementBlock(model, module, statements, context);
		}

	}

}
