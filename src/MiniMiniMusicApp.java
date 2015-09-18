import java.awt.BorderLayout;

import javax.sound.midi.*;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MiniMiniMusicApp {
	public static void main(String[] args){
//		MiniMiniMusicApp mini = new MiniMiniMusicApp();
//		mini.play();
		JFrame frame = new JFrame();
		
		JButton button = new JButton();
		
		frame.getContentPane().add(BorderLayout.EAST, button);
		
		frame.setSize(300, 300);
		frame.setVisible(true);
	} 
	
	public void play(){
		try {
			Sequencer player = MidiSystem.getSequencer();
			player.open();
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			
			Track track = seq.createTrack();
			
			ShortMessage a = new ShortMessage();
			a.setMessage(192, 1, 20, 100);
			MidiEvent noteOn = new MidiEvent(a, 1);
			track.add(noteOn);
			
			ShortMessage b = new ShortMessage();
			a.setMessage(192, 1, 20, 100);
			MidiEvent noteOff = new MidiEvent(b, 30);
			track.add(noteOff);
			
			player.setSequence(seq);
			
			player.start();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
