package org.eclipse.epsilon.evl.viatra.tests;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.dom.ModelDeclaration;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;
import org.eclipse.epsilon.evl.EvlModule;
import org.eclipse.epsilon.evl.execute.UnsatisfiedConstraint;
import org.eclipse.epsilon.evl.staticanalyser.EvlStaticAnalyser;
import org.eclipse.epsilon.evl.viatra.mapping.EvlRewritingHandler;

public abstract class EvlToViatraTests {
	
	protected abstract void initializeModel(EmfModel model) throws EolModelLoadingException;

	protected List<Object> executeViatraEmfModel(String evlScriptFolder, String evlScriptFile) throws Exception {
		EvlModule module = rewriteViatraEmfModule(evlScriptFolder, evlScriptFile);
		ViatraEmfModel model = createViatraEmfModel();
		Set<UnsatisfiedConstraint> unsatisfiedConstraints = executeModule(module, model);
		List<Object> erroneousElements = unsatisfiedConstraints.stream().map(UnsatisfiedConstraint::getInstance).collect(Collectors.toList());
		return erroneousElements;
	}

	protected List<Object> executeEmfModel(String evlScriptFolder, String evlScriptFile) throws Exception {
		EvlModule module = loadModule(evlScriptFolder, evlScriptFile);
		EmfModel model = createEmfModel();
		Set<UnsatisfiedConstraint> unsatisfiedConstraints = executeModule(module, model);
		List<Object> erroneousElements = unsatisfiedConstraints.stream().map(UnsatisfiedConstraint::getInstance).collect(Collectors.toList());
		return erroneousElements;
	}
	
	private EvlModule rewriteViatraEmfModule(String folder, String evlFileName) throws Exception {
		EvlModule module = loadModule(folder, evlFileName);

		module.getContext().setModule(module);
		EvlStaticAnalyser staticAnlayser = new EvlStaticAnalyser();
		for (ModelDeclaration modelDeclaration : module.getDeclaredModelDeclarations()) {
			if (modelDeclaration.getDriverNameExpression().getName().equals(ViatraEmfModel.DRIVER_NAME)) {
				staticAnlayser.getContext().setModelFactory(new SubViatraModelFactory());
			}
		}
		staticAnlayser.validate(module);

		new EvlRewritingHandler().invokeRewriters(module, staticAnlayser);
		module.getContext().setModule(module);
		return module;
	}
	
	private EvlModule loadModule(String folder, String eolFileName) throws Exception {
		EvlModule module = new EvlModule();
		module.parse(new File(folder + eolFileName));
		module.getContext().setModule(module);
		return module;
	}
	
	private Set<UnsatisfiedConstraint> executeModule(EvlModule module, EmfModel model) throws EolRuntimeException {
		module.getContext().getModelRepository().addModel(model);
		Set<UnsatisfiedConstraint> unsatisfiedConstraints = module.execute();
		return unsatisfiedConstraints;
	}
	
	private ViatraEmfModel createViatraEmfModel() throws EolModelLoadingException {
		ViatraEmfModel viatraEmfModel = new ViatraEmfModel();
		initializeModel(viatraEmfModel);
		return viatraEmfModel;
	}

	private EmfModel createEmfModel() throws EolModelLoadingException {
		EmfModel model = new EmfModel();
		initializeModel(model);
		return model;
	}
	
	

}
