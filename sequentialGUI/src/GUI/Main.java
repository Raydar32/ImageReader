package GUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import worker.Image;
import worker.ParallelWorker;
import worker.Utils;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import javax.swing.JProgressBar;

public class Main {

	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField txtBaseFolder;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}
	
	public static void createEmptyImageArrayList(List<Image> immagini, String dir) {
		for (int i = 0; i < Utils.countFiles(dir); i++) {
			immagini.add(new Image());
		}

	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		// Building the frame
		frame = new JFrame();
		frame.setBounds(100, 100, 608, 449);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);

		// Text input box
		txtBaseFolder = new JTextField();
		txtBaseFolder.setEditable(false);
		txtBaseFolder.setBounds(29, 79, 181, 20);
		frame.getContentPane().add(txtBaseFolder);
		txtBaseFolder.setColumns(10);

		// Button to load images:
		JButton btnLoadImages = new JButton("Load");
		btnLoadImages.setBounds(224, 132, 107, 20);
		frame.getContentPane().add(btnLoadImages);
		btnLoadImages.setEnabled(false);

		// Table definition with scrolling capability
		DefaultTableModel model = new DefaultTableModel();
		scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 163, 537, 235);
		frame.getContentPane().add(scrollPane);
		table = new JTable(model);
		scrollPane.setViewportView(table);

		// Adding columns
		model.addColumn("ID");
		model.addColumn("Filename");

		// Button select folder click.
		JButton btnSelectDir = new JButton("Select Dir");
		btnSelectDir.setBounds(224, 79, 107, 20);
		frame.getContentPane().add(btnSelectDir);

		// Minor gui components
		JLabel lblNewLabel = new JLabel("Folder with images path:");
		lblNewLabel.setBounds(29, 54, 281, 14);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Image Reader");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(227, 0, 245, 28);
		frame.getContentPane().add(lblNewLabel_1);

		JButton btnHelp = new JButton("?");
		btnHelp.setBounds(418, 7, 54, 20);
		frame.getContentPane().add(btnHelp);
		
		
		//Combobox
		JComboBox<Integer> cmbCoreNumber = new JComboBox<Integer>();
		cmbCoreNumber.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"}));
		cmbCoreNumber.setBounds(29, 130, 96, 22);
		frame.getContentPane().add(cmbCoreNumber);
		
		//Number of core label
		JLabel lblCoresNumber = new JLabel("Cores number");
		lblCoresNumber.setBounds(29, 110, 281, 14);
		frame.getContentPane().add(lblCoresNumber);
				
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(392, 54, 170, 100);
		frame.getContentPane().add(panel);
		
		JLabel lblNewLabel_2 = new JLabel("Detected number of cores:");
		panel.add(lblNewLabel_2);
		
		JLabel lblThreadNumber = new JLabel("");
		lblThreadNumber.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblThreadNumber.setForeground(new Color(0, 128, 128));
		panel.add(lblThreadNumber);
		lblThreadNumber.setText(Integer.toString(Runtime.getRuntime().availableProcessors()));
		
		

		btnSelectDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.setRowCount(0); // Azzero la tabella
				JFileChooser f = new JFileChooser();
				f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				f.showSaveDialog(null);
				txtBaseFolder.setText(f.getSelectedFile().toString());
				btnLoadImages.setEnabled(true);
				
				File[] images = Utils.walkDir(f.getSelectedFile().toString()); // percorsi		
				int i = 0;
				
				for(File f1:images) {
					model.addRow(new String[] {Integer.toString(i),f1.toString()});
					i = i + 1;					
				}
				

			}
		});
		
		

				

		List<Image> immagini = Collections.synchronizedList(new ArrayList<Image>()); // oggetti immagine				
		// Button load images click.
		btnLoadImages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String dir = txtBaseFolder.getText();
				int coreNum = cmbCoreNumber.getSelectedIndex() +1;
				File[] images = Utils.walkDir(dir); // percorsi				
				createEmptyImageArrayList(immagini, dir);	
				
				Thread runForkJoinWorker = new Thread(()-> {
					ParallelWorker forkJoinWorker = new ParallelWorker(dir, immagini, images);
					forkJoinWorker.overrideCoreNum(coreNum);
					forkJoinWorker.start();
				});
			
				runForkJoinWorker.start();
				
				
			}
		});

		
		
		// click on table
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					System.out.println(table.getModel().getValueAt(row, 1));
					Image chosen =  immagini.get(row); // Visualizing the requested image.
					if(!chosen.getPercorso().equals("mock")){
						chosen.draw();
					}else {
						Utils.errorBox("The requested image has not been loaded yet.", "Error");
					}


				}
			}
		});
		

	}
}
