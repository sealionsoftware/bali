package com.sealionsoftware.bali.compiler;

import java.util.Map;

public interface Interpreter {

    String FRAGMENT_CLASS_NAME = "InterpretedFragment";

    Map<String, Object> run(String fragment);

}
