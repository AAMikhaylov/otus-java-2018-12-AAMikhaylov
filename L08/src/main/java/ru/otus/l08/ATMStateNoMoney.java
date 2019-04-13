package ru.otus.l08;

public class ATMStateNoMoney implements ATMState {

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
