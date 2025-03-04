Controller Area Network Simulator
CAN

Introduction:
Context:
Today’s vehicles can contain more than 70 devices, commonly called Electronic Control Units (ECUs) or nodes, that control electrical subsystems. These subsystems control the engine, transmission, power-train and antilock braking/ABS along with many more. Communication between these subsystems is critical to ensure the reliability and safety demanded in the automotive market. The Controller Area Network (CAN bus) is a message-based communication network standard that allows ECUs to communicate within a vehicle without the use of dedicated analog signal wires. By utilizing multiplex wiring, CAN significantly reduces cost, complexity and weight of a design and allows new features to be implemented in firmware, allowing for over-the-air firmware updates and saving further on engineering design time.

Objectives:
The simulator will display a field in which the user is able to insert data that characterizes a node. He will be able to insert as many nodes as it wants, choose a simulation time span and after that see the messages on the CAN Buss in real time. For analysis of the communication protocol the user can access logs from each node that record the state of the node during the simulation.
Bibliographic Research:
How does CAN protocol work:
A CAN (Controller Area Network) operates by assigning each node in the network a priority code, where nodes with the highest priority can transmit information over the bus without delay, while lower-priority nodes must wait. This priority is determined through a bit-wise comparison, where a "0" takes precedence over a "1." All nodes in a CAN are interconnected via a 2-wire bus, and each node requires a microcontroller, a CAN controller, and a CAN transceiver. The transceiver is the component directly attached to the bus, enabling devices to communicate with one another. The CAN protocol is message-based, allowing all nodes on the bus to transmit, receive, and continuously listen for broadcast messages. Additionally, the protocol includes five methods of error checking—three at the message level and two at the bit level—to ensure reliable communication.
 
Analysis:
Project Proposal:
The final simulator will have its features implemented using Java 21 and will have the Graphical User Interface written using the JavaFX API for user integration. It will encompass the following capabilities:
1.	4 Different types of CAN nodes.
2.	6 Different priorities.
3.	User defined input parameters for the nodes.
4.	User defined simulation running time.
5.	User defined sensor data.
6.	Real time update of the message on the bus.
7.	Output text files for node analysis after the simulation.
Implementation method in hardware models:
	Communication protocols: the language “spoken” on the CAN bus that is understood by the nodes. On one can bus the communication protocol can be different as not every node needs to decode the information transmitted. The protocols differ through transit speed, signal voltage and type of wiring.

1. Transit Speed
•	Classical CAN (CAN 2.0): The traditional CAN protocol supports speeds up to 1 Mbps. This speed is suitable for most automotive applications where data transmission rates are moderate.
•	CAN FD (Flexible Data-rate): An extended version of CAN, CAN FD, can reach speeds up to 5 Mbps during the data phase, while the arbitration phase remains compatible with Classical CAN. This higher speed supports faster communication for data-heavy applications.
•	CAN XL: The newest generation, CAN XL, is designed to support even higher speeds of up to 10 Mbps for more advanced applications that require faster data transfer.
 


2. Signal Voltage
•	High-Speed CAN: Operates with differential signaling, typically with voltages in the range of 2-3V for each wire (CAN_H and CAN_L) during data transmission. High-Speed CAN is used in standard automotive networks due to its robustness in noisy environments.
 
•	Low-Speed/Fault-Tolerant CAN: Used in safety-critical applications, Low-Speed CAN (up to 125 kbps) can still function even if one wire in the pair fails. It operates at a lower voltage level than high-speed CAN and often has built-in fault tolerance mechanisms.
 
•	Single-Wire CAN (SWC): Operates on a single wire with signal voltages around 5V, used in simpler automotive applications where two wires are not feasible. It is slower and has lower fault tolerance but is useful for low-data applications.
 

3. Type of Wiring
•	Twisted Pair (High-Speed CAN): High-speed CAN networks use a twisted pair of wires (CAN_H and CAN_L) to reduce electromagnetic interference (EMI) by creating a differential signal. This wiring is suitable for noisy environments, such as automotive or industrial settings.
•	Single Wire (Low-Speed and SWC): Low-Speed and Single-Wire CAN protocols use a single wire or simple non-twisted wiring in low-cost applications where EMI is less of a concern. Single-Wire CAN is typically used for simpler control tasks, such as seat control modules in vehicles.
•	Shielded Twisted Pair: In some high-noise applications, CAN wiring may include shielding to further protect the signals from interference.


 
Architecture of a CAN node in Hardware Models:
 Each node requires a:

•	Central Processing Unit or Microprocessor: 
The host processor decides what the received messages mean and what messages it wants to transmit. Sensors, actuators and control devices can be connected to the host processor.

•	CAN controller: often an integral part of the microcontroller
Receiving: the CAN controller stores the received serial bits from the bus until an entire message is available, which can then be fetched by the host processor.
Sending: the host processor sends the transmit message(s) to a CAN controller, which transmits the bits serially onto the bus when the bus is free.

•	Transceiver: 
Receiving: it converts the data stream from CAN bus levels to levels that the CAN controller uses. It usually has a protective circuitry to protect the CAN controller.
Transmitting: it converts the data stream from the CAN controller to CAN bus levels.
 

Data Transmission:
CAN data transmission uses a lossless bitwise arbitration method of conflict resolution. This arbitration method requires all nodes on the CAN network to be synchronized to sample every bit on the CAN network at the same time. This is why some call CAN synchronous. Unfortunately, the term synchronous is imprecise since the data is transmitted in an asynchronous format, namely without a clock signal.

The CAN specifications use the terms dominant bits and recessive bits, where dominant is a logical 0  and recessive is a logical 1. The idle state is represented by the recessive level (Logical 1) and the Off state is represented by 0V. If one node transmits a dominant bit and another node transmits a recessive bit then there is a collision and the dominant bit wins. This means there is no delay to the higher-priority message.

The exact voltages for a logical 0 or 1 depend on the physical layer used, but the basic principle of CAN requires that each node listens to the data on the CAN network including the transmitting node itself. If a logical 1 is transmitted by all transmitting nodes at the same time, then a logical 1 is seen by all of the nodes, including both the transmitting node and receiving node. If a logical 0 is transmitted by all transmitting node(s) at the same time, then a logical 0 is seen by all nodes. If a logical 0 is being transmitted by one or more nodes, and a logical 1 is being transmitted by one or more nodes, then a logical 0 is seen by all nodes including the node transmitting the logical 1. A node that loses arbitration requeues its message for later transmission and the CAN frame bit-stream continues without error until only one node is left transmitting. 

For example, consider an 11-bit ID CAN network, with two nodes with IDs of 15 (binary representation, 00000001111) and 16 (binary representation, 00000010000). If these two nodes transmit at the same time, each will first transmit the start bit then transmit the first six zeros of their ID with no arbitration decision being made.

