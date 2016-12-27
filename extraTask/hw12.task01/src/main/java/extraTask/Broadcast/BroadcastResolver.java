package extraTask.Broadcast;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.io.File;

/**
 * Class, that coordinate work of Senders and Receivers
 * Note: add receivers before senders if you don't want to miss some messages
 * Note: don't forget to shutdown Resolver if you don't want to have some everworking useless threads
 * Note: if you use addReceivers or addSenders, make sure, that all classes situated in their packages
 */
public class BroadcastResolver {
    private final HashMap<String, TopicCoordinator> topicCoordinators = new HashMap<String, TopicCoordinator>();
    private final ArrayList<Thread> receiverThreads = new ArrayList<Thread>();
    private final ArrayList<Thread> coordinatorsThreads = new ArrayList<Thread>();

    public BroadcastResolver() {}

    /**
     * Interrupt all inner threads, clear all receivers and senders.
     * You can add new receivers and senders after that.
     */
    public void shutdown() {
        synchronized (receiverThreads) {
            for (Thread t : receiverThreads) {
                t.interrupt();
            }
            receiverThreads.clear();
        }
        synchronized (coordinatorsThreads) {
            for (Thread t : coordinatorsThreads) {
                t.interrupt();
            }
            coordinatorsThreads.clear();
            topicCoordinators.clear();
        }
    }

    /**
     * Add all BroadcastReceivers from directory
     * Note: this method can cause some errors if there are some problems with classes
     */
    public void addReceivers(String directory) {
        try {
            File file = new File(directory);
            URL url = file.toURI().toURL(); //Why I can't just use file.toURL!? Stupid Java
            URL[] urls = new URL[]{url};
            ClassLoader classLoader = new URLClassLoader(urls);
            __addReceivers(file, classLoader, directory);
        }
        catch (MalformedURLException e) {

        }
    }

    /**
     * Make an instance of class and add it to Receivers. Starts new Thread.
     * Note: this doesn't mean, that receiver will be singletone
     */
    public void addReceiver(Class<? extends BroadcastReceiver> cl) throws IllegalAccessException, InstantiationException {
        BroadcastReceiverRunnable runnable = new BroadcastReceiverRunnable(cl.newInstance());
        Thread thread = new Thread(runnable);
        synchronized (receiverThreads) {
            receiverThreads.add(thread);
        }
        synchronized (topicCoordinators) {
            for (String topic : runnable.receiver.getTopics()) {
                TopicCoordinator coordinator = topicCoordinators.get(topic);
                if (coordinator == null) {
                    coordinator = new TopicCoordinator();
                    Thread coordinatorThread = new Thread(coordinator);
                    coordinatorsThreads.add(coordinatorThread);
                    topicCoordinators.put(topic, coordinator);
                    coordinatorThread.start();
                }
                synchronized (coordinator.receivers) {
                    coordinator.receivers.add(runnable);
                }
            }
        }
        thread.start();
    }

    /**
     * Add senders from directory. For more information, see addReceivers.
     */
    public void addSenders(String directory) {
        try {
            File file = new File(directory);
            URL url = file.toURI().toURL(); //Why I can't just use file.toURL!? Stupid Java
            URL[] urls = new URL[]{url};
            ClassLoader classLoader = new URLClassLoader(urls);
            __addSenders(file, classLoader, directory);
        }
        catch (MalformedURLException e) {

        }
    }

    /**
     * The same as addReceiver, but with Sender.
     */
    public void addSender(Class<? extends BroadcastSender> cl) throws IllegalAccessException, InstantiationException {
        BroadcastSender runnable = cl.newInstance();
        Thread thread = new Thread(runnable);
        synchronized (receiverThreads) {
            receiverThreads.add(thread);
        }
        synchronized (topicCoordinators) {
            String topic = runnable.getTopic();
            TopicCoordinator coordinator = topicCoordinators.get(topic);
            if (coordinator == null) {
                coordinator = new TopicCoordinator();
                Thread coordinatorThread = new Thread(coordinator);
                coordinatorsThreads.add(coordinatorThread);
                topicCoordinators.put(topic, coordinator);
                coordinatorThread.start();
            }
            runnable.setCoordinator(coordinator);
        }
        thread.start();
    }

