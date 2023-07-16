package com.example.transcsystem.utility;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class AppLog {
    private static final DateTimeFormatter LogDateFormatter = DateTimeFormatter.ofPattern("ddMMyyyy", Locale.ENGLISH);

    public static synchronized void  setComment(String className, String msg, String logType) {

         //**** Now forming the logger file
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoderBase<ILoggingEvent> ple = new PatternLayoutEncoder();
        //ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setPattern("%-12date{dd-MM-YYYY HH:mm:ss.SSSS} - %msg%n");
        ple.setContext(lc);
        ple.start();

        PatternLayoutEncoderBase<ILoggingEvent> ple1 = new PatternLayoutEncoder();
        //ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple1.setPattern("%-12date{dd-MM-YYYY HH:mm:ss.SSS}  -  %msg%n");
        ple1.setContext(lc);
        ple1.start();

        // Staring Console
        ConsoleAppender<ILoggingEvent> logConsoleAppender = getiLoggingEventConsoleAppender(lc, ple1);

        //Starting File appender
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();

        switch (logType){
            case "REQ":
                fileAppender.setFile("C:\\var\\log\\applications\\API\\logs\\request.log");
                break;
            case "RES":
                fileAppender.setFile("C:\\var\\log\\applications\\API\\logs\\response.log");
                break;
            default:

        }

        fileAppender.setEncoder(ple);
        fileAppender.setName("file-app");
        fileAppender.setContext(lc);
        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger("");
        logger.addAppender(fileAppender);
        logger.addAppender(logConsoleAppender);
        logger.setLevel(Level.ALL);
        logger.setAdditive(false);
        try {

            logger.info("INFO:  [" + className + "]: " + msg);

        } catch (Exception e) {
          e.printStackTrace();
        }
        fileAppender.stop();
    }

    private static ConsoleAppender<ILoggingEvent> getiLoggingEventConsoleAppender(LoggerContext lc, PatternLayoutEncoderBase<ILoggingEvent> ple1) {
        ConsoleAppender<ILoggingEvent> logConsoleAppender = new ConsoleAppender<ILoggingEvent>();
        logConsoleAppender.setContext(lc);
        logConsoleAppender.setName("console-app");
        logConsoleAppender.setEncoder(ple1);
        logConsoleAppender.start();
        return logConsoleAppender;
    }

}
