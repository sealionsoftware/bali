package bali.file;

import org.junit.Assert;
import org.junit.Test;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 12/03/14
 */
public class StandardFileManagerUnitTest {

	private FileManager fileManager = new StandardFileManager();

	@Test
	public void testGetHomeDirectory(){
		Directory userHome = fileManager.getHomeDirectory();
		Assert.assertNotNull("Home Directory is not null", userHome);
		Assert.assertEquals("Home Directory is user.home", System.getProperty("user.home"), convert(userHome.getPath()));
	}

	@Test
	public void testGetCurrentDirectory(){
		Directory current = fileManager.getCurrentDirectory();
		Assert.assertNotNull("Current Directory is not null", current);
		Assert.assertEquals("Current Directory is user.home", new java.io.File("").getAbsoluteFile().getPath(), convert(current.getPath()));
	}

	@Test
	public void testGetDirectoryRelative(){

		java.io.File toCreate = new java.io.File("aDirectory");
		toCreate.mkdirs();

		try {
			Directory relative = fileManager.getDirectory(convert("aDirectory"));
			Assert.assertNotNull("Relative Directory is not null", relative);
			Assert.assertEquals("Relative Directory is called aDirectory", toCreate.getAbsoluteFile().getPath(), convert(relative.getPath()));
		} finally {
			toCreate.delete();
		}
	}

	@Test
	public void testGetDirectoryFail(){
		Directory relative = fileManager.getDirectory(convert("notADirectory"));
		Assert.assertNull("No Directory called notADirectory", relative);
	}

	@Test
	public void testGetDirectoryAbsolute(){

		java.io.File toCreate = new java.io.File("aDirectory");
		toCreate.mkdirs();

		String absolutePath = toCreate.getAbsolutePath();

		try {
			Directory absolute = fileManager.getDirectory(convert(absolutePath));
			Assert.assertNotNull("Absolute Directory is not null", absolute);
			Assert.assertEquals("Absolute Directory is called aDirectory", absolutePath, convert(absolute.getPath()));
		} finally {
			toCreate.delete();
		}
	}

	@Test
	public void testGetFileRelative() throws Exception {

		java.io.File toCreate = new java.io.File("aFile.txt");
		toCreate.createNewFile();

		try {
			File relative = fileManager.getFile(convert("aFile.txt"));
			Assert.assertNotNull("Relative File is not null", relative);
			Assert.assertEquals("Relative File is called aFile", toCreate.getAbsoluteFile().getPath(), convert(relative.getPath()));
		} finally {
			toCreate.delete();
		}
	}

	@Test
	public void testGetFileAbsolute() throws Exception {

		java.io.File toCreate = new java.io.File("aFile.txt");
		toCreate.createNewFile();

		String absolutePath = toCreate.getAbsolutePath();

		try {
			File absolute = fileManager.getFile(convert(absolutePath));
			Assert.assertNotNull("Absolute File is not null", absolute);
			Assert.assertEquals("Absolute File is called aFile", toCreate.getAbsoluteFile().getPath(), convert(absolute.getPath()));
		} finally {
			toCreate.delete();
		}
	}

}
