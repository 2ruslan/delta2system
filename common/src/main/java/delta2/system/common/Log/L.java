package delta2.system.common.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.util.FileSize;
import delta2.system.common.FileStructure;
import delta2.system.common.preferences.PreferenceValue;


public class L {

    public static final String _LOG_PATH = FileStructure.GetLogPathDir() + "/d2s.log.txt";

    public static final Logger log =  LoggerFactory.getLogger("delta2system");

    private static ch.qos.logback.classic.Logger root;

    public static void configureLogback(String level) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        FileStructure.GetLogPathDir();

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(context);

        rollingFileAppender.setFile(_LOG_PATH);

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
        encoder.setPattern("%d{yyyy.MM.dd HH:mm:ss.SSS} [%thread] [%-5level] %logger{36} - %msg%n");

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

        root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        setLogLevel(level);

        root.addAppender(rollingFileAppender);
        root.addAppender(logcatAppender);
    }

    public static void setLogLevel(String level){
        root.setLevel(Level.toLevel(level));
    }
}
