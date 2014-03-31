package bali.compiler.module;

import bali.compiler.GeneratedPackage;

import java.io.OutputStream;
import java.util.List;

/**
 * User: Richard
 * Date: 30/04/13
 */
public interface ModuleWriter {

	public void writeModule(List<GeneratedPackage> packages, OutputStream output, String mainClassName) throws Exception;

}
