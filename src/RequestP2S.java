import java.io.DataInputStream;
import java.io.IOException;

public class RequestP2S extends RequestP2P {
	String type;
	String version;
	RFC rfc;
	
	public RequestP2S(DataInputStream dis) throws IOException {
		super(dis);
	}

}
