
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
public class ActionPanelView extends JPanel {
    Deadwood controller;
    BoardLayersListener parent;
    JButton actButton;
    JButton rehearseButton;
    JButton moveButton;
    JButton takeRoleButton;
    JButton upgradeButton;
    JButton skipButton;
    JButton quitButton;
    // Log box
    JTextArea logArea;   
    Color tan = new Color(210, 190, 160);
    Color darkerTan = new Color(180, 155, 120);
    Color green =  new Color(80, 140, 80);
    Color blue = new Color(80, 80, 180);
    Color brown = new Color(140, 100, 50);
    Color red = new Color(200, 40, 40);
    Color rust = new Color(160, 80, 50);
    public ActionPanelView(Deadwood controller, BoardLayersListener parent) {
        this.controller = controller;
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(tan);
        setPreferredSize(new Dimension(170, 100));
        setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
        JLabel title = new JLabel("MENU", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, darkerTan),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)));
        add(title);
        add(Box.createVerticalStrut(8));
        // Create buttons
        actButton = makeButton("Act", green);
        rehearseButton = makeButton("Rehearse", green);
        moveButton = makeButton("Move", green);
        takeRoleButton = makeButton("Take Role", green);
        upgradeButton = makeButton("Upgrade", green);
        skipButton = makeButton("Skip Turn", brown);
        quitButton = makeButton("Quit", red);
        // Button actions
        actButton.addActionListener(e -> controller.actAction());
        rehearseButton.addActionListener(e -> controller.rehearseAction());
        moveButton.addActionListener(e -> showMoveDialog());
        takeRoleButton.addActionListener(e -> showTakeRoleDialog());
        upgradeButton.addActionListener(e -> handleUpgrade());
        skipButton.addActionListener(e -> controller.skipAction());
        quitButton.addActionListener(e -> {
            String name = controller.getCurrentPlayer().getPlayerName();
            boolean confirm = parent.gameConfirm("Quit",
                "Remove " + name + " from the game?");
            if (confirm) controller.quitAction();
        });
        // Add buttons to panel
        add(actButton);
        add(Box.createVerticalStrut(6));
        add(rehearseButton);
        add(Box.createVerticalStrut(6));
        add(moveButton);
        add(Box.createVerticalStrut(6));
        add(takeRoleButton);
        add(Box.createVerticalStrut(6));
        add(upgradeButton);
        add(Box.createVerticalStrut(6));
        add(skipButton);
        add(Box.createVerticalStrut(6));
        add(quitButton);
        add(Box.createVerticalStrut(8));

        // Create game log
        logArea = new JTextArea(10, 14);
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBackground(new Color(245, 235, 210));
/* System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
            StringBuilder line = new StringBuilder();
            public void write(int b) {
                char c = (char) b;
                line.append(c);
                if (c == '\n') {
                    String text = line.toString();
                    line.setLength(0);
                    SwingUtilities.invokeLater(() -> {
                        logArea.append(text);
                        logArea.setCaretPosition(logArea.getDocument().getLength());
                    });
                }
            }
        })); */
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Game Log"));
        add(scroll);
    }

    // Add message to game log
    public void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // Show/hide buttons based on current player
    public void refreshButtons() {
        if (controller.getPlayers().isEmpty()) {
            return;
        }
        Player player = controller.getCurrentPlayer();
        Room room = player.getCurrentRoom();
        boolean working = player.isWorking();
        boolean hasScene = room.isSet() && room.hasActiveScene();
       boolean canTakeRole = false;
        if (hasScene) {
            for (Role r : room.getAvailibleRoles()) {
                if (r.getRequiredRank() <= player.getRank()) {
                    canTakeRole = true;
                    break;
                }
            }
        }
        actButton.setVisible(working);
        rehearseButton.setVisible(working);
        moveButton.setVisible(!working);
        takeRoleButton.setVisible(!working && hasScene && canTakeRole);
        upgradeButton.setVisible(!working);
        skipButton.setVisible(true);
        quitButton.setVisible(true);
    }

    // Show move dialog
    void showMoveDialog() {
        Player player = controller.getCurrentPlayer();
        List<Room> neighbors = player.getCurrentRoom().getAdjacentRooms();
        if (neighbors.isEmpty()) {
            parent.log("No adjacent rooms.");
            return;
        }
        String[] roomNames = new String[neighbors.size()];
        for (int i = 0; i < neighbors.size(); i++) {
            roomNames[i] = neighbors.get(i).getRoomName();
        }
        String choice = parent.gameSelect("Move", "Choose a room:", roomNames);
        if (choice == null) {
            return;
        }
        controller.moveAction(choice);
    }

    // Show take role dialog
   void showTakeRoleDialog() {
   Player player = controller.getCurrentPlayer();
        Room   room   = player.getCurrentRoom();

        // Collect only roles the player's rank qualifies for
        List<Role> available = new ArrayList<>();
        for (Role role : room.getAvailibleRoles()) {
            if (role.getRequiredRank() <= player.getRank()) {
                available.add(role);
            }
        }

        if (available.isEmpty()) {
            parent.gameMessage("Take Role", "No roles available at your rank.");
            return;
        }

        // Label each option as "Role Name (rank X)"
        String[] labels = new String[available.size()];
        for (int i = 0; i < available.size(); i++) {
            labels[i] = available.get(i).getRoleName()
                      + " (rank " + available.get(i).getRequiredRank() + ")";
        }
    String choice = parent.gameSelect("Take Role", "Choose your role:", labels);
    if (choice != null) controller.takeRoleAction(choice.split(" \\(rank")[0]);
}
    // Handle upgrade button
    void handleUpgrade() {
        Player player = controller.getCurrentPlayer();
        Room room = player.getCurrentRoom();
        // Player must move to office first
       if (!player.getCurrentRoom().getRoomName().equalsIgnoreCase("office")) {
            boolean wantToMove = parent.gameConfirm("Upgrade Rank",
                "You must be at the Casting Office to upgrade. Move there now?");
            if (!wantToMove) return; 

            
            controller.moveAction("office");
            parent.refreshView();
        }
        String[] result = parent.gameUpgrade();
        if (result == null) {
            return;
        }
        controller.upgradeAction(result[0], result[1]);
    }

    // Make a styled button
     JButton makeButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
           button.setFont(new Font("Serif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(140, 34));
        button.setPreferredSize(new Dimension(140, 34));
     button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)));
        return button;
    }
}