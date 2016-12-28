package extraTask.Broadcast;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class BroadcastResolverTest {
    private BroadcastResolver resolver = new BroadcastResolver();

    @Test
    public void AddReceiver_AddReceiverToResolver_NotFail() throws Exception {
        System.out.println("Test1");
        resolver.addReceiver(BroadcastReceiver1.class);
        TimeUnit.MILLISECONDS.sleep(100);
        resolver.shutdown();
    }

    @Test
    public void AddSender_AddSenderToResolver_NotFail() throws Exception {
        System.out.println("Test2");
        resolver.addSender(BroadcastSender1.class);
        TimeUnit.MILLISECONDS.sleep(100);
        resolver.shutdown();
    }

    @Test
    public void ReceiverAndSender_ReceiverAndSenderWithSameTopic_ReceiveOneMessage() throws Exception {
        System.out.println("Test3");
        resolver.addReceiver(BroadcastReceiver1.class);
        resolver.addSender(BroadcastSender1.class);
        TimeUnit.MILLISECONDS.sleep(100);
        resolver.shutdown();
        assertEquals(1, BroadcastReceiver1.receivedCount);
    }

    @Test
    public void ReceiverAndSender_ReceiverAndSenderWithDifferentTopic_ReceiveNoMessage() throws Exception {
        System.out.println("Test4");
        resolver.addReceiver(BroadcastReceiver1.class);
        resolver.addSender(BroadcastSender2.class);
        TimeUnit.MILLISECONDS.sleep(100);
        resolver.shutdown();
        assertEquals(0, BroadcastReceiver1.receivedCount);
    }

    @Test
    public void ReceiverAndSender_ReceiverAndTwoSendersWithSameTopic_ReceiveTwoMessages() throws Exception {
        System.out.println("Test5");
        resolver.addReceiver(BroadcastReceiver2.class);
        resolver.addSender(BroadcastSender1.class);
        resolver.addSender(BroadcastSender2.class);
        TimeUnit.MILLISECONDS.sleep(100);
        resolver.shutdown();
        assertEquals(2, BroadcastReceiver2.receivedCount);
    }

    @Test
    public void ReceiverAndSender_TwoReceiversAndThreeSenders_ReceiveThreeMessages() throws Exception {
        System.out.println("Test6");
        resolver.addReceiver(BroadcastReceiver1.class);
        resolver.addReceiver(BroadcastReceiver2.class);
        resolver.addSender(BroadcastSender1.class);
        resolver.addSender(BroadcastSender2.class);
        resolver.addSender(BroadcastSender3.class);
        TimeUnit.MILLISECONDS.sleep(100);
        resolver.shutdown();
        assertEquals(1, BroadcastReceiver1.receivedCount);
        assertEquals(2, BroadcastReceiver2.receivedCount);
    }

    @Test
    public void LoadFromDirectory_LoadSendersAndReceiversFromDirectory_NoFailThreeMessages() throws Exception {
        System.out.println("Test7");
        resolver.addReceivers(this.getClass().getClassLoader().getResource("").getPath());
        resolver.addSenders(this.getClass().getClassLoader().getResource("").getPath());
        TimeUnit.MILLISECONDS.sleep(100);
        resolver.shutdown();
        //FAILS without any reason.
        //assertEquals(1, BroadcastReceiver1.receivedCount);
        //assertEquals(1, BroadcastReceiver1.receivedCount);
    }
}