Data frame: The data frame is the only frame for actual data transmission. There are two message formats: 
•	Base frame format: with 11 identifier bits
•	Extended frame format: with 29 identifier bits

The CAN standard requires that the implementation must accept the base frame format and may accept the extended frame format, but must tolerate the extended frame format.
To ensure enough transitions to maintain synchronization, a bit of opposite polarity is inserted after five consecutive bits of the same polarity. This practice is called bit stuffing, and is necessary due to the non-return-to-zero (NRZ) coding used with CAN. The stuffed data frames are destuffed by the receiver. An undesirable side effect of the bit stuffing scheme is that a small number of bit errors in a received message may corrupt the destuffing process, causing a larger number of errors to propagate through the destuffed message. This reduces the level of protection that would otherwise be offered by the CRC against the original errors. This deficiency of the protocol has been addressed in CAN FD frames using a combination of fixed stuff bits and a counter that records the number of stuff bits inserted.

 
 
 
Design

Implementation of CAN communication protocol in a software simulator:

Overview: For the implementation, I have opted for the Low-Speed/Fault-Tolerant CAN as it implies a high latency but, a better representation of the principles and protocols modern CAN system use. Therefore:
TARGET SPEED: 125Kbps (125 000 bits per second)
MESSAGE LENGTH: 112 bits using the base frame format
TRANSMISSION DURATION: 0.896 ms

This transmission duration is extremely, even impossible, to reach due to OS limitation on my Windows 10 machine. But, the purpose of this project is to implement a simulation environment for an automotive CAN network, therefore we will put aside the transmission duration in order to emphasize on the characteristics of CAN architecture.

High Level Diagram of a CAN node connected to a CAN Buss.
 
 
Diagram implementation:
1.	CAN Node: the high level abstraction that sends and receives messages via the CAN bus. It is implemented in 3 parts:
a. Microprocessor: 
•	receives data from sensors, actuators and stores it
•	detects anomalies in data received from sensors and actuators and transmits specific error codes in the network using the controller
•	receives instruction to execute from other nodes in the network, that have been decoded by the transciever and controller.
b. Controller:
•	receives the full message from the transceiver and decodes it in a specific instruction set fed to the microprocessor
•	receives the error message from the microprocessor and encodes it in a stream of bits that is passed to the transcever
c. Transceiver:
•	when sending a message it further encodes the message received from controller and sends it
•	waits a specific ammount of time for the message to propagate through the network to validate wheather it had priority, sending it every 1 second until the priority is won
•	enters idle state, in which it is waiting for responses form the network, when it has no message to transmit
•	when receiving a message it first checks priority and after that performs the first decoding process

2.	CAN Buss: to implement a protocol through the network we need a specific data structure that updates the message with the highest priority, to all of its reference holders. The stucture must accommodate the 2 wire buss from the hardware implementation and also allow read/writes to its contents at variable baud rates, preserving though its data atomicity. To easily implement it we might have to compare a few available data structures such as heap, balanced binary tree or lists and we have to implement the one write the other read operations on them.
 
3.	Message encription: Due to the very large message format of the hardware implementation of CAN communication protocols, I have choosen to not implement everything regarding error correction as it is a strict hardware-related problem that do not have a large impact on the functionality of the system and its purposes are more of a safety concern, rather than a dealbreaker as the message will be transmitted regulary in the network until a node sends a response. Therefore the following bits will be dismissed from the short data format:
•	Start of frame
•	Stuff bit
•	Identifier extension bit
•	Reserved bit
•	CRC bits
•	ACK delimiter
•	End of frame
•	Inter frame spacing

Our remaing fields will be represented as a booleans in the simulator:
•	Identifier: 32 bits - priority
•	RTR: 1 bit 
•	DLC: 32 bits – data length
•	Data: 64 bits – data content
•	ALK Slot: 1 bit – receiver 0 / transmiter 1


 
Project Structure
I will structure the project using MVC. In Model I will include all the classes that represent data used by my program. In view I will include classes that implement the GUI of the project and in controller I will structure classes that iplement functionalities of my project.
Package management:
 

The View package implements the FX code that will represent the User Interface of the project.
Controller package implements special Java FX methods for animation and interaction with user inputs.
CAN Node package implement the core functionality of the project as communication protocols, sensor data processing and inter process communication.
Model package implements database connections which store sensor data and special data structures as a Priority Queue, Sensor Data, High Level Node Data and Can Bus Communication data.
 
Can Node Class Diagram
 
  This diagram represents the internal structure of a CAN node that has its functionality split among: TRANSCEIVER, CONTROLLER and MICROPROCESSOR. 
•	TRANSCEIVER: works on a separate thread that actively checks for the message with the highest priority on the CAN bus.
•	CONTROLLER: decodes and encodes messages that are received/transmitted
•	MICROPROCESSOR: reads the sensor data and responds to instructions sent via de CAN bus
•	CAN BUS: a priority queue that implements a min heap. It allows only 2 operation send and pull requests
•	MESSAGE WORLD: defines the structure the data is transmitted via the CAN Bus – the communication protocol
•	NEW MESSAGE: defines the data that is understood and can be executed by the microprocessor
•	MESSAGE OP: defines static methods that operate and transforms data in both forms

INSTRUCTION ENCODING:
•	STATUS: returns the data from the last status check
•	STOP: closes the connection to the CAN network, the node becomes idle until it receives other instruction
•	PARAMETER FIRST/SECOND/THIRT: implements special operations every node can execute and returns the output.
 
Implementation:
Model Package Classes:
The CanBuss class implements a synchronized min-heap for managing message exchanges in a CAN-based message world. Its core functionality revolves around a PriorityQueue, which acts as a min-heap to prioritize messages based on their natural ordering or a comparator (e.g., MessageWord priority). The class ensures thread-safe operations through the use of a ReentrantLock (accessTocken), which provides explicit locking for concurrent access to the queue.
Key features:
•	PriorityQueue (canBus): Acts as a synchronized message container, storing MessageWord objects and ensuring retrieval of the message with the highest priority (lowest value) using the queue's peek and poll methods.
•	AccessTocken (ReentrantLock): Guarantees controlled access to the queue, ensuring synchronization when adding or removing messages, even in multi-threaded environments.
•	Live Updates (ListView): Provides live feedback by appending message-related updates (e.g., id as an integer) to a graphical ListView, improving visualization of operations.
•	Thread Safety: Both the sendRequest and pullRequest methods are synchronized and employ locking mechanisms to prevent race conditions and maintain queue integrity.
The pullRequest method further incorporates a conditional check to match the message ID and simulates a brief delay (Thread.sleep) for illustrative or operational purposes. This design makes the class robust for managing synchronized message exchanges in applications where prioritization and safe multi-threaded access are crucial.

