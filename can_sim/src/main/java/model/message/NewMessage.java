package model.message;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;


public class NewMessage {
    private int destination_address, length;
    private String data;
    private boolean type; //1 means that the received message is a response to a past request

    public NewMessage(int destination_address, int length, String data, boolean type){
        this.destination_address = destination_address;
        this.length = length;
        this.data = data;
        this.type = type;
    }
    //instantiate request to get information
    public NewMessage(int destination_address){
        this.destination_address = destination_address;
        this.type = false;
        this.data = "";
        this.length = -1;
    }

    public boolean getTypeMessage(){
        return this.type;
    }
    //change from receiver to transmitter and reverse
    public void changeType(String data){
        this.type = !this.type;
        this.data = data;
    }

    public int getTargetAddress(){
        return this.destination_address;
    }

    public String getTargetInstruction(){
        return this.data;
    }

    //each message has at most 2 instructions
    //each instruction has 4 characters = 8 bytes
    public List<String> getMessage(int communicationProtocol){

        List<String> instructionList = MessageOp.turnMessageToTokens(this.data, communicationProtocol);

        return instructionList;
    }

    public void testMessageConversion(){
        MessageWord m = MessageOp.programToByte(this);
        NewMessage nm = MessageOp.byteToProgram(m);

        System.out.println("Set to: " + this.destination_address + "\nLength: " + this.length +
                "\nType: " + this.type + "\nData: " + this.data);
    }



    byte[] getData(){
        byte[] byteMessage = data.getBytes(StandardCharsets.UTF_8);
        return byteMessage;
    }

    byte[] getAddress(){
        return ByteBuffer.allocate(4).putInt(this.destination_address).array();
    }
    byte[] getLength(){
        return ByteBuffer.allocate(4).putInt(this.length).array();
    }

    Boolean getType(){
        return this.type;
    }

    public void printData(){
        System.out.println("Set to: " + this.destination_address + "\nLength: " + this.length +
                "\nType: " + this.type + "\nData: " + this.data + "\n");
    }

}
