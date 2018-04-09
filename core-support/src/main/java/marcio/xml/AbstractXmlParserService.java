package marcio.xml;

import marcio.xml.codec.XmlNode;

import java.util.Stack;

public abstract class AbstractXmlParserService {

    //region notifier interface
    protected IXmlNodeEmitter xmlNodeEmitter;

    public void setXmlNodeEmitter(IXmlNodeEmitter xmlNodeEmitter) {
        this.xmlNodeEmitter = xmlNodeEmitter;
    }
    //endregion notifier interface

    //region feeder interface
    public abstract void feedInput(byte[] data, int offset, int len) throws Exception;

    public abstract void endOfInput();
    //endregion feeder interface


    //region context maintenance
    protected Stack<XmlNode> xmlElements = new Stack<>();

    protected String getCurrentContext() {
        StringBuilder sb = new StringBuilder();
        for (XmlNode dn : xmlElements) {
            sb.append("/");
            sb.append(dn.nodeType);
        }
        if (!(sb.length() > 0)) {
            sb.append("/");
        }
        return sb.toString();
    }
    //endregion context maintenance

}
