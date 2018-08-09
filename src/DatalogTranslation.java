import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.vlog4j.core.model.api.Atom;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.api.Rule;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;
import org.semanticweb.vlog4j.core.reasoner.Algorithm;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;
import org.semanticweb.vlog4j.core.reasoner.exceptions.VLog4jException;
import org.semanticweb.vlog4j.core.reasoner.implementation.QueryResultIterator;

public class DatalogTranslation {
	protected final Set<OWLAxiom> v_s_normalisedAxioms;
	protected static int i =0;

	public DatalogTranslation(Set<OWLAxiom> normalisedAxioms) {
		v_s_normalisedAxioms = normalisedAxioms;
	}

	public void visitNormalisedAxiomsHash() throws IOException, VLog4jException {
		VisitNormalisedAxioms normalizedAxiomVisitor = new VisitNormalisedAxioms(v_s_normalisedAxioms);
		for (OWLAxiom axiom: v_s_normalisedAxioms) {
			axiom.accept(normalizedAxiomVisitor);
		}

		instance();

		subclass();		
	}

	public void instance() throws IOException, VLog4jException {
		StopWatch timer = new StopWatch();
		timer.start("");
		System.out.println("Start Instance Check...");
		VisitNormalisedAxioms normalizedAxiomVisitor = new VisitNormalisedAxioms(v_s_normalisedAxioms);
		DatalogRules dlogrules = new DatalogRules(v_s_normalisedAxioms);

		dlogrules.instanceRetrievalRules();

		callReasoner(dlogrules,normalizedAxiomVisitor);
		System.out.println(" Instance Retrivals:-"+ i);
		timer.stop(" Instance Retrieval Done!!");
	}

	public void subclass() throws IOException, VLog4jException {
		StopWatch timer = new StopWatch();
		timer.start();
		System.out.println("\n");
		System.out.println("Start SubClass...");
		VisitNormalisedAxioms normalizedAxiomVisitor = new VisitNormalisedAxioms(v_s_normalisedAxioms);
		DatalogRules dlogrules = new DatalogRules(v_s_normalisedAxioms);

		dlogrules.subClassRules();

		callReasoner(dlogrules,normalizedAxiomVisitor);
		System.out.println(" Subsumption Relations:-"+ i);
		timer.stop("Subsumption Done!!");
	}

	public void callReasoner(DatalogRules dlogruls, VisitNormalisedAxioms visitorget) throws IOException, VLog4jException {
		Reasoner reasoner = Reasoner.getInstance();
		List<Rule> allrules = dlogruls.v_l_rules;
		Set<Atom> allfacts = visitorget.getFacts();

		System.out.println("Number of facts :=  "+ allfacts.size());
		allrules.remove(null);
		allfacts.remove(null);
		reasoner.addRules(allrules);
		reasoner.addFacts(allfacts);

		reasoner.load();
		reasoner.setAlgorithm(Algorithm.RESTRICTED_CHASE);
		reasoner.reason();
		countResults(reasoner);
		reasoner.close();
	}

	public void countResults(Reasoner reasoner) throws VLog4jException {
		Variable x = Expressions.makeVariable("x");
		Variable y = Expressions.makeVariable("y");

		Predicate pred = Expressions.makePredicate("inst", 2);

		Atom queryAtom = Expressions.makeAtom(pred, x,y);

		i =0;

		try (QueryResultIterator queryResultIterator = reasoner.answerQuery(queryAtom, true)) {
			queryResultIterator.forEachRemaining(res ->  {
				if (!res.toString().contains("FreshConcept") && !res.toString().contains("aux")) {
					//System.out.println(res.toString());
					i++;
				}
			});

		}
	}
}
