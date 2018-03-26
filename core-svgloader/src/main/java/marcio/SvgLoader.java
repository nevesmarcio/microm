package marcio;

import marcio.batik.IAppendable;
import marcio.batik.SvgService;
import marcio.nio.AsyncIOChunked;
import marcio.nio.ChunkReadHandler;
import marcio.xml.AsyncXmlParserService;
import marcio.xml.XmlNodeHandler;
import marcio.xml.codec.XmlNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;


public class SvgLoader {

    private static final Logger log = LoggerFactory.getLogger(SvgLoader.class);

    final private AsyncIOChunked asyncIOChunked = new AsyncIOChunked();
    final private AsyncXmlParserService asyncXmlService = new AsyncXmlParserService();
    final private SvgService svgService;

    public SvgLoader(IAppendable loadedActorHandler) {
        svgService = new SvgService(loadedActorHandler);

        asyncXmlService.setXmlNodeHandler(new XmlNodeHandler() {
            @Override
            public void handle(XmlNode xmlNode) {
                svgService.feedInput(xmlNode);
            }
        });
    }

    public void loadSvg(String path) throws IOException {
        asyncIOChunked.asyncRead(path, new ChunkReadHandler() {
            @Override
            public void handle(String chunk) {
                try {
                    log.info("chunk read - feeding parser>>>");
                    asyncXmlService.feedInput(chunk.getBytes(), 0, chunk.getBytes().length);
                } catch (XMLStreamException e) {
                    log.error("Couldn't parse chunk!");
                }
            }
        });
    }

}