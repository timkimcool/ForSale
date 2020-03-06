Practicing networking and JavaFX by desiging board game ForSale based on UI from board game arena.

Currently in progress. Got most of the set up complete. Need to code the actual turns.

To do:
 > Phase 1 turns
 > Phase 2 UI + turns
 > Make resource path relative + serialize card back image
 > Update UI based on window size
 
Optimization:
> Animation when purchasing cards
> Flush streams
> Markup README with rules

Next project:
 > Turn application into web app
 > Turn application into mobile app
 
 Card image canvs: 295 x 450

--Setting up network--
Server Side:
1. Create serverSocket to accept client sockets: serverSocket = new ServerSocket(PORT);
2. Set up socket listener: Socket socket = serverSocket.accept();
3. Create data/object input and output streams: new DataOutputStream(socket);
4. Write data/object; in order to send object, object must implement serializable. For fields that cannot be sent over streams, mark them as transient. Then create private writeObject classes to manually serialize fields to send over and then readObject class to deserialize fields from the client.
Client side:
1. Create socket and connect to host: socket = new Socket(HOST, PORT);
2. Create data/object input and output streams
3. receive data/object; see ServerSide step 4.
