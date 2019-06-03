package ru.otus.messageSystem.client;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import ru.otus.messageSystem.Address;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;


public class MsgSocketClient {
    private final int CLIENT_START_INTERVAL_SEC = 2;
    private final Logger logger;
    private final String startCommand;
    private final ScheduledExecutorService executorService;
    private Process process;

    public MsgSocketClient(Address address, String startCommand) {
        this.startCommand = startCommand;
        logger = Logger.getLogger(MsgSocketClient.class.getName() + "." + address.getId());
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        ScheduledFuture<Process> scheduledFuture = executorService.schedule(() -> {
            try {
                return startProcess();
            } catch (IOException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
                return null;
            }
        }, CLIENT_START_INTERVAL_SEC, TimeUnit.SECONDS);
        try {
            process = scheduledFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private Process startProcess() throws IOException {
        logger.info("Starting process with command \"" + startCommand + "\"");
        ProcessBuilder pb = new ProcessBuilder(startCommand.split(" "));
        pb.redirectErrorStream(false);
        Process p = pb.start();
        StreamListener output = new StreamListener(p.getInputStream());
        new Thread(output).start();
        return p;
    }

    public void destroy() {
        if (process != null)
            process.destroy();
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        logger.info("Shutdown complete.");

    }

    private class StreamListener implements Runnable {

        private final InputStream is;

        private StreamListener(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            try (InputStreamReader isr = new InputStreamReader(is)) {

                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null)
                    logger.info(line);

            } catch (IOException e) {

                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }
}
