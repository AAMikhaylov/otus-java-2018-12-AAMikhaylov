package ru.otus.l08;

public class NoMoneyATMState implements ATMState {
    private ATMStates name = ATMStates.NoMoney;

    @Override
    public ATMStates getName() {
        return name;
    }

    @Override
    public boolean canCollection() {
        return true;
    }

    @Override
    public boolean canCashOut() {
        System.out.println("Выдача наличных невозможна. Нет денег!");
        return false;
    }

    @Override
    public boolean canCashIn() {
        return true;
    }


    @Override
    public boolean canPrintBalance() {
        return true;
    }
}
