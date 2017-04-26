public class Peer {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + portNumber;
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
		Peer other = (Peer) obj;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		if (portNumber != other.portNumber)
			return false;
		return true;
	}
	private String hostName;
	private int portNumber;
	
	public Peer(String hostName, int portNumber) {
		this.hostName = hostName;
		this.portNumber = portNumber;
	}
	public Peer() {
		// TODO Auto-generated constructor stub
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	@Override
	public String toString() {
		return hostName +" "+ portNumber;
	}
	
}