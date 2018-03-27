package marcio.batik;

import marcio.batik.custom.MyPathHandler;
import marcio.batik.custom.MyTransformListHandler;
import marcio.batik.game1.LoadedActor;
import marcio.transform.AffineTransformation;
import marcio.transform.Coordinate;
import marcio.xml.codec.XmlNode;

import org.apache.batik.parser.PathParser;
import org.apache.batik.parser.TransformListParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SvgService {
    private static final Logger log = LoggerFactory.getLogger(SvgService.class);


    private AffineTransformation globalTransformation;
    private IAppendable iAppendable;

    /**
     * SvgService outputs LoadedActors through iAppendable interface
     * The service receives a globalTransformation parameter of type AffineTransformation to allow the instantiator to determine
     * a transformation to be applied to every Actor
     *
     * todo: the instantiator will need a way to be notified that no further input is to be expected (is this a good thing?)
     *       we might want the concept of never ending world, therefore the level loading is a feed
     *
     * @param globalTransformation
     * @param iAppendable
     */
    public SvgService(AffineTransformation globalTransformation, IAppendable iAppendable) {
        this.globalTransformation = globalTransformation;
        this.iAppendable = iAppendable;
    }

    public void feedInput(XmlNode xmlNode) {

        log.info("parsing: {}", xmlNode.toString());
        switch (xmlNode.nodeType) {
            case "svg":
                log.info("processing svg element");
                /* todo: i was thinking about extracting viewbox property for the camera, but that would restrict the level to one camera only, and that is a limitation
                    we might want to have the camera shifting depending on triggers for example
                 */
                break;
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
                }

                //apply the global transformation
                th.at.compose(globalTransformation);

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
