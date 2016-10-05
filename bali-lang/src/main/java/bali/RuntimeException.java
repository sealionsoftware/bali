package bali;

public class RuntimeException extends java.lang.RuntimeException {

    public final Object payload;

    public RuntimeException(Object payload) {
        this.payload = payload;
    }
}
