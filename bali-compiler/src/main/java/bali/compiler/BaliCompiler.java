package bali.compiler;

import bali.compiler.bytecode.ASMClassGenerator;
import bali.compiler.bytecode.ASMInterfaceGenerator;
import bali.compiler.bytecode.ASMPackageClassGenerator;
import bali.compiler.bytecode.ConfigurablePackageGenerator;
import bali.compiler.bytecode.Generator;
import bali.compiler.module.JarPackager;
import bali.compiler.module.ModuleWriter;
import bali.compiler.parser.ANTLRParserManager;
import bali.compiler.parser.ParserManager;
import bali.compiler.parser.tree.CompilationUnit;
import bali.compiler.parser.tree.Node;
import bali.compiler.validation.ValidationEngine;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.VisitorValidationEngine;
import bali.compiler.validation.visitor.ClassNameValidator;
import bali.compiler.validation.visitor.ImplementationValidator;
import bali.compiler.validation.visitor.ImportValidator;
import bali.compiler.validation.visitor.ReferenceValidator;
import bali.compiler.validation.visitor.ReturnValueValidator;
import bali.compiler.validation.visitor.Validator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * User: Richard
 * Date: 14/04/13
 */
public class BaliCompiler {

	private static final String BALI_SOURCE_FILE_EXTENSION = ".bali";

	private ParserManager parserManager;
	private ValidationEngine validator;
	private Generator<CompilationUnit, GeneratedPackage> packageBuilder;
	private ModuleWriter moduleWriter;

	public BaliCompiler(ParserManager parser, ValidationEngine validator, Generator packageBuilder, ModuleWriter moduleWriter) {
		this.parserManager = parser;
		this.validator = validator;
		this.packageBuilder = packageBuilder;
		this.moduleWriter = moduleWriter;
	}

	public void compile(File directory) throws Exception {

		List<File> compilationUnits = Arrays.asList(directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(BALI_SOURCE_FILE_EXTENSION);
			}
		}));

		if (compilationUnits.isEmpty()){
			throw new Exception("No Bali Source files found in directory " + directory);
		}

		List<GeneratedPackage> packages = new ArrayList<>();

		for (File compilationUnitFile : compilationUnits){
			String fileName = compilationUnitFile.getName();
			String packageName = fileName.substring(0, fileName.length() - BALI_SOURCE_FILE_EXTENSION.length());
			CompilationUnit compilationUnit = parserManager.parse(compilationUnitFile, packageName);

			List<ValidationFailure> failures = validator.validate(compilationUnit);
			if (failures.size() > 0){
				for (ValidationFailure failure : failures){
					System.err.println("Error on line " + failure.getNode().getLine() + ": " + failure.getMessage());
				}
				throw new Exception(failures.size() + " Compilation Errors" );
			}
			GeneratedPackage pkg = packageBuilder.build(compilationUnit);
			packages.add(pkg);
		}

		moduleWriter.writeModule(directory.getAbsoluteFile().getName(), packages);
	}

	public static void main(String[] args) throws Exception {

		File dir;

		switch (args.length){
			case 0: dir = new File(".").getAbsoluteFile().getParentFile(); break;
			case 1: dir = new File(args[0]); break;
			default: throw new Exception("Usage: bali.compiler.BaliCompiler (source directory)?");
		}

		if (!dir.exists()){
			throw new Exception("Supplied file argument does not exist");
		} else if (!dir.isDirectory()){
			throw new Exception("Supplied file argument is not a directory");
		}

		BaliCompiler compiler = new BaliCompiler(
				new ANTLRParserManager(),
				new VisitorValidationEngine(new Array<Validator<Node>>(new Validator[]{
					new ImportValidator(),
					new ClassNameValidator(),
					new ImplementationValidator(),
					new ReferenceValidator(),
					new ReturnValueValidator()
				})),
				new ConfigurablePackageGenerator(
						new ASMPackageClassGenerator(),
						new ASMInterfaceGenerator(),
						new ASMClassGenerator()
				),
				new JarPackager()
		);

		compiler.compile(dir);

	}
}
