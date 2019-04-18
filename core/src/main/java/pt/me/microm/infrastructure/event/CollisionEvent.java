package pt.me.microm.infrastructure.event;


/*
 * Thanks to: http://www.therealjoshua.com/2012/07/android-architecture-part-10-the-activity-revisited/
 *
 * Nota: Implementação base do interface Event
 */

public class CollisionEvent implements IEvent {

    protected Object source;
    private Enum<?> type;

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public void setSource(Object source) {
        this.source = source;
    }

    @Override
    public Enum<?> getType() {
        return type;
    }

    public CollisionEvent(Enum<?> type) {
        this.type = type;
    }


    private String a, b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }


    private String script;

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }


}
