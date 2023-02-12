import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements Runnable {
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

    private final int WIDTH = d.width, HEIGHT = d.height;
    private Map map;
    private Player player;
    private Raycasting raycasting;

    Thread gameThread;


    GamePanel() {
        this.map = new Map(this);
        this.setPreferredSize(d);
        setBackground(Color.black);

        this.raycasting = new Raycasting(this);
        player = new Player(this, map, raycasting);

        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(new AL());
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {
        player.drawPlayer(g);
        map.drawMap(g);
    }

    @Override
    public void run() {
        // game loop
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        boolean running = true;
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                player.movePlayer();
                repaint();
                delta--;
            }
        }
    }

    // getters
    public int getPanelWidth() {
        return WIDTH;
    }

    public int getPanelHeight() {
        return HEIGHT;
    }

    public Player getPlayer() {
        return player;
    }

    public class AL extends KeyAdapter {
        private static Integer lastKey;

        public void keyPressed(KeyEvent e) {
            
            if (lastKey == null || lastKey != e.getKeyCode()) {
                lastKey = e.getKeyCode();
                player.keyPressedMovement(e);
            }
        }
        public void keyReleased(KeyEvent e) {
            lastKey = null;
            player.keyReleasedMovement(e);
        }
    }
    
}
