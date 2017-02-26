package pt.me.microm.infrastructure;

public interface ICommand {
	Object handler(Object... a);
}	
