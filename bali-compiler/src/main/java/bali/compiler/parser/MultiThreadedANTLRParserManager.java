package bali.compiler.parser;


import bali.compiler.PackageDescription;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * User: Richard
 * Date: 30 Nov
 */
public class MultiThreadedANTLRParserManager implements ParserManager {

	private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public List<ParserRuleContext> buildAbstractSyntaxTrees(List<PackageDescription> packageDescriptions){

		java.util.List<Future<ParserRuleContext>> packageFutures = new ArrayList<>(packageDescriptions.size());
		for (final PackageDescription packageDescription : packageDescriptions){
			packageFutures.add(executorService.submit(new Callable<ParserRuleContext>(){
				public ParserRuleContext call() throws Exception {
					Parser parser = createParser(packageDescription);
					return parser.getRuleContext();
				}
			}));
		}

		final List<ParserRuleContext> packageContexts = new ArrayList<>(packageFutures.size());
		for(Future<ParserRuleContext> future : packageFutures){
			try {
				ParserRuleContext parserRuleContext = future.get();
				packageContexts.add(parserRuleContext);
			} catch (InterruptedException e) {
				throw new RuntimeException("Compilation interrupted whilst parsing");
			} catch (ExecutionException e) {
				throw new RuntimeException("Parsing failed", e.getCause());
			}
		}
		return packageContexts;
	}

	private Parser createParser(PackageDescription packageDescription) {

		try {

			ANTLRInputStream input = new ANTLRInputStream(new InputStreamReader(packageDescription.getFile()));
			Lexer lexer = new BaliLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			tokens.fill();
			BaliParser parser = new BaliParser(tokens);
			parser.setErrorHandler(new DefaultErrorStrategy());

			return parser;

		} catch (Exception e){
			throw new RuntimeException("Parser creation for " + packageDescription.getSourceFileName() + " failed", e);
		}
	}

}
