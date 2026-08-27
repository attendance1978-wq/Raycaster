// Player.java

public class Player {
    private double x, y;
    private double angle;
    private double pitch;
    private double moveSpeed;
    private double mouseSensitivity;
    
    private boolean forward = false;
    private boolean backward = false;
    private boolean left = false;
    private boolean right = false;
    
    private double bobPhase = 0;
    private boolean wasMoving = false;
    private Map map;
    
    public Player(Map map) {
        this.map = map;
        this.x = 10.5;
        this.y = 10.5;
        this.angle = 0.0;
        this.pitch = 0.0;
        this.moveSpeed = 0.08;
        this.mouseSensitivity = 0.005;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getAngle() { return angle; }
    public double getPitch() { return pitch; }
    public double getMoveSpeed() { return moveSpeed; }
    public double getMouseSensitivity() { return mouseSensitivity; }
    public boolean isMoving() { return forward || backward || left || right; }
    public double getBobPhase() { return bobPhase; }
    
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setAngle(double angle) { this.angle = angle; }
    public void setPitch(double pitch) { this.pitch = Math.max(-0.8, Math.min(0.8, pitch)); }
    public void setMoveSpeed(double speed) { this.moveSpeed = speed; }
    public void setMouseSensitivity(double sensitivity) { this.mouseSensitivity = sensitivity; }
    public void setMap(Map map) { this.map = map; }
    
    public void setForward(boolean value) { forward = value; }
    public void setBackward(boolean value) { backward = value; }
    public void setLeft(boolean value) { left = value; }
    public void setRight(boolean value) { right = value; }
    
    public void reset() {
        x = 10.5;
        y = 10.5;
        angle = 0.0;
        pitch = 0.0;
        bobPhase = 0;
        wasMoving = false;
    }
    
    public void update() {
        double moveX = 0;
        double moveY = 0;
        
        if (forward) {
            moveX += Math.cos(angle) * moveSpeed;
            moveY += Math.sin(angle) * moveSpeed;
        }
        if (backward) {
            moveX -= Math.cos(angle) * moveSpeed;
            moveY -= Math.sin(angle) * moveSpeed;
        }
        if (left) {
            moveX += Math.sin(angle) * moveSpeed * 0.8;
            moveY -= Math.cos(angle) * moveSpeed * 0.8;
        }
        if (right) {
            moveX -= Math.sin(angle) * moveSpeed * 0.8;
            moveY += Math.cos(angle) * moveSpeed * 0.8;
        }
        
        double newX = x + moveX;
        double newY = y + moveY;
        
        if (map.isWalkable(newX, y)) {
            x = newX;
        }
        if (map.isWalkable(x, newY)) {
            y = newY;
        }
        
        if (isMoving()) {
            bobPhase += moveSpeed * 2;
            wasMoving = true;
        } else {
            if (wasMoving) {
                bobPhase = 0;
                wasMoving = false;
            }
        }
    }
    
    public void rotate(double deltaX, double deltaY) {
        angle += deltaX * mouseSensitivity;
        pitch = Math.max(-0.8, Math.min(0.8, pitch - deltaY * mouseSensitivity));
    }
    
    public double getBobOffset() {
        return Math.sin(bobPhase) * 2.0;
    }
    
    public double getWeaponBob() {
        return Math.abs(Math.sin(bobPhase)) * 15;
    }
}