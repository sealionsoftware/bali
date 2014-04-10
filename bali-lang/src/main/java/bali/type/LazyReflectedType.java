package bali.type;

import bali.BaliThrowable;
import bali.Boolean;
import bali.Iterator;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.annotation.Parameters;
import bali.collection.Collection;
import bali.collection.LinkedList;
import bali.collection.List;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.LinkedHashMap;
import java.util.Map;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 18/03/14
 */
public class LazyReflectedType<T> implements Type {

	private bali.String className;
	private Collection<Type> typeArguments;
	private Map<String, Type> typeArgumentMap = new LinkedHashMap<>();

	private Collection<Declaration> parameters;
	private Collection<Type> interfaces;
	private Collection<Type> superTypes;

	private Class<T> template;

	public LazyReflectedType(Class<T> template, Collection<Type> typeArguments) {
		this.template = template;
		this.className = convert(template.getName());
		this.typeArguments = typeArguments;
		TypeVariable[] typeParameters = template.getTypeParameters();
		if (!convert(typeArguments.isEmpty())){
			if (typeParameters.length != convert(typeArguments.size())){
				throw new BaliThrowable("Invalid type arguments");
			}
			Iterator<Type> i = typeArguments.iterator();
			for (TypeVariable typeParameter : typeParameters){
				typeArgumentMap.put(typeParameter.getName(), i.next());
			}
		}
	}

	public LazyReflectedType(bali.String className, Collection<Type> typeArguments) {
		this((Class<T>) forName(convert(className)), typeArguments);
	}

