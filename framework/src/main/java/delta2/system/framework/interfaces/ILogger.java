package delta2.system.framework.interfaces;

public interface ILogger {

    void error(String msg);
    void error(String format, Object... args);
    void error(Throwable ex);

    void debug(String msg);
    void debug(String format, Object... args);

    void info(String msg);
    void info(String format, Object... args);

    void trace(String msg);
    void trace(String format, Object... args);

    void warn(String msg);
    void warn(String format, Object... args);
}
