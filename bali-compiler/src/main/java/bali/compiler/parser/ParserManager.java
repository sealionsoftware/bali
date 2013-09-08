package bali.compiler.parser;

import bali.compiler.parser.tree.CompilationUnitNode;

import java.io.File;

/**
 * User: Richard
 * Date: 30/04/13
 */
public interface ParserManager {

	public CompilationUnitNode parse(File compilationUnit, String name) throws Exception;

}
