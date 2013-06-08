package bali.compiler.module;

import bali.compiler.GeneratedModule;
import bali.compiler.GeneratedPackage;

import java.io.File;
import java.util.List;

/**
 * User: Richard
 * Date: 30/04/13
 */
public interface ModuleWriter {

	public GeneratedModule writeModule(String name, List<GeneratedPackage> packages, File directory) throws Exception;

}
