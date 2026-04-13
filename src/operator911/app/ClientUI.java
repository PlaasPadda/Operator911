package operator911.app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Window.Type;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.Font;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class ClientUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLocation;
	private JTextField txtServices;
	private JList listHospital;
	private JList listFire;
	private JList listPolice;
	private JLabel lblAvailableFUnits;
	private JLabel lblFRank;
	private JLabel lblAvailableHUnits;
	private JLabel lblHrank;
	private JLabel lblAvailablePUnits;
	private JLabel lblPrank;
	private JButton btnSend;
	public static final String SERVICES = "FfHhPp";
	public boolean Fshow = false;
	public boolean Hshow = false;
	public boolean Pshow = false;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientUI frame = new ClientUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientUI() {
		setBackground(new Color(119, 118, 123));
		setTitle("ClientUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 921, 684);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLocation = new JLabel("Enter Location");
		lblLocation.setBounds(406, 12, 86, 17);
		lblLocation.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(lblLocation);
		
		txtLocation = new JTextField();
		txtLocation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Clear text wanneer geclick word
				txtLocation.setText("");
			}
		});
		txtLocation.setText("(x,y)");
		txtLocation.setToolTipText("(x,y)");
		txtLocation.setBounds(357, 45, 114, 21);
		contentPane.add(txtLocation);
		txtLocation.setColumns(10);
		
		JButton btnLocation = new JButton("Go");
		btnLocation.setBounds(483, 45, 50, 21);
		contentPane.add(btnLocation);
		
		JLabel lblServices = new JLabel("Enter The First Letters Of The Required Response Services");
		lblServices.setBounds(293, 103, 358, 17);
		contentPane.add(lblServices);
		
		listHospital = new JList();
		listHospital.setBounds(327, 260, 263, 294);
		listHospital.setVisible(false);
		contentPane.add(listHospital);
		
		listFire = new JList();
		listFire.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFire.setBounds(30, 260, 263, 294);
		listFire.setVisible(false);
		contentPane.add(listFire);
		
		listPolice = new JList();
		listPolice.setBounds(628, 260, 263, 294);
		listPolice.setVisible(false);
		contentPane.add(listPolice);
		
		lblAvailableFUnits = new JLabel("Available Fire Units");
		lblAvailableFUnits.setFont(new Font("Dialog", Font.BOLD, 20));
		lblAvailableFUnits.setBounds(60, 212, 203, 17);
		lblAvailableFUnits.setVisible(false);
		contentPane.add(lblAvailableFUnits);
		
		lblFRank = new JLabel("(Ranked Closest to Furthest)");
		lblFRank.setBounds(73, 231, 174, 17);
		lblFRank.setVisible(false);
		contentPane.add(lblFRank);
		
		lblAvailableHUnits = new JLabel("Available Hospital Units");
		lblAvailableHUnits.setFont(new Font("Dialog", Font.BOLD, 20));
		lblAvailableHUnits.setBounds(340, 212, 261, 17);
		lblAvailableHUnits.setVisible(false);
		contentPane.add(lblAvailableHUnits);
		
		lblHrank = new JLabel("(Ranked Closest to Furthest)");
		lblHrank.setBounds(366, 231, 174, 17);
		lblHrank.setVisible(false);
		contentPane.add(lblHrank);
		
		lblAvailablePUnits = new JLabel("Available Police Units");
		lblAvailablePUnits.setFont(new Font("Dialog", Font.BOLD, 20));
		lblAvailablePUnits.setBounds(650, 212, 235, 17);
		lblAvailablePUnits.setVisible(false);
		contentPane.add(lblAvailablePUnits);
		
		lblPrank = new JLabel("(Ranked Closest to Furthest)");
		lblPrank.setBounds(669, 231, 174, 17);
		lblPrank.setVisible(false);
		contentPane.add(lblPrank);
		
		btnSend = new JButton("Dispatch Units");
		btnSend.setBounds(327, 582, 263, 44);
		btnSend.setVisible(false);
		contentPane.add(btnSend);	

		txtServices = new JTextField();
		txtServices.setHorizontalAlignment(SwingConstants.CENTER);
		txtServices.setText("FHP");
		txtServices.setBounds(500, 141, 114, 21);
		contentPane.add(txtServices);
		txtServices.setColumns(10);
		txtServices.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				// Triggered when text is added
				System.out.println("changed");
				handleChange();

			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				// Triggered when text is deleted
				System.out.println("Changed");
				handleChange();
			}
			
		    @Override
		    public void changedUpdate(DocumentEvent e) {
		        // Not used for JTextField (plain text), but must be implemented
		    }
		    
		    private void handleChange() {
		        String text = txtServices.getText();

		        if (text.contains("F")) {
		            listFire.setVisible(true);
		            lblAvailableFUnits.setVisible(true);
		            lblFRank.setVisible(true);
		        } else {
		            listFire.setVisible(false);
		            lblAvailableFUnits.setVisible(false);
		            lblFRank.setVisible(false);
		        }

		        if (text.contains("H")) {
		            listHospital.setVisible(true);
		            lblAvailableHUnits.setVisible(true);
		            lblHrank.setVisible(true);
		        } else {
		            listHospital.setVisible(false);
		            lblAvailableHUnits.setVisible(false);
		            lblHrank.setVisible(false);
		        }

		        if (text.contains("P")) {
		            listPolice.setVisible(true);
		            lblAvailablePUnits.setVisible(true);
		            lblPrank.setVisible(true);
		        } else {
		            listPolice.setVisible(false);
		            lblAvailablePUnits.setVisible(false);
		            lblPrank.setVisible(false);
		        }
		        
		        if (text.isEmpty()) {
		        	btnSend.setVisible(false);
		        } else {
		        	btnSend.setVisible(true);
		        }

		    }

		});

		txtServices.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
		        char keyChar = e.getKeyChar();
		        // Capitalize elke letter
		        if (Character.isLowerCase(keyChar)) {
		            e.setKeyChar(Character.toUpperCase(keyChar));
		        }
		        
		        if (!(SERVICES.contains(String.valueOf(keyChar)))) {
		        	e.consume();
		        }
			}
		});
		txtServices.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtServices.setText("");
			}
		});

		
		JLabel lblFireLetter = new JLabel("F -> FIre Department ");
		lblFireLetter.setForeground(new Color(119, 118, 123));
		lblFireLetter.setBounds(327, 125, 134, 17);
		contentPane.add(lblFireLetter);
		
		JLabel lblHospitalLetter = new JLabel("H -> Hospital");
		lblHospitalLetter.setForeground(new Color(119, 118, 123));
		lblHospitalLetter.setBounds(327, 143, 98, 17);
		contentPane.add(lblHospitalLetter);
		
		JLabel lblPoliceLetter = new JLabel("P -> Police Department");
		lblPoliceLetter.setForeground(new Color(119, 118, 123));
		lblPoliceLetter.setBounds(327, 163, 144, 17);
		contentPane.add(lblPoliceLetter);
		
		

	}
}
