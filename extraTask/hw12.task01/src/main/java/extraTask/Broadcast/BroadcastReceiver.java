package extraTask.Broadcast;

import java.util.Set;

public interface BroadcastReceiver {

    /**
     * Action, when receiver receive new message
     */
    void receive(Object msg);

    /**
     * Get all topics of messages, that receiver can receive.
     * Must be constant
     */
    Set<String> getTopics();
}
