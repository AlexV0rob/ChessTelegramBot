package org.example;

/**
 * Does the main management
 */
public class MainLogic {
	/**
	 * Does the prime management calling auxiliary methods
	 * @param input keeps user's input
	 * @return outgoing message
	 */
	public String processInput(String input) {
		switch (input) {
		//If input is a command /start
		case "/start":
			return startCommand();
		//If input is a command /help
		case "/help":
			return helpCommand();
		//Just some text
		default:
			return echoMessage(input);
		}
	}
	
	/**
	 * Creates and returns /start message
	 * @return message which sends to /start command
	 */
	private String startCommand() {
		//Constant start line
		String startMessage = "Hello, fellow citizen! I'm a bot about chess. My current options are:\n";
		
		//List of abilities
		startMessage += " - return your messages;\n";
		startMessage += " - show help window;\n";
		
		//New line for more comfortable reading
		startMessage += "\n";
		
		//Constant end line
		startMessage += "This is all by now but the list of abilities will be updated during development. Type /help for more information.";
		return startMessage;
	}
	
	/**
	 * Creates and returns /help message
	 * @return message which sends to /help command
	 */
	private String helpCommand() {
		//Constant start line
		String helpMessage = "I can now:\n";
		
		//List of abilities
		helpMessage += " - return your messages;\n";
		helpMessage += " - show help window;\n";
		
		//New line for more comfortable reading
		helpMessage += "\n";
		
		//Constant line as header for command block
		helpMessage += "Currently available commands:\n";
		
		//List of commands
		helpMessage += "/help - shows this message\n";
		helpMessage += "/start - restarts bot\n";
		
		//New line for more comfortable reading
		helpMessage += "\n";
		
		//Constant end line
		helpMessage += "More oprions will be available soon.";
		return helpMessage;
	}
	
	/**
	 * Implements the echo functionality
	 * @return message which sends to any non-command input
	 */
	private String echoMessage(String message) {
		//Takes user's message and return it with a little change
		return "You have sent: " + message;
	}
}
