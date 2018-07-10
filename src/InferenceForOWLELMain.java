import java.io.File;
import java.io.IOException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.vlog4j.core.reasoner.exceptions.VLog4jException;

//Main class upload ontology here
public class InferenceForOWLELMain {
	
	//Normalize norm = new Normalize(factory);
	
	
	public InferenceForOWLELMain() {

	}
	public static void loadOntology(String fileadd) throws OWLOntologyCreationException, VLog4jException, VLog4jException, VLog4jException, IOException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		File file = new File(fileadd);
		OWLOntology onto = man.loadOntologyFromOntologyDocument(file);
		OWLDataFactory factory = man.getOWLDataFactory();
		OWLOntologyID ontID = onto.getOntologyID();		
		Normalize norm = new Normalize(factory,ontID);	
		norm.getFromOntology(onto);
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
		timer.start("Start EL-Ontology reasoning! ");
		String file = args[0];
		try {
			loadOntology(file);
			
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		timer.stop("Done!");
	}
}
