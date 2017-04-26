
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((peer == null) ? 0 : peer.hashCode());
		result = prime * result + rfc_num;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RFC other = (RFC) obj;
		if (peer == null) {
			if (other.peer != null)
				return false;
		} else if (!peer.equals(other.peer))
			return false;
		if (rfc_num != other.rfc_num)
			return false;
		return true;
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
