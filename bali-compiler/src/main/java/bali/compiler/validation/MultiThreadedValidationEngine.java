package bali.compiler.validation;

import bali.compiler.parser.Reaper;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.TerminatedException;
import bali.compiler.reference.BlockDeclaringThread;
import bali.compiler.reference.SimpleReference;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.validation.validator.Validator;
import bali.compiler.validation.validator.ValidatorFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Richard
 * Date: 14/05/13
 */
public class MultiThreadedValidationEngine implements ValidationEngine {

	private ClassLibrary classLibrary;
	private ConstantLibrary constantLibrary;
	private List<ValidatorFactory> validatorFactories;

	public MultiThreadedValidationEngine(ClassLibrary classLibrary, ConstantLibrary constantLibrary, List<ValidatorFactory> validatorFactories) {
		this.classLibrary = classLibrary;
		this.constantLibrary = constantLibrary;
		this.validatorFactories = validatorFactories;
	}

	public Map<String, List<ValidationFailure>> validate(final List<CompilationUnitNode> units) {

		final Map<String,List<ValidationFailure>> validationFailures = new LinkedHashMap<>();
		for (CompilationUnitNode compilationUnitNode : units){
			String unitName = compilationUnitNode.getName();
			List<ClassNode> declaredTypes = new ArrayList<>();
			declaredTypes.addAll(compilationUnitNode.getClasses());
			declaredTypes.addAll(compilationUnitNode.getInterfaces());
			declaredTypes.addAll(compilationUnitNode.getBeans());
			for (ClassNode classNode : declaredTypes){
				String qualifiedClassName = unitName + "." + classNode.getClassName();
				classNode.setQualifiedClassName(qualifiedClassName);
				classLibrary.notifyOfDeclaration(qualifiedClassName);
			}
			constantLibrary.notifyOfPackage(unitName);
		}

		final ReentrantLock lock = new ReentrantLock();
		final List<BlockDeclaringThread> threads = new ArrayList<>();
		final SimpleReference<Boolean> terminated = new SimpleReference<>(false);

		for (final ValidatorFactory validatorFactory : validatorFactories) {
			for (final CompilationUnitNode unit : units){
				List<ValidationFailure> existingFailures = validationFailures.get(unit.getName());
				final List<ValidationFailure> unitFailures = existingFailures != null ? existingFailures : Collections.synchronizedList(new ArrayList<ValidationFailure>());
				final Validator validator = validatorFactory.createValidator();
				BlockDeclaringThread t = new BlockDeclaringThread(new Runnable() {
					public void run() {
						try {
							unitFailures.addAll(
									unit.accept(validator)
							);
						} catch (TerminatedException e){
							unitFailures.addAll(e.getFailures());
							terminated.set(true);
						}
					}
				}, validator.getClass().getName() + ": " + unit.getName(), lock);
				t.start();
				threads.add(t);
				validationFailures.put(unit.getName(), unitFailures);
			}
		}


		Reaper reaperRunnable = new Reaper(threads, lock);
		Thread reaper = new Thread(reaperRunnable, "Validator Reaper");
		reaper.start();

		for(Thread t : threads){
			try {
				t.join();
			} catch (InterruptedException e) {
			}
		}
		reaperRunnable.kill();

		Map<String, List<ValidationFailure>> ret = new HashMap<>();
		for (Map.Entry<String, List<ValidationFailure>> validationFailure : validationFailures.entrySet()){
			List<ValidationFailure> unitFailures = validationFailure.getValue();
			if (!unitFailures.isEmpty()){
				ret.put(validationFailure.getKey(), validationFailure.getValue());
			}
		}

		if (terminated.get() && ret.size() == 0){
			throw new RuntimeException("Validation forcibly terminated but no failures were recorded");
		}

		return ret;
	}

}
