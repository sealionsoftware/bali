package bali.compiler.type;

import bali.compiler.parser.tree.TypeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class TypeLibrary {

	private TypeDeclarationTypeBuilder declarationBuilder = new TypeDeclarationTypeBuilder(this);
	private ClasspathTypeBuilder classpathBuilder = new ClasspathTypeBuilder(this);
	private Map<String, Type> types = new HashMap<>();

	public void addDeclaration(TypeNode declaration) {
		Type ret = declarationBuilder.build(declaration);
		types.put(declaration.getQualifiedClassName(), ret);
		declaration.setResolvedType(ret);
	}

	public Type getType(String fullyQualifiedClassName) {

		Type cached = types.get(fullyQualifiedClassName);
		if (cached != null) {
			return cached;
		}

		Type built = classpathBuilder.build(fullyQualifiedClassName);
		types.put(fullyQualifiedClassName, built);
		classpathBuilder.complete(built);

		return built;
	}

}
