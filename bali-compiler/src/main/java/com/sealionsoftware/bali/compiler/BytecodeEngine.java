package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;

public interface BytecodeEngine {

    GeneratedPackage generate(CodeBlockNode fragment);

}
