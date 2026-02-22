import java.util.*;
import java.util.Random;

public class Dice {
    private int value;
    private int totalVal;
    private int[] lastRolls;
    private Random random;

   

    public Dice(int value) {
        this.value = 0;
        this.totalVal = 0;
        this.lastRolls = new int[0];
        this.random = new Random();

    }

    //Getter for val
    public int getValue() {
        return value;
    }
    //Setter for val
    public void setValue(int value) {
        this.value = value;
    }
    //getter for total val
    public int getTotalVal() {
        return totalVal;
    }
    //setter for total val
    public void setTotalVal(int totalVal) {
        this.totalVal = totalVal;
    }
    //Getter for Last rolls
    public int[] getLastRolls() {
        return lastRolls;
    }
    //Setter for Last rolls

    public void setLastRolls(int[] lastRolls) {
        this.lastRolls = lastRolls;
    }

    //rolls single die (returns val 1-6)
    public int roll() {
        value = random.nextInt(1, 7);
        return value;
    }
    //Add rehearsal bonus to roll value
    public int addRehearsalBonus(int val,int rehearsalTokens) {
        totalVal = val + rehearsalTokens;
        return totalVal;
    }

    //Roll multiple dice for bonus
    public int[] bonusDice(int budget) {
        int[] diceRolls = new int[budget];
        for (int i = 0; i < budget; i++) {
            diceRolls[i] = roll();
        }
        lastRolls = diceRolls;
        return diceRolls;
    }

    //Sorting array in desc order
    public int[] sortDesc(int[] values) {
        int[] sorted = values.clone();

        //Bubble sort in desc order
        for(int i = 0; i < sorted.length -1; i++) {
            for(int j = 0; j < sorted.length - i - 1; j++) {
                if(sorted[j] < sorted[j + 1]) {
                    int temp = sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }
        return sorted;
    }

    //bonus dice that returns sorted results
    public int[] bonusDiceSorted(int budget) {
        return sortDesc(bonusDice(budget));
    }

    //Display sorted rolls
    public void displaySortedRolls(int[] rolls) {
        int[] sorted = sortDesc(rolls);
        for (int i = 0; i < sorted.length; i++) {
            System.out.print(sorted[i]);
            if (i < sorted.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    
}