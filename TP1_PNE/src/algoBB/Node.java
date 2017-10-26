package algoBB;

import java.util.ArrayList;
import java.util.Collections;

public class Node 
{
	int pos;        /** Position of node in tab of objects **/
    int c;          /** Actual usefulness of node **/
    float borneSup;   /** Upper bound of node **/
    float p;        /** Actual weight of node **/
    ArrayList<Objet> tabObj;
    
    public Node()
    {
    	tabObj = new ArrayList<Objet>();
    	p = 0;
    }
    /** Get and Set **/
    public ArrayList<Objet> getTabObj(){return tabObj;}
    public void setTabObj(ArrayList<Objet> chTabObj){tabObj = chTabObj;}
    public int getPos(){return pos;}
    public float getUse(){return c;}
    public void setUse(int usefulness){c = usefulness;}
    public void setPos(int chPos){pos = chPos;}
    public float getWeight(){return p;}
    public void setWeight(float weight){p = weight;}
    public void setBorneSup(int chBorne){borneSup = chBorne;}
    /** Object belongs to that path **/
    public ArrayList<Objet> addObject(Objet o){ 
    	ArrayList<Objet> a = new ArrayList<Objet>(tabObj);
    	Collections.copy(a, tabObj);
    	a.add(o);
    	return a;
    }public String toString()
    {
    	String a = "The bag contains : \n";
    	for (Objet o : tabObj)
    	{
    		a+= o.toString()+"\n";
    	}
    	return a;
    }
}
