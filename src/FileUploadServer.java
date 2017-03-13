import java.io.*;
import java.net.*;

public class FileUploadServer implements Runnable {
	
	public int uploadPort;
	
	private static FileUploadServer instance;
	
	private FileUploadServer()
	{
		uploadPort = Client.getClient().port;
	}
	
	@Override
	public void run() 
	{
		initialize();
	}
	
	public static FileUploadServer getFileServer()
	{
		if(instance==null) instance = new FileUploadServer();
		return instance;
	}

	public void initialize()
	{
		try 
		{
			ServerSocket ssock = new ServerSocket(uploadPort);
			
			while(true)
			{
				Socket sock = ssock.accept();
				System.out.println("Connected");
				new Thread(new Uploader(sock)).start();
			}
		} 
		catch (IOException e) 
		{
			System.err.println(e.getMessage());
		}
		
	}
	
	private class Uploader implements Runnable
	{
		Socket sock;
		DataOutputStream sock_dos;
		DataInputStream sock_dis;
		
		public Uploader(Socket sock) throws IOException
		{
			this.sock = sock;
			sock_dos = new DataOutputStream(sock.getOutputStream());
			sock_dis = new DataInputStream(sock.getInputStream());
		}
		
		@Override
		public void run() 
		{
			try 
			{
//				dos.writeBytes("You are connected\n\r");
				while(true)
				{
					RequestP2P req = new RequestP2P(sock_dis);
					ResponseP2P resp = ResponseP2P.createAndSendResponse(req, sock_dos);
//					resp.sendResponse(dos);
				}
				
			} 
			catch (Exception e) 
			{
				//TODO: Find out why NULL is sent when the socket is closed
				e.printStackTrace();
			}
			finally 
			{
				try {
					sock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
		

	}

	
}