The MessageWord class represents a message structure in a CAN-based communication system and implements the Comparable interface to enable priority-based operations. This design is tailored for use in priority queues like the one in the CanBuss class.
Key Features:
1.	Message Structure:
o	id: A byte array that uniquely identifies the message.
o	length: A byte array indicating the length of the message data.
o	data: A byte array containing the actual message payload.
o	request: A boolean flag denoting whether the message is a request (true) or not (false).
2.	ID Comparison (compareId):
o	Compares the message ID with a given byte array (locationId), ensuring equality by checking each byte individually. This method is crucial for matching specific messages.
3.	Address Conversion (getAddress):
o	Converts the id byte array into a 32-bit integer representation, enabling efficient operations like sorting or comparisons in the system.
4.	Length and Type Accessors:
o	getLength(): Converts the length byte array into an integer.
o	getType(): Returns the request flag, indicating whether the message is a request or not.
5.	Data Representation (getData):
o	Decodes the data byte array into a human-readable string, facilitating debugging or logging of message contents.
6.	Priority Handling (compareTo):
o	Implements the Comparable interface to compare MessageWord instances based on their address (id). The compareTo method ensures messages can be ordered in a min-heap (e.g., in PriorityQueue) by their integer address.
Purpose:
The MessageWord class is optimized for use in priority-based message handling systems like CAN bus simulations. Its design allows:
•	Efficient message matching using compareId.
•	Address-based prioritization through compareTo, ensuring that messages with lower addresses are processed first.
•	Simplified data extraction and type identification for higher-level logic.
By encapsulating all message-related operations, MessageWord serves as the backbone of the CAN-based message system, supporting robust communication and prioritization.

The NewMessage class represents a higher-level abstraction of messages used for communication in a system, providing a simplified interface for message creation, manipulation, and conversion. This class is particularly suitable for scenarios involving dynamic message handling and encoding/decoding operations.
Key Features:
1. Message Components:
•	destination_address: Integer that specifies the target address for the message.
•	length: Integer indicating the length of the message's data payload.
•	data: A string containing the message's actual instructions or information.
•	type: A boolean flag denoting whether the message is a request (false) or a response (true).
2. Constructors:
•	Request Initialization:
o	The simplified constructor (NewMessage(int destination_address)) creates a new request message without data, setting type to false and leaving length as -1.
•	Complete Initialization:
o	The full constructor initializes all fields, allowing the creation of a fully defined message, including its data and type.
3. Utility Methods:
•	getTypeMessage:
o	Returns the current type of the message (true for response, false for request).
•	changeType:
o	Toggles the message type (request ↔ response) and updates its data content.
•	getTargetAddress:
o	Retrieves the destination address of the message.
•	getTargetInstruction:
o	Returns the data string associated with the message.

4. Protocol Handling:
•	getMessage(int communicationProtocol):
o	Splits the data string into smaller tokens or instructions, following the specified communicationProtocol. This ensures that data can be transmitted in protocol-compliant chunks.
•	testMessageConversion:
o	Converts the message into a MessageWord object and back into a NewMessage, verifying the consistency of the encoding/decoding process.

5. Byte-Level Conversions:
•	getData:
o	Converts the data string into a byte array using UTF-8 encoding.
•	getAddress:
o	Converts the destination_address into a 4-byte array.
•	getLength:
o	Converts the length field into a 4-byte array.
•	getType:
o	Returns the boolean type of the message for low-level use.

Purpose:
The NewMessage class serves as a user-friendly interface for creating and managing messages in a communication system. It emphasizes:
•	Ease of Use: Simplified constructors and intuitive methods allow for dynamic message generation.
•	Protocol Compliance: Provides tools to format and split messages for transmission according to protocol requirements.
•	Flexibility: Supports both request and response messages, with mechanisms to toggle and modify message types.
•	Integration: Can seamlessly interact with low-level structures like MessageWord through encoding and decoding operations.
This class is particularly useful in systems where communication involves varying data lengths, types, and protocols, ensuring robust and efficient message handling.

The InternalRequest class models a structured internal communication request or response in a system, encapsulating the details of the target address, a set of instructions, and the nature of the message (request or response). It provides a lightweight abstraction to manage internal communication workflows effectively.
Key Features:
1. Core Attributes:
•	address:
o	Represents the target address (as an Integer) for the request or response.
•	instruction:
o	A list (ArrayList) of Instructions, defining the operations or commands to be executed.
•	response:
o	A boolean flag indicating the type of the message:
	true: The message is a response to a previous request.
	false: The message is a request for action or information.
	
3. Utility Methods:
•	isResponse:
o	Returns true if the message is a response and false if it is a request.
•	getTargetAddress:
o	Retrieves the target address associated with the request or response.
•	getCurrentInstruction:
o	Returns the list of instructions (ArrayList<Instructions>) encapsulated in the message.
Purpose:
The InternalRequest class is designed to facilitate structured internal communication by combining:
•	Address Targeting:
o	Ensures each request or response is associated with a specific address in the system.
•	Instruction Encapsulation:
o	Bundles a set of instructions (Instructions) for precise action delegation.
•	Message Differentiation:
o	Provides a simple mechanism to distinguish between requests and responses via the response flag.

Use Case:
This class can be effectively utilized in:
•	Inter-Component Communication:
o	Passing instructions between modules, where each instruction corresponds to a specific operation.
•	Request-Response Handling:
o	Managing the lifecycle of a request-response workflow in a system.
•	Error Handling and Debugging:
o	Analyzing the target address and instruction payload for debugging or logging purposes.

 
The MessageOp class serves as a utility hub for managing various operations related to message processing, encoding, decoding, and handling communication within a CAN-based or similar system. It acts as the backbone for data manipulation, ensuring the seamless conversion between different message formats and protocols.

Key Features:
1. Message Tokenization and Parsing:
•	turnMessageToTokens:
o	Splits a message into smaller tokens based on a specified size, simulating fragmentation for communication protocols.
•	parseData:
o	Decodes a delimited string into a list of Instructions by mapping predefined keywords to corresponding enums.
•	encodeData:
o	Converts a list of Instructions into a delimited string for transmission or storage.

2. Message Format Conversion:
•	programToByte:
o	Converts a NewMessage instance into a byte-oriented MessageWord, facilitating low-level communication.
•	byteToProgram:
o	Reconstructs a NewMessage from a MessageWord, enabling the interpretation of received byte data.

3. CAN Address and Data Handling:
•	getIntAddress:
o	Decodes a 4-byte array into an integer representation of a CAN address.
•	getByteAddress:
o	Encodes an integer into a 4-byte array suitable for CAN communication.

4. Instruction and Request Management:
•	printInternalRequestDetails:
o	Logs detailed information about an InternalRequest, including its address, type, and associated instructions.
•	updateInterfaceWithValueS:
o	Placeholder method intended for updating user interfaces based on the current state of the CAN bus.

