package eu.gebes.tryjump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import eu.gebes.tryjump.blocks.BlockManager;
import eu.gebes.tryjump.entities.Player;
import eu.gebes.tryjump.utils.SoundManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class TryJump extends ApplicationAdapter {


    public static SoundManager soundManager;
    Environment environment;
    SpriteBatch spriteBatch;
    Texture crosshair;
    ModelBatch modelBatch, shadowBatch;
    Grid grid;
    Player player;
    DirectionalShadowLight shadowLight;
    BlockManager blockManager;

    @Override
    public void create() {
        soundManager = new SoundManager();
        soundManager.music();
        blockManager = new BlockManager();
        grid = new Grid(blockManager);
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.5f));
        environment.add(new DirectionalLight().set(0.2f, 0.2f, 0.2f, 1f, 0.8f, 0.5f));
        environment.add((shadowLight = new DirectionalShadowLight(1024, 1024, 60f, 60f, .1f, 50f))
                .set(1f, 1f, 1f, 40.0f, -35f, -35f));
        environment.shadowMap = shadowLight;

        shadowBatch = new ModelBatch(new DepthShaderProvider());

        spriteBatch = new SpriteBatch();
        modelBatch = new ModelBatch();
        player = new Player(grid);

        crosshair = new Texture(Gdx.files.internal("gui/crosshair.png"));
    }


    @Override
    public void render() {

        Gdx.gl.glEnable(GL30.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL30.GL_BACK);
        Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        //create shadow texture
        shadowLight.begin(Vector3.Zero, player.getCamera().direction);
        shadowBatch.begin(shadowLight.getCamera());
        grid.renderShadow(shadowBatch, environment, player);
        shadowBatch.end();
        shadowLight.end();

        player.update();

        modelBatch.begin(player.getCamera());
        grid.render(modelBatch, environment, player);
        modelBatch.end();


        spriteBatch.begin();
        spriteBatch.draw(crosshair, (float) ((Gdx.graphics.getWidth() - Variables.crosshairSize) * 0.5), (float) ((Gdx.graphics.getHeight() - Variables.crosshairSize) * 0.5), Variables.crosshairSize, Variables.crosshairSize);

        if(Variables.levelEditorModeEnabled){
            spriteBatch.draw(blockManager.getTextureFor(player.getSelectedBuildBlock()), (float) ((Gdx.graphics.getWidth() - Variables.crosshairSize) * 0.5),0, Variables.crosshairSize, Variables.crosshairSize);

        }

        spriteBatch.end();


    }



    @Override
    public void dispose() {
        spriteBatch.dispose();
        crosshair.dispose();
        grid.dispose();
        soundManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        player.getCamera().viewportWidth = width;
        player.getCamera().viewportHeight = height;
        super.resize(width, height);
    }
}
