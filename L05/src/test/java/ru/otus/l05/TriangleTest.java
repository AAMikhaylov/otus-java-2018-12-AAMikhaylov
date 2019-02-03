package ru.otus.l05;

import static ru.otus.l05.MyJUnit.*;

public class TriangleTest {
    public static void main(String... args) {
        runTesting(TriangleTest.class);

    }

    private Triangle t;

    @BeforeEach
    public void beforeEach() {
        t = new Triangle(1.0, 2.0, 2.0);
    }

    @Test
    public void Test1() throws TriangleException {
        double s = t.S();
        assertEquals(0.968, s, 0.001, "Площадь треугольника");
    }

    @Test
    public void Test2() throws TriangleException {
        double s = t.S();
        assertEquals(0.968, s, 0.001, "Площадь треугольника");
    }

    @Test
    public void Test3() throws TriangleException {
        double s = t.S();
        assertEquals(0.1, s, 0.001, "Площадь треугольника");
    }

    @AfterEach
    public void afterEach() {
        t = null;
    }


}
