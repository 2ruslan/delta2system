package delta2.system.common.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.util.FileSize;
import delta2.system.common.FileStructure;


public class L {

    public static void init() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.stop();

        // setup FileAppender
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(lc);
        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] [%-5level] %logger{36} - %msg%n");
        encoder.start();

/*
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
        fileAppender.setContext(lc);
        fileAppender.setFile( FileStructure.GetLogPathDir() + "d2s.log");
        fileAppender.setEncoder(encoder1);
        fileAppender.start();

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> policy = new SizeAndTimeBasedRollingPolicy<>();
        policy.setMaxFileSize(new FileSize(5));
        policy.setFileNamePattern(FileStructure.GetLogPathDir() + "d2s.%d{yyyy-MM-dd}.log");
        policy.setContext(lc);
        policy.setMaxHistory(100);
        policy.setParent(fileAppender);
        policy.start();
*/


        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(lc);
        rollingFileAppender.setFile( FileStructure.GetLogPathDir() + "d2s.log");
        rollingFileAppender.setEncoder(encoder);


        SizeAndTimeBasedRollingPolicy<ILoggingEvent> policy = new SizeAndTimeBasedRollingPolicy<>();
        policy.setMaxFileSize(new FileSize(8 * 1024));
        policy.setFileNamePattern(FileStructure.GetLogPathDir() + "d2s.%d{yyyy-MM-dd_HH-mm-ss}.log");
        policy.setContext(lc);
        policy.setMaxHistory(7);
        policy.setParent(rollingFileAppender);
        policy.setContext(lc);
        policy.start();

        rollingFileAppender.setRollingPolicy(policy);

        rollingFileAppender.start();


        // setup LogcatAppender
        PatternLayoutEncoder encoder2 = new PatternLayoutEncoder();
        encoder2.setContext(lc);
        encoder2.setPattern("[%thread] %msg%n");
        encoder2.start();

        LogcatAppender logcatAppender = new LogcatAppender();
        logcatAppender.setContext(lc);
        logcatAppender.setEncoder(encoder2);
        logcatAppender.start();

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        root.setLevel(Level.TRACE);

        root.addAppender(rollingFileAppender);
        root.addAppender(logcatAppender);

    }


    public static final Logger log =  LoggerFactory.getLogger("delta2system");

    public static void configureLogback() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        FileStructure.GetLogPathDir();

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(context);

        rollingFileAppender.setFile(FileStructure.GetLogPathDir() + "/d2s.log.txt");

        SizeBasedTriggeringPolicy<ILoggingEvent> trgPolicy = new SizeBasedTriggeringPolicy<ILoggingEvent>();
        trgPolicy.setMaxFileSize(new FileSize(3*1024*1024));
        trgPolicy.setContext(context);
        trgPolicy.start();


        FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
        rollingPolicy.setFileNamePattern(FileStructure.GetLogPathDir() + "/d2s.log.%i.txt");
        rollingPolicy.setMaxIndex(9);
        rollingPolicy.setMinIndex(1);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setContext(context);
        rollingPolicy.start();

        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.setTriggeringPolicy(trgPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%logger{35} - %msg%n");
        encoder.setContext(context);
        encoder.start();

        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();

        //----------------
        // setup LogcatAppender
        PatternLayoutEncoder encoder2 = new PatternLayoutEncoder();
        encoder2.setContext(context);
        encoder2.setPattern("[%thread] %msg%n");
        encoder2.start();

        LogcatAppender logcatAppender = new LogcatAppender();
        logcatAppender.setContext(context);
        logcatAppender.setEncoder(encoder2);
        logcatAppender.start();

        //--------------------

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.TRACE);
        root.addAppender(rollingFileAppender);
        root.addAppender(logcatAppender);
    }
}
