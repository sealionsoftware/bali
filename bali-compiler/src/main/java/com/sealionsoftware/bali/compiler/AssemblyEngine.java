package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.Node;

import java.util.Map;

public interface AssemblyEngine {

    void assemble(Node fragment);

    void assemble(Node fragment, Map<String, Class> externalReferences);

}
