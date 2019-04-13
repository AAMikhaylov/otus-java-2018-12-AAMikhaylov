package ru.otus.l08;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


class Dispenser {
    private final List<Cassette> cassettes;
    private static final byte MAX_CASSETE_COUNT = 4;
    private DispenserMemento firstState;

    Dispenser() {
        cassettes = new ArrayList<>(Collections.nCopies(MAX_CASSETE_COUNT, null));
    }

    void loadCassette(Cassette cst, int num) {
        if (num >= 0 && num < MAX_CASSETE_COUNT) {
            cassettes.set(num, cst);
            System.out.println("Загружена кассета номиналом " + cst.getNominal() + ", количество купюр - " + cst.getRemain());
        } else
            System.out.println("Невозможно загрузить  кассету номиналом " + cst.getNominal() + " Отсутствует ячейка с номером " + num + " в диспенсере!");
    }

    private void unloadCassette(int num) {
        if (num >= 0 && num < MAX_CASSETE_COUNT) {
            Cassette cst = cassettes.get(num);
            if (cst != null) {
                System.out.println("Извлечена кассета номиналом " + cst.getNominal() + ", количество оставшихся купюр - " + cst.getRemain());
                cassettes.set(num, null);
            }
        } else
            System.out.println("Кассета не может быть извлечена. Отсутсвует ячейка с номером " + num);
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
        return (sum == amount);
    }

    void dispense(int amount) {
        System.out.println("Набор купюр на сумму " + amount + "руб.");
        if (prepareDispense(amount)) {
            for (Cassette cst : cassettes) {
                if (cst.getPrepareCount() != 0) {
                    System.out.println("Выдано банкнот: " + cst.getPrepareCount() + "шт., номиналом: " + cst.getNominal());
                    cst.dispense();
                }

            }
        } else {
            System.out.println("Невозможно выдать сумму " + amount + "руб. В банкомате доступны купюры номиналом: " + getNominalList());
            resetPrepare();
        }
    }

    int getBalance() {
        int bal = 0;
        for (int i = 0; i < cassettes.size(); i++) {
            Cassette cst = cassettes.get(i);
            if (cst != null)
                bal = bal + cst.getRemain() * cst.getNominal().getValue();
        }
        return bal;
    }

    public void save() {
        firstState = new DispenserMemento(cassettes);
    }

    public void restore() {
        if (firstState != null) {
            Collections.copy(cassettes, firstState.getCassettes());
            cassettes.forEach(cst -> {
                if (cst != null)
                    cst.restore();
            });
        }
    }

    void printBalance() {
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
        for (int i = 0; i < MAX_CASSETE_COUNT; i++) {
            unloadCassette(i);
        }
    }

    private String getNominalList() {
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

