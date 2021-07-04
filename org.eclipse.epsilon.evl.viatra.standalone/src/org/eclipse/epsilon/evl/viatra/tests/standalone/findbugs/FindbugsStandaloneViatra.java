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
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;
import org.eclipse.epsilon.eol.viatra.engine.LocalSearchEngineOptions;
import org.eclipse.epsilon.evl.launch.EvlRunConfiguration;
import org.eclipse.epsilon.evl.viatra.tests.standalone.EvlPreExecuteConfiguration;
import org.eclipse.viatra.query.patternlanguage.emf.EMFPatternLanguageStandaloneSetup;

public class FindbugsStandaloneViatra {
	
	// set in org.eclipse.epsilon.common -> MANIFEST.MF -> org.antlr.runtime: minimum version: 3.2.0, maximum version: 3.2.1

	public static void main(String[] args) throws Exception {
		EMFPatternLanguageStandaloneSetup.doSetup();

		Path root = Paths.get(FindbugsStandaloneViatra.class.getResource("").toURI()),
				modelsRoot = root.getParent().resolve("findbugs");

		StringProperties modelProperties = new StringProperties();
		modelProperties.setProperty(ViatraEmfModel.PROPERTY_NAME, "Java");
		String metamodelUri = modelsRoot.resolve("java-without-eOpposite.ecore").toAbsolutePath().toUri().toString();
		modelProperties.setProperty(ViatraEmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, metamodelUri);

		String modelFile = args[0];
		for (int i = 0; i < 10; i++) {
			String modelUri = modelsRoot.resolve(modelFile).toAbsolutePath().toUri().toString();
			modelProperties.setProperty(ViatraEmfModel.PROPERTY_MODEL_URI, modelUri);

			System.out.println("Model " + modelFile + ", iteration " + i);
			LocalSearchEngineOptions options = new LocalSearchEngineOptions.Builder().enableLocalSearch(false)
					.useBaseIndex(false).build();

			ViatraEmfModel model = new ViatraEmfModel(options);
			EvlRunConfiguration runConfig = EvlRunConfiguration.Builder()
					.withScript(root.resolve("findbugs.evl"))
					.withModel(model, modelProperties)
					.withProfiling()
					.withOutputFile("src/org/eclipse/epsilon/evl/viatra/tests/standalone/findbugs/results-viatra.txt")
					.build();

			EvlPreExecuteConfiguration sm = new EvlPreExecuteConfiguration(runConfig);
			sm.run();
			model.close();
		}

	}

}
