// RaycasterEngine.java - Using ImageRender for 3.28 feet blocks
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.*;

public class RaycasterEngine extends JFrame implements KeyListener, MouseListener, MouseMotionListener {
    private static final int SCREEN_WIDTH = 1024;
    private static final int SCREEN_HEIGHT = 768;
    
    private Canvas canvas;
    private BufferStrategy bufferStrategy;
    private BufferedImage screenBuffer;
    private int[] pixels;
    private Robot robot;
    
    private Map map;
    private Player player;
    private ImageRender imageRender;
    
    private boolean mouseGrabbed = false;
    private int centerX, centerY;
    
    private long frameCount = 0;
    private long lastFPSTime = 0;
    private int currentFPS = 0;
    private boolean showFPS = true;
    private boolean running = true;
    
    private boolean useTexture = true;
    
    public RaycasterEngine() {
        setTitle("Raycaster Engine - 3.28 ft Blocks");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(SCREEN_WIDTH + 16, SCREEN_HEIGHT + 39);
        setLocationRelativeTo(null);
        
        map = new Map();
        player = new Player(map);
        imageRender = ImageRender.getInstance();
        
        canvas = new Canvas() {
            @Override
            public void paint(Graphics g) {
                render();
            }
            
            @Override
            public void update(Graphics g) {
                render();
            }
        };
        canvas.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        canvas.setBackground(Color.BLACK);
        canvas.setIgnoreRepaint(true);
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.setFocusable(true);
        canvas.requestFocus();
        
        add(canvas);
        pack();
        setVisible(true);
        
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        
        screenBuffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((java.awt.image.DataBufferInt) screenBuffer.getRaster().getDataBuffer()).getData();
        
        updateCenterPosition();
        new Thread(this::gameLoop).start();
    }
    
    private void updateCenterPosition() {
        try {
            Point canvasLocation = canvas.getLocationOnScreen();
            centerX = canvasLocation.x + SCREEN_WIDTH / 2;
            centerY = canvasLocation.y + SCREEN_HEIGHT / 2;
        } catch (Exception e) {
        }
    }
    
