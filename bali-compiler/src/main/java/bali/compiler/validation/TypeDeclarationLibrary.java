package bali.compiler.validation;

import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.TypeDeclaration;

import java.lang.String;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class TypeDeclarationLibrary {

	private ClassLoader classLoader;
	private Map<String, TypeDeclaration> declarations = new HashMap<>();

	public TypeDeclarationLibrary(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void addDeclaration(TypeDeclaration declaration){
		declarations.put(declaration.getQualifiedClassName(), declaration);
	}

	public TypeDeclaration getTypeDeclaration(String fullyQualifiedClassName) throws ClassNotFoundException {

		TypeDeclaration cached = declarations.get(fullyQualifiedClassName);
		if (cached != null){
			return cached;
		}

		Class clazz = classLoader.loadClass(fullyQualifiedClassName);
		return buildTypeDeclarationForClass(clazz);
	}

	public TypeDeclaration getTypeDeclaration(Class clazz) {

		TypeDeclaration cached = declarations.get(clazz.getName());
		if (cached != null){
			return cached;
		}

		return buildTypeDeclarationForClass(clazz);
	}

	private TypeDeclaration buildTypeDeclarationForClass(Class clazz){

		ClasspathType classpathType = new ClasspathType();
		classpathType.setAbstract(Modifier.isAbstract(clazz.getModifiers()));
		classpathType.setClassName(clazz.getSimpleName());
		classpathType.setQualifiedClassName(clazz.getName());

		declarations.put(clazz.getName(), classpathType);

		for (java.lang.reflect.Method method : clazz.getMethods()){

			MethodDeclaration methodDeclaration = new MethodDeclaration();
			methodDeclaration.setName(method.getName());
			methodDeclaration.setType(getTypeForClass(method.getReturnType()));
			for (Class argumentClass : method.getParameterTypes()){
				Declaration declaration = new Declaration();
				declaration.setType(getTypeForClass(argumentClass));
				methodDeclaration.addArgument(declaration);
			}

			classpathType.addMethod(methodDeclaration);
		}

		return classpathType;
	}

	private Type getType(java.lang.reflect.Type type){
		if (type instanceof Class){
			return getTypeForClass((Class) type);
		}
		if (type instanceof ParameterizedType){
			return getTypeForParameterizedType((ParameterizedType) type);
		}
		throw new RuntimeException();
	}

	private Type getTypeForClass(Class clazz){
		if (clazz == void.class){
			return null;
		}
		Type type = new Type();
		type.setClassName(clazz.getName());
		type.setDeclaration(getTypeDeclaration(clazz));
		return type;
	}

	private Type getTypeForParameterizedType(ParameterizedType type){
		Type raw = getType(type.getRawType());
		for (java.lang.reflect.Type argumentType : type.getActualTypeArguments()){
			raw.addParameter(getType(argumentType));
		}
		return raw;
	}


	private static class ClasspathType extends TypeDeclaration<MethodDeclaration> {

		private List<MethodDeclaration> declarations = new ArrayList<>();
		private Boolean isAbstract;

		public List<MethodDeclaration> getMethods() {
			return declarations;
		}

		private void setAbstract(Boolean anAbstract) {
			isAbstract = anAbstract;
		}

		public void addMethod(MethodDeclaration method) {
			declarations.add(method);
		}

		public Boolean getAbstract() {
			return isAbstract;
		}
	}
}
