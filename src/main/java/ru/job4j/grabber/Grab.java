package ru.job4j.grabber;

import org.quartz.SchedulerException;

public interface Grab {
    void start() throws SchedulerException;
}
