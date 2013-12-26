package pt.me.microm.session;

import com.google.gson.annotations.Expose;

/**
 * 
 * @author Marcio
 *
 */
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
	
}
