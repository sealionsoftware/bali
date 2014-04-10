package bali.loader;

import bali.Initialisable;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.collection.Collection;
import bali.collection.HashMap;
import bali.collection.Map;
import bali.type.Declaration;
import bali.type.LazyReflectedType;
import bali.type.Type;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 21 Mar
 */
@MetaType(Kind.OBJECT)
public class MavenModuleTypeLoader implements ModuleTypeLoader {

	//TODO: wrap up the "repository" in a seperate dependency
	private Map<String, RepositoryClassLoader> cachedLoaders = new HashMap<>();
	private RepositorySystem system;
	private DefaultRepositorySystemSession session;

	public void initialise() {

		DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
		locator.addService( RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class );
		locator.addService( TransporterFactory.class, FileTransporterFactory.class );
		locator.addService( TransporterFactory.class, HttpTransporterFactory.class );
		locator.setErrorHandler( new DefaultServiceLocator.ErrorHandler(){
			public void serviceCreationFailed( Class<?> type, Class<?> impl, Throwable exception ){
				exception.printStackTrace();
			}
		} );

		system = locator.getService( RepositorySystem.class );
		session = MavenRepositorySystemUtils.newSession();

		LocalRepository localRepo = new LocalRepository(System.getProperty("user.home") + "\\.m2\\repository" ); // TODO - generalise
		session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) );

	}

	public Type load(String coordinates) {

		ClassLoader loader = cachedLoaders.get(coordinates);
		if (loader == null){
			RepositoryClassLoader repositoryClassLoader = new RepositoryClassLoader(convert(coordinates), system, session);
			try {
				repositoryClassLoader.initialise();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			loader = repositoryClassLoader;
			cachedLoaders.put(coordinates, repositoryClassLoader);
		}

		try {

			Class<?> clazz = (Class) loader.loadClass(null);

			//TODO: parameters
			return new LazyReflectedType<>(clazz, (Collection<Type>) bali.collection._.EMPTY);

		} catch (ClassNotFoundException cnfe){
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
