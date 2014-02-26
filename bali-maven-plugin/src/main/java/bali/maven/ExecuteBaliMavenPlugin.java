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


@org.apache.maven.plugins.annotations.Mojo(
		name = "execute-bali",
		defaultPhase = LifecyclePhase.TEST,
		requiresDependencyResolution = ResolutionScope.TEST
)
public class ExecuteBaliMavenPlugin implements Mojo {

	@Parameter(property = "executableClassName")
	private String executableClassName;

	@Parameter(property = "project.artifact")
	private Artifact artifact;

	private Log log;

	public void setLog(Log log) {
		this.log = log;
	}

	public Log getLog() {
		return log;
	}

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			ClassLoader classLoader = new URLClassLoader(new URL[]{artifact.getFile().toURI().toURL()}, Thread.currentThread().getContextClassLoader());
			Class executableClass = classLoader.loadClass(executableClassName);

			Object executable =  executableClass.newInstance();
			if (!(executable instanceof Executable)){
				throw new MojoExecutionException("The specified class is not an instance of " + Executable.class);
			}
			((Executable) executable).execute();
		} catch (ClassNotFoundException e) {
			throw new MojoExecutionException("The specified class could not be found", e);
		} catch (Throwable e) {
			throw new MojoExecutionException("An error occured whilst running", e);
		}
	}



}