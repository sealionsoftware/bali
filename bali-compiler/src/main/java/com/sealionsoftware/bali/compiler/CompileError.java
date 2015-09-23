package com.sealionsoftware.bali.compiler;

public class CompileError {

    public final ErrorCode code;
    public final Integer line;
    public final Integer character;
    public final Integer length;

    public CompileError(ErrorCode code, Integer line, Integer character, Integer length) {
        this.code = code;
        this.line = line;
        this.character = character;
        this.length = length;
    }
}
