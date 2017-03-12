import java.io.*;
import java.net.Socket;

public class FileDownloadClient {
	
	private static FileDownloadClient instance;
	
	public static FileDownloadClient getInstance()
	{
		if(instance == null) instance = new FileDownloadClient();
		return instance;
	}
	
	public boolean requestFileDownload(int rfcNum, String title) throws Exception
	{
		//TODO: get IP and port from the server
		int otherPort = 5678;
		if(Client.getClient().port==5678)
			otherPort = 1234;
		
		Socket sock = new Socket("127.0.0.1",otherPort);
		DataInputStream dis = new DataInputStream(sock.getInputStream());
		DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
		
		RequestP2P req = RequestP2P.createRequest(rfcNum);
		req.sendRequest(dos);
		boolean success = downloadAndSaveFile(dis, rfcNum);
		
		sock.close();
		
		return success;
	}
	
	public boolean downloadAndSaveFile(DataInputStream dis, int rfcNum) throws Exception
	{
		ResponseP2P resp = new ResponseP2P(dis);
		
		if(resp.status != 200) return false;
		
		DataOutputStream dos = new DataOutputStream(new FileOutputStream("RFC/RFC "+rfcNum+"_.txt"));
		dos.write(resp.data);
		dos.close();
		
		return true;
	}
	
}
