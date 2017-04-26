import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client 
{
	public String ipAddr;
	public int port;
	
	private static Client instance;
	
	private Client(int port)
	{
		this.ipAddr = Utils.getIPAddr();
		this.port = port;
		
	}
	
	public void init()
	{
		//Start Upload thread
		Thread uploaderThread = new Thread(FileUploadServer.getFileServer());
		uploaderThread.start();
		
		//Start Downloader in the main thread
		FileDownloadClient fdc = FileDownloadClient.getInstance();
		
	}
	
	
	private static void createInstance(int port)
	{
		if(instance==null) instance = new Client(port);
	}
	
	public static Client getClient()
	{
		return instance;
	}
	
	public void showMenu() throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		FileDownloadClient fdc = FileDownloadClient.getInstance();
		
		int n=1;
		
		System.out.println("Enter the client's public IP: ");
		String my_ip = br.readLine();
		Utils.my_ip = my_ip;
		
		System.out.print("Enter Server IP: ");
		String s_ip = br.readLine();
		
		System.out.print("Enter Server Port: ");
		int s_port = 7734;//Integer.parseInt(br.readLine());
		
		//Adds the local RFC files to index
		addLocalFiles2Index(s_ip,s_port);
		
		while(n>0)
		{
			System.out.print("\n\nEnter the RFC number (0 to exit): ");
			try 
			{
				n = Integer.parseInt(br.readLine());
				System.out.println("Enter the title: ");
				String title = br.readLine();
				
				//Get the response object after sending the LOOKUP request
				ResponseP2S resp = fdc.getLookupResp(n, title, s_ip, s_port);
				
				//Show error message if any received from the server
				if(resp.status != 200)
				{
					System.out.println("Server returned error status "+resp.status+" "+resp.phrase);
					continue;
				}
				
				ArrayList<Peer> peerList = new ArrayList<Peer>();
				
				//Extract the Peer list from the RFC list
				for(RFC r : resp.listOfRFCS)
				{
					peerList.add(r.peer);
				}
				
				//Show the peer list to client and get his option
				for (int i=0; i<resp.listOfRFCS.size();i++)
				{
					System.out.println(""+(i+1)+". "+resp.listOfRFCS.get(i));
				}
				System.out.print("\nEnter the peer from whom you want to download: ");
				int p_choice = Integer.parseInt(br.readLine())-1;
				
				Peer p = resp.listOfRFCS.get(p_choice).peer;
				//Request the download from that particular peer
				boolean status = fdc.requestFileDownload(n, title, 
						p.getHostName(), 
						p.getPortNumber());
				
				if(status)
				{
					System.out.println("Download Success!");
					
					//Send a request to add file to the server index
					createAndSendAddReq(n, title, s_ip, s_port);
					
					System.out.println("File added to index");
				}
				else
					System.out.println("Download Failed!");
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}
	
	public void addLocalFiles2Index(String s_ip,int s_port) throws UnknownHostException, IOException
	{
		File[] files = new File("RFC/").listFiles();
		
		for(File f : files)
		{
			if( !f.getName().contains("_out.txt") )
			{
				int rfcNum = extractRfcNum(f.getName());
				System.out.println("Adding RFC"+rfcNum+" to index");
				createAndSendAddReq(rfcNum, " ", s_ip, s_port);
			}
		}
	}
	
	public int extractRfcNum(String s)
	{
		int rfc = Integer.parseInt(s.substring(4, s.length()-4));
		return rfc;
	}
	
	public static void main(String args[]) throws IOException
	{
		int port = 5127;
		port = Integer.parseInt(args[0]);
		
		Client.createInstance(port);
		Client client = Client.getClient();
		client.init();
		
//		new ClientGUI();
		client.showMenu();
	}
	
	
	
	public void createAndSendAddReq(int rfcNum, String title, String s_ip, int s_port) throws UnknownHostException, IOException
	{
		// Connect to server
		Socket sock = new Socket(s_ip,s_port);
		DataInputStream dis = new DataInputStream(sock.getInputStream());
		DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
		
		//Create P2S request
		RequestP2S req = RequestP2S.createRequest("add", rfcNum, title);
		
		//Send the P2S request to server
		req.sendRequest(dos);
		
		//Create response object from the socket input
		ResponseP2S resp = new ResponseP2S(dis);
		
		sock.close();
	}
}
