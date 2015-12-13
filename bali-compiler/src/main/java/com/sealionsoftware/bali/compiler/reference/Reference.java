package com.sealionsoftware.bali.compiler.reference;

public interface Reference<T> {

	T get();

	void set(T referenced);
}
