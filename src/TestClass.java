import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestClass m = new TestClass();
//		m.peerTests();
//		m.checkResponseP2P();
//		m.checkRequestP2P();
//		m.checkRequestResponse();
		
//		if(args[0].equalsIgnoreCase("s"))
//			m.checkFileServer();
//		
//		else if(args[0].equalsIgnoreCase("c"))
//			m.checkFileClient();
		
		System.out.println(Utils.getIPAddr());
		
	}
	
	public void checkFileServer()
	{
		FileUploadServer fs = FileUploadServer.getFileServer();
		fs.initialize();
	}
	
	public void checkFileClient()
	{
		FileDownloadClient f = new FileDownloadClient();
		try {
			f.requestFileDownload(761, "","127.0.0.1",7734);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void peerTests(){
		PeerManager pm = PeerManager.getPeerManager();
		pm.addPeer("TrialPeer HostName 1", 1234);
		pm.addPeer("TrialPeer HostName 2", 123);
		pm.addPeer("TrialPeer HostName 3", 12);
		pm.addPeer("TrialPeer HostName 4", 1);
		
	//	pm.printPeers();
		
		Peer p = pm.locatePeer("TrialPeer HostName 5");
		System.out.println(p);
		
		boolean b = pm.deletePeer("TrialPeer HostName 2");
		System.out.println(b);
	//	pm.printPeers();
	}
	
	/*
	public void checkResponseP2P()
	{
		try 
		{
			DataInputStream dis = new DataInputStream(new FileInputStream("responseP2P.txt"));
			@SuppressWarnings("unused")
			ResponseP2P r = new ResponseP2P(dis);
			
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void checkRequestP2P()
	{
		try 
		{
			DataInputStream dis = new DataInputStream(new FileInputStream("requestP2P.txt"));
			@SuppressWarnings("unused")
			RequestP2P r = new RequestP2P(dis);
			
			DataOutputStream dos = new DataOutputStream(new FileOutputStream("response"+r.rfcNum+".txt"));
			FileUploadServer.getFileServer().generateResponse(r, dos);
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	*/
	
	public void checkRequestResponse()
	{
		int rfcNum = 761;
		
		try 
		{
			RequestP2P req = RequestP2P.createRequest(rfcNum);
			DataOutputStream req_dos = new DataOutputStream(new FileOutputStream("request.txt"));
			req.sendRequest(req_dos);
			
			
			DataInputStream req_dis = new DataInputStream(new FileInputStream("request.txt"));
			
			DataOutputStream dos = new DataOutputStream(new FileOutputStream("response.txt"));
			ResponseP2P resp = ResponseP2P.createResponse(req);
			resp.sendResponse(dos);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
