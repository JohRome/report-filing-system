package com.johan.client.utilities;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for displaying output to the console, providing methods for printing menus, prompts, and error messages.
 */
@Slf4j
public class Output {

    public static void printMainMenu() {
        log.info("""
                       MAIN MENU OPTIONS
                 ----------------------------
                | 1 - Enter Kafka as User    |
                | 2 - Enter MongoDB as Admin |
                | 3 - Exit program           |
                 ----------------------------
                       MAKE A MENU CHOICE
                """);
    }

    public static void printKafkaMenu() {
        log.info("""
                       KAFKA MENU OPTIONS
                 -------------------------------
                | 1 - File a Disturbance Report |
                | 2 - Get messages in topic     |
                | 3 - Back to main menu         |
                 -------------------------------
                       MAKE A MENU CHOICE
                """);
    }

    public static void printMongoAdminMenu() {
        log.info("""
                  MONGO ADMIN MENU OPTIONS
                 ---------------------------
                | 1 - Print all reports     |
                | 2 - Patch a report by id  |
                | 3 - Delete a report by id |
                | 4 - Back to main menu     |
                 ---------------------------
                     MAKE A MENU CHOICE
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
