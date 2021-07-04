package org.eclipse.epsilon.evl.viatra.equivalence.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.evl.viatra.tests.EvlToViatraTests;
import org.junit.Test;

public class EvlToViatraEquivalenceTests extends EvlToViatraTests {

	private static final String EVL_SCRIPT_FOLDER = "src/org/eclipse/epsilon/evl/viatra/equivalence/tests/";

	@Test
	public void testExistsOperation() throws Exception {
		List<Object> viatraResults = executeViatraEmfModel(EVL_SCRIPT_FOLDER, "testViatraEmfExistsOperation.evl");
		List<Object> emfResults = executeEmfModel(EVL_SCRIPT_FOLDER, "testEvlEmfExistsOperation.evl");
		
		boolean resultsAreEqual = CollectionUtils.isEqualCollection(viatraResults, emfResults);
		assertTrue(resultsAreEqual);
	}

	@Override
	protected void initializeModel(EmfModel model) throws EolModelLoadingException {
		model.setModelFile("model/imdb-0.1.xmi");
		model.setMetamodelFile("model/movies.ecore");
		model.setName("imdb");
		model.load();
	}

}
