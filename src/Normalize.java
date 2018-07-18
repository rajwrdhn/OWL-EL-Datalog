import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
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

	protected final Map<Integer,Set<OWLAxiom>> v_Iterable_MapAxioms = new HashMap<>();
	protected static int v_Iterable_KeyForMap = 0;

	protected final Set<OWLAxiom> v_Normalised_Axioms = new HashSet<>();

	protected final OWLDataFactory v_factory;

	protected final Set<OWLAxiom> v_axioms;
	protected final Set<OWLAxiom> n_axioms;
	protected final Set<OWLAxiom> k_axioms;
	protected final Set<OWLAxiom> del_axioms;
	protected final Map<Integer, Set<OWLAxiom>> v_modifyingMapForAxioms = new HashMap<>();
	public static OWLClassExpression v_classExpression;
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
	//constructor
	public Normalize(OWLDataFactory factory) {
		v_factory=factory;
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
		onto.axioms().forEach(x -> v_Iterable_MapAxioms.get(v_Iterable_KeyForMap).add(x));
		//k_axioms.addAll((Collection<? extends OWLAxiom>) onto.axioms());
		if(v_Iterable_MapAxioms.isEmpty()) {
			System.out.println("No Axioms in the Ontology!!");
		} else {
			visitAxioms(v_Iterable_MapAxioms.get(v_Iterable_KeyForMap));
		}

		//return n_axioms;
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
		if (v_Iterable_MapAxioms.get(v_Iterable_KeyForMap).isEmpty()) {
			System.out.println("Normalisation Complete!! ");
		} else {
			visitAxioms(v_Iterable_MapAxioms.get(v_Iterable_KeyForMap));
		}
	}

	public void crunchCleanNormalisedAxiomFromMap(int keyA) {		
		v_Iterable_MapAxioms.remove(keyA);		
	}

	public void addAxiomToMap (int number, OWLAxiom axiom) {
		v_Iterable_MapAxioms.get(number).add(axiom);		
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

/*	*//**
	 * ClassExpression Visitor for subclassOf Axioms
	 * @param ce
	 *//*
	public void normalizesubClassExpressionAxiom(OWLClassExpression subClassExpr, OWLClassExpression superClassExpr) {

		if (subClassExpr.isOWLThing() && superClassExpr.isClassExpressionLiteral() ) {
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
				} else if (!v_factory.getOWLObjectIntersectionOf(new_descriptions).isClassExpressionLiteral() 
						&& v_factory.getOWLObjectIntersectionOf(descriptions).isClassExpressionLiteral()) { //D and A
					//TODO Done
					setFacts.add(Expressions.makeAtom(subConjEDB, 
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant( v_factory.getOWLObjectIntersectionOf(descriptions).toString()),
							Expressions.makeConstant(getCurrentClassExpression().toString())));
					freshConceptNumber++;

				}else if(subClassExpr.asConjunctSet().size() > 1 || superClassExpr.asConjunctSet().size() > 1) { //C and D
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

	}*/
}
