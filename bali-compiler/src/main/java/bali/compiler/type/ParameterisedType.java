package bali.compiler.type;

import bali.compiler.reference.Reference;
import bali.compiler.reference.SimpleReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

	private List<Type> directSuperTypes;

	private List<Type> superTypes;
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

	private synchronized void init(){

		if (initialised){
			return;
		}
		initialised = true;

		template = template_in.get();

		List<Declaration<Type>> typeParameters = template.getTypeParameters();
		if (typeArguments_in.size() != typeParameters.size()){
			throw new RuntimeException("Invalid Type Arguments " + typeArguments_in + " required: " + typeParameters);
		}

		this.typeArguments = new LinkedHashMap<>();
		Iterator<Declaration<Type>> i = template.getTypeParameters().iterator();
		for (Site typeArgument : typeArguments_in){
			Declaration<Type> typeParameter = i.next();
			if (typeArgument instanceof VariableSite){
				this.typeArguments.put(typeParameter.getName(), typeArgument);
			} else {
				Type bound = typeParameter.getType();
				this.typeArguments.put(typeParameter.getName(), new ErasedSite(typeArgument, bound != null ? bound : null));
			}
		}

		parametriseSuperTypes();
		parametriseInterfaces();
		parametriseParameters();
		parametriseMethods();
		parameteriseOperators();
		parameteriseUnaryOperators();
		parametriseProperties();
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
		if (input instanceof SelfSite){
			return new SelfSite(
					this instanceof Site ?
							(Site) this :
							new ParameterisedSite(template_in, typeArguments_in, false, false),
					input
			);
		}
		if (typeArguments.isEmpty()){
			return input;
		}
		List<Site> parameters = input.getTypeArguments();
		List<Site> newArguments = new ArrayList<>(parameters.size());
		for (Site original : parameters){
			newArguments.add(parametriseSite(original));
		}
		Site ret = new ParameterisedSite(new SimpleReference<>(input.getTemplate()), newArguments, input.isNullable(), input.isThreadSafe());
		if (input instanceof ErasedSite){
			return new ErasedSite(ret, ((ErasedSite) input).getErasure());
		}
		return ret;
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

	private void parametriseSuperTypes(){
		List<Type> ret = new ArrayList<>();
		List<Type> direct = new ArrayList<>();
		for(Type superType : template.getSuperTypes()){
			for(Type superSuperType : superType.getSuperTypes()){
				ret.add(parametriseType(superSuperType));
			}
			Type pType = parametriseType(superType);
			ret.add(pType);
			direct.add(pType);
		}
		this.superTypes = ret;
		this.directSuperTypes = direct;
	}

	private void parametriseInterfaces(){
		List<Type> ret = new ArrayList<>();
		for (Type superType : directSuperTypes){
			for (Type iface : superType.getInterfaces()){
				ret.add(parametriseType(iface));
			}
		}
		for (Type iface : template.getInterfaces()){
			ret.add(parametriseType(iface));
		}
		this.interfaces = ret;
	}

	private void parametriseParameters(){
		List<Declaration<Site>> ret = new ArrayList<>();
		for (Type superType : directSuperTypes){
			for (Declaration<Site> dec : superType.getParameters()){
				ret.add(parametriseDeclarationSite(dec));
			}
		}
		for (Declaration<Site> dec : template.getParameters()){
			ret.add(parametriseDeclarationSite(dec));
		}
		this.parameters = ret;
	}

	private void parametriseProperties(){
		List<Declaration<Site>> ret = new ArrayList<>();
		for (Type superType : directSuperTypes){
			for (Declaration<Site> dec : superType.getProperties()){
				ret.add(parametriseDeclarationSite(dec));
			}
		}
		for (Declaration<Site> dec : template.getProperties()){
			ret.add(parametriseDeclarationSite(dec));
		}
		this.properties = ret;
	}

	private List<Declaration<Site>> parametriseDeclarationSites(List<Declaration<Site>> input){
		List<Declaration<Site>> ret = new ArrayList<>(input.size());
		for (Declaration<Site> dec : input){
			ret.add(parametriseDeclarationSite(dec));
		}
		return ret;
	}

	private void parametriseMethods() {
		List<Method> ret = new ArrayList<>();
		for (Type superType : directSuperTypes){
			for (Method dec : superType.getMethods()){
				ret.add(parametriseMethod(dec));
			}
		}
		for (Method dec : template.getMethods()){
			ret.add(parametriseMethod(dec));
		}
		this.methods = ret;
	}

	private void parameteriseOperators() {
		List<Operator> ret = new ArrayList<>();
		for(Type superType : directSuperTypes){
			for (Operator dec : superType.getOperators()){
				ret.add(parametriseOperator(dec));
			}
		}
		for (Operator dec : template.getOperators()){
			ret.add(parametriseOperator(dec));
		}
		this.operators = ret;
	}

	private void parameteriseUnaryOperators() {
		List<UnaryOperator> ret = new ArrayList<>();
		for(Type superType : directSuperTypes){
			for (UnaryOperator dec : superType.getUnaryOperators()){
				ret.add(parametriseUnaryOperator(dec));
			}
		}
		for (UnaryOperator dec : template.getUnaryOperators()){
			ret.add(parametriseUnaryOperator(dec));
		}
		this.unaryOperators = ret;
	}

	public boolean isAssignableTo(Type t) {

		init();

		if (t == null) {
			return true;
		}

		if (template.getName().equals(t.getTemplate().getName())) {
			Iterator<Site> i = t.getTypeArguments().iterator();
			for (Site argument : typeArguments.values()) {
				if (!i.hasNext()){
					return false;
				}
				Site parameter = i.next();
				if (!argument.isAssignableTo(parameter)) {
					return false;
				}
			}
			return true;
		}

		for (Type superType : superTypes) {
			if (superType.isAssignableTo(t)) {
				return true;
			}
		}

		for (Type iface : interfaces) {
			if (iface.isAssignableTo(t)) {
				return true;
			}
		}

		return false;
	}

	public List<Type> getSuperTypes() {
		init();
		return superTypes;
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
		if (typeArguments_in.size() > 0){
			sb.append("[");
			Iterator<Site> i = typeArguments_in.iterator();
			sb.append(toString(i.next()));
			while(i.hasNext()){
				sb.append(",").append(toString(i.next()));
			}
			sb.append("]");
		}
		return sb.toString();
	}

	public String toString(Site in){
		return in == null ? "?" : in.toString();
	}
}
