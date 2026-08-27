// TextureLoader.java
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TextureLoader {
    private BufferedImage textureImage;
    private int[] texturePixels;
    private int textureWidth;
    private int textureHeight;
    
    public TextureLoader(String path) {
        try {
            // Try to load from assets folder
            File file = new File(path);
            if (!file.exists()) {
                // Try alternative path
                file = new File("assets/image/image.png");
            }
            
            if (file.exists()) {
                textureImage = ImageIO.read(file);
                textureWidth = textureImage.getWidth();
                textureHeight = textureImage.getHeight();
                
                // Convert to pixel array
                texturePixels = new int[textureWidth * textureHeight];
                textureImage.getRGB(0, 0, textureWidth, textureHeight, texturePixels, 0, textureWidth);
                
                System.out.println("Loaded texture: " + file.getAbsolutePath() + " (" + textureWidth + "x" + textureHeight + ")");
            } else {
                System.err.println("Texture file not found: " + path);
                createFallbackTexture();
            }
        } catch (Exception e) {
            System.err.println("Failed to load texture: " + e.getMessage());
            createFallbackTexture();
        }
    }
    
    private void createFallbackTexture() {
        textureWidth = 64;
        textureHeight = 64;
        texturePixels = new int[textureWidth * textureHeight];
        
        // Create a brick-like pattern as fallback
        for (int y = 0; y < textureHeight; y++) {
            for (int x = 0; x < textureWidth; x++) {
                int row = y / 16;
                int col = x / 16;
                boolean isEven = (row + col) % 2 == 0;
                
                int r = isEven ? 180 : 120;
                int g = isEven ? 80 : 60;
                int b = isEven ? 60 : 40;
                
                // Add some variation
                if (x % 16 < 2 || y % 16 < 2) {
                    r = 100;
                    g = 100;
                    b = 100;
                }
                
                texturePixels[y * textureWidth + x] = (r << 16) | (g << 8) | b;
            }
        }
        System.out.println("Created fallback texture");
    }
    
    public int getPixel(int x, int y) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x >= textureWidth) x = textureWidth - 1;
        if (y >= textureHeight) y = textureHeight - 1;
        return texturePixels[y * textureWidth + x];
    }
    
    public int getWidth() { return textureWidth; }
    public int getHeight() { return textureHeight; }
    public BufferedImage getImage() { return textureImage; }
    public boolean isLoaded() { return textureImage != null; }
}