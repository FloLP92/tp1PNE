package algoBB;

public class Node 
{
	int pos;        // Position of node in tab of objects
    float c;          // Actual usefulness of node
    float borneSup;   // Upper bound of node
    float p;        // Actual weight of node
    
    public Node(int level, int usefulness)
    {
    	
    }
    public Node()
    {
    	
    }
    //Get and Set
    public int getPos(){return pos;}
    public float getUse(){return c;}
    public void setUse(float usefulness){c = usefulness;}
    public void setPos(int chPos){pos = chPos;}
    public float getWeight(){return p;}
    public void setWeight(float weight){p = weight;}
    public void setBorneSup(int chBorne){borneSup = chBorne;}
    public String toString()
    {
    	return "a";
    }
}
