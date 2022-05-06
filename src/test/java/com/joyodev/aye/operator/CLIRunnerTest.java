package com.joyodev.aye.operator;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CLIRunnerTest {

    @Test
    public void testListCommandWorking() {
        // given
        CLIRunner cliRunner = new CLIRunner();

        // when
        String output = cliRunner.exec("ls");

        // then
        assertTrue(output.contains("src"));
    }

    @Test
    public void testDummyCommandNotWorking() {
        // given
        CLIRunner cliRunner = new CLIRunner();

        // when
        String output = cliRunner.exec("dummy");

        // then
        assertTrue(output == null);
    }

}
