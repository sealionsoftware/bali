package bali.compiler.processors;

import bali.compiler.GeneratedPackage;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/**
 * User: Richard
 * Date: 13 Dec
 */
public interface CompilationExecutor {

	public List<GeneratedPackage> compile(List<ParserRuleContext> abstractSyntaxTree);
}
