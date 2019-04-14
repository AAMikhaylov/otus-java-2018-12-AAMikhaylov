package ru.otus.l12.base;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity(name="user")
@Table(name = "user")
public class UserDataSet extends DataSet {
    @NaturalId
    @Column(name = "login", length = 50, unique = true, nullable = false)
    private String login;
    @Column(name = "password", length = 50, nullable = false)
    private String password;
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
    @Column(name = "sessionID")
    private String sessionID;

    public UserDataSet() {

    }

    public UserDataSet(String login, String password, String name, int age, AddressDataSet address, PhoneDataSet... phones) {
        this.login = login;
        this.password=password;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = phones;
    }

    public void setPhones(PhoneDataSet[] phones) {
        this.phones = phones;
    }

    public String getPassword() {
        return password;
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
        if (address != null)
            result.append("addresses=\"" + address.getStreet() + "\",\r\n");
        if (phones != null) {
            result.append("phones=[");
            for (int i = 0; i < phones.length; i++)
                if (i == 0)
                    result.append("\"" + phones[i] + "\"");
                else
                    result.append(", \"" + phones[i] + "\"");
            result.append("]\r\n}\r\n");
        }
        return result.toString();
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }
}
