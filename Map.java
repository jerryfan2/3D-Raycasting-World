import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;

import java.awt.Graphics;
import java.awt.Color;

public class Map {

    static final List<List<Integer>> MINI_MAP = new ArrayList<>(
        Arrays.asList(
            Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
            Arrays.asList(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1),
            Arrays.asList(1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 1),
            Arrays.asList(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
            Arrays.asList(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1),
            Arrays.asList(1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1),
            Arrays.asList(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
            Arrays.asList(1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1),
            Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        )
    );

    public static int MINIMAP_SCALE = 5;

    private HashMap<int[], Integer> worldMap;
    private GamePanel gamePanel;
    private int blocksX = MINI_MAP.get(0).size();
    private int blocksY = MINI_MAP.size();
    private int scaleX;
    private int scaleY;
    

    Map(GamePanel g) {
        this.gamePanel = g;
        worldMap = new HashMap<>();
        createWorldMap();
        scaleX = gamePanel.getPanelWidth() / (blocksX * MINIMAP_SCALE);
        scaleY = gamePanel.getPanelHeight() / (blocksY * MINIMAP_SCALE);
    }

    private void createWorldMap() {
        for (int y = 0; y < blocksY; y++) {
            for (int x = 0; x < blocksX; x++) {
                if (MINI_MAP.get(y).get(x) == 1) {
                    worldMap.put(new int[] {x, y}, 1);
                }
            }
        }
    }

    public void drawMap(Graphics g) {
        for (int[] key : worldMap.keySet()) {
            g.setColor(Color.black);
            g.fillRect(key[0] * scaleX, key[1] * scaleY, scaleX, scaleY);
            g.setColor(Color.white);
            g.drawRect(key[0] * scaleX, key[1] * scaleY, scaleX, scaleY);
        }
    }

    // getters
    public HashMap<int[], Integer> getWorldMap() {
        return worldMap;
    }

    public int getScaleX() {
        return scaleX;
    }

    public int getScaleY() {
        return scaleY;
    }
    
}
