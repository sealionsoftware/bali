package com.sealionsoftware.bali.compiler.assembly;

import bali.Logic;
import com.sealionsoftware.bali.compiler.Site;
import com.sealionsoftware.bali.compiler.type.Class;
import com.sealionsoftware.bali.compiler.type.ClassBasedType;
import com.sealionsoftware.bali.compiler.type.ClasspathClassFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InterpreterAssemblySetFactory implements AssemblerSetFactory {

    public Collection<ValidatingVisitor> assemblers(Map<String, java.lang.Class> externalReferences){

        Map<String, Class> library = new HashMap<>();
        ClasspathClassFactory classFactory = new ClasspathClassFactory(library, Thread.currentThread().getContextClassLoader());
        classFactory.addToLibrary(Logic.class);
        classFactory.addToLibrary(bali.Text.class);
        classFactory.addToLibrary(bali.Integer.class);
        classFactory.addToLibrary(bali.Iterable.class);

        Scope externalScope = new Scope();
        for (Map.Entry<String, java.lang.Class> externalReferenceTypeEntry : externalReferences.entrySet()){
            java.lang.Class clazz = externalReferenceTypeEntry.getValue();
            classFactory.addToLibrary(clazz);
            externalScope.add(new FieldData(externalReferenceTypeEntry.getKey(), new Site(new ClassBasedType(library.get(clazz.getName())))));
        }

        return Arrays.asList(
                new TypeAssigningVisitor(library),
                new TypeCheckVisitor(library),
                new ReferenceMatchingVisitor(externalScope),
                new InvocationMethodResolver(),
                new RequiredVariableVisitor()
        );
    }
}
