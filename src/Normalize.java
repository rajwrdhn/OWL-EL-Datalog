import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

public class Normalize {
	protected final OWLDataFactory v_factory;
	protected final Set<OWLAxiom> v_axioms;
	protected final Set<OWLAxiom> n_axioms;
	protected final Set<OWLAxiom> del_axioms;
	protected static OWLClassExpression v_classExpression;
	protected final Optional<IRI> v_IRI;
	protected static long freshConceptNumber;
	protected static long auxnum;
	protected final Set<Atom> setFacts;
	protected final List<Rule> listRules;
	protected final Predicate nomEDB;
	protected final Predicate clsEDB;
	protected final Predicate rolEDB;
	protected final Predicate triple;
	protected final Predicate subClassEDB;
	protected final Predicate inst;
	protected final Predicate topEDB;
	protected final Predicate botEDB;
	protected final Predicate subConjEDB;
	protected final Predicate subExEDB;
	protected final Predicate supExEDB;
	protected final Predicate subSelfEDB;
	protected final Predicate supSelfEDB;
	protected final Predicate subRoleEDB;
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
	public Normalize(OWLDataFactory factory,OWLOntologyID ontID) {
		v_factory=factory;
		v_IRI = ontID.getOntologyIRI();
		v_axioms= new HashSet<>();
		n_axioms= new HashSet<>();
		del_axioms = new HashSet<>();
		v_classExpression = null;
		freshConceptNumber = 1;
		auxnum = 1;
		setFacts = new HashSet<>();
		listRules = new ArrayList<>();
		nomEDB = Expressions.makePredicate("nomEDB",1);
		clsEDB = Expressions.makePredicate("clsEDB",1);
		rolEDB = Expressions.makePredicate("rolEDB", 1);
		triple = Expressions.makePredicate("triple", 3);
		subClassEDB = Expressions.makePredicate("subclassEDB", 2);
		inst = Expressions.makePredicate("inst", 2);
		topEDB = Expressions.makePredicate("topEDB", 1);
		botEDB = Expressions.makePredicate("botEDB", 1);
		subConjEDB = Expressions.makePredicate("subconjEDB", 3);
		subExEDB = Expressions.makePredicate("subexEDB", 3);
		supExEDB = Expressions.makePredicate("supexEDB", 4);
		subSelfEDB = Expressions.makePredicate("subselfEDB", 2);
		supSelfEDB = Expressions.makePredicate("supselfEDB", 2);
		subRoleEDB = Expressions.makePredicate("subroleEDB", 2);
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
		//v_axioms.addAll((Collection<? extends OWLAxiom>) onto.axioms());
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
		AxiomVisitor axmVisitor = new AxiomVisitor();
		for (OWLAxiom axiom : axioms) {
			axiom.accept(axmVisitor);	
			del_axioms.add(axiom);	
		}
		for (OWLAxiom axiom : del_axioms ) {
			v_axioms.remove(axiom);
		}
		if (v_axioms.isEmpty()) {
			System.out.println("Complete!!");
		} else {
			visitAxioms(v_axioms);
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

		if (subClassExpr.isTopEntity() && superClassExpr.isClassExpressionLiteral() ) {
			//TODO what if the super classexpression is a conjunction of CEs ? Can it be?
			//Top subsumes A 
			if (superClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Complement of a named class is not OWL 2 EL !");
			} else {
				setFacts.add(Expressions.makeAtom(topEDB, Expressions.makeConstant(superClassExpr.toString())));				
			}
		} else if (superClassExpr.isBottomEntity() && subClassExpr.isClassExpressionLiteral()) {
			//TODO what if the sub classexpression is a conjunction of CEs? Can it be?
			//A subsumes Bot
			if (subClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Complement of a named class is not OWL 2 EL !");
			} else {
				setFacts.add(Expressions.makeAtom(botEDB, Expressions.makeConstant(subClassExpr.toString())));
			}

		} else if (subClassExpr.isClassExpressionLiteral() && superClassExpr.isClassExpressionLiteral()) {
			if (subClassExpr instanceof OWLObjectComplementOf || superClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Complement of a named class is not OWL 2 EL !");
			} else {
				// A subsumes B 
				// both classnames
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(subClassExpr, superClassExpr));
				//subclass fact A is a subset of B
				setFacts.add(Expressions.makeAtom(subClassEDB, 
						Expressions.makeConstant(subClassExpr.toString()), 
						Expressions.makeConstant(superClassExpr.toString())));
				setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(subClassExpr.toString())));
				setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(superClassExpr.toString())));
			}	
		} else if(subClassExpr.isAnonymous() && superClassExpr.isAnonymous()) {
			// ClassExpr subsumes ClassExpr
			if (!subClassExpr.isClassExpressionLiteral() && !superClassExpr.isClassExpressionLiteral()) {
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(subClassExpr, addFreshClassName(freshConceptNumber)));
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), superClassExpr));
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
							setFacts.add(Expressions.makeAtom(subClassEDB, 
									Expressions.makeConstant(getCurrentClassExpression().toString()), 
									Expressions.makeConstant(ce1.toString())));
							setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(getCurrentClassExpression().toString())));
							setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(ce1.toString())));								
						} else {
							//If ce1 is a class expression other than intersectionOf and complementOf
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
					descriptions.remove(ce);
					i++;
				}
				//remove nulls from hashsets
				descriptions.remove(null);
				new_descriptions.remove(null);
				//classname and classname subsumes B
				if (v_factory.getOWLObjectIntersectionOf(new_descriptions).isClassExpressionLiteral() && 
						v_factory.getOWLObjectIntersectionOf(descriptions).isClassExpressionLiteral()) {
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectIntersectionOf(
							v_factory.getOWLObjectIntersectionOf(new_descriptions), 
							v_factory.getOWLObjectIntersectionOf(descriptions)), 
							getCurrentClassExpression()));
					setFacts.add(Expressions.makeAtom(subConjEDB, 
							Expressions.makeConstant(v_factory.getOWLObjectIntersectionOf(new_descriptions).toString()),
							Expressions.makeConstant(v_factory.getOWLObjectIntersectionOf(descriptions).toString()),
							Expressions.makeConstant(superClassExpr.toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(v_factory.getOWLObjectIntersectionOf(new_descriptions).toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(v_factory.getOWLObjectIntersectionOf(descriptions).toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(superClassExpr.toString())));
				} else if (v_factory.getOWLObjectIntersectionOf(new_descriptions).isClassExpressionLiteral() 
						&& !v_factory.getOWLObjectIntersectionOf(descriptions).isClassExpressionLiteral()){ //A and D 
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectIntersectionOf(descriptions), addFreshClassName(freshConceptNumber)));

					setFacts.add(Expressions.makeAtom(subConjEDB, 
							Expressions.makeConstant(v_factory.getOWLObjectIntersectionOf(new_descriptions).toString()),
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant(superClassExpr.toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(v_factory.getOWLObjectIntersectionOf(descriptions).toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(superClassExpr.toString())));
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

				}*/else { //C and D
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectIntersectionOf(
							addFreshClassName(freshConceptNumber),v_factory.getOWLObjectIntersectionOf(descriptions)), 
							superClassExpr
							));
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(
							v_factory.getOWLObjectIntersectionOf(new_descriptions), addFreshClassName(freshConceptNumber)));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(superClassExpr.toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
					freshConceptNumber++;					
				}
			}
		} else if (subClassExpr instanceof OWLIndividual && superClassExpr.isClassExpressionLiteral()) {
			//TODO Is it correct?
			if (superClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Complement of a named class is not OWL 2 EL !");
			} else {
				setFacts.add(Expressions.makeAtom(nomEDB, Expressions.makeConstant(subClassExpr.toString())));
				setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(superClassExpr.toString())));
				setFacts.add(Expressions.makeAtom(subClassEDB, Expressions.makeConstant(subClassExpr.toString()),
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
		listRules.add(Expressions.makeRule(Expressions.makeAtom(subClass, x,y),
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
				Expressions.makeAtom(subRoleEDB, x,y)));

		// nom(x) :- subClass(x,x)
		listRules.add(Expressions.makeRule(Expressions.makeAtom(nom, x), 
				Expressions.makeAtom(inst, x,x)));
		//nom(x), triple(x,y,x) :- self(x,y)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(nom, x),
				Expressions.makeAtom(triple, x,y,x)
				),
				Expressions.makeConjunction(Expressions.makeAtom(self, x,y))));
		//top(x), inst(y,z) :- inst (y,x)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(top, x),
				Expressions.makeAtom(inst, y,z)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, y,x))));
		//bot(z) , inst (x,z) , inst (v,w) , cls(y) :- inst (v,y)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(bot, z),
				Expressions.makeAtom(inst, x,z),
				Expressions.makeAtom(inst, v,w),
				Expressions.makeAtom(cls, y)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, v,y))));
		//rule subclass (A,B) , subclass(B,C) :- subclass (A,C)   
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(subClass, x,y),
				Expressions.makeAtom(inst, y,z)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, x,z))));
		//subConj(x,y,z) , inst(v,x) , inst(v,y) :- inst(v,z)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(subConj, x,y,z),
				Expressions.makeAtom(inst, v,x),
				Expressions.makeAtom(inst, v,y)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, v,z))));
		//subConj(x,y,z) , inst(v,x) , inst(v,y) :- inst(v,z)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(subConj, x,y,z),
				Expressions.makeAtom(inst, v,x),
				Expressions.makeAtom(inst, v,y)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, v,z))));
		//subEx(v,y,z) , triple(x,v,w) , inst(w,y) :- inst(x,z)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(subEx, v,y,z),
				Expressions.makeAtom(triple, x,v,w),
				Expressions.makeAtom(inst, w,y)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, x,z))));
		//subEx(v,y,z) , self(x,v) , inst(x,y) :- inst(x,z)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(subEx, v,y,z),
				Expressions.makeAtom(self, x,v),
				Expressions.makeAtom(inst, x,y)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, x,z))));
		//supEx(v,w,y,z) , inst(x,v) :- triple(z,y)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(supEx, v,w,y,z),
				Expressions.makeAtom(inst, x,v)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, z,y))));
		//supEx(v,w,x,y) , inst(z,v) :- inst(y,x)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(supEx, v,w,x,y),
				Expressions.makeAtom(inst, z,v)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, y,x))));
		//subSelf(v,z) , self(x,v) :- inst(x,z)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(subSelf, v,z),
				Expressions.makeAtom(self, x,v)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, x,z))));
		//supSelf(y,v) , inst(x,y) :- self(x,v)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(supSelf, y,v),
				Expressions.makeAtom(inst, x,y)
				),
				Expressions.makeConjunction(Expressions.makeAtom(self, x,v))));
		//inst(x,y) , nom(y) , inst(x,z) :- inst(y,z)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(inst, x,y),
				Expressions.makeAtom(nom, y),
				Expressions.makeAtom(inst, x,z)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, y,z))));
		//inst(x,y) , nom(y) , inst(x,z) :- inst(x,z)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(inst, x,y),
				Expressions.makeAtom(nom, y),
				Expressions.makeAtom(inst, x,z)
				),
				Expressions.makeConjunction(Expressions.makeAtom(inst, x,z))));

		//inst(x,y) , nom(y) , triple(z,v,x) :- triple(z,v,y)
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(inst, x,y),
				Expressions.makeAtom(nom, y),
				Expressions.makeAtom(triple, z,v,x)
				),
				Expressions.makeConjunction(Expressions.makeAtom(triple, z,v,y))));
		//role rules (13) (14) from paper
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(subRole, v,w),
				Expressions.makeAtom(triple, x,v,y)
				),
				Expressions.makeConjunction(Expressions.makeAtom(triple, x,w,y))));		
		listRules.add(Expressions.makeRule(Expressions.makeConjunction(
				Expressions.makeAtom(subRole, v,w),
				Expressions.makeAtom(self, x,y)
				),
				Expressions.makeConjunction(Expressions.makeAtom(self, x,w))));
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
			//TODO SubClassOf( ObjectSomeValuesFrom( OPE owl:Thing ) CE )
			System.out.println("Object Property Domain Axiom"+ axiom.getProperty().toString() +"---domain---"
					+ axiom.getDomain().toString());
			//ObjectPropertyDomain := 'ObjectPropertyDomain' '(' ObjectPropertyExpression ClassExpression ')'
		}

		@Override
		public void visit(OWLEquivalentObjectPropertiesAxiom arg0) {
			v_axioms.addAll(arg0.asSubObjectPropertyOfAxioms());
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
			//TODO SubClassOf( owl:Thing ObjectAllValuesFrom( OPE CE ) )
			//throw new IllegalArgumentException(	"Not an OWL 2 EL axiom ! "+axiom.toString()+" Object Property Range Axiom !");
			System.out.println("Object Property Range Axiom"+ axiom.getProperty().toString() +"---range---"
					+ axiom.getRange().toString());
		}

		@Override
		public void visit(OWLObjectPropertyAssertionAxiom axiom) {
			n_axioms.add(axiom);
			Set<OWLIndividual> indi = new HashSet<>();
			axiom.individualsInSignature().forEach(x -> indi.add(x));
			setFacts.add(Expressions.makeAtom(subExEDB,
					Expressions.makeConstant(axiom.getProperty().toString()),
					Expressions.makeConstant(indi.toArray()[0].toString()),					
					Expressions.makeConstant(indi.toArray()[1].toString())
					));
			setFacts.add(Expressions.makeAtom(nomEDB, Expressions.makeConstant(indi.toArray()[0].toString())));
			setFacts.add(Expressions.makeAtom(nomEDB, Expressions.makeConstant(indi.toArray()[1].toString())));
			setFacts.add(Expressions.makeAtom(rolEDB, Expressions.makeConstant(axiom.getProperty().toString())));
		}

		@Override
		public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
			throw new IllegalArgumentException("Functional Object Property Axiom Exception !" + axiom.toString());
		}

		@Override
		public void visit(OWLSubObjectPropertyOfAxiom axiom) {
			//TODO only subRole(R,T) done
			n_axioms.add(axiom);
			setFacts.add(Expressions.makeAtom(subRoleEDB, 
					Expressions.makeConstant(axiom.getSubProperty().toString()),
					Expressions.makeConstant(axiom.getSuperProperty().toString())
					));
			setFacts.add(Expressions.makeAtom(rolEDB, Expressions.makeConstant(axiom.getSubProperty().toString())));
			setFacts.add(Expressions.makeAtom(rolEDB, Expressions.makeConstant(axiom.getSuperProperty().toString())));
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
					setFacts.add(Expressions.makeAtom(subClassEDB, 
							Expressions.makeConstant(axiom.getIndividual().toString()),
							Expressions.makeConstant(axiom.getClassExpression().toString())
							));
					setFacts.add(Expressions.makeAtom(nomEDB, Expressions.makeConstant(axiom.getIndividual().toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(axiom.getClassExpression().toString())));
				}
			} else {
				//C(a)
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), 
						axiom.getClassExpression()));
				n_axioms.add(v_factory.getOWLClassAssertionAxiom(addFreshClassName(freshConceptNumber), 
						axiom.getIndividual()));
				setFacts.add(Expressions.makeAtom(subClassEDB, 
						Expressions.makeConstant(axiom.getIndividual().toString()),
						Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())
						));
				setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
				setFacts.add(Expressions.makeAtom(nomEDB, Expressions.makeConstant(axiom.getIndividual().toString())));
				freshConceptNumber++;					
			}		
		}

		@Override
		public void visit(OWLEquivalentClassesAxiom axiom) {
			//C equivalent to D
			v_axioms.addAll(axiom.asOWLSubClassOfAxioms());
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
			//TODO
			throw new IllegalArgumentException("Same Individual Axiom Exception !");
		}

		@Override
		public void visit(OWLSubPropertyChainOfAxiom axiom) {
			throw new IllegalArgumentException("Sub Property Chain Of Axiom" + axiom.toString());
		}

		@Override
		public void visit(OWLInverseObjectPropertiesAxiom axiom) {
			throw new IllegalArgumentException("Inverse Object Property Exception !" + axiom.toString());
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

		@Override
		public void visit(OWLObjectIntersectionOf ce) {
			//Done in normalizesubClassExpressionAxiom method above
		}

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
						v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber),ce.getFiller()));
					} 
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber))));
					setFacts.add(Expressions.makeAtom(supExEDB,
							Expressions.makeConstant(getCurrentClassExpression().toString()),
							Expressions.makeConstant(ce.getProperty().toString()),
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant("aux"+auxnum)							
							));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant("aux"+auxnum)));
					setFacts.add(Expressions.makeAtom(rolEDB, Expressions.makeConstant(ce.getProperty().toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(getCurrentClassExpression().toString())));
		
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
						setFacts.add(Expressions.makeAtom(supExEDB,
								Expressions.makeConstant(getCurrentClassExpression().toString()),
								Expressions.makeConstant(ce.getProperty().toString()),
								Expressions.makeConstant(ce.getFiller().toString()),
								Expressions.makeConstant("aux"+auxnum)							
								));
						setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(ce.getFiller().toString())));
						setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant("aux"+auxnum)));
						setFacts.add(Expressions.makeAtom(rolEDB, Expressions.makeConstant(ce.getProperty().toString())));
						setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(getCurrentClassExpression().toString())));
						auxnum++;
					}
				}
			} else if (boolSub) {
				if(!ce.getFiller().isClassExpressionLiteral()) {
					// Exists.R.C subsumes A
					//C subsumes X
					if (ce.getFiller() instanceof OWLObjectIntersectionOf)
						v_axioms.add(v_factory.getOWLSubClassOfAxiom(ce.getFiller(), addFreshClassName(freshConceptNumber)));
					//Exists.R.X subsumes A
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber)), 
							getCurrentClassExpression()));
					//subEx(R,X,A) where X-> new concept
					setFacts.add(Expressions.makeAtom(subExEDB, 
							Expressions.makeConstant(ce.getProperty().toString()),
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant(getCurrentClassExpression().toString())
							));
					//role
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(getCurrentClassExpression().toString())));
					setFacts.add(Expressions.makeAtom(rolEDB, Expressions.makeConstant(ce.getProperty().toString())));	
					setCurrentClassExpression(addFreshClassName(freshConceptNumber));
					freshConceptNumber++;
				} else {
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()));
					//Exists.R.B subsumes A
					//subEx(R,B,A)
					setFacts.add(Expressions.makeAtom(subExEDB, Expressions.makeConstant(ce.getProperty().toString()), 
							Expressions.makeConstant(ce.getFiller().toString()),
							Expressions.makeConstant(getCurrentClassExpression().toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(ce.getFiller().toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(getCurrentClassExpression().toString())));
					setFacts.add(Expressions.makeAtom(rolEDB, Expressions.makeConstant(ce.getProperty().toString())));
				}
			}
		}

		@Override
		public void visit(OWLObjectAllValuesFrom ce) {
			throw new IllegalStateException("OWLObjectAllValuesFrom " + ce.toString());
		}

		@Override
		public void visit(OWLObjectHasValue ce) {
			//TODO cross check. Will casting work? ObjectSomeValuesFrom( OPE ObjectOneOf( a ) )
			//or directly put it in setFacts
			v_axioms.add(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), (OWLClassExpression) ce.getFiller()), 
					getCurrentClassExpression()
					));
		}

		@Override
		public void visit(OWLObjectMinCardinality ce) {
			//TODO ObjectMinCardinality( 1 OPE CE ). OWLObjectSomeValuesFrom
			ce.getCardinality();
			throw new IllegalStateException("OWLObjectMinCardinality" + ce.toString());
		}

		@Override
		public void visit(OWLObjectExactCardinality ce) {
			ce.getCardinality();
			throw new IllegalStateException("OWLObjectExactCardinality " + ce.toString());
		}

		@Override
		public void visit(OWLObjectMaxCardinality ce) {
			ce.getCardinality();
			throw new IllegalStateException("OWLObjectMaxCardinality " + ce.toString());
		}

		@Override
		public void visit(OWLObjectHasSelf ce) {
			if(boolSuper) {
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), ce));
				setFacts.add(Expressions.makeAtom(supSelfEDB,
						Expressions.makeConstant(getCurrentClassExpression().toString()),
						Expressions.makeConstant(ce.getProperty().toString())						
						));

				setFacts.add(Expressions.makeAtom(rolEDB, 
						Expressions.makeConstant(ce.getProperty().toString())
						));
				setFacts.add(Expressions.makeAtom(clsEDB, 
						Expressions.makeConstant(getCurrentClassExpression().toString())
						));
			} else if (boolSub) {
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()));
				setFacts.add(Expressions.makeAtom(subSelfEDB,
						Expressions.makeConstant(ce.getProperty().toString()),
						Expressions.makeConstant(getCurrentClassExpression().toString())						
						));
				setFacts.add(Expressions.makeAtom(rolEDB, 
						Expressions.makeConstant(ce.getProperty().toString())
						));
				setFacts.add(Expressions.makeAtom(clsEDB, 
						Expressions.makeConstant(getCurrentClassExpression().toString())
						));
			}
		}

		@Override
		public void visit(OWLObjectOneOf ce) {
			//TODO 
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
