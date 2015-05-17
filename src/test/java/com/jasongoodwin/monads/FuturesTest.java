package com.jasongoodwin.monads;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FuturesTest {
    @Test
    public void itShouldConvertAListOfFuturesToAFutureWithAList() throws Exception {
        //given a list of futures,
        List<Integer> list = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        int size = list.size();
        List<CompletableFuture<Integer>> futures = list
                .stream()
                .map(x -> CompletableFuture.supplyAsync(() -> x))
                .collect(Collectors.toList());

        //when we call sequence,
        CompletableFuture<List<Integer>> futureList = Futures.sequence(futures);

        //then we should get a future with a list
        List<Integer> collectedIntegers = Futures.sequence(futures).get();
        assert(collectedIntegers.size() == size);
        assert(list.get(5) == collectedIntegers.get(5));
    }
}
