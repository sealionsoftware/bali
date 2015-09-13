package com.sealionsoftware.bali.compiler;

import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;

public interface ParseEngine {

    CodeBlockNode parse(String fragment);

}
