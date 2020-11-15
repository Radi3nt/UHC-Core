package fr.radi3nt.uhc.api.exeptions.common;

public class NoArgsException extends Exception {

    private final Integer args;

    public NoArgsException(Integer args) {
        this.args = args;
    }

    public Integer getArgs() {
        return args;
    }
}
