better-java8-monads
==================

I can't help but feel there were some important omissions in the Java8 library.
Stream and Optional are great, but we need to get unclear effects like Exceptions out of the code too.
To make a much more readable code base, I'll try to supply some monads to fill in the gaps - especially Try.

Another omission is a CompletableFuture.sequence method, turning a List<CompletableFuture<T>> into a CompletableFuture<List<T>>

Usage
=====

Import into your project:

SBT
---

    "com.jason-goodwin" % "better-monads" % "0.2.1"

Maven
-----

    <dependency>
	    <groupId>com.jason-goodwin</groupId>
	    <artifactId>better-monads</artifactId>
	    <version>0.2.1</version>
    </dependency>

Try
===

The try monad was attributed to Twitter and placed into the Scala standard library.
While both Scala and Haskell have a monad Either which has a left and a right type, 
a Try is specifically of a type T on success or an exception on failure.

Usage
-----

The Try api is meant to be similar to the Optional type so has the same functions.
- map(x) maps the success value x to a new value and type or otherwise passes the Failure forward.
- flatMap((x) -> f(x)) maps the success value x to a new Try of f(x).
- recover((t) -> x) will return the success value of the Try in the success case or the value x in the failure case. Exposes the exception.
- recoverWith((t) -> f(x)) will return the success value of the Try in the success case or a new try of f(x) in the failure case. Exposes the exception.
- filter((x) -> isTrue(x)) - If Success, returns the same Success if the predicate succeeds, otherwise, returns a Failure with type NoSuchElementException.
- onSuccess((x) -> f(x)) execute some code on success - requires no return value.
- onFailure((x) -> f(x)) execute some code on failure - requires no return value.
- orElse(x) will return the success value of the Try in success case or the value x in failure case.
- orElseTry(f) will return the success value of the Try in success case or a new Try(f) in the failure case.
- toOptional() will return Optional of success value of Try (if not null), otherwise it will return an empty Optional

Futures
=======
There is no sequence method in the Java8 library so I've provided one. You'll find the function Futures.sequence which will convert a List<CompletableFuture<T>> into a CompletableFuture<List<T>>. This is useful in the common use case of processing all of the elements of a list concurrently.

Usage
-----
Simply call Futures.sequence on a List<CompletableFuture<T>> to get back a single future with the list of your items.

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
