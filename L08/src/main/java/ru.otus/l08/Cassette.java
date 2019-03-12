package ru.otus.l08;

class Cassette {
    private final Nominals nominal;
    private int remainCount;
    private int prepareCount;
    private CassetteMemento firstState;

    Cassette(Nominals nominal, int count) {
        this.nominal = nominal;
        this.remainCount = count;
    }

    Nominals getNominal() {
        return nominal;
    }

    void dispense() {
        remainCount = remainCount - prepareCount;
        prepareCount = 0;
    }

    void cashIn(int count) {
        remainCount = remainCount + count;
    }

    int getRemain() {
        return remainCount;
    }

    int getPrepareCount() {
        return prepareCount;
    }

    void setPrepareCount(int prepareCount) {
        this.prepareCount = prepareCount;
    }

    public void save() {
        firstState = new CassetteMemento(remainCount);
    }

    public void restore() {
        if (firstState != null) {
            remainCount = firstState.getRemainCount();
        }
    }


}
