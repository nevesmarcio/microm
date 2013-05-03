package pt.me.microm.session;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.Expose;

public class MyWorld {
	@Expose private String name;
	private List<MyLevel> levels;
	
	
	public String getName() {
		return name;
	}
	protected void setName(String name) {
		this.name = name;
	}

	protected void setLevels(List<MyLevel> levels) { // only available within this package
		this.levels = levels;
	}
	public List<MyLevel> getLevels() {
		return Collections.unmodifiableList(levels);
	}

	
	
	/////// Data properties ////////
	@Expose protected boolean unlocked;				// indica se o world já foi desbloqueado
	
	@Expose protected MyLevel current_story_level; 	// guarda nível actual deste mundo
	@Expose protected int 	current_death_count; 		// guarda o actual numero de mortes acumuladas
	
	@Expose protected HashMap<Date, Integer> scores; 	// guarda o número de vezes que se chegou ao fim do world e com quantas mortes	

	public boolean isUnlocked() {
		return unlocked;
	}
	
	public int getCurrentDeathCount() {
		return current_death_count;
	}
	public void addCurrentDeathCount() {
		current_death_count+=1;
	}
	
	
	protected void completeDataFrom(MyWorld srcMyWorld) {
		this.unlocked = srcMyWorld.unlocked;
		this.current_story_level = srcMyWorld.current_story_level;
		this.current_death_count = srcMyWorld.current_death_count;
		
		if (srcMyWorld.scores != null)
			this.scores = new HashMap<Date, Integer>(srcMyWorld.scores);
	}
	
}
