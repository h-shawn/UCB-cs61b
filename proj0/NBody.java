public class NBody {
    public static double readRadius(String name){
        In in = new In(name);
        int N = in.readInt();
        return in.readDouble();
    }
    public static Planet[] readPlanets(String name){
        In in = new In(name);
        int N = in.readInt();
        Planet [] planets = new Planet[N];
        double radius = in.readDouble();
        for(int i = 0; i < N; i++){
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFileName = in.readString();
            planets[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
        }
        return planets;
    }
    public static void main(String[] args){
        if (args.length < 3) {
            System.out.println("Please enter command line arguments.");
            System.out.println("e.g. java NBody 1.0 2.0 data/planets.txt");
        }
        try {
            double T = Double.parseDouble(args[0]);
            double dt = Double.parseDouble(args[1]);
            String filename = args[2];
            double radius = readRadius(filename);
            Planet[] planets = readPlanets(filename);

            StdDraw.setScale(-radius, radius);
            StdDraw.enableDoubleBuffering();
            StdDraw.clear();

            double t = 0;
            int N = planets.length;
            while(t < T){
                double[] xForces = new double[N];
                double[] yForces = new double[N];
                for(int i = 0; i < N; i++){
                    xForces[i] = planets[i].calcNetForceExertedByX(planets);
                    yForces[i] = planets[i].calcNetForceExertedByY(planets);
                }
                for(int i = 0; i < N; i++)
                    planets[i].update(dt, xForces[i], yForces[i]);
                StdDraw.picture(0, 0, "images/starfield.jpg");
                for(Planet p : planets)
                    p.draw();
                StdDraw.show();
                StdDraw.pause(10);
                t += dt;
            }
            StdOut.printf("%d\n", planets.length);
            StdOut.printf("%.2e\n", radius);
            for (int i = 0; i < planets.length; i++) {
                StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                        planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                        planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
            }
        } catch (NumberFormatException e) {
            System.out.println("Command line arguments are not valid.");
        }
    }
}
