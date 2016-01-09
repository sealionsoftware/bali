package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeSignatureVisitor extends SignatureVisitor {

	private ClasspathClassFactory library;
	private Map<String, Type> typeVariableBounds;

	private Type type;

	private Class template;
	private List<TypeSignatureVisitor> typeArgumentVisitors = new ArrayList<>();

	public TypeSignatureVisitor(ClasspathClassFactory library, Map<String, Type> typeVariableBounds) {
		super(Opcodes.ASM5);
		this.library = library;
		this.typeVariableBounds = typeVariableBounds;
	}

	public void visitEnd() {

		if (template == null){
			return;
		}

        type = new ClassBasedType(
                template,
                typeArgumentVisitors
                        .stream()
                        .map(TypeSignatureVisitor::getType)
                        .collect(Collectors.toList())
        );

		super.visitEnd();
	}

	public void visitTypeVariable(String name) {
		type = new TypeVariable(name, typeVariableBounds.get(name));
	}

	public void visitTypeArgument() {
		throw new RuntimeException("Unbounded type arguments are not supported");
	}

	public SignatureVisitor visitTypeArgument(char wildcard) {

        if (wildcard != '='){
            throw new RuntimeException("Contra/Covariant types are not supported");
        }

		TypeSignatureVisitor visitor = new TypeSignatureVisitor(library, typeVariableBounds);
		typeArgumentVisitors.add(visitor);
		return visitor;
	}

	public void visitClassType(String name) {

        String canonicalName = name.replaceAll("/", ".");
        if (canonicalName.equals(Object.class.getName())){
            return;
        }

		template = library.get(canonicalName);
	}

	public Type getType() {
		return type;
	}
}
