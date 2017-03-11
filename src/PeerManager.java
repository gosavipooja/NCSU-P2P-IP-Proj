import java.util.*;

public class PeerManager {
	
	private List<Peer> listOfPeers;
	private static PeerManager instance;
	
	private PeerManager(){
		listOfPeers = new ArrayList<Peer>();
	}
	
	public static PeerManager getPeerManager(){
		//returns the instance of PeerManager
		if(instance != null)	return instance;
		else{
			instance = new PeerManager();
			return instance;
		}
	}
	
	public void addPeer(String hostName, int portNumber){
		//Adds a peer to the list
		Peer p = new Peer(hostName, portNumber);
		if(listOfPeers == null){
			listOfPeers = new ArrayList<Peer>();
		}
		listOfPeers.add(p);
		System.out.println("Added!");
	}
	
	public boolean deletePeer(String hostName){
		//Deletes a peer from the list
		if(listOfPeers == null)	return false;
		else if(listOfPeers.size() == 0) return false;
		else{
			Peer p = locatePeer(hostName);
			if(p == null) return false;
			else{
				int index = listOfPeers.indexOf(p);
				if(index == -1){
					System.out.println("Not Found!");
					return false;
				}else{
					Peer peerToDelete = listOfPeers.get(index);
					listOfPeers.remove(peerToDelete);
					System.out.println("Deleted!");
					return true;
				}
			}
		}
	}
	
	public Peer locatePeer(String hostName){
		//looks up for the given peer. if exists, returns the peer else return null
		
		for(Peer p : listOfPeers){
			if(p.getHostName().equalsIgnoreCase(hostName)){
				return p;
			}
		}
		return null;
	}
	
	//Uncomment when required
	
/*	public void printPeers(){
 	//prints all peers in the arraylist
 	 
		for(Peer p : listOfPeers){
			System.out.println(p);
		}
	}*/
}