public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV,
                  double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }
    public double calcDistance(Planet p) {
        return Math.sqrt((xxPos - p.xxPos) * (xxPos - p.xxPos) + (yyPos - p.yyPos) * (yyPos - p.yyPos));
    }
    public double calcForceExertedBy(Planet p) {
        double G = 6.67e-11;
        double r2 = (xxPos - p.xxPos) * (xxPos - p.xxPos) + (yyPos - p.yyPos) * (yyPos - p.yyPos);
        return G *mass *p.mass / r2;
    }
    public double calcForceExertedByX(Planet p){
        double F = calcForceExertedBy(p);
        double r = calcDistance(p);
        return F * Math.abs(xxPos - p.xxPos) / r;
    }
    public double calcForceExertedByY(Planet p){
        double F = calcForceExertedBy(p);
        double r = calcDistance(p);
        return F * Math.abs(yyPos - p.yyPos) / r;
    }
    public double calcNetForceExertedByX(Planet[] allPlanets){
        double allF = 0;
        for(Planet p : allPlanets){
            if(p == this)
                continue;
            allF += calcForceExertedByX(p);
        }
        return allF;
    }
    public double calcNetForceExertedByY(Planet[] allPlanets){
        double allF = 0;
        for(Planet p : allPlanets){
            if(p == this)
                continue;
            allF += calcForceExertedByY(p);
        }
        return allF;
    }
    public void update(double dt, double fx, double fy){
        double ax = fx / mass;
        double ay = fy / mass;
        xxVel += dt * ax;
        yyVel += dt * ay;
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }
    public void draw(){
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }
}
