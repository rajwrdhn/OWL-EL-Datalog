import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.vlog4j.core.model.api.Atom;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.api.Rule;
import org.semanticweb.vlog4j.core.model.api.Variable;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;
import org.semanticweb.vlog4j.core.reasoner.Algorithm;
import org.semanticweb.vlog4j.core.reasoner.Reasoner;
import org.semanticweb.vlog4j.core.reasoner.exceptions.EdbIdbSeparationException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.IncompatiblePredicateArityException;
import org.semanticweb.vlog4j.core.reasoner.exceptions.ReasonerStateException;
import org.semanticweb.vlog4j.core.reasoner.implementation.QueryResultIterator;


public class DatalogTranslation extends InferenceForOWLELMain{
	protected final Set<OWLAxiom> v__s_normalisedAxioms = new HashSet<>();

	protected final Set<Atom> v_s_Facts = new HashSet<>();
	protected final List<Rule> v_l_Rules = new ArrayList<>();

	protected final Set<Predicate> v_s_nomEDB = new HashSet<>();
	protected final Set<Predicate> v_s_clsEDB = new HashSet<>();
	protected final Set<Predicate> v_s_rolEDB = new HashSet<>();	
	protected final Set<Predicate> v_s_subClassEDB = new HashSet<>();	
	protected final Set<Predicate> v_s_topEDB = new HashSet<>();
	protected final Set<Predicate> v_s_botEDB = new HashSet<>();
	protected final Set<Predicate> v_s_subConjEDB = new HashSet<>();
	protected final Set<Predicate> v_s_subExEDB = new HashSet<>();
	protected final Set<Predicate> v_s_supExEDB = new HashSet<>();
	protected final Set<Predicate> v_s_subSelfEDB = new HashSet<>();
	protected final Set<Predicate> v_s_supSelfEDB = new HashSet<>();
	protected final Set<Predicate> v_s_subRoleEDB = new HashSet<>();

	protected final Predicate v_P_triple =Expressions.makePredicate("triple", 3);
	protected final Predicate v_P_inst = Expressions.makePredicate("inst", 2);
	protected final Predicate v_P_self = Expressions.makePredicate("self", 2);

	protected final Variable v = Expressions.makeVariable("v");
	protected final Variable w = Expressions.makeVariable("w");
	protected final Variable x = Expressions.makeVariable("x");
	protected final Variable y = Expressions.makeVariable("y");
	protected final Variable z = Expressions.makeVariable("z");


	public DatalogTranslation() {

	}

	public void visitNormalisedAxioms(Collection<? extends OWLAxiom> normalizedAxioms) {
		VisitNormalisedAxioms v_normalizedAxiomVisitor = new VisitNormalisedAxioms();
		for (OWLAxiom axiom: normalizedAxioms) {
			axiom.accept(v_normalizedAxiomVisitor);
		}
	}

