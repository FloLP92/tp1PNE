package algoBB;

import java.util.List;

public class Constraint 
{
	List<Objet> mesObjets;
	int poidsMax;
	
	public Constraint(List<Objet> objects,int max){
		mesObjets = objects;
		poidsMax = max;
	}
}
