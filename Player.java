import java.lang.Math;
import java.awt.event.*;

import java.awt.Graphics;
import java.awt.Color;

import java.util.Arrays;

public class Player {

    private GamePanel gamePanel;
    private Map map;

    private double x_coord;
    private double y_coord;
    private double player_angle; // in radians
    private double player_speed;
    private double rot_speed;
    private double dx;
    private double dy;
    private double d_rot;

    private int screen_x;
    private int screen_y;

    private Raycasting raycasting;

    Player(GamePanel g, Map m, Raycasting r) {
        gamePanel = g;
        map = m;
        raycasting = r;

        x_coord = 1.5; // mini_map coords
        y_coord = 5;
        player_angle = 0;
        player_speed = gamePanel.getPanelWidth() * 0.00004;
        rot_speed = 0.03;
        dx = 0;
        dy = 0;
        d_rot = 0;

        calculateScreenCoords();
    }

    public void drawCenteredCircle(Graphics g, int x, int y, int r) {
        g.setColor(Color.green);
        x = x-(r/2);
        y = y-(r/2);
        g.fillOval(x,y,r,r);
    }

    public void drawPlayer(Graphics g) {
        raycasting.ray_cast(g);
        drawCenteredCircle(g, screen_x, screen_y, 10);
    }

    public void movePlayer() {
        checkWallCollisionMove();
        player_angle += d_rot;
    }

    // returns true if not touching a wall
    public boolean checkWall(int x, int y) {
        int[] temp_coords = new int[] {x, y};
        for (int[] key : map.getWorldMap().keySet()) {
            if (Arrays.equals(key, temp_coords)) {
                return false;
            }
        }
        return true;
    }

    public void checkWallCollisionMove() {
        if (checkWall((int) (x_coord + dx), (int) y_coord)) {
            x_coord += dx;
            calculateScreenCoords();
        }
        if (checkWall((int) x_coord, (int) (y_coord + dy))) {
            y_coord += dy;
            calculateScreenCoords();
        }
        
    }

    public void calculateScreenCoords() {
        screen_x = (int) (x_coord * map.getScaleX());
        screen_y = (int) (y_coord * map.getScaleY());
    }


    public void keyPressedMovement(KeyEvent e) {
        double cos_a = Math.cos(player_angle);
        double sin_a = Math.sin(player_angle);
        double speed_cos_a = player_speed * cos_a;
        double speed_sin_a = player_speed * sin_a;

        int keyCode = e.getKeyCode();
        boolean alreadyMoving = dx != 0 || dy != 0;
        // check for movement
        if (keyCode == KeyEvent.VK_W) {
            dx += speed_cos_a;
            dy += speed_sin_a;
        }
        if (keyCode == KeyEvent.VK_S) {
            dx += -speed_cos_a;
            dy += -speed_sin_a;
        }
        if (keyCode == KeyEvent.VK_A) {
            dx += speed_sin_a;
            dy += -speed_cos_a;
        }
        if (keyCode == KeyEvent.VK_D) {
            dx += -speed_sin_a;
            dy += speed_cos_a;
        }

        // prevent diagonal movement from being too fast
        if (alreadyMoving) {
            dx /= 1.2;
            dy /= 1.2;
        }

        // check for rotation
        if (keyCode == KeyEvent.VK_LEFT) {
            d_rot += -rot_speed;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            d_rot += rot_speed;
        }
        player_angle %= 2 * Math.PI;
    }

    public void keyReleasedMovement(KeyEvent e) {
        dx = 0;
        dy = 0;
        d_rot = 0;
    }

    // getters
    public double getPlayerAngle() {
        return player_angle;
    }

    public double getXCoord() {
        return x_coord;
    }

    public double getYCoord() {
        return y_coord;
    }

    public int getScreenX() {
        return screen_x;
    }

    public int getScreenY() {
        return screen_y;
    }

    public Map getMap() {
        return map;
    }
    
}