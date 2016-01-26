package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.ExpressionNode;

public interface ParseEngine {

    CodeBlockNode parseFragment(String fragment);

    ExpressionNode parseExpression(String fragment);

}
