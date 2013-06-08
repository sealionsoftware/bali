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
import bali.compiler.validation.ConfigurableValidationEngine;
import bali.compiler.validation.ValidationEngine;
import bali.compiler.validation.ValidationException;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.visitor.AssignmentValidator;
import bali.compiler.validation.visitor.ClassNameValidator;
import bali.compiler.validation.visitor.ImplementationValidator;
import bali.compiler.validation.visitor.ImportsValidator;
import bali.compiler.validation.visitor.InvocationValidator;
import bali.compiler.validation.visitor.ListLiteralValidator;
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

	public void compile(File in, File out) throws Exception {

		List<File> compilationUnits = Arrays.asList(in.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(BALI_SOURCE_FILE_EXTENSION);
			}
		}));

		if (compilationUnits.isEmpty()) {
			throw new Exception("No Bali Source files found in directory " + in);
		}

		List<GeneratedPackage> packages = new ArrayList<>();

		for (File compilationUnitFile : compilationUnits) {
			String fileName = compilationUnitFile.getName();
			String packageName = fileName.substring(0, fileName.length() - BALI_SOURCE_FILE_EXTENSION.length());
			CompilationUnit compilationUnit = parserManager.parse(compilationUnitFile, packageName);

			List<ValidationFailure> failures = validator.validate(compilationUnit);
			if (failures.size() > 0) {
				throw new ValidationException(failures);
			}
			GeneratedPackage pkg = packageBuilder.build(compilationUnit);
			packages.add(pkg);
		}

		moduleWriter.writeModule(in.getAbsoluteFile().getName(), packages, out);
	}

	public static void main(String[] args) throws Exception {

		File def = new File(".").getAbsoluteFile().getParentFile();
		File in, out;

		switch (args.length) {
			case 0: {
				in = def;
				out = def;
			}
			break;
			case 1: {
				in = check(args[0]);
				out = def;
			}
			break;
			case 2: {
				in = check(args[0]);
				out = check(args[1]);
			}
			break;
			default:
				throw new Exception("Usage: bali.compiler.BaliCompiler (in directory) (out directory)");
		}

		BaliCompiler compiler = new BaliCompiler(
				new ANTLRParserManager(),
				new ConfigurableValidationEngine(new Array<Validator<CompilationUnit>>(new Validator[]{
						new ImportsValidator(),
						new ClassNameValidator(),
						new ImplementationValidator(),
						new ReferenceValidator(),
						new ReturnValueValidator(),
						new ListLiteralValidator(),
						new InvocationValidator(),
						new AssignmentValidator()
				})),
				new ConfigurablePackageGenerator(
						new ASMPackageClassGenerator(),
						new ASMInterfaceGenerator(),
						new ASMClassGenerator()
				),
				new JarPackager()
		);

		try {
			compiler.compile(in, out);
		} catch (ValidationException e) {
			List<ValidationFailure> failures = e.getFailures();
			System.err.println("Compilation failed (" + failures.size() + " errors)");
			for (ValidationFailure failure : failures) {
				System.err.println("Error on line " + failure.getNode().getLine() + ": " + failure.getMessage());
			}
		}

	}

	private static File check(String name) throws Exception {
		File dir = new File(name);
		if (!dir.exists()) {
			throw new Exception("Supplied file argument " + dir + " does not exist");
		} else if (!dir.isDirectory()) {
			throw new Exception("Supplied file argument " + dir + " is not a directory");
		}
		return dir;
	}
}
