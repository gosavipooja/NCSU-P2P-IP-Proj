
public class RFC implements Comparable<RFC>{
	
	int rfc_num;
	String title;
	
	public RFC(int num, String title)
	{
		this.rfc_num = num;
		this.title = title;
	}

	@Override
	public int compareTo(RFC other) {
		if(this.rfc_num == other.rfc_num && title.toLowerCase().compareTo(other.title)==0)//Check title and RFC number
			return 1;
		
		return 0;
	}
	
	

}
