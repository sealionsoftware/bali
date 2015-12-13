package com.sealionsoftware.bali.compiler.assembly;

interface Interruptable {

    static void wrapException(Interruptable task, String message){
        try {
            task.call();
        } catch (Exception e) {
            throw new RuntimeException(message, e);
        }
    }

    void call() throws InterruptedException;
}
