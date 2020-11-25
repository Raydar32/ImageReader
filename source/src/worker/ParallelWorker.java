package worker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

//'---------------------------------------------------------------------------------------
//' Class     : ParallelWorker
//' Author    : Mini Alessandro (7060381)
//' Purpose   : This  class divides the work in tasks and it starts the fork-join paradigm.
//				this is the core of the program.
//'--------------------------------------------------------------------------------------- 




public class ParallelWorker {	
	private static String dir;
	private static List<Image> immagini;
	private static File[] images;
	private static int numCore = Utils.getProcessorCoreCount();
	

	// '---------------------------------------------------------------------------------------
	// ' Method  : overrideCoreNum
	// ' Purpose : This method is made in case that the user will change the number of the 
	//			   cores to use, this is used for experimental reasons.
	// '---------------------------------------------------------------------------------------
	public void overrideCoreNum(int newNum) {
		ParallelWorker.numCore = newNum;

	}

	
	// '---------------------------------------------------------------------------------------
	// ' Method  : ParallelWorker
	// ' Purpose : This is the constructor of the class, used only to populate the private 
	//			   variables, here we have:
	//						- Dir:		local dir path that contains the images.
	//						- immagini: shared buffer of images.
	//						- images:   images seen on a operating-system level (paths).
	// '---------------------------------------------------------------------------------------
	public ParallelWorker(String dir, List<Image> immagini, File[] images) {
		ParallelWorker.dir = dir;
		ParallelWorker.immagini = immagini;
		ParallelWorker.images = images;
	}

	
	// '---------------------------------------------------------------------------------------
	// ' Method  : start
	// ' Purpose : This method will divide the work and start the fork-join paradigm, this is the
	//			   core of the whole program, so it will be heavily commented.
	// '---------------------------------------------------------------------------------------
	public void start() {		
		//Base case: sequential program (only 1 core will be used), the work is divided manually.
		if (numCore == 1) {
			loadTask sequentialTask = new loadTask(0, immagini, images, 0, Utils.countFiles(dir));
			long startTime = System.currentTimeMillis();
			sequentialTask.fork();
			sequentialTask.join();
			long endTime = System.currentTimeMillis();
			Utils.infoBox("Every image has been loaded in " + (endTime - startTime), "Completed.");
			return;
		}
		
		// if i have more than 2 processor then i divide it automatically.	
		int totale = Utils.countFiles(dir);
		int chunk = totale / numCore; 															//	<- chunks.
		int resto = totale % numCore; 															//  <- reminder.

		// tasks array.
		ArrayList<ForkJoinTask<Boolean>> C = new ArrayList<ForkJoinTask<Boolean>>(numCore);		
	
		// Here we will divide the work.
		int threadID = 0;
		for (int i = 0; i < numCore; i++) {
			// For core 1...N-1 i assign a chunk.
			int start = i * chunk;
			int end = (i + 1) * chunk;
			if (i == numCore - 1) {													//	<- every core from 1 to n-1.
				C.add(new loadTask(threadID, immagini, images, start, end + resto));		
				threadID++;
			}
			else {
				//if N-1 then work + reminder.
				C.add(new loadTask(threadID, immagini, images, start, end));		//	<- last core takes his part
																					// 	   plus the reminder.			
				threadID++;				
			}
		}

		long startTime = System.currentTimeMillis();	    // <- Here i will start the tasks (fork)
		for (ForkJoinTask<Boolean> task : C) {
			task.fork();
		}
		
		for (ForkJoinTask<Boolean> task : C) {				// <- Here i wait the taks to end (join)
			task.join();
		}

		long endTime = System.currentTimeMillis();			// <- timing.		
		Utils.infoBox("Every image has been loaded in " + 	// <- result.
					  (endTime - startTime),
					  "Completed.");	 
	}
}
