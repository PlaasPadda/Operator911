package operator911.app;

import java.awt.EventQueue;

import com.google.gson.Gson;

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
import javax.swing.SwingUtilities;
import javax.swing.DefaultListModel;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import operator911.app.ClientApp;
import operator911.fleetgenerator.DistanceCalculator;

public class ClientUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLocationX;
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

	private static final String SERVICES = "FfHhPp";
	private float xLocation;
	private float yLocation;
	private boolean locEntered = false;
	private boolean Fshow = false;
	private boolean Hshow = false;
	private boolean Pshow = false;

	private List<Resource> fireResources = new ArrayList<>();
	private List<Resource> hospitalResources = new ArrayList<>();
	private List<Resource> policeResources = new ArrayList<>();
	
	private ClientApp client;
	private JLabel lblNewLabel;
	private JTextField txtLocationY;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	

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
		
		client = new ClientApp();

		new Thread(() -> {
	        try {
	            client.connect("localhost", 5000, message -> {
	                // Parse die incoming JSON van die server
	                Gson gson = new Gson();
	                
	                try {
	                    Resource rsrc= gson.fromJson(message, Resource.class);
	                    SwingUtilities.invokeLater(() -> {
	                    	if (rsrc.type.equals("confirm")) {
	                    		JOptionPane.showMessageDialog(ClientUI.this, "Resources Dispatched!");	
	                    		resetUI();
	                    	} else {
	                    		
								System.out.println("Services: " + rsrc.id);
								System.out.println("X: " + rsrc.x);
								System.out.println("Y: " + rsrc.y);
								
								// Calculate die distance
								double distance = DistanceCalculator.calculate(xLocation, yLocation, rsrc.x, rsrc.y);
								double rounded = Math.round(distance * 100.0) / 100.0;
								// Format the entry to display in the JList
								String entry = "ID: " + rsrc.id + " | Type: " + rsrc.type + " | Distance: " + rounded;

								// Add to the correct JList based on first character of id
								if (rsrc.id != null && !rsrc.id.isEmpty() && rsrc.available == true) {
									char firstChar = rsrc.id.charAt(0);

									if (firstChar == 'F') {
										addToList(listFire, entry, rsrc, fireResources);
									} else if (firstChar == 'H') {
										addToList(listHospital, entry, rsrc, hospitalResources);
									} else if (firstChar == 'P') {
										addToList(listPolice, entry, rsrc, policeResources);
									}
								}	                        
	                    	}
	                    });
	                } catch (Exception e) {
	                    // Not a JSON message, handle as plain text
	                    System.out.println("Server says: " + message);
	                }
	            });
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }).start();
        
        
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
		
		txtLocationX = new JTextField();
		txtLocationX.setHorizontalAlignment(SwingConstants.CENTER);
		txtLocationX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Clear text wanneer geclick word
				txtLocationX.setText("");
			}
		});
		txtLocationX.setText("x");
		txtLocationX.setToolTipText("(x,y)");
		txtLocationX.setBounds(372, 45, 31, 21);
		contentPane.add(txtLocationX);
		txtLocationX.setColumns(10);

		txtLocationY = new JTextField();
		txtLocationY.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtLocationY.setText("");
			}
		});
		txtLocationY.setHorizontalAlignment(SwingConstants.CENTER);
		txtLocationY.setToolTipText("(x,y)");
		txtLocationY.setText("y");
		txtLocationY.setColumns(10);
		txtLocationY.setBounds(416, 45, 31, 21);
		contentPane.add(txtLocationY);
		
		JButton btnLocation = new JButton("Go");
		btnLocation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isNumeric(txtLocationX.getText()) && isNumeric(txtLocationY.getText())) {
					xLocation = Float.parseFloat(txtLocationX.getText());
					yLocation = Float.parseFloat(txtLocationY.getText());
					txtServices.setEditable(true);
				} 
			}
		});
		btnLocation.setBounds(500, 45, 50, 21);
		contentPane.add(btnLocation);
		
		JLabel lblServices = new JLabel("Enter The First Letters Of The Required Response Services");
		lblServices.setBounds(293, 103, 358, 17);
		contentPane.add(lblServices);
		
		listHospital = new JList();
		listHospital.setFont(new Font("Dialog", Font.BOLD, 12));
		listHospital.setBounds(317, 260, 286, 294);
		listHospital.setVisible(false);
		contentPane.add(listHospital);
		
		listFire = new JList();
		listFire.setFont(new Font("Dialog", Font.BOLD, 12));
		listFire.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFire.setBounds(12, 260, 286, 294);
		listFire.setVisible(false);
		contentPane.add(listFire);
		
		listPolice = new JList();
		listPolice.setFont(new Font("Dialog", Font.BOLD, 12));
		listPolice.setBounds(623, 260, 286, 294);
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
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (Fshow == true) {
					int selectedIndex = listFire.getSelectedIndex();

					if (selectedIndex != -1) {
						Resource selected = fireResources.get(selectedIndex);
						System.out.println("Selected: " + selected.id);
						sendRequest(selected.id);
					}
				}

				if (Hshow == true) {
					int selectedIndex = listHospital.getSelectedIndex();

					if (selectedIndex != -1) {
						Resource selected = hospitalResources.get(selectedIndex);
						System.out.println("Selected: " + selected.id);
						sendRequest(selected.id);
					}
				}

				if (Pshow == true) {
					int selectedIndex = listPolice.getSelectedIndex();

					if (selectedIndex != -1) {
						Resource selected = policeResources.get(selectedIndex);
						System.out.println("Selected: " + selected.id);
						sendRequest(selected.id);
					}
				}
			}
			
		    private Gson gson = new Gson();
		    
		    private void sendRequest(String id) {
				Request rqst = new Request("request", "", id, 0, 0);
				
				String json = gson.toJson(rqst);

				client.sendMessage(json);
		    	}
		    	
		});
		btnSend.setBounds(327, 582, 263, 44);
		btnSend.setVisible(false);
		contentPane.add(btnSend);	

		txtServices = new JTextField();
		txtServices.setEditable(false);
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
				sendChange();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				// Triggered when text is deleted
				System.out.println("Changed");
				handleChange();
				sendChange();
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
		            Fshow = true;
		        } else {
		            listFire.setVisible(false);
		            lblAvailableFUnits.setVisible(false);
		            lblFRank.setVisible(false);
		            Fshow = false;
		        }

		        if (text.contains("H")) {
		            listHospital.setVisible(true);
		            lblAvailableHUnits.setVisible(true);
		            lblHrank.setVisible(true);
		            Hshow = true;
		        } else {
		            listHospital.setVisible(false);
		            lblAvailableHUnits.setVisible(false);
		            lblHrank.setVisible(false);
		            Hshow = false;
		        }

		        if (text.contains("P")) {
		            listPolice.setVisible(true);
		            lblAvailablePUnits.setVisible(true);
		            lblPrank.setVisible(true);
		            Pshow = true;
		        } else {
		            listPolice.setVisible(false);
		            lblAvailablePUnits.setVisible(false);
		            lblPrank.setVisible(false);
		            Pshow = false;
		        }
		        
		        if (text.isEmpty()) {
		        	btnSend.setVisible(false);
		        } else {
		        	btnSend.setVisible(true);
		        }

		    }
		    
		    private Gson gson = new Gson();
		    
		    private void sendChange() {
		    	String serviceText = txtServices.getText();
		    	
		    	if (serviceText.length() > 0) {
					Request rqst = new Request("info", serviceText, "", xLocation, yLocation);
					
					String json = gson.toJson(rqst);

					client.sendMessage(json);
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
		
		lblNewLabel = new JLabel("(");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		lblNewLabel.setBounds(361, 37, 9, 33);
		contentPane.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel(",");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 20));
		lblNewLabel_1.setBounds(406, 37, 9, 33);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel(")");
		lblNewLabel_2.setFont(new Font("Dialog", Font.BOLD, 20));
		lblNewLabel_2.setBounds(449, 37, 9, 33);
		contentPane.add(lblNewLabel_2);
		
	}
	
	public static boolean isNumeric(String str) {
	    if (str == null || str.isEmpty()) return false;
	    try {
	        Float.parseFloat(str);
	        return true;
	    } catch (NumberFormatException e) {
			// 1. Print full stack trace (includes line number and error location)
			e.printStackTrace(); 

			// 2. Print just the exception name and message
			System.out.println("Exception occurred: " + e.toString());
			return false;
		}
	}
	
	private void addToList(JList list, String entry, Resource rsrc, List<Resource> resources) {
	    DefaultListModel model;

	    if (list.getModel() instanceof DefaultListModel) {
	        model = (DefaultListModel) list.getModel();
	    } else {
	        model = new DefaultListModel();
	        list.setModel(model);
	    }

	    // Only add if the resource is not already in the array
	    boolean alreadyExists = false;
	    for (Resource r : resources) {
	        if (r.id.equals(rsrc.id)) {
	            alreadyExists = true;
	            break;
	        }
	    }

	    if (!alreadyExists) {
	        model.addElement(entry);
	        resources.add(rsrc);
	    } // ← keeps indexes in sync with the JList
	}
	
	private void resetUI() {

    	btnSend.setVisible(false);
        lblPrank.setVisible(false);
        lblHrank.setVisible(false);
        lblFRank.setVisible(false);
        lblAvailablePUnits.setVisible(false);
        lblAvailableHUnits.setVisible(false);
        lblAvailableFUnits.setVisible(false);
        listPolice.setVisible(false);
        listFire.setVisible(false);
        listHospital.setVisible(false);
		txtServices.setText("");
		txtServices.setEditable(false);
		fireResources = new ArrayList<>();
		hospitalResources = new ArrayList<>();
		policeResources = new ArrayList<>();
		
		DefaultListModel model = (DefaultListModel) listFire.getModel();
		model.removeAllElements(); // or model.clear();
		
		model = (DefaultListModel) listHospital.getModel();
		model.removeAllElements(); // or model.clear();
		
		model = (DefaultListModel) listPolice.getModel();
		model.removeAllElements(); // or model.clear();

	}
	
}
