import java.io.File;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

public class Utils {
	
	public static final float VERSION = 1.0f;
	
	public static String getIPAddr()
	{
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isVersionSupported(String ver)
	{
		String t[] = ver.split("/");
		return VERSION >= Float.parseFloat(t[1]);
	}
	
	public static String getOS()
	{
		return System.getProperty("os.name");
	}
	
	public static String getLastModified(File f)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		return sdf.format(f.lastModified()).toString();
	}
	
	public static String getVersionString()
	{
		return "P2P-CI/"+VERSION;
	}

}
