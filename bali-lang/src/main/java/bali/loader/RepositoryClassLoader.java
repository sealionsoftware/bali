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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 31/01/14
 */
public class RepositoryClassLoader extends ClassLoader {

	private final String coordinates;
	private final RepositorySystem system;
	private final RepositorySystemSession session;

	private ClassLoader delegate;

	public RepositoryClassLoader(String coordinates, RepositorySystem system, RepositorySystemSession session) {
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
		List<URL> urls = new ArrayList<>(artifactResults.size());
		for (ArtifactResult result : artifactResults){
			urls.add(result.getArtifact().getFile().toURI().toURL());
		}

		delegate = new URLClassLoader(urls.toArray(new URL[urls.size()]), Thread.currentThread().getContextClassLoader());
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return delegate.loadClass(name);
	}

}
