import java.io.File;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

//Main class upload ontology here
public class InferenceForOWLELMain {
	
	public InferenceForOWLELMain() {
		
	}
	
	
	public OWLOntology Loadontology(String fileadd) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		File file = new File(fileadd);
		OWLOntology onto = man.loadOntologyFromOntologyDocument(file);
		OWLDataFactory factory = man.getOWLDataFactory();
		OWLOntologyID ontID = onto.getOntologyID();
		Normalize norm = new Normalize(factory, ontID);	
		Set<OWLAxiom> axioms= norm.getFromOntology(onto);
		getInputTranslation(axioms);
		OWLOntology ont = man.createOntology(axioms);
		
		return ont;
	}
	
	public void getInputTranslation(Set<OWLAxiom> axioms) {
		InputTranslation inputTra = new InputTranslation(axioms);
	}
	
	public static void main(String args[]){
		System.out.println();
		InferenceForOWLELMain elmain = new InferenceForOWLELMain();
		StopWatch timer = new StopWatch();
		timer.start("Normalize EL-Ontology! ");
		String file = args[0];
		try {
			elmain.Loadontology(file);
			
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.stop("Done!");
	}
}
