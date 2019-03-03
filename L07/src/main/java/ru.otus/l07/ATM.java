package ru.otus.l07;

import java.util.Map;

public class ATM {
    private Dispenser dispenser;
    private String address;
    private String atmName;
    private boolean inService = true;


    public ATM(String atmName, String address) {
        dispenser = new Dispenser();
        this.atmName = atmName;
        this.address = address;
    }

    public ATM() {
        dispenser = new Dispenser();
    }

    public void startIncass() {
        inService = true;
        System.out.println("\r\nИнкассация банкомата " + atmName + ".");
        System.out.println("--------------------------------------------------------");
        dispenser.unloadAllCassettes();
    }

    public void addCassette(Nominals nom, int count, int cassNum) {
        dispenser.loadCassette(new Cassette(nom, count), cassNum);

    }

    public void endIncass() {
        inService = false;
        System.out.println("Инкассация банкомата " + atmName + " завершена.");

    }

    public void cashOut(int amount) {
        if (!inService) {
            System.out.println("\r\nОперация: выдача наличных.");
            System.out.println("--------------------------------------------------------");
            dispenser.dispense(amount);
        }

    }

    public void cashIn(Map<Nominals, Integer> banknotes) {
        if (!inService) {
            System.out.println("\r\nОперация: прием наличных.");
            System.out.print("--------------------------------------------------------");
            dispenser.cashIn(banknotes);
        }
    }

    public void getBalance() {
        System.out.println("\r\nОперация: запрос баланса.");
        System.out.println("--------------------------------------------------------");
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
}
