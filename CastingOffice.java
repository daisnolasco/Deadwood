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
        
        return true;
    }

    // Upgrade player to rank
    public void upgradePlayer(Player player, int newRank, PaymentType payType) {
        if (canUpgrade(player, newRank, payType)) {
            int cost = 0;
            int arrayIndex = newRank - 2; 

            if (payType == PaymentType.DOLLARS) {
                cost = upgradeRankDollars[arrayIndex];
                player.setDollars(player.getDollars() - cost);
            } else if (payType == PaymentType.CREDITS) {
                cost = upgradeRankCredits[arrayIndex];
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
   
    // Add this method for Actions class integration
    public boolean validateCanUpgrade(Player player, int newRank) {
        // Check both payment types
        PaymentType[] paymentTypes = {PaymentType.DOLLARS, PaymentType.CREDITS};
        for (PaymentType payType : paymentTypes) {
            if (canUpgrade(player, newRank, payType)) {
                return true;
            }
        }
        return false;
    }
}