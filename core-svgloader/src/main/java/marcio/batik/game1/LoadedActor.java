package marcio.batik.game1;

import marcio.transform.Coordinate;

import java.util.ArrayList;

public class LoadedActor {
    public ArrayList<Coordinate> path;
    public String id;
    public String behaviour;

    @Override
    public String toString() {
        return "LoadedActor{" +
                "path=" + path +
                ", id='" + id + '\'' +
                ", behaviour='" + behaviour + '\'' +
                '}';
    }
}
