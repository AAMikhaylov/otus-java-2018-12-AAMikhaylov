package ru.otus.messageSystem.channel;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import ru.otus.messageSystem.Address;

import java.util.*;
import java.util.concurrent.*;

public class MsgWorkerTasks {
    private final static int THREADS_COUNT = 2;
    private final Set<Callable<Integer>> tasks = new HashSet<>();
    private List<Future<Integer>> futures;
    private final ExecutorService executor;
    private final Logger logger;
    private boolean started;


    public MsgWorkerTasks(Callable<Integer> sendMsgTask, Callable<Integer> recMsgTask, Address address) {
        tasks.add(sendMsgTask);
        tasks.add(recMsgTask);
        executor = Executors.newFixedThreadPool(THREADS_COUNT);
        logger = Logger.getLogger(MsgWorkerTasks.class.getName() + "." + address.getId());
        started = false;
    }

    public void startTasks() {
        logger.info("Start tasks.");
        futures = new ArrayList<>();
        for (Callable<Integer> task : tasks) {
            Future<Integer> future = executor.submit(task);
            futures.add(future);
        }
        started=true;
    }

    public void stopTasks() {
        futures.forEach(f -> f.cancel(true));
        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        logger.info("Stop tasks.");
        started=false;
    }

    public void destroy() {
        stopTasks();
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        logger.info("Shutdown tasks.");
        started=false;
    }

    public boolean isStarted() {
        return started;
    }
}
