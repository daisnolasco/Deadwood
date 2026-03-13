
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public abstract class gameDialogue<T> {

//dialog colors
    static final Color BG = new Color(210, 190, 160);
    static final Color border = new Color(120, 85, 45);
    static final Color green = new Color(80, 140, 80);
    static final Color rust = new Color(160, 80, 50);
    static final Font titleT = new Font("Serif", Font.BOLD, 16);
    static final Font body = new Font("Serif", Font.PLAIN, 13);
    static final Font button = new Font("Serif", Font.BOLD, 12);

    protected JDialog dlg;
//display styling 
    public T show(JFrame parent, String title) {
        dlg = new JDialog(parent, title, true);
        dlg.setLayout(new BorderLayout(8, 8));
        dlg.getContentPane().setBackground(BG);
        dlg.getRootPane().setBorder(BorderFactory.createLineBorder(border, 3));
        buildList();
        dlg.pack();
        dlg.setMinimumSize(new Dimension(280, 140));
        dlg.setLocationRelativeTo(parent);
        dlg.setVisible(true);
        return getResult();

    }
    protected abstract void buildList();
    protected abstract T getResult();
    // helpers
    // create buttons
    protected static JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(button);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
         btn.setBorder(
         
            BorderFactory.createEmptyBorder(5, 12, 5, 12));
        return btn;
    }

    protected JPanel makeButtonBar(JButton... buttons) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        bar.setBackground(BG);
        for (JButton b : buttons)
            bar.add(b);
        return bar;
    }

    protected JLabel makePrompt(String text) {
        JLabel lbl = new JLabel("<html><center>" + text + "</center></html>",
                SwingConstants.CENTER);
        lbl.setFont(body);
        lbl.setForeground(border);
        lbl.setBorder(BorderFactory.createEmptyBorder(12, 20, 8, 20));
        return lbl;
    }

    // player confirm action
    public static class Confirm extends gameDialogue<Boolean> {
        private String message;
        private boolean result = false;

        public Confirm(String message) {
            this.message = message;
        }

        @Override
        protected void buildList() {
            dlg.add(makePrompt(message), BorderLayout.CENTER);
            JButton yes = makeButton("Yes", green);
            JButton no = makeButton("No", rust);
            yes.addActionListener(e -> {
                result = true;
                dlg.dispose();
            });
            no.addActionListener(e -> dlg.dispose());
            dlg.add(makeButtonBar(yes, no), BorderLayout.SOUTH);
        }

        @Override
        protected Boolean getResult() {
            return result;
        }
    }

    // for scroll list (player count, rooms,roles)
    public static class Select extends gameDialogue<String> {
        private String prompt;
        private String[] options;
        private String result = null;

        public Select(String prompt, String[] options) {
            this.prompt = prompt;
            this.options = options;
        }

        @Override
        protected void buildList() {
            JLabel lbl = new JLabel(prompt, SwingConstants.CENTER);
            lbl.setFont(titleT);
            lbl.setForeground(border);
            lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, border),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            dlg.add(lbl, BorderLayout.NORTH);
            JList<String> list = new JList<>(options);
            list.setFont(body);
            list.setBackground(new Color(235, 215, 185));
            list.setForeground(new Color(60, 35, 10));
            list.setSelectionBackground(border);
            list.setSelectionForeground(Color.WHITE);
            list.setSelectedIndex(0);
            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        result = list.getSelectedValue();
                        dlg.dispose();
                    }
                }
            });

            JPanel mid = new JPanel(new BorderLayout());
            mid.setBackground(BG);
            mid.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
            mid.add(new JScrollPane(list));
            dlg.add(mid, BorderLayout.CENTER);

            JButton ok = makeButton("Confirm", green);
            JButton cancel = makeButton("Cancel", rust);
            ok.addActionListener(e -> {
                result = list.getSelectedValue();
                dlg.dispose();
            });
            cancel.addActionListener(e -> dlg.dispose());
            dlg.add(makeButtonBar(ok, cancel), BorderLayout.SOUTH);
        }

        @Override
        protected String getResult() {
            return result;
        }
    }
