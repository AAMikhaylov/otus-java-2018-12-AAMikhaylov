package ru.otus.l08;

public enum ATMStates {
    NoMoney("Нет денег"), HardwareError("Техническая неисправность"),  InWork("Исправен");
    private String value;

    ATMStates(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }
}
