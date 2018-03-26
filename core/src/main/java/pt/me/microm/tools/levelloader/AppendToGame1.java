package pt.me.microm.tools.levelloader;

import marcio.batik.IAppendable;
import marcio.batik.game1.LoadedActor;

public abstract class AppendToGame1 implements IAppendable {

    @Override
    public void append(LoadedActor loadedActor) {

    }

    public abstract void addCamera();

    public abstract void addBoard();

    public abstract void addDaBox();

    public abstract void addSpawn();

    public abstract void addGoal();

    public abstract void addGround();

    public abstract void addPortal();

    public abstract void addWall();

    public abstract void addStar();

    public abstract void addText();

    public abstract void addTrigger();
}
