// Map.java
import java.awt.*;

public class Map {
    public static final int MAP_SIZE = 24;
    
    private int[][] mapData;
    private Color[][] wallColors;
    
    private Color[] colorPalette = {
        new Color(220, 50, 50), new Color(50, 220, 50), new Color(50, 50, 220),
        new Color(220, 220, 50), new Color(220, 50, 220), new Color(50, 220, 220),
        new Color(220, 150, 50), new Color(150, 50, 220), new Color(50, 220, 150),
        new Color(220, 50, 150), new Color(50, 150, 220), new Color(150, 220, 50)
    };
    
    public Map() {
        initializeMap();
        generateWallColors();
    }
    
    private void initializeMap() {
        mapData = new int[][] {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1},
            {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1},
            {1,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
    }
    
    private void generateWallColors() {
        wallColors = new Color[MAP_SIZE][MAP_SIZE];
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (mapData[i][j] != 0) {
                    int colorIndex = (i + j * 3) % colorPalette.length;
                    wallColors[i][j] = colorPalette[colorIndex];
                } else {
                    wallColors[i][j] = Color.BLACK;
                }
            }
        }
    }
    
    public boolean isWall(int x, int y) {
        if (x < 0 || x >= MAP_SIZE || y < 0 || y >= MAP_SIZE) {
            return true;
        }
        return mapData[x][y] != 0;
    }
    
    public boolean isWalkable(double x, double y) {
        int mapX = (int)x;
        int mapY = (int)y;
        if (mapX < 0 || mapX >= MAP_SIZE || mapY < 0 || mapY >= MAP_SIZE) {
            return false;
        }
        return mapData[mapX][mapY] == 0;
    }
    
    public Color getWallColor(int x, int y) {
        if (x < 0 || x >= MAP_SIZE || y < 0 || y >= MAP_SIZE) {
            return Color.GRAY;
        }
        Color color = wallColors[x][y];
        return color != null ? color : Color.GRAY;
    }
    
    public Color getWallColorShaded(int x, int y, double shade) {
        Color baseColor = getWallColor(x, y);
        int red = (int)(baseColor.getRed() * shade);
        int green = (int)(baseColor.getGreen() * shade);
        int blue = (int)(baseColor.getBlue() * shade);
        return new Color(
            Math.max(0, Math.min(255, red)),
            Math.max(0, Math.min(255, green)),
            Math.max(0, Math.min(255, blue))
        );
    }
    
    public int[][] getMapData() { return mapData; }
    public Color[][] getWallColors() { return wallColors; }
    public int getSize() { return MAP_SIZE; }
    
    public boolean hasTexture() {
        // Always return true to use texture
        BlockTexture tex = BlockTexture.getInstance();
        return tex != null && tex.isLoaded();
    }
    
    public void generateRandomMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (i == 0 || i == MAP_SIZE - 1 || j == 0 || j == MAP_SIZE - 1) {
                    mapData[i][j] = 1;
                } else if (Math.random() < 0.3) {
                    mapData[i][j] = 1;
                } else {
                    mapData[i][j] = 0;
                }
            }
        }
        generateWallColors();
    }
}