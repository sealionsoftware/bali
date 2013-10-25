package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.GeneratedPackage;
import bali.compiler.parser.tree.BeanNode;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.InterfaceNode;

/**
 * User: Richard
 * Date: 30/04/13
 */
public class ConfigurablePackageGenerator implements Generator<CompilationUnitNode, GeneratedPackage>{

	private Generator<CompilationUnitNode, GeneratedClass> packageClassGenerator;
	private Generator<BeanNode, GeneratedClass> beanGenerator;
	private Generator<InterfaceNode, GeneratedClass> interfaceGenerator;
	private Generator<ClassNode, GeneratedClass> classGenerator;

	public ConfigurablePackageGenerator(Generator<CompilationUnitNode, GeneratedClass> packageClassGenerator, Generator<BeanNode, GeneratedClass> beanGenerator, Generator<InterfaceNode, GeneratedClass> interfaceGenerator, Generator<ClassNode, GeneratedClass> classGenerator) {
		this.packageClassGenerator = packageClassGenerator;
		this.beanGenerator = beanGenerator;
		this.interfaceGenerator = interfaceGenerator;
		this.classGenerator = classGenerator;
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

		for (ClassNode clazz : compilationUnit.getClasses()){
			pkg.addClass(classGenerator.build(clazz));
		}

		return pkg;
	}
}
