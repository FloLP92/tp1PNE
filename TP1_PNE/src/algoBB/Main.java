package algoBB;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main 
{
	//Return the lower bound using a greedy algorithm (heuristic)
	int borneInfGlouton(Objet listObjects[],int poidsMax)
	{
		int n = listObjects.length;
		int actualWeight = 0; 
		int borneInfTest = 0;
		for(int i = 0;i<n;i++)
		{ 
			if(actualWeight+listObjects[i].p > poidsMax)
				continue;
			actualWeight += listObjects[i].getWeight();
			borneInfTest += listObjects[i].c;
		}
		return borneInfTest; 
	}
	//Return the lower bound using Fayard and Plateau method
	static int borneInfFayard(Objet mesObjets[],int poidsSac){
		int n = mesObjets.length;
		float ratioActuel = mesObjets[0].r;
		int position = 0;
		int poidsInf = 0;
		int borneInfTest = 0;
		for(int j = 0;j<n;j++){
			for(int i = 0;i<n;i++){ // Find possible object with better ratio
				if(ratioActuel<mesObjets[i].r && mesObjets[i].getX() == true){
					ratioActuel = mesObjets[i].r;
					position  = i;
				}
			}
			if(poidsInf+mesObjets[position].p > poidsSac)
				break;
			mesObjets[position].x = false;
			poidsInf += mesObjets[position].p;
			borneInfTest += mesObjets[position].c;
			ratioActuel = 0;
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
		while ((i < n) && (actualWeight + listObjects.get(i).getWeight() <= poidsMax))
	    {
	        actualWeight    += listObjects.get(i).getWeight();
	        actualUse += listObjects.get(i).getUse();
	        i++;
	    }
		if (i < n)
		{
			float fraction = (poidsMax - actualWeight) / listObjects.get(i).getWeight();
			actualUse += fraction*listObjects.get(i).getUse();
		}
		return actualUse;
	}
	
	public static float algoBB(List<Objet> objets, int poidsMax,int n)
	{
		Queue<Node> queue = new LinkedList<Node>();
		Node nodeRacine;
		nodeRacine = new Node();
		nodeRacine.setWeight(0);
		nodeRacine.setPos(-1);
		queue.add(nodeRacine);
		float utiliteMax = 0;
		for (Node nodePere; (nodePere = queue.poll()) != null;)
		{
		    if (nodePere.getPos() == n-1) //Every object tested
		    	continue;
		    // Case 1 - We put the object in the bag ---------------------
		    Node nodeFils = new Node();
		    nodeFils.setPos(nodePere.pos+1); // We are 1 level lower in tree
		    nodeFils.setWeight(nodePere.p + objets.get(nodeFils.pos).p ) ;  // add previous weight
		    nodeFils.setUse( nodePere.c + objets.get(nodeFils.pos).c ); // add previous utility
		    if(nodeFils.p <= poidsMax && nodeFils.c > utiliteMax) // Lower than max weight - better usefulness
		        utiliteMax = nodeFils.c;
	        float borneSup = borneSupFayard(nodeFils,objets,poidsMax,n);
		    nodeFils.borneSup = borneSup;
	        if (nodeFils.borneSup > utiliteMax)
	        {
	        	queue.add(nodeFils);
	        }
	        // Case 2 - We don't put the object in the bag ---------------------
	        Node nodeFilsOut = new Node();
	        nodeFilsOut.pos = nodePere.pos+1;
	        nodeFilsOut.p = nodePere.p;  // add previous weight
	        nodeFilsOut.c = nodePere.c; // add previous utility
	        nodeFilsOut.borneSup = borneSup;
		    if (nodeFilsOut.borneSup > utiliteMax)
		    	queue.add(nodeFilsOut);
		}
		return utiliteMax;
	}
	public static void main(String[] args) 
	{
		//Data creation---------------------------------
		int poidsMax = 17;
		int poidsActuel = 0;
		List<Objet> objets = Arrays.asList(
			new Objet(3,8),
			new Objet(7,18),
			new Objet(9,20),
			new Objet(6,11) );
		int n  = objets.size();
		Comparator<Objet> comparator = (x, y) -> (x.c < y.c) ? 1 : ((x.c == y.c) ? 0 : -1); // sort on usefulness
		//Collections.sort(objets, Objet.getUsefulnessComparator());
		Collections.sort(objets);;
		for(Objet o : objets)
		{
			System.out.println(o.toString());
		}
		System.out.println("L'utilite max de ce probleme est : "+algoBB(objets,poidsMax,n));
		
		

	}

}
