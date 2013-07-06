package bali.compiler.module;

import bali.compiler.GeneratedClass;
import bali.compiler.GeneratedModule;
import bali.compiler.GeneratedPackage;

import java.io.File;
import java.io.FileOutputStream;
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

	public GeneratedModule writeModule(String name, List<GeneratedPackage> packages, File directory) throws Exception {

		File out = new File(directory, name + ".jar");
		GeneratedModule module = new GeneratedModule(name);

		out.delete();
		out.createNewFile();
		JarOutputStream jos = null;

		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

		try {

			jos = new JarOutputStream(new FileOutputStream(out), manifest);
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
				module.addPackage(generatedPackage);
			}

		} finally {
			if (jos != null)
				jos.close();
		}

		return module;
	}
}
