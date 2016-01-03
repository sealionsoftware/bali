package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MethodSignatureVisitor extends SignatureVisitor {

    private ClasspathClassFactory library;

    private TypeSignatureVisitor returnTypeVisitor ;
    private List<TypeSignatureVisitor> parameterVisitors = new ArrayList<>();
    private Map<String, Type> typeVariableBounds;

    public MethodSignatureVisitor(ClasspathClassFactory library, Map<String, Type> typeVariableBounds) {
        super(Opcodes.ASM5);
        this.library = library;
        this.typeVariableBounds = typeVariableBounds;
    }

    public SignatureVisitor visitReturnType() {
        returnTypeVisitor = new TypeSignatureVisitor(library, typeVariableBounds);
        return returnTypeVisitor;
    }

    public SignatureVisitor visitParameterType() {
        TypeSignatureVisitor visitor = new TypeSignatureVisitor(library, typeVariableBounds);
        parameterVisitors.add(visitor);
        return visitor;
    }

    public Type getReturnType() {
        return returnTypeVisitor.getType();
    }

    public List<Type> getParameterTypes() {
        return parameterVisitors.stream().map(TypeSignatureVisitor::getType).collect(Collectors.toList());
    }
}
