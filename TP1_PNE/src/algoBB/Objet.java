package algoBB;

import java.util.Comparator;

public class Objet implements Comparable<Objet>
{
	float p; // Weight
	int c; // Usefulness
	float r; // Ratio Usefulness/Weight
	boolean fixe; // Present or not in the bag (fixed)
    
    public Objet(int weight, int usefulness)
    {
    	p = weight;
    	c = usefulness;
    	r = c/p;
    	fixe = false;
    }
    public float getWeight(){return p;}
    public float getUse(){return c;}
	@Override
	public int compareTo(Objet other) {
		return (this.r < other.r) ? 1 : ((this.r == other.r) ? 0 : -1);
	}
	static Comparator<Objet> getUsefulnessComparator() {
		return new Comparator<Objet>(){
			@Override
			public int compare(Objet x, Objet y) {
				return (x.c < y.c) ? 1 : ((x.c == y.c) ? 0 : -1); // sort on usefulness
			}
		};
	}

	public String toString()
	{
		return "Object with Weight : "+p+" , Usefulness : "+c+" and Ratio : "+r;
	}
}

