package pt.me.microm.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import pt.me.microm.tools.levelloader.BasicShape;

public interface IBody {
    String getName();

    BasicShape getBasicShape();
    Vector2 getPosition();
    float getAngle();

    //FIXME:: não quero expor o body!! - temporário!
    Body getBody();
    void setBody(Body body);

}
