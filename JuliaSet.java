import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class JuliaSet extends JPanel implements AdjustmentListener,ActionListener,ChangeListener{

	JFrame frame;
	JScrollBar scrollBar;
	JScrollBar[] scrollBars;
	JSpinner hueSpinner;
	JSpinner satSpinner;
	JSpinner briSpinner;
	JButton randomize;
	JButton reset;
	double a;
	double b;
	double zoom;
	double hue;
	double sat;
	double bri;

	public JuliaSet(){
		frame = new JFrame("Julia Set Program");
		frame.add(this);
		scrollBars = new JScrollBar[3];
		
		a = 0.0;
		b = 0.0;
		zoom = 1.0;
		hue = 0.0;
		sat = 0.0;
		bri = 250.0;		
		
		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new GridLayout(3,1));

		for(int i = 0; i < scrollBars.length-1; i++){
			scrollBars[i] = new JScrollBar(JScrollBar.HORIZONTAL,0,0,-1000,1000);
			scrollBars[i].addAdjustmentListener(this);
			scrollBars[i].setUnitIncrement(1);

			scrollPanel.add(scrollBars[i]);
		}
		
		scrollBars[2] = new JScrollBar(JScrollBar.HORIZONTAL,0,0,0,1000);
		scrollBars[2].addAdjustmentListener(this);
		scrollBars[2].setUnitIncrement(1);
		scrollPanel.add(scrollBars[2]);
		
		SpinnerNumberModel hueModel = new SpinnerNumberModel(0.0f,0.0f,1000.0f,1.0f);
		SpinnerNumberModel satModel = new SpinnerNumberModel(0.0f,0.0f,1000.0f,1.0f);
		SpinnerNumberModel briModel = new SpinnerNumberModel(bri,0.0f,500.0f,1.0f);
		
		hueSpinner = new JSpinner(hueModel);
		hueSpinner.addChangeListener(this);
		satSpinner = new JSpinner(satModel);
		satSpinner.addChangeListener(this);
		briSpinner = new JSpinner(briModel);
		briSpinner.addChangeListener(this);
		
		JPanel spinnerPanel = new JPanel();
		spinnerPanel.setLayout(new GridLayout(3,1));
		
		spinnerPanel.add(hueSpinner);
		spinnerPanel.add(satSpinner);
		spinnerPanel.add(briSpinner);
		
		randomize = new JButton("Randomize");
		randomize.addActionListener(this);
		reset = new JButton("Reset");
		reset.addActionListener(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		
		buttonPanel.add(randomize);
		buttonPanel.add(reset);
		
		JPanel onePanel = new JPanel();
		onePanel.setLayout(new BorderLayout());
		onePanel.add(scrollPanel,BorderLayout.CENTER);
		onePanel.add(spinnerPanel,BorderLayout.WEST);
		onePanel.add(buttonPanel,BorderLayout.EAST);

		frame.add(onePanel,BorderLayout.SOUTH);
		frame.setSize(750,500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		drawJuliaSet(g);
	}
	
	public void drawJuliaSet(Graphics g) {
		int w = frame.getWidth();
		int h = frame.getHeight();
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				float f = 300.0f;
				double zx = 1.5*((i-(.5*w))/(.5*zoom*w));
				double zy = (j-(.5*h))/(.5*zoom*h);
				
				while((Math.pow(zx,2)+Math.pow(zy, 2)) < 6.0 && f > 0.0f) {
					double dif = ((Math.pow(zx, 2)-Math.pow(zy, 2))+a);
					zy = (2*zx*zy)+b;
					zx = dif;
					f-=1.0f;
				}
				
				int c;
				if(f > 0)
					c = Color.HSBtoRGB((float)((300.0f / f)+hue)% 1, (float)sat, (float)bri);
				else c = Color.HSBtoRGB(300.0f / f, 1, 0);
				
				image.setRGB(i, j, c);
			}
			g.drawImage(image, 0, 0, null);
		}
	}

	public void adjustmentValueChanged(AdjustmentEvent e){
			if(e.getSource()==scrollBars[0]){
				a = (double)(scrollBars[0].getValue()/100.0);
			}
			if(e.getSource()==scrollBars[1]){
				b = (double)(scrollBars[1].getValue()/100.0);
			}
			if(e.getSource()==scrollBars[2]){
				zoom = (double)(scrollBars[2].getValue()/100.0)+1.0;
			}
			System.out.println("Zoom: "+zoom);
		repaint();
	}

	public static void main(String[] args){

		JuliaSet set = new JuliaSet();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == randomize) {
			hue = Math.random()*1000;
			hueSpinner.setValue(hue);
			sat = Math.random()*1000;
			satSpinner.setValue(sat);
			bri = (Math.random()*250)+250;
			briSpinner.setValue(bri);
		}
		if(e.getSource() == reset) {
			a = 0.0;
			scrollBars[0].setValue(0);
			b = 0.0;
			scrollBars[1].setValue(0);
			zoom = 1.0;
			scrollBars[2].setValue(1);
			hue = 0.0;
			hueSpinner.setValue(hue);
			sat = 0.0;
			satSpinner.setValue(sat);
			bri = 250.0;
			briSpinner.setValue(bri);
		}
		repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == hueSpinner) {
			hue = (double)hueSpinner.getValue()/1000.0;
		}
		if(e.getSource() == satSpinner) {
			sat = (double)satSpinner.getValue()/1000.0;
		}
		if(e.getSource() == briSpinner) {
			bri = (double)briSpinner.getValue()/1000.0;
		}
		repaint();
	}

}

