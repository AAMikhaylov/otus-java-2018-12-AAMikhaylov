package ru.otus.l08;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ATMDepartment department = new ATMDepartment();
        ATM atm1 = new ATM("Банкомат на Ленинском проспекте", "Ленинский проспект, 11", new ATMStateNoMoney());
        department.addATM(atm1);
        atm1.collection(new Cassette[]{new Cassette(Nominals.n50, 1000), new Cassette(Nominals.n100, 1000), new Cassette(Nominals.n500, 1000), new Cassette(Nominals.n1000, 1000)});
        atm1.cashOut(52050);
        Map<Nominals, Integer> banknotes = new HashMap<>(4);
        banknotes.put(Nominals.n50, 10);
        banknotes.put(Nominals.n5000, 10);
        banknotes.put(Nominals.n2000, 10);
        banknotes.put(Nominals.n200, 50);
        banknotes.put(Nominals.n1000, 10);
        atm1.cashIn(banknotes);
        atm1.printBalance();
        ATM atm2 = new ATM("Банкомат на Энтузиастов", "Энтузиастов, 22", new ATMStateNoMoney());
        atm2.collection(new Cassette[]{new Cassette(Nominals.n50, 1000), new Cassette(Nominals.n100, 1000), new Cassette(Nominals.n500, 1000), new Cassette(Nominals.n1000, 1000)});
        department.addATM(atm2);
        ATM atm3 = new ATM("Банкомат на Энтузиастов", "Энтузиастов, 1", new ATMStateInWork());
        atm3.collection(new Cassette[]{new Cassette(Nominals.n50, 1000), new Cassette(Nominals.n100, 1000), new Cassette(Nominals.n500, 1000), new Cassette(Nominals.n1000, 1000)});
        department.addATM(atm3);
        department.getBalance();
        atm3.destroy();
        atm3.cashOut(1000);
        atm3.repair();
        atm3.cashOut(1000);
        atm1.setAddress("Варшавская, 21");
        atm2.setAtmName("банкомат в ТРЦ");
        atm3.setAddress("Гагарина, 44");
        System.out.println(atm1);
        System.out.println(atm2);
        System.out.println(atm3);
        atm3.cashOut(1000000);
        atm3.printBalance();
        department.resetATMState();
        atm3.printBalance();
        atm1.cashOut(100);
        atm2.cashOut(1000);
        atm3.cashOut(5000);
        atm3.printBalance();
    }
}
