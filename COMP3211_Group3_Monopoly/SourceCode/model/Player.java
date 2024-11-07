package SourceCode.model;

import java.util.ArrayList;

public class Player {
    private String PlayerName;
    private boolean Bankruptcy;
    private boolean Prison;
    private int Position;
    private ArrayList<Integer> PropertyIDs;

    public Player(String PlayerName){
        this.PlayerName = PlayerName;
        this.Position = 1;
        this.Bankruptcy = false;
        this.Prison = false;
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

    public int getPosition() {
        return this.Position;
    }

    public ArrayList<Integer> getPropertyIDs(){
        return this.PropertyIDs;
    }


    public void setPlayerName(String PlayerName) {
        //check the palyer ID has been exsit or not
        this.PlayerName = PlayerName;
    }

    public void setBankruptcy(Boolean Bankruptcy) {
        //check the palyer ID has been exsit or not
        this.Bankruptcy = Bankruptcy;
    }

    public void SetPrison(Boolean Prison) {
        //check the palyer ID has been exsit or not
        this.Prison = Prison;
    }

    public void setPosition(int Position) {
        //check the palyer ID has been exsit or not
        this.Position = Position;
    }

    public void setPropertyIDs(){
        //
    }

    
}
