import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.vlog4j.core.reasoner.Algorithm;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;

//Main class upload ontology here
public class InferenceForOWLELMain {
	protected final Set<String> v_individualNames;
	protected final Set<String> v_classNames;
	protected final Set<String> v_roleNames;
	
	public InferenceForOWLELMain() {
		v_individualNames = new HashSet<>();
		v_classNames = new HashSet<>();
		v_roleNames = new HashSet<>();
	}
	
	public void FinalReasoning() {
		final Reasoner reasoner = Reasoner.getInstance();
		reasoner.setAlgorithm(Algorithm.SKOLEM_CHASE);
	}
	
	public void inputTranslation(OWLOntology onto) {
		onto.individualsInSignature().forEach(x -> v_individualNames.add("{nom("+x+")}"));
		onto.objectPropertiesInSignature().forEach(x -> v_roleNames.add("{rol("+x+")}"));
		onto.classesInSignature().forEach(x -> v_classNames.add("{cls("+x+")}") );
	}
	
	public OWLOntology loadOntology(String fileadd) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		File file = new File(fileadd);
		OWLOntology onto = man.loadOntologyFromOntologyDocument(file);
		OWLDataFactory factory = man.getOWLDataFactory();
		OWLOntologyID ontID = onto.getOntologyID();
		Normalize norm = new Normalize(factory, ontID);	
		Set<OWLAxiom> axioms= norm.getFromOntology(onto);
		OWLOntology ont = man.createOntology(axioms);
		
		return ont;
	}
	public static void main(String args[]){
		System.out.println();
		InferenceForOWLELMain elmain = new InferenceForOWLELMain();
		StopWatch timer = new StopWatch();
		timer.start("Normalize EL-Ontology! ");
		String file = args[0];
		try {
			elmain.loadOntology(file);
			
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		timer.stop("Done!");
	}
}
