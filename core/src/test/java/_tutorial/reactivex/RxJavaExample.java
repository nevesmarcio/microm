package _tutorial.reactivex;

import helper.GameTest;
import io.reactivex.*;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RxJavaExample extends GameTest {
    private static final Logger log = LoggerFactory.getLogger(RxJavaExample.class);

    private ScheduledThreadPoolExecutor sch;

    @Before
    public void setUp() throws Exception {
        sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
    }

    @Test
    public void example1() throws InterruptedException {
        log.info("start");
        Observable observable = Observable.just("Hello");

        Observable observable1 = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(final ObservableEmitter e) throws Exception {
                sch.scheduleAtFixedRate(
                        new Runnable() {
                            @Override
                            public void run() {
                                e.onNext("*");
                            }
                        },
                        0,
                        333,
                        TimeUnit.MILLISECONDS);
            }
        });

        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                log.info(o.toString());
            }
        });

        observable1.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                log.debug(o.toString());
            }
        });

        log.info("end");
        while (true) {
            Thread.sleep(1000);
        }

    }

    /**
     * http://www.vogella.com/tutorials/RxJava/article.html
     * https://github.com/ReactiveX/RxJava/wiki/Creating-Observables
     * https://medium.com/yammer-engineering/converting-callback-async-calls-to-rxjava-ebc68bde5831
     * https://github.com/ReactiveX/RxJava/wiki/What%27s-different-in-2.0#entering-the-reactive-world
     * https://afterecho.uk/blog/turning-a-callback-into-an-rx-observable.html
     *
     * https://blog.thoughtram.io/angular/2016/06/16/cold-vs-hot-observables.html
     * @throws InterruptedException
     */
    @Test
    public void example2() throws InterruptedException {

        Flowable<Integer> flowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final FlowableEmitter<Integer> e) throws Exception {
                sch.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        e.onNext(Integer.valueOf(1));
                    }
                }, 0, 333, TimeUnit.MILLISECONDS);
            }
        }, BackpressureStrategy.LATEST);

        flowable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                log.info(">" + integer.toString());
            }
        });

        while(true) {
            Thread.sleep(1000);
        }
    }

}
