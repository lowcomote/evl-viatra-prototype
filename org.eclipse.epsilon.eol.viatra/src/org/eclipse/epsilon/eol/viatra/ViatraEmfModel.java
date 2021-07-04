package org.eclipse.epsilon.eol.viatra;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.viatra.engine.LocalSearchEngineOptions;
import org.eclipse.epsilon.eol.viatra.engine.ViatraEngineBridge;

public class ViatraEmfModel extends EmfModel implements IRunViatra, AutoCloseable {
	
	public static final String DRIVER_NAME = "ViatraEMF";

	private ViatraEngineBridge viatraEngine;
	private LocalSearchEngineOptions localSearchOptions;
	
	public ViatraEmfModel() {
		this(LocalSearchEngineOptions.DISABLED);
	}
	
	public ViatraEmfModel(LocalSearchEngineOptions localSearchOptions) {
		this.localSearchOptions = localSearchOptions;
	}

	@Override
	public void load() throws EolModelLoadingException {
		super.load();

		Resource resource = getResource();
		EObject firstObj = resource.getContents().get(0);
		EPackage rootPkg = getRootEPackage(firstObj);

		String rootNsUri = rootPkg.getNsURI();
		Registry ePkgRegistry = EPackage.Registry.INSTANCE;
		if (!ePkgRegistry.containsKey(rootNsUri)) {
			ePkgRegistry.put(rootNsUri, rootPkg);
		}
	}

	private EPackage getRootEPackage(EObject obj) {
		EPackage pkg = null;
		if (obj instanceof EPackage) {
			pkg = (EPackage) obj;
		} else {
			pkg = obj.eClass().getEPackage();
		}

		EPackage root = pkg.getESuperPackage();
		if (root == null) {
			return pkg;
		} else {
			return getRootEPackage(root);
		}
	}

	private void init() {
		if (viatraEngine == null) {
			Resource resource = getResource();
			viatraEngine = new ViatraEngineBridge(resource, localSearchOptions);
		}
	}

	@Override
	public Object runViatra(String patternDefinition, String patternName, String methodName,
			int expectedNumberOfMatches, Object firstPatternParameter, Object secondPatternParameter,
			Object thirdPatternParameter) {
		init();
		Object result = viatraEngine.runViatra(patternDefinition, patternName, methodName, expectedNumberOfMatches,
				firstPatternParameter, secondPatternParameter, thirdPatternParameter);
		return result;
	}
	
	@Override
	public void close() {
		super.close();
		viatraEngine.close();
	}

}
