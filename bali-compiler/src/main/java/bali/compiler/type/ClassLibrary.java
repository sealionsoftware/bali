package bali.compiler.type;

import bali.compiler.parser.tree.ClassNode;
import bali.compiler.reference.BlockingReference;
import bali.compiler.reference.Reference;
import bali.compiler.reference.SimpleReference;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class ClassLibrary {

	private final ClassDeclarationTypeBuilder declarationBuilder;
	private final ClasspathTypeBuilder classpathBuilder;
	private final Map<String, Reference<Class>> types;

	public ClassLibrary(ClassLoader dependencyLoader) {
		declarationBuilder = new ClassDeclarationTypeBuilder();
		classpathBuilder = new ClasspathTypeBuilder(this, dependencyLoader);
		types = new HashMap<>();
	}

	public void notifyOfDeclaration(String qualifiedClassName) {
		Reference<Class> reference = new BlockingReference<>();
		types.put(qualifiedClassName, reference);
	}

	public Class addDeclaration(ClassNode declaration) {
		Class ret = declarationBuilder.build(declaration);
		Reference<Class> reference = types.get(declaration.getQualifiedClassName());
		reference.set(ret);
		return ret;
	}

	public synchronized Reference<Class> getReference(String fullyQualifiedClassName) {

		if (Object.class.getName().equals(fullyQualifiedClassName)){
			return new SimpleReference<>(null);
		}

		Reference<Class> cached = types.get(fullyQualifiedClassName);
		if (cached != null) {
			return cached;
		}

		cached = new SimpleReference<>();
		types.put(fullyQualifiedClassName, cached);
		try {
			cached.set(classpathBuilder.build(fullyQualifiedClassName));
		} catch (Exception e){
			types.remove(fullyQualifiedClassName);
			throw e;
		}

		return cached;
	}

}
