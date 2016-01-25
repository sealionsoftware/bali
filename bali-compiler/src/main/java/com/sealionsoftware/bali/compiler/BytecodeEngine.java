package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;

public interface BytecodeEngine {

    GeneratedPackage generate(CodeBlockNode fragment);

    GeneratedPackage generate(ExpressionNode expression);

}
