
/**
 * Planet
 */
public class Planet {

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    // public Planet()
    // {
    //     xxPos = 0;
    //     yyPos = 0;
    //     xxVel = 0;
    //     yyVel = 0;
    //     mass = 0;
    //     imgFileName = "";
    // }
    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }
    public double calcDistance(Planet that) {
        double dX;
        double dY;
        dX = (this.xxPos - that.xxPos);
        dY = (this.yyPos - that.yyPos);
        return Math.pow((dX * dX + dY * dY), 0.5);
    }
    public double calcForceExertedBy(Planet that) {
        return (6.67 * Math.pow(10, -11) * this.mass * that.mass) / (Math.pow((this.calcDistance(that)), 2));
    }
    public double calcForceExertedByX(Planet that) {
        double dX = that.xxPos - this.xxPos;
        // if (dX > 0) {
        //     return this.calcForceExertedBy(that) * dX / this.calcDistance(that);
        // } else {
        //     return -1.0 * this.calcForceExertedBy(that) * dX / this.calcDistance(that);
        // }
        return this.calcForceExertedBy(that) * dX / this.calcDistance(that);
    }
    public double calcForceExertedByY(Planet that) {
        double dY = that.yyPos - this.yyPos;
        // if (dY > 0) {
        //     return this.calcForceExertedBy(that) * dY / this.calcDistance(that);
        // } else {
        //     return -1.0 * this.calcForceExertedBy(that) * dY / this.calcDistance(that);
        // }
        return this.calcForceExertedBy(that) * dY / this.calcDistance(that);
    }
    public double calcNetForceExertedByX(Planet[] planets) {
        double totalForce = 0.0;
        for (Planet planet : planets) {
            if (this.equals(planet)) {
                continue;
            }
            totalForce += this.calcForceExertedByX(planet);
        }
        return totalForce;
    }
    public double calcNetForceExertedByY(Planet[] planets) {
        double totalForce = 0.0;
        for (Planet planet : planets) {
            if (this.equals(planet)) {
                continue;
            }
            totalForce += this.calcForceExertedByY(planet);
        }
        return totalForce;
    }
    public void update(double s, double xN, double yN) {
        double xAcc = xN / mass;
        double yAcc = yN / mass;
        xxVel += xAcc * s;
        yyVel += yAcc * s;
        xxPos += xxVel * s;
        yyPos += yyVel * s;       
    }
    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
        return;
    }
}