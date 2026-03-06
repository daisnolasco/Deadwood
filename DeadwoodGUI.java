import javax.swing.JOptionPane;

public class DeadwoodGUI extends JFrame {
    private Board board;
    private Actions actions;
    private ArrayList <Player> gamePlayers;
    private int currentPlayerIndex;
    //GUI components
    private JLayeredPane layeredPane;
    private JLabel boardLabel;
    private JPanel statPanel;
    private JLabel turnLabel;
    private java.util.List<JLabel> playerTokens; //1 per player
    private java.util.List<JLabel> costLabels; //1 per set
    private java.util.List<JLabel> shotLabels; //1 per shot
    
    public DeadwoodGUI() {
        super("Deadwood");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        layeredPane = getLayeredPane();
        //1. Load board img and set size
        Image Icon boardIcon = loadImage("board.jpg");
        boardLabel = new JLabel(boardIcon);
        boardLabel.setBounds(0,0,boardIcon.geteIconWidth(),boardIcon.getIconHeight());
        layeredPane.add(boardLabel,Integer.valueOf(0));
        setSize(boardIcon.getIconWidth()+300,boardIcon.getIconHeight() + 50);

        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBounds(boardIcon.getIconWidth() + 10,10,280,200);
        layeredPane.add(statsPanel,Integer.valueOf(2));
        //3. Initialize model
        board = new Board();
        actions = new Actions(board);

        String numStr = JOptionPane.showInputDialog(this, "Enter number of players (2-8):");
        int numPlayers = Integer.parseInt(numStr);
        setupPlayers(numPlayers);
        //Create player token, card label, shot counters
        createPlayerTokens();
        createCardLabels();
        createShotCounters();

        createRoomClickAreas();
        createRoleClickAreas();
        createUpgradeClickAreas();

        updateStatsPanel();
    }

    
}
