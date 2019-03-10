package ru.otus.l08;

public class HardwareErrorATMState implements ATMState {
    private ATMStates name = ATMStates.HardwareError;
    @Override
    public ATMStates getName() {
        return name;
    }

    @Override
    public boolean canCollection() {
        System.out.println("Нельзя проинкассировать неисправный банкомат!");
        return false;
    }

    @Override
    public boolean canCashOut() {
        System.out.println("Выдача наличных невозможна. Банкомат неисправен!");
        return false;
    }

    @Override
    public boolean canCashIn() {
        System.out.println("Прием наличных невозможен. Банкомат неисправен!");
        return false;
    }



    @Override
    public boolean canPrintBalance() {
        System.out.println("Печать баланса невозможна. Банкомат неисправен!");
        return false;
    }
}
