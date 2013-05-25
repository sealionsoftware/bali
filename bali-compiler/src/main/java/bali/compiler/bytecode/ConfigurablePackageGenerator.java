package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.GeneratedPackage;
import bali.compiler.parser.tree.Class;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Interface;

/**
 * User: Richard
 * Date: 30/04/13
 */
public class ConfigurablePackageGenerator implements Generator<CompilationUnit, GeneratedPackage>{

	private Generator<CompilationUnit, GeneratedClass> packageClassGenerator;
	private Generator<Interface, GeneratedClass> interfaceGenerator;
	private Generator<Class, GeneratedClass> classGenerator;

	public ConfigurablePackageGenerator(Generator<CompilationUnit, GeneratedClass> packageClassGenerator, Generator<Interface, GeneratedClass> interfaceGenerator, Generator<Class, GeneratedClass> classGenerator) {
		this.packageClassGenerator = packageClassGenerator;
		this.interfaceGenerator = interfaceGenerator;
		this.classGenerator = classGenerator;
	}

	public GeneratedPackage build(CompilationUnit compilationUnit) throws Exception {
		GeneratedPackage pkg = new GeneratedPackage(compilationUnit.getName());

		pkg.addClass(packageClassGenerator.build(compilationUnit));

		for (Interface iface : compilationUnit.getInterfaces()){
			pkg.addClass(interfaceGenerator.build(iface));
		}

		for (Class clazz : compilationUnit.getClasses()){
			pkg.addClass(classGenerator.build(clazz));
		}

		return pkg;
	}
}
