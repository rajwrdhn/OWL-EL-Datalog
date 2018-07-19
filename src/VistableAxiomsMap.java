import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * Class for deleting and adding in HashMap
 * @author raj
 * Unused Class, Not required
 */
public class VistableAxiomsMap extends Normalize {

	//protected static Map<Integer, Set<OWLAxiom>> crunchMap = new HashMap<>();
	protected static int v_iter_mapkey = 0 ;

	public VistableAxiomsMap(OWLDataFactory factory) {
		super(factory);
		// TODO Auto-generated constructor stub
	}

	public void crunchCleanNormalisedAxiomFromMap(int keyA) {		
		this.v_Iterable_MapAxioms.remove(keyA);		
	}

	public void addAxiomToMap (int number, Set<OWLAxiom> axioms) {
		v_Iterable_MapAxioms.put(number, axioms);		
	}

}
