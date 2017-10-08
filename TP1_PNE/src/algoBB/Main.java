package algoBB;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main 
{
	//Return the lower bound using a greedy algorithm
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
		for(j = 0;j<n;j++){
			for(i = 0;i<n;i++){ // Find possible object with better ratio
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
	int borneSupFayard(Objet mesObjets[],int poidsSac){
		float fraction = (float) (poidsSac - poidsInf)/mesObjets[position].p;
		int borneSup = borneInferieurOpt+ fraction*mesObjets[position].c;
		return borneSup;
	}
	//Reset every object as a possible choice
	void remiseAzero(Objet mesObjets[]){ // every object is a possible choice
		for(i = 0;i<n;i++){
			mesObjets[i].x = 0;
		}
	}
	public static void algoBB(Objet objets[], int poidsMax, int n)
	{
		Queue<Node> queue = new LinkedList<Node>();
		Node u,nodeFils;
		u = new Node();
		nodeFils = new Node();
		u.setWeight(0);
		queue.add(u);
		float utiliteMax = 0;
		for (Node nodePere; (nodePere = queue.poll()) != null;)
		{
		    if (nodePere.getPos() == n-1) //Every object tested
		    	continue;
		    // Case 1 - We put the object in the bag ---------------------
		    nodeFils.setPos(2);
		    nodeFils.setPos(nodePere.getPos()+1); // We are 1 level lower in tree
		    nodeFils.setWeight( nodePere.getPos() + objets[nodePere.pos].p ) ;  // add previous weight
		    nodeFils.setUse( nodePere.getUse() + objets[nodePere.getPos()].getC() ); // add previous utility
		    if(nodeFils.p <= poidsMax && nodeFils.c > utiliteMax) // Lower than max weight - better usefulness
		        utiliteMax = nodeFils.getUse();
	        nodeFils.borneSup = borneSupFayard(objets, poidsMax);
	        if (nodeFils.borneSup > utiliteMax)
	        
	        // Case 2 - We don't put the object in the bag ---------------------
		    nodeFils.p = u.p;  // add previous weight
		    nodeFils.c = u.c; // add previous utility
		    if (nodeFils.borneSup > utiliteMax)
		}
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
		Collections.sort(objets, Objet.getUsefulnessComparator());
		for(Objet o : objets)
		{
			System.out.println(o.toString());
		}
		
		

	}

}
