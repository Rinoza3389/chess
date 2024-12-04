package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import ui.reqres.*;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.*;

public class Client {

    static String currAuthToken = null;
    static final ServerFacade FACADE = new ServerFacade(8080);
    static HashMap<Integer, GameData> listOfGames = null;
    static GameData currGame = null;
    static String role = null;
    private static WebSocketFacade ws;
    private static NotificationHandler notifHandler = new NotificationHandler();

    static {
        try {
            ws = new WebSocketFacade(8080, notifHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Client() {

    }

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
                    register(scanner);
                } else if (selectedOption == 2) {
                    login(scanner);
                } else if (selectedOption == 3) {
                    System.out.println("Goodbye!!");
                    break;
                } else if (selectedOption == 4) {
                    System.out.println("Register: Allows you to create an account and start playing! " +
                            "Will require a username, email, and password.\n" +
                            "Login: Allows you to sign into an already existing account and start playing! " +
                            "Will require a username and password.\n" +
                            "Quit: Allows you to exit the game.\n" +
                            "Help: Pulls up this menu with information about each option.\n");
                } else {
                    System.out.println("Invalid option entered. Please try again.");
                }
            }
            else if (currGame == null){
                System.out.println("\nPlease select one of the following options by entering the corresponding number.\n" +
                        "1: Play Game\n" +
                        "2: Create Game\n" +
                        "3: Observe Game\n" +
                        "4: List Games\n" +
                        "5: Help\n" +
                        "6: Logout");
                int selectedOption = scanner.nextInt();
                if (selectedOption == 1) {
                    play(scanner);
                }
                else if (selectedOption == 2) {
                    create(scanner);
                }
                else if (selectedOption == 3) {
                    observe(scanner);
                }
                else if (selectedOption == 4) {
                    list();
                }
                else if (selectedOption == 5) {
                    System.out.println("Play Game: Allows you to join an existing game based on specified game number and color choice.\n" +
                            "Create Game: Allows you to create a new game with a name of your choosing. " +
                            "NOTE: You will not automatically join the new game.\n" +
                            "Observe Game: Allows you to observe an existing game based on a specified game number.\n" +
                            "List Games: Lists all the games that currently exist.\n" +
                            "Help: Pulls up this menu with information about each option.\n" +
                            "Logout: Allows you to logout and return to the run page.");
                }
                else if (selectedOption == 6) {
                    logout();
                } else {
                    System.out.println("Invalid option entered. Please try again.");
                }
            }

            else {
                System.out.println("\nPlease select one of the following options by entering the corresponding number.\n" +
                        "1: Make Move\n" +
                        "2: Highlight Legal Moves\n" +
                        "3: Redraw Chess Board\n" +
                        "4: Help\n" +
                        "5: Leave\n" +
                        "6: Resign");
                int selectedOption = scanner.nextInt();
                if (selectedOption == 1) {
                    makeMove(scanner);
                }
                else if (selectedOption == 2) {
                    highlight(scanner);
                }
                else if (selectedOption == 3) {
                    notifHandler.run(role);
                }
                else if (selectedOption == 4) {
                    System.out.println("Make Move: Allows you to input what move you want to make.\n" +
                            "Highlight Legal Moves: Allows you to input the piece for which you want ot highlight legal moves. " +
                            "The selected piece and all squares it can legally move to will be highlighted.\n" +
                            "Redraw Chess Board: Redraws the chess board on your screen.\n" +
                            "Help: Pulls up this menu with information about each option.\n" +
                            "Leave: Removes you from the game and sends you back to the main menu.\n" +
                            "Resign: You forfeit the game and the game is over. :(");
                }
                else if (selectedOption == 5) {
                    leave(scanner);
                }
                else if (selectedOption == 6) {
                    resign(scanner);
                }
                else {
                    System.out.println("Invalid option entered. Please try again.");
                }
            }
        }

