package bali.compiler.processors;

import bali.compiler.GeneratedClass;
import bali.compiler.TreeProperty;
import bali.compiler.parser.BaliBaseVisitor;

public class ASMClassGenerator extends BaliBaseVisitor<Void> {

    private TreeProperty<GeneratedClass> generatedClass;

    public ASMClassGenerator(TreeProperty<GeneratedClass> generatedClass) {
       this.generatedClass = generatedClass;
    }
}
