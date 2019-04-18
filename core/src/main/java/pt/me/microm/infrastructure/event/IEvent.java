package pt.me.microm.infrastructure.event;


/**
 * Thanks to:
 * http://www.therealjoshua.com/2012/07/android-architecture-part-10-the
 * -activity-revisited/
 * <p>
 * O namespace *.event.* contém o conjunto mínimo de classes necessárias a
 * implementar um design pattern Observer-Observable. O observable é uma classe
 * que tem necessáriamente que extender a classe EventDispatcher para lhe
 * conferir um conjunto de propriedades métodos. O observer é uma classe que tem
 * que implementar o interface IEventListener. Para além disso ele subscreve-se
 * à escuta no conjunto de EventDispatchers dos quais quer ser notificado.
 * <p>
 * Nota: Este é o objecto transmitido pelo EventDispatcher para o EventListener.
 */
public interface IEvent {

    public Enum<?> getType();

    public Object getSource();

    /**
     * Este método só deverá ser utilizado pelos EventDispatchers para aferir
     * qual é o source do Evento. Não deve ser usado em qualquer outra
     * circunstância!
     *
     * @param source
     */
    public void setSource(Object source);

}
