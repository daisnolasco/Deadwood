import java.awt.*;
import javax.swing.*;

// left player stats panel
public class PlayerPanelView extends JPanel {
    private Deadwood controller;
    BoardLayersListener parent;
    // panel colors
    Color tan = new Color(210, 190, 160);
    Color brown = new Color(120, 85, 45);
    Color darkText = new Color(60, 35, 10);
    Color green = new Color(80, 160, 80);

    public PlayerPanelView(Deadwood controller, BoardLayersListener parent) {
        this.controller = controller;
        this.parent = parent;
        // set player cards vertically
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(tan);
        setPreferredSize(new Dimension(160, 100));
        // padding inside panel
        setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
    }

    // updating player stats after eveery player turn
    public void refreshPlayers() {
        // redraw
        removeAll();
        JLabel header = new JLabel("Players:");
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        add(header);
        add(Box.createVerticalStrut(6));
        // highlighting active player to green
        for (Player p : controller.getPlayers()) {
            boolean isActive = (p == controller.getCurrentPlayer());
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(tan);
            card.setMaximumSize(new Dimension(160, 200));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            int thickness = isActive ? 3 : 1;
            Color borderColor = isActive ? green : brown;
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, thickness),
                    BorderFactory.createEmptyBorder(4, 6, 4, 6)));
            // player icon and name
            JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
            nameRow.setBackground(tan);
            nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            // Dice image
            JLabel diceLabel = new JLabel(parent.getDice(p));
            nameRow.add(diceLabel);
            // Name with active marker
            String nameText = (isActive ? "Active: " : "  ") + p.getPlayerName();
            JLabel nameLabel = new JLabel(nameText);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            nameLabel.setForeground(darkText);
            nameRow.add(nameLabel);
            card.add(nameRow);
            // Stats text below
            JTextArea info = new JTextArea(p.getDisplayInfo());
            info.setEditable(false);
            info.setBackground(tan);
            info.setFont(new Font("SansSerif", Font.PLAIN, 11));
            info.setLineWrap(true);
            info.setWrapStyleWord(true);
            info.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
            info.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(info);
            add(card);
            add(Box.createVerticalStrut(4));
        }

        revalidate();
        repaint();
    }
}