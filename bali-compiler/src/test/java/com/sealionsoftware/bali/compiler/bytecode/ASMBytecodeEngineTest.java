package com.sealionsoftware.bali.compiler.bytecode;

import com.sealionsoftware.bali.compiler.BytecodeEngine;
import com.sealionsoftware.bali.compiler.GeneratedClass;
import com.sealionsoftware.bali.compiler.GeneratedPackage;
import com.sealionsoftware.bali.compiler.Interpreter;
import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.*;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ASMBytecodeEngineTest {

    private CompilationThreadManager monitor = mock(com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager.class);
    private BytecodeEngine subject = new ASMBytecodeEngine();

    @Test
    public void testGenerateFragment() {

        CodeBlockNode node = new CodeBlockNode(0, 0);

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

    @Test
    public void testFragmentVariables() {

        VariableNode variableNode = new VariableNode(0, 0);
        variableNode.setName("aVariable");
        LogicLiteralNode valueNode = new LogicLiteralNode(0, 0, monitor);
        valueNode.setValue(true);
        variableNode.setValue(valueNode);
        CodeBlockNode node = new CodeBlockNode(0, 0);
        node.addStatement(variableNode);

        GeneratedPackage ret = subject.generate(node);

        assertThat(ret, notNullValue());
        List<GeneratedClass> generatedClasses = ret.getClasses();
        assertThat(generatedClasses, hasSize(1));

    }

    @Test
    public void testFragmentCatch() {

        TryStatementNode tryNode = new TryStatementNode(0, 0);
        tryNode.setCoveredStatement(mock(StatementNode.class));
        tryNode.setCaughtName("error");
        tryNode.setCaughtType(mock(TypeNode.class));
        tryNode.setCatchBlock(mock(StatementNode.class));

        CodeBlockNode node = new CodeBlockNode(0, 0);
        node.addStatement(tryNode);

        GeneratedPackage ret = subject.generate(node);

        assertThat(ret, notNullValue());
        List<GeneratedClass> generatedClasses = ret.getClasses();
        assertThat(generatedClasses, hasSize(1));

    }

    @Test
    public void testGenerateEvaluation() {

        LogicLiteralNode valueNode = new LogicLiteralNode(0, 0, monitor);
        valueNode.setValue(true);

        GeneratedPackage ret = subject.generate(valueNode);

        assertThat(ret, notNullValue());
        assertThat(ret.getName(), equalTo(""));

        List<GeneratedClass> generatedClasses = ret.getClasses();
        assertThat(generatedClasses, notNullValue());
        assertThat(generatedClasses, hasSize(1));

        GeneratedClass clazz = generatedClasses.get(0);
        assertThat(clazz, notNullValue());
        assertThat(clazz.getName(), equalTo(Interpreter.EVALUATION_CLASS_NAME));
        assertThat(clazz.getCode(), notNullValue());
    }

}