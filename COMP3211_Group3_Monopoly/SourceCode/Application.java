package SourceCode;

import java.util.*;
import SourceCode.model.GameController;

public class Application {
    public static void main(String[] args) {
        GameController GC = new GameController();
        GC.SavedGameboard.add("DefaultGameboard");
        // Initialize and run the system
        System.out.println("Welcome to Monopoly!");
        while (true) {
            System.out.println("Please select the game mode (Input the number only)");
            System.out.println("1. Start a new game");
            System.out.println("2. Continue a already existing games game");
            System.out.println("3. Design a gameboard");
            System.out.print(">>");

            Scanner scanner = new Scanner(System.in);
            String GameMode = scanner.nextLine();

            try {
                int number = Integer.parseInt(GameMode);

                if (number >= 1 && number <= 2) {
                    switch (number) {

                        case 1:
                            System.out.println("");
                            System.out.print("Please enter the number of players (2-6) >> ");
                            Scanner scanner2 = new Scanner(System.in);
                            String NumPlayers = scanner2.nextLine();

                            try {
                                int number2 = Integer.parseInt(NumPlayers);
                                if (number2 >= 2 && number2 <= 6) {
                                    for (int i = 0; i < number2; i++) {
                                        System.out.println("");
                                        System.out.printf("Name the Player %d >> ", i + 1);
                                        String Name = scanner.nextLine();
                                        if (!GC.addPlayer(Name)) {
                                            i--;
                                        }
                                    }
                                    System.out.println("");
                                    System.out.println("Game starting with " + NumPlayers + " players");
                                    System.out.println("");
                                    System.out.println("Please select the gameboard");
                                    System.out.println("1. System default gameboard.");
                                    System.out.println("2. Customized gameboards");
                                    System.out.print(">>");
                                    String gameboard = scanner2.nextLine();

                                    try {
                                        int num = Integer.parseInt(gameboard);

                                        switch (num) {
                                            case 1:
                                                System.out.println("");
                                                GC.GameboardSetup(true);
                                                break;
                                        
                                            case 2:
                                                System.out.println("");
                                                GC.GameboardSetup(false);
                                                break;

                                            default:
                                                break;
                                        }
                                    }
                                    catch (NumberFormatException e) {
                                        System.out.print(
                                                "Invalid input. Please enter a valid integer >>");
                                    }

                                    scanner.close();
                                    break;
                                } else {
                                    System.out.print("Please enter a valid number between 2 and 6 >> ");
                                    break;
                                }

                            } catch (NumberFormatException e) {
                                System.out.print(
                                        "Invalid input. Please enter a valid integer >>");
                            }
                            break;

                        case 2:
                            //
                            break;

                        default:
                            //
                            break;
                    }
                        

                } else {
                    System.out.print("Please enter a valid number >> ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid integer between 2 and 6. >>");
            }

            // System.out.println("Decide the order of player");
            //
        }
    }

}
