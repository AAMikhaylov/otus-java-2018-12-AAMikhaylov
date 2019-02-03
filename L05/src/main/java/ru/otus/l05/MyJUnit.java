package ru.otus.l05;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class MyJUnit {
    public static void main(String... args) {
        runTesting(TriangleTest.class);

    }

    private static void runMethods(List<Method> mList, Object o) throws InvocationTargetException, IllegalAccessException {
        for (Method m : mList)
            m.invoke(o);
    }

    public static <T> void runTesting(Class<T> c) {
        ArrayList<Method> beforeMethodsArr = new ArrayList<>();
        ArrayList<Method> afterMethodsArr = new ArrayList<>();
        ArrayList<Method> testMethodsArr = new ArrayList<>();
        Method[] mtd = c.getDeclaredMethods();
        for (int i = 0; i < mtd.length; i++) {
            if (mtd[i].isAnnotationPresent(BeforeEach.class))
                beforeMethodsArr.add(mtd[i]);
            if (mtd[i].isAnnotationPresent(Test.class))
                testMethodsArr.add(mtd[i]);
            if (mtd[i].isAnnotationPresent(AfterEach.class))
                afterMethodsArr.add(mtd[i]);
        }
        try {
            T testClassObj = c.getDeclaredConstructor().newInstance();
            for (Method m : testMethodsArr) {
                runMethods(beforeMethodsArr, testClassObj);
                m.invoke(testClassObj);
                runMethods(afterMethodsArr, testClassObj);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void assertEquals(double expected, double actual, double delta, String message) {
        if (Math.abs(expected - actual) > delta) throw new MyAssertionFailedError(message, expected, actual);
    }
}

