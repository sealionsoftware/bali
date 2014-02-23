package bali.compiler.type;

import bali.compiler.reference.Reference;
import bali.compiler.reference.SimpleReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 12/02/14
 */
public class ParameterisedType implements Type {

	private Reference<Class> template_in;
	private List<Site> typeArguments_in;

	private Class template;
	private Map<String, Site> typeArguments;

	private boolean initialised = false;

	private Type superType;
	private List<Type> interfaces;
	private List<Declaration<Site>> parameters;
	private List<Method> methods;
	private List<Operator> operators;
	private List<UnaryOperator> unaryOperators;
	private List<Declaration<Site>> properties;

	public ParameterisedType(Reference<Class> template) {
		this(template, Collections.<Site>emptyList());
	}

	public ParameterisedType(Reference<Class> template, List<Site> typeArguments) {
		this.template_in = template;
		this.typeArguments_in = typeArguments;
	}

	private void init(){

		if (initialised){
			return;
		}
		initialised = true;

		template = template_in.get();

		List<Declaration<Type>> typeParameters = template.getTypeParameters();
		if (typeArguments_in.size() != typeParameters.size()){
			throw new RuntimeException("Invalid Type Arguments " + typeArguments + " required: " + typeParameters);
		}

		this.typeArguments = new LinkedHashMap<>();
		Iterator<Declaration<Type>> i = template.getTypeParameters().iterator();
		for (Site typeArgument : typeArguments_in){
			Declaration<Type> typeParameter = i.next();
			this.typeArguments.put(typeParameter.getName(), typeArgument);
		}

		if (typeArguments.size() > 0){

			if (template.getSuperType() != null){
				superType = parametriseType(template.getSuperType());
			}
			interfaces = parametriseTypes(template.getInterfaces());
			parameters =  parametriseDeclarationSites(template.getParameters());
			methods = parametriseMethods(template.getMethods());
			operators = parameteriseOperators(template.getOperators());
			unaryOperators = parameteriseUnaryOperators(template.getUnaryOperators());
			properties = parametriseDeclarationSites(template.getProperties());
		} else {
			if (template.getSuperType() != null){
				superType = template.getSuperType();
			}
			interfaces = template.getInterfaces();
			parameters =  template.getParameters();
			methods = template.getMethods();
			operators = template.getOperators();
			unaryOperators = template.getUnaryOperators();
			properties = template.getProperties();
		}
	}

	private Type parametriseType(Type input){
		if (input == null){
			return null;
		}
		if (input instanceof VariableType){
			VariableType vt = (VariableType) input;
			return typeArguments.get(vt.getName());
		}
		List<Site> oldArguments = input.getTypeArguments();
		List<Site> newArguments = new ArrayList<>(oldArguments.size());
		for (Site original : oldArguments){
			newArguments.add(parametriseSite(original));
		}
		return new ParameterisedType(new SimpleReference<>(input.getTemplate()), newArguments);
	}

	private Site parametriseSite(Site input){
		if (input == null){
			return null;
		}
		if (input instanceof VariableType){
			VariableType vt = (VariableType) input;
			return typeArguments.get(vt.getName());
		}
		List<Site> parameters = input.getTypeArguments();
		List<Site> newArguments = new ArrayList<>(parameters.size());
		for (Site original : parameters){
			newArguments.add(parametriseSite(original));
		}
		return new ParameterisedSite(new SimpleReference<>(input.getTemplate()), newArguments, input.isNullable(), input.isThreadSafe());
	}

	private Declaration<Type> parametriseDeclarationType(Declaration<Type> input){
		return new Declaration<>(input.getName(), parametriseType(input.getType()));
	}

	private Declaration<Site> parametriseDeclarationSite(Declaration<Site> input){
		return new Declaration<>(input.getName(), parametriseSite(input.getType()));
	}

	private Method parametriseMethod(Method input){
		return new Method(input.getName(), parametriseSite(input.getType()), parametriseDeclarationSites(input.getParameters()));
	}

