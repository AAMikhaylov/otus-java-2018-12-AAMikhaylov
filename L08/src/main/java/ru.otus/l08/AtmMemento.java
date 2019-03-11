package ru.otus.l08;

public class AtmMemento {
    private final ATMState atmState;
    private final String address;
    private final String atmName;

    public AtmMemento(ATM atm, Dispenser dispenser) {
        this.atmState = atm.getState();
        this.address = atm.getAddress();
        this.atmName = atm.getAtmName();
        dispenser.save();
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
