import java.io.*;
import java.net.*;

public class FileUploadServer {
	
	private static final int UPLOAD_PORT = 1234;
	
	private static FileUploadServer instance;
	
	private FileUploadServer()
	{
		
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
			ServerSocket ssock = new ServerSocket(UPLOAD_PORT);
			
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
		DataOutputStream dos;
		DataInputStream dis;
		
		public Uploader(Socket sock) throws IOException
		{
			this.sock = sock;
			dos = new DataOutputStream(sock.getOutputStream());
			dis = new DataInputStream(sock.getInputStream());
		}
		
		@Override
		public void run() 
		{
			try 
			{
//				dos.writeBytes("You are connected\n\r");
				while(true)
				{
					RequestP2P req = new RequestP2P(dis);
					ResponseP2P resp = ResponseP2P.createResponse(req);
					resp.sendResponse(dos);
				}
				
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			finally 
			{
//				dis.close();
//				dos.close();
			}
			
		}
		
		

	}
}
