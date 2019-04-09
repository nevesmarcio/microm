package pt.me.microm.model;

public interface IContact {

	/**
	 * Adiciona um ponto de contacto entre o objecto this<-->oModel
	 * 
	 * @param oModel
	 * @return devolve o número de pontos de contacto entre os 2 objectos após a
	 *         adição, ou -1 em caso de erro
	 */
	public int addPointOfContactWith(IBody oModel);
	
	
	/**
	 * Subtrai um ponto de contacto entre o objecto this<-->oModel
	 * 
	 * @param oModel
	 * @return devolve o número de pontos de contacto entre os 2 objectos após a
	 *         subtracção, ou -1 em caso de erro
	 */
	public int subtractPointOfContactWith(IBody oModel);
	
	/**
	 * Invocado quando um objecto entra em contacto com o outro. Este método
	 * abstrai o numero de pontos de contacto efectivo
	 * 
	 * @param oModel
	 */
	public void beginContactWith(IBody oModel);

	/**
	 * Invocado quando um objecto deixa de estar em contacto com o outro. Este
	 * método abstrai o numero de pontos de contacto efectivo
	 * 
	 * @param oModel
	 */
	public void endContactWith(IBody oModel);
	
	
	public void disposeNotif(AbstractModel oModel);
	
}
