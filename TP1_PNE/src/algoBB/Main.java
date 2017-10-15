package algoBB;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main 
{
	//Return the lower bound using a greedy algorithm (heuristic) (approached method) 
	static int borneInfGlouton(List<Objet> listObjects,int poidsMax)
	{
		int n = listObjects.size();
		int actualWeight = 0; 
		int borneInfTest = 0;
		for(int i = 0;i<n;i++)
		{ 
			if(actualWeight+listObjects.get(i).p > poidsMax) //If we can add the next item
				continue;
			actualWeight += listObjects.get(i).getWeight(); //add weight to bag
			borneInfTest += listObjects.get(i).c; //add utility to bag
		}
		return borneInfTest; 
	}
	
	//Return the upper bound using Fayard and Plateau method
	static float borneSupFayard(Node node, List<Objet> listObjects,int poidsMax,int n){
		if (node.getWeight() >= poidsMax)
			return 0;
		int i = node.getPos();
		float actualUse = node.getUse();
		float actualWeight = node.getWeight();
		while ((i < n) && (actualWeight + listObjects.get(i).getWeight() <= poidsMax)) //Like Glouton method
	    {
	        actualWeight    += listObjects.get(i).getWeight();
	        actualUse += listObjects.get(i).getUse();
	        i++;
	    }
		if (i < n) // add a fraction of the next item to reach maximum weight of the bag
		{
			float fraction = (poidsMax - actualWeight) / listObjects.get(i).getWeight();
			actualUse += fraction*listObjects.get(i).getUse();
		}
		return actualUse;
	}
	
	public static Node algoBB(List<Objet> objets, int poidsMax,int n)
	{
		Queue<Node> queue = new LinkedList<Node>(); //Creation of an empty node 
		Node nodeRacine;
		Node finalNode = new Node();
		nodeRacine = new Node();
		nodeRacine.setWeight(0);
		nodeRacine.setPos(-1);
		queue.add(nodeRacine);
		int utiliteMax = borneInfGlouton(objets, poidsMax); // get inf bound  
		for (Node nodePere; (nodePere = queue.poll()) != null;)
		{
		    if (nodePere.getPos() == n-1) //Every object tested
		    	continue;
		    // Case 1 - We put the object in the bag ---------------------
		    Node nodeFils = new Node();
		    nodeFils.setPos(nodePere.pos+1); // We are 1 level lower in tree
		    nodeFils.setWeight(nodePere.p + objets.get(nodeFils.pos).p ) ;  // add previous weight
		    nodeFils.setUse( nodePere.c + objets.get(nodeFils.pos).c ); // add previous utility
		    
		    //recover data of the useful items( items in bag or not) 
		    Objet o = objets.get(nodeFils.pos);
		    nodeFils.setTabObj(nodePere.addObject(o));
		    
		    if(nodeFils.p <= poidsMax && nodeFils.c > utiliteMax) // Lower than max weight - better usefulness
		    {
		    	utiliteMax = nodeFils.c;
		    	finalNode = nodeFils;
		    }
	        float borneSup = borneSupFayard(nodeFils,objets,poidsMax,n);
		    nodeFils.borneSup = borneSup;
	        if (nodeFils.borneSup > utiliteMax) // If we get a better usefulness : keep going with add in queue 
	        {
	        	queue.add(nodeFils);
	        }
	        // Case 2 - We don't put the object in the bag ---------------------
	        Node nodeFilsOut = new Node();
	        nodeFilsOut.pos = nodePere.pos+1;
	        nodeFilsOut.tabObj = nodePere.tabObj;
	        nodeFilsOut.p = nodePere.p;  // add previous weight
	        nodeFilsOut.c = nodePere.c; // add previous utility
	        nodeFilsOut.borneSup = borneSup;
		    if (nodeFilsOut.borneSup > utiliteMax) // If borneSup not greater, useless to go further in tree
		    	queue.add(nodeFilsOut);
		}
		return finalNode;
	}
	public static void main(String[] args) 
	{
		//Data creation - Test Algorithm BB---------------------------------
		int poidsMax = 17;
		List<Objet> objets = Arrays.asList(
			new Objet(3,8),
			new Objet(7,18),
			new Objet(9,20),
			new Objet(6,11) );
		int n  = objets.size();
		Comparator<Objet> comparator = (x, y) -> (x.c < y.c) ? 1 : ((x.c == y.c) ? 0 : -1); // sort on usefulness
		//Collections.sort(objets, Objet.getUsefulnessComparator());
		Collections.sort(objets);
		System.out.println("Here is the possible objects :");
		for(Objet o : objets)
		{
			System.out.println(o.toString());
		}
		Node finalNode = algoBB(objets,poidsMax,n);
		System.out.println("\nMax usefulness is : "+finalNode.c);
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

	}

}
