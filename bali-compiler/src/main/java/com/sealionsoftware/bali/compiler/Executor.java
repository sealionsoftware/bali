package com.sealionsoftware.bali.compiler;

import java.util.Map;

public interface Executor {

    Map<String, Object> executeFragment(GeneratedPackage generated);

    Object executeExpression(GeneratedPackage generated);

}
