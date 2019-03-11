package ru.otus.l08;

import java.util.ArrayList;
import java.util.List;

public class DispenserMemento {
    private final List<Cassette> cassettes;

    public DispenserMemento(List<Cassette> cassettes) {
        this.cassettes = new ArrayList<>(cassettes);
        cassettes.forEach(cst -> {
            if (cst != null)
                cst.save();
        });

    }

    public List<Cassette> getCassettes() {
        return cassettes;
    }
}
