package extraTask.Broadcast;

public interface BroadcastSender extends Runnable {

    /**
     * Get topic of messages, that Sender sends.
     * Must be constant
     */
    String getTopic();

    /**
     * Sets a new TopicCoordinator.
     * You should send your messages to this thing.
     * See BroadcastResolver.TopicCoordinator
     */
    void setCoordinator(BroadcastResolver.TopicCoordinator coordinator);
}
