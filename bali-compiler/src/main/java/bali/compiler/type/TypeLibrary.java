package bali.compiler.type;

import bali.compiler.parser.tree.TypeNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class TypeLibrary {

	private TypeDeclarationTypeBuilder declarationBuilder = new TypeDeclarationTypeBuilder(this);
	private ClasspathTypeBuilder classpathBuilder = new ClasspathTypeBuilder(this);
	private PackageConstantsBuilder constantsBuilder = new PackageConstantsBuilder(this);
	private Map<String, Reference<Type>> types = new HashMap<>();
	private Map<String, List<Declaration>> constants = new HashMap<>();

	public void notifyOfDeclaration(TypeNode declaration) {
		Reference<Type> reference = new Reference<>();
		types.put(declaration.getQualifiedClassName(), reference);
	}

	public void addDeclaration(TypeNode declaration) {
		Reference<Type> reference = types.get(declaration.getQualifiedClassName());
		Type ret = declarationBuilder.build(declaration);
		reference.set(ret);
		declaration.setResolvedType(ret);
	}

	public void addConstants(String name, List<Declaration> declarations) {
		constants.put(name, declarations);
	}

	public Type getType(String fullyQualifiedClassName) {
		return getReference(fullyQualifiedClassName).get();
	}

	public List<Declaration> getConstants(String fullyQualifiedPackageName) {
		List<Declaration> cached = constants.get(fullyQualifiedPackageName);
		if (cached != null) {
			return cached;
		}
		List<Declaration> packageConstants = constantsBuilder.buildPackageConstants(fullyQualifiedPackageName);
		constants.put(fullyQualifiedPackageName, packageConstants);
		return packageConstants;
	}

	public Reference<Type> getReference(String fullyQualifiedClassName) {

		Reference<Type> cached = types.get(fullyQualifiedClassName);
		if (cached != null) {
			return cached;
		}

		cached = new Reference<>();
		types.put(fullyQualifiedClassName, cached);
		Type built = classpathBuilder.build(fullyQualifiedClassName);
		cached.set(built);

		return cached;
	}

}
