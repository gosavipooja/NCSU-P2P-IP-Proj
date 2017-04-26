import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class RequestP2S {
	
	HashMap<String, String> headers;
		
	String type;
	String version;
	RFC rfc;
	
	public RequestP2S() {
		headers = new HashMap<>();
	}

	public RequestP2S(DataInputStream dis) throws IOException {
		headers = new HashMap<>();
		
		rfc = parseFirstLine(dis);
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
	
	@SuppressWarnings("deprecation")
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
			addHeaderField("Host", tokens[1]);
		}
		else if(tokens[0].equalsIgnoreCase("Title"))
		{
			addHeaderField("Title", tokens[1]);
			rfc.title = tokens[1];
		}
		else {
			
			addHeaderField("Port", 
					tokens[1]);
			rfc.peer.setPortNumber(Integer.parseInt(tokens[1]));
		}
		return true;
	}
	
	public static RequestP2S createRequest(String requestType,int rfcNum, String title)
	{
		RequestP2S req = new RequestP2S();
				
		/*How to determine the request type*/
		
		if(requestType.equalsIgnoreCase("add")) req.type = "ADD";
		else if(requestType.equalsIgnoreCase("lookup")) req.type = "LOOKUP";
		else if(requestType.equalsIgnoreCase("list")) req.type = "LIST";
		
			
		req.addHeaderField("Host", Utils.getIPAddr());
		req.addHeaderField("Port", "5678");
		req.addHeaderField("Title", title);
		req.rfc = new RFC();
		req.rfc.rfc_num = rfcNum;
/*		req.rfc.peer.setHostName(Utils.getIPAddr());
		req.rfc.peer.setPortNumber(Integer.parseInt("5678"));*/
		req.version = Utils.getVersionString();
			
		return req;
	}

	public void sendRequest(DataOutputStream dos)
	{
		try
		{
			dos.writeBytes(type+" RFC "+rfc.rfc_num+" "+version+"\r\n");
			for(String k:headers.keySet())
			{
				dos.writeBytes(k+": "+headers.get(k)+"\r\n");
			}
			dos.writeBytes("\r\n");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getHeaderField(String k)
	{
		return this.headers.get(k);
	}
	
	public void addHeaderField(String k, String v)
	{
		this.headers.put(k, v);
	}
}
