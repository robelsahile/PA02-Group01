/*
 * TCSS 305 Homework 4: SnapShop
 * 
 * THIS FILE CONTAINS SOLUTION CODE. THIS FILE SHOULD NOT BE DISTRIBUTED UNDER
 * ANY CIRCUMSTANCES! IF YOU ARE A STUDENT, YOU SHOULD NOT BE LOOKING AT THIS
 * FILE, AND SHOULD DELETE IT IMMEDIATELY.
 */

package gui;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;
import image.PixelImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The GUI for SnapShop.
 * 
 * @author Marty Stepp
 * @author Daniel M. Zimmerman
 * @author Alan Fowler
 * @author Charles Bryan
 * @version Winter 2016
 */
public class SnapShopGUI {
	//timmy roma refactor # 5 replace magic literal
	private static final int GRID_COLOMS = 1;
    /** The window title. */
    private static final String TITLE = "TCSS 305 SnapShop";
    
    /** A ToolKit. */
    private static final Toolkit KIT = Toolkit.getDefaultToolkit();

    /** The frame for this application. */
    private JFrame myFrame;

    /** The label in which we will display the image. */
    private JLabel myImageLabel;

    /** The open button. */
    private JButton myOpenButton;

    /** The save button. */
    private JButton mySaveButton;

    /** The close button. */
    private JButton myCloseButton;

    /** The list of filter buttons. */
    private List<JButton> myFilterButtons;

    /** The file chooser. */
    private JFileChooser myChooser;

    /** The image. */
//    private PixelImage myImage;
    
    //REFACTORED CODE using Rename variable
    // Robel Sahle #5
    private PixelImage myPixelImage;

    /**
     * Initializes the GUI.
     */
    public SnapShopGUI() {
        doComponents();
        doEvents();
        doLayout();
        doEnabling();
    }

    /**
     * Sets up the GUI and makes it visible.
     */
    public void start() {
        // set properties of the frame
        myFrame.setTitle(TITLE);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setMinimumSize(new Dimension());
        myFrame.pack();
        myFrame.setMinimumSize(myFrame.getSize());
        centerFrame();
        myFrame.setVisible(true);
    }

    /**
     * Creates a button to activate the specified filter, on the specified
     * panel.
     * 
     * @param theFilter The filter.
     * @return the created button.
     */
    private JButton createButton(final Filter theFilter) {
        final JButton button = new JButton(theFilter.getDescription());
        button.setEnabled(false);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                theFilter.filter(myPixelImage);

                myImageLabel.repaint(); // do this
                
                // or do this:
                // myImageLabel.setIcon(new ImageIcon(myImage));
                
                // either way, actionPerformed is just 2 lines of code.
            }
        });
        return button;
    }

    /**
     * Set up the graphical components (frame, image label, buttons).
     */
    private void doComponents() {
        myFrame = new JFrame();
        myImageLabel = new JLabel();
        
        // image will align left if I do nothing...
        /*
         * later in this code myImageLabel is added directly to the Center region
         * of a JFrame.
         * 
         * Q) What if I do no alignment and just add the label on a panel
         * and then add the panel to the frame?
         * A) aligns center horizontally, but to top vertically.
         */
        
        // works on frame; top center on panel
        myImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        myImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // aligns left on Frame; aligns top center on panel
//        myImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        myImageLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        // aligns left on Frame; top center on panel
//        myImageLabel.setAlignmentX(SwingConstants.CENTER);
//        myImageLabel.setAlignmentY(SwingConstants.CENTER);

        myOpenButton = new JButton("Open...");
        mySaveButton = new JButton("Save As...");
        myCloseButton = new JButton("Close Image");
        
        // To display icons on these buttons, comment out the three lines above
        // and use the three lines below instead.
        
//        myOpenButton = new JButton("Open...", new ImageIcon("icons\\open.gif"));
//        mySaveButton = new JButton("Save As...", new ImageIcon("icons\\save.gif"));
//        myCloseButton = new JButton("Close Image", new ImageIcon("icons\\close.gif"));
    }

    /**
     * Enable all the buttons appropriately.
     */
    private void doEnabling() {
        final boolean hasIcon = myPixelImage != null && myImageLabel.getIcon() != null;
        for (final JButton button : myFilterButtons) {
            button.setEnabled(hasIcon);
        }
        mySaveButton.setEnabled(hasIcon);
        myCloseButton.setEnabled(hasIcon);
    }

    /**
     * Sets up the event listeners.
     */
    private void doEvents() {
        // load an image when the Open button is clicked
        myOpenButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                loadImage();
                doEnabling();
            }
        });
        
        // save an image when the Save button is clicked
        mySaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                saveImage();
            }
        });

        // close an image when the Close button is clicked
        myCloseButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
