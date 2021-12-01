import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Minesweeper extends JFrame {

    private final int frameSIZE = 300;
    private Board board;
    private JLabel timer;
    private int timeRem;

    public Minesweeper() throws HeadlessException, InterruptedException {
        super("MineSweeper");
        Cell.loadIcons();
        this.setSize(frameSIZE, frameSIZE);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        timeRem = 1001;
        board = new Board();
        timer = new JLabel(String.format("Remaining Time: %d", timeRem));
        createMenuBar();
        this.add(timer, BorderLayout.NORTH);
        this.add(board.getGrid());
        this.add(board.getRemMines(), BorderLayout.SOUTH);
        run();
    }

    public void startNewGame() {
        this.board.startNewGame();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JMenuItem newGame = new JMenuItem("New");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRem = 1000;
                startNewGame();
            }
        });
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoadGameMenu();
            }
        });
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSaveMenu();
            }
        });
        fileMenu.add(newGame);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(exit);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }

    private void showSaveMenu() {
        int oldStat = this.board.getGameStatus();
        this.board.pause();
        Object input = JOptionPane.showInputDialog(this, "Save As",
                "Save Game", JOptionPane.QUESTION_MESSAGE);
        while (input != null && input.toString().trim().isEmpty()) {
            input = JOptionPane.showInputDialog(this, "Game name can't be empty",
                    "Save Game", JOptionPane.QUESTION_MESSAGE);
        }
        if (input == null) {
            this.board.setGameStatus(oldStat);
            return;
        }
        if (input == null)
            this.board.setGameStatus(oldStat);
        else SaveServer.saveGame(this.board.getCells(), this.timeRem, input.toString().trim());
        this.board.setGameStatus(oldStat);
    }

    private void showLoadGameMenu() {
        int oldStat = this.board.getGameStatus();
        this.board.pause();
        ArrayList<String> tmp = SaveServer.getAllGames();
        Object[] savedGames = tmp.toArray();
        String message = tmp.isEmpty() ? "There are no saved games in the database." :
                "Select a game to load.";
        Object selectionObject = JOptionPane.showInputDialog(this, message,
                "Load Game", JOptionPane.PLAIN_MESSAGE, null, savedGames,
                savedGames[tmp.size() - 1]);
        if (selectionObject == null)
            this.board.setGameStatus(oldStat);
        this.timeRem = SaveServer.loadGame(this.board, selectionObject.toString().trim());
        timer.setText(String.format("Remaining Time: %d", timeRem));
    }

    private void run() throws InterruptedException {
        while (true) {
            if (board.getGameStatus() == Board.ONGOING) {
                timer.setText(String.format("Remaining Time: %d", timeRem));
                Thread.sleep(1000);
                timeRem--;
            }
            if (board.getGameStatus() == Board.LOSE) {
                board.getRemMines().setText("GAME OVER! You have stepped on a mine.");
            }
            if (board.getGameStatus() == Board.WIN) {
                board.getRemMines().setText(String.format("Congratulations! You won, your score is %d.", timeRem + 1));
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Minesweeper test = new Minesweeper();
    }
}
