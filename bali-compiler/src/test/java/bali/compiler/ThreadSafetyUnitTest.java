package bali.compiler;

import bali.compiler.validation.ValidationException;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;

/**
 * User: Richard
 * Date: 21/11/13
 */
public class ThreadSafetyUnitTest {

	private static final String TEST_TARGETS_NAME = "bali/compiler/threadsafe/";
	private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	private BaliCompiler compiler = new BaliCompiler();

	@Test(expected = ValidationException.class)
	public void testVariableAssignFail() throws Exception {
		InputStream is = classLoader.getResourceAsStream(TEST_TARGETS_NAME + "/fail/varassign.bali");
		compiler.compile(
				Collections.singletonList(new PackageDescription("fail", is)),
				Mockito.mock(OutputStream.class),
				classLoader
		);
	}

	@Test
	public void testVariableAssignPass() throws Exception {
		InputStream is = classLoader.getResourceAsStream(TEST_TARGETS_NAME + "/pass/varassign.bali");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		compiler.compile(
				Collections.singletonList(new PackageDescription("pass", is)),
				os,
				classLoader
		);
		Assert.assertTrue(os.toByteArray().length > 1024);
	}

}
