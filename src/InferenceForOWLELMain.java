import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	protected final String[] v_arrargsList;

	public InferenceForOWLELMain(String[] args) {
		this.v_arrargsList = args;
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
		System.out.println();
		StopWatch timer = new StopWatch();
		InferenceForOWLELMain inferMain = new InferenceForOWLELMain(args);
		timer.start("Start EL-Ontology reasoning! ");
		String file = args[0];
		List<String>  argslist = new ArrayList<String>();
/*		if (args.length > 0 && args[0] == file) {
			for (int i = 0; i < args.length; i++) {
				
			}
		}*/
		try {
			inferMain.loadOntology(file);
			inferMain.applyDatalogRules(args[1]);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		timer.stop("Done!");
	}
}
