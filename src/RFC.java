
public class RFC implements Comparable<RFC>{
	
	int rfc_num;
	String title;
	Peer peer;
	
	public RFC(){
		this.rfc_num = -1;
		this.title = "";
		this.peer = new Peer();
	}
	
	public RFC(int num, String title, String host_name)
	{
		this.rfc_num = num;
		this.title = title;
		//this.host_name = host_name;
		this.peer = PeerManager.getPeerManager().locatePeer(host_name);
	}

	@Override
	public int compareTo(RFC other) {
		if(this.rfc_num == other.rfc_num)// && title.toLowerCase().compareTo(other.title)==0)//Check title and RFC number
			return 0;
		
		return -1;
	}
	
	@Override
	public String toString()
	{
		return "RFC "+rfc_num+" "+title+" "+peer;
	}
	


}
