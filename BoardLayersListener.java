
/*
Deadwood GUI helper file
Author: Moushumi Sharmin
This file shows how to create a simple GUI using Java Swing and Awt Library
Classes Used: JFrame, JLabel, JButton, JLayeredPane
*/
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class BoardLayersListener extends JFrame implements GameView {
    // JLabels
    JLabel boardlabel;
    JLabel cardlabel;
    JLabel playerlabel;
    JLabel mLabel;
    // shot markers
    private Map<Room, List<JLabel>> shotMarkers = new HashMap<>();
    // face-down cards
    List<JLabel> cardBackLabels = new ArrayList<>();
    Map<Room, JLabel> cardFrontLabels = new HashMap<>();
    Map<Player, JLabel> playerTokens = new HashMap<>();
    // dice tokens
    int numPlayers;
    List<String> playerNames = new ArrayList<>();
    // JButtons
    JButton bAct;
    JButton bRehearse;
    JButton bMove;
    // JLayered Pane
    JLayeredPane bPane;
    // Controller for actions
    Deadwood controller;
    // left panel for player stats
    PlayerPanelView playerPanel;
    // buttons /availible action panels
    ActionPanelView actionPanel;
    // Constructor
    // to scale board and components
    double scale;

    int tokenSize;
    // Colors

    Color tan = new Color(210, 190, 160);
    Color darkerTan = new Color(180, 155, 120);
    Color red = new Color(200, 40, 40);

    public BoardLayersListener(Deadwood controller) {
        // Set the title of the JFrame
        super("Deadwood");
        // attempt to scale board
        this.controller = controller;

        // // Shrink for the left/right panels, board = 1200 x 900
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        double scaleW = (screen.width - 340) / 1200.00;
        double scaleH = (screen.height - 80) / 900.00;
        scale = Math.min(scaleW, scaleH);
        if (scale > 1.0)
            scale = 1.0;
        // Board after scaling
        int boardW = (int) (1200 * scale);
        int boardH = (int) (900 * scale);
        // scale player token
        tokenSize = (int) (40 * scale);
        if (tokenSize < 20)
            tokenSize = 20;
        // Set the exit option for the JFrame
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        // title stying
        JLabel title = new JLabel("Deadwood Game", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setOpaque(true);
        title.setBackground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        add(title, BorderLayout.NORTH);
        // Create the JLayeredPane to hold the display, cards, dice and buttons
        bPane = new JLayeredPane();
        bPane.setPreferredSize(new Dimension(boardW, boardH));
        bPane.setBackground(tan);
        bPane.setOpaque(true);
        // Create the deadwood board
        boardlabel = new JLabel();
        ImageIcon icon = new ImageIcon("Images/board.jpg");
        Image scaledBoard = icon.getImage().getScaledInstance(boardW, boardH, Image.SCALE_SMOOTH);
        boardlabel.setIcon(new ImageIcon(scaledBoard));
        boardlabel.setBounds(0, 0, boardW, boardH);
        // Add the board to the lowest layer
        bPane.add(boardlabel, new Integer(0));
        add(bPane, BorderLayout.CENTER);
        // player stats
        playerPanel = new PlayerPanelView(controller, this);
        add(playerPanel, BorderLayout.WEST);
        // Create the Menu for action buttons on right panel
        actionPanel = new ActionPanelView(controller, this);
        add(actionPanel, BorderLayout.EAST);
        // red exit button under stats pannel
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnEnd = new JButton("End Game  X");
        btnEnd.setBackground(red);
        btnEnd.setForeground(Color.WHITE);
        btnEnd.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnEnd.setFocusPainted(false);
        btnEnd.setOpaque(true);
        btnEnd.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(120, 30, 30), 2),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)));
        btnEnd.addActionListener(e -> {
            boolean confirm = gameDialogue.EndGameDialog.show(this);
            if (confirm)
                controller.endAction();
        });
        southPanel.add(btnEnd);
        add(southPanel, BorderLayout.SOUTH);
        setSize(boardW + 340, boardH + 80);
        setLocationRelativeTo(null);
    }

    // This class implements Mouse Events
    class boardMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            ((JButton) e.getSource()).setBackground(
                    ((JButton) e.getSource()).getBackground().brighter());
        }

        public void mousePressed(MouseEvent e) {
            ((JButton) e.getSource()).setBackground(
                    ((JButton) e.getSource()).getBackground().darker());
        }

        public void mouseReleased(MouseEvent e) {
            ((JButton) e.getSource()).setBackground(
                    ((JButton) e.getSource()).getBackground().brighter());
        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {
            ((JButton) e.getSource()).setBackground(
                    ((JButton) e.getSource()).getBackground().darker());
        }
    }

    // scen cards and shot counters displauyed
    void placeCardBacks() {
        // remove old shot markers
        for (List<JLabel> lbls : shotMarkers.values()) {
            for (JLabel lbl : lbls) {
                bPane.remove(lbl);
            }
        }
        shotMarkers.clear();
        // remove old cards
        for (JLabel lbl : cardBackLabels) {
            bPane.remove(lbl);
        }
        for (JLabel lbl : cardFrontLabels.values()) {
            bPane.remove(lbl);
        }
        cardBackLabels.clear();
        cardFrontLabels.clear();

        ImageIcon backRaw = new ImageIcon("Images/Cardback.png");
        // place cards
        for (Room room : controller.getBoard().getAllSets()) {
            if (!room.hasActiveScene())
                continue;
            int x = (int) (room.getX() * scale);
            int y = (int) (room.getY() * scale);
            int w = (int) (room.getW() * scale);
            int h = (int) (room.getH() * scale);
            //flip card if player enters room
            boolean playerHere = !room.getPlayersInRoom().isEmpty();
            if (playerHere && room.getCurrentScene() != null
                    && room.getCurrentScene().getImgPath() != null) {
                ImageIcon frontIcon = new ImageIcon(
                        new ImageIcon("Images/Card/" + room.getCurrentScene().getImgPath())
                                .getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
                JLabel front = new JLabel(frontIcon);
                front.setBounds(x, y, w, h);
                cardFrontLabels.put(room, front);
                bPane.add(front, Integer.valueOf(2));
            } else {//keep face down
                ImageIcon backIcon = new ImageIcon(
                        backRaw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
                JLabel back = new JLabel(backIcon);
                back.setBounds(x, y, w, h);
                cardBackLabels.add(back);
                bPane.add(back, Integer.valueOf(1));
            }
        }
        // place shot counters for each room
        for (Room room : controller.getBoard().getAllSets()) {
            if (!room.hasActiveScene())
                continue;
            List<JLabel> markers = new ArrayList<>();
            //stores x,y , w and h of 1 shot counyter
            for (int[] pos : room.getShotPositions()) {
                int x = (int) (pos[0] * scale);
                int y = (int) (pos[1] * scale);
                int w = (int) (pos[2] * scale);
                int h = (int) (pos[3] * scale);
                ImageIcon rawShot = new ImageIcon("Images/shot.png");
                Image scaledShot = rawShot.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
                JLabel shotLabel = new JLabel(new ImageIcon(scaledShot));
                shotLabel.setBounds(x, y, w, h);
                markers.add(shotLabel);
                bPane.add(shotLabel, Integer.valueOf(4));
            }
            shotMarkers.put(room, markers);
        }//refresh display
        bPane.revalidate();
        bPane.repaint();
    }// set players to a dice
//create player token label for each pllayer after set up
    void createPlayerTokens() {
        List<Player> players = controller.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (playerTokens.containsKey(p))
                continue;
            JLabel token = new JLabel(getDice(p));
            token.setSize(tokenSize, tokenSize);
            playerTokens.put(p, token);
            //keeping tokens on top layer
            bPane.add(token, new Integer(6 + i));
        }
    }
    void updatePlayerTokens() {
        // Track how many players are placed in each room so far,
        Map<Room, Integer> roomSlot = new HashMap<>();
        List<Player> players = controller.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            JLabel token = playerTokens.get(p);
            if (token == null)
                continue;
            // update dice icon in case rank changed
            token.setIcon(getDice(p));
            Room room = p.getCurrentRoom();
            Role role = p.getCurrentRole();
            if (room != null && role != null) {
                // player is on a role, position at role coordinates
                int[] pos = getRolePosition(room, role);
                token.setBounds(pos[0], pos[1], tokenSize, tokenSize);
            } else if (room != null) {
                // player in room but not on role ,place in grid inside room
                int slot = roomSlot.getOrDefault(room, 0);
                roomSlot.put(room, slot + 1);
                // grid parameters
                int cols = 4;
                int margin = 2;
                int col = slot % cols;
                int row = slot / cols;
                //  position relative to room area
                int roomX = (int) (room.getX() * scale);
                int roomY = (int) (room.getY() * scale);
                int roomW = (int) (room.getW() * scale);
                int roomH = (int) (room.getH() * scale);
                int x = roomX + margin + col * (tokenSize + margin);
                int y = roomY + margin + row * (tokenSize + margin);
                token.setBounds(x, y, tokenSize, tokenSize);
            }
        }
        bPane.revalidate();
        bPane.repaint();
    }
