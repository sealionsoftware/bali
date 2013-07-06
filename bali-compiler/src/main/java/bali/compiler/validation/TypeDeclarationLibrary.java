package bali.compiler.validation;

import bali.Operator;
import bali.compiler.parser.tree.Declaration;
import bali.compiler.parser.tree.MethodDeclaration;
import bali.compiler.parser.tree.Type;
import bali.compiler.parser.tree.TypeDeclaration;

import java.lang.String;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
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

		ClasspathTypeDeclaration classpathType = new ClasspathTypeDeclaration();
		classpathType.setAbstract(Modifier.isAbstract(clazz.getModifiers()));
		classpathType.setClassName(clazz.getSimpleName());
		classpathType.setQualifiedClassName(clazz.getName());

		declarations.put(clazz.getName(), classpathType);

		for (java.lang.reflect.Type iface : clazz.getGenericInterfaces()){
			classpathType.addImplementation(getType(iface));
		}

		for (TypeVariable parameter : clazz.getTypeParameters()){
			classpathType.addParameter(getTypeForVariable(parameter));
		}

		for (java.lang.reflect.Method method : clazz.getMethods()){

			MethodDeclaration methodDeclaration = new MethodDeclaration();
			methodDeclaration.setName(method.getName());
			methodDeclaration.setType(getType(method.getGenericReturnType()));
			for (java.lang.reflect.Type argumentType : method.getGenericParameterTypes()){
				Declaration declaration = new Declaration();
				declaration.setType(getType(argumentType));
				methodDeclaration.addArgument(declaration);
			}

			if (method.isAnnotationPresent(Operator.class)){
				Operator operator = method.getAnnotation(Operator.class);
				methodDeclaration.setOperator(operator.value());
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
		if (type instanceof TypeVariable){
			return getTypeForVariable((TypeVariable) type);
		}
		if (type instanceof WildcardType){
			return getTypeForClass((Class) Object.class);
		}
		return null;
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

	private Type getTypeForVariable(TypeVariable type){
		Type ret = new Type();
		ret.setClassName(type.getName());
		ret.setErase(true);
		return ret;
	}

	private static class ClasspathTypeDeclaration extends TypeDeclaration<MethodDeclaration> {

		private List<MethodDeclaration> methodDeclarations = new ArrayList<>();
		private Boolean isAbstract;

		public List<MethodDeclaration> getMethods() {
			return methodDeclarations;
		}

		private void setAbstract(Boolean anAbstract) {
			isAbstract = anAbstract;
		}

		public void addMethod(MethodDeclaration method) {
			methodDeclarations.add(method);
		}

		public Boolean getAbstract() {
			return isAbstract;
		}

		public String toString(){
			return getClassName() ;
		}
	}
}
