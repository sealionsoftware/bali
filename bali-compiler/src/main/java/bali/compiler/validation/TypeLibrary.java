package bali.compiler.validation;

import bali.compiler.parser.tree.TypeDeclaration;
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
	private ClasspathTypeBuilder classpathBuilder = new ClasspathTypeBuilder(this);
	private Map<String, Type> types = new HashMap<>();

	public void addDeclaration(TypeDeclaration declaration){
		types.put(declaration.getQualifiedClassName(), declarationBuilder.build(declaration));
	}

	public Type getType(String fullyQualifiedClassName) {

		Type cached = types.get(fullyQualifiedClassName);
		if (cached != null){
			return cached;
		}

		return classpathBuilder.build(fullyQualifiedClassName);
	}

//	public TypeDeclaration getTypeDeclaration(Class clazz) {
//
//		TypeDeclaration cached = declarations.get(clazz.getName());
//		if (cached != null){
//			return cached;
//		}
//
//		return builder.build(clazz);
//	}

//	private Type getType(java.lang.reflect.Type type, Map<String, Type> parametrization) throws ClassNotFoundException {
//		if (type instanceof Class){
//			return getTypeForClass((Class) type);
//		}
//		if (type instanceof ParameterizedType){
//			return getTypeForParameterizedType((ParameterizedType) type, parametrization);
//		}
//		if (type instanceof TypeVariable){
//			return getTypeForVariable((TypeVariable) type, parametrization);
//		}
//		if (type instanceof WildcardType){
//			return getTypeForClass((Class) Object.class);
//		}
//		return null;
//	}

//	private Type getTypeForClass(Class clazz) throws ClassNotFoundException {
//		if (clazz == void.class){
//			return null;
//		}
//		Type type = new Type();
//		type.setClassName(clazz.getName());
//		type.setDeclaration(getTypeDeclaration(clazz));
//		return type;
//	}

//	private Type getTypeForParameterizedType(ParameterizedType type, Map<String, Type> parametrization) throws ClassNotFoundException{
//		Type raw = getType(type.getRawType(), parametrization);
//		for (java.lang.reflect.Type argumentType : type.getActualTypeArguments()){
//			raw.addParameter(getType(argumentType, parametrization));
//		}
//		return raw;
//	}
//
//	private Type getTypeForVariable(TypeVariable type, Map<String, Type> parametrization){
//		Type ret = parametrization.get(type.getName());
//		if (ret != null){
//			return ret;
//		}
//		ret = new Type();
//		ret.setClassName(type.getName());
//		ret.setErase(true);
//		return ret;
//	}

}
