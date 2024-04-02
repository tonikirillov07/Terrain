package org.darkness.world;

import lombok.AllArgsConstructor;
import org.darkness.engine.GlobalRender;
import org.darkness.engine.models.Cube;
import org.darkness.engine.sounds.SoundsConstants;
import org.darkness.engine.utils.textures.TexturesConstants;
import org.darkness.engine.utils.transform.Rotation;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

import static org.darkness.Constants.WHITE_COLOR;
import static org.darkness.Constants.WORLD_SIZE;

@AllArgsConstructor
public class Map {
    private final GlobalRender globalRender;

    public void init(){
        for (int i = 0; i < WORLD_SIZE[0]; i++) {
            for (int j = 0; j < WORLD_SIZE[1]; j++) {
                int randomTexture = new Random().nextInt(1, 4);
                int currentTexture =  randomTexture == 1 ? TexturesConstants.DIRT_TEXTURE : randomTexture == 2 ? TexturesConstants.STONE_TEXTURE: TexturesConstants.GRASS_TEXTURE;

                globalRender.load(new Cube(new Vector3f(i, 0, j), new Rotation(-90f, 1, 0, 0), WHITE_COLOR, currentTexture, 1f, randomTexture != 2 ? SoundsConstants.GROUND_SOUNDS: SoundsConstants.STONE_SOUNDS));

            }
        }

        globalRender.load(new Cube(new Vector3f((float) WORLD_SIZE[0] / 3, 2 , (float) WORLD_SIZE[1] / 3), Rotation.IDENTITY, WHITE_COLOR, new Random().nextInt(1, 3) == 1 ? TexturesConstants.DIRT_TEXTURE : TexturesConstants.GRASS_TEXTURE, 1f));
        globalRender.load(new Cube(new Vector3f((float) WORLD_SIZE[0] / 2, 2 , (float) WORLD_SIZE[1] / 2), Rotation.IDENTITY, WHITE_COLOR, new Random().nextInt(1, 3) == 1 ? TexturesConstants.DIRT_TEXTURE : TexturesConstants.GRASS_TEXTURE, 1f));

    }
}