    private void __addSenders(File directory, ClassLoader classLoader, String rootDirectory) {
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        __addSenders(file, classLoader, rootDirectory);
                    } else {
                        try {
                            if (file.getName().lastIndexOf('.') >= 0) {

                                String className = file.getPath().substring(rootDirectory.length(), file.getPath().lastIndexOf('.'));
                                className = className.replaceAll("/", ".");
                                Class<?> cl = classLoader.loadClass(className);
                                addSender((Class<? extends BroadcastSender>) cl);
                            }
                        } catch (ClassCastException e) {
                            //Not a BroadcastReceiver
                        } catch (ClassNotFoundException e) {
                            //Not a class, continue searching
                        } catch (InstantiationException e) {
                            //Not a BroadcastReceiver
                        } catch (IllegalAccessException e) {
                            //Access denied, can't load
                        } catch (NoClassDefFoundError e) {
                            //Something wrong with class file
                        }
                    }
                }
            }
        }
    }

    private void __addReceivers(File directory, ClassLoader classLoader, String rootDirectory) {
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        __addReceivers(file, classLoader, rootDirectory);
                    } else {
                        try {
                            if (file.getName().lastIndexOf('.') >= 0) {

                                String className = file.getPath().substring(rootDirectory.length(), file.getPath().lastIndexOf('.'));
                                className = className.replaceAll("/", ".");
                                Class<?> cl = classLoader.loadClass(className);
                                addReceiver((Class<? extends BroadcastReceiver>) cl);
                            }
                        } catch (ClassCastException e) {
                            //Not a BroadcastReceiver
                        } catch (ClassNotFoundException e) {
                            //Not a class, continue searching
                        } catch (InstantiationException e) {
                            //Not a BroadcastReceiver
                        } catch (IllegalAccessException e) {
                            //Access denied, can't load
                        }
                    }
                }
            }
        }
    }

    /**
     * Coordinator of one fixed topic.
     */
    public class TopicCoordinator implements Runnable {
        private final LinkedList<Object> messages = new LinkedList<Object>();
        private final List<BroadcastReceiverRunnable> receivers = new LinkedList<BroadcastReceiverRunnable>();

        private TopicCoordinator () {
        }

        /**
         * Send message to Receivers
         */
        public void sendMessage(Object msg) {
            synchronized (messages) {
                messages.addLast(msg);
                messages.notify();
            }
        }

        /**
         * Send messages to receivers. Creating new thread don't make sense.
         */
        public void run() {
            boolean interrupted = false;
            while (!interrupted) {
                Object message = null;
                try {
                    synchronized (messages) {
                        while (messages.isEmpty()) {
                            messages.wait();
                        }
                        message = messages.removeFirst();
                    }
                    synchronized (receivers) {
                        for (BroadcastReceiverRunnable receiver : receivers) {
                            receiver.receive(message);
                        }
                    }
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        }
    }

    private class BroadcastReceiverRunnable implements Runnable {
        private final LinkedList<Object> messages = new LinkedList<Object>();
        private BroadcastReceiver receiver;

        BroadcastReceiverRunnable (BroadcastReceiver receiver) {
            this.receiver = receiver;
        }

        void receive(Object msg) {
            synchronized (messages) {
                messages.addLast(msg);
                messages.notify();
            }
        }

        public void run() {
            boolean interrupted = false;
            while (!interrupted) {
                Object message = null;
                try {
                    synchronized (messages) {
                        while (messages.isEmpty()) {
                            messages.wait();
                        }
                        message = messages.removeFirst();
                    }
                    receiver.receive(message);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        }
    }
}
