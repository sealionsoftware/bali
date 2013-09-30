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
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.ConfigurableValidationEngine;
import bali.compiler.validation.ValidationEngine;
import bali.compiler.validation.ValidationException;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.visitor.AssignmentValidator;
import bali.compiler.validation.visitor.BooleanLiteralValidator;
import bali.compiler.validation.visitor.BranchStatementValidator;
import bali.compiler.validation.visitor.ClassValidator;
import bali.compiler.validation.visitor.ConstantValidator;
import bali.compiler.validation.visitor.ConstructionValidator;
import bali.compiler.validation.visitor.DeclaredTypeValidator;
import bali.compiler.validation.visitor.ImplementationValidator;
import bali.compiler.validation.visitor.ImportsValidator;
import bali.compiler.validation.visitor.InterfaceValidator;
import bali.compiler.validation.visitor.InvocationValidator;
import bali.compiler.validation.visitor.ListLiteralValidator;
import bali.compiler.validation.visitor.NumberLiteralValidator;
import bali.compiler.validation.visitor.OperationValidator;
import bali.compiler.validation.visitor.ReferenceValidator;
import bali.compiler.validation.visitor.ReturnValueValidator;
import bali.compiler.validation.visitor.StringLiteralValidator;
import bali.compiler.validation.visitor.ThrowStatementValidator;
import bali.compiler.validation.visitor.TypeResolvingValidator;
import bali.compiler.validation.visitor.UnaryOperationValidator;
import bali.compiler.validation.visitor.Validator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * User: Richard
 * Date: 14/04/13
 */
public class BaliCompiler {

	private static final String BALI_SOURCE_FILE_EXTENSION = ".bali";

	private ParserManager parserManager;
	private ValidationEngine validator;
	private Generator<CompilationUnitNode, GeneratedPackage> packageBuilder;
	private ModuleWriter moduleWriter;

	public BaliCompiler(ParserManager parser, ValidationEngine validator, Generator<CompilationUnitNode, GeneratedPackage> packageBuilder, ModuleWriter moduleWriter) {
		this.parserManager = parser;
		this.validator = validator;
		this.packageBuilder = packageBuilder;
		this.moduleWriter = moduleWriter;
	}

	public void compile(File in, File out) throws Exception {

		List<File> sourceFiles = Arrays.asList(in.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(BALI_SOURCE_FILE_EXTENSION);
			}
		}));

		if (sourceFiles.isEmpty()) {
			throw new Exception("No Bali Source files found in directory " + in);
		}

		List<CompilationUnitNode> compilationUnits = new ArrayList<>();

		for (File sourceFile : sourceFiles) {
			String fileName = sourceFile.getName();
			String packageName = fileName.substring(0, fileName.length() - BALI_SOURCE_FILE_EXTENSION.length());
			CompilationUnitNode compilationUnit = parserManager.parse(sourceFile, packageName);
			compilationUnits.add(compilationUnit);
		}

		Map<String, List<ValidationFailure>> packageFailures = validator.validate(compilationUnits);
		if (packageFailures.size() > 0) {
			throw new ValidationException(packageFailures);
		}

		List<GeneratedPackage> packages = new ArrayList<>();
		for (CompilationUnitNode compilationUnit : compilationUnits) {
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

		TypeLibrary library = new TypeLibrary();

		BaliCompiler compiler = new BaliCompiler(
				new ANTLRParserManager(),
				new ConfigurableValidationEngine(new Array<Validator<? extends Node>>(new Validator<?>[]{
						new ImportsValidator(library),
						new DeclaredTypeValidator(library),
						new TypeResolvingValidator(library),
						new InterfaceValidator(library),
						new ClassValidator(library),
						new ConstantValidator(library),
						new BooleanLiteralValidator(library),
						new NumberLiteralValidator(library),
						new StringLiteralValidator(library),
						new ListLiteralValidator(library),
						new ReferenceValidator(library),
						new InvocationValidator(),
						new UnaryOperationValidator(),
						new OperationValidator(),
						new ReturnValueValidator(),
						new ImplementationValidator(),
						new AssignmentValidator(),
						new ConstructionValidator(library),
						new ThrowStatementValidator(library),
						new BranchStatementValidator()
				})),
				new ConfigurablePackageGenerator(
						new ASMPackageClassGenerator(library),
						new ASMInterfaceGenerator(),
						new ASMClassGenerator(library)
				),
				new JarPackager()
		);

		try {
			compiler.compile(in, out);
		} catch (ValidationException e) {
			List<String> failedFiles = e.getFailedFiles();
			System.err.println("Compilation failed");
			System.err.println();
			for (String failedFile : failedFiles) {
				List<ValidationFailure> failures = e.getFailures(failedFile);
				if (failures.size() > 0) {
					System.err.println("Unit " + failedFile + BALI_SOURCE_FILE_EXTENSION + " failed with " + failures.size() + " errors");
					for (ValidationFailure failure : failures) {
						System.err.println("\t" + failure.getNode().getLine() + ": " + failure.getMessage());
					}
				}
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
