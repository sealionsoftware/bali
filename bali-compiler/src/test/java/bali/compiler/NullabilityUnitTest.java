package bali.compiler;

import bali.compiler.validation.ValidationException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;

/**
 * User: Richard
 * Date: 21/11/13
 */
public class NullabilityUnitTest {

	private static final String TEST_TARGETS_NAME = "bali/compiler/nullable/";
	private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	private BaliCompiler compiler = new BaliCompiler();

	@Test(expected = ValidationException.class)
	public void testVariableAssignFail() throws Exception {
		InputStream is = classLoader.getResourceAsStream(TEST_TARGETS_NAME + "/fail/varassign.bali");
		compiler.compile(Collections.singletonList(new PackageDescription("fail", is)), Mockito.mock(OutputStream.class));
	}

	@Test
	public void testVariableAssignPass() throws Exception {
		InputStream is = classLoader.getResourceAsStream(TEST_TARGETS_NAME + "/pass/varassign.bali");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		compiler.compile(Collections.singletonList(new PackageDescription("pass", is)), os);
		Assert.assertTrue(os.toByteArray().length > 1024);
	}

}
