package pt.me.microm.model.base;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import pt.me.microm.model.PointerToFunction;

/**
 * 
 * @author Márcio Neves
 *
 * Esta classe deverá fazer a gestão das adições/ remoções/ transformações sobre o mundo
 *
 */
public class WorldModelManager {

	private Queue<PointerToFunction> toAddQueue;
	
	public WorldModelManager() {
		toAddQueue = new ConcurrentLinkedQueue<PointerToFunction>();
		
		
	}
	
	public void process() {
		Iterator<PointerToFunction> it = toAddQueue.iterator();
		while (it.hasNext()) {
			PointerToFunction pm = it.next();
			pm.handler();
			it.remove();
		}
	}
	
	public void add(PointerToFunction e) {
		toAddQueue.add(e);
		
	}
	
	
}
