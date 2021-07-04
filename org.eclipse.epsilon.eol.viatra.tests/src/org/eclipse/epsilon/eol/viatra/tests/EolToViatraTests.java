package org.eclipse.epsilon.eol.viatra.tests;

import java.io.File;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.staticanalyser.EolStaticAnalyser;
import org.eclipse.epsilon.eol.types.EolSequence;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;
import org.eclipse.epsilon.eol.viatra.mapping.EolRewritingHandler;

public abstract class EolToViatraTests {

	protected EolModule rewriteViatraEmfModule(String folder, String eolFileName) throws Exception {
		EolModule module = loadModule(folder, eolFileName);

		module.getContext().setModule(module);
		EolStaticAnalyser staticAnlayser = new EolStaticAnalyser();
		
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			if (modelDeclaration.getDriverNameExpression().getName().equals(ViatraEmfModel.DRIVER_NAME)) {
				staticAnlayser.getContext().setModelFactory(new SubViatraModelFactory());
			}
		}
		
		staticAnlayser.validate(module);

		new EolRewritingHandler().invokeRewriters(module, staticAnlayser);
		module.getContext().setModule(module);
		return module;
	}
	
	protected EolModule loadModule(String folder, String eolFileName) throws Exception {
		EolModule module = new EolModule();
		module.parse(new File(folder + eolFileName));
		module.getContext().setModule(module);
		return module;
	}

	protected ViatraEmfModel createViatraEmfModel() throws EolModelLoadingException {
		ViatraEmfModel viatraEmfModel = new ViatraEmfModel();
		initializeModel(viatraEmfModel);
		return viatraEmfModel;
	}

	protected EmfModel createEmfModel() throws EolModelLoadingException {
		EmfModel model = new EmfModel();
		initializeModel(model);
		return model;
	}

	private void initializeModel(EmfModel model) throws EolModelLoadingException {
		model.setModelFile("model/imdb-0.1.xmi");
		model.setMetamodelFile("model/movies.ecore");
		model.setName("imdb");
		model.load();
	}

	protected EolSequence<?> executeModule(EolModule module, EmfModel model) throws EolRuntimeException {
		module.getContext().getModelRepository().addModel(model);
		Object result = module.execute();
		return (EolSequence<?>) result;
	}

}
