package SourceCode.model;

import java.util.Random;

public class Standard_Square {
    private final int position; // where is the property on the gameboard
    private String name; // name of that property
    private int Price; // Price to buy it
    private int Rent; // Price for renting
    private int OwnerID; // -1 represents this property is not owned by any player so far
    private int type;
    // 1 indicate it's Property squares;
    // 2 indicate it's Go squares;
    // 3 indicate it's Chance squares;
    // 4 indicate it's Income tax squares;
    // 5 indicate it's Free parking squares;
    // 6 indicate it's Go to Jail squares;
    // -1 indicate its type hasn't been initialized

    // set the property
    public Standard_Square(int position, String name, int Price, int Rent) {
        this.position = position;
        this.name = name;
        this.Price = Price;
        this.Rent = Rent;
        this.OwnerID = -1;
        this.type = 1;
    }

    // set the other square types
    public Standard_Square(int position, int type) {
        this.position = position;
        this.type = type;
    }

    public String getType(int type) {
        switch (type) {
            case 1:
                return "Property";
            case 2:
                return "Go";
            case 3:
                return "Chance";
            case 4:
                return "Income Tax";
            case 5:
                return "Free Parking";
            case 6:
                return "Go to Jail";
            default:
                return null;
        }
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public int getType() {
        return type;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int newPrice) {
        Price = newPrice;
    }

    public int getRent() {
        return Rent;
    }

    public void setRent(int newRent) {
        Price = newRent;
    }

    public boolean Owned() {
        {
            return !(OwnerID == -1);
            // return true if not owned by any player, otherwise return false
        }
    }

    public int getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(int NewOwnerID) {
        OwnerID = NewOwnerID;
    }

    public void SendToJail(int UserID) {
        // this function will be implemented to put the user into the jail square.
    }

    public int getChance() {

        Random random = new Random();
        // Decide randomly whether to gain or lose
        boolean isGain = random.nextBoolean();

        if (isGain) {
            // Generate a random gain (multiple of 10) up to HKD200
            int maxGain = 20; // 20 * 10 = 200
            int gain = (random.nextInt(maxGain + 1)) * 10;
            return gain;
        } else {
            // Generate a random loss (multiple of 10) up to HKD300
            int maxLoss = 30; // 30 * 10 = 300
            int loss = (random.nextInt(maxLoss + 1)) * 10;
            return loss;
        }
    }

}