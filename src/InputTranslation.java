import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLOntology;

import com.google.inject.ImplementedBy;

public class InputTranslation {
	protected final Set<String >inputTranslationString;
	public InputTranslation() {
		// TODO Auto-generated constructor stub
		inputTranslationString = new HashSet<>();
	}
	
	public void getInputTranslation(OWLOntology ontology) {
		
	}
	
	public class newAxiomVisitor implements OWLAxiomVisitor {
		public newAxiomVisitor() {
			
		}
	}
}
