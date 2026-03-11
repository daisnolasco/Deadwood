public interface GameView {
    void refreshView();
    void log(String message);
    void offerTakeRole();
    void offerUpgrade();
    void showEndGame();
}