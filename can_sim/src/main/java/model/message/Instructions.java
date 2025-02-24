package model.message;

public enum Instructions {
    /*
        Every node implements the commands STATUS and STOP and one custom specific command

     */
    STATUS,
    NO_OP,
    STOP,
    GAS_LEVEL_LOW,
    GAS_LEVEL_HIGH,
    GAS_LEVEL_MEDIUM,
    MOTOR_COLD,
    MOTOR_OVERHEATED,
    MOTOR_IN_PARAMETERS,
    FUMES_LEVEL_GENOCIDE,
    FUMES_LEVEL_HIGH,
    FUMES_LEVEL_MEDIUM,
    FUMES_LEVEL_LOW,
    FUMES_LEVEL_EURO_9
}
