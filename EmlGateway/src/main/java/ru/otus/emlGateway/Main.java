package ru.otus.emlGateway;

public class Main {
    public static void main(String[] args) {
        EmlGateway gateway = new EmlGateway("localhost",1050);
        gateway.start();
    }
}
