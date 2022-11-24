import Fsm.State;
import Fsm.FSM;
import Fsm.Transition;
import Fsm.Event;
import Fsm.Action;
import Fsm.FsmException;

import TCPActions.*;
import TCPEvents.*;
import TCPStates.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TCP {

    public static void main(String[] args) throws Exception{
        //initialize states
        State s_closed = new Closed();
        State s_listen = new Listen();
        State s_closewait = new CloseWait();
        State s_closing = new Closing();
        State s_estab = new Established();
        State s_finwait1 = new FinWait1();
        State s_finwait2 = new FinWait2();
        State s_lastack = new LastAck();
        State s_synrcvd = new SynRcvd();
        State s_synsent = new SynSent();
        State s_timewait = new TimeWait();

        //initialize events
        Event e_passive = new PassiveOpen();
        Event e_ackrcvd = new AckReceived();
        Event e_active = new ActiveOpen();
        Event e_close = new Close();
        Event e_datarcvd = new DataReceived();
        Event e_datasent = new DataSent();
        Event e_fin = new FinReceived();
        Event e_synack = new SynAck();
        Event e_synrcvd = new SynReceived();
        Event e_timeout = new TimeOut();

        //initialize actions
        Action a_def = new Default();
        Action a_recd = new RecData();
        Action a_send = new SenData();
        //initialize fsm
        FSM tcp =  new FSM("TCP", s_closed);

        //add transitions
        tcp.addTransition(new Transition(s_closed, e_passive, s_listen, a_def));
        tcp.addTransition(new Transition(s_closed, e_active, s_synsent, a_def));
        tcp.addTransition(new Transition(s_listen, e_close, s_closed, a_def));
        tcp.addTransition(new Transition(s_listen, e_synrcvd, s_synrcvd, a_def));
        tcp.addTransition(new Transition(s_synrcvd, e_ackrcvd, s_estab, a_def));
        tcp.addTransition(new Transition(s_synrcvd, e_close, s_finwait1, a_def));
        tcp.addTransition(new Transition(s_synsent, e_close, s_closed, a_def));
        tcp.addTransition(new Transition(s_synsent, e_synrcvd, s_synrcvd, a_def));
        tcp.addTransition(new Transition(s_synsent, e_synack, s_estab, a_def));
        tcp.addTransition(new Transition(s_estab, e_datarcvd, s_estab, a_recd));
        tcp.addTransition(new Transition(s_estab, e_datasent, s_estab, a_send));
        tcp.addTransition(new Transition(s_estab, e_close, s_finwait1, a_def));
        tcp.addTransition(new Transition(s_estab, e_fin, s_closewait, a_def));
        tcp.addTransition(new Transition(s_finwait1, e_fin, s_closing, a_def));
        tcp.addTransition(new Transition(s_finwait1, e_ackrcvd, s_finwait2, a_def));
        tcp.addTransition(new Transition(s_finwait2, e_fin, s_timewait, a_def));
        tcp.addTransition(new Transition(s_closewait, e_close, s_lastack, a_def));
        tcp.addTransition(new Transition(s_closing, e_ackrcvd, s_timewait, a_def));
        tcp.addTransition(new Transition(s_lastack, e_ackrcvd, s_closed, a_def));
        tcp.addTransition(new Transition(s_timewait, e_timeout, s_closed, a_def));

        //read input
        String filename = args[0];
        File inp = new File (filename);
        BufferedReader br = new BufferedReader(new FileReader(inp));
        String events;
        while ((events = br.readLine()) != null) {
            String[] events_list;
            events_list = events.split(" ");
            // Print the string
            for (String curr_event : events_list) {
                try {
                    switch (curr_event) {
                        case "PASSIVE":
                            tcp.doEvent(e_passive);
                            break;
                        case "ACTIVE":
                            tcp.doEvent(e_active);
                            break;
                        case "SYN":
                            tcp.doEvent(e_synrcvd);
                            break;
                        case "SYNACK":
                            tcp.doEvent(e_synack);
                            break;
                        case "ACK":
                            tcp.doEvent(e_ackrcvd);
                            break;
                        case "RDATA":
                            if (tcp.currentState() == s_estab) {
                                e_datarcvd.setValue(((Integer) e_datarcvd.getValue()) + 1);
                            }
                            tcp.doEvent(e_datarcvd);
                            break;
                        case "SDATA":
                            if (tcp.currentState() == s_estab) {
                                e_datasent.setValue(((Integer) e_datasent.getValue()) + 1);
                            }
                            tcp.doEvent(e_datasent);
                            break;
                        case "FIN":
                            tcp.doEvent(e_fin);
                            break;
                        case "CLOSE":
                            tcp.doEvent(e_close);
                            break;
                        case "TIMEOUT":
                            tcp.doEvent(e_timeout);
                            break;
                        default:
                            System.out.println("Error: unexpected Event: " + curr_event);
                            break;
                    }
                }
                catch(FsmException e){
                    System.out.println(e.toString());
                }
            }

        }

    }


}