package com.sealionsoftware.bali.compiler.assembly;

import java.util.Collection;
import java.util.Map;

public interface AssemblerSetFactory {

    Collection<ValidatingVisitor> assemblers(Map<String, Class> externalReferences);
}
