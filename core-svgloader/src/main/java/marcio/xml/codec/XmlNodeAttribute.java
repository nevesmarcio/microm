package marcio.xml.codec;

/**
 * Created by marcion on 18/05/2017.
 */
public class XmlNodeAttribute {
    public String name;
    public String value;

    public XmlNodeAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "XmlNodeAttribute{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
