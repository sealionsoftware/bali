package bali.type;

import bali.Iterator;
import bali.annotation.Kind;
import bali.annotation.MetaType;
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

	private Class<T> template;

	public LazyReflectedType(bali.String className, Collection<Type> typeArguments) {
		this.className = className;
		this.typeArguments = typeArguments;

		try {

			template = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(convert(className));
			TypeVariable[] typeParameters = template.getTypeParameters();
			if (typeParameters.length != convert(typeArguments.size())){
				throw new RuntimeException("Invalid type arguments");
			}
			Iterator<Type> i = typeArguments.iterator();
			for (TypeVariable typeParameter : typeParameters){
				typeArgumentMap.put(typeParameter.getName(), i.next());
			}

		} catch (Exception e){
			throw new RuntimeException(e);
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
			Name[] names = getAnnotations(paramAnnotations, new Name[paramAnnotations.length]);
			Nullable[] nullables = getAnnotations(paramAnnotations, new Nullable[paramAnnotations.length]);
			java.lang.reflect.Type[] paramTypes = constructor.getGenericParameterTypes();
			for (int i = 0 ; i < names.length ; i++){
				Name name = names[i];
				Nullable nullable = nullables[i];
				if (name == null){
					throw new RuntimeException("Parameter " + i + " of class " + template + " is not named properly");
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

	private <A extends Annotation> A[] getAnnotations(Annotation[][] parameterListAnnotations, A[] ret){
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
		throw new RuntimeException("Class " + clazz + " has no @Parameters constructor and is not a valid bali type");
	}

	private Type getTypeFor(java.lang.reflect.Type in){
		if (in instanceof TypeVariable){
			return typeArgumentMap.get(((TypeVariable) in).getName());
		}
		return TypeFactory.getType(getSignature(in));
	}

	private String getSignature(java.lang.reflect.Type in){
		if (in instanceof TypeVariable){
			// TODO: will this work if varaible name is translated?
			return convert(typeArgumentMap.get(((TypeVariable) in).getName()).getClassName());
		}
		if (in instanceof ParameterizedType){
			ParameterizedType pt = (ParameterizedType) in;
			Class base = (Class) pt.getRawType();

			int i = 0;
			java.lang.reflect.Type[] typeArguments = pt.getActualTypeArguments();
			StringBuilder sb = new StringBuilder(base.getName())
					.append("[")
					.append(getSignature(typeArguments[i++]));
			while (i < typeArguments.length){
				sb.append(",")
						.append(getSignature(typeArguments[i++]));
			}
			sb.append("]");

			return sb.toString();
		}
		if (in instanceof Class){
			return ((Class) in).getName();
		}
		return in.toString();
	}

	public T createObject(Collection<?> arguments) {
		try {
			return (T) template.getConstructors()[0].newInstance(convert(arguments).toArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
