import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

public class ResponseP2P {
	HashMap<String,String> headers;
	
	String version;
	int status;
	String phrase;
	String data;
	
	public ResponseP2P(DataInputStream dis)
	{
		headers = new HashMap<>();
		
		try 
		{
			parseFirstLine(dis);
			while(parseLine(dis));
			
			int len = Integer.parseInt( headers.get("Content-Length") );
			byte b[] = new byte[len];
			int n = dis.read(b);
			this.data = new String(b,"UTF-8");
		}
		catch (IOException e) 
		{
			System.err.println("Error occured while parsing response");
			e.printStackTrace();
		}
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
}
