package model.message;

import static model.message.MessageOp.getIntAddress;

public class MessageWord implements Comparable<MessageWord> {
    byte[] id, length, data;
    boolean request;

    MessageWord(byte[] id, byte[] length, byte[] data, boolean request) {
        this.id = id;
        this.length = length;
        this.data = data;
        this.request = request;
    }

    public MessageWord(byte[] id){
        this.id = id;
    }

    boolean compareId(byte[] locationId) {
        if (this.id == null || locationId == null)
            return false;
        if (this.id == locationId)
            return true;
        return false;

    }

    int getAddress() {
        return (id[0] & 0xFF) << 24 | (id[1] & 0xFF) << 16 | (id[2] & 0xFF) << 8 | (id[3] & 0xFF);
    }

    int getLength() {
        return (length[0] & 0xFF) << 24 | (length[1] & 0xFF) << 16 | (length[2] & 0xFF) << 8 | (length[3] & 0xFF);
    }

    Boolean getType() {
        return request;
    }

    String getData() {
        return new String(data);
    }

    @Override
    public int compareTo(MessageWord other) {
        int val1 = getIntAddress(this.id);
        int val2 = getIntAddress(other.id);

        return Integer.compare(val1, val2);
    }
}
