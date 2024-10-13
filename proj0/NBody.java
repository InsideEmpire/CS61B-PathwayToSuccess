public class NBody {
    private static int N;
    private static double R;

    public static double readRadius(String fileName) {
        In in = new In(fileName);
        N = in.readInt();
        R = in.readDouble();
        return R;
    }

    public static Planet[] readPlanets(String fileName) {
        readRadius(fileName);
        In in = new In(fileName);
        Planet[] planets = new Planet[N];
        in.readInt(); // 跳过行数
        in.readDouble(); // 跳过半径
        for (int i = 0; i < N; i++) {
            //planets[i] = new Planet();
            planets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
            // planets[i].xxPos = in.readDouble();
            // planets[i].yyPos = in.readDouble();
            // planets[i].xxVel = in.readDouble();
            // planets[i].yyVel = in.readDouble();
            // planets[i].mass = in.readDouble();
            // planets[i].imgFileName = in.readString();
        }
        return planets;
    }

    private static String imageToDraw = "images/starfield.jpg";

    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();
        double T = Double.valueOf(args[0]);
        double dt = Double.valueOf(args[1]);
        String filename = args[2];
        Planet[] planets = readPlanets(filename);
        StdDraw.setScale(-R, R);
        StdDraw.clear();

        // StdDraw.show();
        // StdDraw.pause(2000);
        double time = 0;
        while (time <= T) {
            StdDraw.clear();
            double[] xForces = new double[N];
            double[] yForces = new double[N];
            for (int i = 0; i < N; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);

            }
            for (int i = 0; i < N; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, imageToDraw);
            for (Planet planet : planets) {
                planet.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            time += dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", R);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
