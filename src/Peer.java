public class Peer {
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