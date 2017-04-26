import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	static ArrayList<RFC> rfcList;
	
	int port = 7734;
	String myIP;
	
	public Server()
	{
		myIP = Utils.getIPAddr();
	}
	
	public static void main(String args[])
	{
		rfcList = new ArrayList<>();
		Server server = new Server();
		server.initialize();
	}
	
	public void initialize()
	{
		System.out.println("Initializing server");
		try 
		{
			ServerSocket ssock = new ServerSocket(port);
			
			while(true)
			{
				//Accept the connection
				Socket sock = ssock.accept();
				System.out.println("Connected to "+ssock.getInetAddress()+":"+ssock.getLocalPort());
				
				//Launch a new thread
				new Thread(new Responder(sock)).start();
			}
		} 
		catch (IOException e) 
		{
			System.err.println(e.getMessage());
		}
		
	}
	
	private class Responder implements Runnable
	{

		Socket sock;
		DataOutputStream sock_dos;
		DataInputStream sock_dis;;
		
		public Responder(Socket sock) throws IOException
		{
			this.sock = sock;
			sock_dos = new DataOutputStream(sock.getOutputStream());
			sock_dis = new DataInputStream(sock.getInputStream());
		}
		
		
		@Override
		public void run() 
		{
			RequestP2S req;
			try 
			{
				req = new RequestP2S(sock_dis);
				ResponseP2S resp = ResponseP2S.createAndSendResponse(req, sock_dos);
				sock.close();
			} 
			
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Add list to RFC manager
		private void processADD(RequestP2S req)
		{
			Peer p = new Peer(req.getHeaderField("Host"), 
					Integer.parseInt(req.getHeaderField("Port")));
			
			RFC rfc = new RFC();
			rfc.peer = p;
			rfc.rfc_num = req.rfc.rfc_num;
			rfc.title = req.getHeaderField("Title");
					
		}
		
	}

}
