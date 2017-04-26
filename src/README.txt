1. To compile the file use 'make' command.

2. Cleanup can be performed by 'make clean'

3. After compilation, the Client can be started using 'java Client' and Server using 'java Server'

4. IMPORTANT: Before the Client is started, ensure that there is folder named 'RFC/' at the same level as the .java files

5. When Client in started, you will be prompted to enter the Client's IP address using which he can be connected over internet. This was done to mitigate the issue when multiple network interfaces are present

6. As soon as the client starts, it sends ADD requests for the files in the 'RFC/' directory

7. The client will present a prompt where entering a positive number will create a LOOKUP request for that particular RFC number. If the user enters -1, LIST request is created which causes the server to send all the files in the system. If -2 is entered, the Client can add any new file into the server index.

8. The RFC manager on server always ensures that only unique entrues are seen stored.

9. The server runs at port 7734 and client uses port 5678 for uploading files to other peers.

