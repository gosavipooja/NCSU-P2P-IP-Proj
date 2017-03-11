import java.util.*;

public class PeerManager {
	
	private List<Peer> listOfPeers;
	
	public PeerManager(){
		listOfPeers = new ArrayList<Peer>();
	}
	
	public void addPeer(Peer p){
		//Adds a peer to the list
		if(listOfPeers == null){
			listOfPeers = new ArrayList<Peer>();
		}
		listOfPeers.add(p);
		System.out.println("Added!");
	}
	
	public boolean deletePeer(Peer p){
		//Deletes a peer from the list
		if(listOfPeers == null)	return false;
		else if(listOfPeers.size() == 0) return false;
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
	public int locatePeer(Peer p){
		//looks up for the given peer. if exists, return the port number else return -1
		int index = listOfPeers.indexOf(p);
		if(index == -1){
			System.out.println("Not Found!");
			return -1;
		}else{
			Peer foundPeer = listOfPeers.get(index);
			return foundPeer.getPortNumber();
		}
	}
	
}