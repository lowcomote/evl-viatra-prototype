package org.eclipse.epsilon.eol.viatra.rewriting.tests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.parse.EolUnparser;
import org.eclipse.epsilon.eol.viatra.tests.EolToViatraTests;
import org.junit.Assert;
import org.junit.Test;

public class EolToViatraRewritingTests extends EolToViatraTests {

	private static final String EOL_SCRIPT_FOLDER = "src/org/eclipse/epsilon/eol/viatra/rewriting/tests/";

	@Test
	public void testViatraSelectOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraSelectOperation.eol", "testViatraSelectOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatraSelectOneOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraSelectOneOperation.eol",
				"testViatraSelectOneOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatraRejectOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraRejectOperation.eol", "testViatraRejectOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatranMatchOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatranMatchOperation.eol", "testViatranMatchOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatraExistsOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraExistsOperation.eol", "testViatraExistsOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatraAtLeastNMatchOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraAtLeastNMatchOperation.eol",
				"testViatraAtLeastNMatchOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatraAtMostNMatchOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraAtMostNMatchOperation.eol",
				"testViatraAtMostNMatchOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatraCountOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraCountOperation.eol", "testViatraCountOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatraNoneOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraNoneOperation.eol", "testViatraNoneOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatraOneOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraOneOperation.eol", "testViatraOneOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatraForAllOperationRewriting() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraForAllOperation.eol", "testViatraForAllOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	@Test
	public void testViatraSelectOperationExecution() throws Exception {
		List<String> actualAndExpected = new ArrayList<>();
		actualAndExpected = prepareRewritingTestCase("testViatraSelectOperation.eol", "testViatraSelectOperation.txt");
		Assert.assertEquals("Failed", actualAndExpected.get(1), actualAndExpected.get(0));
	}

	private List<String> prepareRewritingTestCase(String eolFileName, String rewritedFileName) throws Exception {
		EolModule module = rewriteViatraEmfModule(EOL_SCRIPT_FOLDER, eolFileName);
		String actual = new EolUnparser().unparse(module);
		String expected = Files.readString(Path.of(EOL_SCRIPT_FOLDER + rewritedFileName));
		return Arrays.asList(removeWhitespace(actual), removeWhitespace(expected));
	}

	private static String removeWhitespace(String str) {
		return str.replaceAll("\\s+", "");
	}

}
