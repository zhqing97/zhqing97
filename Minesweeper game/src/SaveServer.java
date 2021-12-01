import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Scanner;

public class SaveServer {

    public static ArrayList<String> getAllGames() {
        File dir = new File("database/");
        ArrayList<String> savedGames = new ArrayList<>();
        if (dir.exists()) {
            for (File file : dir.listFiles())
                savedGames.add(file.getName());
        }
        return savedGames;
    }

    public static void saveGame(Cell cells[][], int remTime, String filename) {
    	url = "jdbc:sqlite:javaclass.db";
    	try {
    		Connection connect = DriverManager.getConnection(url);
            PrintWriter out = new PrintWriter(String.format("database/%s", filename));
            out.write(remTime + "\n");
            for (Cell[] row : cells) {
                for (Cell cur : row) {
                    out.write(cur + " ");
                }
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int loadGame(Board board, String filename) {
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(String.format("database/%s", filename)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int remTime = sc.nextInt();
        int n = Board.n;
        int[][] val = new int[n][n], stat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                val[i][j] = sc.nextInt();
                stat[i][j] = sc.nextInt();
            }
        }
        board.startNewGame(val, stat);
        return remTime;
    }


}
