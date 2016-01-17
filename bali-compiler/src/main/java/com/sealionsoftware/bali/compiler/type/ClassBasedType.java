package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.Collections.Each;
import com.sealionsoftware.bali.compiler.Method;
import com.sealionsoftware.bali.compiler.Operator;
import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sealionsoftware.Collections.both;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class ClassBasedType implements Type {

    private Class template;
    private List<Type> typeArguments;

    private Map<String, Type> typeArgumentMap;

    private boolean superTypeInitialised = false;
    private Type superType;

    private List<Type> interfaces;
    private Map<String, Method> methods;
    private Map<String, Operator> operators;
    private Map<String, Operator> unaryOperators;

    public ClassBasedType(Class template) {
        this(template, emptyList());
    }

    public ClassBasedType(Class template, List<Type> typeArguments) {
        this.template = template;
        this.typeArguments = typeArguments;
    }

    public String getClassName() {
        return template.getClassName();
    }

    public Class getTemplate() {
        return template;
    }

    public Type getSuperType() {
        if (!superTypeInitialised){
            superTypeInitialised = true;
            superType = parameterise(template.getSuperType());
        }
        return superType;
    }

    public List<Type> getInterfaces() {
        if (interfaces == null){
            initialiseInterfaces();
        }
        return interfaces;
    }

    private void initialiseInterfaces(){
        List<Type> interfaces = new ArrayList<>();
        for (Type rawInterface : template.getInterfaces()){
            interfaces.add(parameterise(rawInterface));
        }
        this.interfaces = interfaces;
    }

    public List<Method> getMethods() {
        if (methods == null){
            initaliseMethods();
        }
        return unmodifiableList(new ArrayList<>(methods.values()));
    }

    public Method getMethod(String name) {
        if (methods == null){
            initaliseMethods();
        }
        return methods.get(name);
    }

    private void initaliseMethods() {
        Map<String, Method> methods = new HashMap<>();

        List<Method> rawMethods = new ArrayList<>();
        rawMethods.addAll(template.getMethods());
        for (Type iface : getInterfaces()){
            rawMethods.addAll(iface.getMethods());
        }

        for (Method rawMethod : rawMethods){
            List<Parameter> rawParameters = rawMethod.getParameters();
            List<Parameter> parameterisedParameters = new ArrayList<>(rawParameters.size());
            for (Parameter rawParameter : rawParameters){
                parameterisedParameters.add(new Parameter(rawParameter.name, parameterise(rawParameter.type)));
            }
            methods.put(
                    rawMethod.getName(),
                    new Method(
                            rawMethod.getName(),
                            parameterise(rawMethod.getReturnType()),
                            parameterisedParameters,
                            getUltimateTemplate(rawMethod)
                    )
            );
        }
        this.methods = methods;
    }

    private Method getUltimateTemplate(Method raw){
        Method lowerTemplate = raw.getTemplateMethod();
        return lowerTemplate == null ? raw : getUltimateTemplate(lowerTemplate);
    }

    public List<Parameter> getTypeArguments() {
        if (typeArgumentMap == null){
            initaliseTypeArguments();
        }
        return typeArgumentMap
                .entrySet()
                .stream()
                .map((entry) -> new Parameter(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private void initaliseTypeArguments() {
        typeArgumentMap = new HashMap<>();
        for (Each<Parameter, Type> item : both(template.getTypeParameters(), typeArguments)){
            typeArgumentMap.put(item.i.name, item.j);
        }
    }

    public Boolean isAssignableTo(Type other) {

        if (other == null || other == this) {
            return true;
        }

        if (typeArgumentMap == null){
            initaliseTypeArguments();
        }

        if (template.getClassName().equals(other.getClassName())) {
            for (Parameter argument : other.getTypeArguments()){
                Type typeArgument = typeArgumentMap.get(argument.name);
                if (!typeArgument.isAssignableTo(argument.type)) {
                    return false;
                }
            }
            return true;
        }

        Type superType = getSuperType();
        if (superType != null && superType.isAssignableTo(other)) {
            return true;
        }

        for (Type iface : getInterfaces()) if (iface.isAssignableTo(other)) {
            return true;
        }

        return false;
    }

    public List<Operator> getOperators() {
        if (operators == null){
            initaliseOperators();
        }
        return unmodifiableList(new ArrayList<>(operators.values()));
    }

    public Operator getOperator(String name) {
        if (operators == null){
            initaliseOperators();
        }
        return operators.get(name);
    }

    public List<Operator> getUnaryOperators() {
        if (unaryOperators == null){
            initaliseUnaryOperators();
        }
        return unmodifiableList(new ArrayList<>(unaryOperators.values()));
    }

    public Operator getUnaryOperator(String name) {
        if (unaryOperators == null){
            initaliseUnaryOperators();
        }
        return unaryOperators.get(name);
    }

    private void initaliseOperators() {
        List<Operator> rawOperators = new ArrayList<>();
        rawOperators.addAll(template.getOperators());
        for (Type iface : getInterfaces()) {
            rawOperators.addAll(iface.getOperators());
        }

        this.operators = parameteriseOperators(rawOperators);
    }

    private  Map<String, Operator> parameteriseOperators(List<Operator> rawOperators) {
        Map<String, Operator> operators = new HashMap<>();

        for (Operator rawOperator : rawOperators) {
            List<Parameter> rawParameters = rawOperator.getParameters();
            List<Parameter> parameterisedParameters = new ArrayList<>(rawParameters.size());
            for (Parameter rawParameter : rawParameters) {
                parameterisedParameters.add(new Parameter(rawParameter.name, parameterise(rawParameter.type)));
            }
            operators.put(
                    rawOperator.getSymbol(),
                    new Operator(
                            rawOperator.getName(),
                            parameterise(rawOperator.getReturnType()),
                            parameterisedParameters,
                            rawOperator.getSymbol(),
                            getUltimateTemplate(rawOperator)
                    )
            );
        }
        return operators;
    }

    private void initaliseUnaryOperators() {

        List<Operator> rawOperators = new ArrayList<>();
        rawOperators.addAll(template.getUnaryOperators());
        for (Type iface : getInterfaces()) {
            rawOperators.addAll(iface.getUnaryOperators());
        }

        this.unaryOperators = parameteriseOperators(rawOperators);
    }

    private Type parameterise(Type raw){

        if (raw == null){
            return null;
        }

        if (raw instanceof TypeVariable){

            if (typeArgumentMap == null){
                initaliseTypeArguments();
            }

            TypeVariable vt = (TypeVariable) raw;
            return typeArgumentMap.get(vt.getName());
        }

        List<Parameter> arguments = raw.getTypeArguments();

        if (arguments.isEmpty()){
            return raw;
        }

        List<Type> newArguments = new ArrayList<>(arguments.size());
        for (Parameter argument : arguments){
            newArguments.add(parameterise(argument.type));
        }

        return new ClassBasedType(raw.getTemplate(), newArguments);
    }

    public String toString() {

        StringBuilder builder = new StringBuilder(template.getClassName());
        if (typeArguments != null && !typeArguments.isEmpty()){
            builder.append("<");
            Iterator<Type> i = typeArguments.iterator();
            builder.append(i.next());
            while (i.hasNext()){
                builder.append(", ").append(i.next());
            }
            builder.append(">");
        }
        return builder.toString();
    }
}
