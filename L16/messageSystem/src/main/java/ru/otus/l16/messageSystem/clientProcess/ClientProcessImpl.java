package ru.otus.l16.messageSystem.clientProcess;

import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.log4j.Logger;
import ru.otus.l16.messageSystem.Address;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;


public class ClientProcessImpl implements ClientProcess {
    private static int instanceCount = 0;
    private final int CLIENT_START_INTERVAL_SEC = 5;
    private final Logger logger;
    private final String startCommand;
    private final ScheduledExecutorService executorService;
    private Process process;

    public ClientProcessImpl(Address address, String startCommand) {
        this.startCommand = startCommand;
        logger = Logger.getLogger(ClientProcessImpl.class.getName() + "." + address.getId());
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void start() {
        if (startCommand.isEmpty())
            return;
        ScheduledFuture<Process> scheduledFuture = executorService.schedule(() -> {
            try {
                return startProcess();
            } catch (IOException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
                return null;
            }
        }, CLIENT_START_INTERVAL_SEC * instanceCount, TimeUnit.SECONDS);
        instanceCount++;
        try {
            process = scheduledFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private Process startProcess() throws IOException {
        logger.info("Client process: Starting process with command \"" + startCommand + "\"");
        ProcessBuilder pb = new ProcessBuilder(startCommand.split(" "));
        pb.redirectErrorStream(true);
        Process p = pb.start();
        StreamListener output = new StreamListener(p.getInputStream());
        new Thread(output).start();
        return p;
    }

    @Override
    public void shutdown() {
        if (process != null)
            process.destroy();
        executorService.shutdownNow();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        logger.info("Client process: shutdown complete.");

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
