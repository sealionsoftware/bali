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
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@org.apache.maven.plugins.annotations.Mojo(
		name = "compile-bali",
		defaultPhase = LifecyclePhase.COMPILE,
		requiresDependencyResolution = ResolutionScope.COMPILE
)
public class CompileBaliGoal implements Mojo {

	@Parameter(property = "project.artifactId")
	private String moduleName;
	@Parameter(property = "project.basedir")
	private File baseDirectory;
	@Parameter(property = "project.build.directory")
	private File targetDirectory;
	@Parameter(property = "project.artifact")
	private Artifact artifact;
	@Parameter(property = "project.dependencyArtifacts")
	private Set<Artifact> dependencyArtifacts;

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
			String relativePath = sourceFile.toURI().getPath().substring(sourceDirectory.toURI().getPath().length());
			String packageName = relativePath.substring(0, relativePath.length() - BaliCompiler.BALI_SOURCE_FILE_EXTENSION.length()).replaceAll("/", ".");
			try {
				packageDescriptions.add(new PackageDescription(
						packageName,
						new FileInputStream(sourceFile),
						sourceFile.getName()
				));
			} catch (FileNotFoundException fnfe){
				log.error("File not found", fnfe);
			}
		}

		try {
			int i = 0;
			URL[] dependencies = new URL[dependencyArtifacts.size()];
			for (Artifact dependencyArtifact : dependencyArtifacts){
				dependencies[i++] = dependencyArtifact.getFile().toURI().toURL();
			}
			URLClassLoader classLoader = new URLClassLoader(dependencies, Thread.currentThread().getContextClassLoader());

			targetDirectory.mkdirs();
			File outputFile = new File(targetDirectory, moduleName + ".bar");
			OutputStream os = new FileOutputStream(outputFile);
			compiler.compile(packageDescriptions, os, classLoader);
			artifact.setFile(outputFile);
			log.info("Compilation complete");

		} catch (ValidationException e) {
			List<String> failedFiles = e.getFailedFiles();
			for (String failedFile : failedFiles) {
				List<ValidationFailure> failures = e.getFailures(failedFile);
				int numberOfFailures = failures.size();
				if (numberOfFailures > 0) {
					StringBuilder message = new StringBuilder("Unit ")
							.append(failedFile)
							.append(" failed with ")
							.append(numberOfFailures)
							.append(" error");
					if (numberOfFailures > 1){
						message.append("s");
					}
					log.error(message.toString());
					for (ValidationFailure failure : failures) {
						Node node = failure.getNode();
						log.error("\t" + node.getLine() + ":" + node.getCharacter() + " " + failure.getMessage());
					}
				}
			}
			throw new MojoExecutionException("Compilation failed");
		} catch (Exception e) {
			throw new MojoFailureException(e.getMessage(), e);
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