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
import java.util.Collections;
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
	@Parameter(property = "project.artifacts")
	private Set<Artifact> artifacts;

	private Log log;

	public void setLog(Log log) {
		this.log = log;
	}

	public Log getLog() {
		return log;
	}

	public void execute() throws MojoExecutionException, MojoFailureException {

		log.info("Executing " + executableClassName);

		try {
			int i = 0;
			URL[] dependencies = new URL[artifacts.size() + 1];
			dependencies[i++] = artifact.getFile().toURI().toURL();
			for (Artifact resolvedArtifact : artifacts){
				dependencies[i++] = resolvedArtifact.getFile().toURI().toURL();
			}
			ClassLoader classLoader = new URLClassLoader(dependencies,
							new LinkClassLoader(Collections.<Class<?>>singleton(Executable.class)));
			Class executableClass = classLoader.loadClass(executableClassName);
			final Object executable =  executableClass.newInstance();
			if (!(executable instanceof Executable)){
				throw new MojoExecutionException("The specified class is not an instance of " + Executable.class);
			}
			ThreadGroup group = new ThreadGroup("Bali Threads");
			Thread executeThread = new Thread(group, new Runnable() {
				public void run() {
					((Executable) executable).execute();
				}
			});
			executeThread.setDaemon(true);
			executeThread.setContextClassLoader(classLoader);
			executeThread.start();
			executeThread.join();
			while(group.activeCount() > 0){
				Thread[] threads = new Thread[group.activeCount()*2];
				group.enumerate(threads);
				for (Thread childThread : threads){
					childThread.join();
				}
			}

			log.info("Execution complete");


		} catch (ClassNotFoundException e) {
			throw new MojoExecutionException("The specified class could not be found", e);
		} catch (Throwable e) {
			throw new MojoFailureException("An error occured whilst running", e);
		}
	}



}