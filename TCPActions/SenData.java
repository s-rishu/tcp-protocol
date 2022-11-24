package TCPActions;
import Fsm.Action;
import Fsm.FSM;
import Fsm.Event;

public class SenData extends Action{
    public SenData() {
        super();
    }

    public void execute(FSM var1, Event var2) {
        System.out.println("DATA sent "+ var2.getValue());
    }
}