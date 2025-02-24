package model.message;

import java.util.ArrayList;

public class InternalRequest {
    private Integer address;
    private ArrayList<Instructions> instruction;
    private boolean response; // 1 when the message is a response/ 0 when is a request

    public InternalRequest(Integer address, ArrayList<Instructions> instruction, boolean response){
        this.instruction = instruction;
        this.address = address;
        this.response = response;
    }

    public boolean isResponse(){
        return response;
    }

    public Integer getTargetAddress(){
        return this.address;
    }

    public ArrayList<Instructions> getCurrentInstruction(){
        return this.instruction;
    }



}
