import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileDownloadClient {
	
	public void requestFileDownload(int rfcNum, String title) throws Exception
	{
		//TODO: get IP and port from the server
		
		Socket sock = new Socket("127.0.0.1",1234);
		DataInputStream dis = new DataInputStream(sock.getInputStream());
		DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
		
		RequestP2P req = RequestP2P.createRequest(rfcNum);
		req.sendRequest(dos);
		downloadAndSaveFile(dis, rfcNum);
		
		sock.close();
	}
	
	public void downloadAndSaveFile(DataInputStream dis, int rfcNum) throws Exception
	{
		ResponseP2P resp = new ResponseP2P(dis);
		
		if(resp.status != 200) return;
		
		DataOutputStream dos = new DataOutputStream(new FileOutputStream("RFC/RFC "+rfcNum+"_.txt"));
		dos.write(resp.data);
		dos.close();
	}
	
}
