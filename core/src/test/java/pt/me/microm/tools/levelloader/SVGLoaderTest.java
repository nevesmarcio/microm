package pt.me.microm.tools.levelloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import helper.GameTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;

import static pt.me.microm.tools.levelloader.XmlUtil.asList;

public class SVGLoaderTest extends GameTest {
    private static final Logger log = LoggerFactory.getLogger(SVGLoaderTest.class.getSimpleName());

    @Test
    public void testGetFillColor() {
        FileHandle f = Gdx.files.internal("test.svg");
        SVGLoader svgLoader = new SVGLoader(f);
        try {
            NodeList nodeList = svgLoader.searchElementByExpression("//svg/g/path[contains(@id,'camera')]");
            for (Node n: asList(nodeList))
                log.debug("::{}", new Object[]{n});
            log.debug("test!");

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        log.debug("test");

    }

}