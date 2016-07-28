package minesweeper;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Connor Waslo on 6/24/2016.
 */
public class Tile {

    public static final float SIZE = 64.0f;

    private boolean hasMine;
    private boolean flagged;
    private boolean selected;

    private int row, col;

    private int adjMines;

    private Texture tex;

    // Paths for various tiles
    private static final String mine = "res/SelectedTileMine.png";
    private static final String flag = "res/Flag.png";
    private static final String unselected = "res/UnselectedTile.png";
    private static final String empty = "res/SelectedTile.png";
    private static final String one = "res/SelectedTileOne.png";
    private static final String two = "res/SelectedTileTwo.png";
    private static final String three = "res/SelectedTileThree.png";
    private static final String four = "res/SelectedTileFour.png";
    private static final String five = "res/SelectedTileFive.png";
    private static final String six = "res/SelectedTileSix.png";
    private static final String seven = "res/SelectedTileSeven.png";
    private static final String eight = "res/SelectedTileEight.png";

    public Tile(int row, int col, boolean hasMine) {
        this.row = row;
        this.col = col;
        this.hasMine = hasMine;

        flagged = false;
        selected = false;

        adjMines = 0;

        tex = new Texture(unselected);
    }

    public void render() {
        tex.bind();

        glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2f(col * SIZE, row * SIZE);

            glTexCoord2f(1, 0);
            glVertex2f((col * SIZE) + SIZE, row * SIZE);

            glTexCoord2f(1, 1);
            glVertex2f((col * SIZE) + SIZE, (row * SIZE) + SIZE);

            glTexCoord2f(0, 1);
            glVertex2f(col * SIZE, (row * SIZE) + SIZE);
        glEnd();
    }

    public void setAdjMines(int mines) {
        adjMines = mines;
    }

    public void setTexture() {
        if (hasMine && selected) {
            tex.set(mine);
        } else if (!hasMine && selected) {
            switch (adjMines) {
                case 0:
                    tex.set(empty);
                    break;
                case 1:
                    tex.set(one);
                    break;
                case 2:
                    tex.set(two);
                    break;
                case 3:
                    tex.set(three);
                    break;
                case 4:
                    tex.set(four);
                    break;
                case 5:
                    tex.set(five);
                    break;
                case 6:
                    tex.set(six);
                    break;
                case 7:
                    tex.set(seven);
                    break;
                case 8:
                    tex.set(eight);
                    break;
            }
        } else if (hasMine && selected) {
            tex.set(mine);
        } else if (flagged) {
            tex.set(flag);
        } else {
            tex.set(unselected);
        }
    }

    public void setFlag(boolean flag) {
        flagged = flag;
    }

    public void setMine() {
        hasMine = true;
    }

    public void select() {
        selected = true;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public int getAdjMines() {
        return adjMines;
    }
}
