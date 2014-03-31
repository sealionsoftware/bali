package bali.loader;

import bali.Initialisable;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Parameters;
import bali.collection.Collection;
import bali.collection.HashMap;
import bali.collection.Map;
import bali.type.Declaration;
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

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Manifest;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 21 Mar
 */
@MetaType(Kind.OBJECT)
public class MavenModuleLoader<T> implements ModuleLoader<T>, Initialisable {

	private Type T;

	//TODO: wrap up the "repository" in a seperate dependency
	private Map<String, RepositoryClassLoader> cachedLoaders = new HashMap<>();
	private RepositorySystem system;
	private DefaultRepositorySystemSession session;
	private Class<T> tClass;

	@Parameters
	public MavenModuleLoader(@Name("T") Type T) {
		this.T = T;
	}

	public void initialise() {

		try {
			tClass = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(convert(T.getClassName()));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

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

	public T load(String coordinates, Map parameters) {

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

			Class<T> clazz = (Class<T>) loader.loadClass(null);

			if (!tClass.isAssignableFrom(clazz)){
				throw new RuntimeException("The loaded class is not compatible with the ModuleLoaders return type");
			}

			//TODO: parameters
			return clazz.newInstance();

		} catch (ClassNotFoundException cnfe){
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Collection<Declaration> getParameters(String coordinates) {
		throw new RuntimeException("Not implemented yet");
	}
}
