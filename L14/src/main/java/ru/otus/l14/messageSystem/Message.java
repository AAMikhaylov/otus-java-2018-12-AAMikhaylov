package ru.otus.l14.messageSystem;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Message {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final Address from;
    private final Address to;
    private final String id;

    public Message(Address from, Address to) {
        this.from = from;
        this.to = to;
        this.id = String.valueOf(ID_GENERATOR.getAndIncrement());
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public abstract void exec(Addressee addressee);

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id != null ? id.equals(message.id) : message.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
