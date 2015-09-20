package com.sealionsoftware.bali.compiler.asm;

import com.sealionsoftware.bali.compiler.BytecodeEngine;
import com.sealionsoftware.bali.compiler.GeneratedClass;
import com.sealionsoftware.bali.compiler.GeneratedPackage;
import com.sealionsoftware.bali.compiler.Interpreter;
import com.sealionsoftware.bali.compiler.tree.BooleanLiteralNode;
import com.sealionsoftware.bali.compiler.tree.CodeBlockNode;
import com.sealionsoftware.bali.compiler.tree.VariableNode;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ASMBytecodeEngineTest {

    private BytecodeEngine subject = new ASMBytecodeEngine();

    @Test
    public void testGenerate() throws Exception {

        VariableNode variableNode = new VariableNode(0, 0);
        variableNode.setName("aVariable");
        BooleanLiteralNode valueNode = new BooleanLiteralNode(0,0);
        variableNode.setValue(valueNode);
        CodeBlockNode node = new CodeBlockNode(0, 0);
        node.addStatement(variableNode);

        GeneratedPackage ret = subject.generate(node);

        assertThat(ret, notNullValue());
        assertThat(ret.getName(), equalTo(""));

        List<GeneratedClass> generatedClasses = ret.getClasses();
        assertThat(generatedClasses, notNullValue());
        assertThat(generatedClasses, hasSize(1));

        GeneratedClass clazz = generatedClasses.get(0);
        assertThat(clazz, notNullValue());
        assertThat(clazz.getName(), equalTo(Interpreter.FRAGMENT_CLASS_NAME));
        assertThat(clazz.getCode(), notNullValue());
    }


}