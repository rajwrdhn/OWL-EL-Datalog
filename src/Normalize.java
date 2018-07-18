import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.EdbIdbSeparationException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.IncompatiblePredicateArityException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.ReasonerStateException;

public class Normalize {

	protected static Map<Integer,Set<OWLAxiom>> v_Iterable_MapAxioms = new HashMap<Integer,Set<OWLAxiom>>();
	protected static int v_Iterable_KeyForMap = 1;
	protected static Set<OWLAxiom> v_For_FurtherNormalisation = new HashSet<OWLAxiom>();

	protected final Set<OWLAxiom> v_Normalised_Axioms = new HashSet<>();

	protected final OWLDataFactory v_factory;

	protected static long v_counter_Fresh_Concept = 0;


	public static OWLClassExpression v_classExpression;
	protected static long freshConceptNumber;
	protected static long auxnum;
	//constructor
	public Normalize(OWLDataFactory factory) {
		v_factory=factory;
		v_classExpression = null;
		freshConceptNumber = 1;
		auxnum = 1;
	}

	/**
	 * Call axiomVisitor Class , Normalize and return new set of normalized axioms.
	 * @throws IOException 
	 * @throws IncompatiblePredicateArityException 
	 * @throws EdbIdbSeparationException 
	 * @throws ReasonerStateException 
	 */
	public void getFromOntology(OWLOntology onto) throws OWLOntologyCreationException, ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		Set<OWLAxiom> asi = new HashSet<>();
		
		onto.axioms().forEach(x -> asi.add(x));
		
		v_Iterable_MapAxioms.put(v_Iterable_KeyForMap, asi);
		/*onto.axioms().forEach(x -> {
            if (x != null) {
            	v_Iterable_MapAxioms.put(v_Iterable_KeyForMap, asi);
            } //else 
        });*/
		//map.values().removeIf(Objects::isNull);
		//k_axioms.addAll((Collection<? extends OWLAxiom>) onto.axioms());
		if(v_Iterable_MapAxioms.isEmpty()) {
			System.out.println("No Axioms in the Ontology!!");
		} else {
			visitAxioms(v_Iterable_MapAxioms.get(v_Iterable_KeyForMap));
		}

		//return v_Normalised_Axioms;
	}

	/**
	 * Visit all Axioms in the initial Ontology and normalize the axioms
	 */
	public void visitAxioms(Collection<? extends OWLAxiom> axioms) throws OWLOntologyCreationException {
		AxiomVisitorForNormalisation axmVisitor = new AxiomVisitorForNormalisation(v_factory);
		
		for (OWLAxiom axiom : axioms) {
			axiom.accept(axmVisitor);
		}
		v_Iterable_KeyForMap++;
		v_Iterable_MapAxioms.put(v_Iterable_KeyForMap, v_For_FurtherNormalisation);
		v_For_FurtherNormalisation.clear();
		if (v_Iterable_MapAxioms.get(v_Iterable_KeyForMap).isEmpty()) {
			System.out.println("Normalisation Complete!! ");
		} else {
			visitAxioms(v_Iterable_MapAxioms.get(v_Iterable_KeyForMap));
		}
	}

	public void crunchCleanNormalisedAxiomFromMap(int keyA) {		
		v_Iterable_MapAxioms.remove(keyA);		
	}
	
	//NOt working as map is not initialised
	public void addAxiomToMap(int number, OWLAxiom axiom) {
		if (v_Iterable_MapAxioms.containsKey(number)) {
			v_Iterable_MapAxioms.get(number).add(axiom);
		} else {
			System.out.println("Okay !!");
		}
	}

	public boolean isNonComplementOFNamedClass(OWLClassExpression ce) {
		if (ce instanceof OWLObjectComplementOf) {
			throw new IllegalStateException();
		}
		return ce.isClassExpressionLiteral();
	}

	public boolean isNotNamedClass(OWLClassExpression ce) {
		return !ce.isClassExpressionLiteral();
	}

	public OWLAxiom addAxiomOfConjunctSubClass(OWLClassExpression ce1, OWLClassExpression ce2, OWLClassExpression ce3) {
		//ce1 and ce2 subsumes ce3
		return v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectIntersectionOf(ce1,ce2),ce3);
	}

	public OWLAxiom addSubClassAxiom(OWLClassExpression ce1, OWLClassExpression ce2) {
		//ce1 subsumes ce2
		return v_factory.getOWLSubClassOfAxiom(ce1, ce2);
	}

	public OWLAxiom addSomevaluesFromAxiom(OWLClassExpression ce1, OWLObject obj,OWLClassExpression ce2) {		
		return v_factory.getOWLSubClassOfAxiom(ce1, v_factory.getOWLObjectSomeValuesFrom((OWLObjectPropertyExpression) obj, ce2));
	}
	/**
	 * Adds the fresh concept name
	 */
	public OWLClassExpression addFreshClassName(long conceptNumber) {

		return v_factory.getOWLClass(IRI.create("#FreshConcept" + conceptNumber));
	}
	/**
	 * Adds Fresh Class name to the existentially quantified Concept expression
	 */
	public OWLObjectSomeValuesFrom addSomeValuesFromToFreshClassName(OWLObjectSomeValuesFrom expr, long conceptNumber) {
		return v_factory.getOWLObjectSomeValuesFrom(expr.getProperty(), addFreshClassName(conceptNumber));
	}

	/**
	 * set the class expression to be used as super or sub class in ClassExpressionNormalize Visitor
	 */
	public void setCurrentClassExpression(OWLClassExpression ce) {
		v_classExpression = ce;
	}

	/** 
	 * @return classExpression set in for ClassExpressionNormalize Visitor
	 */
	public OWLClassExpression getCurrentClassExpression() {
		return v_classExpression;
	}
}