//                closeImage();
                mycloseImage();
            }
        });

    }

    /**
     * Sets up the graphical components.
     */
    private void doLayout() {
        // create the filter buttons
        myFilterButtons = new ArrayList<JButton>();
        
        myFilterButtons.add(createButton(new EdgeDetectFilter()));
        myFilterButtons.add(createButton(new EdgeHighlightFilter()));
        myFilterButtons.add(createButton(new FlipHorizontalFilter()));
        myFilterButtons.add(createButton(new FlipVerticalFilter()));
        myFilterButtons.add(createButton(new GrayscaleFilter()));
        myFilterButtons.add(createButton(new SharpenFilter()));
        myFilterButtons.add(createButton(new SoftenFilter()));

        // add them to a panel
        final JPanel westNorthPanel = new JPanel(new GridLayout(myFilterButtons.size(), GRID_COLOMS));
        for (final JButton button : myFilterButtons) {
//            final JPanel buttonPanel = new JPanel();
//            buttonPanel.add(button);
            westNorthPanel.add(button);
//            final JPanel buttonPanel = new JPanel();
//            buttonPanel.add(button);
//            westNorthPanel.add(buttonPanel);
        }
        final JPanel westOuterPanel = new JPanel(new BorderLayout());
        westOuterPanel.add(westNorthPanel, BorderLayout.NORTH);
        
        // south panel to hold the file Open button
        final JPanel westSouthPanel = new JPanel(new GridLayout(3, GRID_COLOMS));
        westSouthPanel.add(myOpenButton);

        westSouthPanel.add(mySaveButton);

        westSouthPanel.add(myCloseButton);
        westOuterPanel.add(westSouthPanel, BorderLayout.SOUTH);

        
        myFrame.add(westOuterPanel, BorderLayout.WEST);
        
//        final JPanel westOuterPanel = new JPanel();
//        westOuterPanel.add(westInnerPanel);
//        myFrame.add(westInnerPanel, BorderLayout.WEST);

        // label added directly the frame - works
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePanel.add(myImageLabel);
        myFrame.add(imagePanel, BorderLayout.CENTER);
        
        // label added to panel does not work
//        JPanel imagePanel = new JPanel();
//        imagePanel.add(myImageLabel);
//        myFrame.add(imagePanel);


    }

    /**
     * Attempts to load the image using a file chooser.
     */
    private void loadImage() {
        try {
            // load the image and set the icon on the label
            final File selectedFile = showChooser(true);
            if (selectedFile == null) {
                return;
            }

            myPixelImage = PixelImage.load(selectedFile);
            myImageLabel.setIcon(new ImageIcon(myPixelImage));
            //Timmy roma refactor 3 moved statements into function
            setFrame();
            centerFrame();
        } catch (final IOException ex) {
            JOptionPane.showMessageDialog(myFrame,
                                          "The selected file did not contain an image!",
                                          "Error!",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Attempts to center the JFrame on the screen. */
    private void centerFrame() {
//        myFrame.setLocation(
//            (int) (KIT.getScreenSize().getWidth() / 2 - myFrame.getWidth() / 2),
//            (int) (KIT.getScreenSize().getHeight() / 2 - myFrame.getHeight() / 2));
        
        //REFACTORED CODE using Extract Variable
        //added variable explaining the parameters for setLocation method.
    	// Robel Sahle #1

        int xCenter  = (int) (KIT.getScreenSize().getWidth() / 2 - myFrame.getWidth() / 2);
        int yCenter = (int) (KIT.getScreenSize().getHeight() / 2 - myFrame.getHeight() / 2);
        myFrame.setLocation(xCenter, yCenter);
        
    }

    /**
     * Attempts to save the image using a file chooser.
     */
    private void saveImage() {
        try {
            // load the image and set the icon on the label
            final File selectedFile = showChooser(false);
            if (selectedFile == null) {
                return;
            }

            myPixelImage.save(selectedFile);
        } catch (final IOException ex) {
            JOptionPane.showMessageDialog(myFrame, ex.getMessage());
        }
    }

    /**
     * Closes the image.
     */
//    private void closeImage() {
    
    //REFACTORED CODE using Rename field
    //renaming closeImage to mycloseImage();
    //Robel Sahle #2
    private void mycloseImage() {
        // my_image = null;
        myImageLabel.setIcon(null);
        doEnabling();
        //Timmy roma refactor 3 moved statements into function
        setFrame();
        centerFrame();
    }
    //Timmy roma refactor 3 moved statements into function
    private void setFrame()
    {
        myFrame.setMinimumSize(new Dimension());
        myFrame.pack();
        myFrame.setMinimumSize(myFrame.getSize());
    }

    /**
     * Shows the file chooser, either to open or save a file.
     * 
     * @param theOpenFlag true if we're showing the chooser to open a file,
     *            false otherwise.
     * @return The file that was chosen, if any.
     */
    private File showChooser(final boolean theOpenFlag) {
//        File result = null;

        // construct file chooser if necessary
        if (myChooser == null) {
            // my_chooser = new JFileChooser(System.getProperty(".home"));
        	//refactor #4 timmy roma remove dead code, was an extra myChooser = new JFileChooser("."); here.
            
            myChooser = new JFileChooser(".");
        }

        int choice;
        
//        if (theOpenFlag) {
//            choice = myChooser.showOpenDialog(myFrame);
//        } else {
//            choice = myChooser.showSaveDialog(myFrame);
//        }
        
        //REFACTORED CODE using Inline Variable
        //Changed the if block with used to assign to variable to an inline statement
        // Robel Sahle #3
        
        choice = (theOpenFlag) ? myChooser.showOpenDialog(myFrame) : myChooser.showSaveDialog(myFrame);

//        if (choice == JFileChooser.APPROVE_OPTION) {
//            result = myChooser.getSelectedFile();
//        }
//
//        return result;
        
        //REFACTORED CODE using Removed Middle Man
        //Replaced the return value directly upon checking the if condition.
        //Robel Sahle #4
        
        return  (choice == JFileChooser.APPROVE_OPTION) ?  myChooser.getSelectedFile() : null;
    }

}
