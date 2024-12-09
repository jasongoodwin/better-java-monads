package com.jasongoodwin.monads;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Monadic Try type.
 * Represents a result type that could have succeeded with type T or failed with a Throwable.
 * Originally was Exception but due to seeing issues with eg play with checked Throwable,
 * And also seeing that Scala deals with throwable,
 * I made the decision to change it to use Throwable.
 *
 * @param <T>
 */

public abstract class Try<T> {

    protected Try() {
    }

    public static <U> Try<U> ofFailable(TrySupplier<U> f) {
        Objects.requireNonNull(f);

        try {
            return Try.successful(f.get());
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    public static <U> Try<U> ofOptional(Optional<U> op, Throwable e) {
        if(op.isPresent()) {
            return new Success<>(op.get());
        } else {
            return new Failure<>(e);
        }
    }

    public static <U> Try<U> ofOptional(Optional<U> op) {
        return ofOptional(op, new IllegalArgumentException("Missing Value"));
    }

    /**
     * Transform success or pass on failure.
     * Takes an optional type parameter of the new type.
     * You need to be specific about the new type if changing type
     *
     * Try.ofFailable(() -&gt; "1").&lt;Integer&gt;map((x) -&gt; Integer.valueOf(x))
     *
     * @param f   function to apply to successful value.
     * @param <U> new type (optional)
     * @return Success&lt;U&gt; or Failure&lt;U&gt;
     */

    public abstract <U> Try<U> map(TryMapFunction<? super T, ? extends U> f);

    /**
     * Transform success or pass on failure, taking a Try&lt;U&gt; as the result.
     * Takes an optional type parameter of the new type.
     * You need to be specific about the new type if changing type.
     *
     * Try.ofFailable(() -&gt; "1").&lt;Integer&gt;flatMap((x) -&gt; Try.ofFailable(() -&gt; Integer.valueOf(x)))
     * returns Integer(1)
     *
     * @param f   function to apply to successful value.
     * @param <U> new type (optional)
     * @return new composed Try
     */
    public abstract <U> Try<U> flatMap(TryMapFunction<? super T, Try<U>> f);

    /**
     * Specifies a result to use in case of failure.
     * Gives access to the exception which can be pattern matched on.
     *
     * Try.ofFailable(() -&gt; "not a number")
     * .&lt;Integer&gt;flatMap((x) -&gt; Try.ofFailable(() -&gt;Integer.valueOf(x)))
     * .recover((t) -&gt; 1)
     * returns Integer(1)
     *
     * @param f function to execute on successful result.
     * @return new composed Try
     */

    public abstract T recover(Function<? super Throwable, T> f);

    /**
     * Try applying f(t) on the case of failure.
     * @param f function that takes throwable and returns result
     * @return a new Try in the case of failure, or the current Success.
     */
    public abstract Try<T> recoverWith(TryMapFunction<? super Throwable, Try<T>> f);

    /**
     * Return a value in the case of a failure.
     * This is similar to recover but does not expose the exception type.
     *
     * @param value return the try's value or else the value specified.
     * @return new composed Try
     */
    public abstract T orElse(T value);

    /**
     * Return another try in the case of failure.
     * Like recoverWith but without exposing the exception.
     *
     * @param f return the value or the value from the new try.
     * @return new composed Try
     */
    public abstract Try<T> orElseTry(TrySupplier<T> f);

    /**
     * Gets the value T on Success or throws the cause of the failure.
     *
     * @return T
     * @throws Throwable produced by the supplier function argument
     */

    public abstract <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * Gets the value T on Success or throws the cause of the failure.
     *
     * @return T
     * @throws Throwable
     */
    public abstract T get() throws Throwable;

    /**
     * Gets the value T on Success or throws the cause of the failure wrapped into a RuntimeException
     * @return T
     * @throws RuntimeException
     */
    public abstract T getUnchecked();

    public abstract boolean isSuccess();

    /**
     * Performs the provided action, when successful
     * @param action action to run
     * @return new composed Try
     * @throws E if the action throws an exception
     */
    public abstract <E extends Throwable> Try<T> onSuccess(TryConsumer<T, E> action) throws E;

    /**
     * Performs the provided action, when failed
     * @param action action to run
     * @return new composed Try
     * @throws E if the action throws an exception
     */
    public abstract <E extends Throwable> Try<T> onFailure(TryConsumer<Throwable, E> action) throws E;

    /**
     * If a Try is a Success and the predicate holds true, the Success is passed further.
     * Otherwise (Failure or predicate doesn't hold), pass Failure.
     * @param pred predicate applied to the value held by Try
     * @return For Success, the same success if predicate holds true, otherwise Failure
     */
    public abstract Try<T> filter(Predicate<T> pred);

    /**
     * Try contents wrapped in Optional.
     * @return Optional of T, if Success, Empty if Failure or null value
     */
    public abstract Optional<T> toOptional();

    /**
     * Factory method for failure.
     *
     * @param e throwable to create the failed Try with
     * @param <U> Type
     * @return a new Failure
     */

    public static <U> Try<U> failure(Throwable e) {
        return new Failure<>(e);
    }

    /**
     * Factory method for success.
     *
     * @param x value to create the successful Try with
     * @param <U> Type
     * @return a new Success
     */
    public static <U> Try<U> successful(U x) {
        return new Success<>(x);
    }
}

class Success<T> extends Try<T> {
    private final T value;

    public Success(T value) {
        this.value = value;
    }

    @Override
    public <U> Try<U> flatMap(TryMapFunction<? super T, Try<U>> f) {
        Objects.requireNonNull(f);
        try {
            return f.apply(value);
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @Override
    public T recover(Function<? super Throwable, T> f) {
        Objects.requireNonNull(f);
        return value;
    }

    @Override
    public Try<T> recoverWith(TryMapFunction<? super Throwable, Try<T>> f) {
        Objects.requireNonNull(f);
        return this;
    }

    @Override
    public T orElse(T value) {
        return this.value;
    }

    @Override
    public Try<T> orElseTry(TrySupplier<T> f) {
        Objects.requireNonNull(f);
        return this;
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return value;
    }

    @Override
    public T get() throws Throwable {
        return value;
    }

    @Override
    public T getUnchecked() {
        return value;
    }

    @Override
    public <U> Try<U> map(TryMapFunction<? super T, ? extends U> f) {
        Objects.requireNonNull(f);
        try {
            return new Success<>(f.apply(value));
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public <E extends Throwable> Try<T> onSuccess(TryConsumer<T, E> action) throws E {
      action.accept(value);
      return this;
    }

    @Override
    public Try<T> filter(Predicate<T> p) {
        Objects.requireNonNull(p);

        if (p.test(value)) {
            return this;
        } else {
            return Try.failure(new NoSuchElementException("Predicate does not match for " + value));
        }
    }

    @Override
    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }

    @Override
    public <E extends Throwable> Try<T> onFailure(TryConsumer<Throwable, E> action) {
      return this;
    }
}


class Failure<T> extends Try<T> {
    private final Throwable e;

    Failure(Throwable e) {
        this.e = e;
    }

    @Override
    public <U> Try<U> map(TryMapFunction<? super T, ? extends U> f) {
        Objects.requireNonNull(f);
        return Try.failure(e);
    }

    @Override
    public <U> Try<U> flatMap(TryMapFunction<? super T, Try<U>> f) {
        Objects.requireNonNull(f);
        return Try.failure(e);
    }

    @Override
    public T recover(Function<? super Throwable, T> f) {
        Objects.requireNonNull(f);
        return f.apply(e);
    }

    @Override
    public Try<T> recoverWith(TryMapFunction<? super Throwable, Try<T>> f) {
        Objects.requireNonNull(f);
        try{
            return f.apply(e);
        }catch(Throwable t){
            return Try.failure(t);
        }
    }

    @Override
    public T orElse(T value) {
        return value;
    }

    @Override
    public Try<T> orElseTry(TrySupplier<T> f) {
        Objects.requireNonNull(f);
        return Try.ofFailable(f);
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        throw exceptionSupplier.get();
    }

    @Override
    public T get() throws Throwable {
        throw e;
    }

    @Override
    public T getUnchecked() {
        throw new RuntimeException(e);
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public <E extends Throwable> Try<T> onSuccess(TryConsumer<T, E> action) {
      return this;
    }

    @Override
    public Try<T> filter(Predicate<T> pred) {
        return this;
    }

    @Override
    public Optional<T> toOptional() {
        return Optional.empty();
    }

    @Override
    public <E extends Throwable> Try<T> onFailure(TryConsumer<Throwable, E> action) throws E {
      action.accept(e);
      return this;
    }
}
