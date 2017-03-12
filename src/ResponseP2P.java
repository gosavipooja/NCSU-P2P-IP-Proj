import java.io.*;
import java.io.IOException;
import java.util.*;

public class ResponseP2P {
	HashMap<String,String> headers;
	
	String version;
	int status;
	String phrase;
	byte[] data;
	
	public ResponseP2P(DataInputStream dis)
	{
		headers = new HashMap<>();
		
		try 
		{
			parseFirstLine(dis);
			while(parseLine(dis));
			if(status == 200)//No data when error response is sent
				getData(dis);
		}
		catch (IOException e) 
		{
			System.err.println("Error occured while parsing response");
			e.printStackTrace();
		}
	}
	
	public ResponseP2P()
	{
		headers = new HashMap<>();
		status = 200;
		phrase = "OK";
	}
	
	public static ResponseP2P createResponse(int statusCode)
	{
		ResponseP2P resp = new ResponseP2P();
		
		switch(statusCode)
		{
			case 200:
				resp.status = 200;
				resp.phrase = "OK";
				break;
			
			case 404:
				resp.status = 404;
				resp.phrase = "Not Found";
				break;
				
			case 505:
				resp.status = 505;
				resp.phrase = "P2P-CI Version Not Supported";
				break;
				
			default:
			case 400:
				resp.status = 400;
				resp.phrase = "Bad Request";
				break;				
		}
		
		resp.addHeaderField("Data", new Date().toString());
		resp.addHeaderField("OS", Utils.getOS());
		
		return resp;
		
	}
	
	private void parseFirstLine(DataInputStream dis) throws IOException
	{
		String line = dis.readLine();
		String tokens[] = line.split(" ");
		
		version = tokens[0];
		status = Integer.parseInt(tokens[1]);
		phrase="";
		for(int i=2;i<tokens.length;i++)
			phrase += tokens[i];
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
	
	public void getData(DataInputStream dis) throws IOException
	{
		int len = Integer.parseInt( headers.get("Content-Length") );
		byte b[] = new byte[len];
		int n = dis.read(b);
		this.data = b;
	}
	
	public void addHeaderField(String k, String v)
	{
		this.headers.put(k, v);
	}
	
	public void sendResponse(DataOutputStream dos)
	{
		try 
		{
			dos.writeBytes(Utils.getVersionString()+" "+status+" "+phrase+"\r\n");
			for(String k:headers.keySet())
			{
				dos.writeBytes(k+": "+headers.get(k)+"\r\n");
			}
			
			dos.writeBytes("\r\n");
			if(status == 200)
				dos.write(data, 0, data.length);
		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