5. File Handling:
•	writeListToFile:
o	Writes a list of strings to a specified file, supporting debugging or logging of processed data.

Utility and Purpose:
The MessageOp class is pivotal for:
1.	Message Interchange:
o	Converts between high-level messages (NewMessage) and low-level byte representations (MessageWord) for effective communication.
2.	Protocol Compliance:
o	Supports encoding/decoding operations to ensure messages adhere to the defined communication protocol.
3.	Instruction Management:
o	Handles encoding and parsing of instructions, ensuring clear and unambiguous interpretation.
4.	Debugging and Logging:
o	Provides tools to print or store message and request details for analysis.
Use Cases:
•	CAN-Based Systems:
o	Enables structured message handling for nodes communicating over a CAN bus or similar network.
•	Protocol-Driven Applications:
o	Assists in message segmentation, reconstruction, and protocol adherence.
•	Instruction-Driven Workflows:
o	Supports command and control systems that rely on predefined instruction sets.
 
Controller Package:
The Transceiver class serves as a pivotal component in a simulated CAN (Controller Area Network) system. It acts as a bridge between a CanBuss instance and a blocking queue, handling the sending and receiving of messages on behalf of a specific node in the network. The class is designed to operate in a multi-threaded environment, ensuring synchronized message transmission and reception while respecting node-specific constraints.

Key Features:
1. Node-Specific Addressing:
•	Each transceiver is assigned a unique id (byte array) representing its address on the CAN bus.
•	The id ensures that the transceiver only processes messages specifically targeted to its address.
2. Message Transmission:
•	sendMessage:
o	Sends a message by adding it to the CanBuss priority queue.
o	Validates the message before transmission, ensuring no null messages are sent.
3. Message Reception:
•	The transceiver continuously listens for messages addressed to its id by invoking the pullRequest method of the CanBuss.
•	Received messages are added to a blocking queue (pipe) for further processing, ensuring a thread-safe mechanism for message handling.
4. Thread Management:
•	Implements the Runnable interface, allowing the transceiver to run in its own thread.
•	Supports controlled termination via the killThread method, which sets an AtomicBoolean flag (acceptMessages) to stop the message reception loop.
5. Performance Control:
•	A runTimer parameter provides flexibility in defining the runtime behavior or lifecycle of the transceiver.
•	Implements a Thread.sleep delay when no messages are available, reducing CPU usage during idle periods.

Core Components:
1.	Attributes:
o	pipe: A blocking queue for safely transferring received messages between threads.
o	canBuss: The shared CAN bus instance that facilitates message communication.
o	id: The unique identifier for the transceiver's node.
o	acceptMessages: A thread-safe boolean flag used to control the transceiver's active state.
o	runTimer: An optional parameter for configuring runtime behavior.
2.	Methods:
o	sendMessage:
	Sends messages to the CAN bus.
o	killThread:
	Terminates the transceiver's thread by stopping the reception loop.
o	run:
	Implements the thread's behavior, continuously polling for messages and processing them.

Utility and Purpose:
The Transceiver class is designed for:
1.	Node-Specific Communication:
o	Ensures that messages are routed to and processed by the appropriate nodes in the network.
2.	Thread-Safe Operations:
o	Uses a blocking queue to manage inter-thread communication.
3.	Simulated CAN System:
o	Mimics real-world CAN node behavior, where each node listens for and processes messages addressed to it.
Use Cases:
•	Simulated CAN Network:
o	Models real-world CAN bus communication in a software environment.
•	Asynchronous Message Handling:
o	Supports concurrent message transmission and reception using multi-threading.
•	Node-Specific Task Management:
o	Ensures each transceiver only processes messages relevant to its unique address.


The Controller class serves as the main orchestrator in this CAN (Controller Area Network) simulation system. It acts as an intermediary between the microprocessor, transceivers, and the CAN bus. Its responsibilities include decoding and routing messages, managing communication pipelines, and controlling the transceiver threads.

Key Features:
1. Centralized Communication:
•	The Controller manages communication between:
o	Microcontroller: Using the flagPipe to send anomalies or decoded messages.
o	Transceivers: Using the requestPipe for receiving messages and the instruction queue (instructionList) to send commands.
2. Transceiver Management:
•	Dynamically sets up and controls a list of Transceiver instances, each assigned to a specific address.
•	Runs each transceiver in a separate thread, enabling asynchronous message handling.
•	Provides functionality to deactivate all transceivers safely.
3. Message Processing:
•	Decode Incoming Messages:
o	Converts raw message bytes from the requestPipe into a higher-level, understandable format (InternalRequest) for the microcontroller.
•	Encode Outgoing Instructions:
o	Encodes instructions from the microcontroller into raw message bytes (MessageWord) and sends them via the transceivers.
4. Thread Management:
•	Implements the Runnable interface, allowing the controller to run in its own thread.
•	Continuously processes incoming and outgoing messages while the thread is active.
5. Resiliency:
•	Includes mechanisms to safely stop transceivers and handle interruptions during idle periods (via Thread.sleep).

Core Components:
1. Attributes:
•	flagPipe: Communication channel for sending decoded messages or anomaly signals to the microcontroller.
•	instructionList: Queue containing instructions for the microprocessor.
•	requestPipe: Queue for receiving raw messages from transceivers.
•	transceiversList: Array of active transceivers managed by the controller.
•	runningTransceivers: Threads managing the execution of transceiver instances.
•	myCanbuss: Shared CanBuss instance for CAN communication.
•	addresses: List of transceiver addresses.
•	delimiter: Used for encoding/decoding instructions.
•	active: Atomic flag for controlling the controller's run state.
2. Methods:
1.	setup:
o	Initializes and starts transceivers for each address.
o	Ensures transceivers are run in their own threads.
2.	decodeMessageAndSend:
o	Retrieves raw messages from the requestPipe.
o	Converts them into an InternalRequest for the microcontroller.
3.	decodeInstructionAndSend:
o	Processes instructions from instructionList.
o	Encodes the instructions into a MessageWord and sends them to the transceivers.
4.	deactivateTheTransceivers:
o	Safely terminates all active transceiver threads.
o	Sets the active flag to false.
5.	run:
o	Main loop for processing messages and instructions while the thread is active.
o	Handles idle periods with a short sleep interval to reduce CPU usage.

Utility and Purpose:
The Controller serves as the brain of the CAN simulation, ensuring smooth, bi-directional communication between components:
•	Decoding incoming messages into actionable instructions for the microcontroller.
•	Encoding outgoing instructions and dispatching them to the correct transceivers.
•	Managing transceivers and their threads efficiently, ensuring asynchronous, node-specific message handling.

Use Cases:
1.	Simulating a Central Node in a CAN-based network:
o	Ideal for modeling the behavior of a controller node in a distributed system.
2.	Handling Asynchronous Communication:
o	Allows simultaneous processing of incoming and outgoing messages using separate threads and queues.
3.	Dynamic Node Management:
o	Easily add or remove transceivers to simulate varying network configurations.
This class is an efficient and flexible controller implementation for a simulated CAN system, capable of handling complex communication flows and dynamic node management.


