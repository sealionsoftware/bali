package bali.compiler.processors;

import bali.compiler.*;
import bali.compiler.parser.BaliVisitor;
import bali.compiler.type.ClassLibrary;
import bali.compiler.type.Type;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;

import java.util.*;

import static java.util.Arrays.asList;

/**
 * User: Richard
 * Date: 13 Dec
 */
public class MultiThreadedCompilationExecutor implements CompilationExecutor {

	public List<GeneratedPackage> compile(final List<ParserRuleContext> packageContexts) {

		final ClassLibrary classLibrary = new ClassLibrary();
		final CompilationProgressMonitor monitor = new CompilationProgressMonitor();
		final ExceptionGatherer exceptionGatherer = new ExceptionGatherer();

        final TreeProperty<Map<String, String>> qualifiedNamesMap = new TreeProperty<>("qualifiedNamesMap", monitor);
        final TreeProperty<String> qualifiedClassName = new TreeProperty<>("qualifiedClassName", monitor);
		final TreeProperty<Type> type = new TreeProperty<>("type", monitor);
        final TreeProperty<GeneratedClass> generatedClass = new TreeProperty<>("generatedClass", monitor);
        final TreeProperty<GeneratedPackage> generatedPackage = new TreeProperty<>("generatedPackage", monitor);


		final Map<String, List<ValidationFailure>> validationFailures = new HashMap<>();
		final List<GeneratedPackage> compiledPackages = new ArrayList<>();

		final List<BaliVisitor<Void>> processors = Arrays.<BaliVisitor<Void>>asList(
                new QualifiedNameMapper(qualifiedNamesMap),
                new ImportValidator(),
                new ASMClassGenerator(generatedClass),
                new GeneratedPackageAssembler(compiledPackages, generatedPackage)
        );

		for (final BaliVisitor processor: processors){
			Thread t = new Thread((new Runnable() {
				public void run() {
					monitor.registerThread();
					try {
						for (RuleContext packageContext : packageContexts) {
							packageContext.accept(processor);
						}
					} finally {
						monitor.deregisterThread();
					}
				}
			}), processor.getClass().getName());
			t.setUncaughtExceptionHandler(exceptionGatherer);
			t.start();
		}

		monitor.run();

		if (!validationFailures.isEmpty()){
			throw new ValidationException(validationFailures);
		}

		assert exceptionGatherer.getGatheredExceptions().isEmpty() : exceptionGatherer.getGatheredExceptions();
		assert monitor.getBlockages().isEmpty() : monitor.getBlockages();
		assert compiledPackages.size() == packageContexts.size() : "No problems were detected but not all of the provided packages have been compiled successfully";

		return compiledPackages;
	}
}