    private void gameLoop() {
        long lastTime = System.nanoTime();
        final double TARGET_FPS = 60.0;
        final double NS_PER_FRAME = 1000000000.0 / TARGET_FPS;
        double delta = 0;
        
        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / NS_PER_FRAME;
            lastTime = currentTime;
            
            if (delta >= 1) {
                update();
                render();
                delta--;
                
                frameCount++;
                if (System.currentTimeMillis() - lastFPSTime >= 1000) {
                    currentFPS = (int)frameCount;
                    frameCount = 0;
                    lastFPSTime = System.currentTimeMillis();
                    if (showFPS) {
                        setTitle("Raycaster Engine - 3.28 ft Blocks | FPS: " + currentFPS);
                    }
                }
            }
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void update() {
        player.update();
        
        if (mouseGrabbed) {
            try {
                Point mousePos = MouseInfo.getPointerInfo().getLocation();
                updateCenterPosition();
                
                int deltaX = mousePos.x - centerX;
                int deltaY = mousePos.y - centerY;
                
                if (deltaX != 0 || deltaY != 0) {
                    player.rotate(deltaX, deltaY);
                    robot.mouseMove(centerX, centerY);
                }
                
            } catch (Exception e) {
            }
        }
    }
    
    private void render() {
        Arrays.fill(pixels, 0xFF000000);
        
        // Solid floor
        int floorColor = (80 << 16) | (80 << 8) | 80;
        Arrays.fill(pixels, 0, SCREEN_WIDTH * SCREEN_HEIGHT, floorColor);
        
        double fov = Math.PI / 3.0;
        double halfFov = fov / 2.0;
        double screenWidth = SCREEN_WIDTH;
        double screenHeight = SCREEN_HEIGHT;
        
        double bobOffset = player.getBobOffset();
        int mapSize = map.getSize();
        
        double playerX = player.getX();
        double playerY = player.getY();
        double playerAngle = player.getAngle();
        double playerPitch = player.getPitch();
        
        boolean useTex = useTexture && imageRender != null && imageRender.isLoaded();
        double blockSize = ImageRender.BLOCK_SIZE_FEET;
        
        for (int x = 0; x < SCREEN_WIDTH; x++) {
            double rayAngle = playerAngle - halfFov + (x / screenWidth) * fov;
            
            double dirX = Math.cos(rayAngle);
            double dirY = Math.sin(rayAngle);
            
            double mapX = Math.floor(playerX);
            double mapY = Math.floor(playerY);
            
            double deltaDistX = Math.abs(1.0 / dirX);
            double deltaDistY = Math.abs(1.0 / dirY);
            
            double stepX, stepY;
            double sideDistX, sideDistY;
            
            if (dirX < 0) {
                stepX = -1;
                sideDistX = (playerX - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - playerX) * deltaDistX;
            }
            
            if (dirY < 0) {
                stepY = -1;
                sideDistY = (playerY - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - playerY) * deltaDistY;
            }
            
            boolean hit = false;
            int side = 0;
            
            for (int i = 0; i < 100; i++) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                
                int mapIndexX = (int)mapX;
                int mapIndexY = (int)mapY;
                
                if (mapIndexX < 0 || mapIndexX >= mapSize || mapIndexY < 0 || mapIndexY >= mapSize) {
                    break;
                }
                
                if (map.isWall(mapIndexX, mapIndexY)) {
                    hit = true;
                    break;
                }
            }
            
            if (hit) {
                double perpDist;
                if (side == 0) {
                    perpDist = (mapX - playerX + (1 - stepX) / 2.0) / dirX;
                } else {
                    perpDist = (mapY - playerY + (1 - stepY) / 2.0) / dirY;
                }
                
                perpDist *= Math.cos(playerAngle - rayAngle);
                
                // Calculate wall height for 3.28 ft (1 meter) blocks
                double wallHeight = (blockSize * screenHeight) / perpDist;
                double wallTop = screenHeight / 2.0 - wallHeight / 2.0 + playerPitch * screenHeight / 2.0 + bobOffset;
                double wallBottom = screenHeight / 2.0 + wallHeight / 2.0 + playerPitch * screenHeight / 2.0 + bobOffset;
                
                double shade = Math.max(0.2, Math.min(1.0, 1.0 - (perpDist / 25.0) * 1.0));
                if (side == 1) shade *= 0.75;
                
                int top = (int)Math.max(0, wallTop);
                int bottom = (int)Math.min(SCREEN_HEIGHT, wallBottom);
                
                if (top < bottom) {
                    int wallHeightPixels = bottom - top;
                    
                    // Calculate wallX texture coordinate (0 to 1 for full texture)
                    double wallX;
                    if (side == 0) {
                        wallX = (mapY - playerY + (1 - stepY) / 2.0) / perpDist;
                    } else {
                        wallX = (mapX - playerX + (1 - stepX) / 2.0) / perpDist;
                    }
                    wallX -= Math.floor(wallX);
                    
                    for (int y = top; y < bottom; y++) {
                        double v = (double)(y - top) / wallHeightPixels;
                        double u = wallX;
                        
                        int color;
                        if (useTex) {
                            // Use ImageRender for texture mapping
                            color = imageRender.getPixelShaded(u, v, shade);
                        } else {
                            // Fallback to solid color
                            int mapIndexX = (int)mapX;
                            int mapIndexY = (int)mapY;
                            Color wallColor = map.getWallColorShaded(mapIndexX, mapIndexY, shade);
                            color = (wallColor.getRed() << 16) | (wallColor.getGreen() << 8) | wallColor.getBlue();
                        }
                        
                        if (y >= 0 && y < SCREEN_HEIGHT) {
                            pixels[y * SCREEN_WIDTH + x] = color;
                        }
                    }
                }
            }
        }
        
        Graphics2D g2d = screenBuffer.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawCrosshair(g2d);
        drawWeapon(g2d);
        drawMinimap(g2d);
        drawHUD(g2d);
        
        g2d.dispose();
        
        Graphics g = null;
        try {
            g = bufferStrategy.getDrawGraphics();
            if (g != null) {
                g.drawImage(screenBuffer, 0, 0, null);
            }
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
        
        if (bufferStrategy != null) {
            bufferStrategy.show();
        }
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    private void drawMinimap(Graphics2D g) {
        int miniSize = 5;
        int miniX = 10;
        int miniY = 10;
        int mapSize = map.getSize();
        
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRoundRect(miniX - 3, miniY - 3, mapSize * miniSize + 6, mapSize * miniSize + 6, 5, 5);
        
        int[][] mapData = map.getMapData();
        Color[][] wallColors = map.getWallColors();
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                if (mapData[i][j] != 0) {
                    g.setColor(wallColors[i][j] != null ? wallColors[i][j] : new Color(200, 200, 200));
                } else {
                    g.setColor(new Color(40, 40, 40, 200));
                }
                g.fillRect(miniX + i * miniSize, miniY + j * miniSize, miniSize, miniSize);
            }
        }
        
        int px = miniX + (int)(player.getX() * miniSize);
        int py = miniY + (int)(player.getY() * miniSize);
        g.setColor(new Color(255, 0, 0, 80));
        g.fillOval(px - 5, py - 5, 10, 10);
        g.setColor(Color.RED);
        g.fillOval(px - 2, py - 2, 4, 4);
        g.setColor(new Color(0, 255, 0, 220));
        g.setStroke(new BasicStroke(2));
        int endX = px + (int)(Math.cos(player.getAngle()) * 12);
        int endY = py + (int)(Math.sin(player.getAngle()) * 12);
        g.drawLine(px, py, endX, endY);
        g.setColor(new Color(255, 255, 255, 150));
        g.drawRoundRect(miniX - 3, miniY - 3, mapSize * miniSize + 6, mapSize * miniSize + 6, 5, 5);
    }
    
