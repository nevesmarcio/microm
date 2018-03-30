package marcio.transform;


public class SplineVertex {

    public Coordinate cp1;
    public Coordinate p;
    public Coordinate cp2;


    // For straight lines
    public SplineVertex(double x, double y) {
        construct(x, y, x, y, x, y);
    }

    public SplineVertex(Coordinate p) {
        construct(p, p, p);
    }

    // For cubic splines
    public SplineVertex(double x, double y, double x1, double y1, double x2, double y2) {
        construct(x, y, x1, y1, x2, y2);
    }

    public SplineVertex(Coordinate p, Coordinate cp1, Coordinate cp2) {
        construct(p, cp1, cp2);
    }


    private void construct(double x, double y, double x1, double y1, double x2, double y2) {
        this.cp1 = new Coordinate(x1, y1);
        this.p = new Coordinate(x, y);
        this.cp2 = new Coordinate(x2, y2);
    }

    private void construct(Coordinate p, Coordinate cp1, Coordinate cp2) {
        this.cp1 = cp1;
        this.p = p;
        this.cp2 = cp2;
    }

    //region accelerator methods
    public SplineVertex offsetXYBy(double x, double y) {
        this.cp1.x += x;
        this.cp1.y += y;
        this.p.x += x;
        this.p.y += y;
        this.cp2.x += x;
        this.cp2.y += y;
        return this;
    }

    public SplineVertex offsetXBy(double x) {
        this.cp1.x += x;
        this.p.x += x;
        this.cp2.x += x;
        return this;
    }

    public SplineVertex offsetYBy(double y) {
        this.cp1.y += y;
        this.p.y += y;
        this.cp2.y += y;
        return this;
    }

    public SplineVertex setXYTo(double x, double y) {
        this.cp1.x = x;
        this.cp1.y = y;
        this.p.x = x;
        this.p.y = y;
        this.cp2.x = x;
        this.cp2.y = y;
        return this;
    }

    public SplineVertex setXTo(double x) {
        this.cp1.x = x;
        this.p.x = x;
        this.cp2.x = x;

        return this;
    }

    public SplineVertex setYTo(double y) {
        this.cp1.y = y;
        this.p.y = y;
        this.cp2.y = y;

        return this;
    }

    public SplineVertex offsetPBy(double x, double y){
        this.p.x+=x;
        this.p.y+=y;
        return  this;
    }

    public SplineVertex offsetCP1By(double x, double y){
        this.cp1.x+=x;
        this.cp1.y+=y;
        return  this;
    }

    public SplineVertex offsetCP2By(double x, double y){
        this.cp2.x+=x;
        this.cp2.y+=y;
        return  this;
    }

    public SplineVertex setPTo(double x, double y){
        this.p.x=x;
        this.p.y=y;
        return  this;
    }

    public SplineVertex setCP1To(double x, double y){
        this.cp1.x=x;
        this.cp1.y=y;
        return  this;
    }

    public SplineVertex setCP2To(double x, double y){
        this.cp2.x=x;
        this.cp2.y=y;
        return  this;
    }

    public SplineVertex getCopy() {
        return new SplineVertex(p, cp1, cp2);
    }

    //endregion accelerator methods
}
