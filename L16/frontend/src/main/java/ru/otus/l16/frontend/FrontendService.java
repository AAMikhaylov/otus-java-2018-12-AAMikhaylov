package ru.otus.l16.frontend;

import ru.otus.l16.messageSystem.Address;
import ru.otus.l16.messageSystem.message.Message;


public interface FrontendService {

    void init();

    void sendMessage(Message message);

    Message getAnswer(Message srcMsg);

    Address getAddress();

    Address getDbAddress();
  }
