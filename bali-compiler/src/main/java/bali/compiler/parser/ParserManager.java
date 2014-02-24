package bali.compiler.parser;

import bali.compiler.parser.tree.CompilationUnitNode;

import java.io.InputStream;

/**
 * User: Richard
 * Date: 30/04/13
 */
public interface ParserManager {

	public CompilationUnitNode parse(InputStream compilationUnit, String name) throws Exception;

}
