package ru.otus.l08;

import java.util.Map;

public class ATM implements DepCommands {
    private final Dispenser dispenser;
    private String address;
    private String atmName;
    private ATMState atmState;
    private AtmMemento firstState;

    public ATM(String atmName, String address, ATMState atmState) {
        dispenser = new Dispenser();
        this.atmName = atmName;
        this.address = address;
        this.atmState = atmState;
        firstState = new AtmMemento(this, dispenser);
    }

    public void collection(Cassette... cst) {
        System.out.println("\r\nИнкассация. " + this);
        System.out.println("--------------------------------------------------------");
        if (atmState.canCollection()) {
            dispenser.unloadAllCassettes();
            for (int i = 0; i < cst.length; i++)
                dispenser.loadCassette(cst[i], i);
            atmState = new ATMStateInWork();
            System.out.println("Инкассация завершена.");
            firstState = new AtmMemento(this, dispenser);
        }
    }

    public void cashOut(int amount) {
        System.out.println("\r\nОперация: выдача наличных. " + this);
        System.out.println("--------------------------------------------------------");
        if (atmState.canCashOut()) {
            dispenser.dispense(amount);
            if (getBalance() == 0)
                atmState = new ATMStateNoMoney();
        }
    }

    public void cashIn(Map<Nominals, Integer> banknotes) {
        System.out.println("\r\nОперация: прием наличных. " + this);
        System.out.print("--------------------------------------------------------");
        if (atmState.canCashIn()) {
            dispenser.cashIn(banknotes);
            if (getBalance() != 0 && atmState.getClass() == ATMStateNoMoney.class)
                atmState = new ATMStateInWork();
        }

    }

    public void repair() {
        System.out.println("\r\nОперация: ремонт. " + this);
        System.out.println("--------------------------------------------------------");
        System.out.println("Успешно.");
        atmState = new ATMStateInWork();
    }

    public void destroy() {
        System.out.println(this + " сломался.");
        atmState = new ATMStateHardwareError();
    }


    @Override
    public void restoreFirstState() {
        if (firstState != null) {
            this.atmState = firstState.getAtmState();
            this.atmName = firstState.getAtmName();
            this.address = firstState.getAddress();
            dispenser.restore();
        }
    }

    @Override
    public int getBalance() {
        return dispenser.getBalance();
    }

    public void printBalance() {
        System.out.println("\r\nОперация: печать баланса. " + this);
        System.out.println("--------------------------------------------------------");
        if (atmState.canPrintBalance())
            dispenser.printBalance();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAtmName(String atmName) {
        this.atmName = atmName;
    }

    public String getAddress() {
        return address;
    }

    public String getAtmName() {
        return atmName;
    }

    public ATMState getAtmState() {
        return atmState;
    }

    @Override
    public String toString() {
        return "Банкомат \"" + atmName + "\" по адресу: \"" + address + "\".";
    }
}
