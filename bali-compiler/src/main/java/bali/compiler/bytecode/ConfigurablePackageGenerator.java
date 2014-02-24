package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.GeneratedPackage;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.InterfaceNode;
import bali.compiler.parser.tree.ObjectNode;
import bali.compiler.parser.tree.RunStatementNode;

/**
 * User: Richard
 * Date: 30/04/13
 */
public class ConfigurablePackageGenerator implements Generator<CompilationUnitNode, GeneratedPackage>{

	private Generator<CompilationUnitNode, GeneratedClass> packageClassGenerator;
	private Generator<BeanNode, GeneratedClass> beanGenerator;
	private Generator<InterfaceNode, GeneratedClass> interfaceGenerator;
	private Generator<ObjectNode, GeneratedClass> classGenerator;
	private Generator<RunStatementNode, GeneratedClass> runStatementGenerator;

	public ConfigurablePackageGenerator(
			Generator<CompilationUnitNode, GeneratedClass> packageClassGenerator,
			Generator<BeanNode, GeneratedClass> beanGenerator,
			Generator<InterfaceNode, GeneratedClass> interfaceGenerator,
			Generator<ObjectNode, GeneratedClass> classGenerator,
	        Generator<RunStatementNode, GeneratedClass> runStatementGenerator
	) {
		this.packageClassGenerator = packageClassGenerator;
		this.beanGenerator = beanGenerator;
		this.interfaceGenerator = interfaceGenerator;
		this.classGenerator = classGenerator;
		this.runStatementGenerator = runStatementGenerator;
	}

	public GeneratedPackage build(CompilationUnitNode compilationUnit) throws Exception {

		GeneratedPackage pkg = new GeneratedPackage(compilationUnit.getName());

		pkg.addClass(packageClassGenerator.build(compilationUnit));

		for (BeanNode bean : compilationUnit.getBeans()){
			pkg.addClass(beanGenerator.build(bean));
		}

		for (InterfaceNode iface : compilationUnit.getInterfaces()){
			pkg.addClass(interfaceGenerator.build(iface));
		}

		for (ObjectNode clazz : compilationUnit.getClasses()){
			pkg.addClass(classGenerator.build(clazz));
		}

		for (RunStatementNode runStatementNode : compilationUnit.getRunStatements()){
			pkg.addClass(runStatementGenerator.build(runStatementNode));
		}

		return pkg;
	}
}
