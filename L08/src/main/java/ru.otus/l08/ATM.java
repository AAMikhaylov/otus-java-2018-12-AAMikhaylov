package ru.otus.l08;

import java.util.Map;

public class ATM implements DepCommands {
    private Dispenser dispenser;
    private String address;
    private String atmName;
    private ATMState state;
    private MementoATM StartATMState;


    public ATM(String atmName, String address, ATMState state) {
        dispenser = new Dispenser();
        this.atmName = atmName;
        this.address = address;
        this.state = state;
        StartATMState = new MementoATM(state, address, atmName);
    }

    private void restoreMemento(MementoATM m) {
        this.state = m.getAtmState();
        this.atmName = m.getAtmName();
        this.address = m.getAddress();

    }


    public void collection(Cassette... cst) {

        System.out.println("\r\nИнкассация. " + toString());
        System.out.println("--------------------------------------------------------");
        if (state.canCollection()) {
            dispenser.unloadAllCassettes();
            for (int i = 0; i < cst.length; i++)
                dispenser.loadCassette(cst[i], i);
            state = new InWorkATMState();
            System.out.println("Инкассация завершена.");
        }
    }


    public void cashOut(int amount) {
        System.out.println("\r\nОперация: выдача наличных. " + toString());
        System.out.println("--------------------------------------------------------");
        if (state.canCashOut()) {
            dispenser.dispense(amount);
            if (getBalance() == 0)
                state = new NoMoneyATMState();
        }

    }

    public void cashIn(Map<Nominals, Integer> banknotes) {
        System.out.println("\r\nОперация: прием наличных. " + toString());
        System.out.print("--------------------------------------------------------");
        if (state.canCashIn()) {
            dispenser.cashIn(banknotes);
            if (getBalance() != 0 && state.getName() == ATMStates.NoMoney)
                state = new InWorkATMState();
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

        state = new InWorkATMState();
    }

    public void destroy() {

        System.out.println(toString() + " сломался.");
        state = new HardwareErrorATMState();
    }


    @Override
    public void restoreFirstState() {
        restoreMemento(StartATMState);
    }

    public int getBalance() {
        return dispenser.getBalance();
    }

    public void printBalance() {

        System.out.println("\r\nОперация: печать баланса. "+toString());
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

    @Override
    public String toString() {
        return "Банкомат \"" + atmName + "\" по адресу: \"" + address + "\".";
    }
}
