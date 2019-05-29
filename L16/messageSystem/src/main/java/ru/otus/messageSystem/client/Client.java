package ru.otus.messageSystem.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private final int CLIENT_START_INTERVAL_SEC = 2;
    private final Logger logger;

    private final StringBuffer out = new StringBuffer();
    private final String startCommand;
    private final String name;
    private final ScheduledExecutorService executorService;
    private Process process;

    public Client(String startCommand, String name) {
        this.startCommand = startCommand;
        this.name = name;
        logger = Logger.getLogger(Client.class.getName() + "." + name);
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {

        ScheduledFuture<Process> scheduledFuture = executorService.schedule(() -> {
            try {
                return startProcess();
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
                return null;
            }
        }, CLIENT_START_INTERVAL_SEC, TimeUnit.SECONDS);
        try {
            process = scheduledFuture.get();
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } catch (ExecutionException e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

    }

    private Process startProcess() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(startCommand.split(" "));
        pb.redirectErrorStream(true);
        Process p = pb.start();
        StreamListener output = new StreamListener(p.getInputStream(), "OUTPUT");
        new Thread(output).start();
        return p;
    }

    public void destroy() {

        if (process != null)
            process.destroy();
        executorService.shutdown();

    }

    public String getOutput() {
        return out.toString();
    }

    private class StreamListener implements Runnable {
        private final Logger logger = Logger.getLogger(this.getClass().getName());
        private final InputStream is;
        private final String type;

        private StreamListener(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (InputStreamReader isr = new InputStreamReader(is)) {
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    //out.append(type).append(">").append(line).append("\r\n");
                    logger.log(Level.INFO, type + ">" + line + "\r\n");
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }
}
