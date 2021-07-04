package org.eclipse.epsilon.eol.viatra.runviatra.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.types.EolSequence;
import org.eclipse.epsilon.eol.viatra.ViatraEmfModel;
import org.eclipse.epsilon.eol.viatra.tests.EolToViatraTests;
import org.junit.Test;

public class EolToRunViatraIntegrationTests extends EolToViatraTests {

	private static final String EOL_SCRIPT_FOLDER = "src/org/eclipse/epsilon/eol/viatra/runviatra/tests/";

	@Test
	public void testSelectOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraSelectOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);

		int is2010 = (Integer) result.get(0);
		int earlierThan2010 = (Integer) result.get(1);
		int laterThan2010 = (Integer) result.get(2);
		int not2010 = (Integer) result.get(3);

		assertEquals(605, is2010);
		assertEquals(7019, earlierThan2010);
		assertEquals(2776, laterThan2010);
		assertEquals(9795, not2010);
		assertEquals(not2010, earlierThan2010 + laterThan2010);
	}

	@Test
	public void testSelectOneOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraSelectOneOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);
		assertNotNull(result.get(0));
	}

	@Test
	public void testCountOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraCountOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);

		int is2010 = (Integer) result.get(0);
		int earlierThan2010 = (Integer) result.get(1);
		int laterThan2010 = (Integer) result.get(2);
		int not2010 = (Integer) result.get(3);

		assertEquals(605, is2010);
		assertEquals(not2010, earlierThan2010 + laterThan2010);
	}

	@Test
	public void testExistsOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraExistsOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);

		boolean has2010 = (Boolean) result.get(0);
		boolean earlierThan2010 = (Boolean) result.get(1);
		boolean laterThan2010 = (Boolean) result.get(2);
		boolean not2010 = (Boolean) result.get(3);

		assertTrue(has2010);
		assertTrue(earlierThan2010);
		assertTrue(laterThan2010);
		assertTrue(not2010);
	}

	@Test
	public void testRejectOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraRejectOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);

		int notIs2010 = (Integer) result.get(0);
		int notEarlierThan2010 = (Integer) result.get(1);
		int notLaterThan2010 = (Integer) result.get(2);
		int is2010 = (Integer) result.get(3);
		int notASpecificMovie = (Integer) result.get(4);

		int sumOfMovies = 605 + 9795;
		assertEquals(605, is2010);
		assertEquals(sumOfMovies - 2776, notLaterThan2010);
		assertEquals(sumOfMovies - 7019, notEarlierThan2010);
		assertEquals(9795, notIs2010);
		assertEquals(sumOfMovies - 1, notASpecificMovie);
	}

	@Test
	public void testOneOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraOneOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);

		boolean shouldBeTrue = (Boolean) result.get(0);
		boolean shouldBeFalse = (Boolean) result.get(1);

		assertTrue(shouldBeTrue);
		assertFalse(shouldBeFalse);
	}

	@Test
	public void testNoneOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraNoneOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);

		boolean shouldBeFalse = (Boolean) result.get(0);
		boolean shouldBeTrue = (Boolean) result.get(1);

		assertTrue(shouldBeTrue);
		assertFalse(shouldBeFalse);
	}

	@Test
	public void testNMatchOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraNMatchOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);

		boolean shouldBeTrue1 = (Boolean) result.get(0);
		boolean shouldBeTrue2 = (Boolean) result.get(1);
		boolean shouldBeTrue3 = (Boolean) result.get(2);
		boolean shouldBeTrue4 = (Boolean) result.get(3);

		assertTrue(shouldBeTrue1);
		assertTrue(shouldBeTrue2);
		assertTrue(shouldBeTrue3);
		assertTrue(shouldBeTrue4);
	}

	@Test
	public void testAtLeastNMatchOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraAtLeastNMatchOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);

		boolean shouldBeTrue1 = (Boolean) result.get(0);
		boolean shouldBeTrue2 = (Boolean) result.get(1);
		boolean shouldBeFalse3 = (Boolean) result.get(2);
		boolean shouldBeTrue4 = (Boolean) result.get(3);
		boolean shouldBeFalse5 = (Boolean) result.get(4);

		assertTrue(shouldBeTrue1);
		assertTrue(shouldBeTrue2);
		assertFalse(shouldBeFalse3);
		assertTrue(shouldBeTrue4);
		assertFalse(shouldBeFalse5);
	}

	@Test
	public void testAtMostNMatchOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraAtMostNMatchOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);

		boolean shouldBeFalse1 = (Boolean) result.get(0);
		boolean shouldBeTrue2 = (Boolean) result.get(1);
		boolean shouldBeTrue3 = (Boolean) result.get(2);
		boolean shouldBeFalse2 = (Boolean) result.get(3);
		boolean shouldBeTrue4 = (Boolean) result.get(4);

		assertFalse(shouldBeFalse1);
		assertTrue(shouldBeTrue2);
		assertTrue(shouldBeTrue3);
		assertFalse(shouldBeFalse2);
		assertTrue(shouldBeTrue4);
	}

	@Test
	public void testForAllOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraForAllOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);

		boolean shouldBeFalse1 = (Boolean) result.get(0);
		boolean shouldBeTrue2 = (Boolean) result.get(1);
		boolean shouldBeTrue3 = (Boolean) result.get(2);

		assertFalse(shouldBeFalse1);
		assertTrue(shouldBeTrue2);
		assertTrue(shouldBeTrue3);
	}
	
	@Test
	public void testParameterizedOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraParameterizedOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);
		
		int first = (Integer) result.get(0);
		int second = (Integer) result.get(1);
		int third = (Integer) result.get(2);
		int fourth = (Integer) result.get(3);
		
		assertEquals(129, first);
		assertEquals(58, second);
		assertEquals(8, third);
		assertEquals(0, fourth);
	}
	
	@Test
	public void testActorMovieJoinOperation() throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, "testViatraActorMovieJoinOperation.eol");
		ViatraEmfModel model = createViatraEmfModel();
		EolSequence<?> result = executeModule(module, model);
		
		int numberOfActors = (Integer) result.get(0);
		boolean actorWasInMovie = (Boolean) result.get(1);
		
		assertEquals(20, numberOfActors);
		assertTrue(actorWasInMovie);
	}

}
