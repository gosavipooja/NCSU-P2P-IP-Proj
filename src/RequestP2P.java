import java.util.*;

public class RequestP2P {
	
	HashMap<String,String> headers;
	
	String method;
	int rfcNum;
	String version;

	public RequestP2P(Scanner sc)
	{
		headers = new HashMap<>();
		
		parseFirstLine(sc);
		
		while(parseLine(sc));
	}
	
	private void parseFirstLine(Scanner sc)
	{
		this.method = sc.next();
		sc.next();//Ignore "RFC"
		this.rfcNum = sc.nextInt();
		this.version = sc.nextLine();
	}
	
	private boolean parseLine(Scanner sc)
	{
		String line = sc.nextLine();
		
		if(line.compareTo("")==0 || line==null)
			return false;
		
		String tokens[] = line.split(": ");
		headers.put(tokens[0], tokens[1]);
		return true;
		
	}
}