        // Close the scanner
        scanner.close();
    }

    private static void register(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Username: ");
        String user = scanner.nextLine();
        System.out.println("Email: ");
        String email = scanner.nextLine();
        System.out.println("Password: ");
        String pass = scanner.nextLine();
        if (user.trim().isEmpty() || email.trim().isEmpty() || pass.trim().isEmpty()) {
            System.out.println("PLease enter valid input for each option.");
        } else {
            RegisterRequest regReq = new RegisterRequest(user, pass, email);
            var output = FACADE.registerFacade(regReq);
            if (output instanceof RegisterResponse) {
                System.out.println("Registration successful!!");
                currAuthToken = ((RegisterResponse) output).authToken();
            } else if (output instanceof String) {
                System.out.println(output);
            } else {
                System.out.println("Error occurred but nothing was returned.");
            }
        }
    }

    private static void login(Scanner scanner) {
        scanner.nextLine();
        System.out.println("Username: ");
        String user = scanner.nextLine();
        System.out.println("Password: ");
        String pass = scanner.nextLine();

        if (user.trim().isEmpty() || pass.trim().isEmpty()) {
            System.out.println("Please enter valid input for each option.");
        } else {
            LoginRequest logReq = new LoginRequest(user, pass);
            var output = FACADE.loginFacade(logReq);
            if (output instanceof LoginResponse) {
                System.out.println("Login successful!!");
                currAuthToken = ((LoginResponse) output).authToken();
            } else if (output instanceof String) {
                System.out.println(output);
            } else {
                System.out.println("Error occurred but nothing was returned.");
            }
        }

    }

    private static void play(Scanner scanner) {
        if (listOfGames == null) {
            System.out.println("Please go confirm game options by selecting 'List Games.'");
        }
        else {

            System.out.println("Enter the number of the game you'd like to join: ");
            try {
                Integer number = scanner.nextInt();
                currGame = listOfGames.get(number);
                if (currGame == null) {
                    System.out.println("Sorry! That game doesn't seem to exist.");
                } else {
                    scanner.nextLine();
                    System.out.println("Which role would you like to play? (WHITE or BLACK): ");
                    role = scanner.nextLine();
                    if (role.equals("WHITE") || role.equals("BLACK")) {
                        JoinRequest joinReq = new JoinRequest(currAuthToken, role, currGame.gameID());
                        var output = FACADE.joinFacade(joinReq);
                        if (output instanceof String){
                            System.out.println(output);
                            currGame = null;
                        } else {
                            try {
                                ws.connectToGame(currAuthToken, currGame.gameID());

                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                    else {
                        System.out.println("Sorry. Please try entering role color again.");
                        role = null;
                        currGame = null;
                    }
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Please enter a valid number");
                scanner.nextLine();
            }
        }
    }

    private static void create(Scanner scanner) {
        scanner.nextLine();
        System.out.println("New game name: ");
        String name = scanner.nextLine();

        if (name.trim().isEmpty()) {
            System.out.println("Please enter valid input for each option.");
        } else {
            CreateGameRequest cgReq = new CreateGameRequest(currAuthToken, name);
            var output = FACADE.createGameFacade(cgReq);
            if (output instanceof CreateGameResponse) {
                System.out.println("Game created successfully!! Now you need to join.");
            } else if (output instanceof String) {
                System.out.println(output);
            } else {
                System.out.println("Error occurred but nothing was returned.");
            }
        }
    }

    private static void observe(Scanner scanner) {
        if (listOfGames == null) {
            System.out.println("Please go confirm game options by selecting 'List Games.'");
        }
        else {
            System.out.println("Enter the number of the game you'd like to observe: ");
            Integer number = scanner.nextInt();

            currGame = listOfGames.get(number);
            if (currGame == null) {
                System.out.println("Sorry! That game doesn't seem to exist.");
            } else {
                try {
                    ws.connectToGame(currAuthToken, currGame.gameID());

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static void list() {
        ListRequest lisReq = new ListRequest(currAuthToken);
        var output = FACADE.listFacade(lisReq);
        if (output instanceof ListResponse) {
            listOfGames = new HashMap<>() {};
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

    private static void logout() {
        LogoutRequest logoutRequest = new LogoutRequest(currAuthToken);
        var output = FACADE.logoutFacade(logoutRequest);
        if (output==null) {
            System.out.println("Logout Successful!!");
            currAuthToken = null;
        }
        else if (output instanceof String) {
            System.out.println(output);
        }
    }

    private static ChessPosition getPos(Scanner scanner) {
        System.out.println("Enter the column letter (A-H): ");
        scanner.nextLine();
        String input = scanner.nextLine().trim();

        if (input.length() == 1 && input.matches("[A-Ha-h]")) {
            char letter = input.toUpperCase().charAt(0);

            if (letter >= 'A' && letter <= 'H') {
                int col = letter - 'A' + 1;
                System.out.println("Enter the row number (1-8): ");
                try {
                    int row = scanner.nextInt();
                    if (row >= 1 && row <= 8) {
                        return new ChessPosition(row, col);
                    } else {
                        System.out.println("Invalid row. Please enter a number from 1 to 8.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid row. Please enter a number from 1 to 8.");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Invalid column. Please enter a letter from A to H.");
            }
        } else {
            System.out.println("Invalid column. Please enter a letter from A to H.");
        }
        return null;
    }

    private static void highlight(Scanner scanner) {
        System.out.println("Please enter the coordinates for the piece you'd like to view legal moves for.");
        ChessPosition currentPos = getPos(scanner);
        if (currentPos != null) {
            Collection<ChessMove> legalMoves = notifHandler.getGame().validMoves(currentPos);
            if (legalMoves != null) {
                Set<ChessPosition> possPos = new HashSet<>();
                for (ChessMove move : legalMoves) {
                    possPos.add(move.getEndPosition());
                }
                notifHandler.highlight(role, possPos, currentPos);
            } else {
                System.out.println("There is no piece in this location.");
            }
        }
    }

    private static void makeMove(Scanner scanner) {
        System.out.println("Please enter the coordinates for the piece you'd like to move.");
        ChessPosition currentPos = getPos(scanner);
        System.out.println("Please enter the coordinates for the place you'd like to move to.");
        ChessPosition futurePos = getPos(scanner);
        ChessPiece currPiece = notifHandler.getGame().getBoard().getPiece(currentPos);
        ChessMove move = new ChessMove(currentPos, futurePos, null);
        if (currPiece != null && currPiece.getPieceType()== ChessPiece.PieceType.PAWN) {
            if ((role.equals("WHITE") && futurePos.getRow() == 8 && currPiece.getTeamColor() == ChessGame.TeamColor.WHITE)
                    || (role.equals("BLACK") && futurePos.getRow() == 1 && currPiece.getTeamColor() == ChessGame.TeamColor.BLACK))
            {
                System.out.println("Please enter your choice of promotion piece. (BISHOP, ROOK, KNIGHT, QUEEN)");
                String promo = scanner.nextLine();
                switch (promo) {
                    case "QUEEN" : move = new ChessMove(currentPos, futurePos, ChessPiece.PieceType.QUEEN);
                    case "BISHOP" : move = new ChessMove(currentPos, futurePos, ChessPiece.PieceType.BISHOP);
                    case "ROOK" : move = new ChessMove(currentPos, futurePos, ChessPiece.PieceType.ROOK);
                    case "KNIGHT" : move = new ChessMove(currentPos, futurePos, ChessPiece.PieceType.KNIGHT);
                    default :
                        System.out.println("Please enter a valid promo type.");
                        move = null;
                }

            }
        }
        if (move != null) {
            try {
                ws.makeMove(move, currAuthToken, currGame.gameID());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void resign(Scanner scanner) {
        System.out.println("Are you sure you want to resign?\n1. Yes\n2. No");
        int answer = scanner.nextInt();
        if (answer == 1) {
            try {
                ws.resignGame(currAuthToken, currGame.gameID());
            } catch (Exception e) {
                System.out.println("Error occurred while leaving the game.");
            }
        }
    }

    private static void leave(Scanner scanner) {
        try {
            ws.leaveGame(currAuthToken, currGame.gameID());
            role = null;
            currGame = null;
        } catch (Exception e) {
            System.out.println("Error occurred while leaving the game.");
        }
    }
}