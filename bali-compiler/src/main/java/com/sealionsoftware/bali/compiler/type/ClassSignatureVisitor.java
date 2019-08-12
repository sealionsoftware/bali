package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Parameter;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassSignatureVisitor extends SignatureVisitor implements Opcodes {

    private ClasspathClassFactory library;

    private List<TypeSignatureVisitor> interfaceVisitors = new ArrayList<>();
    private TypeSignatureVisitor superClassVisitor;

    private Deque<TypeContext> typeParamStack = new LinkedList<>();
    private Map<String, Site> typeVariableBounds;


    public ClassSignatureVisitor(ClasspathClassFactory library, Map<String, Site> typeVariableBounds) {
        super(ASM7);
        this.library = library;
        this.typeVariableBounds = typeVariableBounds;
    }

    public void visitFormalTypeParameter(String name) {
        typeParamStack.push(new TypeContext(name));
    }

    public SignatureVisitor visitClassBound() {
        TypeSignatureVisitor typeParameterVisitor = new TypeSignatureVisitor(library, typeVariableBounds);
        typeParamStack.peek().typeVisitor = typeParameterVisitor;
        return typeParameterVisitor;
    }

    public SignatureVisitor visitInterfaceBound() {
        return visitClassBound();
    }

    public SignatureVisitor visitSuperclass() {
        superClassVisitor = new TypeSignatureVisitor(library, typeVariableBounds);
        return superClassVisitor;
    }

    public SignatureVisitor visitInterface() {
        TypeSignatureVisitor interfaceVisitor = new TypeSignatureVisitor(library, typeVariableBounds);
        interfaceVisitors.add(interfaceVisitor);
        return interfaceVisitor;
    }

    public List<Parameter> getTypeParameters() {
        return typeParamStack.stream().map((context) -> new Parameter(context.name, context.typeVisitor.getSite())).collect(Collectors.toList());
    }

    public List<Type> getInterfaces() {
        return interfaceVisitors.stream().map((item) -> item.getSite().type).collect(Collectors.toList());

    }

    public Type getSuperType() {
        return superClassVisitor != null ? superClassVisitor.getSite() != null ? superClassVisitor.getSite().type : null : null;
    }

}
