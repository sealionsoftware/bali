package bali.compiler.validation.type;

import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 27/08/13
 */
public abstract class MethodDeclaringType extends Type {

	private List<Method> methods;
	private List<Site> interfaces;

	public MethodDeclaringType(String className,
	                           List<Declaration> parameters,
	                           List<Method> methods,
	                           List<Site> interfaces) {
		super(className, parameters);
		this.interfaces = interfaces;
		this.methods = methods;
	}

	public List<Site> getInterfaces() {
		return interfaces;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public Method getMethod(String name, List<Site> argumentTypes){

		for (Method method : methods){
			if (method.getName().equals(name)){
				List<Declaration> parameters = method.getParameters();
				if (checkIfArgumentsAreAssignable(argumentTypes, parameters)){
					return method;
				}
			}
		}
		return null;
	}

	private boolean checkIfArgumentsAreAssignable(List<Site> argumentTypes, List<Declaration> parameters){
		if (argumentTypes.size() != parameters.size()){
			return false;
		}
		Iterator<Site> i = argumentTypes.iterator();
		Iterator<Declaration> j =parameters.iterator();
		while (i.hasNext()){
			if (!i.next().isAssignableTo(j.next().getType())){
				return false;
			}
		}
		return true;
	}



}
