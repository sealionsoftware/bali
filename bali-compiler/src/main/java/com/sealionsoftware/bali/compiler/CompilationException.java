package com.sealionsoftware.bali.compiler;

import java.util.List;

public class CompilationException extends RuntimeException {

    public List<CompileError> errorList;

    public CompilationException(List<CompileError> errorList) {
        super("Compilation Failed: " + errorList);
        this.errorList = errorList;
    }
}
