
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

//dialog template for dialog throughout game 
public abstract class gameDialogue<T> {
    // dialog colors
    static final Color BG = new Color(210, 190, 160);
    static final Color border = new Color(120, 85, 45);
    static final Color green = new Color(80, 140, 80);
    static final Color rust = new Color(160, 80, 50);
    static final Font titleT = new Font("Serif", Font.BOLD, 16);
    static final Font body = new Font("Serif", Font.PLAIN, 13);
    static final Font button = new Font("Serif", Font.BOLD, 12);
    protected JDialog dlg;
    // display styling
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
    // subclasses must use
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

    // center panel with buttons centered
    protected JPanel makeButtonBar(JButton... buttons) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        bar.setBackground(BG);
        for (JButton b : buttons)
            bar.add(b);
        return bar;
    }

    // creates a style message label using hmtl for longer text
    protected JLabel makePrompt(String text) {
        JLabel lbl = new JLabel("<html><center>" + text + "</center></html>",
                SwingConstants.CENTER);
        lbl.setFont(body);
        lbl.setForeground(border);
        lbl.setBorder(BorderFactory.createEmptyBorder(12, 20, 8, 20));
        return lbl;
    }
//confrimation dialoge
    // player confirm action
    public static class Confirm extends gameDialogue<Boolean> {
        private String message;
        private boolean result = false;
        public Confirm(String message) {
            this.message = message;
        }
        @Override
        protected void buildList() {//message prommpt
            dlg.add(makePrompt(message), BorderLayout.CENTER);
            JButton yes = makeButton("Yes", green);
            JButton no = makeButton("No", rust);
            yes.addActionListener(e -> {
                result = true;
                dlg.dispose();
            });
            no.addActionListener(e -> dlg.dispose());
            dlg.add(makeButtonBar(yes, no), BorderLayout.SOUTH);
        }//return confirmation

        @Override
        protected Boolean getResult() {
            return result;
        }
    }
//selection dialog
    // list selection
    public static class Select extends gameDialogue<String> {
        private String prompt;
        private String[] options;
        private String result = null;
//for  scrollable list options
        public Select(String prompt, String[] options) {
            this.prompt = prompt;
            this.options = options;
        }

        // title and scroll list
        @Override
        protected void buildList() {
            JLabel lbl = new JLabel(prompt, SwingConstants.CENTER);
            lbl.setFont(titleT);
            lbl.setForeground(border);
            lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, border),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            dlg.add(lbl, BorderLayout.NORTH);
            // styling and list of options
            JList<String> list = new JList<>(options);
            list.setFont(body);
            list.setBackground(new Color(235, 215, 185));
            list.setForeground(new Color(60, 35, 10));
            list.setSelectionBackground(border);
            list.setSelectionForeground(Color.WHITE);
            list.setSelectedIndex(0);
            // double click to select
            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        result = list.getSelectedValue();
                        dlg.dispose();
                    }
                }
            });
            // confirm or cancel buttons for prompts
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
    //input dialog
    //text input for getting names
    public static class Input extends gameDialogue<String> {
        private String prompt;
        private String result = null;

        public Input(String prompt) {
            this.prompt = prompt;

        }//text input dialog that retuns string
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
            Runnable confirm = () -> {
                result = field.getText().trim();
                dlg.dispose();
            };//confirm buttone
            ok.addActionListener(e -> confirm.run());
            field.addActionListener(e -> confirm.run());
            dlg.add(makeButtonBar(ok), BorderLayout.SOUTH);
        }
        protected String getResult() {
            return result;
        }

    }
//message dialog
    // display messages with no return val
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

    // dialogue for actions
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
            //grid for rank and payment selection
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
                //convert payemnt type for controller
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
    }
