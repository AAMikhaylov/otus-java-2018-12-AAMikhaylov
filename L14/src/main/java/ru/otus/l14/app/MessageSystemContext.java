package ru.otus.l14.app;

import ru.otus.l14.front.FrontendServiceImpl;
import ru.otus.l14.messageSystem.Address;
import ru.otus.l14.messageSystem.MessageSystem;

public class MessageSystemContext {
    private final MessageSystem messageSystem;
    private Address frontAddress;
    private Address dbAddress;
    private final FrontendServiceImpl userService;

    public MessageSystemContext(MessageSystem messageSystem, FrontendServiceImpl userService) {
        this.messageSystem = messageSystem;
        this.userService=userService;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public Address getFrontAddress() {
        return frontAddress;
    }

    public Address getDbAddress() {
        return dbAddress;
    }

    public void setFrontAddress(Address frontAddress) {
        this.frontAddress = frontAddress;
    }

    public void setDbAddress(Address dbAddress) {
        this.dbAddress = dbAddress;
    }

    public FrontendServiceImpl getUserService() {
        return userService;
    }
}
