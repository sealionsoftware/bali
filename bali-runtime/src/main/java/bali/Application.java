package bali;
import java.lang.String;
import bali.classloader.RepositoryClassLoader;
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

/**
 * User: Richard
 * Date: 04/02/14
 */
public class Application {

	public static void main(String[] args) throws Throwable {

		if (args.length != 2){
			System.err.println("Usage: java bali.Application coordinates class");
			return;
		}

		String[] coordinates = args[0].split(":");
		if (coordinates.length != 3){
			System.err.println("coordinates must be of the form groupId:artifactId:version");
			return;
		}
		String barCoordinates = coordinates[0] + ":" + coordinates[1] + ":bar:" + coordinates[2];

		DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
		locator.addService( RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class );
		locator.addService( TransporterFactory.class, FileTransporterFactory.class );
		locator.addService( TransporterFactory.class, HttpTransporterFactory.class );
		locator.setErrorHandler( new DefaultServiceLocator.ErrorHandler(){
			public void serviceCreationFailed( Class<?> type, Class<?> impl, Throwable exception ){
				exception.printStackTrace();
			}
		} );

		RepositorySystem system = locator.getService( RepositorySystem.class );

		DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

		LocalRepository localRepo = new LocalRepository(System.getProperty("user.home") + "\\.m2\\repository" ); // TODO - generalise
		session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) );

		//		session.setTransferListener( new ConsoleTransferListener() );
		//		session.setRepositoryListener( new ConsoleRepositoryListener() );

		RepositoryClassLoader classLoader = new RepositoryClassLoader(barCoordinates, system, session);
		classLoader.initialise();
		Class executableClass = classLoader.loadClass(args[1]);
		Object executable =  executableClass.newInstance();
		if (!(executable instanceof Executable)){
			throw new IllegalArgumentException("The specified class is not an instance of " + Executable.class);
		}
		((Executable) executable).execute();
	}

}
