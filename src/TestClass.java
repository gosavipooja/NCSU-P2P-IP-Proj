
public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestClass m = new TestClass();
		m.peerTests();
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
}
