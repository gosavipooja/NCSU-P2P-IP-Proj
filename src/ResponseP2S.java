import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ResponseP2S {
	
	ArrayList<RFC> listOfRFCS;
	String version;
	int status;
	String phrase;
	
	public ResponseP2S(DataInputStream dis)
	{
		listOfRFCS = new ArrayList<RFC>();
		
		try 
		{
			parseFirstLine(dis);
			while(parseLine(dis));
		}
		catch (IOException e) 
		{
			System.err.println("Error occured while parsing response");
			e.printStackTrace();
		}
	}
	
	
	public ResponseP2S() {
		listOfRFCS = new ArrayList<RFC>();
		// TODO Auto-generated constructor stub
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
		
		//while(line!=null && line!="\\s+"){
			String tokens[] = line.split(" ");
			RFC rfc = new RFC();
			rfc.rfc_num = Integer.parseInt(tokens[2]);
			String title = "";
			for(int i = 3;i < tokens.length - 2;i++){
				title = title + tokens[i]+ " ";
			}
			rfc.title = title;
			Peer peer= new Peer(tokens[tokens.length - 2],Integer.parseInt(tokens[tokens.length - 1]));
			rfc.peer = peer;
			
			listOfRFCS.add(rfc);
		//}
		return true;
	}
	
	public static ResponseP2S createResponseHeaders(int statusCode)
	{
		ResponseP2S resp = new ResponseP2S();
		
		switch(statusCode)
		{
			case 200:
				resp.status = 200;
				resp.phrase = "OK";
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

		return resp;
		
	}
	
	public static ResponseP2S createAndSendResponse(RequestP2S req, DataOutputStream sock_dos)
	{
		ResponseP2S resp;
		//Validate the request
		if(!req.type.equalsIgnoreCase("ADD") && !req.type.equalsIgnoreCase("LOOKUP") && !req.type.equalsIgnoreCase("LIST"))
			resp = ResponseP2S.createResponseHeaders(400);//Bad request as method is not supported
		
		else if(!Utils.isVersionSupported(req.version))
			resp = ResponseP2S.createResponseHeaders(505);//Send 505 Version not supported error
		
		else 
		{
			//Create new ResponseP2P object with 200 OK status 
			resp = ResponseP2S.createResponseHeaders(200);
			resp = ResponseP2S.createResponse(req);	
			
			//Send the list of files
			try 
			{
				resp.sendHeaders(sock_dos);
				resp.sendResponse(sock_dos);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			return resp;
		}
		
		resp.sendHeaders(sock_dos);
		return resp;
	}
	
	public static ResponseP2S createResponse(RequestP2S req){
		ResponseP2S resp = new ResponseP2S();
		
		if(req.type.equalsIgnoreCase("ADD"))
		{
			Peer p = new Peer(req.getHeaderField("Host"), 
					Integer.parseInt(req.getHeaderField("Port")));
			
			
			RFC rfc = new RFC();
			rfc.rfc_num = req.rfc.rfc_num;
			rfc.title = req.getHeaderField("Title");
			rfc.peer = p;
			
			//TODO: ask pooja why this was added
//			resp.listOfRFCS.add(rfc);
			
			//Add it to RFC manager
			RfcManager.getRfcManager().addRfc(rfc);
			System.out.println("Added RFC "+rfc.rfc_num+" to list");
		}
		
		else if(req.type.equalsIgnoreCase("LOOKUP"))
		{
			ArrayList<Peer> peers = RfcManager.getRfcManager().findRFC(req.rfc.rfc_num);
			for(Peer peer: peers){
				RFC rfc = new RFC();
				rfc.peer = peer;
				rfc.rfc_num = req.rfc.rfc_num;
				rfc.title = req.rfc.title;
				
				resp.listOfRFCS.add(rfc);
			}
		}
		else if(req.type.equalsIgnoreCase("LIST")){
			resp.listOfRFCS.addAll(RfcManager.getRfcManager().getRfc_list());
		}
		else{
			//Error
		}
		return resp;
	}
	
	public void sendHeaders(DataOutputStream dos)
	{
		try 
		{
			dos.writeBytes(Utils.getVersionString()+" "+status+" "+phrase+"\r\n");
			dos.writeBytes("\r\n");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void sendResponse(DataOutputStream dos){
		try{
			for(RFC rfc : listOfRFCS){
				dos.writeBytes(rfc.toString());
				dos.writeBytes("\r\n");
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
