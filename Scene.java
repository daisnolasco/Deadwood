import java.util.*;

public class Scene {
    private String sceneName;
    private String sceneDescription;
    private int movieBudget;
    private int sceneNumber;
    private List<Role> starRoles = new ArrayList<>();
    List<Role> availibleStarRoles = new ArrayList<>();

    public Scene(String sceneName, String sceneDesciption, int movieBudget,int sceneNumber, ArrayList<Role> StarRoles) {
        this.sceneName = sceneName;
        this.sceneDescription = sceneDesciption;
        this.movieBudget = movieBudget;
        this.sceneNumber = sceneNumber;
        if (StarRoles != null) {
            this.starRoles.addAll(StarRoles);
        }

    }

    // setters and getters
    public String getSceneName() {
        return sceneName;
    }

    public String getSceneDescription() {
        return sceneDescription;
    }

    public int getMovieBudget() {
        return movieBudget;
    }

      public int getSceneNum() {
        return sceneNumber;
    }
    public List<Role> getStarRoles() {
        return starRoles;
    }

    // adding player to scenecard
    public void addStarRole(Role star) {
        if (star != null) {
            starRoles.add(star);
        }
    }

    

    public void resetScene() {
        // removes player from role ;
        for (Role star : starRoles) {
            if (star.isOccupied()) {
                star.removePlayerFromRole();
            }
        }
    }

    public List<Role> getAvailibleStarRoles() {
        availibleStarRoles.clear();
        // for-loop for star roles in role and checks if role is occupied
        for (Role star : getStarRoles()) {
            if (!star.isOccupied() )
                availibleStarRoles.add(star);
            
        }
        return availibleStarRoles;
    }

    protected void displaySceneInfo() {
        System.out.println("Scene Name: " + getSceneName());
        System.out.println("Description: " + getSceneDescription());
        System.out.println("Movie Budget: " + getMovieBudget());
     

    }

}
