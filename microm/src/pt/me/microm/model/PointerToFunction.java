package pt.me.microm.model;

/**
 * 
 * @author Márcio Neves
 * 
 *         Uso este interface para garantir que os objectos são criados fora do
 *         "step" do box2d
 * 
 */
public interface PointerToFunction {
	public Object handler(Object ... a);
}
