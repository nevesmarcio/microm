import org.apache.batik.parser.ParseException;

public class LengthHandler implements org.apache.batik.parser.LengthHandler {
	public final String TAG = this.getClass().getSimpleName();
	private boolean debug = true;
	
	public float value;
	
	@Override
	public void startLength() throws ParseException {
		LogMessage.log(TAG, "startLength", "", debug);
	}

	@Override
	public void lengthValue(float v) throws ParseException {
		LogMessage.log(TAG, "lengthValue", ""+v, debug);
		value = v;
	}

	@Override
	public void em() throws ParseException {
		LogMessage.log(TAG, "em", "", debug);
	}

	@Override
	public void ex() throws ParseException {
		LogMessage.log(TAG, "ex", "", debug);
	}

	@Override
	public void in() throws ParseException {
		LogMessage.log(TAG, "in", "", debug);
	}

	@Override
	public void cm() throws ParseException {
		LogMessage.log(TAG, "cm", "", debug);
	}

	@Override
	public void mm() throws ParseException {
		LogMessage.log(TAG, "mm", "", debug);
	}

	@Override
	public void pc() throws ParseException {
		LogMessage.log(TAG, "pc", "", debug);
	}

	@Override
	public void pt() throws ParseException {
		LogMessage.log(TAG, "pt", "", debug);
	}

	@Override
	public void px() throws ParseException {
		LogMessage.log(TAG, "px", "", debug);
	}

	@Override
	public void percentage() throws ParseException {
		LogMessage.log(TAG, "%", "", debug);
	}

	@Override
	public void endLength() throws ParseException {
		LogMessage.log(TAG, "endLength", "", debug);
	}

}