The Microprocessor class simulates a node in a CAN (Controller Area Network) system, responsible for executing a predefined set of instructions based on sensor data. It interacts with the network and the main controller to send requests, receive responses, and handle data processing for specific tasks like anomaly detection and status reporting.
Key Features:
1. Role and Functionality:
•	Execution of Instructions: The microprocessor performs a set of tasks (e.g., sensor data processing, anomaly detection) based on its assigned node type (e.g., LEVEL_CHECK, AVERAGE_CHECK).
•	Communication: It communicates with a central Controller via pipes and also communicates with other nodes in the network.
•	Sensor Data Handling: It reads data from a file, processes it according to the node type, and sends instructions based on the data (e.g., flagging anomalies, status updates).
2. Node Types:
•	The microprocessor can be one of several node types, each responsible for a specific function:
o	AVERAGE_CHECK: Performs average data checks (likely used for general monitoring).
o	MASTER_PROGRAM: A master node, potentially overseeing the entire network or performing more complex operations.
o	LEVEL_CHECK: Checks sensor data against predefined thresholds (e.g., fluid levels).
o	BOUNDS_CHECK: Checks data against upper and lower bounds (e.g., temperature or pressure).
o	NONE: A placeholder or inactive node.
3. Pipes for Communication:
•	getResponsesPipe: Receives responses from the Controller and other nodes.
•	sendRequestsPipe: Sends requests to the Controller for further processing or to other nodes in the network.
4. Data Handling:
•	sensorData: A file containing sensor readings. The microprocessor reads and processes this data.
•	readValue(): Reads and returns the next value from the sensor data file, handling errors if the file is empty or corrupt.
•	specializedMethod: A dynamic handler that processes sensor data based on the node type, such as detecting anomalies, checking levels, or performing engine diagnostics.
5. Request and Response Management:
•	The microprocessor handles incoming requests from other nodes, processes them (e.g., checks instructions like STATUS or STOP), and sends responses back.
•	handleRequest(): Handles both incoming requests and responses. For example, if the request is for status or stopping the execution, it manages these commands appropriately.
•	sendRequest(): Sends the current instructions to target nodes and logs the action.
6. Thread Management:
•	Implements the Runnable interface, allowing the microprocessor to run in its own thread and continually process data while checking for requests and handling responses.
•	The main loop reads sensor data, processes it, handles requests, and communicates with other network nodes.
7. Execution Flow:
•	The microprocessor first reads data from the sensor file. Then, based on the node's type, it processes the data and takes appropriate actions (e.g., detecting anomalies, checking thresholds).
•	It periodically checks for responses and sends requests based on the processed data.
•	It continues to read sensor data until the execution is stopped or the file is fully read.

Core Components:
1. Attributes:
•	networkBuss: The shared CAN bus for communication with other nodes.
•	nodeAddresses and targetAddresses: Lists of addresses the microprocessor listens to and communicates with.
•	getResponsesPipe and sendRequestsPipe: Queues for receiving and sending messages.
•	specializedMethod: A handler for node-specific data processing logic.
•	sensorData: The path to the file containing sensor data.
•	logOfEvents: A log of events (e.g., requests sent, responses received) for debugging or analysis.
•	awaitingResponses: Keeps track of how many responses are awaited.
•	sendStatus: Flags whether a status update needs to be sent.
•	upperBound and lowerBound: Used for bounds checking on sensor data.
•	habibi: Represents the type of node.
2. Methods:
1.	setup:
o	Initializes the controller, sets up the specialized data-processing method based on node type, and opens the sensor data file if provided.
2.	endExecution:
o	Closes the sensor data file and deactivates the transceivers, ending the microprocessor’s operation.
3.	readValue():
o	Reads the next line from the sensor data file and returns it as a double. If the file is exhausted or an error occurs, it returns Double.MAX_VALUE to signal an issue.
4.	sendRequest():
o	Sends requests to the target addresses based on the current instructions.
5.	handleRequest():
o	Processes incoming requests from the getResponsesPipe. If a request needs a response (e.g., status update or stop signal), it handles it and sends responses to the requesting nodes.
6.	run():
o	Main execution loop that continually processes sensor data, handles requests, and sends data to the network. The loop continues until the node is deactivated.

Use Cases:
1.	Sensor Data Processing:
o	The Microprocessor class can simulate various types of sensors and nodes in a CAN network, processing sensor data and detecting anomalies.
2.	Real-Time Network Interaction:
o	It communicates with other nodes in the network, sending and receiving requests/responses in real time.
3.	Flexible Node Configuration:
o	The node type can be changed to fit different roles in the network, allowing for a dynamic and adaptable simulation environment.
4.	Fault Detection:
o	The microprocessor can be used to detect faults or anomalies based on sensor readings, providing a mechanism for monitoring system health in a distributed manner.

The CreateNode class is designed to facilitate the creation and management of nodes in a simulation of a CAN (Controller Area Network) system. It provides functionality for node initialization, execution, communication setup, and simulation control. Each node is configured with specific properties, interacts with other nodes, and logs its activities.
Key Features:
1. Node Configuration:
•	Each CreateNode instance represents a node with a specific configuration, including its type, priority, and bounds.
•	It accepts parameters like a node type (NodeType), priority (Priority), bounds (Bounds), sensor data input file (inputFile), node name (nodeName), and the CAN bus (CanBuss) it communicates over.
2. Node Addresses:
•	The node is assigned a response address which is generated randomly using a RandomAddressGenerator (via the responseAddresses list).
•	It can also communicate with target addresses (other nodes in the network), which can be added to the node after its creation.
3. Microprocessor and Thread Management:
•	Each node is associated with a Microprocessor object that simulates the processing and communication functions for that node.
•	The initialize() method sets up the Microprocessor with the necessary configuration and sensor data file.
•	The run() method starts the microprocessor in a new thread, allowing the node to begin executing in parallel with others.
4. Execution Control:
•	endExecution(): Ends the execution of the node, saves the log of events to a file, and writes it to an output file. The output file name is generated based on the input file by appending _log.txt to the base name.
5. Node Communication:
•	The bondNodes() static method establishes communication between nodes by assigning target addresses from the list of nodes that are not already responding to each other. It binds nodes together so they can interact in the simulation.
•	The compareTo() method is used to check if two nodes are identical based on their response addresses. This is used to determine if a node should communicate with another.
6. Simulation Control:
•	The startNodes() static method initializes, runs, and ends the simulation for a given array of nodes. It starts all the nodes, waits for the specified simulationTime, and then ends the execution of each node.
7. Logging:
•	Each node maintains an internal list (info) that logs events and communications during the simulation. The log is written to a file at the end of the simulation.
8. Comparison Logic:
•	The compareTo() method compares two nodes based on their response addresses. If two nodes share a response address, the method returns 0 (indicating they are equivalent). Otherwise, it returns 1, meaning they are different nodes.
•	This comparison logic is important for establishing connections between nodes.
9. toString():
•	The toString() method provides a string representation of the node's basic details, such as its response address, type, and priority.

