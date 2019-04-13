package ru.otus.l08;

enum Nominals {
    n10(10), n50(50), n100(100), n200(200), n500(500), n1000(1000), n2000(2000), n5000(5000);
    private int value;

    Nominals(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return value + "руб.";
    }
}
