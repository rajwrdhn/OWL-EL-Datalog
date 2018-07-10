import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.text.StyledEditorKit.ForegroundAction;

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
	protected final Set<String> inputStringTranslation;
	protected final Set<Atom> setFacts;
	protected final Set<Atom> setAtoms;
	protected final List<Rule> listRules;
	protected final Predicate nom;
	protected final Predicate cls;
	protected final Predicate triple;
	protected final Predicate subClass;
	protected final Predicate inst;
	protected final Predicate top;
	protected final Predicate bot;
	protected final Predicate subConj;
	protected final Predicate subEx;
	protected final Predicate supEx;
	protected final Predicate subSelf;
	protected final Predicate supSelf;
	protected final Predicate self;
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
        inputStringTranslation = new HashSet<>();
        v_classExpression = new HashSet<>();
        setFacts = new HashSet<>();
        setAtoms = new HashSet<>();
        listRules = new ArrayList<>();
        nom = Expressions.makePredicate("nom",1);
        cls = Expressions.makePredicate("cls",1);
        triple = Expressions.makePredicate("triple", 3);
        subClass = Expressions.makePredicate("subclass", 2);
        inst = Expressions.makePredicate("inst", 2);
        top = Expressions.makePredicate("top", 1);
        bot = Expressions.makePredicate("bot", 1);
        subConj = Expressions.makePredicate("subconj", 3);
        subEx = Expressions.makePredicate("subex", 3);
        supEx = Expressions.makePredicate("supex", 4);
        subSelf = Expressions.makePredicate("subself", 2);
        supSelf = Expressions.makePredicate("supself", 2);
        self = Expressions.makePredicate("self", 2);
        v = Expressions.makeVariable("v");
        w = Expressions.makeVariable("w");
        x = Expressions.makeVariable("x");
        y = Expressions.makeVariable("y");
        z = Expressions.makeVariable("z");
	}
	
    /**
	 * Call axiomVisitor Class , Normalize and return new set of normalized axioms.
	 */
	public Set<OWLAxiom> getFromOntology(OWLOntology onto) throws OWLOntologyCreationException {
		onto.axioms().forEach(x -> v_axioms.add(x));
		//v_axioms.addAll((Collection<? extends OWLAxiom>) onto.axioms());
		if(v_axioms.isEmpty()) {
			System.out.println("No Axioms in the Ontology!!");
		} else {
			visitAxioms(v_axioms);
		}	
		return n_axioms;
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
	/**
	 * gets the property "R" from "Exists.R.C"
	 */
	public OWLObjectPropertyExpression getPropertyfromClassExpression(OWLObjectSomeValuesFrom expr) {		
		return expr.getProperty();
	}
	/**
	 * Return the Class from Existentially quantified Class Expression
	 */
	public OWLClassExpression getClassFromObjectSomeValuesFrom(OWLObjectSomeValuesFrom expr) {
		
		return expr.getFiller();
	}
	/**
	 * Return the Class from Self Restriction Class Expression
	 */
	public OWLClassExpression getClassFromObjectSomeValuesFrom(OWLObjectHasSelf expr) {
		return expr;
	}
	
	public OWLClassExpression getConjunctClassExpression(OWLClassExpression expr1, OWLClassExpression expr2, OWLClassExpression rem) {
		Set<OWLClassExpression> ex = new HashSet<>() ;
		ex.add(expr1);
		ex.add(expr2);
		ex.remove(rem);		
		return v_factory.getOWLObjectIntersectionOf(ex);
	}
/*	public void normalizeExpression(OWLClassExpression classExpr) {
		ClassExpressionNormalizer clsExprNormalizer = new ClassExpressionNormalizer();
		//Normalize the Class Expressions 
		classExpr.accept(clsExprNormalizer);
	}*/
	
	public void checkAllClassExpression(OWLClassExpression ce) {
		Iterator<OWLClassExpression> iter = ce.asConjunctSet().iterator();
		while(iter.hasNext()) {
			if(iter.next().isClassExpressionLiteral()) {
				
			}
		}
	}
	
	/**
	 * input translation as string of subsumption axioms
	 * @return
	 */
	public Set<String> getinputTranslationAxioms() {
		return inputStringTranslation;
	}
	/**
	 * class Expression Visitor
	 * @param ce
	 */
	public void normalizeClassExpression(OWLClassExpression ce) {
		//clear the set of class expression
		v_classExpression.clear();
		
		ClassExpressionNormalizer ceVisitor = new ClassExpressionNormalizer();
		ce.accept(ceVisitor);
		
	}
	public void rules() {
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
	}
	
	/*
	 * Visitor Class for Axioms in the Ontology.
	 */
	protected class AxiomVisitor implements OWLAxiomVisitor {
		
        protected long freshConceptNumber;
        protected long auxnum;
		protected long i ;
		protected boolean cNameBool;
		protected boolean[] allClassNames;
		protected boolean allClassExpr;
		protected final Set<OWLClassExpression> axiConj;
		public AxiomVisitor() {
			freshConceptNumber =1;
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

			if(axiom.getSubClass().isClassExpressionLiteral() && axiom.getSuperClass().isClassExpressionLiteral()) {
				// A subsumes B
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(), axiom.getSuperClass()));
				//subclass fact A is a subset of B
				setFacts.add(Expressions.makeAtom(subClass, 
						Expressions.makeConstant(axiom.getSubClass().toString()), 
						Expressions.makeConstant(axiom.getSuperClass().toString())));
				setFacts.add(Expressions.makeAtom(inst, 
						Expressions.makeConstant(axiom.getSubClass().toString()), 
						Expressions.makeConstant(axiom.getSuperClass().toString())));
			} else if (axiom.getSuperClass().isClassExpressionLiteral() && 
					axiom.getSubClass() instanceof OWLObjectSomeValuesFrom && 
					!axiom.getSubClass().isAnonymous() && 
					!axiom.getSubClass().isClassExpressionLiteral() && 
					axiom.getSubClass().asConjunctSet().size()==1) {
				//Exists.R.C subsumes A
				if (getClassFromObjectSomeValuesFrom( (OWLObjectSomeValuesFrom)axiom.getSubClass()).isClassExpressionLiteral()) {
					n_axioms.add(axiom);
					//Exists.R.B subsumes A
					//subEx(R,B,A)
					setFacts.add(Expressions.makeAtom(subEx, Expressions.makeConstant(((OWLObjectSomeValuesFrom) axiom.getSubClass()).getProperty().toString()), 
							Expressions.makeConstant(((OWLObjectSomeValuesFrom)axiom.getSubClass()).getFiller().toString()),
							Expressions.makeConstant(axiom.getSuperClass().toString())));
					setFacts.add(Expressions.makeAtom(triple,  
							Expressions.makeConstant(((OWLObjectSomeValuesFrom)axiom.getSubClass()).getFiller().toString()),
							Expressions.makeConstant(((OWLObjectSomeValuesFrom) axiom.getSubClass()).getProperty().toString()),
							Expressions.makeConstant(axiom.getSuperClass().toString())));
				} else {
					// Exists.R.C subsumes A
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(getClassFromObjectSomeValuesFrom(
							(OWLObjectSomeValuesFrom)axiom.getSubClass()), 
							addFreshClassName(freshConceptNumber)));
					//Exists.R.X subsumes A
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(
							(OWLObjectSomeValuesFrom)addFreshClassName(freshConceptNumber), 
							axiom.getSuperClass()));
					//subEx(R,X,A) where X-> new concept
					setFacts.add(Expressions.makeAtom(subEx, 
							Expressions.makeConstant(((OWLObjectSomeValuesFrom)axiom.getSubClass()).getProperty().toString()),
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant(axiom.getSuperClass().toString())
							));
					freshConceptNumber++;
					
				}
			
			} else if (axiom.getSuperClass() instanceof OWLObjectSomeValuesFrom && axiom.getSubClass().isClassExpressionLiteral()) {
				// A subsumes Exists.R.C
				if (getClassFromObjectSomeValuesFrom( (OWLObjectSomeValuesFrom)axiom.getSuperClass()).isClassExpressionLiteral() ) {
					n_axioms.add(axiom);
					inputStringTranslation.add("{subEx("+axiom.getSubClass()+
							((OWLObjectSomeValuesFrom) axiom.getSuperClass()).getProperty()+","+
							((OWLObjectSomeValuesFrom)axiom.getSuperClass()).getFiller() +","+"aux"+auxnum+")}");
					setFacts.add(Expressions.makeAtom(supEx, 
							Expressions.makeConstant(axiom.getSubClass().toString()),
							Expressions.makeConstant(((OWLObjectSomeValuesFrom)axiom.getSuperClass()).getProperty().toString()),
							Expressions.makeConstant(((OWLObjectSomeValuesFrom)axiom.getSuperClass()).getFiller().toString()),
							Expressions.makeConstant("aux"+auxnum)
							));
					auxnum++;
					
				} else {
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), 
							getClassFromObjectSomeValuesFrom((OWLObjectSomeValuesFrom)axiom.getSuperClass())));
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(), 
							addSomeValuesFromToFreshClassName((OWLObjectSomeValuesFrom) axiom.getSuperClass(), 
									freshConceptNumber)));
					inputStringTranslation.add("{subEx("+axiom.getSubClass()+","+
									getPropertyfromClassExpression((OWLObjectSomeValuesFrom)axiom.getSuperClass())+","
							+addFreshClassName(freshConceptNumber)+","+"aux"+auxnum+")}");
					setFacts.add(Expressions.makeAtom(supEx,
							Expressions.makeConstant(axiom.getSubClass().toString()),
							Expressions.makeConstant(getPropertyfromClassExpression((OWLObjectSomeValuesFrom)axiom.getSuperClass()).toString()),
							Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
							Expressions.makeConstant("aux"+auxnum)							
							));
					auxnum++;
					freshConceptNumber++;
					
				}
				
			} else if (axiom.getSubClass().isAnonymous() && axiom.getSuperClass().isAnonymous()) {
				// C subsumes D
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(), addFreshClassName(freshConceptNumber)));
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), axiom.getSuperClass()));
				freshConceptNumber++;
				
			} else if (axiom.getSubClass().isTopEntity()) {
				//Empty Set 
				//Top subsumes C
				setFacts.add(Expressions.makeAtom(top, Expressions.makeConstant(axiom.getSuperClass().toString())));
				inputStringTranslation.add("{top("+axiom.getSuperClass()+")}");
				
			} else if (axiom.getSuperClass().isBottomEntity()) {
				//Empty Set
				//Bottom subsumes C
				setFacts.add(Expressions.makeAtom(bot, Expressions.makeConstant(axiom.getSubClass().toString())));
				inputStringTranslation.add("{bot("+axiom.getSubClass()+")}");
				
			} else if (axiom.getSubClass().isClassExpressionLiteral() && !axiom.getSuperClass().isClassExpressionLiteral() 
					&& axiom.getSuperClass().isAnonymous() && axiom.getSuperClass().asConjunctSet().size()> 1) {
				//A subsumes C and D 
				Iterator<OWLClassExpression> itr = axiom.getSuperClass().asConjunctSet().iterator();				
				while(itr.hasNext()) {
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(), itr.next()));
				}
				
			} else if (axiom.getSuperClass().isClassExpressionLiteral() && !axiom.getSubClass().isClassExpressionLiteral() 
					&& axiom.getSubClass().isAnonymous() && axiom.getSubClass().asConjunctSet().size()>1) {
				//C and D subsumes B 
				Iterator<OWLClassExpression> itr = axiom.getSubClass().asConjunctSet().iterator();
				int i = axiom.getSubClass().asConjunctSet().size();
				//Set<OWLAxiom> tmpaxm = new HashSet<>();
				if (i==2) {
					while (itr.hasNext()) {
						if (itr.next().isClassExpressionLiteral()) {
							allClassNames[i-1] = true;
							axiConj.add(itr.next());
							i--;
						}
						else {
							allClassNames[i-1] = false;
							i--;
						}
					}
				}
				if (allClassNames[0]==allClassNames[1]==true) {
					//A and B subsumes X
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(), axiom.getSuperClass()));
					setFacts.add(Expressions.makeAtom(subConj, 
							Expressions.makeConstant(axiConj.toArray()[0].toString()),
							Expressions.makeConstant(axiConj.toArray()[1].toString()),
							Expressions.makeConstant(axiom.getSuperClass().toString())
							));					
				} else {
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(itr.next(), addFreshClassName(freshConceptNumber)));
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(getConjunctClassExpression(axiom.getSubClass(), 
							addFreshClassName(freshConceptNumber), itr.next()), axiom.getSuperClass()));
					freshConceptNumber++;
					
				}
			} else {
				
				System.out.println("This is left!! subclass ----------" + axiom.getSubClass().toString() +
						"          super-------"+axiom.getSuperClass().toString());
				if (axiom.getSubClass().isAnonymous() && axiom.getSuperClass().isClassExpressionLiteral() 
						&& axiom.getSubClass().asConjunctSet().size() ==1) {
					normalizeClassExpression(axiom.getSubClass());
				} else if (axiom.getSuperClass().isAnonymous() && axiom.getSubClass().isClassExpressionLiteral() 
						&& axiom.getSuperClass().asConjunctSet().size() ==1) {
					normalizeClassExpression(axiom.getSuperClass());
				} else {
					throw new IllegalAccessError("Not a OWL-EL Axiom!!");
				}
			}
		}
		@Override
		public void visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
			//throw new IllegalAccessError("Annotation Negative Object Property Assertion Axiom Exception !");
			throw new IllegalArgumentException("The axiom "+arg0+" contains Negative Object property, "
					+ "which is not allowed in OWL 2 EL. ");
			//A negative object property assertion NegativeObjectPropertyAssertion( OPE a1 a2 ) 
			//states that the individual a1 is not connected by the object property expression OPE to the individual a2.
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
		public void visit(OWLObjectPropertyDomainAxiom arg0) {
			System.out.println("Object Property Domain Axiom"+ arg0);
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
			throw new IllegalArgumentException(
					"Not an OWL 2 EL axiom ! "+arg0.toString()+" Object Property Range Axiom !");
		}

		@Override
		public void visit(OWLObjectPropertyAssertionAxiom axiom) {
			n_axioms.add(axiom);
			Set<OWLIndividual> indi = new HashSet<>();
			axiom.individualsInSignature().forEach(x -> indi.add(x));
			setFacts.add(Expressions.makeAtom(subEx,
					Expressions.makeConstant(axiom.getProperty().toString()),
					Expressions.makeConstant(indi.toArray()[0].toString()),					
					Expressions.makeConstant(indi.toArray()[1].toString())
					));
		}

		@Override
		public void visit(OWLFunctionalObjectPropertyAxiom arg0) {
			throw new IllegalAccessError("Functional Object Property Axiom Exception !");
		}

		@Override
		public void visit(OWLSubObjectPropertyOfAxiom arg0) {
			// TODO Auto-generated method stub
			System.out.println("Sub Object Property Of Axiom");
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
			
			if (axiom.getClassExpression().isClassExpressionLiteral()) {
				n_axioms.add(v_factory.getOWLClassAssertionAxiom(axiom.getClassExpression(), axiom.getIndividual()));
				setFacts.add(Expressions.makeAtom(subClass, 
						Expressions.makeConstant(axiom.getIndividual().toString()),
						Expressions.makeConstant(axiom.getClassExpression().toString())
						));
				inputStringTranslation.add("{subClass("+axiom.getIndividual()+","+axiom.getClassExpression()+")}");
				
			} else {
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), 
						axiom.getClassExpression()));
				n_axioms.add(v_factory.getOWLClassAssertionAxiom(addFreshClassName(freshConceptNumber), 
						axiom.getIndividual()));
				setFacts.add(Expressions.makeAtom(subClass, 
						Expressions.makeConstant(axiom.getIndividual().toString()),
						Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())
						));
				inputStringTranslation.add("{subClass("+axiom.getIndividual()+","+addFreshClassName(freshConceptNumber)+")}");
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
	protected class ClassExpressionNormalizer implements OWLClassExpressionVisitor {
		
		public ClassExpressionNormalizer() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void visit(OWLObjectIntersectionOf ce) {
			
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ "  object intersection of !  ");
		}

		@Override
		public void visit(OWLObjectUnionOf ce) {
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ "  object union of !  ");	
		}

		@Override
		public void visit(OWLObjectComplementOf ce) {
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ "  object complement of !  ");	
		}

		@Override
		public void visit(OWLObjectSomeValuesFrom ce) {
			// TODO Auto-generated method stub
			System.out.println(ce.toString());
			v_classExpression.add(ce);			
		}

		@Override
		public void visit(OWLObjectAllValuesFrom ce) {
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ " for all values from !");
		}

		@Override
		public void visit(OWLObjectHasValue ce) {
			// TODO Auto-generated method stub
			System.out.println(ce.toString());
			ce.getFiller();
			v_classExpression.add(ce.asSomeValuesFrom());
		}

		@Override
		public void visit(OWLObjectMinCardinality ce) {
			// TODO Auto-generated method stub
			ce.getFiller();
			ce.getCardinality();
			ce.getProperty().asObjectPropertyExpression();
			v_classExpression.add(ce);
		}

		@Override
		public void visit(OWLObjectExactCardinality ce) {
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ "  exact cardinality !  ");				
		}

		@Override
		public void visit(OWLObjectMaxCardinality ce) {			
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ "  max cardinality !  ");	
		}

		@Override
		public void visit(OWLObjectHasSelf ce) {			
			ce.getProperty();
		}

		@Override
		public void visit(OWLObjectOneOf ce) {
			throw new IllegalArgumentException(
					"Not OWL 2 EL Expression !!  "+ce.toString()+" Object one of !");
		}

		@Override
		public void visit(OWLDataSomeValuesFrom ce) {
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ "  data some values from !  ");				
		}

		@Override
		public void visit(OWLDataAllValuesFrom ce) {
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ " data all values from !  ");					
		}

		@Override
		public void visit(OWLDataHasValue ce) {
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ " data has value !  ");				
		}

		@Override
		public void visit(OWLDataMinCardinality ce) {
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ " data min cardinality !  ");				
		}

		@Override
		public void visit(OWLDataExactCardinality ce) {
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ " data exact cardinality !  ");				
		}

		@Override
		public void visit(OWLDataMaxCardinality ce) {
			 throw new IllegalArgumentException
			 ("Not a OWL 2 EL Expression !! "+ ce.toString()+ " data max cardinality !  ");				
		}
		
	}
}
