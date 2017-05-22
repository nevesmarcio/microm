package marcio.xml;

import com.badlogic.gdx.Gdx;
import helper.GameTest;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import marcio.rx.io.AsyncFileReader;
import marcio.xml.codec.XmlNode;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * http://reactivex.io/documentation/operators.html#transforming
 * http://blog.danlew.net/2015/03/02/dont-break-the-chain/
 *http://akarnokd.blogspot.com/2016/02/flatmap-part-1.html
 * Goal for this test:
 *    create a chain that transforms the output
 *      1. clean whitespaces
 *      2.
 */
public class AsyncXmlTestWithReactiveX extends GameTest {
    private static final Logger logger = LoggerFactory.getLogger(AsyncXmlTestWithReactiveX.class);

    private String p;

    @Before
    public void setUp() throws Exception {
//        p = Gdx.files.local("./build/resources/test/test.svg").path();
        p = Gdx.files.local("./build/resources/test/employee.xml").path();
    }

    @Test
    public void testRead() throws IOException, InterruptedException {
        AsyncXmlService asyncXmlService = new AsyncXmlService(null);

        AsyncFileReader
                .read(p)
                .compose(asyncXmlService)
//                .filter(new Predicate<XmlNode>() {
//                    @Override
//                    public boolean test(XmlNode xmlNode) throws Exception {
//                        return !xmlNode.context.equals("/employee");
//                    }
//                })
                .subscribe(new Consumer<XmlNode>() {
                    @Override
                    public void accept(XmlNode s) throws Exception {
                        logger.info("***> {}", s);
                    }
                });

        Thread.sleep(3000);//1 second to give some time for the subscribers to actually do some work
    }

}