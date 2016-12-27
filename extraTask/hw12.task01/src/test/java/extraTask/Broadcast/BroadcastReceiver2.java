package extraTask.Broadcast;


import java.util.HashSet;
import java.util.Set;

public class BroadcastReceiver2 implements BroadcastReceiver {
    private static final Set<String> topics = new HashSet<String>();

    public BroadcastReceiver2() {
        topics.add("Topic1");
        topics.add("Topic2");
    }

    public void receive(Object msg) {
        System.out.println("Receiver2: " + msg.toString());
    }

    public Set<String> getTopics() {
        return topics;
    }
}
