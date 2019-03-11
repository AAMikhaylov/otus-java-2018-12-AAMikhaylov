package ru.otus.l08;

public class ATMStateInWork implements ATMState {

    @Override
    public boolean canCollection() {
        return true;
    }

    @Override
    public boolean canCashOut() {
        return true;
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
