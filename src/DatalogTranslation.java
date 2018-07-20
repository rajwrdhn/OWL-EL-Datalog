import java.io.IOException;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.vlog4j.core.model.api.Atom;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;
import org.semanticweb.vlog4j.core.reasoner.Algorithm;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;
import org.semanticweb.vlog4j.core.reasoner.exceptions.EdbIdbSeparationException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.IncompatiblePredicateArityException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.ReasonerStateException;
import org.semanticweb.vlog4j.core.reasoner.implementation.QueryResultIterator;


public class DatalogTranslation {
	protected static Set<OWLAxiom> v_s_normalisedAxioms;

	public DatalogTranslation(Set<OWLAxiom> normalisedAxioms) {
		v_s_normalisedAxioms = normalisedAxioms;
	}

	public void visitNormalisedAxiomsHash() throws ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		VisitNormalisedAxioms normalizedAxiomVisitor = new VisitNormalisedAxioms(v_s_normalisedAxioms);
		for (OWLAxiom axiom: v_s_normalisedAxioms) {
			axiom.accept(normalizedAxiomVisitor);
			System.out.println(axiom);
		}
		
		DatalogRules dlogrules = new DatalogRules(v_s_normalisedAxioms);
		
		//assign
		Set<Predicate> setnom = normalizedAxiomVisitor.getSetNom();
		dlogrules.setnomEDB(setnom);
		System.out.println(setnom.isEmpty());
		
		Set<Predicate> setcls = normalizedAxiomVisitor.getSetCls();
		dlogrules.setclassEDB(setcls);
		System.out.println(setcls.size());
		
		dlogrules.makeRules();
		
		callReasoner(dlogrules,normalizedAxiomVisitor);
	}
	

	

	
	public void callReasoner(DatalogRules dlogruls, VisitNormalisedAxioms visitorget) throws ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		Reasoner reasoner = Reasoner.getInstance();
		System.out.println(dlogruls.v_l_Rules.isEmpty());
		reasoner.addRules(dlogruls.v_l_Rules);
		reasoner.addFacts(visitorget.v_s_Facts);

		reasoner.load();
		reasoner.setAlgorithm(Algorithm.SKOLEM_CHASE);
		reasoner.setReasoningTimeout(1);
		System.out.println("Starting Skolem Chase with 1 second timeout.");

		/* Indeed, the Skolem Chase did not terminate before timeout. */
		boolean skolemChaseFinished = reasoner.reason();
		System.out.println("Has Skolem Chase algorithm finished before 1 second timeout? " + skolemChaseFinished);
		System.out.println(
				"Answers to query " + Expressions.makeAtom("inst", dlogruls.x,dlogruls.y) + " after reasoning with the Skolem Chase for 1 second:");
		printOutQueryAnswers(Expressions.makeAtom("inst", dlogruls.x,dlogruls.y), reasoner);
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
