package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Site;
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
    private Map<String, Site> typeVariableBounds;

    public MethodSignatureVisitor(ClasspathClassFactory library, Map<String, Site> typeVariableBounds) {
        super(Opcodes.ASM7);
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

    public Site getReturnType() {
        return returnTypeVisitor.getSite();
    }

    public List<Site> getParameterTypes() {
        return parameterVisitors.stream().map(TypeSignatureVisitor::getSite).collect(Collectors.toList());
    }
}
