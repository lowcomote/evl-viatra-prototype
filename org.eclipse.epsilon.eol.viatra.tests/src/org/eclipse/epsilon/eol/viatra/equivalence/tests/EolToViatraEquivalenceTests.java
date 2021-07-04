package org.eclipse.epsilon.eol.viatra.equivalence.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.types.EolSequence;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;
import org.eclipse.epsilon.eol.viatra.tests.EolToViatraTests;
import org.junit.Test;

public class EolToViatraEquivalenceTests extends EolToViatraTests {

	private static final String VIATRA_EOL_SCRIPT_FOLDER = "src/org/eclipse/epsilon/eol/viatra/runviatra/tests/";
	private static final String EMF_EOL_SCRIPT_FOLDER = "src/org/eclipse/epsilon/eol/viatra/equivalence/tests/";

	@Test
	public void testSelectOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraSelectOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfSelectOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	@Test
	public void testSelectOneOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraSelectOneOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfSelectOneOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	@Test
	public void testCountOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraCountOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfCountOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	@Test
	public void testExistsOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraExistsOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfExistsOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	@Test
	public void testRejectOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraRejectOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfRejectOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	@Test
	public void testOneOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraOneOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfOneOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	@Test
	public void testNoneOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraNoneOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfNoneOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	@Test
	public void testNMatchOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraNMatchOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfNMatchOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	@Test
	public void testAtLeastNMatchOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraAtLeastNMatchOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfAtLeastNMatchOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	@Test
	public void testAtMostNMatchOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraAtMostNMatchOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfAtMostNMatchOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	@Test
	public void testForAllOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraForAllOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfForAllOperation.eol");
		assertEquals(emfResults, viatraResults);
	}
	
	@Test
	public void testParameterizedOperation() throws Exception {
		EolSequence<?> viatraResults = executeViatraEmfModel("testViatraParameterizedOperation.eol");
		EolSequence<?> emfResults = executeEmfModel("testEmfEolParameterizedOperation.eol");
		assertEquals(emfResults, viatraResults);
	}

	private EolSequence<?> executeViatraEmfModel(String eolScriptFile) throws Exception {
		EolModule module = rewriteViatraEmfModule(VIATRA_EOL_SCRIPT_FOLDER, eolScriptFile);
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);
		return result;
	}

	private EolSequence<?> executeEmfModel(String eolScriptFile) throws Exception {
		EolModule module = loadModule(EMF_EOL_SCRIPT_FOLDER, eolScriptFile);
		EmfModel model = createEmfModel();
		EolSequence<?> result = executeModule(module, model);
		return result;
	}

}
