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
import org.semanticweb.vlog4j.core.reasoner.exceptions.EdbIdbSeparationException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.IncompatiblePredicateArityException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.ReasonerStateException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.VLog4jException;
//Main class upload ontology here
public class InferenceForOWLELMain {

	protected static Set<OWLAxiom> v_normalisedAxioms = new HashSet<>();
	protected final String[] v_arrargsList;

	public InferenceForOWLELMain(String[] args) {
		this.v_arrargsList = args;
	}

	/**
	 * 
	 * @param fileadd
	 * @throws OWLOntologyCreationException
	 * @throws VLog4jException
	 * @throws VLog4jException
	 * @throws VLog4jException
	 * @throws IOException
	 */
	public void loadOntology(String fileadd) throws OWLOntologyCreationException, VLog4jException, VLog4jException, VLog4jException, IOException {

		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		OWLOntology onto = man.loadOntologyFromOntologyDocument(new File(fileadd));
		OWLDataFactory factory = man.getOWLDataFactory();		

		Normalize norm = new Normalize(factory);			
		v_normalisedAxioms = norm.getFromOntology(onto);
	}

	/**
	 * 
	 * @param arg1
	 * @throws ReasonerStateException
	 * @throws EdbIdbSeparationException
	 * @throws IncompatiblePredicateArityException
	 * @throws IOException
	 */
	public void applyDatalogRules(String arg1) throws ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		DatalogTranslation dlog = new DatalogTranslation(v_normalisedAxioms, arg1);
		dlog.visitNormalisedAxiomsHash(arg1);
	}
	
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws VLog4jException 
	 */
	public static void main(String []args) throws VLog4jException, IOException{
		
		StopWatch timer = new StopWatch();
		InferenceForOWLELMain inferMain = new InferenceForOWLELMain(args);		

		String file = args[0];

		try {
			
			timer.start("Start Normalisation! ");
			inferMain.loadOntology(file);
			timer.stop("Stop Normalisation");
			
			timer.start("Start EL-Ontology reasoning! ");
			inferMain.applyDatalogRules(args[1]);
			timer.stop("Done!");
		
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}		
	}
}
