package eu.gebes.tryjump;

import eu.gebes.tryjump.blocks.Block;
import eu.gebes.tryjump.blocks.BlockManager;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorldGenerator {

    BlockManager blockManager;
    int width;
    int height;
    int depth;

    public Block[][][] generate() {

        Block[][][] blocks = new Block[width][height][depth];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {


                }
            }
        }

        return blocks;
    }


}