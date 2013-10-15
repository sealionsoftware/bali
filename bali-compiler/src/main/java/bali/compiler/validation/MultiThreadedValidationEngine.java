package bali.compiler.validation;

import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.validation.visitor.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class MultiThreadedValidationEngine implements ValidationEngine {

	private List<Validator> validators;
	private ExecutorService executorService;

	public MultiThreadedValidationEngine(List<Validator> validators, ExecutorService executorService) {
		this.validators = validators;
		this.executorService = executorService;
	}

	public Map<String, List<ValidationFailure>> validate(final List<CompilationUnitNode> units) {

		final List<Future<Map<String,List<ValidationFailure>>>> futureFailures = new ArrayList<>();

		for (final Validator validator : validators) {

				Future<Map<String,List<ValidationFailure>>> future = executorService.submit(new Callable<Map<String,List<ValidationFailure>>>() {
					public Map<String,List<ValidationFailure>> call() throws Exception {
						Map<String,List<ValidationFailure>> threadFailures = new HashMap<>();
						for (final CompilationUnitNode unit : units) {
							List<ValidationFailure> unitFailures = unit.accept(validator);
							if (!unitFailures.isEmpty()){
								threadFailures.put(unit.getName(), unitFailures);
							}
						}
						validator.onCompletion();
						return threadFailures;
					}
				});
				futureFailures.add(future);
		}

//		executorService.execute(new Runnable() {
//			public void run() {
//				try {
//					Thread.sleep(10000);
//					for (Future<Map<String,List<ValidationFailure>>> future : futureFailures){
//						if (!future.isDone()){
//							future.cancel(true);
//						}
//					}
//				} catch (InterruptedException e){
//				}
//			}
//		});

		Map<String, List<ValidationFailure>> failures = new HashMap<>();

		List<CancellationException> failedValidators = new ArrayList<>();

		for (Future<Map<String,List<ValidationFailure>>> futureEntry : futureFailures){
			try {
				Map<String,List<ValidationFailure>> validationFailures = futureEntry.get();
				if (validationFailures.size() > 0){
					for (Map.Entry<String,List<ValidationFailure>> entry : validationFailures.entrySet()){
						List<ValidationFailure> previousFailures = failures.get(entry.getKey());
						if (previousFailures == null){
							previousFailures = new ArrayList<>();
							failures.put(entry.getKey(), previousFailures);
						}
						previousFailures.addAll(entry.getValue());
					}
				}
			} catch (CancellationException cancelled){
				failedValidators.add(cancelled);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (failures.isEmpty() && !failedValidators.isEmpty()){
			throw new FailedValidationException(failedValidators);
		}

		return failures;
	}

}
