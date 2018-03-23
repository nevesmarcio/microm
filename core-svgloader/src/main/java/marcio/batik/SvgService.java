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
                String d;
                log.info("Processing attribute='{}' with value='{}'", "d", d=xmlNode.attributes.get("d")); //mandatory
                pp.parse(d);

                //optional
                String transform;
                log.info("Processing attribute='{}' with value='{}'", "transform", transform=xmlNode.attributes.get("transform"));
                tlp.parse(transform);

                String id;
                log.info("Processing attribute='{}' with value='{}'", "id", id=xmlNode.attributes.get("id"));
                //todo: parse id

                String custom_script;
                log.info("Processing attribute='{}' with value='{}'", "custom-script", custom_script=xmlNode.attributes.get("custom-script"));
                //todo: parse custom-script

                //todo: call IAppendable (which is still agnostic of the game)
                iAppendable.append(); // the appendable object caracteristics are commented in the interface definition


            break;
            default:
                log.warn("Ignoring '{}' element", xmlNode.nodeType);
        }
    }


//    public void parsePaths(Element baseElement) {
//
//        NodeList nl = baseElement.getElementsByTagName("path");
//
//        // Path parser
//        OpenGLPathHandler ph = new OpenGLPathHandler();
//        PathParser pp = new PathParser();
//        pp.setPathHandler(ph);
//
//        for(int i=0; i<nl.getLength(); i++) {
//            Element elm = (Element) nl.item(i);
//
//            // Get path data
//            String path = elm.getAttribute("d");
//            LogMessage.print(path);
//            pp.parse(path);
//
//            // Get transformation data
//            String transform = elm.getAttribute("transform");
//
//            // Transform the resulting spline
//            ph.currentSpline.transfrom(parseTransform(transform));
//            addPath(ph.currentSpline);
//        }
//    }


//    private AffineTransformation parseTransform (String transform) {
//        // Parse
//        TransformHandler th = new TransformHandler();
//        TransformListParser tlp = new TransformListParser();
//
//        tlp.setTransformListHandler(th);
//        tlp.parse(transform);
//
//        //at.composeBefore(th.at);
//        return th.at;
//    }

}
