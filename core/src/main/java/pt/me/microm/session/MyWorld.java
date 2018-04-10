package pt.me.microm.session;

import com.google.gson.annotations.Expose;

import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Marcio
 *
 */
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
	
}
