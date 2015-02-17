package bali.compiler;

import bali.compiler.module.JarPackager;
import bali.compiler.module.ModuleWriter;
import bali.compiler.parser.MultiThreadedANTLRParserManager;
import bali.compiler.parser.ParserManager;
import bali.compiler.processors.CompilationExecutor;
import bali.compiler.processors.MultiThreadedCompilationExecutor;
import bali.compiler.processors.ValidationException;
import bali.compiler.processors.ValidationFailure;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * User: Richard
 * Date: 14/04/13
 */
public class BaliCompiler {

	public static final String BALI_SOURCE_FILE_EXTENSION = ".bali";

	private ParserManager parserManager = new MultiThreadedANTLRParserManager();
	private CompilationExecutor compilationExecutor = new MultiThreadedCompilationExecutor();
	private ModuleWriter moduleWriter = new JarPackager();

	public void compile(final List<PackageDescription> packageDescriptions, OutputStream output, ClassLoader dependencies, String mainClassName) {

		List<ParserRuleContext> abstractSyntaxTrees = parserManager.buildAbstractSyntaxTrees(packageDescriptions);

		List<GeneratedPackage> compiledPackages = compilationExecutor.compile(abstractSyntaxTrees);

		moduleWriter.writeModule(compiledPackages, output, mainClassName);

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
			sourceFiles = asList(in.listFiles(new FilenameFilter() {
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

			compiler.compile(packageDescriptions, os, Thread.currentThread().getContextClassLoader(), null);

		} catch (ValidationException e) {
			os.close();
			List<String> failedFiles = e.getFailedFiles();
			System.err.println("Compilation failed");
			System.err.println();
			for (String failedFile : failedFiles) {
				List<ValidationFailure> failures = e.getFailures(failedFile);
				if (failures.size() > 0) {
					System.err.println("Unit " + failedFile + BALI_SOURCE_FILE_EXTENSION + " failed with " + failures.size() + " errors");
					for (ValidationFailure failure : failures) {
						ParserRuleContext node = failure.getNode();
						Token start = node.getStart();
						System.err.println("\t" + start.getLine() + ":" + start.getStartIndex() + " " + failure.getMessage());
					}
				}
			}

			if (!outputFile.delete()){
				System.err.println("Could not delete incomplete output file " + outputFile.getName());
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

}
