package marcio.batik.custom;

import org.apache.batik.parser.NumberListHandler;
import org.apache.batik.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyNumberListHandler implements NumberListHandler {
    private static final Logger log = LoggerFactory.getLogger(MyNumberListHandler.class);
	public float value;
	
	@Override
	public void startNumberList() throws ParseException {
        log.debug("startNumberList");
	}

	@Override
	public void endNumberList() throws ParseException {
        log.debug("endNumberList");
	}

	@Override
	public void startNumber() throws ParseException {
        log.debug("startNumber");
	}

	@Override
	public void endNumber() throws ParseException {
        log.debug("endNumber");
	}

	@Override
	public void numberValue(float v) throws ParseException {
        log.debug("numberValue:{}",v);
        this.value=v;
	}

}
