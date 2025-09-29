package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for testing work of MainLogic class.
 */
class MainLogicTest {
	/**
	 * MainLogic's echoMessage method test
	 */
    @Test
    void echoMessageTest() {
        assertEquals("You have sent: TEST", new MainLogic().processInput("TEST"));
    }
    
    /**
	 * MainLogic's startCommand method test
	 */
    @Test
    void startCommandTest() {
        assertEquals("Hello, fellow citizen! I'm a bot about chess. My current options are:\n - return your messages;\n - show help window;\n\nThis is all by now but the list of abilities will be updated during development. Type /help for more information.", new MainLogic().processInput("/start"));
    }
    
    /**
	 * MainLogic's helpCommand method test
	 */
    @Test
    void helpCommandTest() {
        assertEquals("I can now:\n - return your messages;\n - show help window;\n\nCurrently available commands:\n/help - shows this message\n/start - restarts bot\n\nMore oprions will be available soon.", new MainLogic().processInput("/help"));
    }
}
