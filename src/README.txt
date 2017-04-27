Team Members:
Pooja Indrajit Gosavi (pigosavi)
Guru Darshan Pollepalli Manohara (gpollep)

1. To compile the projectt use 'make' command.

2. Clean up can be performed by 'make clean'

3. After compilation, the Client can be started using 'java Client' and Server using 'java Server'

4. IMPORTANT: Before the Client is started, ensure that there is folder named 'RFC/' at the same level as the .java source files

5. When Client in started, you will be prompted to enter the Client's IP address using which he can be connected over internet. This was done to mitigate the issue when multiple network interfaces are present

6. As soon as the client starts, it sends ADD requests for the files in the 'RFC/' directory

7. The client will present a prompt where entering a positive number will create a LOOKUP request for that particular RFC number. If the user enters -1, LIST request is created which causes the server to send all the indexes in the system. If -2 is entered, the Client can add any new file into the server index.

8. The RFC manager on server always ensures that only unique entries are seen stored.

9. The server runs at port 7734 and client listens on port 5678 for uploading files to other peers.

10. For both LOOKUP and LIST, the user can further provide the choice of the file which he wants to download and where he wants to download it from

11. All the files are stored in the following format:
RFC/

NOTE: Please ensure that JDK installed on the system before running the program and 'javac' is present in the PATH. Makefile used javac for compilation.
If its not installed please use the following commands on Ubuntu:
	sudo add-apt-repository ppa:openjdk-r/ppa  
	sudo apt-get update   
	sudo apt-get install openjdk-7-jdk 