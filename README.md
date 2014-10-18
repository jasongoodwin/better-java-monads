better-java-monads
==================

I can't help but feel there were some important omissions in the Java8 library.
Stream and Optional are great, but we need to get unimplicit effects like Exceptions out of the code too.
To make a much more readable code base, I'll try to supply some monads to fill in the gaps.

Try
===

The try monad was attributed to Twitter and placed into the Scala standard library.
While both Scala and Haskell have a monad Either which has a left and a right type, 
a Try is specifically of a type T on success or an exception on failure.
