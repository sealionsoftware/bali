package bali.compiler.validator;

import bali.compiler.BaliCompiler;
import bali.compiler.PackageDescription;
import junit.framework.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;

/**
 * User: Richard
 * Date: 21/11/13
 */
public class TypeInferUnitTest {

	private static final String TEST_TARGETS_NAME = "bali/compiler/validator/";
	private static final String PACKAGE_NAME = "typeinfer";
	private static final String FILE_NAME = PACKAGE_NAME + BaliCompiler.BALI_SOURCE_FILE_EXTENSION;
	private static final ClassLoader CLASSLOADER = Thread.currentThread().getContextClassLoader();

	private BaliCompiler compiler = new BaliCompiler();

	@Test
	public void testVariableAssignPass() throws Exception {
		InputStream is = CLASSLOADER.getResourceAsStream(TEST_TARGETS_NAME + "pass/" + FILE_NAME);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		compiler.compile(
				Collections.singletonList(new PackageDescription(PACKAGE_NAME, is, FILE_NAME)),
				os,
				CLASSLOADER
		);
		Assert.assertTrue("Non empty file", os.toByteArray().length > 0);
	}

}
