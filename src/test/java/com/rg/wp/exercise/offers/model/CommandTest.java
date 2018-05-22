package com.rg.wp.exercise.offers.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class CommandTest {

    @Test
    public void cancelCommandWillReturnTrueWhenCommandIsCancel() {
        Command command = new Command();
        command.setCommand("cancel");
        assertThat(command.cancelCommand(), is(true));
    }

    @Test
    public void cancelCommandWillReturnFalseWhenCommandIsNotCancel() {
        Command command = new Command();
        command.setCommand("delete");
        assertThat(command.cancelCommand(), is(false));
    }

}