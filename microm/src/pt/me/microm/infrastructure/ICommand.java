package pt.me.microm.infrastructure;

public interface ICommand {
	public Object handler(Object... a);
}	
