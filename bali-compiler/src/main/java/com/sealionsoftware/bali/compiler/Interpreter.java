package com.sealionsoftware.bali.compiler;

import java.util.Map;

public interface Interpreter {

    Map<String, Object> interpret(String fragment);

}
