package bali.compiler.type;


import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 29/09/13
 */
public class PackageConstantsBuilder {

	private ClassLibrary library;
	private ClassLoader classLoader;

	public PackageConstantsBuilder(ClassLibrary library) {
		this.library = library;
		this.classLoader = Thread.currentThread().getContextClassLoader();
	}

	public List<Declaration<Site>> buildPackageConstants(String name){

		ClassReader reader;
		String className = name.replace('.','/').concat(".class");
		try {
			InputStream classFileStream = classLoader.getResourceAsStream(className);
			if (classFileStream == null){
				return new ArrayList<>();
			}
			reader = new ClassReader(classFileStream);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read bytecode for package class " + className, e);
		}

		PackageConstantsBuilderVisitor visitor = new PackageConstantsBuilderVisitor(library);
		reader.accept(visitor, ClassReader.SKIP_FRAMES);

		return visitor.getDeclaredConstants();
	}
}
