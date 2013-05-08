package pt.me.microm.tools.levelloader;
import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

/**
 * 
 */

/**
 * @author mneves
 *
 */
public class BasicShapeTest {
	private BasicShape basicShape; 
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		basicShape = null;
	}

	/**
	 * Test method for {@link pt.me.microm.tools.levelloader.BasicShape#getFillColor()}.
	 */
	@Test
	public void testGetFillColor() {
		//fail("Not yet implemented");
		basicShape = new BasicShape("m 425.67827,432.93665 89.09545,0 0,118.79393 -89.09545,0 z",
									"fill:#ff6600;fill-opacity:0.71;stroke:none",
									new Vector2(),
									new Vector2(),
									ObjectType.NONE);
		Logger log = Logger.getLogger(BasicShapeTest.class.getSimpleName());
		log.info(basicShape.getFillColor().toString());
		
		assert(basicShape.getFillColor().r == (float)0xff/(float)0xff);
		assert(basicShape.getFillColor().g == (float)0x66/(float)0xff);
		assert(basicShape.getFillColor().b == (float)0x00/(float)0xff);
		assert(basicShape.getFillColor().a == 0.71f);
	}

}
