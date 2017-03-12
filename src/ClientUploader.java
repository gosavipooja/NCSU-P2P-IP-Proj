import java.io.*;

public class ClientUploader {
	
	private static ClientUploader instance;
	
	private ClientUploader()
	{
		
	}
	
	public static ClientUploader getCleintUploader()
	{
		if(instance==null) instance = new ClientUploader();
		return instance;
	}

	private void initialize()
	{
		
	}
	
	public ResponseP2P generateResponse(RequestP2P req, DataOutputStream dos)
	{
		ResponseP2P resp;
		File file = getRFCFile(req);

		//Validate the request
		if(!req.method.equalsIgnoreCase("GET"))
			resp = ResponseP2P.createResponse(400);//Bad request as method is not supported
		
		else if(!Utils.isVersionSupported(req.version))
			resp = ResponseP2P.createResponse(505);//Send 505 Version not supported error
		
		else if (!file.exists()) 
			resp = ResponseP2P.createResponse(404);//send 404 Not Found
		
		else 
		{
			//Create new ResponseP2P object with 200 OK status 
			resp = ResponseP2P.createResponse(200);
			
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
		
		resp.sendResponse(dos);
		
		return resp;
	}
	
	private File getRFCFile(RequestP2P req)
	{
		return new File("RFC/RFC "+req.rfcNum+".txt");
	}
}
