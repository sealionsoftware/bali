package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.type.Class;
import com.sealionsoftware.bali.compiler.type.ClasspathClassFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InterpreterAssemblySetFactory implements AssemblerSetFactory {

    public Collection<ValidatingVisitor> assemblers(){

        Map<String, Class> library = new HashMap<>();
        ClasspathClassFactory classFactory = new ClasspathClassFactory(library, Thread.currentThread().getContextClassLoader());
        classFactory.addToLibrary(bali.Boolean.class);
        classFactory.addToLibrary(bali.Text.class);
        classFactory.addToLibrary(bali.Integer.class);

        return Arrays.<ValidatingVisitor>asList(
                new TypeAssigningVisitor(library),
                new TypeCheckVisitor(library),
                new ReferenceMatchingVisitor(),
                new InvocationMethodResolver()
        );
    }
}
