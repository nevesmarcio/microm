package pt.me.microm.model.stuff;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Márcio Neves
 *
 * Esta classe deverá gerir os portais.
 * - Deve guardar uma lista dos portais e respectivas relações entre pares
 * - Deve disponibilizar operações de mais alto nivel para fazer o teleport p.e.
 */
public class PortalModelManager {
	public List<PortalModel> portals;
	
	public PortalModelManager() {
		portals = new ArrayList<PortalModel>();
		
	}
	

}
