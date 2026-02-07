import java.util.*;

public class Scene {
    private String sceneName;
    private String sceneDescription;
    private int movieBudget;
    private List<Role> starRoles;

    public Scene(String sceneName, String sceneDesciption, int movieBudget) {
        this.sceneName = sceneName;
        this.sceneDescription = sceneDesciption;
        this.movieBudget = movieBudget;
      

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

    public List<Role> getAvailibleStarRoles() {
        List<Role> availibleStarRoles = new ArrayList<>();
        //for-loop for star roles in role and checks if role is occupied 
        return availibleStarRoles;
    }

}
