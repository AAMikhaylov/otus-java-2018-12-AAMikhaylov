package ru.otus.l14.app.messages;

import ru.otus.l14.app.FrontendService;
import ru.otus.l14.messageSystem.Address;
import ru.otus.l14.messageSystem.Message;

public class MsgGetUsersCountAnswer extends MsgToFrontend {
    private final Message msgSource;
    private final String usersCount;

    public MsgGetUsersCountAnswer(Address from, Address to, String usersCount, Message msgSource) {
        super(from, to);
        this.msgSource = msgSource;
        this.usersCount = usersCount;
    }

    public String getUsersCount() {
        return usersCount;
    }

    @Override
    public void exec(FrontendService frontendService) {
        frontendService.saveAnswer(this, msgSource);

    }
}
