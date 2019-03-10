package ru.otus.l08;

public class InWorkATMState implements ATMState {
    private ATMStates name = ATMStates.InWork;

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
