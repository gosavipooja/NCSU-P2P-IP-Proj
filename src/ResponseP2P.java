import java.io.*;
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
	
	public ResponseP2P(DataInputStream dis, DataOutputStream dos)
	{
		headers = new HashMap<>();
		
		try 
		{
			parseFirstLine(dis);
			while(parseLine(dis));
			if(status == 200)//No data when error response is sent
				pushData(dis,dos);
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
	
	public static ResponseP2P createResponseHeaders(int statusCode)
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
	
	public void pushData(DataInputStream dis, DataOutputStream dos) throws IOException
	{
		System.out.println("Using optimized code");
		
		int orig_len = Integer.parseInt( headers.get("Content-Length") );
		
		int rem_len = orig_len;
		
		byte b[] = new byte[Math.min(Constants.TRANSFER_CHUNK_SIZE, orig_len)];
		
		while(rem_len>=Constants.TRANSFER_CHUNK_SIZE)
		{
			int n = dis.read(b);
			dos.write(b);
			rem_len -= n;
		}
		
		if(rem_len>0)
		{
			byte b1[] = new byte[rem_len];
			int n = dis.read(b1);
			dos.write(b1);
		}
		
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
	
	public static ResponseP2P createResponse(RequestP2P req)
	{
		ResponseP2P resp;
		File file = getRFCFile(req);

		//Validate the request
		if(!req.method.equalsIgnoreCase("GET"))
			resp = ResponseP2P.createResponseHeaders(400);//Bad request as method is not supported
		
		else if(!Utils.isVersionSupported(req.version))
			resp = ResponseP2P.createResponseHeaders(505);//Send 505 Version not supported error
		
		else if (!file.exists()) 
			resp = ResponseP2P.createResponseHeaders(404);//send 404 Not Found
		
		else 
		{
			//Create new ResponseP2P object with 200 OK status 
			resp = ResponseP2P.createResponseHeaders(200);
			
			resp.addHeaderField("Last-Modified",Utils.getLastModified(file));
			resp.addHeaderField("Content-Length", file.length()+"");
			resp.addHeaderField("Content-Type", "text/plain");
			
			try 
			{
				DataInputStream dis = new DataInputStream(new FileInputStream(file));
				resp.getData(dis);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		return resp;
	}
	
	private static File getRFCFile(RequestP2P req)
	{
		return new File("RFC/RFC "+req.rfcNum+".txt");
	}
}