	private Operator parametriseOperator(Operator input){
		return new Operator(input.getName(), parametriseSite(input.getType()), parametriseSite(input.getParameter()), input.getMethodName());
	}

	private UnaryOperator parametriseUnaryOperator(UnaryOperator input){
		return new UnaryOperator(input.getName(), parametriseSite(input.getType()), input.getMethodName());
	}

	private List<Type> parametriseTypes(List<Type> input){
		List<Type> ret = new ArrayList<>(input.size());
		for (Type type : input){
			ret.add(parametriseType(type));
		}
		return ret;
	}

	private List<Site> parametriseSites(List<Site> input){
		List<Site> ret = new ArrayList<>(input.size());
		for (Site type : input){
			ret.add(parametriseSite(type));
		}
		return ret;
	}

	private List<Declaration<Type>> parametriseDeclarationTypes(List<Declaration<Type>> input){
		List<Declaration<Type>> ret = new ArrayList<>(input.size());
		for (Declaration<Type> dec : input){
			ret.add(parametriseDeclarationType(dec));
		}
		return ret;
	}

	private List<Declaration<Site>> parametriseDeclarationSites(List<Declaration<Site>> input){
		List<Declaration<Site>> ret = new ArrayList<>(input.size());
		for (Declaration<Site> dec : input){
			ret.add(parametriseDeclarationSite(dec));
		}
		return ret;
	}

	private List<Method> parametriseMethods(List<Method> input) {
		List<Method> ret = new ArrayList<>(input.size());
		for (Method dec : input){
			ret.add(parametriseMethod(dec));
		}
		return ret;
	}

	private List<Operator> parameteriseOperators(List<Operator> input) {
		List<Operator> ret = new ArrayList<>(input.size());
		for (Operator dec : input){
			ret.add(parametriseOperator(dec));
		}
		return ret;
	}

	private List<UnaryOperator> parameteriseUnaryOperators(List<UnaryOperator> input) {
		List<UnaryOperator> ret = new ArrayList<>(input.size());
		for (UnaryOperator dec : input){
			ret.add(parametriseUnaryOperator(dec));
		}
		return ret;
	}

	public boolean isAssignableTo(Type t) {

		init();

		if (t == null) {
			return true;
		}

		if (template.getName().equals(t.getTemplate().getName())) {
			Iterator<Site> i = t.getTypeArguments().iterator();
			for (Site argument : typeArguments.values()) {
				Site parameter = i.next();
				if (!argument.isAssignableTo(parameter)) {
					return false;
				}
			}
			return true;
		}

		if (superType != null && getSuperType().isAssignableTo(t)){
			return true;
		}

		for (Type iface : interfaces) {
			if (iface.isAssignableTo(t)) {
				return true;
			}
		}

		return false;
	}

	public Type getSuperType() {
		init();
		return superType;
	}

	public List<Site> getTypeArguments() {
		return typeArguments_in;
	}

	public List<Declaration<Site>> getParameters() {
		init();
		return parameters;
	}

	public List<Method> getMethods() {
		init();
		return methods;
	}

	public List<Type> getInterfaces() {
		init();
		return interfaces;
	}

	public List<Operator> getOperators() {
		init();
		return operators;
	}

	public List<UnaryOperator> getUnaryOperators() {
		init();
		return unaryOperators;
	}

	public List<Declaration<Site>> getProperties() {
		init();
		return properties;
	}

	public Class getTemplate() {
		return template_in.get();
	}

	public String toString() {
		init();
		StringBuilder sb = new StringBuilder();
		sb.append(template.getName());
		if (typeArguments.size() > 0){
			sb.append("<");
			Iterator<Site> i = typeArguments.values().iterator();
			sb.append(i.next().getTemplate().getName());
			while(i.hasNext()){
				sb.append(",").append(i.next().getTemplate().getName());
			}
			sb.append(">");
		}
		return sb.toString();
	}
}
