package marcio;

import com.badlogic.gdx.utils.XmlReader;

public class XptoXml extends XmlReader {
    @Override
    protected void open(String name) {
        super.open(name);
    }

    @Override
    protected void attribute(String name, String value) {
        super.attribute(name, value);
    }

    @Override
    protected String entity(String name) {
        return super.entity(name);
    }

    @Override
    protected void text(String text) {
        super.text(text);
    }

    @Override
    protected void close() {
        super.close();
    }
}
