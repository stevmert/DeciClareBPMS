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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import model.ValidationStatus;
import model.constraint.ParsingCache;
import model.constraint.ResourceConstraint;
import model.constraint.existence.ActivityAvailabilitySchedule;
import model.constraint.relation.AtMostLag;
import model.data.BooleanDataAttribute;
import model.data.CategoricalDataAttribute;
import model.data.DataAttribute;
import model.resource.Resource;
import model.resource.ResourceRole;

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
			new DeciClareEngineGUI(readModel($fileChooser.getSelectedFiles()[0])).start();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Reading model", "Error reading file!", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	private static ArrayList<Constraint> readModel(File file) throws IOException {
		ParsingCache pc = new ParsingCache();
		ArrayList<Constraint> model = new ArrayList<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while(line != null) {
				try {
					model.add(Constraint.parseConstraint(line, pc));
				} catch(Exception e) {
					e.printStackTrace();
				}
				line = reader.readLine();
			}
		} finally {
			if(reader != null)
				reader.close();
		}
		return model;
	}

	private ArrayList<Constraint> model;
	private long currentTime;
	private Trace trace;
	private ArrayList<Activity> activities;
	private JPanel tracePanel;
	private JPanel activityPanel;
	private JTextField timeField;
	private ArrayList<JComboBox<String>> boolDataBoxesMain;
	private ArrayList<JCheckBox> catDataBoxesMain;
	private ArrayList<JComboBox<String>> boolDataBoxesPopup;
	private ArrayList<JCheckBox> catDataBoxesPopup;
	private boolean doDataActions;
	private JButton curRestrBut;
	private String currentRestrictions;

	public DeciClareEngineGUI(ArrayList<Constraint> model) {
		super("DeciClareEngine");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.model = model;
		this.currentTime = 0;
		trace = new Trace();
		activities = getActivities();
		doDataActions = true;
	}

	public void start() {
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
		JPanel butP = new JPanel();
		butP.setLayout(new BoxLayout(butP, BoxLayout.X_AXIS));
		//				JButton refreshBut = new JButton("Refresh");
		//				refreshBut.addActionListener(new ActionListener() {
		//					@Override
		//					public void actionPerformed(ActionEvent arg0) {
		//						refreshActs(activityPanel);
		//					}
		//				});
		//				butP.add(refreshBut);
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
		timeP.add(new JLabel("Current time:"));
		timeField = new JTextField(""+currentTime, 5);
		timeField.setEditable(false);
		timeP.add(timeField);
		JButton timeBut = new JButton("Change");
		timeBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String ans = JOptionPane.showInputDialog(DeciClareEngineGUI.this, "Enter the new time:", "DeciClareEngine - Time", JOptionPane.PLAIN_MESSAGE);
				if(ans != null && ans.trim().length() > 0)
					try {
						long newCurrentTime = Long.parseLong(ans);
						long minTime = Math.max(trace.getActivityEvents().isEmpty()?0:trace.getActivityEvents().get(trace.getActivityEvents().size()-1).getEnd(),
								trace.getDataEvents().isEmpty()?0:trace.getDataEvents().get(trace.getDataEvents().size()-1).getTime());
						if(minTime > newCurrentTime)
							JOptionPane.showMessageDialog(DeciClareEngineGUI.this, "Invalid input: " + ans
									+ "\nThe current time has to be at least " + minTime,
									"DeciClareEngine - Time", JOptionPane.ERROR_MESSAGE);
						else {
							currentTime = newCurrentTime;
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
		timeField.setText(currentTime+"");
		if(refreshActs)
			refreshActs(activityPanel);
	}

	private void addTrace(JPanel p) {
		tracePanel = new JPanel();
		tracePanel.setLayout(new BoxLayout(tracePanel, BoxLayout.Y_AXIS));
		refreshTrace(false);
		p.add(tracePanel);
	}

	private void refreshTrace(boolean refreshActs) {
		tracePanel.removeAll();
		if(trace.getActivityEvents().isEmpty())
			addTextLine(tracePanel, "No Executed Activities");
		else {
			addTextLine(tracePanel, "Executed Activities:");
			for(ActivityEvent e : trace.getActivityEvents())
				addTextLine(tracePanel, " - " + e.toString());
		}
		addTextLine(tracePanel, " ");
		if(trace.getDataEvents().isEmpty())
			addTextLine(tracePanel, "No Data Events");
		else {
			addTextLine(tracePanel, "Data Events:");
			for(DataEvent e : trace.getDataEvents())
				addTextLine(tracePanel, " - " + e.toString());
		}
		addTextLine(tracePanel, " ");
		if(trace.getResourceEvents().isEmpty())
			addTextLine(tracePanel, "No Resource Events");
		else {
			addTextLine(tracePanel, "Resource Events:");
			for(ResourceEvent e : trace.getResourceEvents())
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
		for(DataAttribute da : getDataAttributes()) {
			JPanel lineP = new JPanel();
			lineP.add(new JLabel(da.getName() + ": "));
			p.add(lineP);
			if(da instanceof BooleanDataAttribute) {
				JComboBox<String> comBox = new JComboBox<>(new String[]{"", "true", "false"});
				comBox.addActionListener(new ActionListener () {
					public void actionPerformed(ActionEvent e) {
						if(doDataActions) {
							String selected = (String) comBox.getSelectedItem();
							if(selected.length() == 0)
								addDataEvent((BooleanDataAttribute) da, null, time==null?currentTime:time, cachedDataEvents);
							else if(selected.equals("true"))
								addDataEvent((BooleanDataAttribute) da, true, time==null?currentTime:time, cachedDataEvents);
							else
								addDataEvent((BooleanDataAttribute) da, false, time==null?currentTime:time, cachedDataEvents);
						}
					}
				});
				lineP.add(comBox);
				boolDataBoxes.add(comBox);
			} else {
				CategoricalDataAttribute cda = (CategoricalDataAttribute) da;
				for(String v : cda.getValues()) {
					JCheckBox cb = new JCheckBox(v, false);
					cb.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent ae) {
							if(doDataActions)
								addDataEvent(cda, cb.getText(), cb.isSelected(), time==null?currentTime:time, cachedDataEvents);
						}
					});
					lineP.add(cb);
					catDataBoxes.add(cb);
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

	protected void addDataEvent(BooleanDataAttribute da, Boolean value, long time, ArrayList<DataEvent> cachedDataEvents) {
		ArrayList<DataEvent> tmp;
		ArrayList<DataEvent> searchList;
		if(cachedDataEvents == null) {
			tmp = trace.getDataEvents();
			searchList = trace.getDataEvents();
		} else {
			tmp = cachedDataEvents;
			searchList = new ArrayList<>(trace.getDataEvents());
			searchList.addAll(cachedDataEvents);
		}
		DataEvent same = null;
		for(int i = searchList.size()-1; i >=0; i--) {
			DataEvent de = searchList.get(i);
			if(de.getTime() >= time
					&& de.getDataElement() instanceof BooleanDataAttribute
					&& de.getDataElement().getName().equals(da.getName())) {
				same = de;
				break;
			}
		}
		DataEvent prev = null;
		for(int i = searchList.size()-1; i >=0; i--) {
			DataEvent de = searchList.get(i);
			if(de.getTime() < time
					&& de.getDataElement() instanceof BooleanDataAttribute
					&& de.getDataElement().getName().equals(da.getName())) {
				prev = de;
				break;
			}
		}
		if(same != null) {
			if(value != null
					&& ((BooleanDataAttribute) same.getDataElement()).getValue() == value
					&& same.isActivated())
				return;
			tmp.remove(same);
			if(value == null && (prev == null || !prev.isActivated())
					|| (!same.isActivated() && value != null && ((BooleanDataAttribute) same.getDataElement()).getValue() == value)) {
				if(cachedDataEvents == null)
					refreshTrace(true);
				return;
			}
		}
		if(prev == null) {
			if(value == null)
				return;
			if(da.getValue() == value)
				tmp.add(getDataEvent(da, true, time));
			else
				tmp.add(getDataEvent(new BooleanDataAttribute(da.getName(), value, da.getParent()), true, time));
		} else {
			boolean isActivation = true;
			boolean val = ((BooleanDataAttribute) prev.getDataElement()).getValue();
			if(value == null)
				isActivation = false;
			else
				val = value;
			if(((BooleanDataAttribute) prev.getDataElement()).getValue() == val
					&& prev.isActivated() == isActivation)
				;//do nothing
			else if(da.getValue() == val)
				tmp.add(getDataEvent(da, isActivation, time));
			else
				tmp.add(getDataEvent(new BooleanDataAttribute(da.getName(), val, da.getParent()), isActivation, time));
		}
		Collections.sort(tmp);
		if(cachedDataEvents == null)
			refreshTrace(true);
	}

	protected void addDataEvent(CategoricalDataAttribute cda, String value, boolean isActivated, long time, ArrayList<DataEvent> cachedDataEvents) {
		ArrayList<DataEvent> tmp;
		if(cachedDataEvents == null)
			tmp = trace.getDataEvents();
		else
			tmp = cachedDataEvents;
		DataEvent same = null;
		for(int i = tmp.size()-1; i >=0; i--) {
			DataEvent de = tmp.get(i);
			if(de.getTime() >= time
					&& de.getDataElement() instanceof CategoricalDataAttribute
					&& de.getDataElement().getName().equals(cda.getName())
					&& ((CategoricalDataAttribute) de.getDataElement()).getValue().equals(value)) {
				same = de;
				break;
			}
		}
		if(same != null) {
			if(same.isActivated() == isActivated)
				return;
			tmp.remove(same);
		} else if(cda.getValue().equals(value))
			tmp.add(getDataEvent(cda, isActivated, time));
		else
			tmp.add(getDataEvent(new CategoricalDataAttribute(cda.getName(), cda.getValues(), value, cda.getParent()), isActivated, time));
		Collections.sort(tmp);
		refreshTrace(true);
	}

	public void refreshActs(JPanel p) {
		if(p == null)
			return;
		HashMap<Resource, Integer> resourceUsage = getResourceUsage();
		ArrayList<Activity> sectionA_satisfaction = new ArrayList<>();
		ArrayList<Activity> sectionB_possibleActivitySatisfaction = new ArrayList<>();
		ArrayList<Activity> sectionC_timeViolation = new ArrayList<>();
		ArrayList<Activity> sectionD_violation = new ArrayList<>();
		HashMap<Activity, String> explanations = new HashMap<>();
		HashSet<Activity> deadlines = new HashSet<>();
		HashSet<Activity> delays = new HashSet<>();
		HashSet<Constraint> deadendConstraints = new HashSet<>();
		for(Activity a : activities) {
			ArrayList<ActivityEvent> potentialTraceActs = new ArrayList<>(trace.getActivityEvents());
			potentialTraceActs.add(getActivityEvent(a, currentTime));
			ArrayList<ResourceEvent> potentialResourceEvents = new ArrayList<>(trace.getResourceEvents());//TODO: use...
			Trace potentialTrace = new Trace(potentialTraceActs, trace.getDataEvents(), potentialResourceEvents);
			ArrayList<Constraint> generalViolations = new ArrayList<>();
			ArrayList<Constraint> timeViolations = new ArrayList<>();
			ArrayList<Constraint> possibleActivityViolations = new ArrayList<>();
			HashMap<Constraint, Long> deadlinesA = new HashMap<>();
			HashMap<Constraint, Long> delaysA = new HashMap<>();
			for(Constraint c : model) {
				if(!(c instanceof ResourceConstraint//TODO: add support...
						|| c instanceof ActivityAvailabilitySchedule
						|| c instanceof AtMostLag)) {
					ValidationStatus status = c.validate(potentialTrace, resourceUsage, currentTime);
					//TODO: what if constraint is optional???
					if(status.equals(ValidationStatus.VIOLATED))
						generalViolations.add(c);
					else if(status.equals(ValidationStatus.TIME_SATISFIABLE)) {
						timeViolations.add(c);
						delaysA.put(c, status.getBound());
					} else if(status.equals(ValidationStatus.ACTIVITY_SATISFIABLE)) {
						possibleActivityViolations.add(c);
						//TODO: random generation attempt to find working finished trace?
						//		-> warning if no, but keep on list...
						//		-> stop generation attempt when one trace has been found
						//		-> brute force with incremental length?
						//		-> put in separate section? =section of next-activities that are uncertain to lead to finishable trace
					} else if(status.equals(ValidationStatus.ACTIVITY_SATISFIABLE_WITH_DEADLINE)) {
						possibleActivityViolations.add(c);
						deadlinesA.put(c, status.getBound());
					} else if(status.equals(ValidationStatus.DEADEND))
						deadendConstraints.add(c);
				}
			}
			if(!generalViolations.isEmpty()) {
				sectionD_violation.add(a);
				explanations.put(a, makeText("'" + a + "' is not allowed as next activity because:", generalViolations));
			} else if(!timeViolations.isEmpty()) {
				sectionC_timeViolation.add(a);
				if(!deadlinesA.isEmpty())
					deadlines.add(a);
				if(!delaysA.isEmpty())
					delays.add(a);
				explanations.put(a, makeText("'" + a + "' is currently not allowed as next activity, but will be in the near future, because:", timeViolations, delaysA, deadlinesA));
			} else if(!possibleActivityViolations.isEmpty()) {
				sectionB_possibleActivitySatisfaction.add(a);
				if(!deadlinesA.isEmpty())
					deadlines.add(a);
				if(!delaysA.isEmpty())
					delays.add(a);
				explanations.put(a, makeText("'" + a + "' is allowed as next activity, but with additional future requirements, because:", possibleActivityViolations, delaysA, deadlinesA));
			} else//TODO: add deadlines
				sectionA_satisfaction.add(a);
		}
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
			addSection("sectionC_timeViolation", p);
			for(Activity a : sectionC_timeViolation) {
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
			addSection("sectionD_violation", p);
			for(Activity a : sectionD_violation) {
				JButton b = new JButton(a.toString());
				b.setEnabled(false);
				p.add(b);
				JButton bInfo = getExplButton(a, explanations.get(a));
				p.add(bInfo);
			}
			if(sectionA_satisfaction.size() + sectionB_possibleActivitySatisfaction.size() + sectionC_timeViolation.size() == 0) {
				if(currentRestrictions == null)
					JOptionPane.showMessageDialog(this, "Congradulations!\n\nThe process ended successfully.",
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
							+ "\n" + currentRestrictions);
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
					d.setLocationRelativeTo(null);
					d.setVisible(true);
				}
			}
		} else {
			addSection("DEADEND IN EXECUTION", p);
			JButton b = new JButton("Deadend");
			b.setEnabled(false);
			p.add(b);
			JButton bInfo = getExplButton(null, makeText("Deadend because", new ArrayList<>(deadendConstraints)));
			p.add(bInfo);
		}
		//finish GUI
		pack();
		setLocationRelativeTo(null);
	}

	private String makeText(String header, ArrayList<Constraint> constraints, HashMap<Constraint, Long> delays, HashMap<Constraint, Long> deadlines) {
		String res = header + "\n";
		if(delays != null)
			for(Constraint c : delays.keySet()) {
				if(deadlines != null && deadlines.containsKey(c))
					res += "\n!DELAY(" + delays.get(c) + ") AND DEADLINE(" + deadlines.get(c) + ")! " + c;
				else
					res += "\n!DELAY(" + delays.get(c) + ")! " + c;
			}
		if(deadlines != null)
			for(Constraint c : deadlines.keySet())
				if(delays == null || !delays.containsKey(c))
					res += "\n!DEADLINE(" + deadlines.get(c) + ")! " + c;
		for(Constraint c : constraints)
			if((delays == null || !delays.containsKey(c))
					&& (deadlines == null || !deadlines.containsKey(c)))
				res += "\n" + c;
		return res;
	}

	private String makeText(String header, ArrayList<Constraint> constraints) {
		return makeText(header, constraints, null, null);
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
		JPanel dataP = new JPanel();
		JPanel butP = new JPanel();
		durP.setLayout(new FlowLayout(FlowLayout.LEFT));
		dataP.setLayout(new BoxLayout(dataP, BoxLayout.Y_AXIS));
		butP.setLayout(new FlowLayout(FlowLayout.CENTER));
		cont.add(durP, BorderLayout.NORTH);
		JScrollPane scrollP = new JScrollPane(dataP);
		cont.add(scrollP, BorderLayout.CENTER);
		cont.add(butP, BorderLayout.SOUTH);
		durP.add(new JLabel("Activity duration:"));
		JTextField durF = new JTextField("2", 5);
		durP.add(durF);
		ArrayList<DataEvent> cachedDataEvents = new ArrayList<>();
		addData(dataP, currentTime+1, cachedDataEvents);//TODO: currentTime+duration-1?
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
							trace.getActivityEvents().add(getActivityEvent(a, duration));
							trace.getDataEvents().addAll(cachedDataEvents);
							currentTime += duration;
							currentRestrictions = restrictions;
							refreshTime(false);
							refreshTrace(false);
							refreshData(false);
							refreshActs(activityPanel);
							boolDataBoxesPopup = null;
							catDataBoxesPopup = null;
							d.setVisible(false);
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
		d.setLocationRelativeTo(null);
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
				d.setLocationRelativeTo(null);
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
				Trace t;
				if(a == null)
					t = trace;
				else {
					ArrayList<ActivityEvent> potentialTraceActs = new ArrayList<>(trace.getActivityEvents());
					potentialTraceActs.add(getActivityEvent(a, currentTime));
					ArrayList<ResourceEvent> potentialResourceEvents = new ArrayList<>(trace.getResourceEvents());
					t = new Trace(potentialTraceActs, trace.getDataEvents(), potentialResourceEvents);
				}
				JDialog d = new JDialog(DeciClareEngineGUI.this, "Relevant model" + (a==null?"":("for '" + a + "'")), true);
				d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				Container cont = d.getContentPane();
				JPanel textP = new JPanel();
				textP.setLayout(new FlowLayout(FlowLayout.LEFT));
				cont.add(textP, BorderLayout.CENTER);
				JPanel butP = new JPanel();
				butP.setLayout(new FlowLayout(FlowLayout.CENTER));
				cont.add(butP, BorderLayout.SOUTH);
				JTextArea txt = new JTextArea(getModelText(getRelevantModel(t)));
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
				d.setLocationRelativeTo(null);
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
				String curRestr = "";
				for(Constraint c : model) {
					if(!(c instanceof ResourceConstraint
							|| c instanceof ActivityAvailabilitySchedule
							|| c instanceof AtMostLag)) {
						ValidationStatus status = c.validate(trace, null, currentTime);
						if(status.equals(ValidationStatus.VIOLATED)
								|| status.equals(ValidationStatus.TIME_SATISFIABLE)
								|| status.equals(ValidationStatus.DEADEND)) {
							curRestr = "VIOLATION FOUND!\n\n" + c.toString();
							break;
						} else if(status.equals(ValidationStatus.ACTIVITY_SATISFIABLE))
							curRestr += "\n" + c.toString();
						else if(status.equals(ValidationStatus.ACTIVITY_SATISFIABLE_WITH_DEADLINE))
							curRestr += "\nDEADLINE(" + status.getBound() + ") " + c.toString();
					}
				}
				JTextArea txt = new JTextArea(curRestr.trim());
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
				d.setLocationRelativeTo(null);
				d.setVisible(true);
			}
		});
		return b;
	}

	private void setSize(Window w, Container c) {
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

	protected ArrayList<Constraint> getRelevantModel(Trace t) {
		ArrayList<Constraint> relModel = new ArrayList<>();
		for(Constraint c : model)
			if(c.isActivated(t))
				relModel.add(c);
		return relModel;
	}

	protected String getModelText(ArrayList<Constraint> model) {
		String res = "";
		for(Constraint c : model)
			res += "\n" + c.toString();
		return res.trim();
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

	private ArrayList<DataAttribute> getDataAttributes() {
		HashSet<String> names = new HashSet<>();
		ArrayList<DataAttribute> res = new ArrayList<>();
		for(Constraint c : model)
			for(DataAttribute da : c.getUsedDataAttributes())
				if(names.add(da.getName()))
					res.add(da);
		Collections.sort(res);
		return res;
	}

	private ArrayList<Activity> getActivities() {
		HashSet<Activity> tmp = new HashSet<>();
		for(Constraint c : model)
			for(Activity a : c.getUsedActivities())
				tmp.add(a);
		ArrayList<Activity> res = new ArrayList<>(tmp);
		Collections.sort(res);
		return res;
	}

	private ActivityEvent getActivityEvent(Activity a, long duration) {
		return new ActivityEvent(a.getName(), currentTime, currentTime+duration);
	}

	private DataEvent getDataEvent(DataAttribute da, boolean isActivated, long time) {
		return new DataEvent(da, isActivated, time);
	}

	private static HashMap<Resource, Integer> getResourceUsage() {
		HashMap<Resource, Integer> resourceUsages = new HashMap<>();
		ResourceRole nurse_scrub = new ResourceRole("scrub nurse");
		ResourceRole nurse_or = new ResourceRole("OR nurse", nurse_scrub);
		ResourceRole nurse = new ResourceRole("nurse", nurse_or);
		resourceUsages.put(nurse, 0);
		ResourceRole surgeon = new ResourceRole("orthopedic surgeon");
		ResourceRole anesthesiologist = new ResourceRole("anesthesiologist");
		ResourceRole erDoc = new ResourceRole("emergency doctor");
		ResourceRole doctor = new ResourceRole("doctor", surgeon, anesthesiologist, erDoc);
		resourceUsages.put(doctor, 0);
		ResourceRole receptionist = new ResourceRole("receptionist");
		resourceUsages.put(receptionist, 0);
		ResourceRole surgicalAss = new ResourceRole("surgical assistant");
		resourceUsages.put(surgicalAss, 0);
		ResourceRole receptiondesk = new ResourceRole("reception desk");
		resourceUsages.put(receptiondesk, 0);
		ResourceRole examroom = new ResourceRole("examination room");
		resourceUsages.put(examroom, 0);
		ResourceRole xRayroom = new ResourceRole("X-ray room");
		resourceUsages.put(xRayroom, 0);
		ResourceRole cTroom = new ResourceRole("CT room");
		resourceUsages.put(cTroom, 0);
		ResourceRole or = new ResourceRole("operating room");
		resourceUsages.put(or, 0);
		ResourceRole patientRoom = new ResourceRole("patient room");
		resourceUsages.put(patientRoom, 0);
		ResourceRole patientbed = new ResourceRole("patient bed");
		resourceUsages.put(patientbed, 0);
		ResourceRole recoveryRoom = new ResourceRole("recovery room");
		resourceUsages.put(recoveryRoom, 0);
		ResourceRole recoverybed = new ResourceRole("recovery bed");
		resourceUsages.put(recoverybed, 0);
		return resourceUsages;
	}
}