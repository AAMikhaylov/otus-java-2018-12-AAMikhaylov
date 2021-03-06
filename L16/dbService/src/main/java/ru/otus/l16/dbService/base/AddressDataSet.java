package ru.otus.l16.dbService.base;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class AddressDataSet extends DataSet {
    @Column(name = "street")
    private String street;

    public AddressDataSet() {
    }

    public AddressDataSet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }
}