//game event dialogs
    // Shows act outcome
    public static class ActResult extends gameDialogue<Void> {
        private final boolean success;
        private final String message;

        public ActResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        @Override
        protected void buildList() {
            //styling green for success and red for fail
            Color headerColor = success ? green : rust;
            String headerText = success ? "Success!" : "Failed";
            JLabel header = new JLabel(headerText, SwingConstants.CENTER);
            header.setFont(titleT);
            header.setForeground(headerColor);
            header.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            dlg.add(header, BorderLayout.NORTH);
            // Extract reward only
            String reward = "";
            if (message.contains("credit") || message.contains("$")) {
                String[] lines = message.split("\n");
                for (String line : lines) {
                    if (line.contains("Earned") || line.contains("$")) {
                        reward = line;
                        break;
                    }
                }
            }
            if (!reward.isEmpty()) {
                JLabel rewardLbl = new JLabel(
                        "<html><center>" + reward + "</center></html>",
                        SwingConstants.CENTER);
                rewardLbl.setFont(body);
                rewardLbl.setForeground(border);
                rewardLbl.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));

                dlg.add(rewardLbl, BorderLayout.CENTER);
            }

            JButton ok = makeButton("OK", headerColor);
            ok.addActionListener(e -> dlg.dispose());

            dlg.add(makeButtonBar(ok), BorderLayout.SOUTH);
        }

        @Override
        protected Void getResult() {
            return null;
        }
    }

    // Prompts player to act when guarantee success
    public static class GuaranteedActDialog {
        public static void show(JFrame parent, Deadwood controller) {
            boolean doAct = new Confirm("You are guaranteed to succeed! Act now?")
                    .show(parent, "Guaranteed Success");
            if (doAct)
                controller.actAction();
            else
                controller.declineTakeRole();
        }
    }

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
            if (choice != null)
                controller.moveAction(choice);
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
            }//filter roles by rank 
            String[] labels = available.stream()
                    .map(r -> r.getRoleName() + " (rank " + r.getRequiredRank() + ")")
                    .toArray(String[]::new);
            String choice = new Select("Choose your role:", labels).show(parent, "Take Role");
            if (choice != null)
                controller.takeRoleAction(choice.split(" \\(rank")[0]);
        }
    }

    // Pure upgrade form display 
    public static class UpgradeDialog {
        public static void show(JFrame parent, Deadwood controller) {
            String[] result = new Upgrade().show(parent, "Upgrade Rank");
            if (result != null)
                controller.upgradeAction(result[0], result[1]);
        }
    }
//scene is complete 
    public static class SceneWrapDialog extends gameDialogue<Void> {
        private final String message;

        public SceneWrapDialog(String message) {
            this.message = message;
        }

        @Override
        protected void buildList() {
            JLabel title = new JLabel("Scene Wrapped!", SwingConstants.CENTER);
            title.setFont(titleT);
            title.setForeground(green);
            title.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            dlg.add(title, BorderLayout.NORTH);
            JLabel msg = new JLabel(
                    "<html><center>" + message.replace("\n", "<br>") + "</center></html>",
                    SwingConstants.CENTER);
            msg.setFont(body);
            msg.setForeground(border);
            msg.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
            dlg.add(msg, BorderLayout.CENTER);

            JButton ok = makeButton("OK", green);
            ok.addActionListener(e -> dlg.dispose());
            dlg.add(makeButtonBar(ok), BorderLayout.SOUTH);
        }

        @Override
        protected Void getResult() {
            return null;
        }
    }

    public static class Notice extends gameDialogue<Void> {
        private final String titleText;
        private final String message;

        public Notice(String titleText, String message) {
            this.titleText = titleText;
            this.message = message;
        }

        @Override
        protected void buildList() {
            JLabel title = new JLabel(titleText, SwingConstants.CENTER);
            title.setFont(titleT);
            title.setForeground(border);
            title.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, border),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            dlg.add(title, BorderLayout.NORTH);

            JLabel msg = new JLabel(
                    "<html><center>" + message.replace("\n", "<br>") + "</center></html>",
                    SwingConstants.CENTER);
            msg.setFont(body);
            msg.setForeground(border);
            msg.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
            dlg.add(msg, BorderLayout.CENTER);

            JButton ok = makeButton("OK", border);
            ok.addActionListener(e -> dlg.dispose());
            dlg.add(makeButtonBar(ok), BorderLayout.SOUTH);
        }

        @Override
        protected Void getResult() {
            return null;
        }
    }

    public static class EndGameDialog {
        public static boolean show(JFrame parent) {
            return new Confirm("End the game now and show final scores?")
                    .show(parent, "End Game");
        }
    }

}