package model.message;

import javafx.scene.control.ListView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MessageOp {
    public static List<String> turnMessageToTokens(String message, int tokenSize){
        List<String> tokens = new ArrayList<>();

        // tokenize the string based on the "baud rate" chosen
        for(int i = 0; i< message.length(); i+=tokenSize){
            tokens.add(message.substring(i, Math.min(i + tokenSize, message.length())));
        }

        return tokens;
    }

    public static MessageWord programToByte(NewMessage message){
        byte[] byteMessage = message.getData();
        byte[] byteAddress = message.getAddress();
        byte[] byteLength = message.getLength();
        Boolean byteType = message.getType();

        return new MessageWord(byteAddress,byteLength,byteMessage,byteType);
    }

    public static NewMessage byteToProgram(MessageWord data){
        int programAddress = data.getAddress();
        int programLength = data.getLength();
        String programInstruction = data.getData();
        boolean programType = data.getType();

        return new NewMessage(programAddress, programLength, programInstruction, programType);
    }
    public static void testMessageTransmission(MessageWord request){
        NewMessage nm = MessageOp.byteToProgram(request);
        nm.printData();
    }
    public static int getIntAddress(byte[] id){
        return (id[0] & 0xFF) << 24 | (id[1] & 0xFF) << 16 | (id[2] & 0xFF) << 8 | (id[3] & 0xFF);
    }

    public static byte[] getByteAddress(Integer id){
        return ByteBuffer.allocate(4).putInt(id).array();
    }

    public static ArrayList<Instructions> parseData(String data, char delimiter){

        String[] tokens = data.split(String.valueOf(delimiter));
        ArrayList<Instructions> decodedInstructions = new ArrayList<>();
        for(String token : tokens){
            switch (token) {
                case "STATUS":
                    decodedInstructions.add(Instructions.STATUS);
                    break;
                case "NO_OP":
                    decodedInstructions.add(Instructions.NO_OP);
                    break;
                case "STOP":
                    decodedInstructions.add(Instructions.STOP);
                    break;
                case "GAS_LEVEL_LOW":
                    decodedInstructions.add(Instructions.GAS_LEVEL_LOW);
                    break;
                case "GAS_LEVEL_HIGH":
                    decodedInstructions.add(Instructions.GAS_LEVEL_HIGH);
                    break;
                case "GAS_LEVEL_MEDIUM":
                    decodedInstructions.add(Instructions.GAS_LEVEL_MEDIUM);
                    break;
                case "MOTOR_COLD":
                    decodedInstructions.add(Instructions.MOTOR_COLD);
                    break;
                case "MOTOR_OVERHEATED":
                    decodedInstructions.add(Instructions.MOTOR_OVERHEATED);
                    break;
                case "MOTOR_IN_PARAMETERS":
                    decodedInstructions.add(Instructions.MOTOR_IN_PARAMETERS);
                    break;
                case "FUMES_LEVEL_GENOCIDE":
                    decodedInstructions.add(Instructions.FUMES_LEVEL_GENOCIDE);
                    break;
                case "FUMES_LEVEL_HIGH":
                    decodedInstructions.add(Instructions.FUMES_LEVEL_HIGH);
                    break;
                case "FUMES_LEVEL_MEDIUM":
                    decodedInstructions.add(Instructions.FUMES_LEVEL_MEDIUM);
                    break;
                case "FUMES_LEVEL_LOW":
                    decodedInstructions.add(Instructions.FUMES_LEVEL_LOW);
                    break;
                case "FUMES_LEVEL_EURO_9":
                    decodedInstructions.add(Instructions.FUMES_LEVEL_EURO_9);
                    break;
                default:
                    System.out.println("Decoding unknown instruction: " + token);
                    break;
            }
        }
        return decodedInstructions;
    }

    public static String encodeData(ArrayList<Instructions> data, char delimiter){
        StringBuilder builder = new StringBuilder();

        for(Instructions instruction : data){
            switch (instruction) {
                case STATUS:
                    builder.append("STATUS");
                    break;
                case NO_OP:
                    builder.append("NO_OP");
                    break;
                case STOP:
                    builder.append("STOP");
                    break;
                case GAS_LEVEL_LOW:
                    builder.append("GAS_LEVEL_LOW");
                    break;
                case GAS_LEVEL_HIGH:
                    builder.append("GAS_LEVEL_HIGH");
                    break;
                case GAS_LEVEL_MEDIUM:
                    builder.append("GAS_LEVEL_MEDIUM");
                    break;
                case MOTOR_COLD:
                    builder.append("MOTOR_COLD");
                    break;
                case MOTOR_OVERHEATED:
                    builder.append("MOTOR_OVERHEATED");
                    break;
                case MOTOR_IN_PARAMETERS:
                    builder.append("MOTOR_IN_PARAMETERS");
                    break;
                case FUMES_LEVEL_GENOCIDE:
                    builder.append("FUMES_LEVEL_GENOCIDE");
                    break;
                case FUMES_LEVEL_HIGH:
                    builder.append("FUMES_LEVEL_HIGH");
                    break;
                case FUMES_LEVEL_MEDIUM:
                    builder.append("FUMES_LEVEL_MEDIUM");
                    break;
                case FUMES_LEVEL_LOW:
                    builder.append("FUMES_LEVEL_LOW");
                    break;
                case FUMES_LEVEL_EURO_9:
                    builder.append("FUMES_LEVEL_EURO_9");
                    break;
                default:
                    // Handle unknown or unexpected instructions
                    System.out.println("Unknown instruction: " + instruction);
                    break;
            }
            builder.append(delimiter);
        }
        return builder.toString();
    }

    public static void printInternalRequestDetails(InternalRequest request) {
        System.out.println("address: " + request.getTargetAddress());
        System.out.println("response: " + (request.isResponse() ? "1" : "0"));

        // Print instructions
        System.out.print("instructions: ");
        ArrayList<Instructions> instructions = request.getCurrentInstruction();
        if (instructions != null && !instructions.isEmpty()) {
            for (Instructions instruction : instructions) {
                System.out.print(instruction + " ");
            }
        } else {
            System.out.print("No instructions");
        }
        System.out.println("\n");
    }

    public static void writeListToFile(ArrayList<String> list, String fileName) {
        File file = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String line : list) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Data successfully written to the file: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + fileName);
            e.printStackTrace();
        }
    }

    public static void updateInterfaceWithValueS(PriorityQueue<MessageWord> canbus){
        //make sure canbus is not empty
//        MessageWord m = canbus.peek();
//        NewMessage n = byteToProgram(m);
//        CreateNode node = new CreateNode()
    }
}