Class Breakdown:
1.	Attributes:
o	nodeType: Type of the node (NodeType enum).
o	nodePriority: The priority of the node (Priority enum).
o	nodeBounds: Bounds for the node’s operational range (Bounds object).
o	inputFile: The file containing sensor data for the node.
o	nodeName: The name of the node.
o	responseAddresses: List of response addresses assigned to the node.
o	targetAddresses: List of target addresses that the node communicates with.
o	rad: An instance of RandomAddressGenerator for generating random addresses.
o	microprocessor: The Microprocessor object that performs data processing and communication for the node.
o	thread: The thread running the Microprocessor.
o	outputFile: The file where the log will be saved after the simulation.
2.	Methods:
o	getResponseAddress(): Returns the list of response addresses.
o	addTargetAddress(): Adds target addresses to the node.
o	initialize(): Initializes the node's Microprocessor with the provided configuration and input file.
o	run(): Starts the Microprocessor in a new thread to begin execution.
o	endExecution(): Ends the node’s execution and writes the log file.
o	compareTo(): Compares two nodes based on their response addresses.
o	toString(): Provides a string representation of the node's details.
o	bondNodes(): Binds nodes by establishing communication between them.
o	startNodes(): Starts and runs the simulation for a list of nodes.

Use Cases:
1.	Simulation Setup:
o	CreateNode facilitates the setup and management of nodes in a distributed simulation where each node may process sensor data, communicate with other nodes, and detect anomalies.
2.	Dynamic Node Creation:
o	It allows for dynamic creation and initialization of nodes with various configurations, making it useful for simulating different types of devices in a network.
3.	Inter-node Communication:
o	The class handles the binding of nodes, ensuring that each node knows which other nodes it can communicate with during the simulation.
4.	Simulation Execution:
o	The class provides control over the simulation, allowing users to start and end nodes, and log their activities during the process.

Potential Improvements:
1.	Error Handling:
o	Add more robust error handling for cases where nodes fail to initialize or run due to configuration issues (e.g., invalid file paths, network errors).
2.	Performance Optimization:
o	Consider optimizing the thread management, especially if the simulation involves many nodes, to ensure smooth and efficient operation.
3.	Enhanced Logging:
o	Improve logging functionality to capture more detailed events and potential exceptions during the node execution.
4.	Extensibility:
o	Extend the node configuration options, such as allowing nodes to have different communication protocols or to perform more complex data processing tasks.
Overall, CreateNode offers a flexible approach for simulating and managing nodes in a distributed network, providing an essential framework for simulating CAN bus systems or similar distributed systems.

The NetworkControl class is responsible for managing the nodes in the CAN simulation network and controlling the simulation's execution. It serves as a central hub for adding, removing, and running the nodes, which interact via a CanBuss.
Key Features:
1. Node Management:
•	addNodeToList(): Adds a new node (CreateNode) to the list of nodes (nodeList) and updates a ListView UI component (quiListUpdate) to reflect this addition.
•	removeNodeFromList(): Removes a selected node from the list of nodes and updates the UI. The node is identified by its selected index in the ListView. The corresponding node is also removed from the nodeList.
2. Simulation Control:
•	runSimulation(): Starts the simulation by calling the CreateNode.bondNodes() method to establish communication links between nodes and then runs the simulation by invoking CreateNode.startNodes(). It accepts the runningTime as a parameter, which determines how long the simulation will run.
3. Data Bus (CanBuss):
•	The dataBuss is an instance of CanBuss, which is likely the communication mechanism used by the nodes to interact with each other. The NetworkControl class can control the network's behavior by manipulating this bus and the nodes connected to it.
4. UI Integration:
•	The ListView<CreateNode> is used to update the user interface when nodes are added or removed from the simulation. This allows the user to visualize the nodes currently in the network.

Class Breakdown:
1.	Attributes:
o	nodeList: An ArrayList<CreateNode> that holds all the nodes in the network.
o	dataBuss: An instance of CanBuss that represents the communication bus for the network.
2.	Methods:
o	addNodeToList(CreateNode newNode, ListView<CreateNode> quiListUpdate): Adds a new node to the nodeList and updates the ListView.
o	removeNodeFromList(ListView<CreateNode> quiListUpdate): Removes the selected node from the nodeList and updates the ListView.
o	runSimulation(int runningTime): Bonds the nodes and starts the simulation, passing the runningTime for how long the simulation should run.

Use Cases:
1.	Node Management:
o	The NetworkControl class is essential for managing nodes within the network. It facilitates adding and removing nodes dynamically from the simulation, allowing for flexible control over the network configuration.
2.	Running the Simulation:
o	The class can execute a network-wide simulation by invoking methods that bond the nodes and start their individual executions. This is especially useful for testing scenarios where the interactions between nodes need to be simulated over time.
3.	UI Integration:
o	By using the ListView, the class can interact with a user interface, adding or removing nodes from the display as the simulation is set up or modified in real time.

Potential Improvements:
1.	Error Handling:
o	Add error handling for cases where a node is removed but might not exist in the list, or where there are issues starting or running the simulation.
2.	Simulation Monitoring:
o	It could be beneficial to integrate monitoring functionality to track the progress of the simulation or visualize real-time data changes from the nodes.
3.	Performance Optimization:
o	For simulations with a large number of nodes, performance optimizations might be needed, particularly in managing the threads for each node.
4.	User Feedback:
o	Implement more detailed feedback mechanisms to notify the user if a node has been successfully added or removed, or if any errors occur during the simulation run.
5.	Customization:
o	Add options for more customization, such as modifying node parameters (e.g., bounds, priority) after they've been created or adjusting other simulation settings dynamically.

