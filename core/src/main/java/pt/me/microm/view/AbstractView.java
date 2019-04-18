package pt.me.microm.view;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.me.microm.controller.loop.ScreenTickManager;
import pt.me.microm.controller.loop.itf.IScreenTick;
import pt.me.microm.infrastructure.event.IEvent;
import pt.me.microm.infrastructure.event.listener.IEventListener;
import pt.me.microm.model.AbstractModel;

import java.util.UUID;

public abstract class AbstractView implements Disposable, IScreenTick {
    private static final String TAG = AbstractView.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(TAG);

    // all reusable view facilities can be declared like this renderer
    protected static final ShapeRenderer renderer = new ShapeRenderer();


    private AbstractModel modelRef;

    public AbstractView(AbstractModel model) {
        this(model, 0);
    }

    private UUID devID;
    private String simpleName;

    public AbstractView(AbstractModel modelRef, final int zIndex) {
        if (logger.isDebugEnabled())
            logger.debug("++abstract ctor of {} | allocID: {}", simpleName = this.getClass().getSimpleName(), (devID = UUID.randomUUID()).toString());

        this.modelRef = modelRef;
        this.modelRef.addListener(AbstractModel.EventType.ON_MODEL_INSTANTIATED, new IEventListener() {

            @Override
            public void onEvent(IEvent event) {
                // Sem o PostRunnable isto iria correr na thread do model
                // Assim garante-se que este delayed init corre na thread do GUI
                ScreenTickManager.PostRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (logger.isDebugEnabled())
                            logger.debug("..abstract method {}.DelayedInit() about to be called", simpleName);
                        DelayedInit();
                        // Regista este objecto para ser informado dos screen ticks
                        // Este registo só pode ser efectuado depois do Modelo instanciado
                        ScreenTickManager.getInstance().addEventListener(AbstractView.this, zIndex);

                    }
                });
            }
        });
    }

    public abstract void DelayedInit();

    @Override
    public void dispose() {
        if (logger.isDebugEnabled()) logger.debug("--abstract dispose of {} | allocID: {}", simpleName, devID);

        this.modelRef = null;

        //Elimina o registo deste objecto para ser informado dos screen ticks
        ScreenTickManager.getInstance().removeEventListener(this);
    }

    @Override
    protected void finalize() throws Throwable {
        if (logger.isDebugEnabled()) logger.debug("{} was GC'ed | allocID: {}", simpleName, devID);
        super.finalize();
    }

}
