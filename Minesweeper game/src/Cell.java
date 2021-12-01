import javax.swing.*;

public class Cell extends JButton {
    public static final int EMPTY = 0, MINE = 9, COVERED = 10, FLAG = 11, WFLAG = 12, UNCOVERED = -1;
    private static ImageIcon[] icons;
    private int status;
    private int value;
    public int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.setEnabled(true);
    }

    public void set(int value, int status) {
        this.value = value;
        this.status = status;
        updateIcon();
    }

    private void updateIcon() {
        if (this.status == UNCOVERED) {
            this.setIcon(icons[this.value]);
        } else {
            this.setIcon(icons[this.status]);
        }
    }

    public static void loadIcons() {
        if (icons != null) return;
        icons = new ImageIcon[13];
        for (int i = 0; i < icons.length; i++) {
            String path = String.format("resources/%d.png", i);
            icons[i] = new ImageIcon(path);
        }
    }

    public boolean isCoveredMine() {
        return this.isCovered() && isMine();
    }

    public void uncover() {
        this.status = UNCOVERED;
        updateIcon();
    }

    public void flag() {
        this.status = isMine() ? FLAG : WFLAG;
        updateIcon();
    }

    public boolean isMine() {
        return this.value == MINE;
    }

    public boolean isCovered() {
        return this.status == COVERED;
    }

    public boolean isEmpty() {
        return this.value == EMPTY;
    }

    public boolean isExposedMine() {
        return isMine() && this.status == UNCOVERED;
    }

    @Override
    public String toString() {
        return this.value + " " + this.status;
    }


}
