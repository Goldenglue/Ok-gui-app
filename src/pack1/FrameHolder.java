package pack1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.add(beeHive, BorderLayout.WEST);
        frame.add(guiHolder, BorderLayout.EAST);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                doSomething();
            }
        });
    }

    void doSomething() {
        System.out.println("asdf");
        frame.dispose();
        System.exit(0);
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
