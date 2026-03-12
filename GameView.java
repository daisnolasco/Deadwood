public interface GameView {
    //interface contoller uses to update gui
    void refreshView();
    void log(String message);
    void offerTakeRole();
    void offerUpgrade();
    void showEndGame();
     void showActResult(String message);
}