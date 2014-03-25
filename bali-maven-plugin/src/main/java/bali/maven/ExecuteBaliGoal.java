package bali.maven;

import bali.Executable;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;


@org.apache.maven.plugins.annotations.Mojo(
		name = "execute-bali",
		defaultPhase = LifecyclePhase.TEST,
		requiresDependencyResolution = ResolutionScope.TEST
)
public class ExecuteBaliGoal implements Mojo {

	@Parameter(property = "executableClassName")
	private String executableClassName;
	@Parameter(property = "project.artifact")
	private Artifact artifact;
	@Parameter(property = "project.dependencyArtifacts")
	private Set<Artifact> dependencyArtifacts;

	private Log log;

	public void setLog(Log log) {
		this.log = log;
	}

	public Log getLog() {
		return log;
	}

	public void execute() throws MojoExecutionException, MojoFailureException {

		Thread t = Thread.currentThread();
		ClassLoader original = t.getContextClassLoader();
		try {
			int i = 0;
			URL[] dependencies = new URL[dependencyArtifacts.size()];
			for (Artifact resolvedArtifact : dependencyArtifacts){
				dependencies[i++] = resolvedArtifact.getFile().toURI().toURL();
			}
			ClassLoader classLoader = new URLClassLoader(new URL[]{artifact.getFile().toURI().toURL()}, new URLClassLoader(dependencies, Thread.currentThread().getContextClassLoader()));
			t.setContextClassLoader(classLoader);
			Class executableClass = classLoader.loadClass(executableClassName);
			Object executable =  executableClass.newInstance();
			if (!(executable instanceof Executable)){
				throw new MojoExecutionException("The specified class is not an instance of " + Executable.class);
			}
			((Executable) executable).execute();
		} catch (ClassNotFoundException e) {
			throw new MojoExecutionException("The specified class could not be found", e);
		} catch (Throwable e) {
			throw new MojoFailureException("An error occured whilst running", e);
		} finally {
			t.setContextClassLoader(original);
		}
	}



}