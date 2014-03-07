package bali.compiler;

import bali.compiler.bytecode.ASMBeanGenerator;
import bali.compiler.bytecode.ASMClassGenerator;
import bali.compiler.bytecode.ASMInterfaceGenerator;
import bali.compiler.bytecode.ASMPackageClassGenerator;
import bali.compiler.bytecode.ASMRunStatementGenerator;
import bali.compiler.bytecode.ConfigurablePackageGenerator;
import bali.compiler.bytecode.Generator;
import bali.compiler.module.JarPackager;
import bali.compiler.module.ModuleWriter;
import bali.compiler.parser.ANTLRParserManager;
import bali.compiler.parser.ParserManager;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.Node;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.validation.MultiThreadedValidationEngine;
import bali.compiler.validation.ValidationEngine;
import bali.compiler.validation.ValidationException;
import bali.compiler.validation.ValidationFailure;
import bali.compiler.validation.validator.ArrayLiteralValidatorFactory;
import bali.compiler.validation.validator.AssignmentValidatorFactory;
import bali.compiler.validation.validator.BeanValidatorFactory;
import bali.compiler.validation.validator.BooleanLiteralValidatorFactory;
import bali.compiler.validation.validator.BranchStatementValidatorFactory;
import bali.compiler.validation.validator.ClassValidatorFactory;
import bali.compiler.validation.validator.ConstantValidatorFactory;
import bali.compiler.validation.validator.ImplementationValidatorFactory;
import bali.compiler.validation.validator.ImportsValidatorFactory;
import bali.compiler.validation.validator.InterfaceValidatorFactory;
import bali.compiler.validation.validator.InvocationValidatorFactory;
import bali.compiler.validation.validator.NumberLiteralValidatorFactory;
import bali.compiler.validation.validator.OperationValidatorFactory;
import bali.compiler.validation.validator.ReferenceValidatorFactory;
import bali.compiler.validation.validator.ResolvablesValidatorFactory;
import bali.compiler.validation.validator.ReturnValueValidatorFactory;
import bali.compiler.validation.validator.RunStatementValidatorFactory;
import bali.compiler.validation.validator.SiteValidatorFactory;
import bali.compiler.validation.validator.StringLiteralValidatorFactory;
import bali.compiler.validation.validator.ThrowStatementValidatorFactory;
import bali.compiler.validation.validator.TypeResolvingValidatorFactory;
import bali.compiler.validation.validator.UnaryOperationValidatorFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 14/04/13
 */
public class BaliCompiler {

	public static final String BALI_SOURCE_FILE_EXTENSION = ".bali";
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

	public BaliCompiler() {

		parserManager = new ANTLRParserManager();

		validator = new MultiThreadedValidationEngine(Arrays.asList(
				new ImportsValidatorFactory(),
				new ResolvablesValidatorFactory(),
				new TypeResolvingValidatorFactory(),
				new BeanValidatorFactory(),
				new InterfaceValidatorFactory(),
				new ClassValidatorFactory(),
				new ConstantValidatorFactory(),
				new BooleanLiteralValidatorFactory(),
				new NumberLiteralValidatorFactory(),
				new StringLiteralValidatorFactory(),
				new ArrayLiteralValidatorFactory(),
				new ReferenceValidatorFactory(),
				new InvocationValidatorFactory(),
				new UnaryOperationValidatorFactory(),
				new OperationValidatorFactory(),
				new ReturnValueValidatorFactory(),
				new ImplementationValidatorFactory(),
				new AssignmentValidatorFactory(),
				new ThrowStatementValidatorFactory(),
				new BranchStatementValidatorFactory(),
				new RunStatementValidatorFactory(),
				new SiteValidatorFactory()
		));

		packageBuilder = new ConfigurablePackageGenerator(
				new ASMPackageClassGenerator(),
				new ASMBeanGenerator(),
				new ASMInterfaceGenerator(),
				new ASMClassGenerator(),
				new ASMRunStatementGenerator()
		);

		moduleWriter = new JarPackager();

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

		String moduleName = in.getName();

		List<File> sourceFiles;
		if (!in.isDirectory()){
			if (!in.getName().endsWith(BALI_SOURCE_FILE_EXTENSION)){
				throw new Exception("Not a valid bali source file " + in);
			}
			sourceFiles = Collections.singletonList(in);
		} else {
			sourceFiles = Arrays.asList(in.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(BALI_SOURCE_FILE_EXTENSION);
				}
			}));
			if (sourceFiles.isEmpty()) {
				throw new Exception("No Bali Source files found in directory " + in);
			}
		}

		List<PackageDescription> packageDescriptions = new ArrayList<>();
		for (File sourceFile : sourceFiles) {
			String fileName = sourceFile.getName();
			String packageName = fileName.substring(0, fileName.length() - BALI_SOURCE_FILE_EXTENSION.length());
			packageDescriptions.add(new PackageDescription(
					packageName,
					new FileInputStream(sourceFile),
					fileName
			));
		}

		BaliCompiler compiler = new BaliCompiler();
		File outputFile = new File(out, moduleName + ".bar");
		OutputStream os = new FileOutputStream(outputFile);

		try {

			compiler.compile(packageDescriptions, os, Thread.currentThread().getContextClassLoader());

		} catch (ValidationException e) {
			os.close();
			outputFile.delete();
			List<String> failedFiles = e.getFailedFiles();
			System.err.println("Compilation failed");
			System.err.println();
			for (String failedFile : failedFiles) {
				List<ValidationFailure> failures = e.getFailures(failedFile);
				if (failures.size() > 0) {
					System.err.println("Unit " + failedFile + BALI_SOURCE_FILE_EXTENSION + " failed with " + failures.size() + " errors");
					for (ValidationFailure failure : failures) {
						Node node = failure.getNode();
						System.err.println("\t" + node.getLine() + ":" + node.getCharacter() + " " + failure.getMessage());
					}
				}
			}
		}
		os.close();

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

	public void compile(List<PackageDescription> packageDescriptions, OutputStream output, ClassLoader dependencies) throws Exception {

		ClassLibrary library = new ClassLibrary(dependencies);
		ConstantLibrary constantLibrary = new ConstantLibrary(library);

		List<CompilationUnitNode> compilationUnits = new ArrayList<>();

		for (PackageDescription packageDescription : packageDescriptions) {
			CompilationUnitNode compilationUnit = parserManager.parse(packageDescription);
			compilationUnits.add(compilationUnit);
		}

		Map<String, List<ValidationFailure>> packageFailures = validator.validate(compilationUnits, library, constantLibrary);
		if (packageFailures.size() > 0) {
			throw new ValidationException(packageFailures);
		}

		List<GeneratedPackage> packages = new ArrayList<>();
		for (CompilationUnitNode compilationUnit : compilationUnits) {
			GeneratedPackage pkg = packageBuilder.build(compilationUnit);
			packages.add(pkg);
		}

		moduleWriter.writeModule(packages, output);
	}
}
