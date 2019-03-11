package ru.otus.l08;

import java.util.Map;

public class ATM implements DepCommands {
    private final Dispenser dispenser;
    private String address;
    private String atmName;
    private ATMState state;
    private AtmMemento firstState;


    public ATM(String atmName, String address, ATMState state) {
        dispenser = new Dispenser();
        this.atmName = atmName;
        this.address = address;
        this.state = state;
        firstState = new AtmMemento(this, dispenser);

    }


    public void collection(Cassette... cst) {

        System.out.println("\r\nИнкассация. " + toString());
        System.out.println("--------------------------------------------------------");
        if (state.canCollection()) {
            dispenser.unloadAllCassettes();
            for (int i = 0; i < cst.length; i++)
                dispenser.loadCassette(cst[i], i);
            state = new ATMStateInWork();
            System.out.println("Инкассация завершена.");
            firstState = new AtmMemento(this, dispenser);
        }
    }


    public void cashOut(int amount) {
        System.out.println("\r\nОперация: выдача наличных. " + toString());
        System.out.println("--------------------------------------------------------");
        if (state.canCashOut()) {
            dispenser.dispense(amount);
            if (getBalance() == 0)
                state = new ATMStateNoMoney();
        }

    }

    public void cashIn(Map<Nominals, Integer> banknotes) {
        System.out.println("\r\nОперация: прием наличных. " + toString());
        System.out.print("--------------------------------------------------------");
        if (state.canCashIn()) {
            dispenser.cashIn(banknotes);
            if (getBalance() != 0 && state.getClass() == ATMStateNoMoney.class)
                state = new ATMStateInWork();
        }

    }

    @Override
    public void sendBalance(ATMDepartment department) {
        department.addATMBal(getBalance());
    }

    public void repair() {
        System.out.println("\r\nОперация: ремонт. " + toString());
        System.out.println("--------------------------------------------------------");
        System.out.println("Успешно.");

        state = new ATMStateInWork();
    }

    public void destroy() {

        System.out.println(toString() + " сломался.");
        state = new ATMStateHardwareError();
    }


    @Override
    public void restoreFirstState() {
        if (firstState != null) {
            this.state = firstState.getAtmState();
            this.atmName = firstState.getAtmName();
            this.address = firstState.getAddress();
            dispenser.restore();
        }
    }

    public int getBalance() {
        return dispenser.getBalance();
    }

    public void printBalance() {

        System.out.println("\r\nОперация: печать баланса. " + toString());
        System.out.println("--------------------------------------------------------");
        if (state.canPrintBalance())
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

    public ATMState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Банкомат \"" + atmName + "\" по адресу: \"" + address + "\".";
    }
}