//player clicks option in selection
    public static class Input extends gameDialogue<String> {
private String prompt;
    private String result = null;
    public Input(String prompt){
        this.prompt=prompt;

    }
     protected void buildList() {
        dlg.add(makePrompt(prompt), BorderLayout.NORTH);
        JTextField field = new JTextField(16);
        field.setFont(body);
        field.setBackground(new Color(235, 215, 185));
        field.setForeground(new Color(60, 35, 10));
        JPanel mid = new JPanel(new BorderLayout());
        mid.setBackground(BG);
        mid.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        mid.add(field);
        dlg.add(mid, BorderLayout.CENTER);
        JButton ok = makeButton("OK", green);
        // Runnable so both button and Enter key do the same thing
        Runnable confirm = () -> { result = field.getText().trim(); dlg.dispose(); }; // ← was missing result =
        ok.addActionListener(e    -> confirm.run());
        field.addActionListener(e -> confirm.run()); // ← was commented out
        dlg.add(makeButtonBar(ok), BorderLayout.SOUTH);
    }
    protected String getResult() { return result; }


    }
    // display messages
    public static class Message extends gameDialogue<Void> {
        private String message;

        public Message(String message) {
            this.message = message;
        }

        @Override
        protected void buildList() {
            dlg.add(makePrompt(message), BorderLayout.CENTER);
            JButton ok = makeButton("OK", border);
            ok.addActionListener(e -> dlg.dispose());
            dlg.add(makeButtonBar(ok), BorderLayout.SOUTH);
        }

        @Override
        protected Void getResult() {
            return null;
        }
    }
//dialogue for actions 
    public static class Upgrade extends gameDialogue<String[]> {
        private String[] result = null;

        @Override
        protected void buildList() {
            JLabel title = new JLabel("Visit the Casting Office", SwingConstants.CENTER);
            title.setFont(titleT);
            title.setForeground(border);
            title.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, border),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            dlg.add(title, BorderLayout.NORTH);
            JPanel mid = new JPanel(new GridLayout(2, 2, 8, 8));
            mid.setBackground(BG);
            mid.setBorder(BorderFactory.createEmptyBorder(12, 16, 8, 16));
            JLabel rankLbl = new JLabel("Upgrade to rank:");
            rankLbl.setFont(body);
            rankLbl.setForeground(border);
            JComboBox<String> rankBox = new JComboBox<>(new String[] { "2", "3", "4", "5", "6" });
            JLabel payLbl = new JLabel("Pay with:");
            payLbl.setFont(body);
            payLbl.setForeground(border);
            JComboBox<String> payBox = new JComboBox<>(new String[] { "Dollars", "Credits" });
            mid.add(rankLbl);
            mid.add(rankBox);
            mid.add(payLbl);
            mid.add(payBox);
            dlg.add(mid, BorderLayout.CENTER);
            JButton ok = makeButton("Upgrade", green);
            JButton cancel = makeButton("Cancel", rust);
            ok.addActionListener(e -> {
                String pay = payBox.getSelectedItem().equals("Dollars") ? "d" : "c";
                result = new String[] { (String) rankBox.getSelectedItem(), pay };
                dlg.dispose();
            });
            cancel.addActionListener(e -> dlg.dispose());
            dlg.add(makeButtonBar(ok, cancel), BorderLayout.SOUTH);
        }
        @Override
        protected String[] getResult() {
            return result;
        }
    }// Shows adjacent rooms and moves player
    // Add to gameDialogue.java — after Upgrade class

// Shows adjacent rooms and moves player
public static class MoveDialog {
    public static void show(JFrame parent, Deadwood controller) {
        List<Room> neighbors = controller.getCurrentPlayer().getCurrentRoom().getAdjacentRooms();
        if (neighbors.isEmpty()) {
            new Message("No adjacent rooms to move to.").show(parent, "Move");
            return;
        }
        String[] names = neighbors.stream()
            .map(Room::getRoomName).toArray(String[]::new);
        String choice = new Select("Choose a room:", names).show(parent, "Move");
        if (choice != null) controller.moveAction(choice);
    }
}

// Shows available roles and assigns one to player
public static class TakeRoleDialog {
    public static void show(JFrame parent, Deadwood controller) {
        Room room = controller.getCurrentPlayer().getCurrentRoom();
        List<Role> available = new ArrayList<>();
        for (Role r : room.getAvailibleRoles())
            if (r.getRequiredRank() <= controller.getCurrentPlayer().getRank())
                available.add(r);
        if (available.isEmpty()) {
            new Message("No roles available at your rank.").show(parent, "Take Role");
            return;
        }
        String[] labels = available.stream()
            .map(r -> r.getRoleName() + " (rank " + r.getRequiredRank() + ")")
            .toArray(String[]::new);
        String choice = new Select("Choose your role:", labels).show(parent, "Take Role");
        if (choice != null) controller.takeRoleAction(choice.split(" \\(rank")[0]);
    }
}

// Checks office, moves if needed, then upgrades
public static class UpgradeDialog {
    public static void show(JFrame parent, Deadwood controller) {
        if (!controller.getCurrentPlayer().getCurrentRoom().getRoomName().equalsIgnoreCase("office")) {
            new Message("You must be in the Casting Office to upgrade.").show(parent, "Cannot Upgrade");
            return;
        }
        String[] result = new Upgrade().show(parent, "Upgrade Rank");
        if (result != null) controller.upgradeAction(result[0], result[1]);
    }
}

}