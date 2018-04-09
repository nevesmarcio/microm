package marcio;

import com.badlogic.gdx.files.FileHandle;
import marcio.batik.IAppendable;
import marcio.batik.SvgService;
import marcio.transform.AffineTransformation;
import marcio.xml.IXmlNodeEmitter;
import marcio.xml.LibgdxXmlParserService;
import marcio.xml.codec.XmlNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LibgdxSvgLoader {

    private static final Logger log = LoggerFactory.getLogger(LibgdxSvgLoader.class);

    final private LibgdxXmlParserService libgdxXmlParserService = new LibgdxXmlParserService();
    final private SvgService svgService;

    public LibgdxSvgLoader(AffineTransformation globalTransformation, IAppendable loadedActorHandler) {
        svgService = new SvgService(globalTransformation, loadedActorHandler);

        libgdxXmlParserService.setXmlNodeEmitter(new IXmlNodeEmitter() {
            @Override
            public void emit(XmlNode xmlNode) {
                svgService.feedInput(xmlNode);
            }
        });
    }

    public void loadSvg(FileHandle fileHandle) throws Exception {
        final String readContent = fileHandle.readString();
        libgdxXmlParserService.feedInput(readContent.toString().getBytes(), 0, readContent.toString().getBytes().length);

    }

}