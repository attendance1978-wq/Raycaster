public class Matrix4x4 {
    public double[][] m = new double[4][4];
    
    public Matrix4x4() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                m[i][j] = (i == j) ? 1.0 : 0.0;
    }
    
    public static Matrix4x4 perspective(double fov, double aspect, double near, double far) {
        Matrix4x4 mat = new Matrix4x4();
        double tanHalfFov = Math.tan(fov / 2.0);
        mat.m[0][0] = 1.0 / (aspect * tanHalfFov);
        mat.m[1][1] = 1.0 / tanHalfFov;
        mat.m[2][2] = -(far + near) / (far - near);
        mat.m[2][3] = -(2.0 * far * near) / (far - near);
        mat.m[3][2] = -1.0;
        mat.m[3][3] = 0.0;
        return mat;
    }
    
    public Vector3D multiply(Vector3D v) {
        double[] vec = {v.x, v.y, v.z, 1.0};
        double[] result = new double[4];
        for (int i = 0; i < 4; i++) {
            result[i] = 0;
            for (int j = 0; j < 4; j++) {
                result[i] += m[i][j] * vec[j];
            }
        }
        if (result[3] != 0) {
            result[0] /= result[3];
            result[1] /= result[3];
            result[2] /= result[3];
        }
        return new Vector3D(result[0], result[1], result[2]);
    }
}