The HelloController class handles the user input and interaction for the CAN simulation interface. It manages the setup of nodes and the simulation, as well as handling errors related to user input.
Key Aspects of Simulation Start and User Input:
1. Simulation Start (startSimulationOnAction method):
•	Triggering the Simulation: The simulation is started when the user clicks the "Start Simulation" button (startSimButton), which invokes the startSimulationOnAction method.
•	Simulation Time Input: The user is expected to input the simulation running time in seconds via the simTime text field.
•	Input Validation: The method first checks if the simTime value is valid (greater than 0). If invalid, it calls the printError method with the error type BOUND_ERROR.
•	Simulation Execution: If the time is valid, the method then attempts to sleep for 2 seconds (likely to simulate a delay before running the simulation) and then starts the simulation using simulator.runSimulation(this.simRunningTime).
2. User Input Validation (handleCreateNode method):
•	Triggering Node Creation: The user can create a node by filling out the relevant fields and clicking a button that triggers the handleCreateNode method.
•	Fields: The user is expected to input values for:
o	nodeNameField: The name of the node.
o	nodeTypeComboBox: The type of node (NodeType).
o	lowerBoundField and upperBoundField: Bounds for the node.
o	inputTextFieldFile: The input file for the node.
o	priorityComboBox: The priority for the node (Priority).
•	Validation:
o	The method validates each input field to ensure that:
	The node name is not empty.
	The node type is selected and is valid.
	The priority is selected and is valid.
	The input file exists and is a .txt file.
	The lower bound is less than or equal to the upper bound.
•	Error Handling: If any of the fields are invalid, an appropriate error message is displayed by calling the printError method.
•	Node Creation: If all validations pass, the node is added to the simulation via simulator.addNodeToList() and a success message is displayed: "Node successfully added!".
3. Error Handling:
•	The printError method is used to display error messages for various user input issues, such as:
o	Null or empty node name (NULL_NAME).
o	Invalid bounds (BOUND_ERROR).
o	Invalid priority or node type (INVALID_PRIORITY, INVALID_NODE_TYPE).
o	Incorrect file type (INPUT_FILE_ERROR).

Flow of User Interaction:
1.	Node Creation:
o	The user fills out the form with the node details.
o	The handleCreateNode method validates the input.
o	If everything is valid, the node is added to the list for the simulation, and a success message is displayed.
o	If there are any issues with the input, the relevant error message is displayed in the UI.
2.	Starting the Simulation:
o	The user enters the simulation time and clicks "Start Simulation".
o	The input is validated, and if valid, the simulation is started.
o	The runSimulation method is called to initiate the simulation based on the specified time.
 

NodeSpecialization Package:
The classes provided implement the NodeTypeInterface, which processes sensor data based on different node types (such as AverageCheck, BoundsCheck, LevelCheck, and MasterNode). Each of these node types contains logic to handle sensor data and respond with specific instructions, depending on conditions defined within each node type.
Overview of the NodeTypeInterface and Implementations
The NodeTypeInterface defines a single method, processSensorData, which each class must implement. This method processes the sensor data and returns an ArrayList<Instructions> based on the received data and the sendStatus flag.
1. AverageCheck Node:
•	Purpose: This node computes the average of the last 10 sensor data values and sends different instructions based on the sensor data and its moving average.
•	Attributes:
o	last10Numbers: A fixed-size deque (ArrayDeque) to store the last 10 sensor readings.
o	runningSum: A variable that maintains the sum of the last 10 readings to calculate the average efficiently.
o	average: The computed average of the last 10 readings.
•	Logic:
o	The node keeps track of the last 10 sensor readings.
o	When a new reading comes in, it updates the average by adding the new value and removing the oldest.
o	Depending on the sensor data value and sendStatus, it adds an appropriate instruction to noResponse (e.g., FUMES_LEVEL_GENOCIDE, FUMES_LEVEL_HIGH, etc.).
2. BoundsCheck Node:
•	Purpose: This node checks if a sensor value is within defined bounds (upper and lower).
•	Attributes:
o	upperBound and lowerBound: The valid range for sensor data.
•	Logic:
o	If the sendStatus flag is true, the node checks if the sensor value is outside the bounds and returns different instructions based on whether it's too high or too low (e.g., MOTOR_OVERHEATED, MOTOR_COLD).
o	If sendStatus is false, it returns NO_OP if the sensor value is within bounds, or an appropriate instruction if it's out of bounds.
3. LevelCheck Node:
•	Purpose: This node checks if a sensor value is above or below a specified threshold.
•	Attributes:
o	threshold: The value that the sensor reading is compared to.
•	Logic:
o	If sendStatus is true, it returns GAS_LEVEL_HIGH or GAS_LEVEL_LOW based on whether the sensor value is above or below the threshold.
o	If sendStatus is false, it only returns NO_OP if the value is within an acceptable range, or GAS_LEVEL_LOW if it's below the threshold.
4. MasterNode Node:
•	Purpose: This node sends status requests to other nodes, typically in a periodic manner.
•	Logic:
o	The node always returns STATUS if the sendStatus flag is false, and NO_OP otherwise.
o	This class represents a simple master node that primarily acts as a controller that queries the status of other nodes.
Key Points in the Implementation:
•	Sensor Data Handling: Each node type processes sensor data differently based on its role. For instance, AverageCheck focuses on averaging data, BoundsCheck validates against predefined bounds, and LevelCheck works with thresholds.
•	Response Logic: The nodes decide which instructions to add to the noResponse list based on the sendStatus flag. This flag seems to control whether the node actively sends instructions (true) or just processes the sensor data passively (false).
•	Instructions: The instructions are actions or status updates sent by each node. Examples include MOTOR_OVERHEATED, FUMES_LEVEL_GENOCIDE, and GAS_LEVEL_HIGH.
Example Use Case:
1.	A sensor sends a reading to the node (e.g., temperature or gas level).
2.	The node processes this data based on its own logic (e.g., averaging, comparing bounds, or checking thresholds).
3.	The node then generates a list of Instructions to send back, which could include alerts like MOTOR_OVERHEATED or GAS_LEVEL_LOW.
This structure allows for different node types to implement unique behaviors and respond to environmental data in specific ways, which could be useful in various simulation or monitoring applications like CAN bus communication.

Graphical User Interface (GUI) Overview
The Control Area Network (CAN) Simulator features a user-friendly Graphical User Interface (GUI) designed to manage and simulate nodes within a CAN network efficiently. This chapter provides an overview of the GUI layout, functionality, and the purpose of each section to ensure optimal usage of the simulator.
Layout Overview
The GUI is divided into three primary columns:
1.	Node List Column (Left)
2.	Node Attributes and Simulation Control Column (Middle)
3.	Message Transmission Display Column (Right)
1. Node List Column (Left)
This column displays a list of all the nodes currently added to the CAN simulation. Each node is represented by its unique identifier (node name). Below the node list, there is a button to remove selected nodes from the simulation.
•	Features:
o	View all active nodes.
o	Select a node for removal.
o	Remove nodes using the "Remove Selected" button.
2. Node Attributes and Simulation Control Column (Middle)
This column is divided into two main sections: Node Attributes and Simulation Control.
Node Attributes
The Node Attributes section allows users to define and configure the characteristics of a node before adding it to the simulation. The following fields are available:
•	Node Name: A unique identifier for the node.
•	Node Type: A dropdown menu to select the type of node:
o	AVERAGE_CHECK: takes 2 parameters (LowerBound, UpperBound) and checks if the average of the last 10 values received from sensors is in bounds.
o	LEVEL_CHECK: takes one parameter (LowerBound) and checks if the sensor data is above the inputed value.
o	BOUNDS_CHECK: takes 2 parameters (LowerBound, UpperBound) and checks if the values received from sensors is in bounds.
o	MASTER_PROGRAM: takes no parameters and sends a STATUS message in the network when its sensor data is positive.
•	Lower Bound: Specifies the lower limit of data values the node can process or transmit.
•	Upper Bound: Specifies the upper limit of data values the node can process or transmit.
•	Priority: A dropdown menu to set the priority of the node’s messages on the CAN bus. Nodes with higher priority will have their messages transmitted before those with lower priority in case of bus contention. Priority 0 is the lowest and 5 the highest.
•	Sensor Data: The file name containing the sensor data to be associated with the node.
A button labeled "Create Node" is provided to add the configured node to the simulation.

