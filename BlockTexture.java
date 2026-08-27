// BlockTexture.java
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class BlockTexture {
    private BufferedImage textureImage;
    private int[] texturePixels;
    private int textureWidth;
    private int textureHeight;
    private boolean loaded = false;
    private static BlockTexture instance = null;
    
    private BlockTexture() {
        loadTexture();
    }
    
    public static BlockTexture getInstance() {
        if (instance == null) {
            instance = new BlockTexture();
        }
        return instance;
    }
    
    private void loadTexture() {
        try {
            // Try multiple paths
            String[] paths = {
                "assets/image/image.png",
                "assets/image.png",
                "image.png",
                "../assets/image/image.png",
                "./assets/image/image.png"
            };
            
            File file = null;
            for (String path : paths) {
                File f = new File(path);
                if (f.exists()) {
                    file = f;
                    break;
                }
            }
            
            if (file != null && file.exists()) {
                textureImage = ImageIO.read(file);
                textureWidth = textureImage.getWidth();
                textureHeight = textureImage.getHeight();
                
                texturePixels = new int[textureWidth * textureHeight];
                textureImage.getRGB(0, 0, textureWidth, textureHeight, texturePixels, 0, textureWidth);
                
                loaded = true;
                System.out.println("✓ Texture loaded successfully: " + file.getAbsolutePath());
                System.out.println("  Size: " + textureWidth + "x" + textureHeight);
            } else {
                System.err.println("✗ Texture file not found at: assets/image/image.png");
                createFallbackTexture();
            }
        } catch (Exception e) {
            System.err.println("✗ Failed to load texture: " + e.getMessage());
            createFallbackTexture();
        }
    }
    
    private void createFallbackTexture() {
        textureWidth = 64;
        textureHeight = 64;
        texturePixels = new int[textureWidth * textureHeight];
        
        // Create a brick-like texture
        for (int y = 0; y < textureHeight; y++) {
            for (int x = 0; x < textureWidth; x++) {
                int brickRow = y / 16;
                int brickCol = x / 16;
                boolean offset = (brickRow % 2 == 1);
                int col = brickCol;
                if (offset) {
                    col = (x + 8) / 16;
                }
                
                int r, g, b;
                if ((brickRow + col) % 2 == 0) {
                    r = 180; g = 80; b = 60; // Red brick
                } else {
                    r = 140; g = 60; b = 40; // Dark brick
                }
                
                // Mortar lines
                if (x % 16 < 2 || y % 16 < 2) {
                    r = 200; g = 190; b = 180;
                }
                
                // Add texture variation
                if (Math.random() < 0.05) {
                    r += (int)(Math.random() * 20 - 10);
                    g += (int)(Math.random() * 20 - 10);
                    b += (int)(Math.random() * 20 - 10);
                }
                
                texturePixels[y * textureWidth + x] = 
                    (Math.min(255, Math.max(0, r)) << 16) | 
                    (Math.min(255, Math.max(0, g)) << 8) | 
                    Math.min(255, Math.max(0, b));
            }
        }
        loaded = true;
        System.out.println("✓ Created fallback brick texture");
    }
    
    public int getPixel(int x, int y) {
        if (!loaded || texturePixels == null) {
            return 0x888888;
        }
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x >= textureWidth) x = textureWidth - 1;
        if (y >= textureHeight) y = textureHeight - 1;
        return texturePixels[y * textureWidth + x];
    }
    
    public int getWidth() { return textureWidth; }
    public int getHeight() { return textureHeight; }
    public BufferedImage getImage() { return textureImage; }
    public boolean isLoaded() { return loaded; }
}