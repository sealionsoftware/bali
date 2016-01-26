package com.sealionsoftware.bali.compiler;

import java.util.Map;

public interface Interpreter {

    String FRAGMENT_CLASS_NAME = "InterpretedFragment";
    String EVALUATION_CLASS_NAME = "InterpretedEvaluation";

    Map<String, Object> run(String fragment);

    Object evaluate(String expression);

}
