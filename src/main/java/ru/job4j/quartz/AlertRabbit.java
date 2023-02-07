package ru.job4j.quartz;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    public static Properties setUp() throws IOException {
        Properties config = new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            config.load(in);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return config;
    }

    public static void main(String[] args) {
        try {
            Properties config = setUp();
            Class.forName(config.getProperty("driver-class-name"));
            Connection cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connect", cn);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(config.getProperty("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            try (Connection connect = (Connection) context.getJobDetail().getJobDataMap().get("connect");
                 PreparedStatement ps =
                         connect.prepareStatement("insert into rabbit(created_date) values (?)")) {
                long millis = System.currentTimeMillis();
                Timestamp timestamp = new Timestamp(millis);
                ps.setTimestamp(1, timestamp);
                ps.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