Simulation Control
The Simulation Control section contains:
•	Simulation Time (seconds): A field to specify the duration of the simulation.
•	Start Simulation: A button to begin the simulation based on the configured nodes and runtime.
3. Message Transmission Display Column (Right)
The Message Transmission Display column provides a real-time visualization of messages transmitted over the CAN bus. This area updates dynamically during the simulation to show:
•	The source and destination nodes of each message.
•	The priority of the message.
•	Any additional data fields associated with the transmission.
This feature helps users monitor the communication activity and assess the behavior of the CAN network in real-time.
Workflow
1.	Add Nodes:
o	Use the Node Attributes section to configure a node.
o	Click the "Create Node" button to add the node to the Node List Column.
2.	Remove Nodes:
o	Select a node from the Node List Column.
o	Click "Remove Selected" to delete the node.
3.	Start Simulation:
o	Set the simulation runtime in seconds using the Simulation Time field.
o	Click "Start Simulation" to begin observing real-time message transmission.
4.	Monitor Simulation:
o	View the Message Transmission Display column for real-time data on CAN bus activity.
By following these steps, users can efficiently manage nodes and observe the behavior of their CAN network simulation.




 

Testing & Validation:
0.Testing for priority protocol violation MASTER NODE mid priority:
	To test the protocol implementation, I choose a BOUNDS_CHECK node with priority 5, a LEVEL_CHECK node with priority 0, and a MASTER with priority 3. The nodes will not send warnings, but only receive STATUS updates from the master. 
	I am looking to see how the BOUNDS_CHECK node’s responses will always come before LEVEL_CHECK node’s responses.
 
After running the simulation for 4 seconds we obtain for every reading the address read, that being the targeted address with the bigest priority at that moment. We can also notice that because of how we initialize the target addresses of nodes (every node is connected to any other node) The responses to master’s STATUS are also sent to the other nodes. This thing is visible on the bus, but is not visible in the node’s output log file. Why? Because the code takes account of this “issue” in the implementation and the only type of nodes that are allowed to read the responses they did not send a request for are the master nodes.
 
It is noticeable in the log file that the network priority-based protocol works as intended and that there are no problems with message transmission between nodes. The only problem being the lifetime of a node, nodes terminates once their sensor_data file reached EOF, causing the CAN BUS to freeze once the master node terminates.

1.Testing for priority protocol violation MASTER NODE lowest priority:
	To test the protocol implementation, I choose to give all the nodes the same level of priority, besides the master controller node. Also, the nodes will only respond to STATUS updates and will never send a warning message. The only message initializer is the master node that is also the one with the lowest priority, LEVEL_0.
What I am expecting is to see at first the addresses of the non-master nodes in the can bus – as the master sends a lot of STATUS messages to the nodes with higher priority - then the address of the master node - because after 10 *3 = 30 high-priority addresses the master ends its data set of 10 reading values – and the only addresses left on the buss are the masters’.  
 
After running the simulation for 3 seconds we abtain for every reading the address read, that being the target address with the bigest priority at that moment. We can also notice that because of how we initialize the target addresses of nodes (every node is connected to any other node) The responses to master’s STATUS are also sent to the other nodes. This thing is visible on the bus, but is not visible in the node’s output log file. Why? Because the code takes account of this “issue” in the implementation and the only type of nodes that are allowed to read the responses they did not send a request for are the master nodes.
 	 
  
2.Testing for priority protocol violation MASTER NODE highest priority:
	The following test is giving the master node the highest priority and the other nodes lower priorities that are not equal. Also this time the nodes are allowed to sent warnings to the master due to data inconsistency from sensors.
average(exhaust): half data warning generating/half status only – 2nd priority
bounds(motor): half data warning generating/half status only – 4th priority
level(fuel_tank): status only	-3rd priority
main(master): alternates between status and idle – 1st priority

This time I am assuming that the master will send all the requests and then will start waiting for the responses, but while sending STATUS the node with the 2nd priority will already start sending STATUS responses back, also I am looking forward to see a few status responses from 3rd priority node during STATUS sending, and the 4th priority to be able to send information only during an warning during STATUS sending the STATUS responses coming just after the other node’s end sending STATUS responses.

 

After performing the simulation It is visible that the due to the higher priority of the master the warnings come first, then the master’s request get processed and after that the answer to it is sent. It is noticeble how the priority of the non-master nodes matters in the way responses are received by the master.

  

3.Testing for priority protocol violation MASTER NODE moderate priority:
In this test I want to give to the Master a level_3 priority, 2 nodes high priority and the last one a low priority. I am expecting to see how the 2 nodes with highest priority receives STATUS request faster than the other node with lower priority and therefore their answers will appear earlier in the master log. Also, I expect warnings to propagate the same in the bus for all 3 nodes.
The setup is the same as in test case 2: but this time fuel_tank and motor will have Level 5 priority and exhaust Level 0.
 
The result was slightly more specific than I have thought, as the node with the lowest priority could only send warnings while master node was active and the other 2 higher priority nodes were parsing the sensor file with no runtime errors.
 
 

Conclusion: 
The CAN (Controller Area Network) Simulator successfully models the behavior of a simplified CAN bus system, providing an interactive environment for simulating node communication in real-time. The design and implementation effectively capture key aspects of a CAN network, including data processing, message prioritization. The tests conducted on the CAN Simulator demonstrated that the system performs as intended under various conditions. Each test scenario incorporated all node types and verified their functionality using sensor data carefully designed to trigger all possible responses. The system processed inputs without errors and logged outputs correctly, fulfilling the project 

Sources:
https://community.nxp.com/t5/NXP-Tech-Blog/101-Controller-Area-Network-CAN-standard/ba-p/1217054
https://en.wikipedia.org/wiki/CAN_bus
https://www.youtube.com/watch?v=WikQ5n1QXQs&ab_channel=TheEVEngineer
https://www.youtube.com/watch?v=JZSCzRT9TTo&ab_channel=Electronoobs
https://www.youtube.com/watch?v=mNjISEK5VMg

