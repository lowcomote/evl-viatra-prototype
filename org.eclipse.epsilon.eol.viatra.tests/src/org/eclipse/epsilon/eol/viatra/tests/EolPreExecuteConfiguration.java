package org.eclipse.epsilon.eol.viatra.tests;

import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.launch.EolRunConfiguration;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalyser;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;
import org.eclipse.epsilon.eol.viatra.mapping.EolRewritingHandler;

public class EolPreExecuteConfiguration extends EolRunConfiguration{
	IEolModule module;
	
	public EolPreExecuteConfiguration(EolRunConfiguration other) {
		super(other);
		module = super.getModule();
	}
	
	@Override
	protected void preExecute() throws Exception {
		super.preExecute();
		
		module.getContext().setModule(module);
			EolStaticAnalyser staticAnlayser = new EolStaticAnalyser();
			for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
				if (modelDeclaration.getDriverNameExpression().getName().equals(ViatraEmfModel.DRIVER_NAME)) 
					staticAnlayser.getContext().setModelFactory(new SubViatraModelFactory());
			}
			staticAnlayser.validate(module);
        
		new EolRewritingHandler().invokeRewriters(module, staticAnlayser);
		
		//System.err.println(new EolUnparser().unparse( (EolModule) module));
		
			
		
	}
}
