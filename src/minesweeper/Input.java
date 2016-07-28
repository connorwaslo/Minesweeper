package minesweeper;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Connor Waslo on 7/27/2016.
 */
public class Input {

    private boolean pressed;
    private int leftCount;
    private int rightCount;

    public Input() {
        pressed = false;
        leftCount = 0;
        rightCount = 0;
    }

    public void leftMousePressed(long window, Map map) {
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS && leftCount < 1) {
            leftCount++;

            map.selectTile(map.getMouseRow(window), map.getMouseCol(window));
        }

        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) == GLFW_RELEASE) {
            leftCount = 0;
        }
    }

    public void rightMousePressed(long window, Map map) {
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS && rightCount < 1) {
            rightCount++;

            map.flagTile(map.getMouseRow(window), map.getMouseCol(window));
        }

        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_RIGHT) == GLFW_RELEASE) {
            rightCount = 0;
        }
    }
}
