package org.eclipse.epsilon.evl.viatra.mapping;

import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalysisContext;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;
import org.eclipse.epsilon.evl.IEvlModule;
import org.eclipse.epsilon.evl.staticanalyser.EvlStaticAnalyser;

public class EvlRewritingHandler {

	public void invokeRewriters(IEvlModule module, EvlStaticAnalyser staticAnalyser) {
		EolStaticAnalysisContext context = staticAnalyser.getContext();
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			IModel model = modelDeclaration.getModel();
			if (modelDeclaration.getDriverNameExpression().getName().equals(ViatraEmfModel.DRIVER_NAME)) {
				new EvlToViatra().map(model, module, context, staticAnalyser);
			}
		}
	}
}
