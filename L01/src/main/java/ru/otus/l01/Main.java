package ru.otus.l01;
import com.google.common.math.*;

import java.math.BigInteger;

public class Main {

    public static void main(String... args) {
        BigInteger factorial = BigIntegerMath.factorial(500);

        System.out.println(factorial);
    }
}
