import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
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
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
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

public class Normalize {
    protected final OWLDataFactory v_factory;
    protected final Set<OWLAxiom> v_axioms;
    protected final Set<OWLAxiom> n_axioms;
    protected final IRI v_IRI;
	protected final Set<String> inputStringTranslation;
	//constructor
    public Normalize(OWLDataFactory factory, OWLOntologyID ontID) {
        v_factory=factory;
        v_IRI = ontID.getOntologyIRI();
        v_axioms= new HashSet<>();
        n_axioms= new HashSet<>();
        inputStringTranslation = new HashSet<>();
	}
	/*
	 * Call axiomVisitor Class , Normalize and return new set of normalized axioms.
	 */
	public Set<OWLAxiom> getFromOntology(OWLOntology onto) throws OWLOntologyCreationException {
		v_axioms.addAll(onto.getAxioms());
		visitAxioms(onto.getLogicalAxioms());
		
		return n_axioms;
	}
	
	/*
	 * Visit all Axioms in the initial Ontology and normalize the axioms
	 */
	public void visitAxioms(Collection<? extends OWLAxiom> axioms) throws OWLOntologyCreationException {
		AxiomVisitor axmVisitor = new AxiomVisitor();
		
		for (OWLAxiom axiom : axioms) {
			axiom.accept(axmVisitor);
		}
	}
	
