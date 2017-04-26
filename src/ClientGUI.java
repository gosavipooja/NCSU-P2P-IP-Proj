import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ClientGUI {
	
	private JTextField textInput;
	private  JProgressBar progressBar;
	private JPanel listPanel;
	
	public ClientGUI()
	{
		JFrame guiFrame = new JFrame();
		
		//Frame init
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Example GUI");
        guiFrame.setSize(500,250);
        guiFrame.setLocationRelativeTo(null);
        guiFrame.getContentPane().setLayout(new BoxLayout(guiFrame.getContentPane(),BoxLayout.Y_AXIS));
        
        //Search Panel init
        JPanel panel = new JPanel();
        panel.setVisible(true);
        JLabel searchLabel = new JLabel("Enter RFC number");
        textInput = new JTextField(10);
        JButton searchButton =  new JButton("Search");
        searchButton.addActionListener(new SearchButtonAction());
        JButton listAllBtn = new JButton("List All");
        
        panel.add(searchLabel);
        panel.add(textInput);
        panel.add(searchButton);
        panel.add(listAllBtn);
        
        //Progress Panel init
        JPanel progressPanel = new JPanel();
        progressBar = new JProgressBar(0,100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressPanel.add(progressBar);
        progressPanel.setLayout(new GridLayout(0, 1));
        
        listPanel = new JPanel();
        String listStrings[] = {"Hello","How are you?"};
        JList<String> list = new JList<String>(listStrings);
        listPanel.add(list);
        listPanel.setLayout(new GridLayout(0, 1));
        JButton downloadBtn = new JButton("Download");
//        listPanel.add(downloadBtn);
        
        //Add everything to the frame
        guiFrame.add(panel);
        guiFrame.add(listPanel);
        guiFrame.add(downloadBtn);
        guiFrame.add(progressPanel);
        guiFrame.setVisible(true);
        guiFrame.pack();
	}
	
	private class SearchButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			FileDownloadClient fdc = FileDownloadClient.getInstance();
			
			boolean status = false;
			try 
			{
				int n = Integer.parseInt(textInput.getText());
				status = fdc.requestFileDownload(n, "");
			} 
			catch(NumberFormatException e)
			{
				
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			if(status)
			{
				System.out.println("Download Success!");
				progressBar.setValue(100);
			}
			else
				System.out.println("Download Failed!");
		}
		
	}
	
	public static void main2(String args[])
	{
		new ClientGUI();
	}

}
