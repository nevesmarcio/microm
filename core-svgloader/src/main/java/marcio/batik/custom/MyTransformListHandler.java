package marcio.batik.custom;

import marcio.transform.AffineTransformation;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.TransformListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTransformListHandler implements TransformListHandler {
    private static final Logger log = LoggerFactory.getLogger(MyTransformListHandler.class);

    public AffineTransformation at;

    @Override
    public void startTransformList() throws ParseException {
        log.debug("startTransformList");
        at = new AffineTransformation();
    }

    @Override
    public void matrix(float a, float b, float c, float d, float e, float f)
            throws ParseException {
        log.debug("matrix a:{}, b:{}, c:{}, d:{}, e:{}, f:{}", a, b, c, d, e, f);
        at.setTransformation(a, c, e, b, d, f);
    }

    @Override
    public void rotate(float theta) throws ParseException {
        log.debug("rotate theta:{}", theta);
    }

    @Override
    public void rotate(float theta, float cx, float cy) throws ParseException {
        log.debug("rotate theta:{}, cx:{}, cy:{}", theta, cx, cy);
    }

    @Override
    public void translate(float tx) throws ParseException {
        log.debug("translate tx:{}", tx);
        at.translate(tx, 0);
    }

    @Override
    public void translate(float tx, float ty) throws ParseException {
        log.debug("translate tx:{}, ty:{}", tx, ty);
        at.translate(tx, ty);
    }

    @Override
    public void scale(float sx) throws ParseException {
        log.debug("scale sx:{}", sx);
    }

    @Override
    public void scale(float sx, float sy) throws ParseException {
        log.debug("scale sx:{}, sy:{}", sx, sy);
    }

    @Override
    public void skewX(float skx) throws ParseException {
        log.debug("skewX skx:{}", skx);
    }

    @Override
    public void skewY(float sky) throws ParseException {
        log.debug("skewY sky:{}", sky);
    }

    @Override
    public void endTransformList() throws ParseException {
        log.debug("endTransformList");
    }
}
