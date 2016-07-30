package minesweeper;

import de.matthiasmann.twl.utils.PNGDecoder;
import minesweeper.states.StateManager;
import minesweeper.states.States;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Connor Waslo on 6/22/2016.
 */
public class Game implements Runnable {

    public static int WIDTH = 640;
    public static int HEIGHT = 640;

    private Thread thread;
    private boolean running = false;

    private long window;

    private Map map;
    private StateManager stateManager;

    private void start() {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    private void init() {
        // Check if GLFW initializes
        if (glfwInit() != GL_TRUE) {
            throw new RuntimeException("GLFW did not initialize correctly");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        window = glfwCreateWindow(WIDTH, HEIGHT, "Minesweeper", NULL, NULL);

        // If window creation fails
        if (window == NULL) {
            throw new RuntimeException("Window creation failed");
        }
        
        // Set mouse button callback
        glfwSetMouseButtonCallback(window, new Input());

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

        // Set context for OpenGL
        glfwMakeContextCurrent(window);

        glfwShowWindow(window);

        // Register context with LWJGL 3
        GL.createCapabilities();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        map = new Map(10);
        stateManager = new StateManager(States.PLAY);
    }

    public void run() {
        init();

        while (running) {
            update();
            render();

            if (glfwWindowShouldClose(window) == GL_TRUE) {
                running = false;
            }
        }

        glfwTerminate();
    }

    private void update() {
        // Take input
        if (stateManager.currentState() == States.PLAY) {

            // Double buffer input with previous state of button and current state
            // Get only single presses by comparing previous and current button state
            if (Input.currButtons[GLFW_MOUSE_BUTTON_LEFT] && !Input.prevButtons[GLFW_MOUSE_BUTTON_LEFT])
                map.selectTile(map.getMouseRow(window), map.getMouseCol(window));
            else if (Input.currButtons[GLFW_MOUSE_BUTTON_RIGHT] && !Input.prevButtons[GLFW_MOUSE_BUTTON_RIGHT])
                map.flagTile(map.getMouseRow(window), map.getMouseCol(window));

            Input.prevButtons[GLFW_MOUSE_BUTTON_LEFT] = Input.currButtons[GLFW_MOUSE_BUTTON_LEFT];
            Input.prevButtons[GLFW_MOUSE_BUTTON_RIGHT] = Input.currButtons[GLFW_MOUSE_BUTTON_RIGHT];
        }

        // If player loses
        if (map.hasLost()) {
            stateManager.setState(States.LOSS);
        } else if (map.hasWon()) {
            stateManager.setState(States.VICTORY);
        }

        glfwPollEvents();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Draw here
        map.render();

        // Check for loss/victory and draw accordingly
        if (stateManager.isActive()) {
            stateManager.render();
        }

        glfwSwapBuffers(window);
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
