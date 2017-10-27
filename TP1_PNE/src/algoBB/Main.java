package algoBB;
// quadri@lri.fr
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
		    if( (nodeFils.p <= poidsMax && nodeFils.c > utiliteMax) || 
		    		(nodeFils.p <= poidsMax && nodeFils.c == utiliteMax && finalNode.c == 0 && utiliteMax > 0) )  // Lower than max weight - better usefulness (or same than glouton algorithm but only first time)
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
		float dEdx = 1,dEdy = 1;
		int i;
		for(i = 0; i < 1000 && erreur > 0.01; i++)
		{
			E = (x-1)*(x-2) + (y+3)*(y+4); //equation
			dEdx = 2*x-3; //dérivate by x
			dEdy = 2*y+7; //derivate by y
			x -= nu*dEdx; 
			y -= nu*dEdy;
			erreurx = (dEdx > 0) ? dEdx : -dEdx; //absolute value
			erreury = (dEdy > 0) ? dEdy : -dEdy; //absolute value
			erreur = (erreurx > erreury) ? erreurx : erreury; //we take the biggest error
		}
		System.out.println("i = "+i+", x = "+x+", y = "+y+", E = "+E);
		System.out.println("dE/dx = "+dEdx+", dE/dy = "+dEdy);
	}
	/*
	public List<Objet> creationListKeyboard()
	{
		List<Objet> objets = Arrays.asList();
		return objets
	}*/
	public static void main(String[] args) 
	{
		/** Data creation - Test Algorithm BB **/
		int poidsMax = 17;
		List<Objet> objets = Arrays.asList(
			/*
			new Objet(3,8),
			new Objet(7,18),
			new Objet(9,20),
			new Objet(6,11) );*/
			new Objet(6,10),
			new Objet(3,36),
			new Objet(7,40) );	
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
		
		/** ---------------------------------Raisonnement BB multi-constraints---------------------------------**/
		/** On a un ensemble de contraintes w1...wn et une fonction objectif f
		 * On cherche à représenter le problème sous une seule contrainte
		 * On va additionner l'ensemble des contraintes pour obtenir une contrainte finale donnée par le vecteur w = (1 1 ... 1) 
		 * (La dimension du vecteur w est égale au nombre de contraintes w1...wn = n)
		 * On cherche a minimiser notre ensemble f et w1...wn à l'aide du vecteur d'agrégation w
		 * w n'est pas performant actuellement et pourrait être améliorer
		 * On va lancer la descente de gradient pour affiner w, notre but étant d'obtenir un vecteur qui minimise au maximum notre ensemble f + w1...wn
		 * Pour réaliser cela on doit choisir la direction de descente qui est basée sur le gradient de notre fonction objectif
		 * (Il faut prendre en compte que la dimension du gradient de f peut ne pas être égale à celle de w (nombre de contraintes) )
		 * Une fois cela pris en compte, on peut utiliser l'algorithme de gradient donné sur le polycopié et présent ci-dessus
		 * A chaque étape on va jouer sur l'un des coefficients du facteur pour se rapprocher de la direction évaluée et affiner notre w
		 * On obtiendra finalement un vecteur d'agrégation w qui pourra minimiser de facon optimale f et w1...wn
		**/
	}

}
