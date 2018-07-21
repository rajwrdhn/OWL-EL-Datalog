import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.vlog4j.core.model.api.Atom;
import org.semanticweb.vlog4j.core.model.api.Rule;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;
import org.semanticweb.vlog4j.core.reasoner.Algorithm;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;
import org.semanticweb.vlog4j.core.reasoner.exceptions.EdbIdbSeparationException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.IncompatiblePredicateArityException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.ReasonerStateException;
import org.semanticweb.vlog4j.core.reasoner.implementation.QueryResultIterator;

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
		
		dlogrules.makeRules();
		
		callReasoner(dlogrules,normalizedAxiomVisitor);
	}
	
/*	public void getargsList() {
		String[] list = new 
		for (int i = 1; i < list.size(); i++) {
			if(list)
		}
	}*/

	public void callReasoner(DatalogRules dlogruls, VisitNormalisedAxioms visitorget) throws ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		Reasoner reasoner = Reasoner.getInstance();
		List<Rule> allrules = dlogruls.v_l_rules;
		Set<Atom> allfacts = visitorget.getFacts();
		
		System.out.println(allrules.size());
		System.out.println(allfacts.size());
		
		reasoner.addRules(allrules);
		reasoner.addFacts(allfacts);
		

		reasoner.load();
		reasoner.setAlgorithm(Algorithm.SKOLEM_CHASE);
		reasoner.setReasoningTimeout(1);
		System.out.println("Starting Skolem Chase with 1 second timeout.");
		
		Variable x = Expressions.makeVariable("x");
		Variable y = Expressions.makeVariable("y");
		Atom a =Expressions.makeAtom("inst", x, y);
		/* Indeed, the Skolem Chase did not terminate before timeout. */
		boolean skolemChaseFinished = reasoner.reason();
		System.out.println("Has Skolem Chase algorithm finished before 1 second timeout? " + skolemChaseFinished);
		System.out.println(
				"Answers to query " + a + " after reasoning with the Skolem Chase for 1 second:");
		printOutQueryAnswers(a , reasoner);
		reasoner.close();

	}

	private void printOutQueryAnswers(Atom queryAtom, Reasoner reasoner) throws ReasonerStateException {
		// TODO Auto-generated method stub
		System.out.println();
		System.out.println("Answers to query " + queryAtom + " before materialisation:");
		try (QueryResultIterator answersBeforeMaterialisation = reasoner.answerQuery(queryAtom, true)) {
			while (answersBeforeMaterialisation.hasNext()) {
				System.out.println(" - " + answersBeforeMaterialisation.next());
			}
			System.out.println();
		}
	}
}
