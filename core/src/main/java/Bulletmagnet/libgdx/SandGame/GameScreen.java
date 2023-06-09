package Bulletmagnet.libgdx.SandGame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class GameScreen implements Screen, InputProcessor {

    private SpriteBatch batch;
    private Texture SandTexture = new Texture("sand.png");
    private Pixmap pixmap;
    private int sandWidth;
    private int sandHeight;
    private int[][] sandArray;
    private OrthographicCamera camera;
    int sandSize = 25;
    private ArrayList<SandParticle> sandParticles;

    private Texture DarkSandTexture = new Texture("dark.png");
    private Texture RedSandTexture = new Texture("red.png");

    @Override
    public void show() {
        // Initialize sprite batch and sand textures
        batch = new SpriteBatch();

        sandParticles = new ArrayList<>();

        sandWidth = Gdx.graphics.getWidth();
        sandHeight = Gdx.graphics.getHeight();

        pixmap = new Pixmap(sandWidth, sandHeight, Pixmap.Format.RGBA8888);
        sandArray = new int[sandWidth][sandHeight];

        // Set background color
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, sandWidth, sandHeight);
        Gdx.input.setInputProcessor(this);
    }



    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update sand
        updateSand();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Draw sand
        batch.begin();
        for (SandParticle particle : sandParticles) {
            particle.render(batch, sandSize);
        }

        // Add new sand particle with left or right click
        Vector3 worldPos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        int sandX = (int) (worldPos.x / sandSize);
        int sandY = (int) (worldPos.y / sandSize);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            // Draw the first sand type with left click
            addSandParticle(sandX, sandY, 1);
        } else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            // Draw the second sand type with right click
            addSandParticle(sandX, sandY, 2);
        } else if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            addSandParticle(sandX, sandY, 3);
        }
        batch.end();
    }

    private void addSandParticle(int sandX, int sandY, int sandType) {
        if (sandX >= 0 && sandX < sandArray.length && sandY >= 0 && sandY < sandArray[0].length) {
            sandArray[sandX][sandY] = sandType;
        }
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Dispose of resources
        batch.dispose();
        DarkSandTexture.dispose();
        pixmap.dispose();
    }

    private void updateSand() {
        // Loop over each sand particle from bottom to top
        for (int y = 1; y < sandHeight; y++) {
            for (int x = 0; x < sandWidth; x++) {
                int sandType = sandArray[x][y];
                if (sandType > 0) {
                    // Check if the space below the sand particle is empty
                    if (sandArray[x][y - 1] == 0) {
                        // Move the sand particle down
                        sandArray[x][y] = 0;
                        sandArray[x][y - 1] = sandType;
                    } else {
                        // Check if the space to the left or right of the sand particle is empty
                        if (x > 0 && sandArray[x - 1][y - 1] == 0 && MathUtils.randomBoolean(0.5f)) {
                            // Move the sand particle to the left
                            sandArray[x][y] = 0;
                            sandArray[x - 1][y - 1] = sandType;
                        } else if (x < sandWidth - 1 && sandArray[x + 1][y - 1] == 0 && MathUtils.randomBoolean(0.5f)) {
                            // Move the sand particle to the right
                            sandArray[x][y] = 0;
                            sandArray[x + 1][y - 1] = sandType;
                        }
                    }
                }
            }
        }

        // Update sandParticles with new positions
        sandParticles.clear();
        for (int x = 0; x < sandWidth; x++) {
            for (int y = 0; y < sandHeight; y++) {
                int sandType = sandArray[x][y];
                if (sandType > 0) {
                    Texture texture = getTextureForSandType(sandType);
                    sandParticles.add(new SandParticle(x * sandSize, y * sandSize, texture));
                }
            }
        }
    }

    private Texture getTextureForSandType(int sandType) {
        // Replace with your own logic to map sandType to the correct texture
        // For example:
        switch (sandType) {
            case 1:
                return SandTexture;
            case 2:
                return DarkSandTexture;
            // Add more cases for additional sand types
            case 3:
                return RedSandTexture;
            default:
                return SandTexture;
        }
    }





    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
