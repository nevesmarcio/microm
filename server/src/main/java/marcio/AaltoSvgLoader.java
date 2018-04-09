package marcio;

import marcio.batik.IAppendable;
import marcio.batik.SvgService;
import marcio.nio.AsyncIOChunked;
import marcio.nio.IChunkReadHandler;
import marcio.transform.AffineTransformation;
import marcio.xml.AaltoXmlParserService;
import marcio.xml.IXmlNodeEmitter;
import marcio.xml.codec.XmlNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AaltoSvgLoader {

    private static final Logger log = LoggerFactory.getLogger(AaltoSvgLoader.class);

    final private AsyncIOChunked asyncIOChunked = new AsyncIOChunked();
    final private AaltoXmlParserService aaltoXmlParserService = new AaltoXmlParserService();
    final private SvgService svgService;

    public AaltoSvgLoader(AffineTransformation globalTransformation, IAppendable loadedActorHandler) {
        svgService = new SvgService(globalTransformation, loadedActorHandler);

        aaltoXmlParserService.setXmlNodeEmitter(new IXmlNodeEmitter() {
            @Override
            public void emit(XmlNode xmlNode) {
                svgService.feedInput(xmlNode);
            }
        });
    }

    public void loadSvg(String path) throws Exception {
        asyncIOChunked.asyncRead(path, new IChunkReadHandler() {
            @Override
            public void handle(String chunk) {
                try {
                    log.info("chunk read - feeding parser>>>");
                    aaltoXmlParserService.feedInput(chunk.getBytes(), 0, chunk.getBytes().length);
                } catch (Exception e) {
                    log.error("Couldn't parse chunk!");
                }
            }

            @Override
            public void noMoreInput() {
                aaltoXmlParserService.endOfInput();
            }
        });
    }

}