package bali.compiler.type;

import bali.compiler.parser.tree.TypeNode;
import bali.compiler.reference.BlockingReference;
import bali.compiler.reference.Reference;
import bali.compiler.reference.Semaphore;
import bali.compiler.reference.SimpleReference;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class TypeLibrary {

	private final TypeDeclarationTypeBuilder declarationBuilder = new TypeDeclarationTypeBuilder(this);
	private final ClasspathTypeBuilder classpathBuilder = new ClasspathTypeBuilder(this);
	private final Map<String, Reference<Type>> types = new HashMap<>();
	private Semaphore localTypesComplete = new Semaphore();

	public void notifyOfDeclaration(String qualifiedClassName) {
		Reference<Type> reference = new BlockingReference<>();
		types.put(qualifiedClassName, reference);
	}

	public Type addDeclaration(TypeNode declaration) {
		Type ret = declarationBuilder.build(declaration);
		Reference<Type> reference = types.get(declaration.getQualifiedClassName());
		reference.set(ret);
		return ret;
	}

	public void localTypesComplete() {
		localTypesComplete.release();
	}

	public void checkTypesComplete() {
		localTypesComplete.check();
	}

	public Type getType(String fullyQualifiedClassName) {
		return getReference(fullyQualifiedClassName).get();
	}

	public Reference<Type> getReference(String fullyQualifiedClassName) {
		Reference<Type> cached = types.get(fullyQualifiedClassName);
		if (cached != null) {
			return cached;
		}

		cached = new BlockingReference<>();
		types.put(fullyQualifiedClassName, cached);
		Type built = classpathBuilder.build(fullyQualifiedClassName);
		cached.set(built);
		return cached;
	}

}
