import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class RequestP2P {
	
	HashMap<String,String> headers;
	
	String method;
	int rfcNum;
	String version;

	public RequestP2P(DataInputStream dis)
	{
		headers = new HashMap<>();
		
		try {
			parseFirstLine(dis);
			while(parseLine(dis));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public RequestP2P()
	{
		headers = new HashMap<>();
	}
	
	public static RequestP2P createRequest(int rfcNum)
	{
		RequestP2P req = new RequestP2P();
		
		req.method = "GET";
		req.rfcNum = rfcNum;
		req.version = Utils.getVersionString();
		
		req.addHeaderField("Host", Utils.getIPAddr());
		req.addHeaderField("OS", Utils.getOS());
		
		return req;
	}
	
	public void sendRequest(DataOutputStream dos)
	{
		try
		{
			dos.writeBytes(method+" RFC "+rfcNum+" "+version+"\r\n");
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
	
	private void parseFirstLine(DataInputStream dis) throws IOException
	{
		String line = dis.readLine();
		String[] tokens = line.split(" ");
		this.method = tokens[0];
		//Ignore tokens[1]="RFC"
		this.rfcNum = Integer.parseInt(tokens[2]);
		this.version = tokens[3];
	}
	
	private boolean parseLine(DataInputStream dis) throws IOException
	{
		String line = dis.readLine();
		
		if(line.compareTo("")==0 || line==null)
			return false;
		
		String tokens[] = line.split(": ");
		headers.put(tokens[0], tokens[1]);
		return true;
		
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
