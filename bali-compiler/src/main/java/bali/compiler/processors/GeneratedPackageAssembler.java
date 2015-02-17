package bali.compiler.processors;

import bali.compiler.GeneratedPackage;
import bali.compiler.TreeProperty;
import bali.compiler.parser.BaliBaseVisitor;
import bali.compiler.parser.BaliParser;

import java.util.List;

public class GeneratedPackageAssembler extends BaliBaseVisitor<Void> {

    private TreeProperty<GeneratedPackage> generatedPackageProperty;
    private List<GeneratedPackage> compiledPackages;
    private GeneratedPackage generatedPackage;

    public GeneratedPackageAssembler(List<GeneratedPackage> compiledPackages, TreeProperty<GeneratedPackage> generatedPackage) {
        this.generatedPackageProperty = generatedPackage;
        this.compiledPackages = compiledPackages;
    }

    @Override
    public Void visitPackageDeclaration(BaliParser.PackageDeclarationContext ctx) {
        generatedPackage = new GeneratedPackage(ctx.);

        super.visitPackageDeclaration(ctx);
    }

    @Override
    public Void visitInterfaceDeclaration(BaliParser.InterfaceDeclarationContext ctx) {



        return super.visitInterfaceDeclaration(ctx);
    }

    @Override
    public Void visitObjectDeclaration(BaliParser.ObjectDeclarationContext ctx) {
        return super.visitObjectDeclaration(ctx);
    }

    @Override
    public Void visitBeanDeclaration(BaliParser.BeanDeclarationContext ctx) {
        return super.visitBeanDeclaration(ctx);
    }
}
