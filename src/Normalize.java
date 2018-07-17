import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
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
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
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

import uk.ac.manchester.cs.owl.owlapi.OWLObjectSomeValuesFromImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLSubClassOfAxiomImpl;


public class Normalize {
	protected final OWLDataFactory v_factory;
	protected final Set<OWLAxiom> v_axioms;
	protected final Set<OWLAxiom> n_axioms;
	protected final Set<OWLAxiom> k_axioms;
	protected final Set<OWLAxiom> del_axioms;
	public static OWLClassExpression v_classExpression;
	protected final Optional<IRI> v_IRI;
	protected static long freshConceptNumber;
	protected static long auxnum;
	protected final Set<Atom> setFacts;
	protected final List<Rule> listRules;
	protected final Set<Predicate> nomEDB;
	protected final Set<Predicate> clsEDB;
	protected final Set<Predicate> rolEDB;
	protected final Predicate triple;
	protected final Set<Predicate> subClassEDB;
	protected final Predicate inst;
	protected final Set<Predicate> topEDB;
	protected final Set<Predicate> botEDB;
	protected final Set<Predicate> subConjEDB;
	protected final Set<Predicate> subExEDB;
	protected final Set<Predicate> supExEDB;
	protected final Set<Predicate> subSelfEDB;
	protected final Set<Predicate> supSelfEDB;
	protected final Set<Predicate> subRoleEDB;
	protected final Predicate self;
	protected final Predicate nom;
	protected final Predicate cls;
	protected final Predicate rol;
	protected final Predicate subClass;
	protected final Predicate top;
	protected final Predicate bot;
	protected final Predicate subConj;
	protected final Predicate subEx;
	protected final Predicate supEx;
	protected final Predicate subSelf;
	protected final Predicate supSelf;
	protected final Predicate subRole;
	protected final Variable v;
	protected final Variable w;
	protected final Variable x;
	protected final Variable y;
	protected final Variable z;
	final AxiomVisitor axmVisitor;
	//constructor
	public Normalize(OWLDataFactory factory,OWLOntologyID ontID) {
		axmVisitor = new AxiomVisitor();
		v_factory=factory;
		v_IRI = ontID.getOntologyIRI();
		v_axioms= new HashSet<>();
		n_axioms= new HashSet<>();
		k_axioms= ConcurrentHashMap.newKeySet();
		del_axioms = new HashSet<>();
		v_classExpression = null;
		freshConceptNumber = 1;
		auxnum = 1;
		setFacts = new HashSet<>();
		listRules = new ArrayList<>();
		nomEDB = new HashSet<>();
		clsEDB = new HashSet<>();
		rolEDB = new HashSet<>();
		triple = Expressions.makePredicate("triple", 3);
		subClassEDB = new HashSet<>();
		inst = Expressions.makePredicate("inst", 2);
		topEDB = new HashSet<>();
		botEDB = new HashSet<>();
		subConjEDB = new HashSet<>();
		subExEDB = new HashSet<>();
		supExEDB = new HashSet<>();
		subSelfEDB = new HashSet<>();
		supSelfEDB = new HashSet<>();
		subRoleEDB = new HashSet<>();
		self = Expressions.makePredicate("self", 2);
		nom = Expressions.makePredicate("nom",1);
		cls = Expressions.makePredicate("cls",1);
		rol = Expressions.makePredicate("rol", 1);
		subClass = Expressions.makePredicate("subclass", 2);
		top = Expressions.makePredicate("top", 1);
		bot = Expressions.makePredicate("bot", 1);
		subConj = Expressions.makePredicate("subconj", 3);
		subEx = Expressions.makePredicate("subex", 3);
		supEx = Expressions.makePredicate("supex", 4);
		subSelf = Expressions.makePredicate("subself", 2);
		supSelf = Expressions.makePredicate("supself", 2);
		subRole = Expressions.makePredicate("subrole", 2);
		v = Expressions.makeVariable("v");
		w = Expressions.makeVariable("w");
		x = Expressions.makeVariable("x");
		y = Expressions.makeVariable("y");
		z = Expressions.makeVariable("z");
	}

