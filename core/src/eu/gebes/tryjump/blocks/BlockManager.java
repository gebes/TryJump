package eu.gebes.tryjump.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlockManager implements Disposable {

    Map<Block.Type, Texture> textures = new HashMap<>();


    public BlockManager(){
        for (Block.Type value : Block.Type.values()) {
            textures.put(value, new Texture(Gdx.files.internal(value.getPath())));
        }
    }

    public Block getBlockFor(Block.Type type){
        return new Block(textures.get(type), type);
    }

    public Texture getTextureFor(Block.Type type){
        return textures.get(type);
    }

    @Override
    public void dispose() {
        for (Map.Entry<Block.Type, Texture> typeTextureEntry : textures.entrySet()) {
            typeTextureEntry.getValue().dispose();
        }
    }
}
