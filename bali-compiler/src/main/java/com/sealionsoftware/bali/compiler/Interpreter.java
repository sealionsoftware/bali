package com.sealionsoftware.bali.compiler;


public interface Interpreter {

    String FRAGMENT_CLASS_NAME = "InterpretedFragment";
    String EVALUATION_CLASS_NAME = "InterpretedEvaluation";

    void run(String fragment);

    Object evaluate(String expression);

}
