package ru.otus.l08;

public class MementoATM {
    private final ATMState atmState;
    private final String address;
    private final String atmName;

    public MementoATM(ATMState atmState, String address, String atmName) {
        this.atmState = atmState;
        this.address = address;
        this.atmName = atmName;
    }

    public ATMState getAtmState() {
        return atmState;
    }

    public String getAddress() {
        return address;
    }

    public String getAtmName() {
        return atmName;
    }
}
