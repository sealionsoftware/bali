package bali.compiler.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 29/08/13
 */
public class ParametrizedSite implements Site {

	private Reference<Type> typeReference;
	private List<Site> typeArguments;

	private Type type;

	private Map<String, Declaration> typeParameters;
	private List<Site> interfaces;
	private List<Declaration> parameters;
	private List<Method> methods;
	private List<Operator> operators;
	private List<UnaryOperator> unaryOperators;
	private List<Declaration> properties;

	// Used by standard type infrastructure
	public ParametrizedSite(Type type, List<Site> typeArguments) {
		this.type = type;
		this.typeArguments = typeArguments;
	}

	// Used inside type construction (to avoid infinite loops)
	public ParametrizedSite(Reference<Type> typeReference, List<Site> typeArguments) {
		this.typeReference = typeReference;
		this.typeArguments = typeArguments;
	}

	private Map<String, Declaration> parametriseTypeDeclarations(List<Declaration> parameterDeclarations) {

		if (parameterDeclarations.size() != typeArguments.size()) {
			throw new RuntimeException("Invalid Parameterization: " + typeArguments + " => " + parameterDeclarations);
		}

		Map<String, Declaration> ret = new HashMap<>();
		Iterator<Site> i = typeArguments.iterator();
		for (Declaration declaration : parameterDeclarations) {
			Site site = i.next();
			ret.put(declaration.getName(),
					new Declaration(
						declaration.getName(),
						site
			));
		}

		return ret;
	}

	private List<Declaration> parametriseDeclarations(List<Declaration> parameterDeclarations) {

		List<Declaration> ret = new ArrayList<>(parameterDeclarations.size());
		for (Declaration declaration : parameterDeclarations) {
			Site site = typeParameters.get(declaration.getName()).getType();
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

	private List<Site> parametriseSites(List<Site> interfaces) {

		List<Site> ret = new ArrayList<>();
		for (Site iface : interfaces) {
			ret.add(parametriseSite(iface));
		}
		return ret;
	}

	private List<Method> parametriseMethods(List<Method> methods) {

		List<Method> ret = new ArrayList<>();
		for (Method method : methods) {

			List<Declaration> parametrisedArgumentDeclarations = new ArrayList<>();
			for (Declaration argumentDeclaration : method.getParameters()) {
				parametrisedArgumentDeclarations.add(
						new Declaration(
								argumentDeclaration.getName(),
								parametriseSite(argumentDeclaration.getType())
						)
				);
			}

			Site methodType = method.getType();
			ret.add(new Method(
					method.getName(),
					methodType != null ? parametriseSite(methodType) : null,
					parametrisedArgumentDeclarations
			));

		}
		return ret;
	}

	private List<Operator> parametriseOperators(List<Operator> operators) {

		List<Operator> ret = new ArrayList<>();
		for (Operator operator : operators) {
			Site returnType = operator.getType();
			ret.add(new Operator(
					operator.getName(),
					returnType != null ? parametriseSite(returnType) : null,
					parametriseSite(operator.getParameter()),
					operator.getMethodName()
			));

		}
		return ret;
	}

	private List<UnaryOperator> parametriseUnaryOperators(List<UnaryOperator> operators) {
		List<UnaryOperator> ret = new ArrayList<>();
		for (UnaryOperator operator : operators) {
			Site returnType = operator.getType();
			ret.add(new UnaryOperator(
					operator.getName(),
					returnType != null ? parametriseSite(returnType) : null,
					operator.getMethodName()
			));
		}
		return ret;
	}

	private Site parametriseSite(Site original) {

		if (original instanceof VariableSite){

			if (typeParameters == null){
				this.typeParameters = parametriseTypeDeclarations(getType().getTypeParameters());
			}

			Declaration ret = typeParameters.get(original.getName());
			if (ret != null) {
				return new ErasedSite(ret.getType(), (VariableSite) original);
			}
			return original;
		}


		List<Site> parametrisedArguments = new ArrayList<>();
		for (Declaration argument : original.getTypeParameters()) {
			parametrisedArguments.add(parametriseSite(argument.getType()));
		}

		return new ParametrizedSite(
				original.getType(),
				parametrisedArguments
		);
	}



	public boolean isAssignableTo(Site t) {

		if (t == null) {
			return true;
		}

		if (getName().equals(t.getName())) {
			Iterator<Declaration> i = t.getParameters().iterator();
			for (Declaration argument : getParameters()) {
				Declaration parameter = i.next();
				if (!argument.getType().isAssignableTo(parameter.getType())) {
					return false;
				}
			}
			return true;
		}

		for (Site iface : getInterfaces()) {
			if (iface.isAssignableTo(t)) {
				return true;
			}
		}

		return false;
	}

	public String getName() {
		return getType().getName();
	}

	public List<Declaration> getTypeParameters() {
		if (typeParameters == null){
			this.typeParameters = parametriseTypeDeclarations(getType().getTypeParameters());
		}
		return new ArrayList<>(typeParameters.values());
	}

	public List<Declaration> getParameters() {
		if (parameters == null){
			this.parameters = parametriseDeclarations(getType().getParameters());
		}
		return parameters;
	}

	public List<Method> getMethods() {
		if (methods == null){
			this.methods = parametriseMethods(getType().getMethods());
		}
		return methods;
	}

	public List<Site> getInterfaces() {
		if (interfaces == null){
			this.interfaces = getType().getInterfaces();
			this.interfaces = parametriseSites(getType().getInterfaces());
		}
		return interfaces;
	}

	public List<Operator> getOperators() {
		if (operators == null){
			this.operators = parametriseOperators(getType().getOperators());
		}
		return operators;
	}

	public List<UnaryOperator> getUnaryOperators() {
		if (unaryOperators == null){
			this.unaryOperators = parametriseUnaryOperators(getType().getUnaryOperators());
		}
		return unaryOperators;
	}

	public List<Declaration> getProperties() {
		if (properties == null){
			this.properties = parametriseDeclarations(getType().getProperties());
		}
		return properties;
	}

	public Type getType() {
		if (type == null){
			type = typeReference.get();
		}
		return type;
	}

	public String toString() {

		if(type == null){
			return "Not initialised";
		}

		if (typeArguments.isEmpty()){
			return type.getName();
		}

		Iterator<Site> i = typeArguments.iterator();
		StringBuilder sb = new StringBuilder(type.getName())
				.append("<")
				.append(i.next());
		while(i.hasNext()){
			sb.append(",")
					.append(i.next());
		}
		sb.append(">");
		return sb.toString();
	}
}
