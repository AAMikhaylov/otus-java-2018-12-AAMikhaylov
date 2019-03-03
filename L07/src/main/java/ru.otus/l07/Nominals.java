package ru.otus.l07;

enum Nominals {
    n10(10), n50(50), n100(100), n200(200), n1000(1000), n2000(2000), n5000(5000);
    private short value;

    Nominals(int value) {
        this.value = (short) value;
    }

    public short getValue() {
        return value;
    }

    public String toString() {
        return value + "руб.";
    }
}
