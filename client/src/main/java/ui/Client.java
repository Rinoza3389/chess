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
                System.out.println("\nPlease select one of the following options by entering the corresponding number.\n" +
                        "1: Play Game\n" +
                        "2: Create Game\n" +
                        "3: Observe Game\n" +
                        "4: List Games\n" +
                        "5: Help\n" +
                        "6: Logout");
                int selectedOption = scanner.nextInt();
                if (selectedOption == 1) {
                    System.out.println("You Selected: Play Game");
                }
                else if (selectedOption == 2) {
                    System.out.println("You Selected: Create Game");
                }
                else if (selectedOption == 3) {
                    System.out.println("You Selected: Observe Game");
                }
                else if (selectedOption == 4) {
                    System.out.println("You Selected: List Games");
                }
                else if (selectedOption == 5) {
                    System.out.println("Play Game: Allows you to join an existing game based on specified game number and color choice.\n" +
                            "Create Game: Allows you to create a new game with a name of your choosing. NOTE: You will not automatically join the new game.\n" +
                            "Observe Game: Allows you to observe an existing game based on a specified game number.\n" +
                            "List Games: Lists all the games that currently exist.\n" +
                            "Help: Pulls up this menu with information about each option.\n" +
                            "Logout: Allows you to logout and return to the main page.");
                }
                else if (selectedOption == 6) {
                    LogoutRequest logoutRequest = new LogoutRequest(currAuthToken);
                    var output = facade.logoutFacade(logoutRequest);
                    if (output==null) {
                        System.out.println("Logout Successful!!");
                        currAuthToken = null;
                    }
                    else if (output instanceof String) {
                        System.out.println(output);
                    }
                }
            }
        }

        // Close the scanner
        scanner.close();
    }
}
