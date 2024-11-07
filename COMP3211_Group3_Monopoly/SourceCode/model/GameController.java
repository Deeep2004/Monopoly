package SourceCode.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GameController {
    public ArrayList<Player> Players = new ArrayList<Player>();
    public static Standard_Square[] squares = new Standard_Square[20];
    public ArrayList<String> SavedGameboard = new ArrayList<String>();
    public ArrayList<String> SavedGame = new ArrayList<String>();
    public int Round;
    public int Turn;
    private final Random randnumber = new Random();

    public boolean addPlayer(String PlayerName) {
        for (Player player : Players) {
            if (player.getPlayerName().equalsIgnoreCase(PlayerName)) {
                System.out.println("PlayerName has existed, please try another one");
                return false;
            }
        }
        Player newPlayer = new Player(PlayerName);
        Players.add(newPlayer);
        System.out.printf("New player %s has been created\n", PlayerName);
        return true;
    }

    public int[] DoubleDice() {
        int[] DiceNum = new int[2];
        DiceNum[0] = randnumber.nextInt(6) + 1;
        DiceNum[1] = randnumber.nextInt(6) + 1;
        return DiceNum;
    }

    public boolean Jail() {
        return true;
    }

    public int Chance() {
        return -1;
    }

    public boolean Tax() {
        return true;
    }

    public void GameboardSetup(boolean customizable) {
        // if customizable is true then it leads to by-default gameboard, otherwise to
        // customize function
        if (customizable) {
            loadFromFile("DefaultGameboard", true);
        } else {
            System.out.println("");
            System.out.println("Pick one option below.");
            System.out.println("1. Load an existing gameboard and customize it");
            System.out.println("2. Design a new gameboard");
            System.out.print(">>");
            Scanner scan = new Scanner(System.in);
            String option = scan.nextLine();
            try {
                int number = Integer.parseInt(option);
                switch (number) {
                    case 1:
                        NewGameboard(true);
                        break;
                    case 2:
                        NewGameboard(false);
                        break;
                    default:
                        System.out.println("Input a valid number!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer between 1 and 20.");
            }
            scan.close();
        }
    }

    public void NewGameboard(boolean usingTemplate) {
        if (usingTemplate) {
            Scanner scan = new Scanner(System.in);
            System.out.println("");
            System.out.println("Current saved gameboards: ");
            for (int i = 0; i < SavedGameboard.size(); i++) {
                System.out.printf("%d. %s \n", i + 1, SavedGameboard.get(i));
            }
            System.out.println("");

            while (true){
                System.out.println("Pick the one gameboard on which you would like to customise based>> ");
                String gameboard = scan.nextLine();
                try {
                    int number = Integer.parseInt(gameboard);
                    if (number < 1 || number > SavedGameboard.size()) {
                        System.out.println("Invaild input!");
                    }
                    else{
                        String fileName = SavedGameboard.get(number - 1);
                        loadFromFile(fileName, true);
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            }
            
            System.out.println("");
            System.out.println("Current squares on the gameboard: ");

            for (int i = 0; i < 20; i++) {
                if (squares[i].getType() == 1) { // property
                    System.out.printf("%d. Property: %s, Price: %d, Rent: %d.\n", i, squares[i].getName(),
                            squares[i].getPrice(),
                            squares[i].getRent());
                } else {
                    int type = squares[i].getType();
                    System.out.printf("%d. %s\n", i, squares[i].getType(type));
                }
            }
            System.out.println("");

            while (true) {
                System.out.println("Enter the number of square you wish to customize (Stop by enter \"Quit\") >>  ");
                String text = scan.nextLine();
                if (text.equals("Quit")) {
                    while (true) {
                        System.out.println(
                                "All changes saved! Do you wish to save this new gameboard, you can use it next time?");
                        System.out.println("1. No, I just wish to play this gameboard one time.");
                        System.out.println("2. Yes, I wish to save this gameboard.");
                        System.out.print(">>");
                        String option = scan.nextLine();
                        try {
                            int number = Integer.parseInt(option);
                            if (number == 1) {
                                System.out.println("Sure, you can continue the game.");
                                scan.close();
                                return;
                            } else if (number == 2) {
                                System.out.print("Sure, please enter the gameboard name >>");
                                String file_name = scan.nextLine();
                                scan.close();
                                SaveGameboard(file_name);
                            } else {
                                System.out.println("Invaild input. Please enter a valid integer between 1 and 2");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid integer between 1 and 2.");
                        }
                    }
                }
                try {
                    int number = Integer.parseInt(text);
                    if (number >= 1 && number <= 20) {
                        if (squares[number].getType() == 1) {
                            // only customizable when the suqare is a property
                            System.out.printf("You have chosen the property%d: \n", number);
                            System.out.printf("Name: %s, Price: %d, Rent: %d \n", squares[number].getName(),
                                    squares[number].getPrice(), squares[number].getRent());
                            while (true) {
                                System.out.print(
                                        "Pick the one you wish to customize: 1. Name\n 2. Price\n 3. Rent\n 4. Quit\n");
                                try {
                                    String text2 = scan.nextLine();
                                    if (text == "4") {
                                        System.out.println("All changes saved!");
                                        break;
                                    }
                                    int number2 = Integer.parseInt(text2);
                                    if (number2 >= 1 && number2 <= 3) {
                                        switch (number2) {
                                            case 1:
                                                System.out.printf("The current name: %s, enter the new name >>\n",
                                                        squares[number].getName());
                                                String newName = scan.nextLine();
                                                for (int i = 0; i < 20; i++) {
                                                    if (newName.equals(squares[i].getName())) {
                                                        System.out.println("The name has existed.");
                                                        break;
                                                    }
                                                }
                                                System.out.printf("You have changed the name of square%d to %s! \n",
                                                        number, newName);
                                                break;

                                            case 2:
                                                System.out.printf("The current Price: %d, enter the new Price >>\n",
                                                        squares[number].getPrice());
                                                String text3 = scan.nextLine();
                                                int newPrice = Integer.parseInt(text3);
                                                squares[number].setPrice(newPrice);
                                                System.out.printf("You have changed the price of square%d to %s! \n",
                                                        number, newPrice);
                                                break;

                                            case 3:
                                                System.out.printf("The current Rent: %d, enter the new Rent >>\n",
                                                        squares[number].getRent());
                                                String text4 = scan.nextLine();
                                                int newRent = Integer.parseInt(text4);
                                                squares[number].setPrice(newRent);
                                                System.out.printf("You have changed the rent of square%d to %s! \n",
                                                        number, newRent);
                                                break;
                                        }
                                    } else {
                                        System.out
                                                .println(
                                                        "Number is out of range. Please enter a number between 1 and 4.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a valid integer between 1 and 4.");
                                }
                            }
                        } else {
                            System.out.println("This square has nothing to customize, please retry");
                        }
                    } else {
                        System.out.println("Number is out of range. Please enter a number between 1 and 20.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer between 1 and 20.");
                }
            }
        } 
        
        
        else { // design the gameboard from the start

        }

    }

    private void SaveGameboard(String file_name) {
        // function to save the gameboard
        Scanner scan = new Scanner(System.in);
        if (SavedGameboard.contains(file_name)) {
            while (true) {
                System.out.println(
                        "This gameboard name is alread there, do you wish to replace it or keep both?");
                System.out.println("1. Overwrite it.");
                System.out.println("2. Keep both.");
                System.out.print(">>");
                String num = scan.nextLine();
                try {
                    int option = Integer.parseInt(num);
                    if (option == 1) {
                        saveToFile(file_name, true);
                        scan.close();
                        return;
                    } else if (option == 2) {
                        int count = 0;
                        while (SavedGameboard.contains(file_name)) {
                            count++;
                            file_name = file_name + "_" + count;
                        }
                        saveToFile(file_name, true);
                        scan.close();
                        return;
                    } else {
                        System.out.println(
                                "Invalid input. Please enter a valid integer between 1 and 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(
                            "Invalid input. Please enter a valid integer between 1 and 2.");
                }

            }
            
        }
        saveToFile(file_name, true);
        scan.close();
    }

    public static void saveToFile(String filename, Boolean gameboardOrgame) {
        String fullPath = "SourceCode/file/" + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            for (Standard_Square square : squares) {
                if (square != null) {
                    writer.write(squareToString(square));
                    writer.newLine();
                }
            }
            System.out.println("Your gameboard is saved as " + filename);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    private static String squareToString(Standard_Square square) {
        return square.getPosition() + "," + square.getName() + "," + square.getPrice() + "," +
                square.getRent() + "," + square.getOwnerID() + "," + square.getType();
    }

    public static void loadFromFile(String filename, Boolean gameboardOrgame) {
        String fullpath = "SourceCode/file/" + filename;
        try (BufferedReader reader = new BufferedReader(new FileReader(fullpath))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < squares.length) {
                squares[index] = stringToSquare(line);
                index++;
            }
            System.out.println("Gameboard loaded from " + filename);
        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }
    }

    private static Standard_Square stringToSquare(String line) {
        String[] parts = line.split(",");
        int position = Integer.parseInt(parts[0]);
        String name = parts[1];
        int price = Integer.parseInt(parts[2]);
        int rent = Integer.parseInt(parts[3]);
        int type = Integer.parseInt(parts[5]);

        switch (type) {
            case 1: // property
                return new Standard_Square(position, name, price, rent);// (int position, String name, int Price, int
                                                                        // Rent)

            default:
                return new Standard_Square(position, type);// (int position, String name, int Price, int Rent)
        }
    }


    public static void SaveGame(String game_name){

    }

    public static void LoadGame(String game_name){

    }
}
