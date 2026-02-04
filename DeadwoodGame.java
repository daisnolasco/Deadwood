import java.util.*;

public class DeadwoodGame {
    private int totalPlayers;
    int rank = 0;
    int credits = 0;
    private int totalDays = 4;
    private int currentDay;
    public ArrayList<Player> players = new ArrayList<Player>();
    private String playerInput;
  
    public boolean validInput = true;
    public boolean runGame = true;
    public static void main(String[] args) {
        DeadwoodGame game= new DeadwoodGame();
        
    }
    public int getCurrentDay(){
        return currentDay;
    }
   public int getTotalDay(){
        return totalDays;
    }
   


    public void getPlayers() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Number of Players (2-8).");
        totalPlayers = input.nextInt();
        while (validInput) {
            if (totalPlayers == 2 || totalPlayers == 3) {
                totalDays = 3;
                validInput=false;

            } else if (totalPlayers == 5) {
                credits = 2;
               validInput=false;

            } else if (totalPlayers == 6) {
                credits = 4;
            } else if (totalPlayers == 7 || totalPlayers == 8) {
                rank = 2;
                validInput=false;
            } else {
                System.out.println("Not valid Number ,Enter Number of Players (2-8).");

            }
            
        }
       
         for(int i=0; i<totalPlayers;i++){
            System.out.println("Enter Name for Player " + i+1);
            String playerName=input.nextLine();
            players.add( new Player(playerName, rank, credits, 0, null));
         }
        

    }
    public void startGame(){
        
    }
    

}
