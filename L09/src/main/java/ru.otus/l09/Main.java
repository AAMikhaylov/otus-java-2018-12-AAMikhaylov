package ru.otus.l09;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    private static void test(Object o) {
        JSonWriter jw = new JSonWriter(System.out);
        Gson gson = new Gson();
        jw.writeObject(o);
        System.out.println("\n" + gson.toJson(o) + "\n");
    }

    public static void main(String[] args) throws IOException {
        test(1);
        test("AnyString");
        test(new int[]{1, 2, 3, 53});
        test(new User[]{new User("User1"), null, new User("2")});
        test(Collections.singletonList(1));
        test(Collections.singletonList(null));
        test(null);
        test((Collections.singletonList(new User(""))));


        TestObject t = new TestObject();
        JSonWriter jw = new JSonWriter(System.out);
        jw.writeObject(t);
        Gson gson = new Gson();
        System.out.println("\r\n"+gson.toJson(t) + "=gson");
    }
}
