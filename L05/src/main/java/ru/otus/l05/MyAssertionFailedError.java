package ru.otus.l05;

public class MyAssertionFailedError extends Error {
    MyAssertionFailedError(String message, Object expected, Object actual) {
        super(message + " ==> expected: <" + expected.toString() + "> but was: <" + actual.toString() + ">");
    }
}
