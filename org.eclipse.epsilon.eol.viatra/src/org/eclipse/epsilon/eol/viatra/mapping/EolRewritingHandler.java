package org.eclipse.epsilon.eol.viatra.mapping;

import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalyser;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalysisContext;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;

public class EolRewritingHandler {

	public void invokeRewriters(IEolModule module, EolStaticAnalyser staticAnalyser) {
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			IModel model = modelDeclaration.getModel();
			if (modelDeclaration.getDriverNameExpression().getName().equals(ViatraEmfModel.DRIVER_NAME)) {
				EolStaticAnalysisContext context = staticAnalyser.getContext();
				new EolToViatra().map(model, module, context, staticAnalyser);
			}
		}
	}
}
