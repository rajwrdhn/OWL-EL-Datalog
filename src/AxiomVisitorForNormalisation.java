import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
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

/*
 * Visitor Class for Axioms in the Ontology.
 */
public class AxiomVisitorForNormalisation extends Normalize implements OWLAxiomVisitor {

	static OWLClassExpression v_classExpression = null;

	protected static Set<OWLAxiom> v_Normalised_Axioms = new HashSet<>();
	
	protected static Set<OWLAxiom> v_For_FurtherNormalisation = new HashSet<>();

	static int v_counter_FreshConcept = 0;

	public AxiomVisitorForNormalisation(OWLDataFactory factory) {
		super(factory);
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
	public static OWLClassExpression getCurrentClassExpression() {
		return v_classExpression;
	}

	public void setCounterOfFreshNumber(int x) {
		v_counter_FreshConcept= x;
	}
	public int getCounterOfFreshNumber() {
		return v_counter_FreshConcept;
	}
	
	public Set<OWLAxiom> getV_Normalised_Axioms() {
		return v_Normalised_Axioms;
	}
	
	public Set<OWLAxiom> getAxiomsForFurtherNorm() {
		return v_For_FurtherNormalisation;
	}
	public void clear() {
		v_For_FurtherNormalisation.clear();
		v_For_FurtherNormalisation.remove(null);
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
		//if (axiom.getSubClass())
		ClassExpressionVisitorForNormalisationLeft ceVisitorL = new ClassExpressionVisitorForNormalisationLeft(v_factory);
		OWLClassExpression new_Expr = addFreshClassName(v_counter_FreshConcept);
		v_counter_FreshConcept++;
		setCurrentClassExpression(new_Expr);
		axiom.getSubClass().accept(ceVisitorL);

		ClassExpressionVisitorForNormalisationRight ceVisitorR = new ClassExpressionVisitorForNormalisationRight(v_factory);
		OWLClassExpression new_ExprL = addFreshClassName(v_counter_FreshConcept);
		v_counter_FreshConcept++;
		setCurrentClassExpression(new_ExprL);
		axiom.getSuperClass().accept(ceVisitorR);

		getV_Normalised_Axioms().add(addSubClassAxiom(new_ExprL, new_Expr));
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
		//throw new IllegalAccessError("Disjoint Class Axiom Exception !");
	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom) {
		try {
			System.out.println("Data Property Domain Axiom Exception !" + axiom.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {

		axiom.asOWLSubClassOfAxiom().accept(this);	
	}

	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {

		Iterator<OWLSubObjectPropertyOfAxiom> iter = axiom.asSubObjectPropertyOfAxioms().iterator();
		while (iter.hasNext()) {
			getV_Normalised_Axioms().add(iter.next());
		}
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
		getV_Normalised_Axioms().add(axiom);
	}

	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
		
		try {
			System.out.println("Functional Object Property Axiom Exception !" + axiom.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {
		//TODO only subRole(R,T) done. check?
		// for full use OWLObjectVisitor
		// Concept product
/*		if (axiom.objectPropertiesInSignature().count() ==2) {
			getV_Normalised_Axioms().add(axiom);
		} */
	}

	@Override
	public void visit(OWLDisjointUnionAxiom axiom) {
		throw new IllegalArgumentException("Disjoint Union Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
		try {
			System.out.println(axiom.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom axiom) {
		try {
			System.out.println("Data Property Range Axiom Exception !" + axiom.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom) {
		//throw new IllegalArgumentException("Functional Data Property Axiom Exception !" + axiom.toString());
		try {
			System.out.println("Functional Data Property Axiom Exception !" + axiom.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
		throw new IllegalArgumentException("Equivalent Data Properties Axiom Exception !" + axiom.toString());
	}
	@Override
	public void visit(OWLClassAssertionAxiom axiom) {

		//axiom.asOWLSubClassOfAxiom().accept(this);
		OWLClassExpression new_Expression =addFreshClassName(v_counter_FreshConcept);
		v_counter_FreshConcept++;
		//X(a)
		getV_Normalised_Axioms().add(v_factory.getOWLClassAssertionAxiom(new_Expression, axiom.getIndividual()));
		//X :- C
		getAxiomsForFurtherNorm().add(addSubClassAxiom(new_Expression, axiom.getClassExpression()));
		System.out.println(axiom.getClassExpression());
		//addSubClassAxiom(new_Expression, axiom.getClassExpression()).accept(this);s
	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		Iterator<OWLSubClassOfAxiom> iter = axiom.asOWLSubClassOfAxioms().iterator();
		while (iter.hasNext()) {
			getAxiomsForFurtherNorm().add(iter.next());
		}
	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {
		throw new IllegalArgumentException("Data Property Assertion Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
		//throw new IllegalArgumentException("Transitive Object Property Axiom Exception !" + axiom.toString());
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
		try {
			System.out.println("Inverse Functional Object Property Axiom Exception !" + axiom.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void visit(OWLSameIndividualAxiom axiom) {
		throw new IllegalArgumentException("Same Individual Axiom Exception !");
	}

	@Override
	public void visit(OWLSubPropertyChainOfAxiom axiom) {
		//throw new IllegalArgumentException("Sub Property Chain Of Axiom" + axiom.toString());
		try {
			
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		//throw new IllegalArgumentException("Inverse Object Property Exception !" + axiom.toString());
		try {
			
		} catch (Exception e) {
			e.getMessage();
		}
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
