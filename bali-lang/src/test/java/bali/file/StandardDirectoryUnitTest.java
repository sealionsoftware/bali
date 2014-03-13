package bali.file;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

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
		java.io.File tmp = new java.io.File(System.getProperty("java.io.tmpdir"));
		delegate = new java.io.File(tmp, "unittests");
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

	@AfterClass
	public static void tearDown() throws Exception{
		Files.walkFileTree(delegate.toPath(), new SimpleFileVisitor<Path>() {
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				if (exc == null) {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				} else {
					throw exc;
				}
			}
		});
	}

}
