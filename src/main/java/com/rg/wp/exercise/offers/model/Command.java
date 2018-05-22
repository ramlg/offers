package com.rg.wp.exercise.offers.model;


public class Command {

    private String command;

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean cancelCommand() {
        return "cancel".equals(command);
    }
}