	private static Class<?> forName(String name){
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			throw new BaliThrowable(e.getMessage());
		}
	}

	public bali.String getClassName() {
		return className;
	}

	public Collection<Type> getTypeArguments() {
		return typeArguments;
	}

	public Collection<Declaration> getParameters() {
		if (parameters == null){
			createParameters();
		}
		return parameters;
	}

	private synchronized void createParameters(){
		if (parameters == null){
			List<Declaration> parameters = new LinkedList<>();
			Constructor<T> constructor = getParametersConstructor(template);
			Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
			Name[] names = gatherAnnotations(paramAnnotations, new Name[paramAnnotations.length]);
			Nullable[] nullables = gatherAnnotations(paramAnnotations, new Nullable[paramAnnotations.length]);
			java.lang.reflect.Type[] paramTypes = constructor.getGenericParameterTypes();
			for (int i = 0 ; i < names.length ; i++){
				Name name = names[i];
				Nullable nullable = nullables[i];
				if (name == null){
					throw new BaliThrowable("Parameter " + i + " of class " + template + " is not named properly");
				}
				parameters.add(new Declaration(
						convert(names[i].value()),
						getTypeFor(paramTypes[i]),
						convert(nullable != null)
				));
			}

			this.parameters = parameters;
		}
	}

	private synchronized void createInterfaces(){
		if (interfaces == null){
			if (template.isInterface()){
				interfaces = bali.collection._.EMPTY;
			}
			List<Type> interfaces = new LinkedList<>();
			for(java.lang.reflect.Type reflectedType : template.getGenericInterfaces()) {
				interfaces.add(TypeFactory.getType(getSignature(reflectedType)));
			}
			this.interfaces = interfaces;
		}
	}

	private synchronized void createSuperTypes(){
		if (superTypes == null){
			List<Type> superTypes = new LinkedList<>();
			if (template.isInterface()){
				for(java.lang.reflect.Type reflectedType : template.getGenericInterfaces()) {
					superTypes.add(TypeFactory.getType(getSignature(reflectedType)));
				}
			} else if (template.getSuperclass() != Object.class){
				superTypes.add(TypeFactory.getType(getSignature(template.getGenericSuperclass())));
			}

			this.superTypes = superTypes;
		}
	}

	private <A extends Annotation> A[] gatherAnnotations(Annotation[][] parameterListAnnotations, A[] ret){
		int i = 0;
		Class<A> componentType = (Class<A>) ret.getClass().getComponentType();
		for (Annotation[] parameterAnnotations : parameterListAnnotations){
			for (Annotation parameterAnnotation : parameterAnnotations){
				if (componentType.isAssignableFrom(parameterAnnotation.annotationType())){
					ret[i] = (A) parameterAnnotation;
					break;
				}
			}
			i++;
		}
		return ret;
	}

	private <C> Constructor<C> getParametersConstructor(Class<C> clazz){
		Constructor<C>[] constructors = (Constructor<C>[]) clazz.getConstructors();
		for (Constructor<C> constructor : constructors){
			if (constructor.isAnnotationPresent(Parameters.class)){
				return constructor;
			}
		}
		throw new BaliThrowable("Class " + clazz + " has no @Parameters constructor and is not a valid bali type");
	}

	private Type getTypeFor(java.lang.reflect.Type in){
		if (in instanceof TypeVariable){
			return typeArgumentMap.get(((TypeVariable) in).getName());
		}
		return TypeFactory.getType(getSignature(in));
	}

	private String getSignature(java.lang.reflect.Type in){
		if (in instanceof Class){
			return ((Class) in).getName();
		}
		if (in instanceof ParameterizedType){
			ParameterizedType pt = (ParameterizedType) in;
			Class base = (Class) pt.getRawType();

			java.lang.reflect.Type[] typeArguments = pt.getActualTypeArguments();
			List<String> signatures = new LinkedList<>();
			for (java.lang.reflect.Type typeArgument : typeArguments){
				String signituare = getSignature(typeArgument);
				if (signituare != null){
					signatures.add(signituare);
				}
			}
			StringBuilder sb = new StringBuilder(base.getName());
			Iterator<String> i = signatures.iterator();
			if(convert(i.hasNext())){
				sb.append("[")
						.append(i.next());
				while (convert(i.hasNext())){
					sb.append(",")
							.append(i.next());
				}
				sb.append("]");
			}
			return sb.toString();
		}
		if (in instanceof TypeVariable){
			Type replaced = typeArgumentMap.get(((TypeVariable) in).getName());
			return replaced != null ? convert(replaced.getClassName()) : null;
		}

		return in.toString();
	}

	public T createObject(Collection<?> arguments) {
		try {
			return (T) template.getConstructors()[0].newInstance(convert(arguments).toArray());
		} catch (Exception e) {
			throw new BaliThrowable(e.getMessage());
		}
	}

	public Boolean assignableTo(Type otherType) {

		if (className.equals(otherType.getClassName())){

			Iterator<Type> thisIterator = typeArguments.iterator();
			Iterator<Type> thatIterator = otherType.getTypeArguments().iterator();
			while(convert(thisIterator.hasNext().and(thatIterator.hasNext()))){
				if (!convert(thisIterator.next().assignableTo(thatIterator.next()))){
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;

		}

		if (interfaces == null){
			createInterfaces();
		}

		Iterator<Type> i = interfaces.iterator();
		while (convert(i.hasNext())){
			Type iface = i.next();
			if (convert(iface.assignableTo(otherType))){
				return Boolean.TRUE;
			}
		}

		if (superTypes == null){
			createSuperTypes();
		}

		i = superTypes.iterator();
		while (convert(i.hasNext())){
			Type supertType = i.next();
			if (convert(supertType.assignableTo(otherType))){
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	public Boolean instanceOf(@Name("className") bali.String className) {
		if (convert(className.equalTo(this.className))){
			return Boolean.TRUE;
		}
		if (superTypes == null){
			createSuperTypes();
		}
		Iterator<Type> i = superTypes.iterator();
		while (convert(i.hasNext())){
			Type superType = i.next();
			if (convert(className.equalTo(superType.getClassName()))){
				return Boolean.TRUE;
			}
		}
		if (interfaces == null){
			createInterfaces();
		}
		i = interfaces.iterator();
		while (convert(i.hasNext())){
			Type iface = i.next();
			if (convert(className.equalTo(iface.getClassName()))){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(convert(className));
		if (!convert(typeArguments.isEmpty())){
			sb.append("[");
			Iterator<Type> i = typeArguments.iterator();
			if (convert(i.hasNext())){
				sb.append(i.next().toString());
			}
			while (convert(i.hasNext())){
				sb.append(",").append(i.next().toString());
			}
			sb.append("]");
		}
		return sb.toString();
	}
}
