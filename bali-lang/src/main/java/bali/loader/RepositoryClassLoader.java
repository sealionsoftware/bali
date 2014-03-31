package bali.loader;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * User: Richard
 * Date: 31/01/14
 */
public class RepositoryClassLoader extends ClassLoader {

	private final String coordinates;
	private final RepositorySystem system;
	private final RepositorySystemSession session;

	private ClassLoader delegate;

	private String mainClassName;

	public RepositoryClassLoader(String coordinates, RepositorySystem system, RepositorySystemSession session) {
		super(null);
		this.coordinates = coordinates;
		this.system = system;
		this.session = session;
	}

	public void initialise() throws Exception {

		Artifact artifact = new DefaultArtifact(coordinates);

		DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.RUNTIME);

		CollectRequest collectRequest = new CollectRequest();
		collectRequest.setRoot( new Dependency( artifact, JavaScopes.RUNTIME ) );
//		collectRequest.setRepositories( Booter.newRepositories( system, session ) );

		DependencyRequest dependencyRequest = new DependencyRequest( collectRequest, classpathFlter );

		List<ArtifactResult> artifactResults = system.resolveDependencies( session, dependencyRequest ).getArtifactResults();
		int i = 0;
		URL[] urls = new URL[artifactResults.size()];
		for (ArtifactResult result : artifactResults){
			urls[i++] = result.getArtifact().getFile().toURI().toURL();
		}

		JarFile jar = new JarFile(artifactResults.get(0).getArtifact().getFile());
		Manifest manifest = jar.getManifest();
		if (manifest != null){
			mainClassName = (String) manifest.getMainAttributes().get(Attributes.Name.MAIN_CLASS);
		}

		delegate = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if(name == null){
			if (mainClassName == null){
				throw new RuntimeException("This module does not have a main class");
			}
			name = mainClassName;
		}
		return delegate.loadClass(name);
	}

	public java.util.Enumeration<URL> getResources(String name) throws IOException {
		return delegate.getResources(name);
	}

	public URL getResource(String name) {
		return delegate.getResource(name);
	}

}
