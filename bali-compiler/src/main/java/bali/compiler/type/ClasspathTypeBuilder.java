package bali.compiler.type;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Builds a Validator "Class" model for referenced types on the classpath
 * <p/>
 * User: Richard
 * Date: 23/08/13
 */
public class ClasspathTypeBuilder {

	private ClassLibrary library;
	private ClassLoader classLoader;

	public ClasspathTypeBuilder(ClassLibrary library, ClassLoader classLoader) {
		this.library = library;
		this.classLoader = classLoader;
	}

	public Class build(String typeToBuild) {

		ClassReader reader;
		try {
			InputStream classFileStream = classLoader.getResourceAsStream(typeToBuild.replace('.','/').concat(".class"));
			reader = new ClassReader(classFileStream);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read bytecode for class " + typeToBuild, e);
		}

		ClassPathTypeBuilderVisitor visitor = new ClassPathTypeBuilderVisitor(library);
		reader.accept(visitor, ClassReader.SKIP_FRAMES);
		return visitor.getClasspathClass();
	}

}
