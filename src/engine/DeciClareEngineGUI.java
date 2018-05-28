package engine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import miner.log.ActivityEvent;
import miner.log.DataEvent;
import miner.log.ResourceEvent;
import miner.log.Trace;
import model.Activity;
import model.Constraint;
import model.constraint.resource.AtLeastUsage;
import model.constraint.resource.AtMostUsage;
import model.data.BooleanDataAttribute;
import model.data.CategoricalDataAttribute;
import model.data.DataAttribute;
import model.resource.Resource;

public class DeciClareEngineGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		JFileChooser $fileChooser = new JFileChooser();
		$fileChooser.setMultiSelectionEnabled(true);//does not work if 'false'???
		$fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		$fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int selectionResult = $fileChooser.showOpenDialog(null);
		if(selectionResult != JFileChooser.APPROVE_OPTION
				|| $fileChooser.getSelectedFiles().length == 0)
			System.exit(0);
		if($fileChooser.getSelectedFiles().length > 1) {
			JOptionPane.showMessageDialog(null, "Reading model", "Cannot accept multiple files!", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		try {
			new DeciClareEngineGUI(DeciClareEngine.readModel($fileChooser.getSelectedFiles()[0])).start();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Reading model", "Error reading file!", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	private DeciClareEngine engine;
	private JPanel tracePanel;
	private JPanel activityPanel;
	private JLabel timeLabel;
	private ArrayList<JComboBox<String>> boolDataBoxesMain;
	private ArrayList<JCheckBox> catDataBoxesMain;
	private ArrayList<JComboBox<String>> boolDataBoxesPopup;
	private ArrayList<JCheckBox> catDataBoxesPopup;
	private boolean doDataActions;
	private JButton curRestrBut;

	public DeciClareEngineGUI(ArrayList<Constraint> model) {
		super("DeciClareEngine");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		doDataActions = true;
		this.engine = new DeciClareEngine(model);
	}

	protected void setTrace(Trace trace) {
		engine.setTrace(trace);
		refreshTime(false);
		refreshTrace(false);
		refreshData();
		refreshActs(activityPanel);
	}

	public void start() {
		changeResource();
		Container cont = getContentPane();
		cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
		JPanel mainP = new JPanel();
		mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS));
		JScrollPane scrollPaneMain = new JScrollPane(mainP);
		cont.add(scrollPaneMain);
		JPanel timeP = new JPanel();
		timeP.setLayout(new FlowLayout(FlowLayout.LEFT));
		addTime(timeP);
		mainP.add(timeP);
		JPanel resourceP = new JPanel();
		resourceP.setLayout(new FlowLayout(FlowLayout.LEFT));
		addResource(resourceP);
		mainP.add(resourceP);
		JPanel traceP = new JPanel();
		traceP.setLayout(new FlowLayout(FlowLayout.LEFT));
		addTrace(traceP);
		mainP.add(traceP);
		activityPanel = new JPanel(new GridLayout(0, 2));
		mainP.add(activityPanel);
		JPanel dataP = new JPanel();
		dataP.setLayout(new BoxLayout(dataP, BoxLayout.Y_AXIS));
		//		dataP.setAlignmentX(Component.RIGHT_ALIGNMENT);
		addData(dataP, null, null);
		mainP.add(dataP);
		//TODO: Show/change how many available resources (in general, ignoring how many in use) and how many in use. resource instances left = available - in use
		JPanel butP = new JPanel();
		butP.setLayout(new BoxLayout(butP, BoxLayout.X_AXIS));
		JButton relModBut = getRelevantModelButton(null);
		butP.add(relModBut);
		curRestrBut = getCurrentRestrictionsButton(null);
		butP.add(curRestrBut);
		cont.add(butP);
		refreshActs(activityPanel);
		setPreferredSize(new Dimension(getWidth()+19, getHeight()));
		setSize(this, scrollPaneMain);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void addTime(JPanel p) {
		JPanel timeP = new JPanel();
		timeP.setLayout(new FlowLayout(FlowLayout.LEFT));
		timeLabel = new JLabel("");
		refreshTime(false);
		timeP.add(timeLabel);
		JButton timeBut = new JButton("Change");
		timeBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String ans = JOptionPane.showInputDialog(DeciClareEngineGUI.this, "Enter the new time:", "DeciClareEngine - Time", JOptionPane.PLAIN_MESSAGE);
				if(ans != null && ans.trim().length() > 0)
					try {
						long newCurrentTime = Long.parseLong(ans);
						long minTime = Math.max(engine.getTrace().getActivityEvents().isEmpty()?0:engine.getTrace().getActivityEvents().get(engine.getTrace().getActivityEvents().size()-1).getEnd(),
								engine.getTrace().getDataEvents().isEmpty()?0:engine.getTrace().getDataEvents().get(engine.getTrace().getDataEvents().size()-1).getTime());
						if(minTime > newCurrentTime)
							JOptionPane.showMessageDialog(DeciClareEngineGUI.this, "Invalid input: " + ans
									+ "\nThe current time has to be at least " + minTime,
									"DeciClareEngine - Time", JOptionPane.ERROR_MESSAGE);
						else {
							engine.setCurrentTime(newCurrentTime);
							refreshTime(true);
						}
					} catch(NumberFormatException e) {
						JOptionPane.showMessageDialog(DeciClareEngineGUI.this, "Invalid input: " + ans,
								"DeciClareEngine - Time", JOptionPane.ERROR_MESSAGE);
					}
			}
		});
		timeP.add(timeBut);
		//		p.setAlignmentX(Component.RIGHT_ALIGNMENT);
		p.add(timeP);
	}

	private void refreshTime(boolean refreshActs) {
		timeLabel.setText("Current time: " + engine.getCurrentTime());
		if(refreshActs)
			refreshActs(activityPanel);
	}

	private void addResource(JPanel p) {
		JPanel resourceP = new JPanel();
		resourceP.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel resourceLabel = new JLabel("");
		refreshResource(resourceLabel, false);
		resourceP.add(resourceLabel);
		JButton timeBut = new JButton("Change");
		timeBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeResource();
				refreshResource(resourceLabel, true);
			}
		});
		resourceP.add(timeBut);
		p.add(resourceP);
	}

	private void refreshResource(JLabel resourceLabel, boolean refreshActs) {
		resourceLabel.setText("Current resource: " + engine.getActiveResource());
		if(refreshActs)
			refreshActs(activityPanel);
	}

	private void changeResource() {
		ArrayList<Resource> tmp = engine.getExpandedResources();
		if(tmp.isEmpty()) {
			engine.setActiveResource(null);
			return;
		}
		Resource[] options = tmp.toArray(new Resource[tmp.size()]);
		Resource ans = (Resource) JOptionPane.showInputDialog(this, "Select the active resource (=your role in the process):", "Active Resource Selection",
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if(ans == null)
			throw new RuntimeException();
		engine.setActiveResource(ans);
	}

	private void addTrace(JPanel p) {
		tracePanel = new JPanel();
		tracePanel.setLayout(new BoxLayout(tracePanel, BoxLayout.Y_AXIS));
		refreshTrace(false);
		p.add(tracePanel);
	}

	private void refreshTrace(boolean refreshActs) {
		tracePanel.removeAll();
		if(engine.getTrace().getActivityEvents().isEmpty())
			addTextLine(tracePanel, "No Executed Activities");
		else {
			addTextLine(tracePanel, "Executed Activities:");
			for(ActivityEvent e : engine.getTrace().getActivityEvents())
				addTextLine(tracePanel, " - " + e.toString());
		}
		addTextLine(tracePanel, " ");
		if(engine.getTrace().getDataEvents().isEmpty())
			addTextLine(tracePanel, "No Data Events");
		else {
			addTextLine(tracePanel, "Data Events:");
			for(DataEvent e : engine.getTrace().getDataEvents())
				addTextLine(tracePanel, " - " + e.toString());
		}
		addTextLine(tracePanel, " ");
		if(engine.getTrace().getResourceEvents().isEmpty())
			addTextLine(tracePanel, "No Resource Events");
		else {
			addTextLine(tracePanel, "Resource Events:");
			for(ResourceEvent e : engine.getTrace().getResourceEvents())
				addTextLine(tracePanel, " - " + e.toString());
		}
		if(refreshActs)
			refreshActs(activityPanel);
	}

	private void addTextLine(JPanel p, String text) {
		JLabel l = new JLabel(text);
		l.setAlignmentX(Component.LEFT_ALIGNMENT);
		p.add(l);
	}

	private void addData(JPanel p, Long time, ArrayList<DataEvent> cachedDataEvents) {
		ArrayList<JComboBox<String>> boolDataBoxes;
		ArrayList<JCheckBox> catDataBoxes;
		boolean setCatDataElems = engine.getCatDataElems() == null;
		if(setCatDataElems)
			engine.setCatDataElems(new ArrayList<>());
		boolean setBoolDataElems = engine.getBoolDataElems() == null;
		if(setBoolDataElems)
			engine.setBoolDataElems(new ArrayList<>());
		if(cachedDataEvents == null) {
			boolDataBoxesMain = new ArrayList<>();
			boolDataBoxes = boolDataBoxesMain;
			catDataBoxesMain = new ArrayList<>();
			catDataBoxes = catDataBoxesMain;
		} else {
			boolDataBoxesPopup = new ArrayList<>();
			boolDataBoxes = boolDataBoxesPopup;
			catDataBoxesPopup = new ArrayList<>();
			catDataBoxes = catDataBoxesPopup;
		}
		for(DataAttribute da : engine.getDataAttributes()) {
			JPanel lineP = new JPanel();
			JLabel lab = new JLabel(da.getName() + ": ");
			lab.setForeground(Color.BLUE);
			lineP.add(lab);
			p.add(lineP);
			if(da instanceof BooleanDataAttribute) {
				JComboBox<String> comBox = new JComboBox<>(new String[]{"", "true", "false"});
				comBox.addActionListener(new ActionListener () {
					public void actionPerformed(ActionEvent e) {
						if(doDataActions) {
							String selected = (String) comBox.getSelectedItem();
							if(selected.length() == 0)
								addDataEvent((BooleanDataAttribute) da, null, time==null?engine.getCurrentTime():time, cachedDataEvents);
							else if(selected.equals("true"))
								addDataEvent((BooleanDataAttribute) da, true, time==null?engine.getCurrentTime():time, cachedDataEvents);
							else
								addDataEvent((BooleanDataAttribute) da, false, time==null?engine.getCurrentTime():time, cachedDataEvents);
						}
					}
				});
				lineP.add(comBox);
				boolDataBoxes.add(comBox);
				if(setBoolDataElems)
					engine.getBoolDataElems().add((BooleanDataAttribute) da);
			} else {
				CategoricalDataAttribute cda = (CategoricalDataAttribute) da;
				int i = 0;
				for(String v : cda.getValues()) {
					if(i > 4) {
						lineP = new JPanel();
						p.add(lineP);
						i = -1;
					}
					JCheckBox cb = new JCheckBox(v, false);
					cb.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent ae) {
							if(doDataActions)//TODO: seems to validate in execute dialog...
								addDataEvent(cda, cb.getText(), cb.isSelected(), time==null?engine.getCurrentTime():time, cachedDataEvents);
						}
					});
					lineP.add(cb);
					catDataBoxes.add(cb);
					if(setCatDataElems)
						engine.getCatDataElems().add(cda);
					i++;
				}
			}
		}
	}

	private void refreshData(boolean fromMainToPopup) {
		doDataActions = false;
		if(fromMainToPopup) {
			for(int i = 0; i < boolDataBoxesMain.size(); i++)
				boolDataBoxesPopup.get(i).setSelectedIndex(boolDataBoxesMain.get(i).getSelectedIndex());
			for(int i = 0; i < catDataBoxesMain.size(); i++)
				catDataBoxesPopup.get(i).setSelected(catDataBoxesMain.get(i).isSelected());
		} else {
			for(int i = 0; i < boolDataBoxesPopup.size(); i++)
				boolDataBoxesMain.get(i).setSelectedIndex(boolDataBoxesPopup.get(i).getSelectedIndex());
			for(int i = 0; i < catDataBoxesPopup.size(); i++)
				catDataBoxesMain.get(i).setSelected(catDataBoxesPopup.get(i).isSelected());
		}
		doDataActions = true;
	}

	private void refreshData() {
		doDataActions = false;
		//reset all
		for(int i = 0; i < boolDataBoxesMain.size(); i++)
			boolDataBoxesMain.get(i).setSelectedIndex(0);
		for(int i = 0; i < catDataBoxesMain.size(); i++)
			catDataBoxesMain.get(i).setSelected(false);
		//get current state
		HashSet<DataAttribute> currentSituation = engine.getCurrentSituation();
		//set
		for(DataAttribute da : currentSituation) {
			if(da instanceof BooleanDataAttribute) {
				for(int i = 0; i < engine.getBoolDataElems().size(); i++)
					if(engine.getBoolDataElems().get(i).getName().equals(da.getName())) {
						boolDataBoxesMain.get(i).setSelectedIndex(((BooleanDataAttribute) da).getValue()?1:2);
						break;
					}
			} else {
				for(int i = 0; i < engine.getCatDataElems().size(); i++)
					if(engine.getCatDataElems().get(i).getName().equals(da.getName())
							&& catDataBoxesMain.get(i).getText().equals(((CategoricalDataAttribute) da).getValue())) {
						catDataBoxesMain.get(i).setSelected(true);
						break;
					}
			}
		}
		doDataActions = true;
	}

	protected void addDataEvent(BooleanDataAttribute da, Boolean value, long time, ArrayList<DataEvent> cachedDataEvents) {
		if(engine.addDataEvent(da, value, time, cachedDataEvents))
			refreshTrace(true);
	}

	protected void addDataEvent(CategoricalDataAttribute cda, String value, boolean isActivated, long time, ArrayList<DataEvent> cachedDataEvents) {
		if(engine.addDataEvent(cda, value, isActivated, time, cachedDataEvents))
			refreshTrace(true);
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Constraint> addResources(JPanel dataAndResourceP, Activity a,
			HashMap<Resource, JTextField> resourceTextFields) {
		ArrayList<? extends Constraint>[] arr = engine.getResourceSubmodel(a);
		ArrayList<Resource> rs = new ArrayList<>(engine.getResourceMapping().keySet());
		Collections.sort(rs);
		for(Resource r : rs) {
			int low = engine.getLowerUsage(r, (ArrayList<AtLeastUsage>) arr[1]);
			Integer up = engine.getUpperUsage(r, (ArrayList<AtMostUsage>) arr[2]);
			JPanel p = new JPanel();
			JLabel l = new JLabel(r + " ["
					+ low + "-" + (up==null?"infinite":up) + "]: ");
			p.add(l);
			JTextField t = new JTextField(""+low, 2);
			resourceTextFields.put(r, t);
			p.add(t);
			dataAndResourceP.add(p);
		}
		return (ArrayList<Constraint>) arr[0];
	}

	public void refreshActs(JPanel p) {
		if(p == null)
			return;
		ArrayList<Activity> sectionA_satisfaction = new ArrayList<>();
		ArrayList<Activity> sectionB_possibleActivitySatisfaction = new ArrayList<>();
		ArrayList<Activity> sectionC_resourceViolation = new ArrayList<>();
		ArrayList<Activity> sectionD_timeViolation = new ArrayList<>();
		ArrayList<Activity> sectionE_violation = new ArrayList<>();
		HashMap<Activity, String> explanations = new HashMap<>();
		HashSet<Activity> deadlines = new HashSet<>();
		HashSet<Activity> delays = new HashSet<>();
		HashSet<Constraint> deadendConstraints = new HashSet<>();
		engine.validate(sectionA_satisfaction, sectionB_possibleActivitySatisfaction, sectionC_resourceViolation,
				sectionD_timeViolation, sectionE_violation, explanations, deadlines, delays, deadendConstraints);
		p.removeAll();
		if(deadendConstraints.isEmpty()) {
			addSection("sectionA_satisfaction", p);
			for(Activity a : sectionA_satisfaction) {
				JButton b = getActButton(a);
				p.add(b);
				p.add(new JLabel(" "));
			}
			addSection("sectionB_possibleActivitySatisfaction", p);
			for(Activity a : sectionB_possibleActivitySatisfaction) {
				JButton b = getActButton(a, delays.contains(a), deadlines.contains(a),
						explanations.get(a).substring(explanations.get(a).indexOf("\n")).trim());
				p.add(b);
				JPanel ppp = new JPanel(new GridLayout(0, 2));
				JButton bInfo = getExplButton(a, explanations.get(a));//TODO: remove restrictions that already applied to the partial trace
				JButton bMod = getRelevantModelButton(a);
				ppp.add(bInfo);
				ppp.add(bMod);
				p.add(ppp);
			}
			addSection("sectionC_resourceViolation", p);
			for(Activity a : sectionC_resourceViolation) {
				JButton b = getActButton(a, delays.contains(a), deadlines.contains(a));
				b.setEnabled(false);
				p.add(b);
				JPanel ppp = new JPanel(new GridLayout(0, 2));
				JButton bInfo = getExplButton(a, explanations.get(a));
				JButton bMod = getRelevantModelButton(a);
				ppp.add(bInfo);
				ppp.add(bMod);
				p.add(ppp);
			}
			addSection("sectionD_timeViolation", p);
			for(Activity a : sectionD_timeViolation) {
				JButton b = getActButton(a, delays.contains(a), deadlines.contains(a));
				b.setEnabled(false);
				p.add(b);
				JPanel ppp = new JPanel(new GridLayout(0, 2));
				JButton bInfo = getExplButton(a, explanations.get(a));
				JButton bMod = getRelevantModelButton(a);
				ppp.add(bInfo);
				ppp.add(bMod);
				p.add(ppp);
			}
			addSection("sectionE_violation", p);
			for(Activity a : sectionE_violation) {
				JButton b = new JButton(a.toString());
				b.setEnabled(false);
				p.add(b);
				JButton bInfo = getExplButton(a, explanations.get(a));
				p.add(bInfo);
			}
			if(sectionA_satisfaction.size() + sectionB_possibleActivitySatisfaction.size() + sectionC_resourceViolation.size() + sectionD_timeViolation.size() == 0) {
				if(engine.getCurrentRestrictions() == null)
					JOptionPane.showMessageDialog(this, "Congratulations!\n\nThe process ended successfully.",
							"DeciClareEngine", JOptionPane.INFORMATION_MESSAGE);
				else {
					JDialog d = new JDialog(DeciClareEngineGUI.this, "DeciClareEngine ERROR", true);
					d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					Container cont = d.getContentPane();
					JPanel textP = new JPanel();
					textP.setLayout(new FlowLayout(FlowLayout.LEFT));
					cont.add(textP, BorderLayout.CENTER);
					JPanel butP = new JPanel();
					butP.setLayout(new FlowLayout(FlowLayout.CENTER));
					cont.add(butP, BorderLayout.SOUTH);
					JTextArea txt = new JTextArea("Ow no! The process is deadlocked!"
							+ "\nAlways check the restrictions that apply before selecting an activity to execute..."
							+ "\n\nThe following constraints cannot be satisfied anymore:"
							+ "\n" + engine.getCurrentRestrictions());
					txt.setEditable(false);
					txt.setBackground(Color.RED);
					JScrollPane scrollPane = new JScrollPane(txt);
					textP.add(scrollPane);
					JButton okBut = new JButton("OK");
					okBut.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							d.setVisible(false);
						}
					});
					butP.add(okBut);
					setSize(d, scrollPane);
					d.setLocationRelativeTo(this);
					d.setVisible(true);
				}
			}
		} else {
			addSection("DEADEND IN EXECUTION", p);
			JButton b = new JButton("Deadend");
			b.setEnabled(false);
			p.add(b);
			JButton bInfo = getExplButton(null, DeciClareEngine.makeText("Deadend because", new ArrayList<>(deadendConstraints)));
			p.add(bInfo);
		}
		//finish GUI
		pack();
		setLocationRelativeTo(this);
	}

	private JButton getActButton(Activity a) {
		return getActButton(a, false, false);
	}

	private JButton getActButton(Activity a, boolean hasDelays, boolean hasDeadlines) {
		return getActButton(a, hasDelays, hasDeadlines, null);
	}

	private JButton getActButton(Activity a, boolean hasDelays, boolean hasDeadlines, String restrictions) {
		JButton b = new JButton(a.toString()
				+ (hasDelays?" (!DELAYS!)":"") + (hasDeadlines?" (!DEADLINES!)":""));
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				executeActivityDialog(a, restrictions);
			}
		});
		return b;
	}

	protected void executeActivityDialog(Activity a, String restrictions) {
		JDialog d = new JDialog(this, "Executing '" + a + "'...", true);
		d.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowClosing(WindowEvent arg0) {
				boolDataBoxesPopup = null;
				catDataBoxesPopup = null;
			}
			@Override
			public void windowClosed(WindowEvent arg0) {}
			@Override
			public void windowActivated(WindowEvent arg0) {}
		});
		Container cont = d.getContentPane();
		JPanel durP = new JPanel();
		JPanel dataAndResourceP = new JPanel();
		JPanel butP = new JPanel();
		durP.setLayout(new FlowLayout(FlowLayout.LEFT));
		dataAndResourceP.setLayout(new BoxLayout(dataAndResourceP, BoxLayout.Y_AXIS));
		butP.setLayout(new FlowLayout(FlowLayout.CENTER));
		cont.add(durP, BorderLayout.NORTH);
		JScrollPane scrollP = new JScrollPane(dataAndResourceP);
		cont.add(scrollP, BorderLayout.CENTER);
		cont.add(butP, BorderLayout.SOUTH);
		durP.add(new JLabel("Activity duration:"));
		JTextField durF = new JTextField("2", 5);
		durP.add(durF);
		ArrayList<DataEvent> cachedDataEvents = new ArrayList<>();
		addData(dataAndResourceP, engine.getCurrentTime()+1, cachedDataEvents);
		HashMap<Resource, JTextField> resourceTextFields = new HashMap<>();
		ArrayList<Constraint> submodel = addResources(dataAndResourceP, a, resourceTextFields);
		refreshData(true);
		JButton execBut = new JButton("Execute");
		execBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String durString = durF.getText();
				if(durString.trim().length() > 0) {
					try {
						long duration = Long.parseLong(durString.trim());
						if(duration < 1)
							JOptionPane.showMessageDialog(d, "Invalid duration: " + duration,
									"DeciClareEngine", JOptionPane.ERROR_MESSAGE);
						else {
							HashMap<Resource, String> resourceTextInputs = new HashMap<>();
							for(Resource r : resourceTextFields.keySet())
								resourceTextInputs.put(r, resourceTextFields.get(r).getText().trim());
							String resourcesOk = engine.getResourceValidationString(a, restrictions,
									resourceTextInputs, duration, cachedDataEvents, submodel);
							if(resourcesOk == null) {
								refreshTime(false);
								refreshTrace(false);
								refreshData(false);
								refreshActs(activityPanel);
								boolDataBoxesPopup = null;
								catDataBoxesPopup = null;
								d.setVisible(false);
							} else
								JOptionPane.showMessageDialog(d, "Invalid resource input: '" + resourcesOk + "'",
										"DeciClareEngine", JOptionPane.ERROR_MESSAGE);
						}
					} catch(NumberFormatException e) {
						JOptionPane.showMessageDialog(d, "Invalid duration input: " + durString,
								"DeciClareEngine", JOptionPane.ERROR_MESSAGE);
					}
				} else
					JOptionPane.showMessageDialog(d, "The duration is required!",
							"DeciClareEngine", JOptionPane.ERROR_MESSAGE);
			}
		});
		JButton cancBut = new JButton("Cancel");
		cancBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				d.setVisible(false);
				boolDataBoxesPopup = null;
				catDataBoxesPopup = null;
			}
		});
		butP.add(execBut);
		butP.add(cancBut);
		setSize(d, scrollP);
		d.setSize(new Dimension(d.getWidth()+20, d.getHeight()));
		d.setLocationRelativeTo(this);
		d.setVisible(true);
	}

	private JButton getExplButton(Activity a, String explanation) {
		JButton b = new JButton("Explanation");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog d = new JDialog(DeciClareEngineGUI.this, "Explanation for " + (a==null?"DEADEND":("'" + a + "'")), true);
				d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				Container cont = d.getContentPane();
				JPanel textP = new JPanel();
				textP.setLayout(new FlowLayout(FlowLayout.LEFT));
				cont.add(textP, BorderLayout.CENTER);
				JPanel butP = new JPanel();
				butP.setLayout(new FlowLayout(FlowLayout.CENTER));
				cont.add(butP, BorderLayout.SOUTH);
				JTextArea txt = new JTextArea(explanation);
				txt.setEditable(false);
				JScrollPane scrollPane = new JScrollPane(txt);
				textP.add(scrollPane);
				JButton okBut = new JButton("OK");
				okBut.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						d.setVisible(false);
					}
				});
				butP.add(okBut);
				setSize(d, scrollPane);
				d.setLocationRelativeTo(DeciClareEngineGUI.this);
				d.setVisible(true);
			}
		});
		return b;
	}

	private JButton getRelevantModelButton(Activity a) {
		JButton b = new JButton("Relevant Model");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog d = new JDialog(DeciClareEngineGUI.this, "Relevant model" + (a==null?"":("for '" + a + "'")), true);
				d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				Container cont = d.getContentPane();
				JPanel textP = new JPanel();
				textP.setLayout(new FlowLayout(FlowLayout.LEFT));
				cont.add(textP, BorderLayout.CENTER);
				JPanel butP = new JPanel();
				butP.setLayout(new FlowLayout(FlowLayout.CENTER));
				cont.add(butP, BorderLayout.SOUTH);
				JTextArea txt = new JTextArea(engine.getRelevantModelString(a));
				txt.setEditable(false);
				JScrollPane scrollPane = new JScrollPane(txt);
				textP.add(scrollPane);
				JButton okBut = new JButton("OK");
				okBut.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						d.setVisible(false);
					}
				});
				butP.add(okBut);
				setSize(d, scrollPane);
				d.setLocationRelativeTo(DeciClareEngineGUI.this);
				d.setVisible(true);
			}
		});
		return b;
	}

	private JButton getCurrentRestrictionsButton(Object object) {
		JButton b = new JButton("Current Restrictions");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JDialog d = new JDialog(DeciClareEngineGUI.this, "Current Restrictions", true);
				d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				Container cont = d.getContentPane();
				JPanel textP = new JPanel();
				textP.setLayout(new FlowLayout(FlowLayout.LEFT));
				cont.add(textP, BorderLayout.CENTER);
				JPanel butP = new JPanel();
				butP.setLayout(new FlowLayout(FlowLayout.CENTER));
				cont.add(butP, BorderLayout.SOUTH);
				JTextArea txt = new JTextArea(engine.getCurrentRestrictionsString());
				txt.setEditable(false);
				JScrollPane scrollPane = new JScrollPane(txt);
				textP.add(scrollPane);
				JButton okBut = new JButton("OK");
				okBut.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						d.setVisible(false);
					}
				});
				butP.add(okBut);
				setSize(d, scrollPane);
				d.setLocationRelativeTo(DeciClareEngineGUI.this);
				d.setVisible(true);
			}
		});
		return b;
	}

	protected void setSize(Window w, Container c) {
		if(c == null)
			c = w;
		w.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		boolean doRepack = false;
		if(c.getWidth() > width-50) {
			c.setPreferredSize(new Dimension(((int) width)-50, c.getHeight()));
			doRepack = true;
			if(c instanceof JScrollPane)
				c.setPreferredSize(new Dimension((int) c.getPreferredSize().getWidth(), (int) c.getPreferredSize().getHeight()+20));
		}
		if(c.getHeight() > height-100) {
			c.setPreferredSize(new Dimension((int) c.getPreferredSize().getWidth(), ((int) height)-100));
			doRepack = true;
		}
		if(doRepack)
			w.pack();
		setSize(w);
	}

	private void setSize(Window w) {
		w.pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		boolean doRepack = false;
		if(w.getWidth() > width-50) {
			w.setPreferredSize(new Dimension(((int) width)-50, w.getHeight()));
			doRepack = true;
		}
		if(w.getHeight() > height-100) {
			w.setPreferredSize(new Dimension((int) w.getPreferredSize().getWidth(), ((int) height)-100));
			doRepack = true;
		}
		if(doRepack)
			w.pack();
	}

	private void addSection(String sectionName, JPanel p) {
		addSection(sectionName, p, false);
	}

	private void addSection(String sectionName, JPanel p, boolean isDeadend) {
		JPanel ppp1 = new JPanel();
		if(isDeadend)
			ppp1.setBackground(Color.RED);
		else
			ppp1.setBackground(Color.BLUE);
		JLabel l = new JLabel(sectionName);
		if(isDeadend)
			l.setForeground(Color.BLACK);
		else
			l.setForeground(Color.WHITE);
		ppp1.add(l);
		p.add(ppp1);
		JPanel ppp2 = new JPanel();
		if(isDeadend)
			ppp2.setBackground(Color.RED);
		else
			ppp2.setBackground(Color.BLUE);
		ppp1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		p.add(ppp2);
	}
}