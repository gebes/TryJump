package eu.gebes.tryjump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import eu.gebes.tryjump.blocks.BlockManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class TryJump extends ApplicationAdapter {

    Environment environment;
    PerspectiveCamera camera;
    SpriteBatch spriteBatch;
    Texture crosshair;
    ModelBatch modelBatch;
    CameraController cameraController;
    Grid grid;

    @Override
    public void create() {
        grid = new Grid();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.5f));
        environment.add(new DirectionalLight().set(0.2f, 0.2f, 0.2f, 1f, 0.8f, 0.5f));

        spriteBatch = new SpriteBatch();
        modelBatch = new ModelBatch();

        camera = new PerspectiveCamera(Variables.FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Variables.gridWidth / 2f * Variables.blockSize, 50 * Variables.blockSize, Variables.gridDepth / 2f * Variables.blockSize);


        camera.near = Variables.cameraNear;
        camera.far = Variables.cameraFar;
        cameraController = new CameraController(camera, grid);
        cameraController.setDegreesPerPixel(Variables.cameraDegreesPerPixel);
        cameraController.setVelocity(Variables.cameraVelocity);

        Gdx.input.setInputProcessor(cameraController);
        Gdx.input.setCursorCatched(true);

        crosshair = new Texture(Gdx.files.internal("gui/crosshair.png"));


    }


    @Override
    public void render() {
        cameraController.update();
        Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        grid.renderGrid(modelBatch, environment, cameraController);
        modelBatch.end();

        spriteBatch.begin();
        spriteBatch.draw(crosshair, (float) ((Gdx.graphics.getWidth() - Variables.crosshairSize) * 0.5), (float) ((Gdx.graphics.getHeight() - Variables.crosshairSize) * 0.5), Variables.crosshairSize, Variables.crosshairSize);
        spriteBatch.end();


    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        crosshair.dispose();
        grid.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        super.resize(width, height);
    }
}
