/*
Deadwood GUI helper file
Author: Moushumi Sharmin
This file shows how to create a simple GUI using Java Swing and Awt Library
Classes Used: JFrame, JLabel, JButton, JLayeredPane
*/
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class BoardLayersListener extends JFrame implements GameView {

    // JLabels
    JLabel boardlabel;
    Deadwood controller ;
    JLabel cardlabel;
    JLabel playerlabel;
    JLabel mLabel;
    // JButtons
    JButton bAct;
    JButton bRehearse;
    JButton bMove;
    // JLayered Pane
    JLayeredPane bPane;
    // Text box under buttons
    JTextArea textBox;
Map<Player, JLabel> playerTokens = new HashMap<>();
List<JLabel> cardBackLabels = new ArrayList<>();

    int numPlayers;
    List<String> playerNames = new ArrayList<>();

    // player cards
    JPanel leftPanel;

    // scaled board size
    static final int origW = 1200;
    static final int origH = 900;
    static final int tokenSize = 46;
    private int boardW;
    private int boardH;
    private double scale = 1.0;

    // Colors
    final Color tanBackground = new Color(210, 190, 160);
    static final Color darkerTan = new Color(180, 155, 120);
    static final Color white = Color.WHITE;
    static final Color green = new Color(80, 160, 80);
    static final Color blue = new Color(80, 80, 180);
    static final Color brown = new Color(140, 100, 50);
    static final Color red = new Color(200, 40, 40);

    public BoardLayersListener(Deadwood controller) {
        super("Deadwood");
       this.controller = controller;
    this.controller.setView(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Scale board to fit screen so it never cuts off
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        scale = Math.min(1.0, Math.min(
                (double) (screen.width - 340) / origW,
                (double) (screen.height - 120) / origH));
        boardW = (int) (origW * scale);
        boardH = (int) (origH * scale);

        // Title
        JLabel title = new JLabel("Deadwood Game", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setOpaque(true);
        title.setBackground(white);
        title.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        add(title, BorderLayout.NORTH);
leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    leftPanel.setBackground(tanBackground);
    leftPanel.setPreferredSize(new Dimension(180, boardH));
    add(leftPanel, BorderLayout.WEST);

    boardPanel();
    rightButtonMenu();
    updatePlayerPanel();  // builds textBox — must come before southPanel uses it

        // exit button
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnEnd = new JButton("End Game X");
        btnEnd.setBackground(red);
        btnEnd.setFont(new Font("SansSerif", Font.BOLD, 13));

        btnEnd.setFocusPainted(false);
        btnEnd.addActionListener(e -> textBox.append(" End Game\n")); // fixed: textBox now exists
        southPanel.add(btnEnd);
        add(southPanel, BorderLayout.SOUTH);

        setSize(boardW + 340, boardH + 80);
        setLocationRelativeTo(null);
    }void placeCardBacks() {
    for (JLabel label : cardBackLabels) {
        bPane.remove(label);
    }
    cardBackLabels.clear();

    ImageIcon backIcon = new ImageIcon("/Images/Cardback.png");

    for (Room room : controller.getBoard().getAllSets()) {
        JLabel back = new JLabel(backIcon);

        int x = room.getX();
        int y = room.getY();

        back.setBounds(
                x,
                y,
                backIcon.getIconWidth(),
                backIcon.getIconHeight()
        );

        cardBackLabels.add(back);
        bPane.add(back, Integer.valueOf(1));
    }

    bPane.repaint();
}

    // left panel with player stats

   void updatePlayerPanel() {

    if (leftPanel == null || controller == null) {
        return;
    }

    leftPanel.removeAll();

    JLabel title = new JLabel("Players");
    title.setFont(new Font("SansSerif", Font.BOLD, 14));
    leftPanel.add(title);
    leftPanel.add(new JLabel(" "));

    for (Player p : controller.getPlayers()) {

        if (p == controller.getCurrentPlayer()) {
            leftPanel.add(new JLabel("Current Player"));
        }

        JTextArea info = new JTextArea(p.getDisplayInfo());
        info.setEditable(false);
        info.setBackground(tanBackground);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        info.setMaximumSize(new Dimension(160,120));

        leftPanel.add(info);
     
    }

    leftPanel.revalidate();
    leftPanel.repaint();
}
       
    void boardPanel() {
        bPane = new JLayeredPane();
        bPane.setPreferredSize(new Dimension(boardW, boardH));
        bPane.setBackground(tanBackground);
        bPane.setOpaque(true);

        // board image
        boardlabel = new JLabel();
        ImageIcon icon = new ImageIcon("Images/board.jpg");
        Image scaled = icon.getImage()
                .getScaledInstance(boardW, boardH, Image.SCALE_SMOOTH);
        boardlabel.setIcon(new ImageIcon(scaled));
        boardlabel.setBounds(0, 0, boardW, boardH);
        bPane.add(boardlabel, new Integer(0));

        // scene card layer
        cardlabel = new JLabel();
        ImageIcon cIcon = new ImageIcon("Images/CardBack.png");
        cardlabel.setIcon(cIcon);
        cardlabel.setBounds(20, 65, cIcon.getIconWidth() + 2, cIcon.getIconHeight());
        cardlabel.setOpaque(true);
        bPane.add(cardlabel, new Integer(1));

        // player token
        playerlabel = new JLabel();
        ImageIcon pIcon = new ImageIcon("Images/r2.png");
        playerlabel.setIcon(pIcon);
        playerlabel.setBounds(114, 227, 46, 46);
        playerlabel.setVisible(false);
        bPane.add(playerlabel, new Integer(3));

        add(bPane, BorderLayout.CENTER);
    } ImageIcon getPlayerDice(Player p) {

    char color = p.getColor();
    int rank = p.getRank();

    String path = "Images/Dice/" + color + rank + ".png";

    return new ImageIcon(path);
}

    void rightButtonMenu() {
               mLabel = new JLabel("MENU", SwingConstants.CENTER);
        mLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        mLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mLabel.setMaximumSize(new Dimension(140, 24));
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(tanBackground);
        rightPanel.setPreferredSize(new Dimension(160, 100));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
rightPanel.add(mLabel);
        rightPanel.add(Box.createVerticalStrut(8));
        // Action buttons
        bAct      = makeButton("Act",       green);
        bRehearse = makeButton("Rehearse",  green);
        bMove     = makeButton("Move",      green);
        JButton bSkip = makeButton("Skip Turn", brown);
        JButton bEnd  = makeButton("Quit",  red);

        bAct.addActionListener(e -> controller.actAction());
bRehearse.addActionListener(e -> controller.RehearseAction());
bMove.addActionListener(e -> textBox.append("Move clicked\n"));
bSkip.addActionListener(e -> controller.skipAction());
bEnd.addActionListener(e -> controller.endAction());

        rightPanel.add(bAct);     
         rightPanel.add(Box.createVerticalStrut(6));
        rightPanel.add(bRehearse);
         rightPanel.add(Box.createVerticalStrut(6));
        rightPanel.add(bMove);     
        rightPanel.add(Box.createVerticalStrut(6));
        rightPanel.add(bSkip);     
        rightPanel.add(Box.createVerticalStrut(6));
        rightPanel.add(bEnd);      
        rightPanel.add(Box.createVerticalStrut(12));

        // Game log text box
        textBox = new JTextArea(10, 14);
        textBox.setEditable(false);
        textBox.setLineWrap(true);
        textBox.setWrapStyleWord(true);
        textBox.setBackground(new Color(245, 235, 210));
        JScrollPane scroll = new JScrollPane(textBox);
        scroll.setBorder(BorderFactory.createTitledBorder("Game Log"));
        rightPanel.add(scroll);

        add(rightPanel, BorderLayout.EAST);
    }

    JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
   
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(140, 34));
        btn.setPreferredSize(new Dimension(140, 34));
        return btn;
    }void createPlayerTokens() {
    for (Player p : controller.getPlayers()) {
        if (!playerTokens.containsKey(p)) {
            JLabel token = new JLabel(getPlayerDice(p));
            token.setSize(tokenSize, tokenSize);
            playerTokens.put(p, token);
            bPane.add(token, Integer.valueOf(3));
        }
    }
}

void updatePlayerTokens() {
    int index = 0;

    for (Player p : controller.getPlayers()) {
        JLabel token = playerTokens.get(p);

        if (token == null) {
            continue;
        }

     
    }

    bPane.repaint();
}
void runSetupDialogs() {

    String[] options = { "2", "3", "4", "5", "6", "7", "8" };

    JList<String> playerList = new JList<>(options);
    playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    playerList.setSelectedIndex(0);

    int result = JOptionPane.showConfirmDialog(
            this,
            new JScrollPane(playerList),
            "How many players?",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);

    if (result != JOptionPane.OK_OPTION) {
        System.exit(0);
    }

    numPlayers = Integer.parseInt(playerList.getSelectedValue());
    textBox.append(" Players: " + numPlayers + "\n");

    playerNames.clear();

    for (int i = 1; i <= numPlayers; i++) {

        String name = null;

        while (name == null || name.trim().isEmpty()) {

            name = JOptionPane.showInputDialog(
                    this,
                    "Enter name for Player " + i + ":",
                    "Player " + i,
                    JOptionPane.QUESTION_MESSAGE);

            if (name == null) {
                System.exit(0);
            }

            if (name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Name cannot be empty.",
                        "Invalid Name",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        playerNames.add(name.trim());
        textBox.append(" Player " + i + ": " + name + "\n");
    }

  textBox.append("Setup complete! Starting game...\n");

controller.setNumPlayers(numPlayers);
controller.setupGame(playerNames);

placeCardBacks();
createPlayerTokens();
updatePlayerTokens();
updatePlayerPanel();
}
   
        // TODO: pass to controller when MVC is wired up
        // controller.setNumPlayers(numPlayers);
        // controller.setupGame(playerNames);
    

    // This class implements Mouse Events (from original helper)
    class boardMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == bAct) {
                playerlabel.setVisible(true);
                textBox.append("Act clicked\n");
            } else if (e.getSource() == bRehearse) {
                textBox.append("Rehearse clicked\n");
            } else if (e.getSource() == bMove) {
                textBox.append("Move clicked\n");
            }
        }

        public void mousePressed(MouseEvent e)  {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e)  {}
        public void mouseExited(MouseEvent e)   {}
    }

    public static void main(String[] args) {
Deadwood controller = new Deadwood();

    BoardLayersListener board = new BoardLayersListener(controller);

    board.setVisible(true);

    SwingUtilities.invokeLater(() -> board.runSetupDialogs());
    }
@Override
public void refreshView() {
    updatePlayerPanel();
    placeCardBacks();
    createPlayerTokens();
    updatePlayerTokens();
    repaint();
}

@Override
public void log(String message) {
    textBox.append(message + "\n");
}

@Override
public void offerTakeRole() {
    textBox.append("Take role option here\n");
}

@Override
public void offerUpgrade() {
    textBox.append("Upgrade option here\n");
}

@Override
public void showEndGame() {
    JOptionPane.showMessageDialog(this, "Game Over");
}
}