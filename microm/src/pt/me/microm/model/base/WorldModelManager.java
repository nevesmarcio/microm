package pt.me.microm.model.base;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import pt.me.microm.infrastructure.ICommand;

/**
 * 
 * @author Márcio Neves
 *
 * Esta classe deverá fazer a gestão das adições/ remoções/ transformações sobre o mundo
 * Serve para garantir que estas operações são feitas fora do step
 * Uso o Command Interface para garantir que os objectos são criados fora do
 * "step" do box2d
 * 
 */
public class WorldModelManager {

	private Queue<ICommand> toAddQueue;
//	private HashMap<String, AbstractModel> registeredObjects;	
	
	public WorldModelManager() {
		toAddQueue = new ConcurrentLinkedQueue<ICommand>();
//		registeredObjects = new HashMap<String, AbstractModel>();
		
	}
	
	/**
	 * Função que permite o agendamento da manipulação dos objectos fora do step do motor fisico
	 * 
	 * @param e
	 */
	public void add(ICommand e) {
		toAddQueue.add(e);
	}

	
	/**
	 * Função a invocar fora do step do motor fisico
	 */
	public void process() {
		Iterator<ICommand> it = toAddQueue.iterator();
		while (it.hasNext()) {
			ICommand pm = it.next();
			pm.handler();
			it.remove();
		}
	}

}
