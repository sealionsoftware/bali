package bali.compiler.type;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds a Validator "Type" model for referenced types on the classpath
 * <p/>
 * User: Richard
 * Date: 23/08/13
 */
public class ClasspathTypeBuilder {

	private TypeLibrary library;

	public ClasspathTypeBuilder(TypeLibrary library) {
		this.library = library;
	}

	private Map<Type, List<Site>> uninitialisedSites = new HashMap<>();

	public Type build(String typeToBuild) {

		ClassReader reader;
		try {
			reader = new ClassReader(typeToBuild);
		} catch (IOException e) {
			throw new RuntimeException("Cannot read bytecode for class " + typeToBuild, e);
		}

		ClassPathTypeBuilderVisitor visitor = new ClassPathTypeBuilderVisitor();
		reader.accept(visitor, ClassReader.SKIP_FRAMES);

		Type ret = visitor.getClasspathType();
		uninitialisedSites.put(ret, visitor.getUninitialisedSites());

		return ret;
	}

	public void complete(Type t) {
		List<Site> uninitialisedSites = this.uninitialisedSites.remove(t);
		for (Site uninitialisedSite : uninitialisedSites) {
			Type type = library.getType(uninitialisedSite.getClassName());
			uninitialisedSite.init(type);
		}
	}

}
