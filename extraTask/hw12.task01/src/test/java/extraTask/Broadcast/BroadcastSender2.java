package extraTask.Broadcast;

public class BroadcastSender2 implements BroadcastSender {
    private BroadcastResolver.TopicCoordinator coordinator = null;

    public String getTopic() {
        return "Topic2";
    }

    public void setCoordinator(BroadcastResolver.TopicCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void run() {
        if (coordinator != null) {
            coordinator.sendMessage("message from Sender2");
        }
    }
}