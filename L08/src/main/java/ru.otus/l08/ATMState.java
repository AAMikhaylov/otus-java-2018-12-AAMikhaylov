package ru.otus.l08;

public interface ATMState {

    boolean canCollection();

    boolean canCashOut();

    boolean canCashIn();

    boolean canPrintBalance();

    ATMStates getName();


}
