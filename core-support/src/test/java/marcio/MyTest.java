package marcio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import helper.GameTest;
import org.junit.Test;

public class MyTest extends GameTest {

    @Test
    public void stuff() {
        XptoXml xmlReader = new XptoXml();
        FileHandle fileHandle = Gdx.files.classpath("logback.xml");

        XmlReader.Element xe = xmlReader.parse(fileHandle);
        System.out.println(xe);
    }

}
