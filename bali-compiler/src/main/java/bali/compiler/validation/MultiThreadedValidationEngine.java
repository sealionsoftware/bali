package bali.compiler.validation;

import bali.compiler.parser.tree.ClassNode;
import bali.compiler.parser.tree.CompilationUnitNode;
import bali.compiler.parser.tree.ImportNode;
import bali.compiler.parser.tree.MethodDeclarationNode;
import bali.compiler.parser.tree.TypeNode;
import bali.compiler.reference.BlockingReference;
import bali.compiler.reference.Reference;
import bali.compiler.reference.Semaphore;
import bali.compiler.reference.SimpleReference;
import bali.compiler.type.ConstantLibrary;
import bali.compiler.type.TypeLibrary;
import bali.compiler.validation.validator.Validator;
import bali.compiler.validation.validator.ValidatorFactory;

import javax.lang.model.type.DeclaredType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

	private TypeLibrary typeLibrary;
	private ConstantLibrary constantLibrary;
	private List<ValidatorFactory> validatorFactories;

	public MultiThreadedValidationEngine(TypeLibrary typeLibrary, ConstantLibrary constantLibrary, List<ValidatorFactory> validatorFactories) {
		this.typeLibrary = typeLibrary;
		this.constantLibrary = constantLibrary;
		this.validatorFactories = validatorFactories;
	}

	public Map<String, List<ValidationFailure>> validate(final List<CompilationUnitNode> units) {

		final Map<String,List<ValidationFailure>> validationFailures = new LinkedHashMap<>();
		for (CompilationUnitNode compilationUnitNode : units){
			String unitName = compilationUnitNode.getName();
			List<TypeNode> declaredTypes = new ArrayList<>();
			declaredTypes.addAll(compilationUnitNode.getClasses());
			declaredTypes.addAll(compilationUnitNode.getInterfaces());
			declaredTypes.addAll(compilationUnitNode.getBeans());
			for (TypeNode typeNode : declaredTypes){
				String qualifiedClassName = unitName + "." + typeNode.getClassName();
				typeNode.setQualifiedClassName(qualifiedClassName);
				typeLibrary.notifyOfDeclaration(qualifiedClassName);
			}
			constantLibrary.notifyOfPackage(unitName);
		}

		List<Thread> threads = new ArrayList<>();
		for (final ValidatorFactory validatorFactory : validatorFactories) {
			for (final CompilationUnitNode unit : units){
				List<ValidationFailure> existingFailures = validationFailures.get(unit.getName());
				final List<ValidationFailure> unitFailures = existingFailures != null ? existingFailures : Collections.synchronizedList(new ArrayList<ValidationFailure>());
				final Validator validator = validatorFactory.createValidator();
				Thread t =  new Thread(new Runnable() {
					public void run() {
						unitFailures.addAll(
								unit.accept(validator)
						);
					}
				}, validator.getClass().getName() + ": " + unit.getName());
				t.start();
				threads.add(t);
				validationFailures.put(unit.getName(), unitFailures);
			}
		}

		for(Thread t : threads){
			try {
				t.join();
			} catch (InterruptedException e) {
			}
		}

		Map<String, List<ValidationFailure>> ret = new HashMap<>();
		for (Map.Entry<String, List<ValidationFailure>> validationFailure : validationFailures.entrySet()){
			List<ValidationFailure> unitFailures = validationFailure.getValue();
			if (!unitFailures.isEmpty()){
				ret.put(validationFailure.getKey(), validationFailure.getValue());
			}
		}

		return ret;
	}

}
