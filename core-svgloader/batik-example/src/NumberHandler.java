import org.apache.batik.parser.NumberListHandler;
import org.apache.batik.parser.ParseException;

public class NumberHandler implements NumberListHandler {
	public final String TAG = this.getClass().getSimpleName();

	float value;
	
	@Override
	public void startNumberList() throws ParseException {
		LogMessage.log(TAG, "startNumberList", "");
	}

	@Override
	public void endNumberList() throws ParseException {
		LogMessage.log(TAG, "endNumberList", "");
	}

	@Override
	public void startNumber() throws ParseException {
		LogMessage.log(TAG, "startNumber", "");

	}

	@Override
	public void endNumber() throws ParseException {
		LogMessage.log(TAG, "endNumber", "");
	}

	@Override
	public void numberValue(float v) throws ParseException {
		LogMessage.log(TAG, "numberValue", ""+v);
	}

}
