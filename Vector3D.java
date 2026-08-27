public class Vector3D {
    public double x, y, z;
    
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3D add(Vector3D v) {
        return new Vector3D(x + v.x, y + v.y, z + v.z);
    }
    
    public Vector3D subtract(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }
    
    public Vector3D multiply(double scalar) {
        return new Vector3D(x * scalar, y * scalar, z * scalar);
    }
    
    public double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }
    
    public double length() {
        return Math.sqrt(x*x + y*y + z*z);
    }
    
    public Vector3D normalize() {
        double len = length();
        if (len == 0) return new Vector3D(0, 0, 0);
        return new Vector3D(x/len, y/len, z/len);
    }
    
    public Vector3D cross(Vector3D v) {
        return new Vector3D(
            y * v.z - z * v.y,
            z * v.x - x * v.z,
            x * v.y - y * v.x
        );
    }
}