package ru.otus.l08;

import java.util.ArrayList;
import java.util.List;

public class ATMDepartment {
    private List<DepCommands> ATMList;
    private int depatmentBal;

    public ATMDepartment() {
        this.ATMList = new ArrayList<>();
    }

    public void addATM(ATM atm) {
        ATMList.add(atm);

    }

    public void addATMBal(int bal) {
        depatmentBal = depatmentBal + bal;
    }

    public void getBalance() {
        System.out.println("\r\nОперация: запрос баланса банкоматов по департаменту.");
        System.out.println("--------------------------------------------------------");
        depatmentBal = 0;
        for (DepCommands atm : ATMList) {
            atm.sendBalance(this);
        }
        System.out.println("Баланс денежных средств по департаменту составляет: "+depatmentBal+"руб.");
    }
    public void resetATMState() {
        System.out.println("\r\nОперация: сброс состояний банкоматов к первоначальному.");
        System.out.println("--------------------------------------------------------");
        for (DepCommands atm : ATMList) {
            atm.restoreFirstState();
        }
        System.out.println("Завершено.");
    }


    public void removeATM(int i) {
        if (i < ATMList.size())
            ATMList.remove(i);
    }


}
