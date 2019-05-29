package ru.otus.messageSystem.clientRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRunner {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ArrayList<String> clientRunComamnds = new ArrayList<>();
    private final int CLIENT_START_DELAY_SEC = 2;

    public ClientRunner() {
        clientRunComamnds.add("java -jar ../dbService/target/dbService.jar");
        clientRunComamnds.add("java -jar ../dbService/target/dbService.jar");
    }

    public void startClients() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        int delayTime = 0;
        for (String command : clientRunComamnds) {
            delayTime += CLIENT_START_DELAY_SEC;
            start(executorService, command, delayTime);
        }
    }

    private void start(ScheduledExecutorService executorService, String command, int delayTime) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(command);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, delayTime, TimeUnit.SECONDS);
    }
}
