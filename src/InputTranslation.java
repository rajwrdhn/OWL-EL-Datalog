import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class InputTranslation {
	protected final Set<OWLAxiom> axioms;
	protected final Set<String> inputStringTranslation;
	public InputTranslation(Set<OWLAxiom> axm) {
		axioms = axm;
		inputStringTranslation = new HashSet<>();
	}
	
	public Set<String> setInputTranslation(Set<OWLAxiom> axms) {
		for (OWLAxiom axiom : axms) {
			traslationOfAxiomsToString(axiom);
		}
		
		return inputStringTranslation;
	}
	
	public void traslationOfAxiomsToString(OWLAxiom axiom) {
		
		if (axiom instanceof OWLClassAssertionAxiom) {
			inputStringTranslation.add("subClass("+((OWLClassAssertionAxiom) axiom).getIndividual().toString()+","+((OWLClassAssertionAxiom) axiom).getClassExpression()+")");			
		} else if (axiom instanceof OWLSubClassOfAxiom) {
			if(((OWLSubClassOfAxiom) axiom).getSubClass().isClassExpressionLiteral()) {
				
			}
		}
	}
}
