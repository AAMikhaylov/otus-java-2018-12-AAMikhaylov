package ru.otus.frontend.messages;

import ru.otus.frontend.FrontendService;
import ru.otus.messageSystem.Address;
import ru.otus.messageSystem.Message;

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
