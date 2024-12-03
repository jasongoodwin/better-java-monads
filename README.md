better-java8-monads
==================

This library was built immediately after Java8 became GA to help fill in some blanks (Try, Futures.sequence)
There are some other libraries mentioned w/ this project on Stack Overflow: https://stackoverflow.com/questions/27787772/try-monad-in-java-8

Feature Overview
----------------

*Try* - Optional exists to express nulls in types, but there is no way to express success/failure in types. Try to the rescue! The Try type is very similar to the Try in Scala's standard lib.

*CompletableFuture.sequence* - If you have a list of futures, there is no obvious way to get a future of a list. This will come in handy!

Usage
=====

Import into your project:

SBT
---

    "com.jason-goodwin" % "better-monads" % "0.4.0"

Maven
-----

    <dependency>
	    <groupId>com.jason-goodwin</groupId>
	    <artifactId>better-monads</artifactId>
	    <version>0.4.0</version>
    </dependency>

Try
===

The try monad was attributed to Twitter and placed into the Scala standard library.
While both Scala and Haskell have a monad Either which has a left and a right type, 
a Try is specifically of a type T on success or an exception on failure.

Usage
-----

The Try api is meant to be similar to the Optional type so has the same functions.
- get() returns the held value or throws the thrown exception
- getUnchecked() returns the held value or throws the thrown exception wrapped in a RuntimeException instance
- map(x) maps the success value x to a new value and type or otherwise passes the Failure forward.
- flatMap((x) -> f(x)) maps the success value x to a new Try of f(x).
- recover((t) -> x) will return the success value of the Try in the success case or the value x in the failure case. Exposes the exception.
- recoverWith((t) -> f(x)) will return the success value of the Try in the success case or a new try of f(x) in the failure case. Exposes the exception.
- filter((x) -> isTrue(x)) - If Success, returns the same Success if the predicate succeeds, otherwise, returns a Failure with type NoSuchElementException.
- onSuccess((x) -> f(x)) execute some code on success - takes Consumer (eg requires no return value).
- onFailure((x) -> f(x)) execute some code on failure - takes Consumer (eg requires no return value).
- orElse(x) will return the success value of the Try in success case or the value x in failure case.
- orElseTry(f) will return the success value of the Try in success case or a new Try(f) in the failure case.
- orElseThrow(() -> throw new T) gets result or on failure will throw checked exception of type T
- toOptional() will return Optional of success value of Try (if not null), otherwise it will return an empty Optional

Futures
=======
There is no sequence method in the Java8 library so I've provided one. You'll find the function Futures.sequence which will convert a `List<CompletableFuture<T>>` into a `CompletableFuture<List<T>>`. This is useful in the common use case of processing all of the elements of a list concurrently.

Usage
-----
Simply call Futures.sequence on a `List<CompletableFuture<T>>` to get back a single future with the list of your items.

    List<Integer> list = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        int size = list.size();
        List<CompletableFuture<Integer>> futures = list
                .stream()
                .map(x -> CompletableFuture.supplyAsync(() -> x))
                .collect(Collectors.toList());

    CompletableFuture<List<Integer>> futureList = Futures.sequence(futures);


Tests
=====

See the tests for examples of all functionality.
