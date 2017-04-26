import java.util.*;

public class RfcManager {

	private ArrayList<RFC> rfc_list;
	
	public ArrayList<RFC> getRfc_list() {
		return rfc_list;
	}

	public void setRfc_list(ArrayList<RFC> rfc_list) {
		this.rfc_list = rfc_list;
	}

	public RfcManager()
	{
		rfc_list = new ArrayList<RFC>();
	}
	
	public static RfcManager getRfcManager()
	{
		if(instance == null) instance = new RfcManager();
		return instance;
	}
	
	/*
	 * Find RFC by number
	 */
	public ArrayList<Peer> findRFC(int rfc_num)
	{
		ArrayList<Peer> peerList = new ArrayList<>(); 
		
		for(RFC r : this.rfc_list)
		{
			if(r.rfc_num == rfc_num)
				peerList.add(r.peer);
		}
		
		return peerList;
	}
	
	private static RfcManager instance;

	/*
	 * Find RFC by title 
	 */
	public ArrayList<Peer> findRFC(String title) {
		
		ArrayList<Peer> peerList = new ArrayList<>();

		for (RFC r : this.rfc_list) 
		{
			if (title.compareToIgnoreCase(r.title) == 0)
				peerList.add(r.peer);
		}

		return peerList;
	}
	
	public void addRfc(int num, String title, String hostname)
	{
		RFC r = new RFC(num,title,hostname);
		rfc_list.add(r);
		
		PeerManager.getPeerManager().addPeer(r.peer.getHostName(), r.peer.getPortNumber());
	}
	
	public void addRfc(RFC r)
	{
		if(!rfc_list.contains(r))
			rfc_list.add(r);
	}
	
	private int getIndex(int rfc_num)
	{
		for(int i=0;i<rfc_list.size();i++)
		{
			if(rfc_list.get(i).rfc_num == rfc_num)
				return i;
		}
		
		return -1;
	}

	public void deleteRfc(int rfc_num) {

		int index = getIndex(rfc_num);
		
		while(index > 0) {
			rfc_list.remove(index);
		}
	}
}
