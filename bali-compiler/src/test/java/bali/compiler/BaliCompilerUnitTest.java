package bali.compiler;

import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static java.lang.Thread.currentThread;
import static junit.framework.Assert.assertNotNull;

/**
 * User: Richard
 * Date: 14/04/13
 */
public class BaliCompilerUnitTest {

	private static final String TEST_TARGET_NAME = "bali/compiler/example.bali";
	private static final String TEST_OUTPUT_NAME = "/../../../unittests";

	@Test
	public void testCompileExample() throws Exception{

		URL fileUrl = currentThread().getContextClassLoader().getResource(TEST_TARGET_NAME);
		assertNotNull(fileUrl);
		File inputs = new File(fileUrl.toURI()).getParentFile();
		File outputs = new File(inputs, TEST_OUTPUT_NAME);

		outputs.delete();
		outputs.mkdirs();

		BaliCompiler.main(new String[]{inputs.toString(), outputs.toString()});
		File[] outputFiles = outputs.listFiles();
		assertNotNull(outputFiles);
		Assert.assertEquals(1, outputFiles.length);
		Assert.assertEquals("compiler.bar", outputFiles[0].getName());

	}

}
