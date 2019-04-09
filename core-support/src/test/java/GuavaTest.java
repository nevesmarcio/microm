import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuavaTest {

    private static final Logger log = LoggerFactory.getLogger(GuavaTest.class.getSimpleName());

    EventBus eventBus;
    ExecutorService executor = Executors.newFixedThreadPool(5);

    @Before
    public void setUp() throws Exception {

        eventBus = new AsyncEventBus(executor);
        eventBus.register(this);
        eventBus.register(new DeadEventProcessor());
    }

    @Test
    public void testRead() throws Exception {

        log.info("start test:{}", this.getClass().getSimpleName());

        executor.submit(new Runnable() {
            @Override
            public void run() {
                log.info("submiting task");
                eventBus.post("blah!");
            }
        });

        CustomEvent ce = new CustomEvent();
        ce.setAction("__action__");
        eventBus.post(ce);
        eventBus.post(1);
        eventBus.unregister(this);
        Thread.sleep(1000);
        log.info("end test:{}", this.getClass().getSimpleName());


    }


    @Subscribe
    public void stringEvent(String event) {
        log.info("event received:{}", event);
    }

    @Subscribe
    public void someCustomEvent(CustomEvent customEvent) {
        log.info("event received:{}", customEvent);
    }


    public class DeadEventProcessor{
        @Subscribe
        public void handleDeadEvent(DeadEvent deadEvent) {
            log.info("event is dead:{}", deadEvent);
        }
//        @Subscribe
//        public void handleDeadEvendt(Object deadEvent) {
//            log.info("handling object wildcard:{}", deadEvent);
//        }
    }

    public class CustomEvent {
        private String action;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        @Override
        public String toString() {
            return "CustomEvent{" +
                    "action='" + action + '\'' +
                    '}';
        }
    }
}
