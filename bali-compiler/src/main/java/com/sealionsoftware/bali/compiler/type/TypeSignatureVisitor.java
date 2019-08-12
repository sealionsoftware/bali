package com.sealionsoftware.bali.compiler.type;

import com.sealionsoftware.bali.compiler.Site;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeSignatureVisitor extends SignatureVisitor {

	private ClasspathClassFactory library;
	private Map<String, Site> typeVariableBounds;

	private Site type;

	private Class template;
	private List<TypeSignatureVisitor> typeArgumentVisitors = new ArrayList<>();

	public TypeSignatureVisitor(ClasspathClassFactory library, Map<String, Site> typeVariableBounds) {
		super(Opcodes.ASM7);
		this.library = library;
		this.typeVariableBounds = typeVariableBounds;
	}

	public void visitEnd() {

		if (template == null){
			return;
		}

        type = new Site(new ClassBasedType(
                template,
                typeArgumentVisitors
                        .stream()
                        .map(TypeSignatureVisitor::getSite)
                        .collect(Collectors.toList())
        ), false);

		super.visitEnd();
	}

	public void visitTypeVariable(String name) {
        Site bound = typeVariableBounds.get(name);
		type = bound != null ? new Site(new TypeVariable(name, bound.type), bound.isOptional) : new Site(new TypeVariable(name));
	}

	public void visitTypeArgument() {
		throw new RuntimeException("Unbounded site arguments are not supported");
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

	public Site getSite() {
		return type;
	}
}
