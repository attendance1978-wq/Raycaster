public class Quaternion {
    public double w, x, y, z;
    
    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Quaternion fromEuler(double pitch, double yaw, double roll) {
        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);
        
        this.w = cr * cp * cy + sr * sp * sy;
        this.x = sr * cp * cy - cr * sp * sy;
        this.y = cr * sp * cy + sr * cp * sy;
        this.z = cr * cp * sy - sr * sp * cy;
        return this;
    }
    
    public Vector3D rotate(Vector3D v) {
        Quaternion p = new Quaternion(0, v.x, v.y, v.z);
        Quaternion q = this;
        Quaternion result = multiply(q, multiply(p, conjugate(q)));
        return new Vector3D(result.x, result.y, result.z);
    }
    
    private Quaternion multiply(Quaternion a, Quaternion b) {
        return new Quaternion(
            a.w*b.w - a.x*b.x - a.y*b.y - a.z*b.z,
            a.w*b.x + a.x*b.w + a.y*b.z - a.z*b.y,
            a.w*b.y - a.x*b.z + a.y*b.w + a.z*b.x,
            a.w*b.z + a.x*b.y - a.y*b.x + a.z*b.w
        );
    }
    
    private Quaternion conjugate(Quaternion q) {
        return new Quaternion(q.w, -q.x, -q.y, -q.z);
    }
}