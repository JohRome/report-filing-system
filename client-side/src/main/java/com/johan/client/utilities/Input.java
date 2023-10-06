package com.johan.client.utilities;

import java.util.Scanner;
/**
 * Utility class for user input handling, providing methods for reading strings and integers from the console.
 */
public class Input {

    private final Scanner userInput;

    /**
     * Constructor for Input, initializes the Scanner for user input.
     */
    public Input() {
        this.userInput = new Scanner(System.in);
    }

    /**
     * Reads a string input from the user with a given prompt.
     *
     * @param prompt The prompt to display to the user.
     * @return The string input provided by the user.
     */
    public String stringInput(String prompt) {
        String stringInput;
        Output.printPrompt(prompt);

        while (true) {
            stringInput = userInput.nextLine();
            // regex is used here to check for unwanted characters, which are not supported by the HTTP Client
            boolean isCorrect = !stringInput.isEmpty() && !stringInput.isBlank() && !stringInput.matches(".*[åäöÅÄÖ].*");

            if (isCorrect)
                return stringInput;
            else
                Output.printError("Input mismatch! Try again, fool -> ");
        }
    }

    /**
     * Reads an integer input from the user.
     *
     * @return The integer input provided by the user.
     */
    public int integerInput() {
        int integerInput;

        try {
            // .nextLine() is used here instead of .nextInt() because of unwanted code jumps
            integerInput = Integer.parseInt(userInput.nextLine());
        } catch (NumberFormatException e) {
            Output.printError("Input mismatch! Try again, fool\n");
            integerInput = integerInput();
        }
        return integerInput;
    }
}
