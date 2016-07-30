package minesweeper;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Connor Waslo on 7/27/2016.
 */
public class Input extends GLFWMouseButtonCallback {

    public static boolean[] currButtons = new boolean[2];
    public static boolean[] prevButtons = new boolean[2];

    @Override
    public void invoke(long window, int button, int action, int mods) {
        currButtons[button] = action == GLFW_PRESS;
    }
}
