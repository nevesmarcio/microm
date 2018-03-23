package marcio.batik;

//todo: think if I want to decode the game object type in the level loader or not - maybe not as the level loader mmight be reused with different games, therefore different concepts

/**
 * The object type has the following characteristics:
 *    Agnostic of the Game
 *    Agnostic of Box2D, however the AffineTransformation can be applied already
 *    The game developer that is integrating the level loader libray has the responsibility of mapping the generic append method to the specific appends that are selected via the ID field
 *
 */
public interface IAppendable {
    void append();
}
