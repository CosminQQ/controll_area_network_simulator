package model.message;

import javafx.scene.control.ListView;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static model.message.MessageOp.getIntAddress;

public class CanBuss {
    private PriorityQueue<MessageWord> canBus;
    private Lock accessTocken;

    private ListView<Integer> liveUpdate;

    public void setL(ListView<Integer> listView){
        liveUpdate = listView;
    }


    public CanBuss() {
        this.canBus = new PriorityQueue<>();
        this.accessTocken = new ReentrantLock();
    }

    public synchronized void sendRequest(MessageWord newMessage) {
        accessTocken.lock();
        canBus.add(newMessage);
        accessTocken.unlock();
    }

    public boolean isEmpty() {
        return this.canBus.isEmpty();
    }

    public synchronized MessageWord pullRequest(byte[] id) {

        MessageWord peekMessage = null;
        if (!canBus.isEmpty()) {
            accessTocken.lock();

            try {
                peekMessage = canBus.peek();
                if (peekMessage != null)
                    if (peekMessage.compareTo(new MessageWord(id)) == 0) {
                            liveUpdate.getItems().add(peekMessage.getAddress());
                            peekMessage = canBus.poll();
                    }else peekMessage = null;
            } catch (NullPointerException e) {
                System.out.println("Error file CanBus/pullRequest/getPeek/id=" + id);
            } finally {
                accessTocken.unlock();
            }
        }


        return peekMessage;
    }
}
