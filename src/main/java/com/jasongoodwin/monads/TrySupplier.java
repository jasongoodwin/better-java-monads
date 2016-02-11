package com.jasongoodwin.monads;

/**
 * This is similar to the Java Supplier function type.
 * It has a checked exception on it to allow it to be used in lambda expressions on the Try monad.
 * @param <T>
 */

public interface TrySupplier<T>{
    T get() throws Throwable;
}
