package bali.compiler.bytecode;

import bali.compiler.GeneratedClass;
import bali.compiler.GeneratedPackage;
import bali.compiler.parser.tree.ClassDeclaration;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.InterfaceDeclaration;

/**
 * User: Richard
 * Date: 30/04/13
 */
public class ConfigurablePackageGenerator implements Generator<CompilationUnit, GeneratedPackage>{

	private Generator<CompilationUnit, GeneratedClass> packageClassGenerator;
	private Generator<InterfaceDeclaration, GeneratedClass> interfaceGenerator;
	private Generator<ClassDeclaration, GeneratedClass> classGenerator;

	public ConfigurablePackageGenerator(Generator<CompilationUnit, GeneratedClass> packageClassGenerator, Generator<InterfaceDeclaration, GeneratedClass> interfaceGenerator, Generator<ClassDeclaration, GeneratedClass> classGenerator) {
		this.packageClassGenerator = packageClassGenerator;
		this.interfaceGenerator = interfaceGenerator;
		this.classGenerator = classGenerator;
	}

	public GeneratedPackage build(CompilationUnit compilationUnit) throws Exception {
		GeneratedPackage pkg = new GeneratedPackage(compilationUnit.getName());

		pkg.addClass(packageClassGenerator.build(compilationUnit));

		for (InterfaceDeclaration iface : compilationUnit.getInterfaces()){
			pkg.addClass(interfaceGenerator.build(iface));
		}

		for (ClassDeclaration clazz : compilationUnit.getClasses()){
			pkg.addClass(classGenerator.build(clazz));
		}

		return pkg;
	}
}