//for testing but should be in room class
    // return board psotion for player role
    int[] getRolePosition(Room room, Role role) {
        if (role.isStarringRole()) {
            int x = (int) ((room.getX() + role.getRoleX()) * scale);
            int y = (int) ((room.getY() + role.getRoleY()) * scale);
            return new int[] { x, y };
        } else {
            //cordinates for  extra roles 
            int x = (int) (role.getRoleX() * scale);
            int y = (int) (role.getRoleY() * scale);
            return new int[] { x, y };
        }
    }
    // get and set dice for player
    ImageIcon getDice(Player p) {
        String path = "Images/Dice/" + p.getColor() + p.getRank() + ".png";
        ImageIcon raw = new ImageIcon(path);
        return new ImageIcon(raw.getImage()
                .getScaledInstance(tokenSize, tokenSize, Image.SCALE_SMOOTH));

    }

    // set up proccess for getting number of players
    void runSetupDialogs() {
        String[] options = { "2", "3", "4", "5", "6", "7", "8" };
        String choice = new gameDialogue.Select("How many players?", options)
                .show(this, "Deadwood Setup");
        if (choice == null)
            System.exit(0);
        numPlayers = Integer.parseInt(choice);
        playerNames.clear();
        // get names
        for (int i = 1; i <= numPlayers; i++) {
            String name = null;
            while (name == null || name.trim().isEmpty()) {
                name = new gameDialogue.Input("Enter name for Player " + i + ":")
                        .show(this, "Player " + i);
                if (name == null)
                    System.exit(0);
                if (name.trim().isEmpty())
                    new gameDialogue.Message("Name cannot be empty.")
                            .show(this, "Invalid Name");
            }
            playerNames.add(name.trim());
        }
        controller.setNumPlayers(numPlayers);
        controller.setupGame(playerNames);
        createPlayerTokens();
        refreshView();

    }

    // game view interfce - observer
    @Override
    public void log(String message) {
        actionPanel.log(message);
    }

    // Refresh all panels to show the current game state
    @Override
    public void refreshView() {
        placeCardBacks(); // update scene card visuals
        updatePlayerTokens(); // move dice tokens to current positions
        playerPanel.refreshPlayers(); // update player stats on the left
        actionPanel.refreshButtons(); // show/hide buttons for current player
        // update markers
        for (Room room : controller.getBoard().getAllSets()) {
            List<JLabel> markers = shotMarkers.get(room);
            if (markers == null)
                continue;
            int shotsLeft = room.getRemainingCounters();
            for (int i = 0; i < markers.size(); i++) {
                markers.get(i).setVisible(i < shotsLeft);
            }
        }
    }
