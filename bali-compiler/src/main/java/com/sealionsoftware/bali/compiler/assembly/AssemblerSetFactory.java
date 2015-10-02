package com.sealionsoftware.bali.compiler.assembly;

import java.util.Collection;

public interface AssemblerSetFactory {

    Collection<ValidatingVisitor> assemblers();
}
