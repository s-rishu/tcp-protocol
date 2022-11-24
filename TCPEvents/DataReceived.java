package TCPEvents;
import Fsm.Event;
public class DataReceived extends Event{
    public DataReceived() {
        super("RDATA", 0);
    }
}
