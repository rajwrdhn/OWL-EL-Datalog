import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.vlog4j.core.reasoner.Algorithm;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;

//Main class upload ontology here
public class InferenceForOWLELMain {
	protected static Set<String> v_individualNames;
	protected static Set<String> v_classNames;
	protected static Set<String> v_roleNames;
	protected static Set<String> v_otherLogicalAxioms;
	
	//Normalize norm = new Normalize(factory);
	
	
	public InferenceForOWLELMain() {
		v_individualNames = new HashSet<>();
		v_classNames = new HashSet<>();
		v_roleNames = new HashSet<>();
		v_otherLogicalAxioms = new HashSet<>();		
		//matcalc= new MaterialisationCalculus();
	}
	
	public void FinalReasoning() {
		final Reasoner reasoner = Reasoner.getInstance();
		reasoner.setAlgorithm(Algorithm.SKOLEM_CHASE);
	}
	/**
	 * set of individual names set ({nom(a)},...)
	 * @return
	 */
	public Set<String> getOWLIndividualNamesAsStrings() {
		return v_individualNames;
	}
	/**
	 * set of class names set ({cls(A)},...)
	 * @return
	 */
	public Set<String> getOWLClassNamesAsStrings() {
		return v_classNames;
	}
	/**
	 * set of role names set ({rol(R)},...)
	 * @return
	 */
	public Set<String> getRoleNamesAsStrings() {
		return v_roleNames;
	}
	
	/**
	 * set of axioms as String
	 * @return
	 */
	public Set<String> getAxiomsAsStrings() {
		return v_otherLogicalAxioms;
	}
	
	public static OWLOntology loadOntology(String fileadd) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		File file = new File(fileadd);
		OWLOntology onto = man.loadOntologyFromOntologyDocument(file);
		OWLDataFactory factory = man.getOWLDataFactory();
		OWLOntologyID ontID = onto.getOntologyID();		
		Normalize norm = new Normalize(factory,ontID);			
		Set<OWLAxiom> axioms= norm.getFromOntology(onto);		
		OWLOntology ont = man.createOntology(axioms);
		//OwlToRulesConverter owlToRulesConverter = new OwlToRulesConverter();
		//owlToRulesConverter.addOntology(ont);		
		v_otherLogicalAxioms = norm.getinputTranslationAxioms();
		ont.individualsInSignature().forEach(x -> v_individualNames.add("{nom("+x.toString()+")}"));
		ont.objectPropertiesInSignature().forEach(x -> v_roleNames.add("{rol("+x.toString()+")}"));
		ont.classesInSignature().forEach(x -> v_classNames.add("{cls("+x.toString()+")}") );
		return ont;
	}
	
	public static void materialisationCalc() {
		MaterialisationCalculus matcalc = new MaterialisationCalculus();	
		matcalc.factBase();
	}
	/**
	 * 
	 * @param args
	 */
	public static void main(String []args){
		System.out.println();
		InferenceForOWLELMain elmain = new InferenceForOWLELMain();
		StopWatch timer = new StopWatch();
		timer.start("Normalize EL-Ontology! ");
		String file = args[0];
		try {
			OWLOntology ont = loadOntology(file);
			materialisationCalc();
			
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		timer.stop("Done!");
	}
}
