package com.jasongoodwin.monads;

/**
 * Todo implement projections.
 * This is not yet usable.
 * @param <L>
 * @param <R>
 */

public abstract class Either<L, R> {
    protected Either(){
    }

    public static <K, U> Left<K, U> asLeft(K value){
        return new Left<K, U>(value);
    }

    public static <K, U> Right<K, U> asRight(U value){
        return new Right<K, U>(value);
    }

    public abstract Either<R, L> swap();

    public abstract boolean isLeft();
    public abstract boolean isRight();
}

class Left<L, R> extends Either<L, R>{
    private L value;

    protected Left(L value) {
        this.value = value;
    }

    @Override
    public Either<R, L> swap() {
        return new Right<R, L>(value);
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }
}

class Right<L, R> extends Either<L, R>{
    private R value;

    protected Right(R value){
        this.value = value;
    }


    @Override
    public Either<R, L> swap() {
        return new Left<R, L>(value);
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }
}
