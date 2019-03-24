package ru.otus.l09;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        TestObject t = new TestObject();

        try (OutputStream os = new FileOutputStream("11.txt")) {
            JSonWriter jw = new JSonWriter(os);
            jw.writeObject(t);
        }

        Gson gson = new Gson();
        System.out.println(gson.toJson(t));
    }
}
