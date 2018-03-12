package marcio.rx.io;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * http://reactivex.io/documentation/operators.html#transforming
 * http://blog.danlew.net/2015/03/02/dont-break-the-chain/
 *http://akarnokd.blogspot.com/2016/02/flatmap-part-1.html
 * Goal for this test:
 *    create a chain that transforms the output
 *      1. clean whitespaces
 *      2.
 */
public class AsyncFileReaderCompositionTest{
    private static final Logger logger = LoggerFactory.getLogger(AsyncFileReaderCompositionTest.class);

    private String p;

    @Before
    public void setUp() throws Exception {
        p = "./build/resources/test/test.svg";
//        p = "./build/resources/test/employee.xml";
    }

    @Test
    public void testRead() throws IOException, InterruptedException {
        AsyncFileReader
                .read(p)
                .compose(new FlowableTransformer<String, String>() { // transforms the source observable, not to the emitted items
                    @Override
                    public Publisher<String> apply(Flowable<String> upstream) {
                        logger.debug("composing");
                        return upstream
                                .observeOn(Schedulers.computation())
                                .subscribeOn(Schedulers.io()); // only affects subscription phase
                    }
                })
                .map(new Function<String, String>() { // transforms the emmited items
                    @Override
                    public String apply(String s) throws Exception {
                        logger.debug("mapping");
                        return s.replace(" ", "µ");
                    }
                })
                .concatMap(new Function<String, Publisher<String>>() { // or flatMap (concatMap preserves the order) - transforms the items emitted by the source observable to publishers (observables)
                    @Override
                    public Publisher<String> apply(String s) throws Exception {
                        logger.debug("concating");
                        s=s.replace("µ","");
                        s=s.replace(System.getProperty("line.separator"), "");
                        return Flowable
                                .just(s)
                                .observeOn(Schedulers.newThread());
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        logger.info("0>>{}", s);
                    }
                });


//        AsyncFileReader.read(p).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                logger.info("1>>{}", s);
//            }
//        });

//        AsyncFileReader.read(p).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                logger.info("2>>{}", s);
//            }
//        });

        Thread.sleep(1000);//1 second to give some time for the subscribers to actually do some work
    }

}