//act result popup 
    @Override
    public void showActResult(boolean success, String message) {
        new gameDialogue.ActResult(success, message).show(this, "Act Result");
    }

    // text should be moved to game dialogue class
    @Override
    public void offerTakeRole() {
        boolean want = new gameDialogue.Confirm(
                "You moved to a set with available roles. Take a role now?")
                .show(this, "Take Role?");
        if (want)
            gameDialogue.TakeRoleDialog.show(this, controller);
    }
//guarteed act
    @Override
    public void offerGuaranteedAct() {
        gameDialogue.GuaranteedActDialog.show(this, controller);
    }
//move to office if they arent curently there
    @Override
    public void offerMoveToOffice() {
        boolean move = new gameDialogue.Confirm(
                "You must be at the Casting Office. Move there now?")
                .show(this, "Casting Office");
        if (move) {
            controller.moveToOfficeAction();
            showUpgradeForm();
        }
    }
//upgrade options
    @Override
    public void showUpgradeForm() {
        String[] result = new gameDialogue.Upgrade().show(this, "Upgrade Rank");
        if (result != null)
            controller.upgradeAction(result[0], result[1]);
    }

    @Override
    public void offerUpgrade() {
        log("Upgrade Availible -> Use the Upgrade button.");
    }
//using html to display final scores and winner
    @Override
    public void showEndGame() {
        List<Player> players = controller.getPlayers();
        Player winner = controller.getBoard().getWinner(players);
        String msg = "<html><center><b>Final Scores</b><br><br>";
        msg += "<br><b>Winner: <font color='green'>" + winner.getPlayerName() + "</font></b>";
        msg += "</center></html>";
        for (Player p : players) {

            if (p == winner) {
                msg += "<font color='green'><b>";
                msg += p.getPlayerName() + ": " + p.playerScore() + " points";
                msg += "</b></font><br>";
            } else {
                msg += p.getPlayerName() + ": " + p.playerScore() + " points<br>";
            }

        }
        new gameDialogue.Message(msg).show(this, "Game Over");
    }

    // deadwood  color theme for swing dialog and components 
    void applyTheme() {
        Color parchment = new Color(210, 190, 160);
        Color darkBrown = new Color(80, 50, 20);
        Color btnColor = new Color(160, 120, 70);
        UIManager.put("OptionPane.background", parchment);
        UIManager.put("OptionPane.messageForeground", darkBrown);
        UIManager.put("Panel.background", parchment);
        UIManager.put("Button.background", btnColor);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("ComboBox.background", parchment);
        UIManager.put("ComboBox.foreground", darkBrown);
        UIManager.put("List.background", parchment);
        UIManager.put("List.foreground", darkBrown);
        UIManager.put("TextField.background", new Color(240, 225, 195));
        UIManager.put("TextField.foreground", darkBrown);
        UIManager.put("Label.foreground", darkBrown);
        UIManager.put("ScrollPane.background", parchment);
        UIManager.put("Viewport.background", parchment);
    }
}
