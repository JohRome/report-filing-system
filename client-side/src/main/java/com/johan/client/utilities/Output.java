package com.johan.client.utilities;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for displaying output to the console, providing methods for printing menus, prompts, and error messages.
 */
@Slf4j
public class Output {

    /**
     * Displays the main menu options to the console.
     */
    public static void printPostToAPIMenu() {
        log.info("""
                ----------------------------------------- MENU OPTIONS -------------------------------------------------
                1 - File a Disturbance Report
                2 - Print all messages in a Kafka topic
                3 - Exit program
                ----------------------------------------- MAKE A CHOICE ------------------------------------------------
                """);
    }

    /**
     * Prints a custom prompt message to the console.
     *
     * @param prompt The prompt message to be displayed.
     */
    public static void printPrompt(String prompt) {
        // sout instead of log.info used because of unwanted new line break
        System.out.print(prompt);
    }

    /**
     * Prints an error message to the console.
     *
     * @param error The error message to be displayed.
     */
    public static void printError(String error) {
        // sout instead of log.info used because of unwanted new line break
        System.out.println(error);
    }
}