	/**
	 * Call axiomVisitor Class , Normalize and return new set of normalized axioms.
	 * @throws IOException 
	 * @throws IncompatiblePredicateArityException 
	 * @throws EdbIdbSeparationException 
	 * @throws ReasonerStateException 
	 */
	public void getFromOntology(OWLOntology onto) throws OWLOntologyCreationException, ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		onto.axioms().forEach(x -> v_axioms.add(x));
		//k_axioms.addAll((Collection<? extends OWLAxiom>) onto.axioms());
		v_axioms.remove(null);
		if(v_axioms.isEmpty()) {
			System.out.println("No Axioms in the Ontology!!");
		} else {
			visitAxioms(v_axioms);
		}
		rules();
		callReasoner();
		//return n_axioms;
	}

	/**
	 * Visit all Axioms in the initial Ontology and normalize the axioms
	 */
	public void visitAxioms(Collection<? extends OWLAxiom> axioms) throws OWLOntologyCreationException {
		//AxiomVisitor axmVisitor = new AxiomVisitor();
		for (OWLAxiom axiom : axioms) {
			axiom.accept(axmVisitor);
			System.out.println(axiom);
			del_axioms.add(axiom);	
		}
		v_axioms.removeAll(del_axioms);		
		v_axioms.addAll(k_axioms);
		v_axioms.remove(null);
		//k_axioms.removeAll(del_axioms);
		if (k_axioms.isEmpty()) {
			System.out.println("Normalisation Complete!! ");
		} else {
			visitAxioms(k_axioms);
		}
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

	/**
	 * ClassExpression Visitor for subclassOf Axioms
	 * @param ce
	 */
	public void normalizesubClassExpressionAxiom(OWLClassExpression subClassExpr, OWLClassExpression superClassExpr) {

		if (subClassExpr.isTopEntity() || subClassExpr.isOWLThing() && superClassExpr.isClassExpressionLiteral() ) {
			//Top subsumes A 
			if (superClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Complement of a named class is not OWL 2 EL !");
			} else {
				clsEDB.add(Expressions.makePredicate(superClassExpr.toString()+"rule", 1));
				topEDB.add(Expressions.makePredicate(subClassExpr.toString()+superClassExpr.toString()+"rule", 1));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(superClassExpr.toString(), 1), Expressions.makeConstant(superClassExpr.toString())));				
			}
		} else if (superClassExpr.isBottomEntity() || superClassExpr.isOWLNothing() && subClassExpr.isClassExpressionLiteral()) {
			//A subsumes Bot
			if (subClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Complement of a named class is not OWL 2 EL !");
			} else {
				clsEDB.add(Expressions.makePredicate(subClassExpr.toString()+"rule", 1));
				botEDB.add(Expressions.makePredicate(superClassExpr.toString()+subClassExpr.toString()+"rule", 1));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(subClassExpr.toString(), 1), Expressions.makeConstant(subClassExpr.toString())));
			}

		} else if (subClassExpr.isClassExpressionLiteral() && superClassExpr.isClassExpressionLiteral()) {
			if (subClassExpr instanceof OWLObjectComplementOf || superClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Complement of a named class is not OWL 2 EL !");
			} else {
				// A subsumes B 
				// both classnames
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(subClassExpr, superClassExpr));
				//subclass fact A is a subset of B
				subClassEDB.add(Expressions.makePredicate(superClassExpr.toString()+subClassExpr.toString()+"rule", 2));
				clsEDB.add(Expressions.makePredicate(subClassExpr.toString()+"rule", 1));
				clsEDB.add(Expressions.makePredicate(superClassExpr.toString()+"rule", 1));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(superClassExpr.toString()+subClassExpr.toString(), 2), 
						Expressions.makeConstant(subClassExpr.toString()), 
						Expressions.makeConstant(superClassExpr.toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(subClassExpr.toString(), 1), Expressions.makeConstant(subClassExpr.toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(superClassExpr.toString(), 1), Expressions.makeConstant(superClassExpr.toString())));
			}	
		} else if(subClassExpr.isAnonymous() && superClassExpr.isAnonymous() && 
				subClassExpr instanceof OWLObjectIntersectionOf && superClassExpr instanceof OWLObjectIntersectionOf
				&& !subClassExpr.isClassExpressionLiteral() && !superClassExpr.isClassExpressionLiteral()) {
			// ClassExpr subsumes ClassExpr
			if (!subClassExpr.isClassExpressionLiteral() && !superClassExpr.isClassExpressionLiteral()) {
				k_axioms.add(v_factory.getOWLSubClassOfAxiom(subClassExpr, addFreshClassName(freshConceptNumber)));
				k_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), superClassExpr));
				freshConceptNumber++;
			}
		} else if (subClassExpr.isClassExpressionLiteral() && !superClassExpr.isClassExpressionLiteral()
				&& superClassExpr.asConjunctSet().size() ==1) {
			if(subClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Not OWL 2 EL !");
			} else {
				//A subsumes C, where C is class expression but not a intersectionOf and not a named class
				setCurrentClassExpression(superClassExpr);
				ClassExpressionNormalize ceVisitor = new ClassExpressionNormalize(true,false);
				superClassExpr.accept(ceVisitor);
			}
		} else if (superClassExpr.isClassExpressionLiteral() && !subClassExpr.isClassExpressionLiteral()
				&& subClassExpr.asConjunctSet().size() ==1) {
			if(superClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Not OWL 2 EL !");
			} else {
				//C subsumes A, where C is class expression but not a intersectionOf and not a named class
				setCurrentClassExpression(subClassExpr);
				ClassExpressionNormalize ceVisitor = new ClassExpressionNormalize(false,true);
				subClassExpr.accept(ceVisitor);
			}
		} else if (subClassExpr.isClassExpressionLiteral() && !superClassExpr.isClassExpressionLiteral()
				&& superClassExpr instanceof OWLObjectIntersectionOf ) {
			if(subClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Not OWL 2 EL !");
			} else {
				// A subsumes ClassExpression , where class Expression is intersectionOf
				setCurrentClassExpression(subClassExpr);
				ClassExpressionNormalize ceVisitor = new ClassExpressionNormalize(false,true);
				// A subsumes ClassExpression 
				for (OWLClassExpression ce1 : superClassExpr.asConjunctSet()) {
					if (ce1 instanceof OWLObjectComplementOf) {
						throw new IllegalArgumentException("Complement Class Name Exception !!");
					} else {
						if(ce1.isClassExpressionLiteral()) {
							n_axioms.add(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), ce1));
							clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
							clsEDB.add(Expressions.makePredicate(ce1.toString()+"rule", 1));
							subClassEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), ce1).toString()+"rule", 2));
							setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), ce1).toString(), 2),
									Expressions.makeConstant(getCurrentClassExpression().toString()), 
									Expressions.makeConstant(ce1.toString())));
							setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), Expressions.makeConstant(getCurrentClassExpression().toString())));
							setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce1.toString(), 1), Expressions.makeConstant(ce1.toString())));								
						} else {
							//If ce1 is a class expression other than intersectionOf and complementOf
							System.out.println("here!!");
							ce1.accept(ceVisitor);
						}
					} 
				}				
			}
		}  else if (superClassExpr.isClassExpressionLiteral() && !subClassExpr.isClassExpressionLiteral()
				&& subClassExpr instanceof OWLObjectIntersectionOf ) {
			if(superClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Not OWL 2 EL !");
			} else {
				// ClassExpression subsumes A, where class expression is a intersectionOf 
				setCurrentClassExpression(superClassExpr);
				//C and D subsumes B
				Set<OWLClassExpression> descriptions = subClassExpr.asConjunctSet(); 
				Set<OWLClassExpression> new_descriptions = new HashSet<>();
				int n = descriptions.size() % 2;
				int i = 0;
				for (OWLClassExpression ce: descriptions) {
					if(i==n)
						break;
					new_descriptions.add(ce);
					i++;
				}
				descriptions.removeAll(new_descriptions);
				//remove nulls from hashsets
				descriptions.remove(null);
				new_descriptions.remove(null);
				//classname and classname subsumes B
				if (v_factory.getOWLObjectIntersectionOf(new_descriptions).asConjunctSet().size() ==1 && 
						v_factory.getOWLObjectIntersectionOf(descriptions).asConjunctSet().size() ==1) {
					OWLClassExpression oce = null;
					for (OWLClassExpression owlClassExpression : new_descriptions) {
						oce = owlClassExpression; 
					} 
					OWLClassExpression desce = null;
					for (OWLClassExpression owlClassExpression : descriptions) {
						desce = owlClassExpression; 
					} 
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectIntersectionOf(
							oce, 
							desce), 
							getCurrentClassExpression()));
					clsEDB.add(Expressions.makePredicate(oce.toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate(desce.toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate(superClassExpr.toString()+"rule", 1));
					subConjEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectIntersectionOf(
							oce, 
							desce), 
							getCurrentClassExpression()).toString()+"rule", 3));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectIntersectionOf(
							oce, 
							desce), 
							getCurrentClassExpression()).toString(), 3), 
							Expressions.makeConstant(oce.toString()),
							Expressions.makeConstant(desce.toString()),
							Expressions.makeConstant(superClassExpr.toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(oce.toString(), 1), Expressions.makeConstant(oce.toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(desce.toString(), 1), Expressions.makeConstant(desce.toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(superClassExpr.toString(), 1), Expressions.makeConstant(superClassExpr.toString())));
				} else if (v_factory.getOWLObjectIntersectionOf(new_descriptions).asConjunctSet().size() ==1 
						&& !v_factory.getOWLObjectIntersectionOf(descriptions).isClassExpressionLiteral()){ //A and D 
					OWLClassExpression oce = null;
					for (OWLClassExpression owlClassExpression : new_descriptions) {
						oce = owlClassExpression; 
					} 
					k_axioms.add(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectIntersectionOf(descriptions), addFreshClassName(freshConceptNumber)));
					subConjEDB.add(Expressions.makePredicate(oce.toString()+
							addFreshClassName(freshConceptNumber).toString()+
							superClassExpr.toString()+"rule", 
							3));
					clsEDB.add(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate(oce.toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate(superClassExpr.toString()+"rule", 1));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(oce.toString()+
							addFreshClassName(freshConceptNumber).toString()+
							superClassExpr.toString(), 
							3), 
							Expressions.makeConstant(oce.toString()),
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant(superClassExpr.toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString(), 1), Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(oce.toString(), 1), Expressions.makeConstant(oce.toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(superClassExpr.toString(), 1), Expressions.makeConstant(superClassExpr.toString())));
					new_descriptions.add(addFreshClassName(freshConceptNumber));
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectIntersectionOf(new_descriptions),
							superClassExpr));
					freshConceptNumber++;
				} /*else if (!v_factory.getOWLObjectIntersectionOf(new_descriptions).isClassExpressionLiteral() 
						&& v_factory.getOWLObjectIntersectionOf(descriptions).isClassExpressionLiteral()) { //D and A
					//TODO Done
					setFacts.add(Expressions.makeAtom(subConjEDB, 
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant( v_factory.getOWLObjectIntersectionOf(descriptions).toString()),
							Expressions.makeConstant(getCurrentClassExpression().toString())));
					freshConceptNumber++;

				}*/else if(subClassExpr.asConjunctSet().size() > 1 || superClassExpr.asConjunctSet().size() > 1) { //C and D
					k_axioms.add(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectIntersectionOf(
							addFreshClassName(freshConceptNumber),v_factory.getOWLObjectIntersectionOf(descriptions)), 
							superClassExpr
							));
					k_axioms.add(v_factory.getOWLSubClassOfAxiom(
							v_factory.getOWLObjectIntersectionOf(new_descriptions), addFreshClassName(freshConceptNumber)));
					clsEDB.add(Expressions.makePredicate(superClassExpr.toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString()+"rule", 1));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(superClassExpr.toString(), 1), Expressions.makeConstant(superClassExpr.toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString(), 1), Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
					freshConceptNumber++;					
				}
			}
		} else if (subClassExpr instanceof OWLIndividual && superClassExpr.isClassExpressionLiteral()) {
			if (superClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Complement of a named class is not OWL 2 EL !");
			} else {
				clsEDB.add(Expressions.makePredicate(subClassExpr.toString()+"rule", 1));
				clsEDB.add(Expressions.makePredicate(superClassExpr.toString()+"rule", 1));
				subClassEDB.add(Expressions.makePredicate(subClassExpr.toString()+superClassExpr.toString()+"rule", 2));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(subClassExpr.toString(), 1), Expressions.makeConstant(subClassExpr.toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(superClassExpr.toString(), 1), Expressions.makeConstant(superClassExpr.toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(subClassExpr.toString()+superClassExpr.toString(), 2), Expressions.makeConstant(subClassExpr.toString()),
						Expressions.makeConstant(superClassExpr.toString())
						));
			}
		} else{
			throw new IllegalArgumentException("Not OWL 2 EL !! sub----" + subClassExpr.toString() + " "
					+ "----super    "+superClassExpr.toString());
		}

	}
	/**
	 * Datalog Rules
	 */
	public void rules() {
/*		listRules.add(Expressions.makeRule(Expressions.makeAtom(subClass, x,y),
				Expressions.makeAtom(subClassEDB, x,y)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(nom, x), 
				Expressions.makeAtom(nomEDB, x)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(top, x), 
				Expressions.makeAtom(topEDB, x)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(bot, x), 
				Expressions.makeAtom(botEDB, x)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(cls, x), 
				Expressions.makeAtom(clsEDB, x)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(rol, x), 
				Expressions.makeAtom(rolEDB, x)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(subConj, x,y,z), 
				Expressions.makeAtom(subConjEDB, x,y,z)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(subEx, x,y,z), 
				Expressions.makeAtom(subExEDB, x,y,z)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(supEx, w,x,y,z), 
				Expressions.makeAtom(supExEDB, w,x,y,z)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(subSelf, x,y), 
				Expressions.makeAtom(subSelfEDB, x,y)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(supSelf, x,y), 
				Expressions.makeAtom(supSelfEDB, x,y)));
		listRules.add(Expressions.makeRule(Expressions.makeAtom(subRole, x,y),
				Expressions.makeAtom(subRoleEDB, x,y)));*/

		
		for (Predicate predicate : nomEDB) {
			// nom(x) :- inst(x,x)
			listRules.add(Expressions.makeRule(Expressions.makeAtom(predicate, x), 
					Expressions.makeAtom(inst, x,x)));
			//nom(x), triple(x,y,x) :- self(x,y)
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, x),
					Expressions.makeAtom(triple, x,y,x)
					),
					Expressions.makeConjunction(Expressions.makeAtom(self, x,y))));
		}


		//top(x), inst(y,z) :- inst (y,x)
		for (Predicate predicate : topEDB) {
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, x),
					Expressions.makeAtom(inst, y,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(inst, y,x))));
		}
		//use botEDB and clsEDB
		//bot(z) , inst (x,z) , inst (v,w) , cls(y) :- inst (v,y)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(bot, z),
				Expressions.makeAtom(inst, x,z),
				Expressions.makeAtom(inst, v,w),
				Expressions.makeAtom(cls, y)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, v,y))));
		
		for (Predicate predicate : subClassEDB) {
			//rule subclass (A,B) , subclass(B,C) :- subclass (A,C)   
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, x,y),
					Expressions.makeAtom(inst, y,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(inst, x,z))));
		}

		//subConj(x,y,z) , inst(v,x) , inst(v,y) :- inst(v,z)
		for (Predicate predicate : subConjEDB) {
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, x,y,z),
					Expressions.makeAtom(inst, v,x),
					Expressions.makeAtom(inst, v,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(inst, v,z))));
		}
		
		//subEx(v,y,z) , triple(x,v,w) , inst(w,y) :- inst(x,z)
		for (Predicate predicate : subExEDB) {
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, v,y,z),
					Expressions.makeAtom(triple, x,v,w),
					Expressions.makeAtom(inst, w,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(inst, x,z))));
			//subEx(v,y,z) , self(x,v) , inst(x,y) :- inst(x,z)
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, v,y,z),
					Expressions.makeAtom(self, x,v),
					Expressions.makeAtom(inst, x,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(inst, x,z))));
		}


		
		for (Predicate predicate : supExEDB) {
			//supEx(v,w,y,z) , inst(x,v) :- triple(z,y)
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, v,w,y,z),
					Expressions.makeAtom(inst, x,v)
					),
					Expressions.makeConjunction(Expressions.makeAtom(inst, z,y))));
			//supEx(v,w,x,y) , inst(z,v) :- inst(y,x)
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, v,w,x,y),
					Expressions.makeAtom(inst, z,v)
					),
					Expressions.makeConjunction(Expressions.makeAtom(inst, y,x))));
		}
		
		for (Predicate predicate : subSelfEDB) {
			//subSelf(v,z) , self(x,v) :- inst(x,z)
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, v,z),
					Expressions.makeAtom(self, x,v)
					),
					Expressions.makeConjunction(Expressions.makeAtom(inst, x,z))));
		}
		for (Predicate predicate : supSelfEDB) {
			//supSelf(y,v) , inst(x,y) :- self(x,v)
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, y,v),
					Expressions.makeAtom(inst, x,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(self, x,v))));
		}	
		for (Predicate predicate : nomEDB) {
			//inst(x,y) , nom(y) , inst(x,z) :- inst(y,z)
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(inst, x,y),
					Expressions.makeAtom(predicate, y),
					Expressions.makeAtom(inst, x,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(inst, y,z))));
			//inst(x,y) , nom(y) , inst(x,z) :- inst(x,z)
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(inst, x,y),
					Expressions.makeAtom(predicate, y),
					Expressions.makeAtom(inst, x,z)
					),
					Expressions.makeConjunction(Expressions.makeAtom(inst, x,z))));

			//inst(x,y) , nom(y) , triple(z,v,x) :- triple(z,v,y)
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(inst, x,y),
					Expressions.makeAtom(predicate, y),
					Expressions.makeAtom(triple, z,v,x)
					),
					Expressions.makeConjunction(Expressions.makeAtom(triple, z,v,y))));
		}
		for (Predicate predicate : subRoleEDB) {
			//role rules (13) (14) from paper
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, v,w),
					Expressions.makeAtom(triple, x,v,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(triple, x,w,y))));		
			listRules.add(Expressions.makeRule(Expressions.makeConjunction(
					Expressions.makeAtom(predicate, v,w),
					Expressions.makeAtom(self, x,y)
					),
					Expressions.makeConjunction(Expressions.makeAtom(self, x,w))));
		}
	}
	/*
	 * 
	 */
	public void callReasoner() throws ReasonerStateException, EdbIdbSeparationException, IncompatiblePredicateArityException, IOException {
		Reasoner reasoner = Reasoner.getInstance();
		reasoner.addRules(listRules);
		reasoner.addFacts(setFacts);
		
		reasoner.load();
		reasoner.setAlgorithm(Algorithm.SKOLEM_CHASE);
		reasoner.setReasoningTimeout(1);
		System.out.println("Starting Skolem Chase with 1 second timeout.");

		/* Indeed, the Skolem Chase did not terminate before timeout. */
		boolean skolemChaseFinished = reasoner.reason();
		System.out.println("Has Skolem Chase algorithm finished before 1 second timeout? " + skolemChaseFinished);
		System.out.println(
				"Answers to query " + Expressions.makeAtom(inst, x,y) + " after reasoning with the Skolem Chase for 1 second:");
		printOutQueryAnswers(Expressions.makeAtom(inst, x,y), reasoner);
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

	/*
	 * Visitor Class for Axioms in the Ontology.
	 */
	protected class AxiomVisitor implements OWLAxiomVisitor {
		//protected final List<OWLClassExpression[]> v_expressionList;


		public AxiomVisitor() {
			//
		}

		@Override
		public void visit(OWLAnnotationAssertionAxiom axiom) {
			//Annotations do not change the logical meaning of the OWL Ontology.
		}

		@Override
		public void visit(OWLSubAnnotationPropertyOfAxiom arg0) {

		}

		@Override
		public void visit(OWLAnnotationPropertyDomainAxiom arg0) {
		}

		@Override
		public void visit(OWLAnnotationPropertyRangeAxiom arg0) {
		}

		@Override
		public void visit(OWLDeclarationAxiom arg0) {
			//Entity (Not Logical Axioms)
		}

		@Override
		public void visit(OWLSubClassOfAxiom axiom) {
			if(!axiom.getSubClass().isBottomEntity() || !axiom.getSuperClass().isTopEntity() ) {
				normalizesubClassExpressionAxiom(axiom.getSubClass(), axiom.getSuperClass());
			} else {
				//Empty Set
			}
		}
		@Override
		public void visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
			//throw new IllegalAccessError("Annotation Negative Object Property Assertion Axiom Exception !");
			throw new IllegalArgumentException("The axiom "+arg0+" contains Negative Object property, "
					+ "which is not allowed in OWL 2 EL. ");
			//A negative ce_bool property assertion NegativeObjectPropertyAssertion( OPE a1 a2 ) 
			//states that the individual a1 is not connected by the ce_bool property expression OPE to the individual a2.
			//not in OWL 2 EL.
		}

		@Override
		public void visit(OWLAsymmetricObjectPropertyAxiom arg0) {
			throw new IllegalAccessError("Asymmetric Object Property Axiom Exception !");
		}

		@Override
		public void visit(OWLReflexiveObjectPropertyAxiom arg0) {
			throw new IllegalAccessError("Reflexive Object Property Axiom Exception !");
		}

		@Override
		public void visit(OWLDisjointClassesAxiom axiom) {
			throw new IllegalAccessError("Disjoint Class Axiom Exception !");
		}

		@Override
		public void visit(OWLDataPropertyDomainAxiom arg0) {
			throw new IllegalAccessError("Data Property Domain Axiom Exception !");
		}

		@Override
		public void visit(OWLObjectPropertyDomainAxiom axiom) {
			k_axioms.add(axiom.asOWLSubClassOfAxiom());
		}

		@Override
		public void visit(OWLEquivalentObjectPropertiesAxiom arg0) {
			//TODO
			k_axioms.addAll(arg0.asSubObjectPropertyOfAxioms());
		}

		@Override
		public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
			throw new IllegalArgumentException("Negative Data Property Assertion Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLDifferentIndividualsAxiom axiom) {
			throw new IllegalArgumentException(
					"Not an OWL 2 EL axiom ! "+axiom.toString()+" Different Individuals Axiom !");
		}

		@Override
		public void visit(OWLDisjointDataPropertiesAxiom axiom) {
			throw new IllegalArgumentException(
					"Not an OWL 2 EL axiom ! "+axiom.toString()+" Disjoint data Property Axiom !");
		}

		@Override
		public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
			throw new IllegalArgumentException(
					"Not an OWL 2 EL axiom ! "+axiom.toString()+" Disjoint Object Property Axiom !");
		}

		@Override
		public void visit(OWLObjectPropertyRangeAxiom axiom) {
			//throw new IllegalArgumentException(	"Not an OWL 2 EL axiom ! "+axiom.toString()+" Object Property Range Axiom !");
		}

		@Override
		public void visit(OWLObjectPropertyAssertionAxiom axiom) {
			//TODO
			n_axioms.add(axiom);
			Set<OWLIndividual> indi = new HashSet<>();
			axiom.individualsInSignature().forEach(x -> indi.add(x));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(axiom.getProperty().toString()+indi.toArray()[0].toString()+indi.toArray()[1].toString(), 3),
					Expressions.makeConstant(indi.toArray()[0].toString()),
					Expressions.makeConstant(axiom.getProperty().toString()),										
					Expressions.makeConstant(indi.toArray()[1].toString())
					));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(indi.toArray()[0].toString(), 1), Expressions.makeConstant(indi.toArray()[0].toString())));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(indi.toArray()[1].toString(), 1), Expressions.makeConstant(indi.toArray()[1].toString())));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(axiom.getProperty().toString(), 1), Expressions.makeConstant(axiom.getProperty().toString())));
			rolEDB.add(Expressions.makePredicate(axiom.getProperty().toString()+"rule", 1));
			rolEDB.add(Expressions.makePredicate(indi.toArray()[0].toString()+"rule", 1));
			rolEDB.add(Expressions.makePredicate(indi.toArray()[1].toString()+"rule", 1));
			subExEDB.add(Expressions.makePredicate(axiom.getProperty().toString()+indi.toArray()[0].toString()+indi.toArray()[1].toString()+"rule", 3));
		}

		@Override
		public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException("Functional Object Property Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLSubObjectPropertyOfAxiom axiom) {
			//TODO only subRole(R,T) done if()
			n_axioms.add(axiom);
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(axiom.getSubProperty().toString()+axiom.getSuperProperty().toString(), 2), 
					Expressions.makeConstant(axiom.getSubProperty().toString()),
					Expressions.makeConstant(axiom.getSuperProperty().toString())
					));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(axiom.getSubProperty().toString(), 1), Expressions.makeConstant(axiom.getSubProperty().toString())));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(axiom.getSuperProperty().toString(), 1), Expressions.makeConstant(axiom.getSuperProperty().toString())));
			subRoleEDB.add(Expressions.makePredicate(axiom.getSubProperty().toString()+axiom.getSuperProperty().toString()+"rule", 2));
			rolEDB.add(Expressions.makePredicate(axiom.getSubProperty().toString()+"rule", 1));
			rolEDB.add(Expressions.makePredicate(axiom.getSuperProperty().toString()+"rule", 1));
		}

		@Override
		public void visit(OWLDisjointUnionAxiom axiom) {
			throw new IllegalArgumentException("Disjoint Union Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException("Symmetric Object Property Axiom" + axiom.toString());
		}

		@Override
		public void visit(OWLDataPropertyRangeAxiom arg0) {
			throw new IllegalAccessError("Data Property Range Axiom Exception !");
		}

		@Override
		public void visit(OWLFunctionalDataPropertyAxiom axiom) {
			throw new IllegalArgumentException("Functional Data Property Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
			throw new IllegalArgumentException("Equivalent Data Properties Axiom Exception !" + axiom.toString());
		}
		@Override
		public void visit(OWLClassAssertionAxiom axiom) {
			//X(a)
			if (axiom.getClassExpression().isClassExpressionLiteral() && 
					!axiom.getClassExpression().isAnonymous()) {
				if (axiom.getClassExpression() instanceof OWLObjectComplementOf) {
					throw new IllegalArgumentException("Object Complement of !" + axiom.toString());
				} else {
					n_axioms.add(v_factory.getOWLClassAssertionAxiom(axiom.getClassExpression(), axiom.getIndividual()));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLClassAssertionAxiom(axiom.getClassExpression(), axiom.getIndividual()).toString(), 2), 
							Expressions.makeConstant(axiom.getIndividual().toString()),
							Expressions.makeConstant(axiom.getClassExpression().toString())
							));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(axiom.getIndividual().toString(), 1), Expressions.makeConstant(axiom.getIndividual().toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(axiom.getClassExpression().toString(), 1), Expressions.makeConstant(axiom.getClassExpression().toString())));
					clsEDB.add(Expressions.makePredicate(axiom.getClassExpression().toString()+"rule", 1));
					nomEDB.add(Expressions.makePredicate(axiom.getIndividual().toString()+"rule", 1));
					subClassEDB.add(Expressions.makePredicate(axiom.getIndividual().toString()+axiom.getClassExpression().toString()+"rule", 2));
				}
			} else {
				//C(a)
				k_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), 
						axiom.getClassExpression()));
				n_axioms.add(v_factory.getOWLClassAssertionAxiom(addFreshClassName(freshConceptNumber), 
						axiom.getIndividual()));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLClassAssertionAxiom(addFreshClassName(freshConceptNumber), axiom.getIndividual()).toString(), 2), 
						Expressions.makeConstant(axiom.getIndividual().toString()),
						Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())
						));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString(), 1), Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(axiom.getIndividual().toString(), 1), Expressions.makeConstant(axiom.getIndividual().toString())));
				clsEDB.add(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString()+"rule", 1));
				nomEDB.add(Expressions.makePredicate(axiom.getIndividual().toString()+"rule", 1));
				subClassEDB.add(Expressions.makePredicate(axiom.getIndividual().toString()+addFreshClassName(freshConceptNumber).toString()+"rule", 2));			
				freshConceptNumber++;					
			}		
		}

		@Override
		public void visit(OWLEquivalentClassesAxiom axiom) {
			//C equivalent to D
			k_axioms.addAll(axiom.asOWLSubClassOfAxioms());
		}

		@Override
		public void visit(OWLDataPropertyAssertionAxiom axiom) {
			throw new IllegalArgumentException("Data Property Assertion Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException("Transitive Object Property Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException("Irreflexive Object Property Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLSubDataPropertyOfAxiom axiom) {
			throw new IllegalArgumentException("Sub Data Property Of Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException("Inverse Functional Object Property Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLSameIndividualAxiom axiom) {
			throw new IllegalArgumentException("Same Individual Axiom Exception !");
		}

		@Override
		public void visit(OWLSubPropertyChainOfAxiom axiom) {
			throw new IllegalArgumentException("Sub Property Chain Of Axiom" + axiom.toString());
		}

		@Override
		public void visit(OWLInverseObjectPropertiesAxiom axiom) {
			//throw new IllegalArgumentException("Inverse Object Property Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLHasKeyAxiom axiom) {
			throw new IllegalArgumentException("Has Key Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLDatatypeDefinitionAxiom axiom) {
			throw new IllegalArgumentException("Data Type Definition Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(SWRLRule axiom) {
			throw new IllegalArgumentException("SWRL rule Exception !" + axiom.toString());
		}
	}


	protected class ClassExpressionNormalize implements OWLClassExpressionVisitor{

		protected final boolean boolSub;
		protected final boolean boolSuper;
		public ClassExpressionNormalize(boolean sub, boolean sup) {
			// boolSub is true when it is not a named class else false. similar for boolSuper.
			boolSub = sub;
			boolSuper = sup;
		}

/*		@Override
		public void visit(OWLObjectIntersectionOf ce) {
			//Done in normalizesubClassExpressionAxiom method above
			if (boolSuper) {
				normalizesubClassExpressionAxiom(getCurrentClassExpression(), ce);
			}else if (boolSub) {
				normalizesubClassExpressionAxiom(ce, getCurrentClassExpression());
			}
		}*/

		@Override
		public void visit(OWLObjectUnionOf ce) {
			throw new IllegalArgumentException("OWLObjectUnionOf not OWL 2 EL ! " + ce.toString()  );
		}

		@Override
		public void visit(OWLObjectComplementOf ce) {
			throw new IllegalArgumentException("OWLObjectComplementOf not OWL 2 EL ! " + ce.toString()  );
		}

		@Override
		public void visit(OWLObjectSomeValuesFrom ce) {
			if(boolSuper) {
				if (!ce.getFiller().isClassExpressionLiteral()) {
					//A subsumes Exists.R. C
					if (ce.getFiller() instanceof OWLObjectIntersectionOf) {
						k_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber),ce.getFiller()));
					} 
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber))));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber))).toString(), 4),
							Expressions.makeConstant(getCurrentClassExpression().toString()),
							Expressions.makeConstant(ce.getProperty().toString()),
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant("aux"+auxnum)							
							));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString(), 1), Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate("aux"+auxnum, 1), Expressions.makeConstant("aux"+auxnum)));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), Expressions.makeConstant(ce.getProperty().toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), Expressions.makeConstant(getCurrentClassExpression().toString())));
					supExEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber))).toString()+"rule", 4));
					clsEDB.add(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate("aux"+auxnum+"rule", 1));
					rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
					setCurrentClassExpression(addFreshClassName(freshConceptNumber));
					auxnum++;
					freshConceptNumber++;
				} else {
					//A subsumes Exists.R. B
					if (ce.getFiller() instanceof OWLObjectComplementOf) {
						throw new IllegalArgumentException("OWLObjectComplementOf not OWL 2 EL ! " + ce.toString()  );
					}else {
						n_axioms.add(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
								v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), ce.getFiller())));
						setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
								v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), ce.getFiller())).toString(), 4),
								Expressions.makeConstant(getCurrentClassExpression().toString()),
								Expressions.makeConstant(ce.getProperty().toString()),
								Expressions.makeConstant(ce.getFiller().toString()),
								Expressions.makeConstant("aux"+auxnum)							
								));
						setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getFiller().toString(), 1), Expressions.makeConstant(ce.getFiller().toString())));
						setFacts.add(Expressions.makeAtom(Expressions.makePredicate("aux"+auxnum, 1), Expressions.makeConstant("aux"+auxnum)));
						setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), Expressions.makeConstant(ce.getProperty().toString())));
						setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), Expressions.makeConstant(getCurrentClassExpression().toString())));
						supExEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
								v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), ce.getFiller())).toString().toString()+"rule", 4));
						clsEDB.add(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString()+"rule", 1));
						clsEDB.add(Expressions.makePredicate("aux"+auxnum+"rule", 1));
						rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
						clsEDB.add(Expressions.makePredicate(ce.getFiller().toString()+"rule", 1));
						auxnum++;
					}
				}
			} else if (boolSub) {
				if(!ce.getFiller().isClassExpressionLiteral()) {
					// Exists.R.C subsumes A
					//C subsumes X
					if (ce.getFiller() instanceof OWLObjectIntersectionOf)
						k_axioms.add(v_factory.getOWLSubClassOfAxiom(ce.getFiller(), addFreshClassName(freshConceptNumber)));
					//Exists.R.X subsumes A
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber)), 
							getCurrentClassExpression()));
					//subEx(R,X,A) where X-> new concept
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber)), 
							getCurrentClassExpression()).toString(), 3), 
							Expressions.makeConstant(ce.getProperty().toString()),
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant(getCurrentClassExpression().toString())
							));
					//role
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString(), 1), Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), Expressions.makeConstant(getCurrentClassExpression().toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), Expressions.makeConstant(ce.getProperty().toString())));	
					subExEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber)), 
							getCurrentClassExpression()).toString()+"rule", 3));
					clsEDB.add(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString()+"rule", 1));
					rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
					setCurrentClassExpression(addFreshClassName(freshConceptNumber));
					freshConceptNumber++;
				} else {
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()));
					//Exists.R.B subsumes A
					//subEx(R,B,A)
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()).toString(), 3), Expressions.makeConstant(ce.getProperty().toString()), 
							Expressions.makeConstant(ce.getFiller().toString()),
							Expressions.makeConstant(getCurrentClassExpression().toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getFiller().toString(), 1), Expressions.makeConstant(ce.getFiller().toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), Expressions.makeConstant(getCurrentClassExpression().toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), Expressions.makeConstant(ce.getProperty().toString())));
					subExEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()).toString()+"rule", 3));
					clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
					rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate(ce.getFiller().toString()+"rule", 1));
				}
			}
		}

		@Override
		public void visit(OWLObjectAllValuesFrom ce) {
			//throw new IllegalStateException("OWLObjectAllValuesFrom " + ce.toString());
		}

		@Override
		public void visit(OWLObjectHasValue ce) {
			k_axioms.add(v_factory.getOWLSubClassOfAxiom(ce.asSomeValuesFrom(), 
					getCurrentClassExpression()
					));
		}

		@Override
		public void visit(OWLObjectMinCardinality ce) {
			k_axioms.add(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), (OWLClassExpression) ce.getFiller()), 
					getCurrentClassExpression()
					));
		}

		@Override
		public void visit(OWLObjectExactCardinality ce) {
			throw new IllegalStateException("OWLObjectExactCardinality " + ce.toString());
		}

		@Override
		public void visit(OWLObjectMaxCardinality ce) {
			throw new IllegalStateException("OWLObjectMaxCardinality " + ce.toString());
		}

		@Override
		public void visit(OWLObjectHasSelf ce) {
			if(boolSuper) {
				//A subsumes Exists Self R
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), ce));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), ce).toString(), 2),
						Expressions.makeConstant(getCurrentClassExpression().toString()),
						Expressions.makeConstant(ce.getProperty().toString())						
						));

				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), 
						Expressions.makeConstant(ce.getProperty().toString())
						));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), 
						Expressions.makeConstant(getCurrentClassExpression().toString())
						));
				rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
				clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
				supSelfEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), ce).toString()+"rule", 2));
			} else if (boolSub) {
				//Exists Self R subsumes A
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()).toString(), 2),
						Expressions.makeConstant(ce.getProperty().toString()),
						Expressions.makeConstant(getCurrentClassExpression().toString())						
						));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), 
						Expressions.makeConstant(ce.getProperty().toString())
						));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), 
						Expressions.makeConstant(getCurrentClassExpression().toString())
						));
				rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
				clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
				subSelfEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()).toString()+"rule", 2));
			
			}
		}

		@Override
		public void visit(OWLObjectOneOf ce) {
			throw new IllegalStateException("OWLObjectOneOf " + ce.toString());
		}

		@Override
		public void visit(OWLDataSomeValuesFrom ce) {
			throw new IllegalStateException("OWLDataSomeValuesFrom " + ce.toString());
		}

		@Override
		public void visit(OWLDataAllValuesFrom ce) {
			throw new IllegalStateException("OWLDataAllValuesFrom " + ce.toString());
		}

		@Override
		public void visit(OWLDataHasValue ce) {
			throw new IllegalStateException("OWLDataHasValue " + ce.toString());
		}

		@Override
		public void visit(OWLDataMinCardinality ce) {
			throw new IllegalStateException("OWLDataMinCardinality " + ce.toString());
		}

		@Override
		public void visit(OWLDataExactCardinality ce) {
			throw new IllegalStateException("OWLDataExactCardinality " + ce.toString());
		}

		@Override
		public void visit(OWLDataMaxCardinality ce) {
			throw new IllegalStateException("OWLDataMaxCardinality " + ce.toString());
		}
	}
}
