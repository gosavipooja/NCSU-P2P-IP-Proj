import java.io.DataInputStream;
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
}
