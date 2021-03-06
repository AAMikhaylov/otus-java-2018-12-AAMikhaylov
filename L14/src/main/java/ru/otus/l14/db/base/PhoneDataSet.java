package ru.otus.l14.db.base;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class PhoneDataSet extends DataSet {

    @Column(name = "phone_number")
    private String number;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number) {
        this.number = number;

    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return number;
    }
}
