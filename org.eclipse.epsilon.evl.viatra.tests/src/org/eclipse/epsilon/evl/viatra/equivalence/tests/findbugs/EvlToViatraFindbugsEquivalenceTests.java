package org.eclipse.epsilon.evl.viatra.equivalence.tests.findbugs;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.evl.viatra.tests.EvlToViatraTests;
import org.junit.Test;

public class EvlToViatraFindbugsEquivalenceTests extends EvlToViatraTests {

	private static final String EVL_SCRIPT_FOLDER = "src/org/eclipse/epsilon/evl/viatra/equivalence/tests/findbugs/";

	@Test
	public void testAllImportsAreUsed() throws Exception {
		List<Object> viatraResults = executeViatraEmfModel(EVL_SCRIPT_FOLDER, "testViatraAllImportsAreUsed.evl");
		List<Object> emfResults = executeEmfModel(EVL_SCRIPT_FOLDER, "testEvlEmfAllImportsAreUsed.evl");
		
		boolean resultsAreEqual = CollectionUtils.isEqualCollection(viatraResults, emfResults);
		assertTrue(resultsAreEqual);
	}

	@Test
	public void testExceptionIsUsed() throws Exception {
		List<Object> viatraResults = executeViatraEmfModel(EVL_SCRIPT_FOLDER, "testViatraExceptionIsUsed.evl");
		List<Object> emfResults = executeEmfModel(EVL_SCRIPT_FOLDER, "testEvlEmfExceptionIsUsed.evl");
		
		boolean resultsAreEqual = CollectionUtils.isEqualCollection(viatraResults, emfResults);
		assertTrue(resultsAreEqual);
	}

	@Test
	public void testVariableIsUsed() throws Exception {
		List<Object> viatraResults = executeViatraEmfModel(EVL_SCRIPT_FOLDER, "testViatraVariableIsUsed.evl");
		List<Object> emfResults = executeEmfModel(EVL_SCRIPT_FOLDER, "testEvlEmfVariableIsUsed.evl");
		
		boolean resultsAreEqual = CollectionUtils.isEqualCollection(viatraResults, emfResults);
		assertTrue(resultsAreEqual);
	}

	@Override
	protected void initializeModel(EmfModel model) throws EolModelLoadingException {
		model.setModelFile("model/findbugs.xmi");
		model.setMetamodelFile("model/java.ecore");
		model.setName("Java");
		model.load();
	}

}
