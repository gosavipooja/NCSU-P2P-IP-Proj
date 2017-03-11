import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestClass m = new TestClass();
		m.peerTests();
		m.checkResponseP2P();
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
}
