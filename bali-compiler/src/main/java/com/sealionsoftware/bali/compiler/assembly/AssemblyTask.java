package com.sealionsoftware.bali.compiler.assembly;

public class AssemblyTask {

    public AssemblyTask(String name, Runnable runnable) {
        this.name = name;
        this.runnable = runnable;
    }

    public final String name;
    public final Runnable runnable;
}
