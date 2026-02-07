//Action class will get playerInput from Deadwood class and it will be validated and excuted in Actions class 
public class Actions {
    private Board board;
    private CastingOffice CastingOffice;
    private Dice dice;

   
    public Actions() {
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
        return player.isWorking();
    }

    public boolean validateCanUpdrade(Player player, int newRank) {
        return true;
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
    public int[] rollBonusDice(int budget){
       return dice.bonusDiceSorted(budget);
    }

    public void Act() {
        //roll dice, get budget  and check if dice>=budget

    }

    public void Rehearse() {
        //adds rehearsal  token and gets next player turn

    }

    public void upgradeRank() {
        //update player rank

    }

    public void wrapScene(Room room) {
        //remove scenes in room,clear roles, 

    }

    public void payOut() {
        //pay players bonus based on bonus dice and player role

    }
    

}
