package worker;

import java.io.File;
import java.util.List;
import java.util.concurrent.RecursiveTask;

//'---------------------------------------------------------------------------------------
//' Class     : loadTask
//' Author    : Mini Alessandro (7060381)
//' Purpose   : This  class defines a fork-join paradigm "Task" element. a Task will 
//				load in memory some images in a shared buffer, each task will load these 
//				images according to a range.
//				every range (or subset) is disjoint from the others.
//'--------------------------------------------------------------------------------------- 




public class loadTask extends RecursiveTask<Boolean> {	
	private static final long serialVersionUID = 1L;
	private int threadID;
	private List<Image> immagini;
	private int startIndex;
	private int endIndex;
	private File[] baseFolder;
	

	// '---------------------------------------------------------------------------------------
	// ' Method  : loadTask
	// ' Purpose : Constructor of the Loadtask class, it will set the private variables in order
	//			   to be used later on.
	// '---------------------------------------------------------------------------------------
	public loadTask(int threadID, List<Image> immagini, File[] baseFolder, int startIndex, int endIndex) {
		this.threadID = threadID;
		this.immagini = immagini;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.baseFolder = baseFolder;

	}
	

	// '---------------------------------------------------------------------------------------
	// ' Method  : compute
	// ' Purpose : Method inherited from RecursiveTask<Boolean>, this method will do the work.
	//			   we use a for loop with a base index and an end index, it will load images in
	//			   the shared buffer held by "immagini" variable.
	// '---------------------------------------------------------------------------------------
	@Override
	protected Boolean compute() {
		for (int k = startIndex; k < endIndex; k++) {
			this.immagini.set(k, new Image(baseFolder[k].getAbsolutePath()));
		}
		System.out.println("Thread " + threadID + " Finito");		//Dbg string.
		return true;
	}

}
