import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.transaction.xa.XAResource;

import dao.CarInfoDao;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import to.CarInfoTo;

import javax.swing.JPanel;

public class Load {

	private JFrame frame;
	/**
	 * @wbp.nonvisual location=-17,14
	 */
	private final JPanel panel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Load window = new Load();
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
	public Load() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(135, 206, 250));
		frame.setSize(1300, 850);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setTitle("Automatic Number Plate Recognition System");
		// frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFileChooser jfc = new JFileChooser();
		frame.getContentPane().setLayout(null);
		JButton btnselect = new JButton("Select Image");
		btnselect.setBounds(13, 5, 172, 33);
		btnselect.setFont(new Font("Times New Roman", Font.BOLD, 20));
		frame.getContentPane().add(btnselect);

		JLabel plateno = new JLabel();
		plateno.setBounds(858, 122, 123, 24);
		plateno.setVerticalAlignment(SwingConstants.TOP);
		plateno.setHorizontalAlignment(SwingConstants.RIGHT);
		plateno.setFont(new Font("Times New Roman", Font.BOLD, 20));
		plateno.setText("Plate Number:");
		JTextField jtfplatenumber = new JTextField();
		jtfplatenumber.setLocation(1076, 119);
		jtfplatenumber.setFont(new Font("Times New Roman", Font.BOLD, 20));
		jtfplatenumber.setColumns(20);
		jtfplatenumber.setSize(153, 30);
		jtfplatenumber.setBackground(new Color(255, 255, 255));
		JLabel lblDate = new JLabel("Date of Arrival");
		lblDate.setBounds(858, 175, 128, 24);
		lblDate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		JTextField jtfdate = new JTextField();
		jtfdate.setBounds(1076, 172, 153, 30);
		jtfdate.setColumns(20);
		jtfdate.setFont(new Font("Times New Roman", Font.BOLD, 20));
		java.util.Date date = new java.util.Date();
		String strDateFormat = "yyyy/MM/dd";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String formattedDate = dateFormat.format(date);
		jtfdate.setText(formattedDate);
		JLabel lblTime = new JLabel("Time of Arrival:");
		lblTime.setBounds(858, 237, 137, 24);
		lblTime.setFont(new Font("Times New Roman", Font.BOLD, 20));
		JTextField jtfTime = new JTextField();
		jtfTime.setBounds(1076, 234, 153, 30);
		strDateFormat = "hh:mm:ss";
		dateFormat = new SimpleDateFormat(strDateFormat);
		formattedDate = dateFormat.format(date);
		jtfTime.setText(formattedDate);
		jtfTime.setFont(new Font("Times New Roman", Font.BOLD, 20));
		jtfTime.setColumns(20);
		JButton btnsave = new JButton("SAVE");
		btnsave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message = "";
				String car_number = jtfplatenumber.getText().trim();
				if (car_number == null) {
					message += "The plate number must not be empty\n";
				} else {
					CarInfoTo record = new CarInfoTo();
					record.setCar_number(car_number);
					CarInfoDao action = new CarInfoDao();
					if (action.insertRecord(record)) {
						message += "New Car is added to the system";
					} else {
						message += "Insertion failure" + action.getErrormessage();
					}
				}
				JOptionPane.showMessageDialog(frame, message);
			}
		});
		btnsave.setBounds(980, 322, 123, 37);
		btnsave.setFont(new Font("Times New Roman", Font.BOLD, 24));
		JButton btnextract = new JButton("Extract Plate");
		btnextract.setBounds(642, 13, 190, 33);
		btnextract.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnextract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tesseract tesseract = new Tesseract();
				tesseract.setDatapath("C:\\Users\\HP\\Downloads\\Tess4J-3.4.8-src\\Tess4J\\tessdata");
				try {
					System.out.println(tesseract.doOCR(image));
				} catch (TesseractException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(btnextract);
		frame.getContentPane().add(plateno);
		frame.getContentPane().add(jtfplatenumber);
		frame.getContentPane().add(lblDate);
		frame.getContentPane().add(jtfdate);
		frame.getContentPane().add(lblTime);
		frame.getContentPane().add(jtfTime);
		frame.getContentPane().add(btnsave);

		JLabel lblimage = new JLabel(
				"<html>\r\n<pre style=\"font-size:60px; color:#FF0000; \">\r\nWELCOME \r\nTO \r\nAUTOMATIC \r\nNUMBER PLATE \r\nEXTRACTION SYSTEM</pre>\r\n</html>");
		lblimage.setHorizontalAlignment(SwingConstants.CENTER);
		lblimage.setVerticalAlignment(SwingConstants.TOP);
		lblimage.setBackground(Color.WHITE);
		lblimage.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblimage.setBounds(13, 85, 819, 614);
		frame.getContentPane().add(lblimage);

		btnselect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().add(jfc);
				int result = jfc.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					try {
						boolean allvalid = true;
						System.out.println("hfjklegh");
						String message = "";
						String path = jfc.getSelectedFile().getAbsolutePath().trim();
						File file = new File(path);
						if (file.exists()) {
							String allowed_extensions = "jpg,png,gif,jpeg";
							int index = path.lastIndexOf(".");
							if (index != -1) {
								String extension = path.substring(index + 1).toLowerCase();
								if (!allowed_extensions.contains(extension)) {
									message += "You can only choose a png, jpg or jpeg file.\n\n";
									allvalid = false;
								}
							} else {
								message += "Not Valid Path for Image File\n\n";
								allvalid = false;
							}
						} else {
							message += "There is No Such File Present in Hard Disk\n\n";
							allvalid = false;
						}
						if (allvalid) {
							FileInputStream fis = new FileInputStream(jfc.getSelectedFile().getAbsolutePath());
							InputStream is = fis;
							BufferedImage img = ImageIO.read(is);
							Image dimg;
							dimg = img.getScaledInstance(lblimage.getWidth(), lblimage.getHeight(), Image.SCALE_SMOOTH);
							ImageIcon icon = new ImageIcon(dimg);
							lblimage.setIcon(icon);
						} else {
							JOptionPane.showMessageDialog(frame, message);	
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// File file = new File(path);
				}
			}
		});

		// image.setSize(img.get);
		// ImageIcon icon=new ImageIcon(img);
		// image.setIcon(icon);

		// frame.getContentPane().add(image);
		// image.add(jfc);
		// frame.add(jfc);
	}
}
