package bali.compiler.validation;

import bali.compiler.parser.tree.TypeNode;
import bali.compiler.validation.type.Type;

import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class TypeLibrary {

	private TypeDeclarationTypeBuilder declarationBuilder = new TypeDeclarationTypeBuilder(this);
	private ClasspathTypeBuilder classpathBuilder = new ClasspathTypeBuilder();
	private Map<String, Type> types = new HashMap<>();

	public void addDeclaration(TypeNode declaration){
		Type ret = declarationBuilder.build(declaration);
		types.put(declaration.getQualifiedClassName(), ret );
		declaration.setResolvedType(ret);
	}

	public Type getType(String fullyQualifiedClassName) {

		Type cached = types.get(fullyQualifiedClassName);
		if (cached != null){
			return cached;
		}

		return classpathBuilder.build(fullyQualifiedClassName);
	}

}
