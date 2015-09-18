import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.sound.midi.*;

import java.util.*;
import java.awt.event.*;

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
//		start.addActionListener(new MyStartListener());
		buttonBox.add(start);
		
		JButton stop = new JButton("Stop");
//		stop.addActionListener(new MyStopListener());
		buttonBox.add(stop);
		
		JButton upTempo = new JButton("Tempo up");
//		upTempo.addActionListener(new MyUpTempoListener());
		buttonBox.add(upTempo);
		
		JButton downTempo = new JButton("Tempo down");
//		downTempo.addActionListener(new MyDownTempoListener());
		buttonBox.add(downTempo);
		
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
		}
		
		background.add(BorderLayout.CENTER, miniPanel);
		
		theFrame.setSize(600, 500);
		theFrame.setVisible(true);
	}
}
