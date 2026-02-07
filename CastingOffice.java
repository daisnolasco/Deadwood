public class CastingOffice {
    // Cost arrays for rank 2-6
    private int[] upgradeRankDollars;
    private int[] upgradeRankCredits;

    // Constructor - initialize cost arrays
    public CastingOffice() {
        upgradeRankDollars = new int[]{4, 10, 18, 28, 40}; // Costs for ranks 2-6
        upgradeRankCredits = new int[]{5, 10, 15, 20, 25}; // Costs for ranks 2-6
    }
    
    public enum PaymentType {
        DOLLARS,
        CREDITS
    }

    // Check for players that can upgrade rank
    public boolean canUpgrade(Player player, int newRank, PaymentType payType) {
        // Validate new rank
        if (newRank < 2 || newRank > 6) {
            System.out.println("Invalid rank. Must be between 2 and 6.");
            return false;
        }

        // Check if new rank > current rank
        if (newRank <= player.getRank()) {
            System.out.println("New rank must be higher than current rank.");
            return false;
        }

        // Cost of desired rank
        int cost = 0;
        int arrayIndex = newRank - 2; 

        if (payType == PaymentType.DOLLARS) {
            cost = upgradeRankDollars[arrayIndex];
            if (player.getDollars() < cost) {
                System.out.println("Not enough dollars to upgrade.");
                return false;
            }
        } else if (payType == PaymentType.CREDITS) {
            cost = upgradeRankCredits[arrayIndex];
            if (player.getCredits() < cost) {
                System.out.println("Not enough credits to upgrade.");
                return false;
            }
        } else {
            System.out.println("Invalid payment type.");
            return false;
        }
        
        return true;  // Fixed: Return true if all checks pass
    }

    // Upgrade player to rank
    public void upgradePlayer(Player player, int newRank, PaymentType payType) {
        if (canUpgrade(player, newRank, payType)) {
            int cost = 0;
            int arrayIndex = newRank - 2; 

            if (payType == PaymentType.DOLLARS) {
                cost = upgradeRankDollars[arrayIndex];
                // Use setDollars to deduct the cost (since Player class has no deductDollars method)
                player.setDollars(player.getDollars() - cost);
            } else if (payType == PaymentType.CREDITS) {
                cost = upgradeRankCredits[arrayIndex];
                // Use setCredits to deduct the cost (since Player class has no deductCredits method)
                player.setCredits(player.getCredits() - cost);
            }
            player.setRank(newRank);
            System.out.println("Player upgraded to rank " + newRank + " using " + payType);
        }
    }

    // Display upgrade cost
    public void displayCosts() {
        System.out.println("Upgrade Costs:");
        for (int i = 0; i < upgradeRankDollars.length; i++) {
            int rank = i + 2;
            System.out.println("Rank " + rank + ": " + upgradeRankDollars[i] + " dollars or " + upgradeRankCredits[i] + " credits");
        }
    }

    // Get dollar cost for rank
    public int getDollarCost(int rank) {
        if (rank < 2 || rank > 6) {
            throw new IllegalArgumentException("Rank must be between 2 and 6.");
        }
        return upgradeRankDollars[rank - 2];
    }
    
    // Get credit cost for rank
    public int getCreditCost(int rank) {
        if (rank < 2 || rank > 6) {
            throw new IllegalArgumentException("Rank must be between 2 and 6.");
        }
        return upgradeRankCredits[rank - 2];
    }

    // Add this method inside the CastingOffice class for testing
    public static void main(String[] args) {
        // Create CastingOffice
        CastingOffice office = new CastingOffice();
        
        // Display costs
        office.displayCosts();
        
        // Create test player - Note: Need to create a Room object for the constructor
        // For testing, we can create a dummy Room or pass null if allowed by your Room class
        Room testRoom = new Room("Trailers"); // Assuming Room class has constructor that takes name
        
        Player player = new Player("Test Actor", 1, 50, 100, testRoom);
        
        System.out.println("\nPlayer: " + player.getPlayerName());
        System.out.println("Current rank: " + player.getRank());
        System.out.println("Dollars: " + player.getDollars());
        System.out.println("Credits: " + player.getCredits());
        
        // Test canUpgrade
        System.out.println("\n--- Testing canUpgrade ---");
        System.out.println("Can upgrade to rank 2 with dollars? " + 
            office.canUpgrade(player, 2, PaymentType.DOLLARS));
        System.out.println("Can upgrade to rank 6 with credits? " + 
            office.canUpgrade(player, 6, PaymentType.CREDITS));
        
        // Test upgradePlayer
        System.out.println("\n--- Testing upgradePlayer ---");
        office.upgradePlayer(player, 2, PaymentType.DOLLARS);
        System.out.println("New rank: " + player.getRank());
        System.out.println("Remaining dollars: " + player.getDollars());
        
        office.upgradePlayer(player, 4, PaymentType.CREDITS);
        System.out.println("New rank: " + player.getRank());
        System.out.println("Remaining credits: " + player.getCredits());
        
        // Test error cases
        System.out.println("\n--- Testing error cases ---");
        Player poorPlayer = new Player("Poor Actor", 1, 1, 2, testRoom);
        office.upgradePlayer(poorPlayer, 2, PaymentType.DOLLARS);
        
        // Try downgrading
        office.upgradePlayer(player, 3, PaymentType.DOLLARS);
        
        // Test getter methods
        System.out.println("\n--- Testing cost getters ---");
        System.out.println("Cost for rank 3 (dollars): " + office.getDollarCost(3));
        System.out.println("Cost for rank 5 (credits): " + office.getCreditCost(5));
    }
}