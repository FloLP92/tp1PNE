package algoBB;

import java.util.Comparator;

/**Representation of an object with weight, usefulness and ratio between the two of them**/
public class Objet implements Comparable<Objet>
{
	float p; /** Weight **/
	int c; /** Usefulness **/
	float r; /** Ratio Usefulness/Weight **/
    
    public Objet(int weight, int usefulness)
    {
    	p = weight;
    	c = usefulness;
    	r = c/p;
    }
    public float getWeight(){return p;}
    public float getUse(){return c;}
    /**Comparator based on ratio**/
	@Override
	public int compareTo(Objet other) {
		return (this.r < other.r) ? 1 : ((this.r == other.r) ? 0 : -1);
	}
	/**Comparator based on Usefulness**/
	static Comparator<Objet> getUsefulnessComparator()
	{
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

