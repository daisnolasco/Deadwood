
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

//Handles right view panel for action buttons and game log 
public class ActionPanelView extends JPanel {
    Deadwood controller;
    // contained in parent class
    BoardLayersListener parent;
    // action buttons
    JButton actButton;
    JButton rehearseButton;
    JButton moveButton;
    JButton takeRoleButton;
    JButton upgradeButton;
    JButton skipButton;
    JButton quitButton;
    private JLabel dayLabel;
    // Log box
    JTextArea logArea;
    // right view colors
    Color tan = new Color(210, 190, 160);
    Color darkerTan = new Color(180, 155, 120);
    Color green = new Color(80, 140, 80);
    Color blue = new Color(80, 80, 180);
    Color brown = new Color(140, 100, 50);
    Color red = new Color(200, 40, 40);
    Color rust = new Color(160, 80, 50);

    // still workin on layout
    public ActionPanelView(Deadwood controller, BoardLayersListener parent) {
        this.controller = controller;
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(tan);
        setPreferredSize(new Dimension(170, 100));
        setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
        // Day display
        dayLabel = new JLabel("Day 1 / 4", SwingConstants.CENTER);
        dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dayLabel.setForeground(new Color(80, 50, 20)); 
        // dark brown
        add(dayLabel);
        add(Box.createVerticalStrut(4));

        // Menu title
        JLabel title = new JLabel("MENU", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, darkerTan),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)));
        add(title);
        add(Box.createVerticalStrut(8));
        // Create buttons
        actButton = makePanelButton("Act", green);
        rehearseButton = makePanelButton("Rehearse", green);
        moveButton = makePanelButton("Move", green);
        takeRoleButton = makePanelButton("Take Role", green);
        upgradeButton = makePanelButton("Upgrade", green);
        skipButton = makePanelButton("Skip Turn", brown);
        quitButton = makePanelButton("Quit", red);
        // Button actions displayed based off player state:
        // buttons for when player is working on a role
        actButton.addActionListener(e -> controller.actAction());
        rehearseButton.addActionListener(e -> controller.rehearseAction());
        // buttons for non wokring players
        moveButton.addActionListener(e -> gameDialogue.MoveDialog.show(parent, controller));
        takeRoleButton.addActionListener(e -> gameDialogue.TakeRoleDialog.show(parent, controller));
        upgradeButton.addActionListener(e -> controller.startUpgrade());
        skipButton.addActionListener(e -> controller.skipAction());
        quitButton.addActionListener(e -> {
            // remove player from game if quit
            String name = controller.getCurrentPlayer().getPlayerName();
            boolean confirm = parent.gameConfirm("Quit",
                    "Remove " + name + " from the game?");
            if (confirm)
                controller.quitAction();
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
        // scroll down log
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
        if (controller.getPlayers().isEmpty())
            return;
        updateDayDisplay();
        Player player = controller.getCurrentPlayer();
        Room room = player.getCurrentRoom();
        boolean working = player.isWorking();
        boolean hasScene = room.isSet() && room.hasActiveScene();
        // Check if any role in this room is available at player's rank
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

    private void updateDayDisplay() {
        int day = controller.getBoard().getDayCount();
        int total = controller.getTotalDays();
        dayLabel.setText("Day " + day + " / " + total);
    }

    // button style for action buttons with shadow and press effect
    JButton makePanelButton(String text, Color color) {
        JButton btn = gameDialogue.makeButton(text, color);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(140, 34));
        btn.setPreferredSize(new Dimension(140, 34));
        Color shadow = color.darker().darker();
        // shadow: thick border on bottom/right
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 3, 3, shadow),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                btn.setBackground(color.darker());
                // pressed: shift shadow to top/left
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(3, 3, 1, 1, shadow),
                        BorderFactory.createEmptyBorder(7, 14, 3, 10)));
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 1, 3, 3, shadow),
                        BorderFactory.createEmptyBorder(5, 12, 5, 12)));
            }
        });
        return btn;
    }
}