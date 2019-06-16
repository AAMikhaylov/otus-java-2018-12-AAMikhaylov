package ru.otus.l16.messageSystem.message;

import ru.otus.l16.messageSystem.Address;

public class MsgAddUser extends Message {
    private final String login;
    private final String password;
    private final String userName;
    private final int age;
    private final String userAddress;
    private final String userPhones;
    private final boolean success;

    public MsgAddUser(Address from, Address to, String login, String password, String userName, int age, String userAddress, String userPhones) {
        super(MsgAddUser.class, from, to);
        this.login = login;
        this.password = password;
        this.userName = userName;
        this.age = age;
        this.userAddress = userAddress;
        this.userPhones = userPhones;
        success =false;
    }

    public MsgAddUser(MsgAddUser origMsg, Address from, Address to,boolean success) {
        super(origMsg.getId(), MsgAddUser.class, from, to);
        this.login = origMsg.login;
        this.password = origMsg.password;
        this.userName = origMsg.userName;
        this.age = origMsg.age;
        this.userAddress = origMsg.userAddress;
        this.userPhones = origMsg.userPhones;
        this.success = success;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserPhones() {
        return userPhones;
    }

    public int getAge() {
        return age;
    }

    public boolean isSuccess() {
        return success;
    }
}
