package com.jasongoodwin.monads;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Futures {
    /**
     * Convert List of CompletableFutures to CompletableFuture with a List.
     * @param futures List of Futures
     * @param <T> type
     * @return CompletableFuture with a List
     */

    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        return CompletableFuture.
                allOf(futures.toArray(new CompletableFuture[futures.size()])).
                thenApply(v ->
                                futures.stream().
                                        map(CompletableFuture::join).
                                        collect(Collectors.toList())
                );
    }
}
