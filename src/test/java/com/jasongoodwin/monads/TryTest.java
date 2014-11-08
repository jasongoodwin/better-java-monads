package com.jasongoodwin.monads;

import org.junit.Test;

import static org.junit.Assert.*;

public class TryTest {
    @Test
    public void itShouldBeSuccessOnSuccess() throws Throwable{
        Try<String> t = Try.ofFailable(() -> "hey");
        assertTrue(t.isSuccess());
    }

    @Test
    public void itShouldHoldValueOnSuccess() throws Throwable{
        Try<String> t = Try.ofFailable(() -> "hey");
        assertEquals("hey", t.get());
    }

    @Test
    public void itShouldMapOnSuccess() throws Throwable{
        Try<String> t = Try.ofFailable(() -> "hey");
        Try<Integer> intT = t.map((x) -> 5);
        intT.get();
        assertEquals(5, intT.get().intValue());
    }

    @Test
    public void itShouldFlatMapOnSuccess() throws Throwable {
        Try<String> t = Try.ofFailable(() -> "hey");
        Try<Integer> intT = t.flatMap((x) -> Try.ofFailable(() -> 5));
        intT.get();
        assertEquals(5, intT.get().intValue());
    }

    @Test
    public void itShouldOrElseOnSuccess() {
        String t = Try.ofFailable(() -> "hey").orElse("jude");
        assertEquals("hey", t);
    }

    @Test
    public void itShouldReturnValueWhenRecoveringOnSuccess() {
        String t = Try.ofFailable(() -> "hey").recover((e) -> "jude");
        assertEquals("hey", t);
    }


    @Test
    public void itShouldReturnValueWhenRecoveringWithOnSuccess() throws Throwable {
        String t = Try.ofFailable(() -> "hey")
                .recoverWith((x) ->
                Try.ofFailable(() -> "Jude")
                ).get();
        assertEquals("hey", t);
    }

    @Test
    public void itShouldOrElseTryOnSuccess() throws Throwable {
        Try<String> t = Try.ofFailable(() -> "hey").orElseTry(() -> "jude");

        assertEquals("hey", t.get());
    }

    @Test
    public void itShouldBeFailureOnFailure(){
        Try<String> t = Try.ofFailable(() -> {
            throw new Exception("e");
        });
        assertFalse(t.isSuccess());
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowExceptionOnGetOfFailure() throws Throwable{
        Try<String> t = Try.ofFailable(() -> {
            throw new IllegalArgumentException("e");
        });
        t.get();
    }

    @Test
    public void itShouldMapOnFailure(){
        Try<String> t = Try.ofFailable(() -> {
            throw new Exception("e");
        }).map((x) -> "hey" + x);

        assertFalse(t.isSuccess());
    }

    @Test
    public void itShouldFlatMapOnFailure(){
        Try<String> t = Try.ofFailable(() -> {
            throw new Exception("e");
        }).flatMap((x) -> Try.ofFailable(() -> "hey"));

        assertFalse(t.isSuccess());
    }

    @Test
    public void itShouldOrElseOnFailure() {
        String t = Try.<String>ofFailable(() -> {
            throw new IllegalArgumentException("e");
        }).orElse("jude");

        assertEquals("jude", t);
    }

    @Test
    public void itShouldOrElseTryOnFailure() throws Throwable {
        Try<String> t = Try.<String>ofFailable(() -> {
            throw new IllegalArgumentException("e");
        }).orElseTry(() -> "jude");

        assertEquals("jude", t.get());
    }

    @Test
    public void itShouldReturnRecoverValueWhenRecoveringOnFailure() {
        String t = Try.ofFailable(() -> "hey")
                .<String>map((x) -> {
                    throw new Exception("fail");
                })
                .recover((e) -> "jude");
        assertEquals("jude", t);
    }


    @Test
    public void itShouldReturnValueWhenRecoveringWithOnFailure() throws Throwable {
        String t = Try.<String>ofFailable(() -> {
            throw new Exception("oops");
        })
                .recoverWith((x) ->
                                Try.ofFailable(() -> "Jude")
                ).get();
        assertEquals("Jude", t);
    }
}

