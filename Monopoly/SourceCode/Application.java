package SourceCode;

import java.util.*;
import SourceCode.model.GameController;

public class Application {
    public static void main(String[] args) {
        GameController GC = new GameController();
        Scanner scanner = new Scanner(System.in);
        // Initialize and run the system
        GC.setupfile();
        System.out.println("Welcome to Monopoly!");
        while (true) {
            System.out.println("Please select the game mode (Input the number only)");
            System.out.println("1. Start a new game");
            System.out.println("2. Continue a already existing game");
            System.out.println("3. Design a gameboard");
            System.out.println("4. Leave monopoly");
            System.out.print(">>");
            boolean continueGame = true;


            String GameMode = scanner.nextLine();

            try {
                int number = Integer.parseInt(GameMode);

                if (number >= 1 && number <= 4) {

                    switch (number) {

                        case 1:
                            System.out.println("");
                            System.out.print("Please enter the number of players (2-6) >> ");
                            String NumPlayers = scanner.nextLine();

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
                                    String gameboard = scanner.nextLine();

                                    try {
                                        int num = Integer.parseInt(gameboard);

                                        switch (num) {
                                            case 1:
                                                System.out.println("");
                                                GC.GameboardSetup(true);
                                                GC.start();
                                                break;

                                            case 2:
                                                System.out.println("");
                                                GC.GameboardSetup(false);
                                                GC.start();
                                                break;

                                            default:
                                                break;
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.print(
                                                "Invalid input. Please enter a valid integer >>");
                                    }

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
                            GC.LoadGame();
                            break;

                        case 3:
                            // Design a new gameboard;
                            GC.NewGameboard();
                            break;

                        case 4:
                            continueGame = false;
                            break;
                    }

                } else {
                    System.out.print("Please enter a valid number >> ");
                }

                

            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid integer between 2 and 6. >>");
            }
            
            if(!continueGame){
                break;
            }
            
        }
        System.out.println("\nThank you for playing Monopoly, byebye");
        scanner.close();
    }

}
