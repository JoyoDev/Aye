package com.joyodev.aye.operator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CLIRunner {

    ProcessBuilder processBuilder;

    public CLIRunner() {
        this.processBuilder = new ProcessBuilder();
    }

    public String exec(String... command) {
        processBuilder.command(command);
        try {
            Process process = processBuilder.start();
            String output = new String(process.getInputStream().readAllBytes());
            process.destroy();
            return output;
        } catch (IOException e) {
            log.error("Error running command {}", command, e);
            return null;
        }
    }

}
