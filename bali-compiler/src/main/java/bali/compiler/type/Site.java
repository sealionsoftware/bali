package bali.compiler.type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 29/08/13
 */
public class Site<T extends Type> {

	private Reference<T> typeReference;
	private List<Site> arguments;

	private T type;
	private List<Declaration> parameters;
	private List<Method> methods;
	private List<Site> interfaces;
	private List<Operator> operators;
	private List<UnaryOperator> unaryOperators;
	private Boolean erase;

	public Site(T t, List<Site> arguments) {
		this(t.getClassName(), arguments, false);
		init(t);
	}
//
//	private Site(T t, List<Site> arguments, Boolean erase) {
//		this.className = t.getClassName();
//		this.arguments = arguments;
//		this.erase = erase;
//		init(t);
//	}

	public Site(String className, List<Site> arguments) {
		this(className, arguments, false);
	}

	private Site(String className, List<Site> arguments, Boolean erase) {
		this.className = className;
		this.arguments = arguments;
		this.erase = erase;
	}

	void init(T type) {

		this.type = type;
		this.parameters = parametriseTypeArguments(type.getTypeParameters(), arguments);

		if (type instanceof MethodDeclaringType) {
			MethodDeclaringType methodic = (MethodDeclaringType) type;
			this.methods = parametriseMethods(methodic.getMethods(), arguments);
			this.interfaces = parametriseInterfaces(methodic.getInterfaces(), arguments);
			if (type instanceof Interface) {
				Interface iface = (Interface) type;
				this.operators = parametriseOperators(iface.getOperators(), arguments);
				this.unaryOperators = parametriseUnaryOperators(iface.getUnaryOperators(), arguments);
			} else {
				this.operators = new ArrayList<>();
				this.unaryOperators = new ArrayList<>();
			}
		} else {
			this.methods = new ArrayList<>();
			this.parameters = new ArrayList<>();
			this.operators = new ArrayList<>();
			this.unaryOperators = new ArrayList<>();
		}
	}

	private List<Declaration> parametriseTypeArguments(List<Declaration> parameterDeclarations, List<Site> parameterArguments) {

		if (parameterDeclarations.size() != parameterArguments.size()) {
			throw new RuntimeException("Invalid Parameterization: " + parameterArguments + " => " + parameterDeclarations);
		}

		List<Declaration> ret = new ArrayList<>(parameterDeclarations.size());
		Iterator<Site> i = parameterArguments.iterator();
		for (Declaration declaration : parameterDeclarations) {
			Site site = i.next();
			if (!site.isAssignableTo(declaration.getType())) {
				throw new RuntimeException("Parameter argument is not within site type");
			}
			ret.add(new Declaration(
					declaration.getName(),
					site
			));
		}

		return ret;
	}

	private List<Site> parametriseInterfaces(List<Site> interfaces, List<Site> parameterArguments) {

		List<Site> ret = new ArrayList<>();
		for (Site iface : interfaces) {
			ret.add(parametriseSite(iface, parameterArguments));
		}
		return ret;
	}

	private List<Method> parametriseMethods(List<Method> methods, List<Site> parameterArguments) {

		List<Method> ret = new ArrayList<>();
		for (Method method : methods) {

			List<Declaration> parametrisedArgumentDeclarations = new ArrayList<>();
			for (Declaration argumentDeclaration : method.getParameters()) {
				parametrisedArgumentDeclarations.add(
						new Declaration(
								argumentDeclaration.getName(),
								parametriseSite(argumentDeclaration.getType(), parameterArguments)
						)
				);
			}

			Site methodType = method.getType();
			ret.add(new Method(
					method.getName(),
					methodType != null ? parametriseSite(methodType, parameterArguments) : null,
					parametrisedArgumentDeclarations
			));

		}
		return ret;
	}

	private List<Operator> parametriseOperators(List<Operator> operators, List<Site> parameterArguments) {

		List<Operator> ret = new ArrayList<>();
		for (Operator operator : operators) {
			ret.add(new Operator(
					operator.getName(),
					parametriseSite(operator.getType(), parameterArguments),
					parametriseSite(operator.getParameter(), parameterArguments),
					operator.getMethodName()
			));

		}
		return ret;
	}

	private List<UnaryOperator> parametriseUnaryOperators(List<UnaryOperator> operators, List<Site> parameters) {
		List<UnaryOperator> ret = new ArrayList<>();
		for (UnaryOperator operator : operators) {
			ret.add(new UnaryOperator(
					operator.getName(),
					parametriseSite(operator.getType(), parameters),
					operator.getMethodName()
			));
		}
		return ret;
	}

	private <T extends Type> Site parametriseSite(Site<T> original, List<Site> parameterArguments) {

		Site ret = retrieveSiteWithName(original.getClassName(), parameterArguments);
		if (ret != null) {
			return ret;
		}

		List<Site> parametrisedArguments = new ArrayList<>();
		for (Site argument : parameterArguments) {
			parameterArguments.add(parametriseSite(argument, parameterArguments));
		}

		return new Site<T>(
				original.getClassName(),
				parametrisedArguments,
				true
		);
	}

	private Site retrieveSiteWithName(String name, List<Site> from) {
		for (Site site : from) {
			if (site.getClassName().equals(name)) {
				return site;
			}
		}
		return null;
	}

	public boolean isAssignableTo(Site t) {

		if (getClassName().equals(t.getClassName())) {
			Iterator<Declaration> i = t.getParameters().iterator();
			for (Declaration argument : getParameters()) {
				Declaration parameter = i.next();
				if (!argument.getType().isAssignableTo(parameter.getType())) {
					return false;
				}
			}
		}

		for (Site iface : getInterfaces()) {
			if (iface.isAssignableTo(t)) {
				return true;
			}
		}

		return false;
	}

	public Method getMethodWithName(String name) {
		for (Method method : methods) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		return null;
	}

	public UnaryOperator getUnaryOperatorWithName(String name) {
		for (UnaryOperator operator : unaryOperators) {
			if (operator.getName().equals(name)) {
				return operator;
			}
		}
		return null;
	}

	public Operator getOperatorWithName(String name) {
		for (Operator operator : operators) {
			if (operator.getName().equals(name)) {
				return operator;
			}
		}
		return null;
	}

	public String getClassName() {
		return className;
	}

	public List<Declaration> getParameters() {
		return parameters;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public List<Site> getInterfaces() {
		return interfaces;
	}

	public List<Operator> getOperators() {
		return operators;
	}

	public void setOperators(List<Operator> operators) {
		this.operators = operators;
	}

	public List<UnaryOperator> getUnaryOperators() {
		return unaryOperators;
	}

	public void setUnaryOperators(List<UnaryOperator> unaryOperators) {
		this.unaryOperators = unaryOperators;
	}

	public T getType() {
		return type;
	}

	public Boolean getErase() {
		return erase;
	}

	public String toString() {
		return className;
	}
}
