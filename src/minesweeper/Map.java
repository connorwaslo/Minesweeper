package minesweeper;

import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Connor Waslo on 6/22/2016.
 */
public class Map {

    private Tile[][] tileMap;
    private int mineCount;
    private int flagCount;

    private boolean lost;
    private boolean clicked;

    public Map(int size) {
        tileMap = new Tile[size][size];
        mineCount = 10;
        flagCount = mineCount;
        lost = false;

        // Stores whether user has selected first tile
        clicked = false;

        init();
    }

    public void flagTile(int row, int col) {
        // If the tile is NOT selected
        if (!tileMap[row][col].isSelected() && !tileMap[row][col].isFlagged() && flagCount > 0) {
            System.out.println("flag");
            tileMap[row][col].setFlag(true);
            flagCount--;
            tileMap[row][col].setTexture();
        } else if (tileMap[row][col].isFlagged()) {
            System.out.println("Unflag");
            tileMap[row][col].setFlag(false);
            flagCount++;
            tileMap[row][col].setTexture();
        }
    }

    public void selectTile(int row, int col) {
        if (!tileMap[row][col].isFlagged()) {
            // If user's FIRST CLICK
            if (!clicked) {
                System.out.println("Clicked just now");
                clicked = true;

                    setMines(row, col);
                    if (tileMap[row][col].getAdjMines() == 0) {
                        System.out.println("First click on a 0");
                        selectAdjacentZeroes(row, col);
                    } else {
                        System.out.println("First click not on a 0");
                        tileMap[row][col].select();
                        tileMap[row][col].setTexture();
                    }


            } else {
                System.out.println("Clicked again");
                    if (tileMap[row][col].hasMine()) {
                        lost = true;
                        tileMap[row][col].select();
                        tileMap[row][col].setTexture();

                        return;
                    } else if (tileMap[row][col].getAdjMines() == 0 && !tileMap[row][col].hasMine()) {
                        selectAdjacentZeroes(row, col);
                    } else {
                        tileMap[row][col].select();
                        tileMap[row][col].setTexture();
                    }

            }
        }
    }

    public void render() {
        for (Tile[] ti : tileMap) {
            for (Tile t : ti) {
                t.render();
            }
        }
    }

    public boolean hasWon() {
        if (clicked) {
            for (Tile[] ti : tileMap) {
                for (Tile t : ti) {
                    if (t.hasMine() && !t.isFlagged())
                        return false;
                }
            }

            return true;
        }

        return false;
    }

    public boolean hasLost() {
        return lost;
    }

    public int getMouseRow(long window) {
        DoubleBuffer yPos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, null, yPos);

        if (yPos.get(0) / Tile.SIZE > tileMap.length - 1) {
            return tileMap.length - 1;
        } else if (yPos.get(0) / Tile.SIZE < 0) {
            return 0;
        }

        return (int) (yPos.get(0) / Tile.SIZE);
    }

    public int getMouseCol(long window) {
        DoubleBuffer xPos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xPos, null);

        if (xPos.get(0) / Tile.SIZE > tileMap[0].length - 1) {
            return tileMap[0].length - 1;
        } else if (xPos.get(0) / Tile.SIZE < 0) {
            return 0;
        }

        return (int) (xPos.get(0) / Tile.SIZE);
    }

    private void init() {
        // Temporarily set each tile to empty
        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[0].length; c++) {
                tileMap[r][c] = new Tile(r, c, false);
            }
        }
    }

    // Set mines, with an exception at parameters row and col
    private void setMines(int row, int col) {
        int r, c;
        for (int i = 0; i < mineCount; i++) {
            r = (int) (Math.random() * tileMap.length);
            c = (int) (Math.random() * tileMap.length);

            if (r == row && c == col) {
                i--;
            } else if (tileMap[r][c].hasMine()) {
                i--; // Repeats iteration
            } else {
                tileMap[r][c].setMine(); // Sets mine at r, c
            }
        }

        // Reset texture for each tile
        for (int x = 0; x < tileMap.length; x++) {
            for (int y = 0; y < tileMap.length; y++) {
                if (!tileMap[x][y].hasMine()) {
                    setAdjacentMines(x, y);
                }

                tileMap[x][y].setTexture();
            }
        }
    }

    private void selectAdjacentZeroes(int row, int col) {
        if (!inBounds(row) || !inBounds(col))
            return;

        if (tileMap[row][col].getAdjMines() == 0 && !tileMap[row][col].isSelected()) {
            tileMap[row][col].select();
            tileMap[row][col].setTexture();

            for (int r = row - 1; r <= row + 1; r++) {

                for (int c = col - 1; c <= col + 1; c++) {
                    if (inBounds(r) && inBounds(c)) {
                        // Recursively check other zeroes
                        if (tileMap[row][col].getAdjMines() == 0 && !tileMap[r][c].isFlagged())
                            selectAdjacentZeroes(r, c);

                        // Also select numbers beside zeroes
                        if (!tileMap[r][c].isFlagged() && !tileMap[r][c].hasMine()) {
                            tileMap[r][c].select();
                            tileMap[r][c].setTexture();
                        }
                    }
                }
            }
        }
    }

    private void setAdjacentMines(int row, int col) {
        int mines = 0;

        for (int r = row - 1; r <= row + 1; r++) {

            for (int c = col - 1; c <= col + 1; c++) {
                if (inBounds(r) && inBounds(c)) {
                    if (tileMap[r][c].hasMine()) {
                        mines++;
                    }
                }
            }
        }

        // Save adjacent mines to each tile
        tileMap[row][col].setAdjMines(mines);
    }

    private boolean inBounds(int n) {
        return n >= 0 && n < tileMap.length;
    }
}
