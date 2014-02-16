import java.awt.*; 
import java.awt.geom.*; 
import java.awt.image.*; 
import javax.imageio.ImageIO; 
import javax.swing.*; 
import java.util.Timer; 
import java.util.TimerTask; 
import java.io.*; 
import java.awt.event.*; 
import javax.imageio.*; 
import javax.swing.event.*; 
import java.util.Set; 


public  class  Main  extends JFrame {
	
	
	Environment env;

	
	Elevator e;

	
	Image img;

	
	myCanvas canvas;

	
	Timer timer;

	
	
	final static int FLOORS = 5;

	
	final static int FLOOR_HEIGHT = 100;

	
	final static int CANVAS_WIDTH = 400;

	
	final static int BTN_WIDTH = 30;

	
	final static int ELEVATOR_WIDTH = 100;

	
	final static int ELEVATOR_MARGIN = 150;

	
	final static int HEIGHT = FLOORS*FLOOR_HEIGHT+10;

	
	final static int MIN_WEIGHT = 25;

	
	final static int MAX_WEIGHT = 127;

	

	
	public  class  myCanvas  extends Canvas {
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			setBackground(Color.WHITE);
			Graphics2D g2 = (Graphics2D) g;
			// Draw floors
			int w = getWidth();
		    g2.setStroke(new BasicStroke(3));
		    for (Floor f : e.env.floors) {
		    	g2.drawString(""+f.floor, 10, f.position-15);
		    	for (int i=0; i<f.people.size(); ++i) {
		    		g2.drawString((f.people.get(i).dir==Elevator.UP?"+":"-")+f.people.get(i).floor, (1+i)*20, (int)f.position-57);
		    		g2.drawImage(img, (1+i)*20, (int)f.position-47, null);
		    	}
		    	g2.draw(new Line2D.Double(0, f.position, w, f.position));
			}
		    // Draw elevator and doors
		    if (e.ready) {
		    	g2.setColor(new Color(190, 190, 190));
		    	g2.fillRect(ELEVATOR_MARGIN, (int) e.z-FLOOR_HEIGHT, ELEVATOR_WIDTH, FLOOR_HEIGHT);
		    	int width = (int) ELEVATOR_WIDTH/2;
		    	g2.setColor(Color.GRAY);
		    	g2.fillRect(ELEVATOR_MARGIN + e.leftDoorPos, (int) e.z-FLOOR_HEIGHT, width, FLOOR_HEIGHT);
		    	g2.fillRect(ELEVATOR_MARGIN + e.rightDoorPos + width, (int) e.z-FLOOR_HEIGHT, width, FLOOR_HEIGHT);
		    	g2.setColor(Color.ORANGE);
		    	g2.drawString(""+e.people.size(), ELEVATOR_MARGIN+10, (int) e.z-FLOOR_HEIGHT+15);
		    }
		}


	}

	
	
	public Main() {
		env = new Environment();
		e = new Elevator(env);
		for (int i=0; i<FLOORS; ++i) {
			env.addFloor(new Floor(e, i, FLOOR_HEIGHT*(FLOORS-i)));
		}
		try {
        	img = ImageIO.read(new File("res/man.png"));
        } catch (Exception ex) { ex.printStackTrace(); }
        initUI();
        e.beReady();
        e.running = true;
		timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                canvas.repaint();
            }
        }, 0, 1000/30);
	}

	
	
	public void initUI() {
		setTitle("Elevator Control");
		JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(new FlowLayout());
        canvas = new myCanvas();
        canvas.setSize(CANVAS_WIDTH, HEIGHT);
        canvas.setBackground(Color.WHITE);
        canvas.setLocation(0, 0);
        panel.add(canvas);
        initPeopleUI();
        setSize(CANVAS_WIDTH+10, HEIGHT+30);
        setLocation(250, 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent w) {
            	e.notPaused = false;
                timer.cancel();
                w.getWindow().dispose();
            }
        });
	}

	
	
	public void initToolsUI() {
		JFrame tools = new JFrame("Tools");
        JPanel toolsPanel = new JPanel();
        tools.add(toolsPanel);
        toolsPanel.setLayout(new GridLayout((FLOORS/3)+1, 3));
        for (int i=0; i<FLOORS; ++i) {
			JButton btn = new JButton(""+(i+1));
			toolsPanel.add(btn);
		}
        tools.setSize(BTN_WIDTH*3, BTN_WIDTH*(FLOORS/3+1));
        tools.setLocation(100, 100);
        tools.setDefaultCloseOperation(EXIT_ON_CLOSE);
        tools.setVisible(true);
        tools.toFront();
	}

	
	
	public void initPeopleUI() {
		JFrame people = new JFrame("People");
        JPanel pplPanel = new JPanel();
        people.add(pplPanel);
        pplPanel.setLayout(new GridLayout(FLOORS, 3));

        for (int i=FLOORS-1; i>=0; --i) {
        	pplPanel.add(new JLabel(""+i));
			JButton add = new JButton("+");
			JButton remove = new JButton("-");
			final int j = i;
			add.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int floor;
					do {
						floor = (int) (Math.random() * FLOORS);
					} while (floor == j);
					final int dir = e.getDir(j, floor);
					env.floors.get(j).people.add(
							new Person(MIN_WEIGHT + (int) (Math.random() * ((MAX_WEIGHT - MIN_WEIGHT) + 1)), floor, dir)
					);
					e.addHallCall(j, dir);
				}
			});
			remove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Person last = e.people.get(e.people.size()-1);
					e.leave(last);
					env.floors.get(j).people.add(last);
					e.addHallCall(j, e.getDir(j, last.floor));
				}
			});
			pplPanel.add(add);
			pplPanel.add(remove);
		}
        people.setSize(30, 30*FLOORS);
        people.setLocation(700, 100);
        people.setDefaultCloseOperation(EXIT_ON_CLOSE);
        people.setVisible(true);
        people.toFront();
	}

	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main win = new Main();
                win.setVisible(true);
            }
        });
	}


}
