package marcio.batik;

import marcio.batik.custom.MyPathHandler;
import marcio.batik.custom.MyTransformListHandler;
import marcio.xml.codec.XmlNode;

import org.apache.batik.parser.PathParser;
import org.apache.batik.parser.TransformListParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SvgService {
    private static final Logger log = LoggerFactory.getLogger(SvgService.class);


    private IAppendable iAppendable;

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
                tlp.parse(transform);

                String id = xmlNode.attributes.get("id");
                log.info("Processing attribute='{}' with value='{}'", "id", id);
                //todo: parse id

                String custom_script = xmlNode.attributes.get("custom-script");
                log.info("Processing attribute='{}' with value='{}'", "custom-script", custom_script);
                //todo: parse custom-script

                //todo: call IAppendable (which is still agnostic of the game)
                iAppendable.append(); // the appendable object characteristics are commented in the interface definition


                break;
            default:
                log.warn("Ignoring '{}' element", xmlNode.nodeType);
        }
    }

}
