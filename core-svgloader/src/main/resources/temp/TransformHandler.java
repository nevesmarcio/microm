package temp;

import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.TransformListHandler;

public class TransformHandler implements TransformListHandler {
	public final String TAG = this.getClass().getSimpleName();

	private boolean debug = false;
	
	public AffineTransformation at;
	
	@Override
	public void startTransformList() throws ParseException {
		LogMessage.log(TAG, "startTransformList", "", debug);
		at = new AffineTransformation();
	}

	@Override
	public void matrix(float a, float b, float c, float d, float e, float f)
			throws ParseException {
		LogMessage.log(TAG, "matrix", "a:"+a+", b:"+b+", c:"+c+", d:"+d+", e:"+e+", f:"+f, debug);
		at.setTransformation(a, c, e, b, d, f);
		//at.setTransform(a, d, c, d, e, f);
		
	}

	@Override
	public void rotate(float theta) throws ParseException {
		LogMessage.log(TAG, "rotate", "theta:"+theta, debug);
	}

	@Override
	public void rotate(float theta, float cx, float cy) throws ParseException {
		LogMessage.log(TAG, "rotate", "theta:"+theta+", cx:"+cx+", cy:"+cy, debug);
	}

	@Override
	public void translate(float tx) throws ParseException {
		LogMessage.log(TAG, "translate", "tx:"+tx, debug);
		at.translate(tx, 0);
	}

	@Override
	public void translate(float tx, float ty) throws ParseException {
		LogMessage.log(TAG, "translate", "tx:"+tx+", ty:"+ty, debug);
		at.translate(tx, ty);
	}

	@Override
	public void scale(float sx) throws ParseException {
		LogMessage.log(TAG, "scale", "sx:"+sx, debug);
	}

	@Override
	public void scale(float sx, float sy) throws ParseException {
		LogMessage.log(TAG, "scale", "sx:"+sx+", sy:"+sy, debug);
	}

	@Override
	public void skewX(float skx) throws ParseException {
		LogMessage.log(TAG, "skewX", "skx:"+skx, debug);
	}

	@Override
	public void skewY(float sky) throws ParseException {
		LogMessage.log(TAG, "skewY", "sky:"+sky, debug);
	}

	@Override
	public void endTransformList() throws ParseException {
		LogMessage.log(TAG, "entTransformList", "", debug);
	}

}
