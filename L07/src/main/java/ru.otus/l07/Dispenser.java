package ru.otus.l07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


class Dispenser {
    private final ArrayList<Cassette> cassettes;
    private final byte maxCasseteCount = 4;

    Dispenser() {
        cassettes = new ArrayList<>(Collections.nCopies(maxCasseteCount, null));
    }

    void loadCassette(Cassette cst, int num) {
        if (num >= 0 && num < maxCasseteCount) {
            cassettes.set(num, cst);
            System.out.println("Загружена кассета номиналом " + cst.getNominal() + ", количество купюр - " + cst.getRemain());
        } else
            System.out.println("Невозможно загрузить  кассету номиналом " + cst.getNominal() + " Отсутствует ячейка с номером " + num + " в диспенсере!");
    }

    void unloadCassette(byte num) {
        Cassette cst = cassettes.get(num);
        if (cst != null) {
            System.out.println("Извлечена кассета номиналом " + cst.getNominal() + ", количество оставшихся купюр - " + cst.getRemain());
            cassettes.set(num, null);
        }
    }

    void cashIn(Map<Nominals, Integer> banknotes) {
        StringBuilder stCashIn = new StringBuilder();
        StringBuilder stCashReject = new StringBuilder();
        int sumCashIn = 0;
        int sumReject = 0;
        for (Map.Entry<Nominals, Integer> entry : banknotes.entrySet()) {
            boolean reject = true;
            for (Cassette cst : cassettes)
                if (entry.getKey().equals(cst.getNominal())) {
                    stCashIn.append("\r\nПринято банкнот номиналом " + entry.getKey() + " в количестве " + entry.getValue() + "шт.");
                    cst.cashIn(entry.getValue());
                    sumCashIn = sumCashIn + entry.getValue() * entry.getKey().getValue();
                    reject = false;
                }
            if (reject)
                stCashReject.append("\r\nБанкноты в количестве " + entry.getValue() + "шт. номиналом " + entry.getKey() + "не приняты банкоматом! Банкомат принимает только купюры номиналом: " + getNominalList());
            sumReject = sumReject + entry.getValue() * entry.getKey().getValue();
        }
        System.out.println(stCashIn);
        System.out.println("Итого принято: " + sumCashIn + "руб.");
        if (stCashReject.length() != 0) {
            System.out.print("Внимание!");
            System.out.println(stCashReject);
            System.out.println("Итого возвращено: " + sumReject + "руб.");
        }
    }

    private boolean prepareDispense(int amount) {
        ArrayList<Cassette> arr = new ArrayList<>(cassettes);
        arr.sort((a, b) -> {
            if (a == null) return 1;
            if (b == null) return -1;
            return b.getNominal().getValue() - a.getNominal().getValue();
        });
        int remain = amount;
        for (int i = 0; i < arr.size(); i++)
            if (remain != 0) {
                Cassette cst = arr.get(i);
                if (cst != null) {
                    int prepareCount = remain / cst.getNominal().getValue();
                    if (prepareCount > cst.getRemain())
                        prepareCount = cst.getRemain();
                    cst.setPrepareCount(prepareCount);
                    remain = remain - cst.getNominal().getValue() * prepareCount;
                }
            }

        int sum = 0;
        for (Cassette cst : cassettes)
            sum = sum + cst.getPrepareCount() * cst.getNominal().getValue();
        if (sum != amount)
            return false;
        return true;

    }

    boolean dispense(int amount) {
        System.out.println("Набор купюр на сумму " + amount + "руб.");
        if (prepareDispense(amount)) {
            for (Cassette cst : cassettes) {
                if (cst.getPrepareCount() != 0) {
                    System.out.println("Выдано банкнот: " + cst.getPrepareCount() + "шт., номиналом: " + cst.getNominal());
                    cst.dispense();
                }

            }
            return true;
        }
        System.out.println("Невозможно выдать сумму " + amount + "руб. В банкомате доступны купюры номиналом: " + getNominalList());
        resetPrepare();
        return false;
    }

    public void printBalance() {
        int bal = 0;

        for (int i = 0; i < cassettes.size(); i++) {
            Cassette cst = cassettes.get(i);
            System.out.println("Кассета: " + (i + 1) + ", номинал: " + cst.getNominal() + ", количество банкнот: " + cst.getRemain() + "шт., сумма: " + cst.getRemain() * cst.getNominal().getValue() + "руб.");
            bal = bal + cst.getRemain() * cst.getNominal().getValue();
        }
        System.out.println("Итого: " + bal + "руб.");

    }

    private void resetPrepare() {
        for (Cassette cst : cassettes)
            cst.setPrepareCount(0);
    }

    void unloadAllCassettes() {

        for (byte i = 0; i < maxCasseteCount; i++) {
            unloadCassette(i);
        }
    }

    String getNominalList() {
        StringBuilder st = new StringBuilder();
        for (Cassette cst : cassettes) {
            if (cst != null)
                if (st.length() == 0)
                    st.append(cst.getNominal());
                else
                    st.append(", " + cst.getNominal());
        }
        return st.toString();
    }
}

