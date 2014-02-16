import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;


public class Main extends JFrame {

	public void initUI() {
		original();
		JFrame ff = new JFrame("Fire Fighter");
        JPanel ffPanel = new JPanel();
        ff.add(ffPanel);
        ffPanel.setLayout(new GridLayout(2, 2));
        JButton up = new BasicArrowButton(BasicArrowButton.NORTH);
        JButton down = new BasicArrowButton(BasicArrowButton.SOUTH);
        JButton open = new JButton("Open");
        JButton close = new JButton("Close");
        
        up.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				e.carCalls.clear();
				e.start();
				e.FFdir = Elevator.UP;
				(new Timer()).schedule(new TimerTask() {
		            public void run() {
		                e.stop();
		            }
		        }, 100);
			}
		});
        down.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				e.carCalls.clear();
				e.start();
				e.FFdir = Elevator.DOWN;
				(new Timer()).schedule(new TimerTask() {
		            public void run() {
		                e.stop();
		            }
		        }, 100);
			}
		});
        
        open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				e.openDoor();
				System.out.println("Wait to open the door");
			}
		});
        
        close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				e.closeDoor();
				System.out.println("Wait to open the door");
			}
		});
        
        ffPanel.add(open);  ffPanel.add(up);
        ffPanel.add(close); ffPanel.add(down);
        ff.setSize(160, 100);
        ff.setLocation(5, 300);
        ff.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ff.setVisible(true);
        ff.toFront();
	}
	
	

}
