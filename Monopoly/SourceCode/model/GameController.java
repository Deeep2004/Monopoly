package SourceCode.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GameController {
    public static ArrayList<Player> Players = new ArrayList<Player>();
    public static Standard_Square[] squares = new Standard_Square[20];
    public ArrayList<String> SavedGameboard = new ArrayList<String>();
    public ArrayList<String> SavedGame = new ArrayList<String>();
    public static int Round = 1;
    public static int Turn = 1;
    private final Random randnumber = new Random();
    Scanner scanner = new Scanner(System.in);

    public void setupfile() {
        String directoryPath1 = "SourceCode/file/gameboard"; // Change this to your folder path
        String directoryPath2 = "SourceCode/file/game"; // Change this to your folder path

        // Create an ArrayList to hold the file names

        // Create a File object for the directory
        File directory1 = new File(directoryPath1);
        File directory2 = new File(directoryPath2);

        // Check if the directory exists and is indeed a directory
        if (directory1.exists() && directory1.isDirectory()) {
            // List all files in the directory
            File[] files = directory1.listFiles();

            // Check if the files array is not null
            if (files != null) {
                for (File file : files) {
                    // Add the file name to the ArrayList
                    if (file.isFile()) { // Check if it's a file (not a directory)
                        SavedGameboard.add(file.getName());
                    }
                }
            }
        }

        // System.out.println("Files in the saved gameboard:");
        // for (String fileName : SavedGameboard) {
        // System.out.println(fileName);
        // }

        if (directory2.exists() && directory2.isDirectory()) {
            // List all files in the directory
            File[] files = directory2.listFiles();

            // Check if the files array is not null
            if (files != null) {
                for (File file : files) {
                    // Add the file name to the ArrayList
                    if (file.isFile()) { // Check if it's a file (not a directory)
                        SavedGame.add(file.getName());
                    }
                }
            }
        }

        // System.out.println("Files in the saved game:");
        // for (String fileName : SavedGame) {
        // System.out.println(fileName);
        // }
    }

    public boolean start() {
        int currentNumPlayers = 0;
        for (int i = 0; i < Players.size(); i++) {
            if (!Players.get(i).getBankruptcy()) {
                currentNumPlayers++;
            }
        }

        while (Round <= 100 && currentNumPlayers > 1) {
            // Declare each round:
            System.out.println("\n\n------------------------------------");
            System.out.printf("-------------Round %d --------------\n", Round);
            System.out.println("------------------------------------");
            boolean flag = true;

            while (flag) {
                System.out.println("\nPick an option below:");
                System.out.println("1. Start this round.");
                System.out.println("2. Check the status of any specific player and all players.");
                System.out.println("3. Check the status of the game");
                System.out.println("4. Save the current game and play later.");
                System.out.print(">>");
                String option = scanner.nextLine();
                try {
                    int number = Integer.parseInt(option);
                    if (number < 1 || number > 4) {
                        System.out.println("Invalid input. Please enter a valid integer between 1 and 4.");
                    } else {
                        switch (number) {
                            case 1:
                                int num = Round();
                                currentNumPlayers = currentNumPlayers - num;
                                Round++;
                                flag = false;
                                break;

                            case 2:
                                checkPlayerStatus(false, 1);
                                break;

                            case 3:
                                checkGameStatus();
                                break;

                            case 4:
                                System.out.print("Please enter the gameboard name >>");
                                String file_name = scanner.nextLine();
                                SaveGame(file_name);
                                return true;

                        }
                    }
                }

                catch (NumberFormatException e) {
                    System.out.print("Invalid input. Please enter a valid integer between 1 and 4. >>");
                }
            }

        }
        System.out.printf("The game ends at round %d.\n", Round);

        System.out.print("\n-------------Final Leadboard--------------\n");
        for (int n = 0; n < Players.size(); n++) {
            System.out.printf("Name: %s. ", Players.get(n).getPlayerName());
            if (Players.get(n).getBankruptcy()) {
                System.out.print("[Out of the game] \n");
            } else {
                System.out.printf("Position: Square No.%d. ", Players.get(n).getPosition());
                System.out.printf("Money: %d \n", Players.get(n).getMoney());
            }
        }
        System.out.println("--------------------------------------------\n");

        int winner = 0;
        int richest = 0;
        for (int n = 0; n < Players.size(); n++) {
            if (Players.get(n).getBankruptcy()) {
                continue;
            }
            if (Players.get(n).getMoney() > richest) {
                winner = n;
            }
        }

        System.out.print("\n------------- Winner --------------\n");
        System.out.printf("------------ %s --------------\n", Players.get(winner).getPlayerName());

        return true;
    }

    private int Turn(int user) {

        // return values:
        // 0: user get bankrupt
        // 1: user in the jail
        // 2: normal

        // System.out.println("------------------------------------");
        System.out.printf("Player %s's turn: \n", Players.get(user).getPlayerName());
        System.out.println("------------------------------------");
        System.out.println("");

        // roll the dice
        int step[] = DoubleDice();
        int steps = step[0] + step[1];

        

        boolean flag = true;
        while (flag) {
            System.out.println("Pick an option below:");
            System.out.println("1. Start my turn.");
            System.out.println("2. Query the next player.");
            System.out.print(">>");
            String option = scanner.nextLine();
            try {
                int opt = Integer.parseInt(option);
                switch (opt) {
                    case 1:
                        flag = false;
                        break;
                
                    case 2:
                        checkPlayerStatus(true, Turn-1);
                        break;

                    default:
                        System.out.print("Invalid input. Please enter a valid integer between 1 and 2. >>");
                        break;
                }
            }
            
            catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid integer between 1 and 2. >>");
            }
        }
       

        if (Players.get(user).getPrison()) {
            int status = inTheJail(user, step);
            switch (status) {
                case 0:
                    return 0;
                case 1:
                    Jail(false, user);
                    break;
                case 2:
                    return 1;
            }
        } else {
            System.out.println("Rolling the dice...\n");
        }

        System.out.printf("Move %d steps \n", steps);
        int current_step = 1;
        int position = Players.get(user).getPosition() - 1;

        while (current_step <= steps) {
            position++;
            if (position == 20) {
                position = 0;
            }
            int type = squares[position].getType();

            // System.out.println("_______________________");
            // System.out.printf("Test: you currently at %d \n", position);
            // System.out.println("_______________________");

            switch (type) {

                case 1:
                    // "Property";

                    if (current_step != steps) {
                        System.out.printf("Passed position %d, %s \n", position + 1, squares[position].getName());
                        break;
                    }

                    // The property is owned
                    if (squares[position].getOwnerID() != -1) {
                        if (squares[position].getOwnerID() == user) {
                            System.out.println("You landed on your own property, had a nice time there!");
                        }
                        int rent = squares[position].getRent();
                        int owner = squares[position].getOwnerID();
                        if (Players.get(user).getMoney() < rent) {
                            Players.get(owner).addMoney(Players.get(user).getMoney());
                            return 0;
                        } else {
                            Players.get(owner).addMoney(rent);
                            Players.get(user).lossMoney(rent);
                            System.out.printf(
                                    "You landed on an owned property %s, you paid %d HKD, and your current money is %d HKD \n",
                                    squares[position].getName(), rent, Players.get(user).getMoney());
                            break;
                        }
                    }

                    // The property is not owned
                    else {
                        int price = squares[position].getPrice();
                        int money = Players.get(user).getMoney();
                        if (money < price) {
                            break;
                        } else {
                            System.out.printf(
                                    "You landed on an unowned property %s, the price is %d HKD, you currently have %d HKD \n",
                                    squares[position].getName(), price, money);
                            System.out.println("Pick an option below:");
                            System.out.println("1. Buy");
                            System.out.println("2. Do not buy");
                            System.out.print(">>");

                            while (true) {
                                try {
                                    String option = scanner.nextLine();
                                    int opt = Integer.parseInt(option);
                                    if (opt <= 0 || opt > 2) {
                                        System.out.print("Input an invail number>>");
                                        continue;
                                    } else {
                                        if (opt == 1) {
                                            Players.get(user).lossMoney(price);
                                            squares[position].setOwnerID(user);
                                            Players.get(user).setPropertyIDs(position);
                                            System.out.printf(
                                                    "You bought a property, you currently have %d HKD \n",
                                                    money - price);
                                        }
                                        break;
                                    }

                                } catch (NumberFormatException e) {
                                    System.out.print(
                                            "Invalid input. Please enter a valid integer >>");
                                    continue;
                                }
                            }
                        }
                    }
                    break;

                case 2:
                    // "Go";
                    System.out.println("Congrats, you passed a Go square, you get 1500 HKD.");
                    Players.get(user).addMoney(1500);
                    System.out.printf("Your current money: %d HKD. \n", Players.get(user).getMoney());
                    break;

                case 3:
                    // "Chance";
                    if (current_step == steps) {
                        int amount = Chance();

                        if (amount < 0) {
                            System.out.printf("Sorry, you passed a CHANCE square, you loss %d HKD. ",
                                    -amount);

                            if (Players.get(user).setBankruptcy(Players.get(user).lossMoney(amount))) {
                                retires(user);
                                return 0;
                            }

                            System.out.printf("Your current money: %d HKD. \n",
                                    Players.get(user).getMoney());

                        } else {
                            System.out.printf("Congrats, you passed a CHANCE square, you get %d HKD. ",
                                    amount);
                            Players.get(user).addMoney(amount);
                            System.out.printf("Your current money: %d HKD. \n",
                                    Players.get(user).getMoney());
                        }
                    } else {
                        System.out.printf("Passed position %d. Chance. \n", position + 1);
                    }
                    break;

                case 4:
                    // "Income Tax";
                    if (current_step == steps) {
                        int tax = Tax(user);
                        System.out.printf("Sorry, you passed a TAX square, you loss %d HKD. \n", tax);
                        Players.get(user).lossMoney(tax);
                        System.out.printf("Your current money: %d HKD. \n", Players.get(user).getMoney());
                    } else {
                        System.out.printf("Passed position %d. Income Tax. \n", position + 1);
                    }
                    break;

                case 5:
                    // "Free Parking";
                    if (current_step == steps) {
                        System.out.println("You landed on a Free Parking square, you are having a nice time there~");
                        break;
                    }
                    System.out.println("You passed a Free Parking square, you had a nice time there~");
                    break;

                case 6:
                    // "Go to Jail";
                    if (current_step == steps) {
                        Jail(true, user);
                        return 1;
                    } else {
                        System.out.printf("Passed position %d. Go to Jail. \n", position + 1);
                    }
                    break;

                case 7:
                    // jail
                    if (current_step == steps) {
                        System.out.printf("Landed on position %d. Visiting Jail. \n", position + 1);
                    }
                    System.out.printf("Passed position %d. Visiting Jail. \n", position + 1);
                    break;
            }
            current_step++;
        }

        Players.get(user).setPosition(position + 1);
        System.out.printf("You finally reached Square No.%d \n\n", position + 1);
        return 2;

    }

    private int Round() {

        int numofplayergetbankrupt = 0;

        // Print the leadboard:
        int num_palyer = Players.size();
        System.out.print("\n-------------Current Leadboard--------------\n");
        for (int n = 0; n < num_palyer; n++) {
            System.out.printf("Name: %s. ", Players.get(n).getPlayerName());
            if (Players.get(n).getBankruptcy()) {
                System.out.print("[Out of the game] \n");
            } else {
                System.out.printf("Position: Square No.%d. ", Players.get(n).getPosition());
                System.out.printf("Money: %d \n", Players.get(n).getMoney());
            }
        }
        System.out.println("--------------------------------------------\n");

        // Start each Turn
        for (int n = 0; n < num_palyer; n++) {

            // skip the user who is bankrupt
            if (Players.get(n).getBankruptcy()) {
                continue;
            }

            else {
                // System.out.println(null);
                int return_value = Turn(n);

                // return values:
                // 0: user get bankrupt
                // 1: user in the jail
                // 2: normal

                switch (return_value) {
                    case 0:
                        System.out.println("You got bankrupt, you are out of the game\n");
                        numofplayergetbankrupt++;
                        break;

                    case 1:
                        break;

                    case 2:
                        break;
                }
            }
        }
        return numofplayergetbankrupt;

    }

    public void checkPlayerStatus(boolean queryOrCheck, int player) {

        if (queryOrCheck) {
            int nextplayer = player + 1;
            if (nextplayer > Players.size()) {
                nextplayer = 0;
            }
            Player next = Players.get(nextplayer);
            System.out.println("The information of players:");
            System.out.printf("Name: %s \n", next.getPlayerName());
            if (next.getBankruptcy()) {
                System.out.println("---Out of the game!---");
                return;
            }
            System.out.printf("Current Position: Square no.%d \n", next.getPosition());
            System.out.printf("Current Money: %d \n", next.getMoney());
            System.out.printf("Whether in Prison or not: %b \n", next.getPrison());
            ArrayList<Integer> CurrentProperty = new ArrayList<Integer>();
            CurrentProperty = next.getProperty();
            System.out.print("The properties this palyer owns:");
            if (CurrentProperty.size() == 0) {
                System.out.print(" Null");
            } else {
                System.out.print("\n");
                for (int j = 0; j < CurrentProperty.size(); j++) {
                    System.out.printf("Position: %d, Name: %s \n",
                            squares[CurrentProperty.get(j)].getPosition(),
                            squares[CurrentProperty.get(j)].getName());
                }
            }
            System.out.println("");

        }

        else {
            System.out.println("Pick the player you would like to check:");
            int num_players = Players.size();
            int i = 1;
            for (; i < Players.size();) {
                System.out.printf("%d. %s. \n", i, Players.get(i - 1).getPlayerName());
                i++;
            }
            System.out.printf("%d. All players.\n", i);
            System.out.print(">>");

            String GameMode = scanner.nextLine();
            try {
                int num = Integer.parseInt(GameMode);
                if (num <= 0 || num > num_players + 1) {
                    System.out.println("Invaild palyer!");
                } else {
                    if (num == i) {
                        System.out.print("\n");
                        System.out.println("The information of players:");
                        for (int n = 0; n < Players.size(); n++) {
                            System.out.print("\n");
                            System.out.printf("Name: %s \n", Players.get(n).getPlayerName());
                            if (Players.get(n).getBankruptcy()) {
                                System.out.println("---Out of the game!---");
                                continue;
                            }
                            System.out.printf("Current Position: Square no.%d \n", Players.get(n).getPosition());
                            System.out.printf("Current Money: %d \n", Players.get(n).getMoney());
                            System.out.printf("Whether in Prison or not: %b \n", Players.get(n).getPrison());
                            ArrayList<Integer> CurrentProperty = new ArrayList<Integer>();
                            CurrentProperty = Players.get(n).getProperty();
                            System.out.print("The properties this palyer owns:");
                            if (CurrentProperty.size() == 0) {
                                System.out.print(" Null");
                            } else {
                                System.out.print("\n");
                                for (int j = 0; j < CurrentProperty.size(); j++) {
                                    System.out.printf("Position: %d, Name: %s \n",
                                            squares[CurrentProperty.get(j)].getPosition(),
                                            squares[CurrentProperty.get(j)].getName());
                                }
                            }
                            System.out.println("");
                        }
                    } else {
                        Player currentPlayer = Players.get(i - 1);
                        System.out.println("\n");
                        System.out.println("The information of picked player:");
                        System.out.printf("Name: %s \n", currentPlayer.getPlayerName());
                        if (currentPlayer.getBankruptcy()) {
                            System.out.println("---Out of the game!---");
                            return;
                        }
                        System.out.printf("Current Position: Square no.%d \n", currentPlayer.getPosition());
                        System.out.printf("Current Money: %d \n", currentPlayer.getMoney());
                        System.out.printf("In Prison or not: %b \n", currentPlayer.getPrison());
                        ArrayList<Integer> CurrentProperty = new ArrayList<Integer>();
                        CurrentProperty = currentPlayer.getProperty();
                        System.out.println("The properties this palyer owns:");
                        if (CurrentProperty.size() == 0) {
                            System.out.print("Null\n");
                        } else {
                            System.out.println("\n");
                            for (int j = 0; j < CurrentProperty.size(); j++) {
                                System.out.printf("Position: %d, Name: %s \n",
                                        squares[CurrentProperty.get(j)].getPosition(),
                                        squares[CurrentProperty.get(j)].getName());
                            }
                            System.out.print("\n");
                        }

                    }
                }
                System.out.println("\n");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid integer. >>");
            }
        }

    }

    public void checkGameStatus() {
        // including the squares and the players positions on the gameboard.
        System.out.println("Current squares on the gameboard: ");
        for (int i = 0; i < 20; i++) {
            if (squares[i].getType() == 1) { // property
                System.out.printf("%d. Property: %s, Price: %d, Rent: %d.\n", i + 1, squares[i].getName(),
                        squares[i].getPrice(),
                        squares[i].getRent());
            } else {
                int type = squares[i].getType();
                System.out.printf("%d. %s\n", i, squares[i].getType(type));
            }
        }
        System.out.println("");

        for (int n = 0; n < Players.size(); n++) {
            System.out.println("The information of players' position:");
            System.out.printf("Name: %s. ", Players.get(n).getPlayerName());
            System.out.printf("Current Position: Square no.%d \n", Players.get(n).getPosition());
        }

    }

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

    private int[] DoubleDice() {
        int[] DiceNum = new int[2];
        DiceNum[0] = randnumber.nextInt(6) + 1;
        DiceNum[1] = randnumber.nextInt(6) + 1;
        return DiceNum;
    }

    private void Jail(boolean InorOut, int user) {
        if (InorOut) {
            // in
            Players.get(user).SetPrison(true);
            Players.get(user).RoundsLeftInJail = 3;
            Players.get(user).setPosition(6);
            System.out.println("Sorry, you landed on a Jail, wait until the next round");
        } else {
            // out
            Players.get(user).SetPrison(false);
            Players.get(user).RoundsLeftInJail = 0;
        }
    }

    private int inTheJail(int user, int[] dice) {

        // return value:
        // 0: indicate the user get bankrupt
        // 1: indicate the user get out off the jail.
        // 2: indicate the user still in the jail

        System.out.printf("You are in the jail now, %d rounds to stay.\n",
                Players.get(user).RoundsLeftInJail);

        // if the rounds left in the jail is only one left:
        if (Players.get(user).RoundsLeftInJail == 1) {
            System.out.println("It is the last round in the jail, so you have to pay the bail.");

            // if the user get bankrupt after paid the bail.
            if (Players.get(user).lossMoney(150)) {
                retires(user);
                return 0; // indicate the user get bankrupt
            }

            // if the user has enough money
            else {
                System.out.printf("You have paid the bail, the money you have now is %d \n",
                        Players.get(user).getMoney());
                return 1; // indidcate the user get out off the jail.
            }
        }

        // more than one rounds left.
        else {
            System.out.println("You may choose one of the two options below:");
            System.out.println("1. Pay a bail of HKD 150.");
            System.out.println("2. Roll the dices (Bailed if two dices get the same number.).");
            System.out.print(">>");

            while (true) {
                try {
                    String option = scanner.nextLine();
                    int num = Integer.parseInt(option);

                    if (num < 0 || num > 2) {
                        System.out.print("Invalid input. Please enter a valid integer.>>");
                        continue;
                    }

                    else {
                        if (num == 1) {
                            if (Players.get(user).getMoney() >= 150) {
                                Players.get(user).lossMoney(150);
                                System.out.printf("You have paid the bail, the money you have now is %d \n",
                                        Players.get(user).getMoney());

                                return 1;

                            } else {
                                System.out.println("You don't have enough money.");
                            }
                        }

                        System.out.println("Rolling the dice...");
                        System.out.printf("First dice shows: %d, second shows %d\n", dice[0], dice[1]);

                        if (dice[0] == dice[1]) {
                            System.out.println("Congrats, you get bailed.");

                            return 1;

                        } else {
                            System.out.println("Sorry, try next time.");
                            Players.get(user).RoundsLeftInJail--;

                            return 2;
                        }
                    }

                } catch (NumberFormatException e) {
                    System.out.print("Invalid input. Try another integher>>");
                    continue;
                }

            }

        }

    }

    private int Chance() {
        Random random = new Random();
        int choice = random.nextInt(2); // Randomly choose between 0 and 1
        if (choice == 0) {
            // Generate a random negative number (multiple of 10) between -10 and -200
            int randomNegative = (random.nextInt(20) + 1) * -10; // Generates -10, -20, ..., -200
            return randomNegative;
        } else {
            // Generate a random positive number (multiple of 10) between 10 and 300
            int randomPositive = random.nextInt(31) * 10; // Generates 0, 10, 20, ..., 300
            return randomPositive;
        }
    }

    private int Tax(int Player) {
        int money = Players.get(Player).getMoney();
        double tenPercent = money * 0.10;
        // Round down to the nearest multiple of 10
        int roundedTax = (int) Math.floor(tenPercent / 10) * 10;
        return roundedTax;
    }

    private void retires(int player) {
        System.out.println("Sorry to inform you that you lose this game, as your money is negative now!");
        Players.get(player).setBankruptcy(true);
        ArrayList<Integer> CurrentProperty = new ArrayList<Integer>();
        CurrentProperty = Players.get(player).getProperty();
        for (int j = 0; j < CurrentProperty.size(); j++) {
            squares[CurrentProperty.get(j)].setOwnerID(-1);
        }
    }

    public void GameboardSetup(boolean customizable) {
        // if customizable is true then it leads to by-default gameboard, otherwise to
        // customize function
        if (customizable) {
            LoadGameboardFromFile("DefaultGameboard", true);
        } else {
            NewGameboard();
        }
    }

    public void NewGameboard() {

        System.out.println("");
        System.out.println("Please select a gameboard you wish to modify, current saved gameboards: ");
        for (int i = 0; i < SavedGameboard.size(); i++) {
            System.out.printf("%d. %s \n", i + 1, SavedGameboard.get(i));
        }
        System.out.println("");

        while (true) {
            System.out.println("Pick the one gameboard on which you would like to customise based>> ");
            String gameboard = scanner.nextLine();
            try {
                int number = Integer.parseInt(gameboard);
                if (number < 1 || number > SavedGameboard.size()) {
                    System.out.println("Invaild input!");
                } else {
                    String fileName = SavedGameboard.get(number - 1);
                    LoadGameboardFromFile(fileName, true);
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
            String text = scanner.nextLine();
            if (text.equals("Quit")) {
                while (true) {
                    System.out.println(
                            "All changes saved! Do you wish to save this new gameboard, you can use it next time?");
                    System.out.println("1. No, I just wish to play this gameboard one time.");
                    System.out.println("2. Yes, I wish to save this gameboard.");
                    System.out.print(">>");
                    String option = scanner.nextLine();
                    try {
                        int number = Integer.parseInt(option);
                        if (number == 1) {
                            System.out.println("Sure, you can continue the game.");
                            scanner.close();
                            return;
                        } else if (number == 2) {
                            System.out.print("Sure, please enter the gameboard name >>");
                            String file_name = scanner.nextLine();
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
                                    "Pick the one you wish to customize:\n 1. Name\n 2. Price\n 3. Rent\n 4. Quit\n");
                            try {
                                String text2 = scanner.nextLine();
                                if (text2.equals("4")) {
                                    System.out.println("All changes saved!");
                                    break;
                                }
                                int number2 = Integer.parseInt(text2);
                                if (number2 >= 1 && number2 <= 3) {
                                    switch (number2) {
                                        case 1:
                                            System.out.printf("The current name: %s, enter the new name >>\n",
                                                    squares[number].getName());
                                            String newName = scanner.nextLine();
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
                                            String text3 = scanner.nextLine();
                                            int newPrice = Integer.parseInt(text3);
                                            squares[number].setPrice(newPrice);
                                            System.out.printf("You have changed the price of square%d to %s! \n",
                                                    number, newPrice);
                                            break;

                                        case 3:
                                            System.out.printf("The current Rent: %d, enter the new Rent >>\n",
                                                    squares[number].getRent());
                                            String text4 = scanner.nextLine();
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

    private void SaveGameboard(String file_name) {
        // function to save the gameboard

        if (SavedGameboard.contains(file_name)) {
            while (true) {
                System.out.println(
                        "This gameboard name is alread there, do you wish to replace it or keep both?");
                System.out.println("1. Overwrite it.");
                System.out.println("2. Keep both.");
                System.out.print(">>");
                String num = scanner.nextLine();
                try {
                    int option = Integer.parseInt(num);
                    if (option == 1) {
                        SaveGameboardToFile(file_name, true);
                        return;
                    } else if (option == 2) {
                        int count = 0;
                        while (SavedGameboard.contains(file_name)) {
                            count++;
                            file_name = file_name + "_" + count;
                        }
                        SaveGameboardToFile(file_name, true);
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
        SaveGameboardToFile(file_name, true);
    }

    public static void SaveGameboardToFile(String filename, Boolean gameboardOrgame) {
        String fullPath = "SourceCode/file/gameboard/" + filename;
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

    public static void LoadGameboardFromFile(String filename, Boolean gameboardOrgame) {
        String fullpath = "SourceCode/file/gameboard/" + filename;
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
                // System.out.printf("Position: %d \n", position);
                return new Standard_Square(position, name, price, rent);// (int position, String name, int Price, int
                                                                        // Rent)

            default:
                // System.out.printf("Position: %d \n", position);
                return new Standard_Square(position, type);// (int position, String name, int Price, int Rent)
        }
    }

    private static String PalyerToString(int player) {
        Player currentPlayer = Players.get(player);
        return currentPlayer.getPlayerName() + "," + currentPlayer.getMoney() + "," + currentPlayer.getBankruptcy()
                + "," + currentPlayer.getPrison() + "," + currentPlayer.RoundsLeftInJail + ","
                + currentPlayer.getPosition();
    }

    private static Player StringToPlayer(String line) {
        String[] parts = line.split(",");
        String playerName = parts[0];
        int money = Integer.parseInt(parts[1]);
        boolean Bankruptcy = Boolean.parseBoolean(parts[2]);
        boolean Prision = Boolean.parseBoolean(parts[3]);
        int RoundsLeftInJail = Integer.parseInt(parts[4]);
        int position = Integer.parseInt(parts[5]);
        Player player = new Player(playerName, money, position, RoundsLeftInJail, Bankruptcy, Prision);
        // System.out.println(player.getPlayerName());
        return player;
    }

    public void SaveGame(String file_name) {

        String fullPath = "SourceCode/file/game/" + file_name;
        SavedGameboard.add(file_name);

        if (SavedGame.contains(file_name)) {
            while (true) {
                System.out.println(
                        "This game name is alread there, do you wish to replace it or keep both?");
                System.out.println("1. Overwrite it.");
                System.out.println("2. Keep both.");
                System.out.print(">>");
                String num = scanner.nextLine();
                try {
                    int option = Integer.parseInt(num);
                    if (option == 1) {
                        break;

                    } else if (option == 2) {
                        int count = 0;
                        while (SavedGameboard.contains(file_name)) {
                            count++;
                            file_name = file_name + "_" + count;
                        }
                        break;

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {

            writer.write(String.valueOf(Round));
            writer.newLine();
            writer.write(String.valueOf(Turn));
            writer.newLine();

            for (Standard_Square square : squares) {
                if (square != null) {
                    writer.write(squareToString(square));
                    writer.newLine();
                }
            }
            for (int i = 0; i < Players.size(); i++) {
                if (Players.get(i) != null) {
                    writer.write(PalyerToString(i));
                    writer.newLine();
                }
            }
            System.out.println("Your game is saved as " + file_name);
            SavedGame.add(file_name);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    public void LoadGame() {
        String fileName;
        System.out.println("The current saved files:");
        for (int i = 0; i < SavedGame.size(); i++) {
            System.out.printf("%d. %s \n", i + 1, SavedGame.get(i));
        }
        System.out.println("");

        while (true) {
            System.out.print("Please select a game you wish to continue playing with>> ");
            String game = scanner.nextLine();
            try {
                int number = Integer.parseInt(game);
                if (number < 1 || number > SavedGame.size()) {
                    System.out.println("Invaild input!");
                } else {
                    fileName = SavedGame.get(number - 1);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        String fullpath = "SourceCode/file/game/" + fileName;

        try (BufferedReader reader = new BufferedReader(new FileReader(fullpath))) {
            Round = Integer.parseInt(reader.readLine());
            Turn = Integer.parseInt(reader.readLine());

            String line;
            int index = 0;
            while (index < squares.length) {
                line = reader.readLine();
                squares[index] = stringToSquare(line);
                index++;
            }

            Players.clear();
            index = 0;
            while ((line = reader.readLine()) != null) {
                // System.out.println(line);
                Player player = StringToPlayer(line);
                // System.out.println(player.getPlayerName());
                Players.add(player);
                System.out.println(Players.size());
                // System.out.printf("The user added has the name of %s\n",
                // Players.get(index).getPlayerName());
                index++;
            }
            System.out.println("Game loaded from " + fileName);

            for (int i = 0; i < squares.length; i++) {
                if (squares[i].getOwnerID() != -1 && squares[i].getType() == 1) {
                    Players.get(squares[i].getOwnerID()).setPropertyIDs(i);
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading from file: " + e.getMessage());
        }

        start();
    }
}
