package delta2.system.common.enums;

public enum ModuleState {
    none,

    needInit,
    initStart,
    initEnd,


    workStart,
    work,

    stop,
    error,
    needStart,
    needStop,
    needRestart
}
