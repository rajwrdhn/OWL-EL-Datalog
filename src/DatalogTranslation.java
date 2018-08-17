import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.vlog4j.core.model.api.Atom;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.api.Rule;
import org.semanticweb.vlog4j.core.model.api.Term;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;
import org.semanticweb.vlog4j.core.reasoner.Algorithm;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;
import org.semanticweb.vlog4j.core.reasoner.exceptions.VLog4jException;
import org.semanticweb.vlog4j.core.reasoner.implementation.QueryResultIterator;

public class DatalogTranslation {
	protected final Set<OWLAxiom> v_s_normalisedAxioms;
	protected static int i =0;

	protected final List<List<Term>> list_terms = new ArrayList<>();
	protected static HashMap<Term, List<Term>> store_map = new HashMap<Term, List<Term>>();

	protected static int count_equivalentclasses = 0;
	protected static int count_subclasses = 0;

	static List<Term> termset = new ArrayList<>();

	protected final Set<Term> allterms = new HashSet<Term>();

	public DatalogTranslation(Set<OWLAxiom> normalisedAxioms) {
		v_s_normalisedAxioms = normalisedAxioms;
	}

	public void visitNormalisedAxiomsHash() throws IOException, VLog4jException {
		VisitNormalisedAxioms normalizedAxiomVisitor = new VisitNormalisedAxioms(v_s_normalisedAxioms);
		for (OWLAxiom axiom: v_s_normalisedAxioms) {
			axiom.accept(normalizedAxiomVisitor);
		}

		//instance();

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

		//countTransitivelyReducedResult();
		System.out.println("Equivalent classes :::::::::::::" + count_equivalentclasses);
		System.out.println("Sub Classes :::::::::::::" + count_subclasses);
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
		reasoner.setAlgorithm(Algorithm.SKOLEM_CHASE);
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
				if (!res.toString().contains("#FreshConcept_X_") && !res.toString().contains("aux_au_")) {
					//System.out.println(res.toString());
					list_terms.add(res.getTerms());
					i++;

				}
			});

		}

		addToSuperConceptOfEach();
	}

	public void addToSuperConceptOfEach() {
		Term[][] arr = new Term[list_terms.size()][2];
		boolean directsuper;

		for (int k = 0; k< list_terms.size(); k++) {
			for (int j =0; j< 2;j++) {
				arr[k][j] = list_terms.get(k).get(j);
			}
		}

		for (int k = 0; k< list_terms.size(); k++) {

			for (int j =0; j< list_terms.size() ;j++) {
				// A sub A and A sub A case gone
				if (arr[k][0].equals( arr[j][0]) && arr[k][1].equals( arr[j][1])) {
					break;
				}

				if (arr[k][1].equals( arr[j][0])) {
					directsuper = true;
					if (!arr[k][0].equals(arr[j][1])) {
						for (int l = 0; l<list_terms.size(); l++) {
							if (arr[j][0].equals( arr[l][0]) && arr[j][1].equals( arr[l][1])) {
								directsuper = false;
								break;
							}

							if (arr[k][0].equals(arr[j][0]) && arr[k][1].equals(arr[l][1])) {
								directsuper = false;
								break;
							}

							if (arr[k][1].equals(arr[j][0]) && arr[j][1].equals(arr[l][0])) {
								directsuper = false;
								count_subclasses--;
							}
						}
						if (directsuper) {
							count_subclasses++;
						}
					}else {
						count_equivalentclasses++;
					}
				}

			}
		}
	}

}
