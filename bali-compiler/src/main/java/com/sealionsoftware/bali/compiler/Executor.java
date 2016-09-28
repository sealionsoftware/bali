package com.sealionsoftware.bali.compiler;

public interface Executor {

    void executeFragment(GeneratedPackage generated);

    Object executeExpression(GeneratedPackage generated);

}
