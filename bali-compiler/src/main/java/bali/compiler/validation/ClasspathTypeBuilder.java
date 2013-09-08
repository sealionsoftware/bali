package bali.compiler.validation;

import bali.compiler.validation.type.Type;
import org.objectweb.asm.ClassReader;

import java.io.IOException;

/**
 * Builds a Validator "Type" model for referenced types on the classpath
 *
 * User: Richard
 * Date: 23/08/13
 */
public class ClasspathTypeBuilder {

	public Type build(String typeToBuild){

		ClassReader reader;
		try {
			reader = new ClassReader(typeToBuild);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read bytecode for class " + typeToBuild, e);
		}

		ClassPathTypeBuilderVisitor visitor = new ClassPathTypeBuilderVisitor();
		reader.accept(visitor, ClassReader.SKIP_CODE);

		return visitor.getClasspathType();
	}

}
