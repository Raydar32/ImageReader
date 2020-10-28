package worker;

import java.io.File;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class loadTask extends RecursiveTask<Boolean> {
	private static final long serialVersionUID = 1L;

	private int threadID;
	private List<Image> immagini;
	private int startIndex;
	private int endIndex;
	private File[] baseFolder;

	public loadTask(int threadID, List<Image> immagini, File[] baseFolder, int startIndex, int endIndex) {
		this.threadID = threadID;
		this.immagini = immagini;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.baseFolder = baseFolder;

	}

	@Override
	protected Boolean compute() {
		for (int k = startIndex; k < endIndex; k++) {
			this.immagini.set(k, new Image(baseFolder[k].getAbsolutePath()));
			//System.out.println(
			//		"Thread " + threadID + " legge : " + baseFolder[k].getAbsolutePath() + " posizione -> " + k);
		}
		System.out.println("Thread " + threadID + " Finito");
		return true;
	}

}
