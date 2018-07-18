import java.util.Iterator;

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
import org.semanticweb.vlog4j.core.model.implementation.Expressions;


	/*
	 * Visitor Class for Axioms in the Ontology.
	 */
	public class AxiomVisitorForNormalisation extends Normalize implements OWLAxiomVisitor {
		//protected final List<OWLClassExpression[]> v_expressionList;
		protected static OWLClassExpression v_Right_Named_ClassExpression = null;
		protected static OWLClassExpression v_Leftt_Named_ClassExpression = null;
		protected static long v_counter_Fresh_Concept = 0;
		
		public AxiomVisitorForNormalisation(OWLDataFactory factory) {
			super(factory);
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
			if (axiom.getSubClass().isClassExpressionLiteral() && axiom.getSuperClass().isClassExpressionLiteral()) {
				if (axiom.getSubClass() instanceof OWLObjectComplementOf || axiom.getSuperClass() instanceof OWLObjectComplementOf) {
					throw new IllegalArgumentException("Complement of a named class is not OWL 2 EL !");
				} else { //this has to be added to normalised axioms here and not in the visitors of class Expression.  
					v_Normalised_Axioms.add(axiom);
				}		
			} else if(!axiom.getSubClass().isClassExpressionLiteral() && !axiom.getSuperClass().isClassExpressionLiteral()) {
				//C subsumes D
				OWLClassExpression new_classExpression = this.addFreshClassName(v_counter_Fresh_Concept);
				OWLAxiom new_Axiom_forsupcls = v_factory.getOWLSubClassOfAxiom(new_classExpression, axiom.getSuperClass());
				OWLAxiom new_Axiom_forsubcls = v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(),new_classExpression);
				this.addAxiomToMap(v_Iterable_KeyForMap +1, new_Axiom_forsubcls);
				this.addAxiomToMap(v_Iterable_KeyForMap +1, new_Axiom_forsupcls);
				v_counter_Fresh_Concept++;
			} else if (axiom.getSubClass().isClassExpressionLiteral()) {
				if (axiom.getSubClass() instanceof OWLObjectComplementOf) {
					throw new IllegalStateException();
				} else {
					ClassExpressionVisitorForNormalisationRight ceVisitor = new ClassExpressionVisitorForNormalisationRight(this.v_factory);
					//set the named class for use
					v_Leftt_Named_ClassExpression = axiom.getSubClass();
					axiom.getSuperClass().accept(ceVisitor);
				}
			} else {
				if (axiom.getSuperClass() instanceof OWLObjectComplementOf) {
					throw new IllegalStateException();
				} else {
					ClassExpressionVisitorForNormalisationLeft ceVisitor = new ClassExpressionVisitorForNormalisationLeft(this.v_factory);
					//set the named class for use
					v_Right_Named_ClassExpression = axiom.getSubClass();
					axiom.getSubClass().accept(ceVisitor);
				}
			} 

			if(!axiom.getSubClass().isBottomEntity() || !axiom.getSuperClass().isTopEntity() ) {
				this.normalizesubClassExpressionAxiom(axiom.getSubClass(), axiom.getSuperClass());
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
		/*	n_axioms.add(axiom);
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
		*/}

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
