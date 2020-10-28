package worker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;


public class ParallelWorker {
	
	private static String dir;
	private static List<Image> immagini;
	private static File[] images;
	private static int numCore = Utils.getProcessorCoreCount() -1;

	public void overrideCoreNum(int newNum) {
		ParallelWorker.numCore = newNum -1 ;

	}

	public ParallelWorker(String dir, List<Image> immagini, File[] images) {
		ParallelWorker.dir = dir;
		ParallelWorker.immagini = immagini;
		ParallelWorker.images = images;

	}

	public void start() {

		
		//Caso base: 1 processore
		if (numCore == 0) {
			loadTask sequentialTask = new loadTask(0, immagini, images, 0, Utils.countFiles(dir));
			long startTime = System.currentTimeMillis();
			sequentialTask.fork();
			sequentialTask.join();
			long endTime = System.currentTimeMillis();
			Utils.infoBox("Every image has been loaded in " + (endTime - startTime), "Completed.");
			return;
		}
		
		//Caso base: 2 processori
		if (numCore == 1) {
			loadTask sequentialTask1 = new loadTask(0, immagini, images, 0, Utils.countFiles(dir)/2);
			//System.out.println("Assegno: " + 0 + " " + 0 + " " + Utils.countFiles(dir)/2);
			loadTask sequentialTask2 = new loadTask(1, immagini, images, Utils.countFiles(dir)/2 , Utils.countFiles(dir));
			//System.out.println("Assegno: " + 1 + " " + Utils.countFiles(dir)/2 + " " +Utils.countFiles(dir));
			long startTime = System.currentTimeMillis();
			sequentialTask1.fork();
			sequentialTask2.fork();
			sequentialTask1.join();
			sequentialTask2.join();
			long endTime = System.currentTimeMillis();
			Utils.infoBox("Every image has been loaded in " + (endTime - startTime), "Completed.");
			return;
		}
		

		// Se ho > 2 processori:		
		// Questo perchè assegno n-1 chunk agli n-1 processori ed il chunk n-esimo
		// Che conterrà al più |chunk| elementi al processore n-esimo.
		int totale = Utils.countFiles(dir);
		int chunk = totale / numCore; // Gli N-1 chunks
		int resto = totale % numCore; // il resto

		// Array di fork-join
		ArrayList<ForkJoinTask<Boolean>> C = new ArrayList<ForkJoinTask<Boolean>>(numCore);
	
		// Questa procedura divide gli indici in chunks e li assegna ai vari task
		int threadID = 0;
		for (int i = 0; i < numCore; i++) {
			// Per gli N-1 core: assegno un chunk.
			int start = i * chunk;
			int end = (i + 1) * chunk;
			C.add(new loadTask(threadID, immagini, images, start, end));
			//System.out.println("Assegno: " + threadID + " " + start + " " + end);
			threadID++;
			// All'ultimo core, assegno l'ultimo chunk (resto)
			if (i == numCore - 1) {
				C.add(new loadTask(threadID, immagini, images, start, end + resto));
				//System.out.println("Assegno: " + threadID + " " + end + " " + (end + resto));
				threadID++;
			}
		}

		// Avvio i task ed eseguo
		long startTime = System.currentTimeMillis();
		for (ForkJoinTask<Boolean> task : C) {
			task.fork();
		}

		for (ForkJoinTask<Boolean> task : C) {
			task.join();
		}

		long endTime = System.currentTimeMillis();

		// toast con risultato.
		Utils.infoBox("Every image has been loaded in " + (endTime - startTime), "Completed.");
	}
}
