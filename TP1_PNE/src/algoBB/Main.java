package algoBB;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/** Main class containing data and BB algorithm **/
public class Main 
{
	/**
	 * @param listObjects
	 * @param poidsMax
	 * @return the lower bound using a greedy algorithm (heuristic)
	 */
	static int borneInfGlouton(List<Objet> listObjects,int poidsMax)
	{
		int n = listObjects.size();
		int actualWeight = 0; 
		int borneInfTest = 0;
		for(int i = 0;i<n;i++)
		{ 
			if(actualWeight+listObjects.get(i).p > poidsMax) //If we can't add the next item
				continue; // next object
			actualWeight += listObjects.get(i).getWeight(); //add weight to bag
			borneInfTest += listObjects.get(i).c; //add usefulness to bag
		}
		return borneInfTest; 
	}
	
	/**
	 * @param node
	 * @param listObjects
	 * @param poidsMax
	 * @param n
	 * @return the upper bound using Fayard and Plateau method
	 */
	static float borneSupFayard(Node node, List<Objet> listObjects,int poidsMax,int n){
		if (node.getWeight() >= poidsMax) // invalid node out of bound
			return 0;
		int i = node.getPos();
		float actualUse = node.getUse();
		float actualWeight = node.getWeight();
		while ((i < n) && (actualWeight + listObjects.get(i).getWeight() <= poidsMax)) //Add object until first one out of bound
	    {
	        actualWeight    += listObjects.get(i).getWeight();
	        actualUse += listObjects.get(i).getUse();
	        i++;
	    }
		if (i < n) // add a fraction of the object that is out of bound
		{
			float fraction = (poidsMax - actualWeight) / listObjects.get(i).getWeight();
			actualUse += fraction*listObjects.get(i).getUse();
		}
		return actualUse;
	}
	
	/**
	 * @param objets
	 * @param poidsMax
	 * @param n
	 * @return the optimal node by using a BB algorithm
	 */
	public static Node algoBB(List<Objet> objets, int poidsMax,int n)
	{
		Queue<Node> queue = new LinkedList<Node>(); //Creation of an empty node 
		Node nodeRacine;
		Node finalNode = new Node();
		nodeRacine = new Node();
		nodeRacine.setWeight(0);
		nodeRacine.setPos(-1);
		queue.add(nodeRacine);
		int utiliteMax = borneInfGlouton(objets, poidsMax); // get inferior bound  
		for (Node nodePere; (nodePere = queue.poll()) != null;) // Queue of node interesting to examine
		{
		    if (nodePere.getPos() == n-1) // Leaf of tree, useless to look below
		    	continue;
		    // Case 1 - We put the object in the bag ---------------------
		    Node nodeFils = new Node();
		    nodeFils.setPos(nodePere.pos+1); // We are 1 level lower in tree
		    nodeFils.setWeight(nodePere.p + objets.get(nodeFils.pos).p ) ;  // add previous weight
		    nodeFils.setUse( nodePere.c + objets.get(nodeFils.pos).c ); // add previous utility
		    
		    // object belongs to the node
		    Objet o = objets.get(nodeFils.pos);
		    nodeFils.setTabObj(nodePere.addObject(o));
		    
		    if(nodeFils.p <= poidsMax && nodeFils.c > utiliteMax) // Lower than max weight - better usefulness
		    {
		    	utiliteMax = nodeFils.c;
		    	finalNode = nodeFils;
		    }
	        float borneSup = borneSupFayard(nodeFils,objets,poidsMax,n);
		    nodeFils.borneSup = borneSup;
	        if (nodeFils.borneSup > utiliteMax) // We can get better usefulness in the nodes below
	        {
	        	queue.add(nodeFils); // we want to continue below
	        }
	        // Case 2 - We don't put the object in the bag ---------------------
	        Node nodeFilsOut = new Node();
	        nodeFilsOut.pos = nodePere.pos+1;
	        nodeFilsOut.tabObj = nodePere.tabObj;
	        nodeFilsOut.p = nodePere.p;  // add previous weight
	        nodeFilsOut.c = nodePere.c; // add previous utility
	        nodeFilsOut.borneSup = borneSup;
		    if (nodeFilsOut.borneSup > utiliteMax) // If borneSup not greater, useless to go further in tree
		    	queue.add(nodeFilsOut); // we want to continue below
		}
		return finalNode;
	}
	
	/**
	 * @param chX
	 * @param chY
	 */
	public static void gradient(int chX, int chY)
	{
		double nu = 0.01;
		System.out.println("(nu = "+nu+")");
		float x = 1;
		System.out.println("(x = "+x+")");
		float y = 6;
		System.out.println("(y = "+y+")");
		float E = 1;
		float erreurx = 1000;
		float erreury = 1000;
		float erreur = 1000;
		float dEdx = 1;
		float dEdy = 1;
		int i;
		for(i = 0; i < 1000 && erreur > 0.01; i++)
		{
			E = (x-1)*(x-2) + (y+3)*(y+4);
			dEdx = 2*x-3;
			dEdy = 2*y+7;
			x -= nu*dEdx;
			y -= nu*dEdy;
			erreurx = (dEdx > 0) ? dEdx : -dEdx;
			erreury = (dEdy > 0) ? dEdy : -dEdy;
			erreur = (erreurx > erreury) ? erreurx : erreury;
		}
		System.out.println("i = "+i+", x = "+x+", y = "+y+", E = "+E);
		System.out.println("dE/dx = "+dEdx+", dE/dy = "+dEdy);
	}
	
	public static void main(String[] args) 
	{
		/** Data creation - Test Algorithm BB **/
		int poidsMax = 17;
		List<Objet> objets = Arrays.asList(
			new Objet(3,8),
			new Objet(7,18),
			new Objet(9,20),
			new Objet(6,11) );
		int n  = objets.size();
		/** sort on usefulness **/
		//Collections.sort(objets, Objet.getUsefulnessComparator()); 
		/** sort on ratio (standard) **/
		Collections.sort(objets);
		System.out.println("Here is the possible objects :");
		for(Objet o : objets){System.out.println(o.toString());}
		Node finalNode = algoBB(objets,poidsMax,n);
		System.out.println("\nMax usefulness is : "+finalNode.c);
		System.out.println("Weight would be : "+finalNode.p);
		System.out.println(finalNode.toString());
		
		//Data creation - Test Algorithm BB multi-constraints---------------------------------
		
		//Create a new Constraint
		List<Objet> objets2 = Arrays.asList(
			new Objet(3,8),
			new Objet(7,18));
		int n2  = objets.size();
		int poidsMax2 = 8;
		Collections.sort(objets2);
		
		Constraint c1 = new Constraint(objets,poidsMax);
		Constraint c2 = new Constraint(objets2,poidsMax2);
		
		gradient(1,2);

	}

}
