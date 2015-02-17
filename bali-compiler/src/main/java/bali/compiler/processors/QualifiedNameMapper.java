package bali.compiler.processors;

import bali.compiler.TreeProperty;
import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 15 Dec
 */
public class QualifiedNameMapper extends BaliBaseVisitor<Void> {

	private TreeProperty<Map<String, String>> mapTreeProperty;
	private Map<String, String> qualifiedNames;

	public QualifiedNameMapper(TreeProperty<Map<String, String>> mapTreeProperty) {
		this.mapTreeProperty = mapTreeProperty;
	}

	public Void visitImportDeclaration(BaliParser.ImportDeclarationContext ctx){
        TerminalNode alias = ctx.IDENTIFIER();
        List<TerminalNode> qualifiedNameTokens = ctx.qualifiedName().IDENTIFIER();

        String typeName = alias != null ? alias.getText() : qualifiedNameTokens.get(qualifiedNameTokens.size() - 1).getText();
		qualifiedNames.put(typeName, ctx.qualifiedName().getText());
        return visitChildren(ctx);
	}

	public Void visitPackageDeclaration(BaliParser.PackageDeclarationContext ctx){
        qualifiedNames = new HashMap<>();
		mapTreeProperty.set(ctx, qualifiedNames);
		return visitChildren(ctx);
	}

}
