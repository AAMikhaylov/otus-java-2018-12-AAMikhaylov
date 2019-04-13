package ru.otus.l07;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM("\"Банкомат на Ленинском проспекте\"", "Ленинский проспект, 11");
        atm.startIncass();
        atm.addCassette(Nominals.n50, 1000, 0);
        atm.addCassette(Nominals.n100, 1000, 1);
        atm.addCassette(Nominals.n1000, 1000, 2);
        atm.addCassette(Nominals.n5000, 1, 3);
        atm.cashOut(52050);
        atm.addCassette(Nominals.n5000, 1, 4);
        atm.endIncass();
        atm.cashOut(52050);

        Map<Nominals, Integer> banknotes = new HashMap<>(4);
        banknotes.put(Nominals.n50, 10);
        banknotes.put(Nominals.n5000, 10);
        banknotes.put(Nominals.n2000, 10);
        banknotes.put(Nominals.n200, 50);
        banknotes.put(Nominals.n1000, 10);
        atm.cashIn(banknotes);
        atm.printBalance();
    }
}
