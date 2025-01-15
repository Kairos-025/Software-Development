package CodesEJ2800;
/* Copyright material for students working on assignments and projects */

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;

public class CodeLab2EJ extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	private static String frame_name = "EJ's Lab #2";
	private static BranchGroup alterableBG, shapeBG;
	private static GroupObjects groupObject;
	private static boolean r_tag = true;
	private static boolean object_tag = true;
    private static boolean isTwoCircles = false; // Tracks object state (one or two circles)
	private static final String OBJECT_NAME = "Circle(s)";
	
	/* a function to build and return the content branch */
	private static BranchGroup create_Scene() {
		alterableBG = new BranchGroup();                   // allow 'alterableBG' to change children
		groupObject = new GroupObjects(L2StarEJ.line_Shape(0.6f)); 
		shapeBG = groupObject.get_ShapeBG();               // get the BranchGroup with a star shape
		
		return GroupObjects.scene_Group(alterableBG, shapeBG);
	}

	/* a constructor to set up for the application */
	public CodeLab2EJ(BranchGroup scene) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		canvas3D.setSize(800, 800);                        // set size of canvas
		SimpleUniverse su = new SimpleUniverse(canvas3D);  // create a SimpleUniverse
		                                                   // set the viewer's location
		CommonsEJ.define_Viewer(su, new Point3d(1.35, -0.35, 1.5)); 		
		scene.addChild(CommonsEJ.add_Lights(CommonsEJ.White, 1));
		
		scene.compile();		                           // optimize the BranchGroup
		su.addBranchGraph(scene);                          // attach 'scene' to 'su'

		Menu m = new Menu("Menu");                         // set menu's label
		m.addActionListener(this);
		MenuBar menuBar = build_MenuBar(m, OBJECT_NAME);                 // build and set the menu bar
		frame.setMenuBar(menuBar);

		setLayout(new BorderLayout());
		add("Center", canvas3D);
		frame.setSize(810, 800);                           // set the size of the frame
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		frame = new JFrame(frame_name + ": Circle");     // NOTE: copyright material
		frame.getContentPane().add(new CodeLab2EJ(create_Scene()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	

	/* a function to build the menu bar of the demo */
	public static MenuBar build_MenuBar(Menu m, String s) {
		MenuBar menuBar = new MenuBar();

		m.add("Exit");		                               // specify menu items
		m.add("Pause/Rotate");
		m.addSeparator();                                  // group of geometry arrays
		m.add(s);
		menuBar.add(m);                                    // add items to the menu

		return menuBar;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String chosen_item = e.getActionCommand();		

		frame.setTitle(frame_name + ": " + chosen_item);
		switch(chosen_item) {                              // handle different menu items
		case "Exit": 
			System.exit(0);                                // quit the application
		case "Pause/Rotate":
			r_tag = (r_tag == true)? false : true;
			CommonsEJ.control_Rotation(r_tag);
			return;                                // Toggle between one and two circles
		case OBJECT_NAME:                                  // change object shape and name
			if (object_tag) {
				groupObject = new GroupObjects(L2StarEJ.line_Shape(0.6f));
				groupObject.get_ShapeBG().setCapability(BranchGroup.ALLOW_DETACH);
				alterableBG.addChild(groupObject.get_ShapeBG());
				
				groupObject = new GroupObjects(L2StarEJ.line_Shape(0.5f));
				groupObject.get_ShapeBG().setCapability(BranchGroup.ALLOW_DETACH);
				alterableBG.addChild(groupObject.get_ShapeBG());
				
				isTwoCircles = true;
				updateFrameTitle();
				
				object_tag = false;		// toggle object state
			}
			else {
				groupObject = new GroupObjects(L2StarEJ.line_Shape(0.6f));
				groupObject.get_ShapeBG().setCapability(BranchGroup.ALLOW_DETACH);
				alterableBG.addChild(groupObject.get_ShapeBG());
				
				isTwoCircles = false;
				updateFrameTitle();
				
				object_tag = true;		// toggle object state
			}
		    
			break;
		default:
			return;
		}
		
		BranchGroup tmpBG = groupObject.get_ShapeBG();     // Save the new (shape) group
        shapeBG.detach();                                  // Detach the previous shape
        shapeBG = tmpBG;
        shapeBG.setCapability(BranchGroup.ALLOW_DETACH);   // Make the new shape detachable
        alterableBG.addChild(shapeBG);                     // Update 'alterableBG'
		
	}	

    /* Update the frame title based on the current object state */
    private void updateFrameTitle() {
        String objectName = isTwoCircles ? "Two Circles" : "Circle";
        frame.setTitle(frame_name + ": " + objectName);
    }
}
