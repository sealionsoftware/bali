package bali.file;

import bali.collection.Collection;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 12/03/14
 */
public class StandardDirectoryUnitTest {

    private static java.io.File delegate;
	private Directory directory = new StandardDirectory(delegate);

	@BeforeClass
	public static void setUp() throws Exception {
		String tempPath = System.getProperty("project.build.directory");
		if (tempPath == null){
			tempPath = System.getProperty("java.io.tmpdir");
		}
		java.io.File tmp = new java.io.File(tempPath);
		delegate = new java.io.File(new java.io.File(new java.io.File(tmp, "unittests"), StandardDirectoryUnitTest.class.getName()), UUID.randomUUID().toString());
		delegate.mkdirs();
	}

	@Test
	public void testCreateDirectory(){
		Directory created = directory.createDirectory(convert("testDirectory"));
		Assert.assertNotNull("Created Directory is not null", created);
		Assert.assertEquals("Created Directory is called testDirectory", "testDirectory", convert(created.getName()));
	}

	@Test
	public void testCreateFile(){
		File created = directory.createFile(convert("testFile"));
		Assert.assertNotNull("Created File is not null", created);
		Assert.assertEquals("Created File is called testFile", "testFile", convert(created.getName()));
	}

	@Test
	public void testDelete(){
		java.io.File toDelete = new java.io.File(delegate, "toDelete");
		toDelete.mkdir();
		new StandardDirectory(toDelete).delete();
		Assert.assertFalse("Directory toDelete no longer exists", toDelete.exists());
	}

	@Test
	public void testGetFile() throws Exception{
		java.io.File toLookUp = new java.io.File(delegate, "toLookUp");
		toLookUp.createNewFile();
		File lookedup = directory.getFile(convert("toLookUp"));
		Assert.assertNotNull("Looked up File is not null", lookedup);
		Assert.assertEquals("Looked up File is called toLookUp", "toLookUp", convert(lookedup.getName()));
	}

	@Test
	public void testGetDirectory() throws Exception{
		java.io.File toLookUp = new java.io.File(delegate, "toLookUp");
		toLookUp.mkdir();
		Directory lookedup = directory.getDirectory(convert("toLookUp"));
		Assert.assertNotNull("Looked up Directory is not null", lookedup);
		Assert.assertEquals("Looked up Directory is called toLookUp", "toLookUp", convert(lookedup.getName()));
	}

	@Test
	public void testGetChildren() throws Exception{
		java.io.File toList = new java.io.File(delegate, "toList");
		toList.mkdir();
		Collection<Directory> listed = directory.getChildren();
		Assert.assertNotNull("Listed Directories are not null", listed);
		Assert.assertFalse("List size is not zero", convert(listed.isEmpty()));
	}


}
