package pt.me.microm.model;

public interface IContact {

	/**
	 * Adiciona um ponto de contacto entre o objecto this<-->oModel
	 * 
	 * @param oModel
	 * @return devolve o número de pontos de contacto entre os 2 objectos após a
	 *         adição, ou -1 em caso de erro
	 */
	public int addPointOfContactWith(ICanCollide oModel);
	
	
	/**
	 * Subtrai um ponto de contacto entre o objecto this<-->oModel
	 * 
	 * @param oModel
	 * @return devolve o número de pontos de contacto entre os 2 objectos após a
	 *         subtracção, ou -1 em caso de erro
	 */
	public int subtractPointOfContactWith(ICanCollide oModel);
	
	
	
	
	
	
	
	
	
	
	public void disposeNotif(AbstractModel oModel);
	
}
