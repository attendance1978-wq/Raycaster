// ImageRender.java - Handles 3.28 feet block rendering with textures
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageRender {
    private BufferedImage textureImage;
    private int[] texturePixels;
    private int textureWidth;
    private int textureHeight;
    private boolean loaded = false;
    private static ImageRender instance = null;
    
    // Block size in feet (3.28 feet = 1 meter)
    public static final double BLOCK_SIZE_FEET = 3.28;
    public static final double BLOCK_SIZE_METERS = 1.0;
    
    private ImageRender() {
        loadTexture();
    }
    
    public static ImageRender getInstance() {
        if (instance == null) {
            instance = new ImageRender();
        }
        return instance;
    }
    
    private void loadTexture() {
        try {
            String[] paths = {
                "assets/image/image.png",
                "assets/image.png",
                "image.png",
                "assets/textures/image.png",
                "../assets/image/image.png"
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
                System.out.println("✓ ImageRender: Texture loaded successfully!");
                System.out.println("  Path: " + file.getAbsolutePath());
                System.out.println("  Size: " + textureWidth + "x" + textureHeight);
                System.out.println("  Block Size: " + BLOCK_SIZE_FEET + " feet (" + BLOCK_SIZE_METERS + " meter)");
            } else {
                System.err.println("✗ ImageRender: Texture not found at: assets/image/image.png");
                createMinecraftStyleTexture();
            }
        } catch (Exception e) {
            System.err.println("✗ ImageRender: Failed to load texture: " + e.getMessage());
            createMinecraftStyleTexture();
        }
    }
    
    private void createMinecraftStyleTexture() {
        textureWidth = 64;
        textureHeight = 64;
        texturePixels = new int[textureWidth * textureHeight];
        
        // Create a Minecraft-style grass block texture
        for (int y = 0; y < textureHeight; y++) {
            for (int x = 0; x < textureWidth; x++) {
                int r, g, b;
                
                // Top section - Grass (green)
                if (y < 8) {
                    r = 80 + (int)(Math.random() * 40);
                    g = 160 + (int)(Math.random() * 50);
                    b = 40 + (int)(Math.random() * 30);
                }
                // Transition
                else if (y < 12) {
                    r = 90 + (int)(Math.random() * 30);
                    g = 140 + (int)(Math.random() * 30);
                    b = 50 + (int)(Math.random() * 20);
                }
                // Dirt section
                else if (y < 20) {
                    r = 130 + (int)(Math.random() * 30);
                    g = 110 + (int)(Math.random() * 25);
                    b = 70 + (int)(Math.random() * 25);
                }
                // Stone section
                else {
                    r = 150 + (int)(Math.random() * 40);
                    g = 145 + (int)(Math.random() * 35);
                    b = 140 + (int)(Math.random() * 30);
                }
                
                // Add texture details (small cracks/patterns)
                if ((x % 16 < 2 && y % 16 < 2) || (x % 16 > 14 && y % 16 > 14)) {
                    r += 20;
                    g += 20;
                    b += 20;
                }
                
                // Add some noise
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
        System.out.println("✓ ImageRender: Created Minecraft-style fallback texture");
        System.out.println("  Size: " + textureWidth + "x" + textureHeight);
        System.out.println("  Block Size: " + BLOCK_SIZE_FEET + " feet (" + BLOCK_SIZE_METERS + " meter)");
    }
    
    // Get a pixel from the texture with proper UV mapping
    public int getPixel(double u, double v) {
        if (!loaded || texturePixels == null) {
            return 0x888888;
        }
        
        // Clamp UV coordinates (0.0 to 1.0)
        if (u < 0) u = 0;
        if (v < 0) v = 0;
        if (u > 1.0) u = 1.0;
        if (v > 1.0) v = 1.0;
        
        int x = (int)(u * (textureWidth - 1));
        int y = (int)(v * (textureHeight - 1));
        
        x = Math.max(0, Math.min(x, textureWidth - 1));
        y = Math.max(0, Math.min(y, textureHeight - 1));
        
        return texturePixels[y * textureWidth + x];
    }
    
    // Get pixel with shading
    public int getPixelShaded(double u, double v, double shade) {
        int pixel = getPixel(u, v);
        
        int red = (int)(((pixel >> 16) & 0xFF) * shade);
        int green = (int)(((pixel >> 8) & 0xFF) * shade);
        int blue = (int)((pixel & 0xFF) * shade);
        
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));
        
        return (red << 16) | (green << 8) | blue;
    }
    
    // Render a full block face with texture
    public void renderBlockFace(int[] pixels, int screenWidth, int screenHeight, 
                                 int x, int top, int bottom, 
                                 double wallX, double wallY, double shade) {
        if (top >= bottom) return;
        if (top < 0) top = 0;
        if (bottom > screenHeight) bottom = screenHeight;
        
        int wallHeight = bottom - top;
        
        for (int y = top; y < bottom; y++) {
            double v = (double)(y - top) / wallHeight;
            double u = wallX;
            
            int color = getPixelShaded(u, v, shade);
            pixels[y * screenWidth + x] = color;
        }
    }
    
    // Render a block face with scaling (for 3.28 feet blocks)
    public void renderBlockFaceScaled(int[] pixels, int screenWidth, int screenHeight,
                                       int x, int top, int bottom,
                                       double wallX, double wallY, double shade,
                                       double blockHeight) {
        if (top >= bottom) return;
        if (top < 0) top = 0;
        if (bottom > screenHeight) bottom = screenHeight;
        
        // Calculate how much of the texture to show based on block height
        double scale = blockHeight / BLOCK_SIZE_FEET;
        
        for (int y = top; y < bottom; y++) {
            double v = (double)(y - top) / (bottom - top);
            // Scale v to show full texture on each block face
            double u = wallX;
            
            int color = getPixelShaded(u, v, shade);
            pixels[y * screenWidth + x] = color;
        }
    }
    
    // Check if texture is loaded
    public boolean isLoaded() {
        return loaded;
    }
    
    // Get texture dimensions
    public int getWidth() { return textureWidth; }
    public int getHeight() { return textureHeight; }
    public BufferedImage getImage() { return textureImage; }
    
    // Get block size information
    public String getBlockSizeInfo() {
        return String.format("Block Size: %.2f feet (%.2f meter)", BLOCK_SIZE_FEET, BLOCK_SIZE_METERS);
    }
    
    // Get texture info
    public String getTextureInfo() {
        if (loaded) {
            return String.format("Texture: %dx%d pixels, %s", 
                               textureWidth, textureHeight, 
                               textureImage != null ? "Loaded" : "Fallback");
        }
        return "Texture: Not Loaded";
    }
}