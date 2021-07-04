package org.eclipse.epsilon.evl.viatra.tests.standalone;

import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;
import org.eclipse.epsilon.evl.IEvlModule;
import org.eclipse.epsilon.evl.launch.EvlRunConfiguration;
import org.eclipse.epsilon.evl.staticanalyser.EvlStaticAnalyser;
import org.eclipse.epsilon.evl.viatra.mapping.EvlRewritingHandler;

public class EvlPreExecuteConfiguration extends EvlRunConfiguration {
	IEvlModule module;

	public EvlPreExecuteConfiguration(EvlRunConfiguration other) {
		super(other);
		module = super.getModule();
	}

	@Override
	public void preExecute() throws Exception {
		super.preExecute();

		module.getContext().setModule(module);
		EvlStaticAnalyser staticAnlayser = new EvlStaticAnalyser();
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			if (modelDeclaration.getDriverNameExpression().getName().equals(ViatraEmfModel.DRIVER_NAME)) {
				staticAnlayser.getContext().setModelFactory(new SubViatraModelFactory());
			}
		}
		staticAnlayser.validate(module);

		new EvlRewritingHandler().invokeRewriters(module, staticAnlayser);

		// System.err.println(new EvlUnparser().unparse((EvlModule) module));

	}
}
