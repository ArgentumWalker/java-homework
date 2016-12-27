package extraTask.Broadcast;

import java.util.HashSet;
import java.util.Set;

/** Receive only one topic */
public class BroadcastReceiver1 implements BroadcastReceiver {
    private static final Set<String> topics = new HashSet<String>();

    public BroadcastReceiver1() {
        topics.add("Topic1");
    }

    public void receive(Object msg) {
        System.out.println("Receiver1: " + msg.toString());
    }

    public Set<String> getTopics() {
        return topics;
    }
}
