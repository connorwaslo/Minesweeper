package minesweeper;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Connor Waslo on 7/25/2016.
 */
public class Texture {

    private int id;
    private int width, height;

    public Texture(String path) {
        set(path);
    }

    public void set(String path) {
        try {
            InputStream in = new FileInputStream(path);

            try {
                PNGDecoder decoder = new PNGDecoder(in);

                width = decoder.getWidth();
                height = decoder.getHeight();

                ByteBuffer buf = ByteBuffer.allocateDirect(4 * width * height);
                decoder.decode(buf, 4 * width, PNGDecoder.Format.RGBA);
                buf.flip();

                id = glGenTextures();
                glBindTexture(GL_TEXTURE_2D, id);

                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
                        GL_RGBA, GL_UNSIGNED_BYTE, buf);

                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            } catch(IOException e) {
                e.printStackTrace();
            }

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
