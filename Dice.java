import java.util.*;
import java.util.Random;

public class Dice {
    private int value;
    private int budgetDice;

   

    public Dice(int value) {
        this.value = value;

    }

    public int getBugetDice() {
        return budgetDice;

    }

    public void setBudgetDice(int budgetDice) {
        this.budgetDice = budgetDice;
    }

    Random random = new Random();

    public int roll() {
        value = random.nextInt(1, 7);
        return value;
    }

    public int[] rollBonusDice(int bugetDice) {
        int[] allBonusDie = new int[budgetDice];
        for (int i = 0; i < bugetDice; i++) {
            allBonusDie[i] = roll();

        }
        return allBonusDie;

    }

    public static void main(String[] args) {
        Dice game = new Dice(4);
        int[] rolls = game.rollBonusDice(4);

        System.out.print("Rolls: ");
        for (int roll : rolls) {
            System.out.print(roll + " ");
        }

    }
}