package ui;

import chess.ChessGame;
import model.GameData;
import server.*;

import java.util.HashMap;
import java.util.Scanner;

public class Client {

    static String currAuthToken = null;
    static ServerFacade facade = new ServerFacade();
    static HashMap<Integer, GameData> listOfGames = null;
    static GameData currGame = null;

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
            }

            else {
                System.out.println("\nPlease select one of the following options by entering the corresponding number.\n" +
                        "1: Play Game\n" +
                        "2: Create Game\n" +
                        "3: Observe Game\n" +
                        "4: List Games\n" +
                        "5: Help\n" +
                        "6: Logout");
                int selectedOption = scanner.nextInt();
                if (selectedOption == 1) {
                    if (listOfGames == null) {
                        System.out.println("Please go confirm game options by selecting 'List Games.'");
                    }
                    else {

                        System.out.println("Enter the number of the game you'd like to join: ");
                        Integer number = scanner.nextInt();

                        currGame = listOfGames.get(number);
                        if (currGame == null) {
                            System.out.println("Sorry! That game doesn't seem to exist.");
                        } else {
                            scanner.nextLine();
                            System.out.println("Which role would you like to play? (WHITE or BLACK): ");
                            String role = scanner.nextLine();
                            if (role.equals("WHITE") || role.equals("BLACK")) {
                                JoinRequest joinReq = new JoinRequest(currAuthToken, role, currGame.gameID());
                                var output = facade.joinFacade(joinReq);
                                if (output instanceof String){
                                    System.out.println(output);
                                } else {
                                    System.out.println("Joined successfully!!");
                                    ChessBoardUI boardUI = new ChessBoardUI(currGame.game().getBoard());
                                    boardUI.main();
                                }
                            } else { System.out.println("Sorry. Please try entering role color again.");}
                        }
                    }
                }
                else if (selectedOption == 2) {
                    scanner.nextLine();
                    System.out.println("New game name: ");
                    String name = scanner.nextLine();

                    CreateGameRequest cgReq = new CreateGameRequest(currAuthToken, name);
                    var output = facade.createGameFacade(cgReq);
                    if (output instanceof CreateGameResponse) {
                        System.out.println("Game created successfully!! Now you need to join.");
                    } else if (output instanceof String) {
                        System.out.println(output);
                    } else {
                        System.out.println("Error occurred but nothing was returned.");
                    }
                }
                else if (selectedOption == 3) {
                    System.out.println("You Selected: Observe Game");
                }
                else if (selectedOption == 4) {
                    ListRequest lisReq = new ListRequest(currAuthToken);
                    var output = facade.listFacade(lisReq);
                    if (output instanceof ListResponse) {
                        listOfGames = new HashMap<Integer, GameData>() {};
                        Integer counter = 1;
                        for (GameData game : ((ListResponse) output).games()) {
                            System.out.format("%d: %s, White User=%s, Black User=%s\n", counter, game.gameName(), game.whiteUsername(), game.blackUsername());
                            listOfGames.put(counter, game);
                            counter++;
                        }
                    } else if (output instanceof String) {
                        System.out.println(output);
                    } else {
                        System.out.println("Error occurred but nothing was returned.");
                    }
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
