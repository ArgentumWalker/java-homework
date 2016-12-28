package extraTask.Broadcast;

public class BroadcastSender1 implements BroadcastSender {
    private BroadcastResolver.TopicCoordinator coordinator = null;

    public String getTopic() {
        return "Topic1";
    }

    public void setCoordinator(BroadcastResolver.TopicCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void run() {
        if (coordinator != null) {
            coordinator.sendMessage("message from Sender1");
        }
    }
}
