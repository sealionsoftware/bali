package bali.compiler.validator;

import bali.compiler.BaliCompiler;
import bali.compiler.PackageDescription;
import bali.compiler.validation.ValidationException;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;

import static java.lang.Thread.*;

/**
 * User: Richard
 * Date: 21/11/13
 */
public class ThreadSafetyUnitTest {

	private static final String TEST_TARGETS_NAME = "bali/compiler/validator/";
	private static final String PACKAGE_NAME = "threadsafe";
	private static final String FILE_NAME = PACKAGE_NAME + BaliCompiler.BALI_SOURCE_FILE_EXTENSION;
	private static final ClassLoader classLoader = currentThread().getContextClassLoader();

	private BaliCompiler compiler = new BaliCompiler();

	@Test(expected = ValidationException.class)
	public void testVariableAssignFail() throws Exception {
		InputStream is = classLoader.getResourceAsStream(TEST_TARGETS_NAME + "fail/" + FILE_NAME);
		compiler.compile(
				Collections.singletonList(new PackageDescription(PACKAGE_NAME, is, FILE_NAME)),
				Mockito.mock(OutputStream.class),
				classLoader,
				null
		);
	}

	@Test
	public void testVariableAssignPass() throws Exception {
		InputStream is = classLoader.getResourceAsStream(TEST_TARGETS_NAME + "pass/" + FILE_NAME);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		compiler.compile(
				Collections.singletonList(new PackageDescription(PACKAGE_NAME, is, FILE_NAME)),
				os,
				classLoader,
				null
		);
		Assert.assertTrue(os.toByteArray().length > 1024);
	}

}
