import java.util.*;

public class Scene {
    private String sceneName;
    private String sceneDescription;
    private int movieBudget;
    private List<Role> starRoles;
    List<Role> availibleStarRoles;

    public Scene(String sceneName, String sceneDesciption, int movieBudget, ArrayList<Role> availibleStarRoles) {
        this.sceneName = sceneName;
        this.sceneDescription = sceneDesciption;
        this.movieBudget = movieBudget;
        this.availibleStarRoles = availibleStarRoles;

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

    public List<Role> getStarRoles() {
        return starRoles;
    }

    // adding player to scenecard
    public void addStarRole(Role star) {
        starRoles.add(star);

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

        // for-loop for star roles in role and checks if role is occupied
        for (Role star : getStarRoles()) {
            if (!star.isOccupied())
                availibleStarRoles.add(star);
        }
        return availibleStarRoles;
    }

    protected void displaySceneInfo() {
        System.out.println("Scene Name: " + sceneName);
        System.out.println("Description: " + sceneDescription);
        System.out.println("Movie Budget: " + movieBudget);
        System.out.println("Star Roles:");
        for (Role star : availibleStarRoles) {
            star.displayRole();
        }

    }

}
