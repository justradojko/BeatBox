import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.sound.midi.*;

import java.util.*;
import java.awt.event.*;
import java.io.*;

public class BeatBox {
	JPanel mainPanel;
	ArrayList<JCheckBox> checkBoxList;
	Sequence sequence;
	Sequencer sequencer;
	Track track;
	JFrame theFrame;
	
	String[] instrumentNameStrings = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustinc Snare", 
			"Crash Cymbal", "Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whitsle", "Low Conga", "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"};
	int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};
	
	public static void main(String[] args){
		BeatBox beatBox = new BeatBox();
		beatBox.buildGUI();
	} 
	
	public void buildGUI(){
		theFrame = new JFrame("Beat Box");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setResizable(false);
		
		BorderLayout layout = new BorderLayout();
		
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		theFrame.add(background);
		
		checkBoxList = new ArrayList<JCheckBox>();
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		
		JButton start = new JButton("Start");
		start.addActionListener(new MyStartListener());
		buttonBox.add(start);
		
		JButton stop = new JButton("Stop");
		stop.addActionListener(new MyStopListener());
		buttonBox.add(stop);
		
		JButton upTempo = new JButton("Tempo up");
		upTempo.addActionListener(new MyUpTempoListener());
		buttonBox.add(upTempo);
		
		JButton downTempo = new JButton("Tempo down");
		downTempo.addActionListener(new MyDownTempoListener());
		buttonBox.add(downTempo);
		
		JButton savePattern = new JButton("Save Beat");
		savePattern.addActionListener(new MySendListener());
		buttonBox.add(savePattern);
		
		JButton loadPattern = new JButton("Load Beat");
		loadPattern.addActionListener(new MyReadInListener());
		buttonBox.add(loadPattern);
		
		JButton clearSelectionButton = new JButton("Clear selection");
		clearSelectionButton.addActionListener(new MyClearSelectionLister());
		clearSelectionButton.setBorder(new EmptyBorder(10, 0, 0, 0));
		buttonBox.add(clearSelectionButton);
		
		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for (String instrument : instrumentNameStrings) {
			nameBox.add(new Label(instrument));
		}
		nameBox.setBorder(new EmptyBorder(5, 0, 0, 0));
		
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		
		GridLayout grid = new GridLayout(16, 16);
		JPanel miniPanel = new JPanel(grid);
		
		for (int i = 0; i < 256; i++) {
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			miniPanel.add(c);
			checkBoxList.add(c);
		}
		
		background.add(BorderLayout.CENTER, miniPanel);
		
		setUpMidi();
		
		theFrame.setSize(600, 500);
		theFrame.setVisible(true);
	}
	
	public void setUpMidi(){
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(120);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buildTrackAndStart(){
		int[] trackList = null;
		
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		
		for (int i = 0; i < 16; i++) {
			trackList = new int[16];
			int key = instruments[i];
			
			for (int j = 0; j < 16; j++) {
				JCheckBox jc = (JCheckBox) checkBoxList.get(j + (16 * i));
				if (jc.isSelected()) {
					trackList[j] = key;
				} else {
					trackList[j] = 0;
				}
			}
			
			makeTracks(trackList);
			track.add(makeEvent(176, 1, 127, 0, 16));
		}
		
		track.add(makeEvent(192, 9, 1, 0, 15));
		try {
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
			sequencer.start();
			sequencer.setTempoInBPM(120);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public class MyStartListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			buildTrackAndStart();
		}
	}
	
	public class MyStopListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			sequencer.stop();
		}
	}
	
	public class MyUpTempoListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * 1.03));
		}
	}
	
	public class MyDownTempoListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float) (tempoFactor * .97));
		}
	}	
	
	public class MyClearSelectionLister implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			for (JCheckBox checkBoxTemp : checkBoxList) {
				checkBoxTemp.setSelected(false);
			}
		}
	}	
	
	public class MySendListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			boolean[] checkboxStates = new boolean[256];
			
			for (int i = 0; i < checkBoxList.size(); i++) {				
				checkboxStates[i] = (boolean)checkBoxList.get(i).isSelected();
			}
			
			try{
				FileOutputStream fileStream = new FileOutputStream(new File("checkbox.ser"));
				ObjectOutputStream os = new ObjectOutputStream(fileStream);
				os.writeObject(checkboxStates);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public class MyReadInListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			boolean[] checboxStates = null;
			
			try {
				FileInputStream fileStream = new FileInputStream(new File("checkbox.ser"));
				ObjectInputStream is = new ObjectInputStream(fileStream);
				checboxStates = (boolean[]) is.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < checboxStates.length; i++) {
				if (checboxStates[i]) {
					checkBoxList.get(i).setSelected(true);
				} else {
					checkBoxList.get(i).setSelected(false);
				}
			}
			
			sequencer.stop();
			buildTrackAndStart();
		}
	}

	
	
	public void makeTracks(int[] list){
		for (int i = 0; i < 16; i++) {
			int key = list[i];
			
			if (key != 0) {
				track.add(makeEvent(144, 9, key, 100, i));
				track.add(makeEvent(128, 9, key, 100, i+1));
			}
		}
	}
	
	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick){
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return event;
	}
}










