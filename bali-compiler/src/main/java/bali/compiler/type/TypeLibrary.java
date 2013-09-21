package bali.compiler.type;

import bali.compiler.parser.tree.TypeNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class TypeLibrary {

	public static final Type NULL_TYPE = new Type(
			"<NULL>",
			Collections.<Declaration>emptyList(),
			Collections.<Site>emptyList(),
			Collections.<Declaration>emptyList(),
			Collections.<Method>emptyList(),
			Collections.<Operator>emptyList(),
			Collections.<UnaryOperator>emptyList(),
			Collections.<Declaration>emptyList(),
			true
	);

	private TypeDeclarationTypeBuilder declarationBuilder = new TypeDeclarationTypeBuilder(this);
	private ClasspathTypeBuilder classpathBuilder = new ClasspathTypeBuilder(this);
	private Map<String, Reference<Type>> types = new HashMap<>();


	public void addDeclaration(TypeNode declaration) {
		Reference<Type> reference = new Reference<>();
		types.put(declaration.getQualifiedClassName(), reference);
		Type ret = declarationBuilder.build(declaration);
		reference.set(ret);
		declaration.setResolvedType(ret);

	}

	public Type getType(String fullyQualifiedClassName) {
		return getReference(fullyQualifiedClassName).get();
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
