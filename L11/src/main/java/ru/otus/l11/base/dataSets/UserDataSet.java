package ru.otus.l11.base.dataSets;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class UserDataSet extends DataSet {
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @OrderColumn()
    PhoneDataSet[] phones;

    public UserDataSet() {
    }

    public UserDataSet(String name, int age, AddressDataSet address, PhoneDataSet... phones) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = phones;
    }

    public void setPhones(PhoneDataSet[] phones) {
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("UserDataSet{\r\n");
        result.append("id=" + getId() + ",\r\n");
        result.append("name=\"" + name + "\",\r\n");
        result.append("age=" + age + ",\r\n");
        result.append("addresses=\"" + address.getStreet() + "\",\r\n");
        result.append("phones=[");
        if (phones != null)
            for (int i = 0; i < phones.length; i++)
                if (i == 0)
                    result.append("\"" + phones[i] + "\"");
                else
                    result.append(", \"" + phones[i] + "\"");
        result.append("]\r\n}\r\n");
        return result.toString();
    }
}
