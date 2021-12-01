import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class Board {

    public static final int ONGOING = 0, WIN = 1, LOSE = 2, PAUSE = 3;
    private static final int[] dx = {0, 0, 1, 1, 1, -1, -1, -1}, dy = {1, -1, 0, 1, -1, 0, 1, -1};
    public static final int n = 16, m = 40;
    private Container grid;
    private Cell[][] cells;
    private int mines;
    private JLabel remMines;
    private int gameStatus;
    private MouseAdapter mouseListener;


    public Board() {
        grid = new Container();
        grid.setLayout(new GridLayout(n, n));
        cells = new Cell[n][n];
        remMines = new JLabel();
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameStatus != ONGOING) return;
                Cell source = (Cell) e.getSource();
                int buttonClicked = e.getButton();
                if (buttonClicked == MouseEvent.BUTTON1) uncoverCell(source);
                else
                    flag(source);
            }
        };
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cells[i][j] = new Cell(i, j);
                cells[i][j].addMouseListener(mouseListener);
                this.grid.add(cells[i][j]);
            }
        }
        startNewGame();
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && y >= 0 && x < n && y < n;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public void buildGrid(int[][] initialValue, int[][] initialStatus) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = initialValue[i][j], status = initialStatus[i][j];
                cells[i][j].set(value, status);
                mines += cells[i][j].isCoveredMine() ? 1 : 0;
                if (cells[i][j].isExposedMine()) this.gameStatus = LOSE;
            }
        }
        if (mines == 0 && gameStatus != LOSE) gameStatus = WIN;
    }

    private void uncoverCell(Cell cell) {
        if (!cell.isCovered()) return;
        cell.uncover();
        if (cell.isMine()) {
            // LOSE
            gameStatus = LOSE;
        }
        if (cell.isEmpty()) {
            int x = cell.x, y = cell.y;
            for (int k = 0; k < 8; k++) {
                int xx = x + dx[k], yy = y + dy[k];
                if (isValidCell(xx, yy) && cells[xx][yy].isEmpty()) {
                    uncoverCell(cells[xx][yy]);
                }
            }
        }
    }

    private void flag(Cell cell) {
        if (!cell.isCovered()) return;
        mines -= cell.isCoveredMine() ? 1 : 0;
        remMines.setText(String.format("%d", mines));
        cell.flag();
        if (mines == 0) {
            // WIN
            gameStatus = WIN;
        }
    }


    public JLabel getRemMines() {
        return remMines;
    }

    public Container getGrid() {
        return grid;
    }


    private int getRandom() {
        return (int) (Math.random() * n);
    }

    public void startNewGame(int[][] value, int[][] status) {
        mines = 0;
        this.gameStatus = PAUSE;
        buildGrid(value, status);
        if (this.gameStatus == PAUSE)
            this.gameStatus = ONGOING;
        remMines.setText(String.format("%d", mines));
    }

    public void startNewGame() {
        int[][] value = new int[n][n], status = new int[n][n];
        for (int[] row : status)
            Arrays.fill(row, Cell.COVERED);

        for (int i = 0; i < m; i++) {
            int x = getRandom(), y = getRandom();
            while (value[x][y] == Cell.MINE) {
                x = getRandom();
                y = getRandom();
            }
            value[x][y] = Cell.MINE;
            for (int k = 0; k < 8; k++) {
                int xx = x + dx[k], yy = y + dy[k];
                if (isValidCell(xx, yy) && value[xx][yy] != Cell.MINE)
                    value[xx][yy]++;
            }
        }

        startNewGame(value, status);

    }

    public void pause() {
        this.gameStatus = PAUSE;
    }

    public Cell[][] getCells() {
        return cells;
    }


    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }
}