	public void makeRules() {



		for (Predicate nom : v_s_nomEDB) {
			// nom(x) :- v_P_inst(x,x)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeAtom(nom, x), 
					Expressions.makeAtom(v_P_inst, x,x)));
			//nom(x), v_P_triple(x,y,x) :- v_P_self(x,y)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(nom, x),
					Expressions.makeAtom(v_P_triple, x,y,x)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_self, x,y))));
		}


		//top(x), v_P_inst(y,z) :- v_P_inst (y,x)
		for (Predicate top : v_s_topEDB) {
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(top, x),
					Expressions.makeAtom(v_P_inst, y,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, y,x))));
		}
		//use botEDB and clsEDB
		//bot(z) , v_P_inst (x,z) , v_P_inst (v,w) , cls(y) :- v_P_inst (v,y)
		for (Predicate cls : v_s_clsEDB) {
			for (Predicate bot : v_s_botEDB) {
				v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
						Expressions.makeAtom(bot, z),
						Expressions.makeAtom(v_P_inst, x,z),
						Expressions.makeAtom(v_P_inst, v,w),
						Expressions.makeAtom(cls, y)
						),
						Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, v,y))));
			}
		}


		for (Predicate subclass : v_s_subClassEDB) {
			//rule subclass (A,B) , subclass(B,C) :- subclass (A,C)   
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subclass, x,y),
					Expressions.makeAtom(v_P_inst, y,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, x,z))));
		}

		//subConj(x,y,z) , v_P_inst(v,x) , v_P_inst(v,y) :- v_P_inst(v,z)
		for (Predicate subconj : v_s_subConjEDB) {
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subconj, x,y,z),
					Expressions.makeAtom(v_P_inst, v,x),
					Expressions.makeAtom(v_P_inst, v,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, v,z))));
		}

		//subEx(v,y,z) , v_P_triple(x,v,w) , v_P_inst(w,y) :- v_P_inst(x,z)
		for (Predicate subex : v_s_subExEDB) {
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subex, v,y,z),
					Expressions.makeAtom(v_P_triple, x,v,w),
					Expressions.makeAtom(v_P_inst, w,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, x,z))));
			//subEx(v,y,z) , v_P_self(x,v) , v_P_inst(x,y) :- v_P_inst(x,z)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subex, v,y,z),
					Expressions.makeAtom(v_P_self, x,v),
					Expressions.makeAtom(v_P_inst, x,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, x,z))));
		}



		for (Predicate supex : v_s_supExEDB) {
			//supEx(v,w,y,z) , v_P_inst(x,v) :- v_P_triple(z,y)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(supex, v,w,y,z),
					Expressions.makeAtom(v_P_inst, x,v)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, z,y))));
			//supEx(v,w,x,y) , v_P_inst(z,v) :- v_P_inst(y,x)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(supex, v,w,x,y),
					Expressions.makeAtom(v_P_inst, z,v)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, y,x))));
		}

		for (Predicate subself : v_s_subSelfEDB) {
			//subSelf(v,z) , v_P_self(x,v) :- v_P_inst(x,z)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subself, v,z),
					Expressions.makeAtom(v_P_self, x,v)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, x,z))));
		}
		for (Predicate supself : v_s_supSelfEDB) {
			//supSelf(y,v) , v_P_inst(x,y) :- v_P_self(x,v)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(supself, y,v),
					Expressions.makeAtom(v_P_inst, x,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_self, x,v))));
		}	
		for (Predicate nom : v_s_nomEDB) {
			//v_P_inst(x,y) , nom(y) , v_P_inst(x,z) :- v_P_inst(y,z)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(v_P_inst, x,y),
					Expressions.makeAtom(nom, y),
					Expressions.makeAtom(v_P_inst, x,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, y,z))));
			//v_P_inst(x,y) , nom(y) , v_P_inst(x,z) :- v_P_inst(x,z)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(v_P_inst, x,y),
					Expressions.makeAtom(nom, y),
					Expressions.makeAtom(v_P_inst, x,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_inst, x,z))));

			//v_P_inst(x,y) , nom(y) , v_P_triple(z,v,x) :- v_P_triple(z,v,y)
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(v_P_inst, x,y),
					Expressions.makeAtom(nom, y),
					Expressions.makeAtom(v_P_triple, z,v,x)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_triple, z,v,y))));
		}
		for (Predicate subrole : v_s_subRoleEDB) {
			//role rules (13) (14) from paper
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subrole, v,w),
					Expressions.makeAtom(v_P_triple, x,v,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_triple, x,w,y))));		
			v_l_Rules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(subrole, v,w),
					Expressions.makeAtom(v_P_self, x,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(v_P_self, x,w))));
		}
	}
	/*
	 * 
	 */
	public void callReasoner() throws ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		Reasoner reasoner = Reasoner.getInstance();
		reasoner.addRules(v_l_Rules);
		reasoner.addFacts(v_s_Facts);

		reasoner.load();
		reasoner.setAlgorithm(Algorithm.SKOLEM_CHASE);
		reasoner.setReasoningTimeout(1);
		System.out.println("Starting Skolem Chase with 1 second timeout.");

		/* Indeed, the Skolem Chase did not terminate before timeout. */
		boolean skolemChaseFinished = reasoner.reason();
		System.out.println("Has Skolem Chase algorithm finished before 1 second timeout? " + skolemChaseFinished);
		System.out.println(
				"Answers to query " + Expressions.makeAtom(v_P_inst, x,y) + " after reasoning with the Skolem Chase for 1 second:");
		printOutQueryAnswers(Expressions.makeAtom(v_P_inst, x,y), reasoner);
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

	/**
	 * 
	 * @author raj
	 *
	 */
	protected class VisitNormalisedAxioms implements OWLAxiomVisitor {

		@Override
		public void visit(OWLAnnotationAssertionAxiom axiom) {

		}

		@Override
		public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {

		}

		@Override
		public void visit(OWLAnnotationPropertyDomainAxiom axiom) {

		}

		@Override
		public void visit(OWLAnnotationPropertyRangeAxiom axiom) {

		}

		@Override
		public void visit(OWLSubClassOfAxiom axiom) {
			if(axiom.getSubClass().isClassExpressionLiteral() && !axiom.getSuperClass().isClassExpressionLiteral()) {
				ClassExpressionVisitorForNormalisedAxiomRight ce_visit = new ClassExpressionVisitorForNormalisedAxiomRight(axiom.getSuperClass()); 
				axiom.getSuperClass().accept(ce_visit);
			} else {
				ClassExpressionVisitorForNormalisedAxiomRight ce_visit = new ClassExpressionVisitorForNormalisedAxiomRight(axiom.getSuperClass()); 
				axiom.getSubClass().accept(ce_visit);
			}
		}

		@Override
		public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLDisjointClassesAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLDataPropertyDomainAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLObjectPropertyDomainAxiom axiom) {
			//Normalised
		}

		@Override
		public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
			//Normalised
		}

		@Override
		public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLDifferentIndividualsAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLDisjointDataPropertiesAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLObjectPropertyRangeAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLObjectPropertyAssertionAxiom axiom) {
			//TODO
		}

		@Override
		public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLSubObjectPropertyOfAxiom axiom) {
			
		}

		@Override
		public void visit(OWLDisjointUnionAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLDataPropertyRangeAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLFunctionalDataPropertyAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLClassAssertionAxiom axiom) {
			// TODO Auto-generated method stub
			OWLAxiomVisitor.super.visit(axiom);
		}

		@Override
		public void visit(OWLEquivalentClassesAxiom axiom) {
			// TODO Auto-generated method stub
			OWLAxiomVisitor.super.visit(axiom);
		}

		@Override
		public void visit(OWLDataPropertyAssertionAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLSubDataPropertyOfAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLSameIndividualAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLSubPropertyChainOfAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLInverseObjectPropertiesAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(OWLHasKeyAxiom axiom) {
			throw new IllegalArgumentException();
		}

		@Override
		public void visit(SWRLRule node) {
			throw new IllegalArgumentException();
		}
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
