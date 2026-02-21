
//Action class will get playerInput from Deadwood class and it will be validated and excuted in Actions class 
import java.util.List;

public class Actions {
    private Board board;
    private CastingOffice castingOffice;
    private Role role;
    private Dice dice;
    private Room room;

    public Actions() {
        this.dice = new Dice(0);
        this.castingOffice = new CastingOffice();
        // Action constructor not yet implemented

    }

    public boolean validateAction(String input) {
        // checks if user entry is one of the provided actions
        String[] validActions = { "move", "take role", "act", "rehearse", "upgrade" };
        for (String action : validActions) {
            if (input.equalsIgnoreCase(action)) {
                return true;
            }
        }
        return false;

    }

    public boolean validateMove(Player player, Room rooms) {
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom == null)
            return false;
        return currentRoom.isAdjacent(rooms);
    }

    public boolean validateTakeRole(Player player, Role role) {
        // not implemented yet
        if (player.isWorking())
            return false;
        if (role.isOccupied())
            return false;
        if (player.getRank() < role.getRequiredRank())
            return false;
        return true;
    }

    public boolean validateAct(Player player) {
        return player.isWorking();

    }

    public boolean validateRehearse(Player player) {
        return player.isWorking();
    }

    public boolean validateCanUpgrade(Player player, int newRank) {
        return castingOffice.validateCanUpgrade(player, newRank);
    }

    public void movePlayer(Player player, Room room) {
        // player is moved to adjacent room
        if (!validateMove(player, room)) {
            System.out.println("Invalid move: not adjacent or no current room.");
            return;
        }
        Room currentRoom = player.getCurrentRoom();
        if (currentRoom != null) {
            currentRoom.removePlayer(player);
        }
        room.addPlayer(player);
        player.setCurrentRoom(room);
        System.out.println(player.getPlayerName() +
                " moved to " + room.getRoomName());

    }

    public void takeRole(Player player, Role role) {
        // adds player is given role
        if (!validateTakeRole(player, role)) {
            System.out.println("Cannot take this role.");
            return;
        }
        role.setAssignedPlayer(player);
        player.setCurrentRole(role);
        System.out.println(player.getPlayerName() + " took role: " + role.getRoleName());

    }

    public int rollDice() {
        return dice.roll();
    }

    public int[] rollBonusDice(int budget) {
        return dice.bonusDiceSorted(budget);
    }

    public void act(Player player) {
        if (!validateAct(player)) {
            System.out.println("You are not working on a role!");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        if (currentRoom == null) {
            System.out.println("Error: Player not in a room.");
            return;
        }

        Scene scene = currentRoom.getCurrentScene();
        if (scene == null) {
            System.out.println("Error: No active scene in the room.");
            return;
        }

        int budget = scene.getMovieBudget();
        int roll = rollDice();
        int total = roll + player.getRehearsalTokens(); 

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
                System.out.println("Act succeeded! Earned 1 credit and 1 dollar.");
            }
            currentRoom.removeShotCounter();
            if (currentRoom.isSceneComplete()) {
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

    public void upgradeRank(Player player, int newRank, CastingOffice.PaymentType paymentType) {
        // update player rank
        castingOffice.upgradePlayer(player, newRank, paymentType);

    }

    public void wrapScene(Room room) {
        // remove scenes in room,clear roles,
        System.out.println("Scene in" + room.getRoomName() + "is a wrap");
        Scene scene = room.getCurrentScene();
        if (scene == null)
            return;
        payOut(room, scene);

        List<Role> allRoles = room.getAllRoles();
        for (Role role : allRoles) {
            Player player = role.getAssignedPlayer();
            if (player != null) {
                player.setCurrentRole(null);
                player.resetTokens();
            }
            role.setAssignedPlayer(null);
        }
        room.removeScene();
        System.out.println("Scene cleared. Players are now free");

    }

    public void payOut(Room room, Scene scene) {
        // pay players bonus based on bonus dice and player role
        int budget = scene.getMovieBudget();
        int[] bonusDice = rollBonusDice(budget);
        List<Player> playersInroom = room.getPlayersInRoom();
        List<Role> starringRoles = scene.getStarRoles();
        int diceIndex = 0;

        if (!starringRoles.isEmpty()) {
            int numRoles = starringRoles.size();
            for (int i = 0; i < budget; i++) {
                Role role = starringRoles.get(i % numRoles);
                Player p = role.getAssignedPlayer();
                if (p != null) {
                    int bonus = bonusDice[i];
                    p.addDollars(bonus);
                    System.out.println(p.getPlayerName()
                            + "(starring) gets $" + bonus);
                }
            }
        }

    }

}