    private void drawCrosshair(Graphics2D g) {
        int cx = SCREEN_WIDTH / 2;
        int cy = SCREEN_HEIGHT / 2;
        int size = 10;
        int gap = 5;
        
        g.setColor(new Color(255, 255, 255, 200));
        g.setStroke(new BasicStroke(2));
        g.drawLine(cx - size - gap, cy, cx - gap, cy);
        g.drawLine(cx + gap, cy, cx + size + gap, cy);
        g.drawLine(cx, cy - size - gap, cx, cy - gap);
        g.drawLine(cx, cy + gap, cx, cy + size + gap);
        g.fillRect(cx - 2, cy - 2, 4, 4);
    }
    
    private void drawWeapon(Graphics2D g) {
        double bob = player.getWeaponBob();
        int weaponX = SCREEN_WIDTH - 180;
        int weaponY = SCREEN_HEIGHT - 200 + (int)bob;
        
        g.setColor(new Color(60, 60, 60, 220));
        g.fillRoundRect(weaponX - 15, weaponY - 20, 30, 100, 10, 10);
        g.setColor(new Color(40, 40, 40, 220));
        g.fillRoundRect(weaponX - 10, weaponY + 50, 20, 40, 5, 5);
        g.setColor(new Color(50, 50, 50, 220));
        g.fillRect(weaponX - 4, weaponY - 30, 8, 20);
        g.setColor(new Color(45, 45, 45, 200));
        g.drawArc(weaponX - 8, weaponY + 20, 16, 20, 0, 180);
    }
    
    private void drawHUD(Graphics2D g) {
        if (showFPS) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRoundRect(SCREEN_WIDTH - 110, 10, 100, 25, 5, 5);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("FPS: " + currentFPS, SCREEN_WIDTH - 100, 28);
            
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRoundRect(10, SCREEN_HEIGHT - 140, 300, 130, 5, 5);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("Pos: " + String.format("%.1f", player.getX()) + ", " + String.format("%.1f", player.getY()), 20, SCREEN_HEIGHT - 115);
            g.drawString("Angle: " + String.format("%.2f", player.getAngle()), 20, SCREEN_HEIGHT - 95);
            g.drawString(imageRender.getBlockSizeInfo(), 20, SCREEN_HEIGHT - 75);
            g.drawString(imageRender.getTextureInfo(), 20, SCREEN_HEIGHT - 55);
            g.drawString("Texture: " + (imageRender.isLoaded() && useTexture ? "ON" : "OFF"), 20, SCREEN_HEIGHT - 35);
            
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRoundRect(10, SCREEN_HEIGHT - 25, 580, 20, 5, 5);
            g.setColor(new Color(200, 200, 200, 200));
            g.setFont(new Font("Arial", Font.PLAIN, 11));
            g.drawString("WASD: Move | Mouse: Look | T: Texture | C: Random Map | R: Reset | F: FPS", 20, SCREEN_HEIGHT - 10);
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_W: player.setForward(true); break;
            case KeyEvent.VK_S: player.setBackward(true); break;
            case KeyEvent.VK_A: player.setLeft(true); break;
            case KeyEvent.VK_D: player.setRight(true); break;
            case KeyEvent.VK_ESCAPE:
                mouseGrabbed = false;
                canvas.setCursor(Cursor.getDefaultCursor());
                break;
            case KeyEvent.VK_ENTER: grabMouse(); break;
            case KeyEvent.VK_F: showFPS = !showFPS; break;
            case KeyEvent.VK_R: player.reset(); break;
            case KeyEvent.VK_T: useTexture = !useTexture; break;
            case KeyEvent.VK_C: 
                map.generateRandomMap();
                player.reset();
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch(key) {
            case KeyEvent.VK_W: player.setForward(false); break;
            case KeyEvent.VK_S: player.setBackward(false); break;
            case KeyEvent.VK_A: player.setLeft(false); break;
            case KeyEvent.VK_D: player.setRight(false); break;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void mouseClicked(MouseEvent e) {
        grabMouse();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mouseDragged(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {}
    
    private void grabMouse() {
        try {
            mouseGrabbed = true;
            canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            updateCenterPosition();
            robot.mouseMove(centerX, centerY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RaycasterEngine());
    }
}