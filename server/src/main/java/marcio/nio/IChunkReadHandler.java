package marcio.nio;

public interface IChunkReadHandler {
    void handle(String chunk);

    void noMoreInput();
}