	/*
	 * Adds the fresh concept name
	 */
	public OWLClassExpression addFreshClassName(long conceptNumber) {		
		return v_factory.getOWLClass(IRI.create( v_IRI + "#FreshConcept" + conceptNumber));
	}
	/*
	 * Adds Fresh Class name to the existentially quantified Concept expression
	 */
	public OWLClassExpression addSomeValuesFromToFreshClassName(OWLObjectSomeValuesFrom expr, long conceptNumber) {
		return v_factory.getOWLObjectSomeValuesFrom(expr.getProperty(), addFreshClassName(conceptNumber));
	}
	/*
	 * Return the Class from Existentially quantified Class Expression
	 */
	public OWLClassExpression getClassFromObjectSomeValuesFrom(OWLObjectSomeValuesFrom expr) {
		
		return expr.getFiller();
	}
	/*
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
		public AxiomVisitor() {
			freshConceptNumber =1;
			cNameBool = false;
			allClassExpr = false;
			i=0;
			auxnum =1;
		}

		@Override
		public void visit(OWLAnnotationAssertionAxiom axiom) {
			//Annotations do not change the logical meaning of the OWL Ontology.
			//False
			System.out.println(axiom.isLogicalAxiom());
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
			//Entity (Not Logical Axiom)
			//False
			System.out.println(arg0.isLogicalAxiom());
		}

		@Override
		public void visit(OWLSubClassOfAxiom axiom) {
			
			if(axiom.getSubClass().isClassExpressionLiteral() && axiom.getSuperClass().isClassExpressionLiteral()) {
				// A subsumes B
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(), axiom.getSuperClass()));
				inputStringTranslation.add("{subClass("+axiom.getSubClass()+","+axiom.getSuperClass()+")}");
			} else if (axiom.getSuperClass().isClassExpressionLiteral() && axiom.getSubClass() instanceof OWLObjectSomeValuesFrom ) {
				//Exists.R.C subsumes A
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(getClassFromObjectSomeValuesFrom((OWLObjectSomeValuesFrom)axiom.getSubClass()), addFreshClassName(freshConceptNumber)));
				n_axioms.add(v_factory.getOWLSubClassOfAxiom((OWLObjectSomeValuesFrom) addFreshClassName(freshConceptNumber), axiom.getSuperClass()));
				inputStringTranslation.add("{subEx("+((OWLObjectSomeValuesFrom) axiom.getSubClass()).getProperty()+","+(OWLObjectSomeValuesFrom) addFreshClassName(freshConceptNumber)+","+axiom.getSuperClass()+")}");
				freshConceptNumber++;
			} else if (axiom.getSuperClass() instanceof OWLObjectSomeValuesFrom && axiom.getSubClass().isClassExpressionLiteral()) {
				// A subsumes Exists.R.C
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(), (OWLObjectSomeValuesFrom) addFreshClassName(freshConceptNumber)));
				inputStringTranslation.add("{subEx("+axiom.getSubClass()+((OWLObjectSomeValuesFrom) axiom.getSubClass()).getProperty()+","+(OWLObjectSomeValuesFrom) addFreshClassName(freshConceptNumber)+","+"aux"+auxnum+")}");
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), getClassFromObjectSomeValuesFrom((OWLObjectSomeValuesFrom)axiom.getSuperClass())));
				auxnum++;
				freshConceptNumber++;
			}/*else if (axiom.getSuperClass().isClassExpressionLiteral() && axiom.getSubClass() instanceof OWLObjectHasSelf ) {
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(getClassFromObjectSomeValuesFrom((OWLObjectHasSelf)axiom.getSubClass()), addFreshClassName(freshConceptNumber)));
				n_axioms.add(v_factory.getOWLSubClassOfAxiom((OWLObjectHasSelf) addFreshClassName(freshConceptNumber), axiom.getSuperClass()));
				freshConceptNumber++;
			} else if (axiom.getSuperClass() instanceof OWLObjectSomeValuesFrom && axiom.getSubClass().isClassExpressionLiteral()) {
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(), (OWLObjectHasSelf) addFreshClassName(freshConceptNumber)));
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), getClassFromObjectSomeValuesFrom((OWLObjectHasSelf)axiom.getSuperClass())));
				freshConceptNumber++;
			}*/else if (axiom.getSubClass().isAnonymous() && axiom.getSuperClass().isAnonymous()) {
				// C subsumes D
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(), addFreshClassName(freshConceptNumber)));
				v_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), axiom.getSuperClass()));
				freshConceptNumber++;
			} else if (axiom.getSubClass().isTopEntity()) {
				//Empty Set 
				//C subsumes Top
				inputStringTranslation.add("{top("+axiom.getSuperClass()+")}");
			} else if (axiom.getSuperClass().isBottomEntity()) {
				//Empty Set
				//Bottom subsumes C
				inputStringTranslation.add("{bot("+axiom.getSubClass()+")}");
			} else if (axiom.getSubClass().isClassExpressionLiteral() && axiom.getSuperClass().isAnonymous() && axiom.getSuperClass().asConjunctSet().size()> 1) {
				//A subsumes C and D 
				Iterator<OWLClassExpression> itr = axiom.getSuperClass().asConjunctSet().iterator();				
				while(itr.hasNext()) {
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(), itr.next()));
				}
			} else if (axiom.getSuperClass().isClassExpressionLiteral() && !axiom.getSubClass().isClassExpressionLiteral() && axiom.getSubClass().isAnonymous() && axiom.getSubClass().asConjunctSet().size()>1) {
				//C and D subsumes B 
				Iterator<OWLClassExpression> itr = axiom.getSubClass().asConjunctSet().iterator();
				int i = axiom.getSubClass().asConjunctSet().size();
				//Set<OWLAxiom> tmpaxm = new HashSet<>();
				if (i==2) {
					while (itr.hasNext()) {
						if (itr.next().isClassExpressionLiteral()) {
							allClassNames[i-1] = true;
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
					inputStringTranslation.add("{subConj("+itr.next()+","+itr.next()+","+axiom.getSuperClass()+")}");
				} else {
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(itr.next(), addFreshClassName(freshConceptNumber)));
					v_axioms.add(v_factory.getOWLSubClassOfAxiom(getConjunctClassExpression(axiom.getSubClass(), addFreshClassName(freshConceptNumber), itr.next()), axiom.getSuperClass()));
					freshConceptNumber++;
				}
			} else {
				System.out.println("This is left!! sub ----------" + axiom.getSubClass().toString() +"          super-------"+axiom.getSuperClass().toString());
			}
		}
		@Override
		public void visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
			throw new IllegalAccessError("Annotation Negative Object Property Assertion Axiom Exception !");
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
			System.out.println("Object Property Domain Axiom");
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
			System.out.println("Different Individuals Axiom");
		}

		@Override
		public void visit(OWLDisjointDataPropertiesAxiom arg0) {
			throw new IllegalAccessError("Disjoint Data Properties Axiom Exception !");			
		}

		@Override
		public void visit(OWLDisjointObjectPropertiesAxiom arg0) {
			throw new IllegalAccessError("Disjoint Object Properties Axiom exception !");
		}

		@Override
		public void visit(OWLObjectPropertyRangeAxiom arg0) {
			System.out.println("Object Property Range Axiom ");
		}

		@Override
		public void visit(OWLObjectPropertyAssertionAxiom axiom) {
			// TODO Check
			n_axioms.add(axiom);
			System.out.println("ObjectProperty Assertion Axiom:"+ axiom.toString());
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
			n_axioms.add(v_factory.getOWLClassAssertionAxiom(addFreshClassName(freshConceptNumber), axiom.getIndividual()));
			n_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), axiom.getClassExpression()));
			freshConceptNumber++;			
		}

		@Override
		public void visit(OWLEquivalentClassesAxiom axiom) {
			//TODO
/*			Iterator<OWLClassExpression> itr = axiom.getClassExpressions().iterator();
			while (itr.hasNext()) {
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(itr.next(), addFreshClassName(freshConceptNumber)));
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber), itr.next()));
				//n_axioms.add(v_factory.getOWLSubClassOfAxiom(itr.next(), addFreshClassName(freshConceptName+1)));
				//n_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptName+1), itr.next()));
				freshConceptNumber ++;
			}*/
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
			throw new IllegalAccessError("Inverse Object Properties Axiom Exception !");
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
}
