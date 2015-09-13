package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;

public interface AssemblyEngine {

    void assemble(CodeBlockNode fragment);

}
