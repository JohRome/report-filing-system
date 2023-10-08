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
    public static void printMainMenu() {
        log.info("""
                 ----------------------------------------- MENU OPTIONS -------------------------------------------------
                1 - Enter Kafka as a User
                2 - Enter MongoDB as an Admin
                3 - Exit program
                ----------------------------------------- MAKE A CHOICE ------------------------------------------------
                """);
    }

    public static void printKafkaMenu() {
        log.info("""
                ----------------------------------------- MENU OPTIONS -------------------------------------------------
                1 - File a Disturbance Report
                2 - Print all messages in a Kafka topic
                3 - Back to main menu
                ----------------------------------------- MAKE A CHOICE ------------------------------------------------
                """);
    }

    public static void printMongoAdminMenu() {
        log.info("""
                ----------------------------------------- MENU OPTIONS -------------------------------------------------
                1 - Print all reports
                2 - Patch a report by id
                3 - Delete a report by id
                4 - Back to main menu
                ----------------------------------------- MAKE A CHOICE ------------------------------------------------
                """);
    }

    public static void printPrompt(String prompt) {
        // sout instead of log.info used because of unwanted new line break
        System.out.print(prompt);
    }

    public static void printError(String error) {
        // sout instead of log.info used because of unwanted new line break
        System.out.print(error);
    }
}
