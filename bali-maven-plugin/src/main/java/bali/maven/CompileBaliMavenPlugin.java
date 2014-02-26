package bali.maven;

import bali.compiler.BaliCompiler;
import bali.compiler.PackageDescription;
import bali.compiler.parser.tree.Node;
import bali.compiler.validation.ValidationException;
import bali.compiler.validation.ValidationFailure;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


@org.apache.maven.plugins.annotations.Mojo(
		name = "compile-bali",
		defaultPhase = LifecyclePhase.COMPILE,
		requiresDependencyResolution = ResolutionScope.COMPILE
)
public class CompileBaliMavenPlugin implements Mojo {

	@Parameter(property = "project.artifactId")
	private String moduleName;
	@Parameter(property = "project.basedir")
	private File baseDirectory;
	@Parameter(property = "project.build.directory")
	private File targetDirectory;

	@Parameter(property = "project.artifact")
	private Artifact artifact;

	private BaliCompiler compiler = new BaliCompiler();
	private Log log;

	public void setLog(Log log) {
		this.log = log;
	}

	public Log getLog() {
		return log;
	}

	public void execute() throws MojoExecutionException, MojoFailureException {

		log.info("Starting compilation");

		File sourceDirectory = new File(baseDirectory, "src/main/bali");
		List<File> sourceFiles = findBaliFiles(sourceDirectory);
		if (sourceFiles.isEmpty()) {
			throw new MojoExecutionException("No bali source files found in directory " + sourceDirectory.getName());
		}

		List<PackageDescription> packageDescriptions = new ArrayList<>();
		for (File sourceFile : sourceFiles) {
			String fileName = sourceFile.getName();
			String packageName = fileName.substring(0, fileName.length() - BaliCompiler.BALI_SOURCE_FILE_EXTENSION.length());
			try {
				packageDescriptions.add(new PackageDescription(
						packageName,
						new FileInputStream(sourceFile)
				));
			} catch (FileNotFoundException fnfe){
				log.error("File not found", fnfe);
			}
		}

		try {

			targetDirectory.mkdirs();
			File outputFile = new File(targetDirectory, moduleName + ".bar");
			OutputStream os = new FileOutputStream(outputFile);
			compiler.compile(packageDescriptions, os);
			artifact.setFile(outputFile);
			log.info("Compilation complete");

		} catch (ValidationException e) {
			List<String> failedFiles = e.getFailedFiles();
			for (String failedFile : failedFiles) {
				List<ValidationFailure> failures = e.getFailures(failedFile);
				if (failures.size() > 0) {
					log.error("Unit " + failedFile + BaliCompiler.BALI_SOURCE_FILE_EXTENSION + " failed with " + failures.size() + " errors");
					for (ValidationFailure failure : failures) {
						Node node = failure.getNode();
						log.error("\t" + node.getLine() + ":" + node.getCharacter() + " " + failure.getMessage());
					}
				}
			}
			throw new MojoExecutionException("Compilation failed");
		} catch (Exception e) {
			throw new MojoFailureException("An Exception occured whilst compiling", e);
		}
	}

	private List<File> findBaliFiles(File directory){
		List<File> sourceFiles = new ArrayList<>();
		File[] files = directory.listFiles();
		if (files != null) for (File file : files){
			if (file.isFile() && file.getName().endsWith(BaliCompiler.BALI_SOURCE_FILE_EXTENSION)){
				sourceFiles.add(file);
			} else if (file.isDirectory()){
				sourceFiles.addAll(findBaliFiles(file));
			}
		}
		return sourceFiles;
	}

}