package bali.compiler;

import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * User: Richard
 * Date: 14/04/13
 */
public class BaliCompilerUnitTest {

	private static final String TEST_TARGET_NAME = "bali/compiler/example.bali";
	private static final String TEST_OUTPUT_NAME = "/../../../unittests";

	@Test
	public void testCompileExample() throws Exception{

		URL fileUrl = Thread.currentThread().getContextClassLoader().getResource(TEST_TARGET_NAME);
		Assert.assertNotNull(fileUrl);
		File inputs = new File(fileUrl.toURI()).getParentFile();
		File outputs = new File(inputs, TEST_OUTPUT_NAME);
		outputs.mkdirs();
		BaliCompiler.main(new String[]{inputs.getAbsolutePath(), outputs.getAbsolutePath()});

		File[] outputFiles = outputs.listFiles();
		Assert.assertEquals(1, outputFiles.length);
		Assert.assertEquals("compiler.jar", outputFiles[0].getName());

	}

}
