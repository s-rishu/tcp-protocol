package TCPActions;
import Fsm.Action;
import Fsm.FSM;
import Fsm.Event;

public class Default extends Action{
    public Default() {
        super();
    }

    public void execute(FSM var1, Event var2) {
        System.out.println("Event " + var2.getName() + " received, current State is " + var1.currentState());
    }
}
