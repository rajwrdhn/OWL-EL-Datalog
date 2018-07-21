import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.vlog4j.core.reasoner.exceptions.*;
import org.semanticweb.vlog4j.core.reasoner.exceptions.EdbIdbSeparationException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.IncompatiblePredicateArityException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.ReasonerStateException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.VLog4jException;
//Main class upload ontology here
public class InferenceForOWLELMain {

	protected static Set<OWLAxiom> v_normalisedAxioms = new HashSet<>();
	

	public InferenceForOWLELMain() {

	}
	public void loadOntology(String fileadd) throws OWLOntologyCreationException, VLog4jException, VLog4jException, VLog4jException, IOException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		File file = new File(fileadd);
		OWLOntology onto = man.loadOntologyFromOntologyDocument(file);
		OWLDataFactory factory = man.getOWLDataFactory();		
		Normalize norm = new Normalize(factory);	
		norm.getFromOntology(onto);
		
		v_normalisedAxioms = norm.getFromOntology(onto);
/*		for(OWLAxiom axiom: v_normalisedAxioms) {
			System.out.println(axiom);;
		}*/
	}
	
	public void applydDatalogRules() throws ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		DatalogTranslation dlog = new DatalogTranslation(v_normalisedAxioms);
		dlog.visitNormalisedAxiomsHash();
	}
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws VLog4jException 
	 */
	public static void main(String []args) throws VLog4jException, IOException{
		System.out.println();
		StopWatch timer = new StopWatch();
		InferenceForOWLELMain inferMain = new InferenceForOWLELMain();
		timer.start("Start EL-Ontology reasoning! ");
		String file = args[0];
		try {
			inferMain.loadOntology(file);
			inferMain.applydDatalogRules();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		timer.stop("Done!");
	}
}
