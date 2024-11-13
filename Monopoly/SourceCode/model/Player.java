package SourceCode.model;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private String PlayerName;
    private int Money; 
    private boolean Bankruptcy;
    private boolean Prison;
    public int RoundsLeftInJail;
    private int Position;
    private ArrayList<Integer> PropertyIDs;

    public Player(String PlayerName){
        this.PlayerName = PlayerName;
        this.Money = 1500;
        this.Position = 1;
        this.RoundsLeftInJail = 0;
        this.Bankruptcy = false;
        this.Prison = false;
        this.PropertyIDs = new ArrayList<Integer>();
    }

    public Player(String PlayerName, int Money, int Position, int RoundsLeftInJail, boolean Bankruptcy, boolean Prison){
        this.PlayerName = PlayerName;
        this.Money = Money;
        this.Position = Position;
        this.RoundsLeftInJail = RoundsLeftInJail;
        this.Bankruptcy = Bankruptcy;
        this.Prison = Prison;
        this.PropertyIDs = new ArrayList<Integer>();
    }

    public String getPlayerName() {
        return this.PlayerName;
    }

    public boolean getBankruptcy() {
        return this.Bankruptcy;
    }

    public boolean getPrison() {
        return this.Prison;
    }

    public int getMoney() {
        return this.Money;
    }

    public void addMoney(int amount){
        this.Money += amount;
    }

    public boolean lossMoney(int amount){
        int current_money = this.Money;
        this.Money = current_money -  amount;
        
        if(this.Money < 0){
            return true;
        }
        else{
            return false;
        }
    }

    public int getPosition() {
        return this.Position;
    }

    public ArrayList<Integer> getProperty(){
        return this.PropertyIDs;
    }

    public void setPlayerName(String PlayerName) {
        //check the palyer ID has been exsit or not
        this.PlayerName = PlayerName;
    }

    public boolean setBankruptcy(Boolean Bankruptcy) {
        //check the palyer ID has been exsit or not
        this.Bankruptcy = Bankruptcy;
        return Bankruptcy;
    }

    public void SetPrison(Boolean Prison) {
        //check the palyer ID has been exsit or not
        this.Prison = Prison;
    }

    public void setPosition(int Position) {
        //check the palyer ID has been exsit or not
        this.Position = Position;
    }

    public void setPropertyIDs(int position){
        this.PropertyIDs.add(position);
    }

    public static String[] generateUniquePlayerNames(int count) {
        String[] playerNames = new String[count];
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < count; i++) {
            String randomName;
            boolean isDuplicate;

            do {
                // Generate a random name of length 6
                StringBuilder nameBuilder = new StringBuilder();
                for (int j = 0; j < 6; j++) {
                    int index = random.nextInt(characters.length());
                    nameBuilder.append(characters.charAt(index));
                }
                randomName = nameBuilder.toString();

                // Check for duplicates in the playerNames array
                isDuplicate = false;
                for (int k = 0; k < i; k++) {
                    if (playerNames[k] != null && playerNames[k].equals(randomName)) {
                        isDuplicate = true; // Found a duplicate
                        break;
                    }
                }

            } while (isDuplicate); // Repeat if a duplicate is found

            playerNames[i] = randomName; // Assign the unique name to the array
        }

        return playerNames;
    }

    
}
