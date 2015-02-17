package bali.compiler.parser;

import bali.compiler.PackageDescription;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/**
 * User: Richard
 * Date: 13 Dec
 */
public interface ParserManager {
	List<ParserRuleContext> buildAbstractSyntaxTrees(List<PackageDescription> packageDescriptions);
}
