package pack1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by IvanOP on 28.04.2017.
 */
public class FrameHolder extends JFrame implements KeyListener {
    private JFrame frame;
    private Habitat beeHive;
    private GUIHolder guiHolder;

    public FrameHolder() {
        this.frame = new JFrame();
        this.beeHive = new Habitat();
        this.guiHolder = new GUIHolder(beeHive, frame);

        guiHolder.setFocusable(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(beeHive, BorderLayout.WEST);
        frame.add(guiHolder, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_B: {
                beeHive.startSimulation();
                break;
            }

            case KeyEvent.VK_E: {
                beeHive.stopSimulation();
                break;
            }

            case KeyEvent.VK_T: {
                beeHive.showSimulationTime();
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

}
