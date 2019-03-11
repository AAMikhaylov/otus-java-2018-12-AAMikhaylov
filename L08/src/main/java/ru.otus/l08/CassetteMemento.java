package ru.otus.l08;

public class CassetteMemento {
    private final int remainCount;

    public CassetteMemento(int remainCount) {
        this.remainCount = remainCount;
    }

    public int getRemainCount() {
        return remainCount;
    }
}
