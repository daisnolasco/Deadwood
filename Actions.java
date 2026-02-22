
import java.util.ArrayList;
import java.util.List;

public class Actions {
    private Board board;
    private CastingOffice castingOffice;
    private Role role;
    private Dice dice;
    private Room currentRoom;

    public Actions(Board board) {
        this.dice = new Dice(0);
        this.castingOffice = new CastingOffice();
        this.board = board;

    }

    public boolean Move(Player player, String room) {
        Room newRoom = null;
        currentRoom = player.getCurrentRoom();
        List<Room> adjacent = currentRoom.getAdjacentRooms();
        String choice = room.trim();
        // if user enters room number add player to room by index
        if (choice.matches("\\d+")) {
            int roomNum = Integer.parseInt(room) - 1;
            if (roomNum < 0 || roomNum >= adjacent.size()) {
                System.out.println("Please select a valid set! or skip");
                return false;
            }
            newRoom = adjacent.get(roomNum);
        } else {
            // add play to room by room name
            for (Room r : adjacent) {
                if (choice.equalsIgnoreCase(r.getRoomName()) && currentRoom.isAdjacent(r)) {
                    newRoom = r;
                    break;

                }
            }
            if (newRoom == null) {
                System.out.println("Please select a valid set! or skip");
                return false;
            }

        }
        // adding player to new room
        currentRoom.removePlayer(player);
        player.setCurrentRoom(newRoom);
        newRoom.addPlayer(player);
        System.out.println("You have moved to " + newRoom.getRoomName() + " from " + currentRoom.getRoomName());
        newRoom.displaySetInfo();

        return true;

    }

    public void takeRole(Player player, String roleName) {
        currentRoom = player.getCurrentRoom();

        if (!currentRoom.isSet() || currentRoom.getCurrentScene() == null) {
            System.out.println("No roles in this set");
            return;
        }

        List<Role> availible = new ArrayList<>();
        for (Role rl : currentRoom.getAvailibleRoles()) {
            if (rl.getRequiredRank() <= player.getRank()) {
                availible.add(rl);
            }
        }
        if (availible.isEmpty()) {
            System.out.println("There are no availible roles in this room!");
            return;
        }
        // player enter role number -> parse int
        Role choice = null;
        if (roleName.matches("\\d+")) {
            int roleNum = Integer.parseInt(roleName) - 1;
            if (roleNum >= 0 && roleNum < availible.size()) {
                choice = availible.get(roleNum);
            }
        } // player enters role by name
        if (choice == null) {

            for (Role r : availible) {
                if (roleName.equalsIgnoreCase(r.getRoleName())) {
                    choice = r;
                    break;

                }

            }
        }
        if (choice == null) {
            System.out.println("Enter valid Role or Skip turn ");
            return;
        }
        if (validateTakeRole(player, choice)) {
            player.setCurrentRole(choice);
            choice.setAssignedPlayer(player);
            System.out.println("Here is you Role Info:");
            choice.displayRole();
            System.out.println(player.getPlayerName() + "has taken a Role!");

        }

    }

    public boolean validateTakeRole(Player player, Role role) {

        if (player.getCurrentRole() != null) {
            System.out.println("You are already working on a role!");
            return false;
        } else if (role.isOccupied()) {
            System.out.println("That role is already occupied!");
            return false;
        } else if (player.getRank() < role.getRequiredRank()) {
            System.out.println("Your rank is not high enough for that role!");
            return false;
        }
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

    public int rollDice() {
        return dice.roll();
    }

    public int[] rollBonusDice(int budget) {
        return dice.bonusDiceSorted(budget);
    }

    public void act(Player player) {

        if (board.isDayOver()) {
            System.out.println("The day is over. You cannot act now.");
            return;
        }

        if (!validateAct(player)) {
            System.out.println("You are not Currently working on a role!");
            return;
        }

        currentRoom = player.getCurrentRoom();

        Scene scene = currentRoom.getCurrentScene();
        if (scene == null) {
            System.out.println("Error: No active scene in the room.");
            return;
        }

        int budget = scene.getMovieBudget();
        int roll = rollDice();
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
                System.out.println("Act succeeded! Earned 1 credit and 1 dollar.");
            }
            System.out.println("Remaining Shots: " + currentRoom.getRemainingCounters());
            currentRoom.removeShotCounter();
            if (currentRoom.isSceneComplete()) {
                wrapScene(currentRoom);
                System.out.println("Scene is complete! ");
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

    public void Rehearse(Player player) {
        if (board.isDayOver()) {
            System.out.println("The day is over. You cannot rehearse now.");
            return;
        }
        if (!validateRehearse(player)) {
            return;
        }

        player.addRehearsalToken();
        System.out.println("Total Rehearsal Tokens: " + player.getRehearsalTokens());
    }

    public void displayUpgradeOptions(Player player) {
        board.moveToOffice(player);
        System.out.println("Current rank: " + player.getRank());
        castingOffice.displayCosts();
        System.out.println("Enter Rank and Payment type (d| dollars) or (c| credits) ");

    }

    public void upgradeRank(Player player, String newRank, String paymentType) {
        // update player rank

        if (!player.getCurrentRoom().getRoomName().equalsIgnoreCase("office")) {
            System.out.println("You must be in casting office to upgrade");
            return;
        }

        if (!newRank.matches("\\d+")) {

            System.out.println("Please Choose a valid rank to upgrade to");
            return;

        }
        int rankNum = Integer.parseInt(newRank);

        if (paymentType.equalsIgnoreCase("d") || paymentType.equalsIgnoreCase("dollar")
                || paymentType.equalsIgnoreCase("dollars")) {
            castingOffice.upgradePlayer(player, rankNum, CastingOffice.PaymentType.DOLLARS);
        } else if ((paymentType.equalsIgnoreCase("c") || paymentType.equalsIgnoreCase("credit")
                || paymentType.equalsIgnoreCase("credits"))) {
            castingOffice.upgradePlayer(player, rankNum, CastingOffice.PaymentType.CREDITS);

        } else {
            System.out.println("Enter Rank and Payment type (d| dollars) or (c| credits) ");
        }

    }

    public void wrapScene(Room room) {
        System.out.println("Scene in " + room.getRoomName() + " is a wrap");
        Scene scene = room.getCurrentScene();
        if (scene == null)
            return;
        payOut(room, scene);
        board.removeScene(room); // this clears the room and decrements activeScenes
    }

    public void payOut(Room room, Scene scene) {
        // pay players bonus based on bonus dice and player role
        int budget = scene.getMovieBudget();
        int[] bonusDice = rollBonusDice(budget);
        List<Role> starringRoles = scene.getStarRoles();
        boolean onCard = false;
        for (Role r : starringRoles) {
            if (r.getAssignedPlayer() != null) {
                onCard = true;
                break;
            }
        }
        int diceIndex = 0;

        if (!starringRoles.isEmpty() && onCard) {
            int numRoles = starringRoles.size();
            for (int i = 0; i < budget; i++) {
                Role role = starringRoles.get(i % numRoles);
                Player p = role.getAssignedPlayer();
                if (p != null) {
                    int bonus = bonusDice[i];
                    p.addDollars(bonus);
                    System.out.println(p.getPlayerName()
                            + " (star role) gets $" + bonus);
                }
            }
        }
        for (Role r : room.getExtraRole()) {
            Player p = r.getAssignedPlayer();
            if (p != null) {
                p.addDollars(r.getRequiredRank());
                System.out.println(p.getPlayerName() + " (Extra role) gets $" + r.getRequiredRank());

            }
        }

    }

}