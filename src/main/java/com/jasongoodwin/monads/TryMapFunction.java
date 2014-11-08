package com.jasongoodwin.monads;


public interface TryMapFunction<T, R> {
    R apply(T t) throws Throwable;
}
