import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

public class RequestP2S {
	
	HashMap<String, String> headers;
		
	String type;
	String version;
	RFC rfc;
	String method;
	
	
	
	public RequestP2S() {
		headers = new HashMap<>();
	}

	public RequestP2S(DataInputStream dis) throws IOException {
		headers = new HashMap<>();
		
		RFC rfc = parseFirstLine(dis);
		while(parseLine(dis,rfc));
	}
	
	public RFC parseFirstLine(DataInputStream dis){
		String line = "";
		try {
			line = dis.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] tokens = line.split(" ");
		this.type = tokens[0];
		//Ignore tokens[1]="RFC"
		RFC rfc = new RFC();
		rfc.rfc_num = Integer.parseInt(tokens[2]);
		this.version = tokens[3];
		return rfc;
	}
	
	public boolean parseLine(DataInputStream dis, RFC rfc){
		
		String line = "";
		try {
			line = dis.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(line.compareTo("")==0 || line==null)
			return false;
		
		
		String tokens[] = line.split(": ");
		
		headers.put(tokens[0], tokens[1]);
		if(tokens[0].equalsIgnoreCase("Host")){
			rfc.peer.setHostName(tokens[1]);
		}else {
			rfc.peer.setPortNumber(Integer.parseInt(tokens[1]));
		}
		return true;
	}
}
