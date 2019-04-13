package ru.otus.l08;

import java.util.ArrayList;
import java.util.List;

public class ATMDepartment {
    private List<DepCommands> atms;

    public ATMDepartment() {
        this.atms = new ArrayList<>();
    }

    public void addATM(ATM atm) {
        atms.add(atm);
    }

    public void getBalance() {
        System.out.println("\r\nОперация: запрос баланса банкоматов по департаменту.");
        System.out.println("--------------------------------------------------------");
        int generalBal = 0;
        for (DepCommands atm : atms) {
            generalBal = generalBal + atm.getBalance();
        }
        System.out.println("Баланс денежных средств по департаменту составляет: " + generalBal + "руб.");
    }

    public void resetATMState() {
        System.out.println("\r\nОперация: сброс состояний банкоматов к первоначальному.");
        System.out.println("--------------------------------------------------------");
        for (DepCommands atm : atms) {
            atm.restoreFirstState();
        }
        System.out.println("Завершено.");
    }


    public void removeATM(int i) {
        if (i < atms.size())
            atms.remove(i);
    }


}
