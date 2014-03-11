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

/**
 * User: Richard
 * Date: 21/11/13
 */
public class NullabilityUnitTest {

	private static final String TEST_TARGETS_NAME = "bali/compiler/validator/";
	private static final String PACKAGE_NAME = "nullable";
	private static final String FILE_NAME = PACKAGE_NAME + BaliCompiler.BALI_SOURCE_FILE_EXTENSION;
	private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

	private BaliCompiler compiler = new BaliCompiler();

	@Test(expected = ValidationException.class)
	public void testVariableAssignFail() throws Exception {
		InputStream is = classLoader.getResourceAsStream(TEST_TARGETS_NAME + "fail/" + FILE_NAME);
		compiler.compile(
				Collections.singletonList(new PackageDescription(PACKAGE_NAME, is, FILE_NAME)),
				Mockito.mock(OutputStream.class),
				classLoader
		);
	}

	@Test
	public void testVariableAssignPass() throws Exception {
		InputStream is = classLoader.getResourceAsStream(TEST_TARGETS_NAME + "pass/" + FILE_NAME);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		compiler.compile(
				Collections.singletonList(new PackageDescription(PACKAGE_NAME, is, FILE_NAME)),
				os,
				classLoader
		);
		Assert.assertTrue(os.toByteArray().length > 1024);
	}

}
