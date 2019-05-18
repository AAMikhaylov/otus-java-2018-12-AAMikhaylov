package ru.otus.l14.app.messages;

import ru.otus.l14.app.FrontendService;
import ru.otus.l14.messageSystem.Address;
import ru.otus.l14.messageSystem.Message;

public class MsgGetUsersAnswer extends MsgToFrontend {
    private final Message msgSource;
    private final String usersJsonList;

    public MsgGetUsersAnswer(Address from, Address to, String usersJsonList, Message msgSource) {
        super(from, to);
        this.msgSource = msgSource;
        this.usersJsonList = usersJsonList;
    }

    public String getUsersJsonList() {
        return usersJsonList;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.saveAnswer(this, msgSource);
    }
}
