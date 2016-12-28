package extraTask.Broadcast;


import java.util.HashSet;
import java.util.Set;

public class BroadcastReceiver2 implements BroadcastReceiver {
    private static final Set<String> topics = new HashSet<String>();
    public static int receivedCount = 0;

    public BroadcastReceiver2() {
        topics.add("Topic1");
        topics.add("Topic2");
        receivedCount = 0;
    }

    public void receive(Object msg) {
        System.out.println("Receiver2: " + msg.toString());
        receivedCount++;
    }

    public Set<String> getTopics() {
        return topics;
    }
}
