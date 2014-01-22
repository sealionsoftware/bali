package bali.compiler;

import bali.compiler.validation.ValidationException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * User: Richard
 * Date: 21/11/13
 */
public class NullabilityUnitTest {

	private static final String TEST_TARGETS_NAME = "bali/compiler/nullable";
	private static final File OUTPUT = new File("/../../../unittests");

	private static File failTestTargets;
	private static File passTestTargets;

	private BaliCompiler compiler = new BaliCompiler();

	@BeforeClass
	public static void setup() throws Exception {
		OUTPUT.delete();
		OUTPUT.mkdirs();

		URL fileUrl = Thread.currentThread().getContextClassLoader().getResource(TEST_TARGETS_NAME);
		Assert.assertTrue(fileUrl != null);
		File testTargets = new File(fileUrl.toURI());
		failTestTargets = new File(testTargets, "fail");
		passTestTargets =  new File(testTargets, "pass");

	}

	@Test(expected = ValidationException.class)
	public void testVariableAssignFail() throws Exception {
		compiler.compile(new File(failTestTargets, "varassign.bali"), OUTPUT);
	}

	@Test
	public void testVariableAssignPass() throws Exception {
		compiler.compile(new File(passTestTargets, "varassign.bali"), OUTPUT);
	}

}
