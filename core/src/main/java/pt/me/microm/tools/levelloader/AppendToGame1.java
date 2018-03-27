package pt.me.microm.tools.levelloader;

import marcio.batik.IAppendable;
import marcio.batik.game1.LoadedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AppendToGame1 implements IAppendable {

    private static final Logger log = LoggerFactory.getLogger(AppendToGame1.class);

    @Override
    public void append(LoadedActor loadedActor) {
        if (loadedActor.id.contains("camera")) {
            log.info("loadedActor={}", loadedActor);
            addCamera(loadedActor);
        } else if (loadedActor.id.contains("board")) {
            log.info("loadedActor={}", loadedActor);
            addBoard(loadedActor);
        } else if (loadedActor.id.contains("daBox")) {
            log.info("loadedActor={}", loadedActor);
            addDaBox(loadedActor);
        } else if (loadedActor.id.contains("spawn")) {
            log.info("loadedActor={}", loadedActor);
            addSpawn(loadedActor);
        } else if (loadedActor.id.contains("goal")) {
            log.info("loadedActor={}", loadedActor);
            addGoal(loadedActor);
        } else if (loadedActor.id.contains("ground")) {
            log.info("loadedActor={}", loadedActor);
            addGround(loadedActor);
        } else if (loadedActor.id.contains("portal")) {
            log.info("loadedActor={}", loadedActor);
            addPortal(loadedActor);
        } else if (loadedActor.id.contains("wall")) {
            log.info("loadedActor={}", loadedActor);
            addWall(loadedActor);
        } else if (loadedActor.id.contains("star")) {
            log.info("loadedActor={}", loadedActor);
            addStar(loadedActor);
        } else if (loadedActor.id.contains("text")) {
            log.info("loadedActor={}", loadedActor);
            addText(loadedActor);
        } else if (loadedActor.id.contains("trigger")) {
            log.info("loadedActor={}", loadedActor);
            addTrigger(loadedActor);
        } else {
            log.warn("unsupported loadedActor={}", loadedActor);

        }
    }

    public abstract void addCamera(LoadedActor loadedActor);

    public abstract void addBoard(LoadedActor loadedActor);

    public abstract void addDaBox(LoadedActor loadedActor);

    public abstract void addSpawn(LoadedActor loadedActor);

    public abstract void addGoal(LoadedActor loadedActor);

    public abstract void addGround(LoadedActor loadedActor);

    public abstract void addPortal(LoadedActor loadedActor);

    public abstract void addWall(LoadedActor loadedActor);

    public abstract void addStar(LoadedActor loadedActor);

    public abstract void addText(LoadedActor loadedActor);

    public abstract void addTrigger(LoadedActor loadedActor);
}
