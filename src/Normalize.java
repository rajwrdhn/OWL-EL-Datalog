import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.management.Descriptor;

import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitorEx;
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
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
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
    protected final Set<OWLClassExpression> v_classExpression;
    protected final Optional<IRI> v_IRI;
    protected long freshConceptNumber;
    protected long auxnum;
    //protected final ClassExpressionNormalize v_ClassExpressionNormalize;
    //protected final ClassExpressionNormalizer v_ClassExpressionNormalizer;
	//protected final Set<String> inputStringTranslation;
	protected final Set<Atom> setFacts;
	protected final Set<Atom> setAtoms;
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
        //inputStringTranslation = new HashSet<>();
        v_classExpression = new HashSet<>();
        //v_ClassExpressionNormalize = new ClassExpressionNormalize();
        //v_ClassExpressionNormalizer = new ClassExpressionNormalizer(null);
        freshConceptNumber = 1;
        auxnum = 1;
        setFacts = new HashSet<>();
        setAtoms = new HashSet<>();
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
		Set<OWLAxiom> new_v_axioms = new HashSet<>();
		new_v_axioms.addAll(axioms);		

		
		for (OWLAxiom axiom : axioms) {
			axiom.accept(axmVisitor);
			//new_v_axioms.remove(axiom);
		}
		for (OWLAxiom axiom : new_v_axioms ) {
			v_axioms.remove(axiom);
		}
		
		if (v_axioms.isEmpty()) {
			System.out.println("Normalisation complete!!");
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

	
	public OWLClassExpression getConjunctClassExpression(OWLClassExpression expr1, OWLClassExpression expr2, OWLClassExpression rem) {
		Set<OWLClassExpression> ex = new HashSet<>() ;
		ex.add(expr1);
		ex.add(expr2);
		ex.remove(rem);		
		return v_factory.getOWLObjectIntersectionOf(ex);
	}

	/**
	 * ClassExpression Visitor
	 * @param ce
	 */
	public void normalizesubClassExpressionAxiom(OWLClassExpression subClassExpr, OWLClassExpression superClassExpr) {
		//clear the set of class expression
		v_classExpression.clear();
		if (subClassExpr.isTopEntity() && superClassExpr.isClassExpressionLiteral()) {
			//Top subsumes A
			setFacts.add(Expressions.makeAtom(topEDB, Expressions.makeConstant(superClassExpr.toString())));				
		} else if (superClassExpr.isBottomEntity() && subClassExpr.isClassExpressionLiteral()) {
			//A subsumes Bot
			setFacts.add(Expressions.makeAtom(botEDB, Expressions.makeConstant(subClassExpr.toString())));
			
		} else if (subClassExpr.isClassExpressionLiteral() && superClassExpr.isClassExpressionLiteral()) {
			if (subClassExpr instanceof OWLObjectComplementOf || superClassExpr instanceof OWLObjectComplementOf) {
				throw new IllegalArgumentException("Complement of a named class is not OWL 2 EL !");
			} else {
				// A subsumes B
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(subClassExpr, superClassExpr));
				//subclass fact A is a subset of B
				setFacts.add(Expressions.makeAtom(subClassEDB, 
						Expressions.makeConstant(subClassExpr.toString()), 
						Expressions.makeConstant(superClassExpr.toString())));
				setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(subClassExpr.toString())));
				setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(superClassExpr.toString())));
			}	
		} else if (subClassExpr.isClassExpressionLiteral() && !superClassExpr.isClassExpressionLiteral()
				&& superClassExpr.asConjunctSet().size() >= 1 ) {
			BoolVisitor boolvisitor = new BoolVisitor();
			boolean subbool = subClassExpr.accept(boolvisitor);
			boolean supbool = superClassExpr.accept(boolvisitor);
			if(!subbool && !supbool) {
				throw new IllegalArgumentException("Not OWL 2 EL !");
			} else {
					// A subsumes ClassExpression 
					ClassExpressionNormalize ceVisitor = new ClassExpressionNormalize(false,true,subClassExpr);
					
					for (OWLClassExpression ce:superClassExpr.asConjunctSet()) {
						if (ce.isClassExpressionLiteral()) {
							if (ce instanceof OWLObjectComplementOf) {
								throw new IllegalArgumentException("Complement Class Name Exception !!");
							} else {
								n_axioms.add(v_factory.getOWLSubClassOfAxiom(subClassExpr, ce));
								setFacts.add(Expressions.makeAtom(subClassEDB, 
										Expressions.makeConstant(subClassExpr.toString()), 
										Expressions.makeConstant(ce.toString())));
								setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(subClassExpr.toString())));
								setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(ce.toString())));								
							}
						} else {
							
							ce.accept(ceVisitor);
						}
				}
			}
		} else if(subClassExpr.isAnonymous() && superClassExpr.isAnonymous()) {
			BoolVisitor boolvisitor = new BoolVisitor();
			boolean subbool = subClassExpr.accept(boolvisitor);
			boolean supbool = superClassExpr.accept(boolvisitor);
			// ClassExpr subsumes ClassExpr
			if (subbool && supbool) {
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(subClassExpr, addFreshClassName(freshConceptNumber)));
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), superClassExpr));
				freshConceptNumber++;
			} else {
				throw new IllegalArgumentException("C subsumes D");
			}
		} else if (superClassExpr.isClassExpressionLiteral() && !subClassExpr.isClassExpressionLiteral()
				&& subClassExpr.asConjunctSet().size() >=1 ) {
			BoolVisitor boolvisitor = new BoolVisitor();
			boolean subbool = subClassExpr.accept(boolvisitor);
			boolean supbool = superClassExpr.accept(boolvisitor);
			
			if(!subbool && !supbool) {
				throw new IllegalArgumentException("Not OWL 2 EL !");
			} else {
				// ClassExpression subsumes A 
				ClassExpressionNormalize ceVisitor = new ClassExpressionNormalize(true,false,superClassExpr);
				//int index = subClassExpr.asConjunctSet().size();
				Set<OWLClassExpression> descriptions = subClassExpr.asConjunctSet(); 
				if (descriptions.size() == 1) {
					subClassExpr.accept(ceVisitor);
				} else if (descriptions.size() == 2) {
					
				} else {
					
				}
	/*			for (OWLClassExpression ce:descriptions) {
					if (ce.isClassExpressionLiteral()) {
						if (ce instanceof OWLObjectComplementOf) {
							throw new IllegalArgumentException("Complement Class Name Exception !!");
						} else {
							n_axioms.add(v_factory.getOWLSubClassOfAxiom(subClassExpr, ce));
							setFacts.add(Expressions.makeAtom(subClassEDB, 
									Expressions.makeConstant(subClassExpr.toString()), 
									Expressions.makeConstant(ce.toString())));
							setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(subClassExpr.toString())));
							setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(ce.toString())));								
						}
					} else {
						
						ce.accept(ceVisitor);
					}
				}*/
			}
		} else {
			throw new IllegalAccessError("Not OWL 2 EL !! sub----" + subClassExpr.toString() + " "
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
		protected final List<OWLClassExpression[]> v_expressionList;
 
		protected long i ;
		protected boolean cNameBool;
		protected boolean[] allClassNames;
		protected boolean allClassExpr;
		protected final Set<OWLClassExpression> axiConj;
		public AxiomVisitor() {
			v_expressionList = new ArrayList<>();
			cNameBool = false;
			allClassExpr = false;
			i=0;
			auxnum =1;
			axiConj = new HashSet<>();
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
			System.out.println("Object Property Domain Axiom"+ axiom.getProperty().toString() +"---domain---"
		+ axiom.getDomain().toString());
			//ObjectPropertyDomain := 'ObjectPropertyDomain' '(' ObjectPropertyExpression ClassExpression ')'
		}

		@Override
		public void visit(OWLEquivalentObjectPropertiesAxiom arg0) {
			System.out.println("Equivalent Object Properties Axiom");
		}

		@Override
		public void visit(OWLNegativeDataPropertyAssertionAxiom arg0) {
			throw new IllegalAccessError("Negative Data Property Assertion Axiom Exception !");
			
		}

		@Override
		public void visit(OWLDifferentIndividualsAxiom arg0) {
			throw new IllegalArgumentException(
					"Not an OWL 2 EL axiom ! "+arg0.toString()+" Different Individuals Axiom !");
		}

		@Override
		public void visit(OWLDisjointDataPropertiesAxiom arg0) {
			throw new IllegalArgumentException(
					"Not an OWL 2 EL axiom ! "+arg0.toString()+" Disjoint data Property Axiom !");
		}

		@Override
		public void visit(OWLDisjointObjectPropertiesAxiom arg0) {
			throw new IllegalArgumentException(
					"Not an OWL 2 EL axiom ! "+arg0.toString()+" Disjoint Object Property Axiom !");
		}

		@Override
		public void visit(OWLObjectPropertyRangeAxiom arg0) {
			
			//throw new IllegalArgumentException(
			//		"Not an OWL 2 EL axiom ! "+arg0.toString()+" Object Property Range Axiom !");
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
		public void visit(OWLFunctionalObjectPropertyAxiom arg0) {
			throw new IllegalAccessError("Functional Object Property Axiom Exception !");
		}

		@Override
		public void visit(OWLSubObjectPropertyOfAxiom axiom) {
			//subRole()
			axiom.getSubProperty();
			axiom.getSuperProperty();
			n_axioms.add(axiom);
			//setFacts.add(Expressions.makeAtom(subRoleEDB, terms));
			// TODO Auto-generated method stub
			System.out.println("Sub Object Property Of Axiom"+"----------sub----"+axiom.getSubProperty().toString()+
					"--------super-------"+axiom.getSuperProperty().toString());
		}

		@Override
		public void visit(OWLDisjointUnionAxiom arg0) {
			throw new IllegalAccessError("Disjoint Union Axiom Exception !");
		}

		@Override
		public void visit(OWLSymmetricObjectPropertyAxiom arg0) {
			// TODO Auto-generated method stub
			System.out.println("Symmetric Object Property Axiom");
		}

		@Override
		public void visit(OWLDataPropertyRangeAxiom arg0) {
			throw new IllegalAccessError("Data Property Range Axiom Exception !");
			
		}

		@Override
		public void visit(OWLFunctionalDataPropertyAxiom arg0) {
			throw new IllegalAccessError("Functional Data Property Axiom Exception !");
		}

		@Override
		public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
			throw new IllegalAccessError("Equivalent Data Properties Axiom Exception !");
		}
		@Override
		public void visit(OWLClassAssertionAxiom axiom) {
			
			if (axiom.getClassExpression().isClassExpressionLiteral() && 
					axiom.getClassExpression().asConjunctSet().size()==1) {
				n_axioms.add(v_factory.getOWLClassAssertionAxiom(axiom.getClassExpression(), axiom.getIndividual()));
				setFacts.add(Expressions.makeAtom(subClassEDB, 
						Expressions.makeConstant(axiom.getIndividual().toString()),
						Expressions.makeConstant(axiom.getClassExpression().toString())
						));
				setFacts.add(Expressions.makeAtom(nomEDB, Expressions.makeConstant(axiom.getIndividual().toString())));
				setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(axiom.getClassExpression().toString())));
				
			} else {
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
		public void visit(OWLDataPropertyAssertionAxiom arg0) {
			throw new IllegalAccessError("Data Property Assertion Axiom Exception !");
		}

		@Override
		public void visit(OWLTransitiveObjectPropertyAxiom arg0) {
			throw new IllegalAccessError("Transitive Object Property Axiom Exception !");
		}

		@Override
		public void visit(OWLIrreflexiveObjectPropertyAxiom arg0) {
			throw new IllegalAccessError("Irreflexive Object Property Axiom Exception !");
		}

		@Override
		public void visit(OWLSubDataPropertyOfAxiom arg0) {
			throw new IllegalAccessError("Sub Data Property Of Axiom Exception !");
		}

		@Override
		public void visit(OWLInverseFunctionalObjectPropertyAxiom arg0) {
			throw new IllegalAccessError("Inverse Functional Object Property Axiom Exception !");
			
		}

		@Override
		public void visit(OWLSameIndividualAxiom arg0) {
			throw new IllegalAccessError("Same Individual Axiom Exception !");
		}

		@Override
		public void visit(OWLSubPropertyChainOfAxiom arg0) {
			System.out.println("Sub Property Chain Of Axiom");
		}

		@Override
		public void visit(OWLInverseObjectPropertiesAxiom arg0) {
			try {
				throw new IllegalAccessException("Inverse Object Properties Axiom Exception !");
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void visit(OWLHasKeyAxiom arg0) {
			throw new IllegalAccessError("Has Key Axiom Exception !");
		}

		@Override
		public void visit(OWLDatatypeDefinitionAxiom arg0) {
			throw new IllegalAccessError("Data Type Definition Axiom Exception !");
		}

		@Override
		public void visit(SWRLRule arg0) {
			throw new IllegalAccessError("SWRL rule Exception !");
			
		}
	}
	
	
	protected class ClassExpressionNormalize implements OWLClassExpressionVisitor{
		
		protected final boolean boolSub;
		protected final boolean boolSuper;
		protected final OWLClassExpression owlClassName;
		public ClassExpressionNormalize(boolean sub, boolean sup, OWLClassExpression ceName) {
			// boolSub is true when it is not a named class else false. similar for boolSuper.
			boolSub = sub;
			boolSuper = sup;
			owlClassName = ceName;
		}
		
		public void visit(OWLClass ce) {
			ce.isClassExpressionLiteral();			
		}
			
		@Override
		public void visit(OWLObjectIntersectionOf ce) {
			if (boolSub) {
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(ce, addFreshClassName(freshConceptNumber)));
				freshConceptNumber++;
			} else if (boolSuper) {
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(owlClassName, ce));
			}
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
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), 
							ce.getFiller()));
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(owlClassName, 
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber))));
					setFacts.add(Expressions.makeAtom(supExEDB,
							Expressions.makeConstant(owlClassName.toString()),
							Expressions.makeConstant(ce.getProperty().toString()),
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant("aux"+auxnum)							
							));
					auxnum++;
					freshConceptNumber++;
				} else {
					//A subsumes Exists.R. B
					if (ce.getFiller() instanceof OWLObjectComplementOf) {
						throw new IllegalArgumentException("OWLObjectComplementOf not OWL 2 EL ! " + ce.toString()  );
					}else {
						n_axioms.add(v_factory.getOWLSubClassOfAxiom(owlClassName, 
								v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber))));
						setFacts.add(Expressions.makeAtom(supExEDB,
								Expressions.makeConstant(owlClassName.toString()),
								Expressions.makeConstant(ce.getProperty().toString()),
								Expressions.makeConstant(ce.getFiller().toString()),
								Expressions.makeConstant("aux"+auxnum)							
								));
						auxnum++;
					}
				}
			} else if (boolSub) {
				if(!ce.getFiller().isClassExpressionLiteral()) {
					// Exists.R.C subsumes A
					//C subsumes X
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(ce.getFiller(), addFreshClassName(freshConceptNumber)));
					//Exists.R.X subsumes A
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber)), 
							owlClassName));
					//subEx(R,X,A) where X-> new concept
					setFacts.add(Expressions.makeAtom(subExEDB, 
							Expressions.makeConstant(ce.getProperty().toString()),
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant(owlClassName.toString())
							));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(owlClassName.toString())));				
					freshConceptNumber++;
				} else {
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(ce, owlClassName));
					//Exists.R.B subsumes A
					//subEx(R,B,A)
					setFacts.add(Expressions.makeAtom(subExEDB, Expressions.makeConstant(ce.getProperty().toString()), 
							Expressions.makeConstant(ce.getFiller().toString()),
							Expressions.makeConstant(owlClassName.toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(ce.getFiller().toString())));
					setFacts.add(Expressions.makeAtom(clsEDB, Expressions.makeConstant(owlClassName.toString())));
				}
			}
		}

		@Override
		public void visit(OWLObjectAllValuesFrom ce) {
			throw new IllegalArgumentException("All Values From not in OWL 2 EL ! " + ce.toString());
		}

		@Override
		public void visit(OWLObjectHasValue ce) {
			//TODO
			visit((OWLObjectSomeValuesFrom)ce.asSomeValuesFrom());
		}

		@Override
		public void visit(OWLObjectMinCardinality ce) {
			// TODO Auto-generated method stub
			OWLClassExpressionVisitor.super.visit(ce);
		}

		@Override
		public void visit(OWLObjectExactCardinality ce) {
			// TODO Auto-generated method stub
			OWLClassExpressionVisitor.super.visit(ce);
		}

		@Override
		public void visit(OWLObjectMaxCardinality ce) {
			// TODO Auto-generated method stub
			OWLClassExpressionVisitor.super.visit(ce);
		}

		@Override
		public void visit(OWLObjectHasSelf ce) {
			if(boolSuper) {
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(owlClassName, ce));
				setFacts.add(Expressions.makeAtom(supSelfEDB,
						Expressions.makeConstant(owlClassName.toString()),
						Expressions.makeConstant(ce.getProperty().toString())						
						));
			} else if (boolSub) {
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(ce, owlClassName));
				setFacts.add(Expressions.makeAtom(supSelfEDB,
						Expressions.makeConstant(ce.getProperty().toString()),
						Expressions.makeConstant(owlClassName.toString())						
						));
			}
		}

		@Override
		public void visit(OWLObjectOneOf ce) {
			// TODO Auto-generated method stub
			OWLClassExpressionVisitor.super.visit(ce);
		}

		@Override
		public void visit(OWLDataSomeValuesFrom ce) {
			// TODO Auto-generated method stub
			OWLClassExpressionVisitor.super.visit(ce);
		}

		@Override
		public void visit(OWLDataAllValuesFrom ce) {
			// TODO Auto-generated method stub
			OWLClassExpressionVisitor.super.visit(ce);
		}

		@Override
		public void visit(OWLDataHasValue ce) {
			// TODO Auto-generated method stub
			OWLClassExpressionVisitor.super.visit(ce);
		}

		@Override
		public void visit(OWLDataMinCardinality ce) {
			// TODO Auto-generated method stub
			OWLClassExpressionVisitor.super.visit(ce);
		}

		@Override
		public void visit(OWLDataExactCardinality ce) {
			// TODO Auto-generated method stub
			OWLClassExpressionVisitor.super.visit(ce);
		}

		@Override
		public void visit(OWLDataMaxCardinality ce) {
			// TODO Auto-generated method stub
			OWLClassExpressionVisitor.super.visit(ce);
		}


	}
    
	protected class BoolVisitor implements OWLClassExpressionVisitorEx<Boolean> {

        public Boolean visit(OWLClass ce_bool) {
            if (ce_bool.isOWLThing())
                return Boolean.FALSE;
            else if (ce_bool.isOWLNothing())
                return Boolean.FALSE;
            else
                return Boolean.TRUE;
        }
        public Boolean visit(OWLObjectIntersectionOf ce_bool) {
            return Boolean.TRUE;
        }
        public Boolean visit(OWLObjectUnionOf ce_bool) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLObjectComplementOf ce_bool) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLObjectOneOf ce_bool) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLObjectSomeValuesFrom ce_bool) {
            return Boolean.TRUE;
        }
        public Boolean visit(OWLObjectAllValuesFrom ce_bool) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLObjectHasValue ce_bool) {
            return Boolean.TRUE;
        }
        public Boolean visit(OWLObjectHasSelf ce_bool) {
            return Boolean.TRUE;
        }
        public Boolean visit(OWLObjectMinCardinality ce_bool) {
            return ce_bool.getCardinality()>0;
        }
        public Boolean visit(OWLObjectMaxCardinality ce_bool) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLObjectExactCardinality ce_bool) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLDataSomeValuesFrom desc) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLDataAllValuesFrom desc) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLDataHasValue desc) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLDataMinCardinality desc) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLDataMaxCardinality desc) {
            return Boolean.FALSE;
        }
        public Boolean visit(OWLDataExactCardinality desc) {
            return Boolean.FALSE;
        }
    }
}
