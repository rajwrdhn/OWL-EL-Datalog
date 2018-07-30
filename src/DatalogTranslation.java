import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.vlog4j.core.model.api.Atom;
import org.semanticweb.vlog4j.core.model.api.Rule;
import org.semanticweb.vlog4j.core.reasoner.Algorithm;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;
import org.semanticweb.vlog4j.core.reasoner.exceptions.EdbIdbSeparationException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.IncompatiblePredicateArityException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.ReasonerStateException;

public class DatalogTranslation {
	protected final Set<OWLAxiom> v_s_normalisedAxioms;

	
	public DatalogTranslation(Set<OWLAxiom> normalisedAxioms) {
		v_s_normalisedAxioms = normalisedAxioms;
	}

	public void visitNormalisedAxiomsHash() throws ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		VisitNormalisedAxioms normalizedAxiomVisitor = new VisitNormalisedAxioms(v_s_normalisedAxioms);
		for (OWLAxiom axiom: v_s_normalisedAxioms) {
			axiom.accept(normalizedAxiomVisitor);
		}
		
		DatalogRules dlogrules = new DatalogRules(v_s_normalisedAxioms);
		
		dlogrules.instanceRetrievalRules();
		
		callReasoner(dlogrules,normalizedAxiomVisitor);
	}

	public void callReasoner(DatalogRules dlogruls, VisitNormalisedAxioms visitorget) throws ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		Reasoner reasoner = Reasoner.getInstance();
		List<Rule> allrules = dlogruls.v_l_rules;
		Set<Atom> allfacts = visitorget.getFacts();
		
		System.out.println("Number of facts :=  "+ allfacts.size());
		
		reasoner.addRules(allrules);
		reasoner.addFacts(allfacts);
		
		reasoner.load();
		reasoner.setAlgorithm(Algorithm.SKOLEM_CHASE);
		reasoner.reason();
		reasoner.close();
	}
}
