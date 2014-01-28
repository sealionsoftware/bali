package bali.compiler.module;

import bali.compiler.GeneratedClass;
import bali.compiler.GeneratedPackage;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * User: Richard
 * Date: 30/04/13
 */
public class JarPackager implements ModuleWriter {

	public void writeModule(List<GeneratedPackage> packages, OutputStream outputStream) throws Exception {

		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

		JarOutputStream jos = new JarOutputStream(outputStream, manifest);

		try {

			Set<String> createdDirectories = new HashSet<>();

			for (GeneratedPackage generatedPackage : packages) {
				StringBuilder completePath = new StringBuilder();
				for (String pathElement : generatedPackage.getName().split("\\.")) {
					completePath.append(pathElement).append("/");
					String currentPath = completePath.toString();
					if (!createdDirectories.contains(currentPath)) {
						createdDirectories.add(currentPath);
						JarEntry entry = new JarEntry(currentPath);
						entry.setTime(System.currentTimeMillis());
						jos.putNextEntry(entry);
						jos.closeEntry();
					}
				}
				for (GeneratedClass clazz : generatedPackage.getClasses()) {
					byte[] code = clazz.getCode();
					JarEntry entry = new JarEntry(completePath.toString() + clazz.getName() + ".class");
					entry.setTime(System.currentTimeMillis());
					jos.putNextEntry(entry);
					jos.write(code);
					jos.closeEntry();
				}
			}

		} finally {
			jos.close();
		}
	}
}
