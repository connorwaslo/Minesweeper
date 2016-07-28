package minesweeper.states;

import minesweeper.Game;
import minesweeper.Texture;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Connor Waslo on 7/26/2016.
 */
public class StateManager {

    private Texture tex;
    private States state;

    private boolean active;

    public StateManager(States initState) {
        // Automatically initialize texture to loss b/c cynicism
        tex = new Texture("res/Loss.png");

        // Do not actively draw automatically
        active = false;

        setState(initState);
    }

    public void setState(States s) {
        state = s;

        switch (state) {
            case PLAY:
                active = false;
                break;
            case VICTORY:
                active = true;
                tex.set("res/Victory.png");
                break;
            case LOSS:
                active = true;
                tex.set("res/Loss.png");
                break;
        }
    }

    public void render() {
        tex.bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f((Game.WIDTH - tex.getWidth()) / 2, (Game.HEIGHT - tex.getHeight()) / 2);

        glTexCoord2f(1, 0);
        glVertex2f((Game.WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                (Game.HEIGHT - tex.getHeight()) / 2);

        glTexCoord2f(1, 1);
        glVertex2f((Game.WIDTH - tex.getWidth()) / 2 + tex.getWidth(),
                (Game.HEIGHT - tex.getHeight()) / 2 + tex.getHeight());

        glTexCoord2f(0, 1);
        glVertex2f((Game.WIDTH - tex.getWidth()) / 2,
                (Game.HEIGHT - tex.getHeight()) / 2 + tex.getHeight());
        glEnd();
    }

    public States currentState() {
        return state;
    }

    public boolean isActive() {
        return active;
    }
}
