import java.lang.Math;

import java.awt.Graphics;
import java.awt.Color;


public class Raycasting {

    private GamePanel gamePanel;

    private static double FOV = Math.PI / 3;
    private static double HALF_FOV = FOV / 2;
    private static int MAX_DEPTH = 20;

    private int num_rays;
    private double delta_angle;

    private double screen_dist;
    private int wall_section_width;

    Raycasting(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        num_rays = gamePanel.getPanelWidth() / 2;
        delta_angle = FOV / num_rays;
        screen_dist = (gamePanel.getPanelWidth() / 2) / Math.tan(HALF_FOV);
        wall_section_width = gamePanel.getPanelWidth() / num_rays;
    }

    public void ray_cast(Graphics g) {
        Player gp_player = gamePanel.getPlayer();
        int grid_x = (int) gp_player.getXCoord();
        int grid_y = (int) gp_player.getYCoord();

        double pos_x = gp_player.getXCoord();
        double pos_y = gp_player.getYCoord();

        double ray_angle = gp_player.getPlayerAngle() - HALF_FOV + 0.0001;
        for (int ray = 0; ray < num_rays; ray++) {
            double cos_a = Math.cos(ray_angle);
            double sin_a = Math.sin(ray_angle);

            // horizontal grids
            double y_horz;
            double dyh;
            if (sin_a > 0) {
                y_horz = grid_y + 1;
                dyh = 1;
            } else {
                y_horz = grid_y - 0.00001;
                dyh = -1;
            }

            double depth_horz = (y_horz - pos_y) / sin_a;
            double x_horz = pos_x + depth_horz * cos_a;

            double delta_horz = dyh / sin_a;
            double dxh = delta_horz * cos_a;

            for (int i = 0; i < MAX_DEPTH; i++) {
                if (!gp_player.checkWall((int) x_horz, (int) y_horz)) break;
                x_horz += dxh;
                y_horz += dyh;
                depth_horz += delta_horz;
            }

            
            // vertical grids
            double x_vert;
            double dx;
            if (cos_a > 0) {
                x_vert = grid_x + 1;
                dx = 1;
            } else {
                x_vert = grid_x - 0.00001;
                dx = -1;
            }

            double depth_vert = (x_vert - (double) pos_x) / cos_a;
            double y_vert = pos_y + depth_vert * sin_a;

            double delta_depth = dx / cos_a;
            double dy = delta_depth * sin_a;

            for (int i = 0; i < MAX_DEPTH; i++) {
                if (!gp_player.checkWall((int) x_vert, (int) y_vert)) break;
                x_vert += dx;
                y_vert += dy;
                depth_vert += delta_depth;
            }

            // depth needed to draw
            double depth = depth_vert < depth_horz ? depth_vert : depth_horz;

            // draw raycasting for minimap
            g.setColor(Color.yellow);
            g.drawLine(gp_player.getScreenX(), gp_player.getScreenY(),
                        (int) (gp_player.getScreenX() + depth * cos_a * gp_player.getMap().getScaleX()),
                        (int) (gp_player.getScreenY() + depth * sin_a * gp_player.getMap().getScaleY()));

            // draw 3D projection
            depth *= Math.cos(gp_player.getPlayerAngle() - ray_angle);
        
            double proj_height = screen_dist / (depth + 0.0001);

            int color_val = (int) (204 / (1 + Math.pow(depth, 5) * 0.00002));
            Color color = new Color(color_val, color_val, color_val);
            g.setColor(color);
            g.drawRect(ray * wall_section_width, 
                        gamePanel.getPanelHeight() / 2 - (int) (proj_height / 2),
                        wall_section_width,
                        (int) proj_height);

            ray_angle += delta_angle;
        }
    }
}
