package org.eclipse.epsilon.evl.viatra.tests.standalone;

import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.staticanalyser.IModelFactory;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;

public class SubViatraModelFactory implements IModelFactory {

	@Override
	public IModel createModel(String driver) {
		ViatraEmfModel model = new ViatraEmfModel();
		return model;
	}

}
