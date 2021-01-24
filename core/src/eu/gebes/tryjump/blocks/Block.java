package eu.gebes.tryjump.blocks;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import eu.gebes.tryjump.Variables;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Block implements Disposable {

    private static ModelBuilder modelBuilder = new ModelBuilder();


    Material material;
    Model model;
    ModelInstance instance;
    Type type;

    public Block(Texture texture, Type type) {
        this.type = type;

        material = new Material(TextureAttribute.createDiffuse(texture));
        modelBuilder.begin();
        modelBuilder.node();
        MeshPartBuilder mesh_part_builder = modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position
                                                                                        | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, material);
        BoxShapeBuilder.build(mesh_part_builder, Variables.blockSize, Variables.blockSize, Variables.blockSize);
        model = modelBuilder.end();

        instance = new ModelInstance(model);
    }

    public void setPosition(float x, float y, float z) {
        instance.transform = new Matrix4().translate(x, y, z);
    }

    public Vector3 getPosition() {
        float x = instance.transform.getValues()[Matrix4.M03];
        float y = instance.transform.getValues()[Matrix4.M13];
        float z = instance.transform.getValues()[Matrix4.M23];
        return new Vector3(x, y, z);
    }


    @Override
    public void dispose() {
        model.dispose();
    }

    public enum Type {


        Dirt(0), Stone(1), Planks(2), Log(3), Leaves(4), Bedrock(5);

        private Integer id;

        Type(Integer value) {
            this.id = value;
        }

        public Integer getId() {
            return id;
        }
        public static Type fromId(Integer id) {
            for (Type at : Type.values()) {
                if (at.getId().equals(id)) {
                    return at;
                }
            }
            return null;
        }

        String getPath() {
            return "block/" + this.toString().toLowerCase() + ".png";
        }

    }


}
