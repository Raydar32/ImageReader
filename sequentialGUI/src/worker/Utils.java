package worker;

import java.io.File;


import javax.swing.JOptionPane;

public class Utils {
	public final static void clearConsole() {
		try {
			final String os = System.getProperty("os.name");

			if (os.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {
			// Handle any exceptions.
		}
	}

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void errorBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.ERROR_MESSAGE);
	}
	
	public static void simpleConsoleClean() {
		for (int i = 0; i < 50; ++i)
			System.out.println();
	}
	
	public static File[] walkDir(String dir){
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
		
	}
	public static int countFiles(String dir) {
		File[] list = walkDir(dir);
		return list.length;
	}
	
	public static int getProcessorCoreCount() {
		return Runtime.getRuntime().availableProcessors();
	}
	
}
