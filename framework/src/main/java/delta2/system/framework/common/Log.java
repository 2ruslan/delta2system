package delta2.system.framework.common;

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
import delta2.system.framework.interfaces.ILogger;


public class Log implements ILogger {
    private static ch.qos.logback.classic.Logger root;

    private final Logger log =  LoggerFactory.getLogger("delta2system");

    private  static ILogger instance;

    private Log(){}

    public static ILogger Instance(){
        return instance;
    }

    public static void Configure(String level, String logDir) {
        Log log = new Log();
        log.ConfigureInner(level, logDir);
        instance = log;
    }

    private void ConfigureInner(String level, String logDir) {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(context);

        rollingFileAppender.setFile(GetLogPath(logDir));

        SizeBasedTriggeringPolicy<ILoggingEvent> trgPolicy = new SizeBasedTriggeringPolicy<ILoggingEvent>();
        trgPolicy.setMaxFileSize(new FileSize(3*1024*1024));
        trgPolicy.setContext(context);
        trgPolicy.start();


        FixedWindowRollingPolicy rollingPolicy = new FixedWindowRollingPolicy();
        rollingPolicy.setFileNamePattern(GetFileNamePattern(logDir));
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

    private static String GetLogPath(String dir){
        return dir + "/d2s.log.txt";
    }

    private static String GetFileNamePattern(String dir){
        return dir + "/d2s.log.%i.txt";
    }

    @Override
    public void error(String msg) {
        log.error(msg);
    }

    @Override
    public void error(String format, Object... args) {
        log.error(format, args);
    }

    @Override
    public void error(Throwable ex) {
        log.error("", ex);
    }

    @Override
    public void debug(String msg) {
        log.debug(msg);
    }

    @Override
    public void debug(String format, Object... args) {
        log.debug(format, args);
    }

    @Override
    public void info(String msg) {
        log.info(msg);
    }

    @Override
    public void info(String format, Object... args) {
        log.info(format, args);
    }

    @Override
    public void trace(String msg) {
        log.trace(msg);
    }

    @Override
    public void trace(String format, Object... args) {
        log.trace(format, args);
    }

    @Override
    public void warn(String msg) {
        log.warn(msg);
    }

    @Override
    public void warn(String format, Object... args) {
        log.warn(format, args);
    }
}
