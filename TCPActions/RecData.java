package TCPActions;
import Fsm.Action;
import Fsm.FSM;
import Fsm.Event;

public class RecData extends Action{
    public RecData() {
        super();
    }

    public void execute(FSM var1, Event var2) {
        System.out.println("DATA received "+ var2.getValue());
    }
}