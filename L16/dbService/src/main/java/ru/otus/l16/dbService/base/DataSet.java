package ru.otus.l16.dbService.base;

import javax.persistence.*;

@MappedSuperclass
public abstract class DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    public DataSet() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
