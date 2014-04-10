package bali.monitor;

import bali.Initialisable;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Parameters;
import bali.type.Type;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static bali.Primitive.convert;

/**
 * A Disposable Resource Pool pre instanciates a pool of resources on an internal thread
 * and forgets them once theyve been requested.
 *
 * TODO: A version with ThreadLocal caching?
 *
 * User: Richard
 * Date: 10 Apr
 */
@MetaType(Kind.MONITOR)
public class DisposableResourcePool<T> implements ResourcePool<T> {

	private Type implementationType;

	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private boolean run = true;
	private boolean initialised = false;

	private BlockingQueue<T> queue;

	@Parameters
	public DisposableResourcePool(Type T, @Name("implementationType") Type implementationType) {
		queue = new BlockingQueue<>(T, convert(10));
		this.implementationType = implementationType;
	}

	public T getResource() {
		if (!initialised){
			throw new RuntimeException("The pool has not been initialised");
		}
		return queue.pop();
	}

	public void initialise() {

		if (!convert(implementationType.getParameters().isEmpty())){
			throw new RuntimeException("Cannot create pools of resources with dependencies yet");
		}

		executorService.submit(new Runnable() {
			public void run() {
				while (run){
					queue.push((T) implementationType.createObject(bali.collection._.EMPTY));
				}
			}
		});
		initialised = true;
	}
}
