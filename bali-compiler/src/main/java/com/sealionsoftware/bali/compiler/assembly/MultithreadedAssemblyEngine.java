package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.AssemblyEngine;
import com.sealionsoftware.bali.compiler.Class;
import com.sealionsoftware.bali.compiler.ClasspathClassFactory;
import com.sealionsoftware.bali.compiler.CompilationException;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MultithreadedAssemblyEngine implements AssemblyEngine {

    public void assemble(CodeBlockNode fragment) {
        for (ValidatingVisitor assembler : assemblers()){
            fragment.accept(assembler);
            if (!assembler.getFailures().isEmpty()){
                throw new CompilationException(assembler.getFailures());
            }
        }
    }

    private Collection<ValidatingVisitor> assemblers(){

        Map<String, Class> library = new HashMap<>();
        ClasspathClassFactory classFactory = new ClasspathClassFactory(library);
        classFactory.addToLibrary(bali.Boolean.class);
        classFactory.addToLibrary(bali.Text.class);

        return Arrays.<ValidatingVisitor>asList(
                new TypeAssigningVisitor(library),
                new TypeCheckVisitor()
        );
    }
}
