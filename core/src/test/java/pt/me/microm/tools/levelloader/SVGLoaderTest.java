package pt.me.microm.tools.levelloader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import helper.GameTest;

import javax.xml.xpath.XPathExpressionException;

public class SVGLoaderTest extends GameTest {
    private static final Logger log = LoggerFactory.getLogger(SVGLoaderTest.class.getSimpleName());

    @Test
    public void testGetFillColor() {
        FileHandle f = Gdx.files.internal("test.svg");
        SVGLoader svgLoader = new SVGLoader(f);
        try {
            NodeList nodeList = svgLoader.searchElementByExpression("//svg/g/path[contains(@id,'camera')]");
            log.debug("test!");

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        log.debug("test");

    }

}