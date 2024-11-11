package ui;

import server.*;

import java.util.Scanner;

public class Client {

    static String currAuthToken = null;
    static ServerFacade facade = new ServerFacade();

    public static void main(String[] args) {
        // Create a Scanner object
        Scanner scanner = new Scanner(System.in);

        System.out.print("Welcome to Maddy's 240 Chess Project!!");

        while (true) {
            if (currAuthToken == null) {
                System.out.println("\nPlease select one of the following options by entering the corresponding number.\n" +
                        "1: Register\n" +
                        "2: Login\n" +
                        "3: Quit\n" +
                        "4: Help");
                int selectedOption = scanner.nextInt();
                if (selectedOption == 1) {
                    scanner.nextLine();
                    System.out.println("Username: ");
                    String user = scanner.nextLine();
                    System.out.println("Email: ");
                    String email = scanner.nextLine();
                    System.out.println("Password: ");
                    String pass = scanner.nextLine();

                    RegisterRequest regReq = new RegisterRequest(user, pass, email);
                    var output = facade.registerFacade(regReq);
                    if (output instanceof RegisterResponse) {
                        System.out.println("Registration successful!!");
                        currAuthToken = ((RegisterResponse) output).authToken();
                    } else if (output instanceof String) {
                        System.out.println(output);
                    } else {
                        System.out.println("Error occurred but nothing was returned.");
                    }
                } else if (selectedOption == 2) {
                    scanner.nextLine();
                    System.out.println("Username: ");
                    String user = scanner.nextLine();
                    System.out.println("Password: ");
                    String pass = scanner.nextLine();

                    LoginRequest logReq = new LoginRequest(user, pass);
                    var output = facade.loginFacade(logReq);
                    if (output instanceof LoginResponse) {
                        System.out.println("Login successful!!");
                        currAuthToken = ((LoginResponse) output).authToken();
                    } else if (output instanceof String) {
                        System.out.println(output);
                    } else {
                        System.out.println("Error occurred but nothing was returned.");
                    }
                } else if (selectedOption == 3) {
                    System.out.println("Goodbye!!");
                    break;
                } else if (selectedOption == 4) {
                    System.out.println("Register: Allows you to create an account and start playing! Will require a username, email, and password.\n" +
                            "Login: Allows you to sign into an already existing account and start playing! Will require a username and password.\n" +
                            "Quit: Allows you to exit the game.\n" +
                            "Help: Pulls up this menu with information about each option.\n");
                } else {
                    System.out.println("Invalid option entered. Please try again.");
                }
            } else {
                System.out.println("Still need to implement post-login UI");
                break;
            }
        }

        // Close the scanner
        scanner.close();
    }
}
