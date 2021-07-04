/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.evl.viatra.tests.standalone.findbugs;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;
import org.eclipse.epsilon.evl.launch.EvlRunConfiguration;

public class FindbugsStandaloneEmf {

	public static void main(String[] args) throws Exception {
		Path root = Paths.get(FindbugsStandaloneEmf.class.getResource("").toURI()),
				modelsRoot = root.getParent().resolve("findbugs");

		StringProperties modelProperties = new StringProperties();
		modelProperties.setProperty(EmfModel.PROPERTY_NAME, "Java");
		String metamodelUri = modelsRoot.resolve("java-without-eOpposite.ecore").toAbsolutePath().toUri().toString();
		modelProperties.setProperty(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, metamodelUri);

		String modelFile = args[0];
		for (int i = 0; i < 10; i++) {
			System.out.println("Model " + modelFile + ", iteration " + i);
			
			String modelUri = modelsRoot.resolve(modelFile).toAbsolutePath().toUri().toString();
			modelProperties.setProperty(ViatraEmfModel.PROPERTY_MODEL_URI, modelUri);

			EmfModel model = new EmfModel();
			EvlRunConfiguration runConfig = EvlRunConfiguration.Builder()
					.withScript(root.resolve("findbugs-emf.evl"))
					.withModel(model, modelProperties)
					.withProfiling()
					.withParallelism(-1)
					.withOutputFile("src/org/eclipse/epsilon/evl/viatra/tests/standalone/findbugs/results-emf.txt")
					.build();
			runConfig.run();
			model.close();
		}

	}

}
