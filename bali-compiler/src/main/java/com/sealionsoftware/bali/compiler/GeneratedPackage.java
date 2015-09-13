package com.sealionsoftware.bali.compiler;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 01/05/13
 */
public class GeneratedPackage {

	private String name;
	private List<GeneratedClass> classes = new ArrayList<>();

	public GeneratedPackage(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<GeneratedClass> getClasses() {
		return classes;
	}

	public void addClass(GeneratedClass clazz){
		classes.add(clazz);
	}

}
