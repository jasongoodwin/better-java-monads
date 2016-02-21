package com.jasongoodwin.monads;

/**
 * This is similar to the Java {@link java.util.function.Consumer Consumer} function type.
 * It has a checked exception on it to allow it to be used in lambda expressions on the Try monad.
 * 
 * @param <T>
 * @param <E> the type of throwable thrown by {@link #accept(Object)}
 */
public interface TryConsumer<T, E extends Throwable> {

  /**
   * Performs this operation on the given argument.
   *
   * @param t the input argument
   */
  void accept(T t) throws E;

}