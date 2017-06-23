package max;

public class Candidate implements Comparable<Candidate> {
    public double xVal;
    public double fitness;

    public Candidate(double xVal) {
        this.xVal = xVal;
    }

    public int compareTo(Candidate o) {
        return new Double(o.fitness).compareTo(new Double(this.fitness));
    }
}