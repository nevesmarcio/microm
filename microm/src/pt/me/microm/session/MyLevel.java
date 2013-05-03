package pt.me.microm.session;

import java.util.Date;
import java.util.HashMap;

import com.google.gson.annotations.Expose;


public class MyLevel {
	private MyWorld parentWorld;
	@Expose private String name;
	
	
	public MyWorld getParentWorld() {
		return parentWorld;
	}
	protected void setParentWorld(MyWorld parentWorld) {
		this.parentWorld = parentWorld;
	}
	
	public String getName() {
		return name;
	}
	protected void setName(String name) {
		this.name = name;
	}
	

	
	/////// Data properties ////////
	@Expose protected boolean unlocked;				// indica se o nível já foi desbloqueado
	@Expose protected HashMap<Date, Integer> scores; 	// número mínimo de mortes com que o nível foi completo	

	public boolean isUnlocked() {
		return unlocked;
	}
	
	protected void completeDataFrom(MyLevel destMyLevel) {
		this.unlocked = destMyLevel.unlocked;
		
		if (destMyLevel.scores != null)
			this.scores = new HashMap<Date, Integer>(destMyLevel.scores);		
	}
}
