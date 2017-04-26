import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class FileDownloadClient {
	
	private static FileDownloadClient instance;
	
	public static FileDownloadClient getInstance()
	{
		if(instance == null) instance = new FileDownloadClient();
		return instance;
	}
	
	/* Fetch the peers list for a particular RFC */
	public ResponseP2S getLookupResp(int rfcNum, String title, String s_ip,int s_port) throws Exception
	{
		String method = "lookup";
		
		if(rfcNum==-1)
			method = "list";
		
		// Connect to server
		Socket sock = new Socket(s_ip,s_port);
		DataInputStream dis = new DataInputStream(sock.getInputStream());
		DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
		
		//Create P2S request
		RequestP2S req = RequestP2S.createRequest(method, rfcNum, title);
		
		//Send the P2S request to server
		req.sendRequest(dos);
		
		//Create response object from the socket input
		ResponseP2S resp = new ResponseP2S(dis);
		
		sock.close();
		
		return resp;
	}
		
	
	public ArrayList<Peer> exractPeerList(ResponseP2S resp)
	{
		if (resp.status != 200)
			return null;
		
		//Extract peer list
		ArrayList<Peer> peerList = new ArrayList<>();
		for (RFC r : resp.listOfRFCS)
		{
			peerList.add(r.peer);
		}
		
		return peerList;
	}
	
	public boolean requestFileDownload(int rfcNum, String title, String p_ip, int p_port) throws Exception
	{
		//TODO: get IP and port from the server
		int otherPort = 5678;
		if(Client.getClient().port==5678)
			otherPort = 1234;
		
//		Socket sock = new Socket("127.0.0.1",otherPort);
		Socket sock = new Socket(p_ip,p_port);
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
		DataOutputStream dos = new DataOutputStream(new FileOutputStream("RFC/RFC "+rfcNum+".txt"));
		ResponseP2P resp = new ResponseP2P(dis,dos);
		
		dos.close();
		
		if(resp.status!=200)
			return false;
		
		return true;
	}
	
}
