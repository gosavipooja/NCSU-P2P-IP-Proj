import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	
	public void showMenu()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		FileDownloadClient fdc = FileDownloadClient.getInstance();
		
		int n=1;
		
		while(n>0)
		{
			System.out.print("\n\nEnter the RFC number (0 to exit): ");
			try 
			{
				n = Integer.parseInt(br.readLine());
				System.out.println("Enter the title: ");
				String title = br.readLine();
				
				boolean status = fdc.requestFileDownload(n, title);
				if(status)
					System.out.println("Download Success!");
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
	
	public static void main(String args[])
	{
		int port = 5127;
		port = Integer.parseInt(args[0]);
		
		Client.createInstance(port);
		Client client = Client.getClient();
		client.init();
		
		
		client.showMenu();
	}
}
