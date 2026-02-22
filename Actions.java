//Action class will get playerInput from Deadwood class and it will be validated and excuted in Actions class 
public class Actions {
    private Board board;
    private CastingOffice castingOffice;
    private Role role;
    private Dice dice;

   
    public Actions() {
        this.dice = new Dice(0);
        this.castingOffice = new CastingOffice();
        //Action constructor not yet implemented 


    }

    public void validateAction() {
        //checks if user entry is one of the provided actions 

    }

    public boolean validateMove(Player player, Room rooms) {
        return rooms.isAdjacent(rooms);
    }

    public boolean validateTakeRole(Player player, Role role) {
        // not implemented yet
        return true;
    }

    public boolean validateAct(Player player) {
        return player.isWorking();

    }

    public boolean validateRehearse(Player player) {
        Scene scene = player.getCurrentRoom().getCurrentScene();
        if (!player.isWorking()) {
            System.out.println("Your are not currenently working on a role");
            return false;
        }
        if (player.getRehearsalTokens() + 1 >= scene.getMovieBudget()) {
            System.out.println("Guaranteed Success You must Act");
          
            return false;

        }
        return true;
    }

    public boolean validateCanUpgrade(Player player, int newRank) {
        return castingOffice.validateCanUpgrade(player, newRank);
    }

    public void movePlayer(Player player, Room room) {
        //player is moved to adjacent room

    }

    public void takeRole() {
        //adds player  is given role 

    }
    public int rolldice(){
        return dice.roll();
    }

    public int[] rollBonusDice(int budget) {
        return dice.bonusDiceSorted(budget);
    }

    public void Act(Player player) {
        if(!validateAct(player)) {
            System.out.println("You are not working on a role!");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        if (currentRoom == null) {
            System.out.println("Error: Player not in a room,");
            return;
        }

        Scene scene = currentRoom.getCurrentScene();
        if (scene == null) {
            System.out.println("Error: No active scene in the room.");
            return;
        }

        int budget = scene.getMovieBudget();
        int roll = rolldice();
        int total = dice.addRehearsalBonus(roll, player.getRehearsalTokens());

        System.out.println("Rolled " + roll + " + " + player.getRehearsalTokens() +
                " rehearsal = " + total + " (budget " + budget + ")");

        Role role = player.getCurrentRole();
        if (total >= budget) {
            if (role.isStarringRole()) {
                player.addCredits(2);
                System.out.println("Act succeeded! Earned 2 credits.");
            } else {
                player.addCredits(1);
                player.addDollars(1);
                System.out.println("Act succeeded! Earned 1 credit.");
            }
            currentRoom.removeShotCounter();
            if (currentRoom.isSceneComeplete()) {
                wrapScene(currentRoom);
         }
        } else {
            if (!role.isStarringRole()) {
                player.addDollars(1);
                System.out.println("Act failed. Earned 1 dollar.");
            } else {
                System.out.println("Act failed. No reward.");
            }
        }
    }

    

    public void Rehearse() {
        //adds rehearsal  token and gets next player turn

    }

    public void upgradeRank(Player player, int newRank, CastingOffice.PaymentType paymentType) {
        //update player rank
        castingOffice.upgradePlayer(player, newRank, paymentType);
         

    }

    public void wrapScene(Room room) {
        //remove scenes in room,clear roles, 

    }

    public void payOut() {
        //pay players bonus based on bonus dice and player role

    }
    

}