package bali.compiler.parser;

import bali.compiler.parser.tree.CompilationUnit;

import java.io.File;

/**
 * User: Richard
 * Date: 30/04/13
 */
public interface ParserManager {

	public CompilationUnit parse(File compilationUnit, String name) throws Exception;

}
