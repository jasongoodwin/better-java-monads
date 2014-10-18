package com.jasongoodwin.monads;

import java.util.Objects;
import java.util.function.Function;

/**
 * This is similar to the Java Supplier function type.
 * It has a checked exception on it to allow it to be used in lambda expressions on the Try monad.
 * @param <T>
 */

interface TrySupplier<T>{
    public T get() throws Exception;
}

abstract class Try<T>{

    public static <U> Try<U> ofFailable(TrySupplier<U> f){
        Objects.requireNonNull(f);

        try{
            U y = f.get();
            return new Success<U>(y);
        }catch (Exception e){
            return new Failure<U>(e);
        }
    }

    public abstract <U> Try<U> map(Function<? super T, ? extends U> mapper);
    public abstract <U> Try<U> flatMap(Function<? super T, Try<U>> mapper);

    public abstract T orElse(T value);
    public abstract T get() throws Exception;

    public abstract boolean isSuccess();

    public static <U> Try<U> failure(Exception e){
        return new Failure<>(e);
    }

    public static <U> Try<U> successful(U x){
        return new Success<U>(x);
    }
}

class Success<T> extends Try<T>{
    private final T value;

    public Success(T value) {
        this.value = value;
    }

    @Override
    public <U> Try<U> flatMap(Function<? super T, Try<U>> mapper) {
        return mapper.apply(value);
    }

    @Override
    public T orElse(T value) {
        return this.value;
    }

    @Override
    public T get() throws Exception {
        return value;
    }

    @Override
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        try{
            return new Success<U>(mapper.apply(value));
        } catch (Exception e){
            return new Failure<U>(e);
        }
    }

    @Override
    public boolean isSuccess() {
        return true;
    }
}


class Failure<T> extends Try<T>{
    private final Exception e;

    Failure(Exception e) {
        this.e = e;
    }

    @Override
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        return Try.<U>failure(e);
    }

    /**
     * We already have an exception so we can't execute the next combinator - pass the exception forward.
     * @param mapper
     * @param <U>
     * @return
     */
    @Override
    public <U> Try<U> flatMap(Function<? super T, Try<U>> mapper) {
        return Try.<U>failure(e);
    }

    @Override
    public T orElse(T value) {
        return value;
    }

    @Override
    public T get() throws Exception {
        throw e;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
