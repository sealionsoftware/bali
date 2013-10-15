package bali.compiler.type;

import bali.compiler.parser.tree.TypeNode;
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

	private TypeDeclarationTypeBuilder declarationBuilder = new TypeDeclarationTypeBuilder(this);
	private ClasspathTypeBuilder classpathBuilder = new ClasspathTypeBuilder(this);
	private Map<String, Reference<Type>> types = new HashMap<>();

	private Semaphore localClassesComplete = new Semaphore();
	private Semaphore localInterfacesComplete = new Semaphore();

	public void notifyOfDeclaration(String qualifiedClassName) {
		Reference<Type> reference = new SimpleReference<>();
		types.put(qualifiedClassName, reference);
	}

	public Type addDeclaration(TypeNode declaration) {
		Type ret = declarationBuilder.build(declaration);
		Reference<Type> reference = types.get(declaration.getQualifiedClassName());
		reference.set(ret);
		return ret;
	}

	public void localClassesComplete() {
		localClassesComplete.release();
	}

	public void localInterfacesComplete() {
		localInterfacesComplete.release();
	}


	public void checkCompilationTypesComplete(){
		localClassesComplete.check();
		localInterfacesComplete.check();
	}

	public Type getType(String fullyQualifiedClassName) {
		return getReference(fullyQualifiedClassName).get();
	}

	public Reference<Type> getReference(String fullyQualifiedClassName) {
		Reference<Type> cached = types.get(fullyQualifiedClassName);
		if (cached != null) {
			return cached;
		}

		cached = new SimpleReference<>();
		types.put(fullyQualifiedClassName, cached);
		Type built = classpathBuilder.build(fullyQualifiedClassName);
		cached.set(built);

		return cached;
	}

}
