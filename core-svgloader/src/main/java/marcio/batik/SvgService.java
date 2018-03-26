package marcio.batik;

import marcio.batik.custom.MyPathHandler;
import marcio.batik.custom.MyTransformListHandler;
import marcio.batik.game1.LoadedActor;
import marcio.transform.Coordinate;
import marcio.xml.codec.XmlNode;

import org.apache.batik.parser.PathParser;
import org.apache.batik.parser.TransformListParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SvgService {
    private static final Logger log = LoggerFactory.getLogger(SvgService.class);


    private IAppendable iAppendable;

    /* todo: assess the need to initiatilze the SVG service with a given AffineTransformation, to transform from SVG to the output coordinate system
     * that can either be done in the SVG service, or the class that implements the IAppendable
     * probably here is more efficient rather that transforming every point again after the loadedActor is delegated to the game logic
     * on the other hand, there will be more transformations needed such as convex, concave forms, and that is game dependant
     * */
    public SvgService(IAppendable iAppendable) {
        this.iAppendable = iAppendable;
    }

    public void feedInput(XmlNode xmlNode) {

        log.info("parsing: {}", xmlNode.toString());
        switch (xmlNode.nodeType) {
            case "path":

                MyPathHandler ph = new MyPathHandler();
                PathParser pp = new PathParser();
                pp.setPathHandler(ph);

                MyTransformListHandler th = new MyTransformListHandler();
                TransformListParser tlp = new TransformListParser();
                tlp.setTransformListHandler(th);

                //Cannot blindly process all attributes - order matters --> d, transform, id, custom-script
                String d = xmlNode.attributes.get("d");
                log.info("Processing attribute='{}' with value='{}'", "d", d); //mandatory
                pp.parse(d);

                //optional
                String transform = xmlNode.attributes.get("transform");
                log.info("Processing attribute='{}' with value='{}'", "transform", transform);
                if (transform != null) {
                    tlp.parse(transform);
                } else {
                    th.at.setToIdentity();
                }

                String id = xmlNode.attributes.get("id");
                log.info("Processing attribute='{}' with value='{}'", "id", id);
                //todo: parse id if not null

                String custom_script = xmlNode.attributes.get("custom-script");
                log.info("Processing attribute='{}' with value='{}'", "custom-script", custom_script);
                //todo: parse custom-script if not null

                //todo: call IAppendable (which is still agnostic of the game)
                LoadedActor loadedActor = new LoadedActor();
                loadedActor.path = new ArrayList<>();
                for (Coordinate src : ph.path) {
                    Coordinate dest = new Coordinate();
                    th.at.transform(src, dest);
                    loadedActor.path.add(dest);
                }
                loadedActor.id = id;
                loadedActor.behaviour = custom_script;

                iAppendable.append(loadedActor); // the appendable object characteristics are commented in the interface definition


                break;
            default:
                log.warn("Ignoring '{}' element", xmlNode.nodeType);
        }
    }

}
