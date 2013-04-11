package pt.me.microm.model.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import pt.me.microm.model.AbstractModel;

/**
 * 
 * @author Márcio Neves
 *
 * Esta classe deverá fazer a gestão das adições/ remoções/ transformações sobre o mundo
 * Serve para garantir que estas operações são feitas fora do step
 */
public class WorldModelManager {

	private Queue<PointerToFunction> toAddQueue;
//	private HashMap<String, AbstractModel> registeredObjects;	
	
	public WorldModelManager() {
		toAddQueue = new ConcurrentLinkedQueue<PointerToFunction>();
//		registeredObjects = new HashMap<String, AbstractModel>();
		
	}
	
	/**
	 * Função que permite o agendamento da manipulação dos objectos fora do step do motor fisico
	 * 
	 * @param e
	 */
	public void add(PointerToFunction e) {
		toAddQueue.add(e);
	}

	
	/**
	 * Função a invocar fora do step do motor fisico
	 */
	public void process() {
		Iterator<PointerToFunction> it = toAddQueue.iterator();
		while (it.hasNext()) {
			PointerToFunction pm = it.next();
			pm.handler();
			it.remove();
		}
	}
	

	

	/**
	 * Uso este interface para garantir que os objectos são criados fora do
	 * "step" do box2d
	 */
	public interface PointerToFunction {
		public Object handler(Object... a);
	}	
	
}
