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
	protected static Set<OWLAxiom> v_NotNormalised = new HashSet<>();

	static int v_counter_FreshConcept = 0;

	public AxiomVisitorForNormalisation(OWLDataFactory factory) {
		super(factory);
	}

	public int getNotNormalisedCount() {
		return v_NotNormalised.size();
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

		if (isNonComplementOFNamedClass(axiom.getSubClass()) ) {

				ClassExpressionVisitorForNormalisationRight ceVisitorR = new ClassExpressionVisitorForNormalisationRight(v_factory);

				setCurrentClassExpression(axiom.getSubClass());
				axiom.getSuperClass().accept(ceVisitorR);

		} else if (isNonComplementOFNamedClass(axiom.getSuperClass()) ) {

				ClassExpressionVisitorForNormalisationLeft ceVisitorL = new ClassExpressionVisitorForNormalisationLeft(v_factory);

				setCurrentClassExpression(axiom.getSuperClass());
				axiom.getSubClass().accept(ceVisitorL);
			
		} else {

				OWLClassExpression new_Expr = addFreshClassName(v_counter_FreshConcept);
				v_counter_FreshConcept++;

				addSubClassAxiom(axiom.getSubClass(), new_Expr).accept(this);
				addSubClassAxiom(new_Expr, axiom.getSuperClass()).accept(this);
			
		}
	}

	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
		try {
			v_NotNormalised.add(arg0);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom arg0) {
		try {
			v_NotNormalised.add(arg0);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
		axiom.asOWLSubClassOfAxiom().accept(this);
	}

	@Override
	public void visit(OWLDisjointClassesAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {
		axiom.asOWLSubClassOfAxiom().accept(this);	
	}

	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLDifferentIndividualsAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLDisjointDataPropertiesAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {
		axiom.asOWLSubClassOfAxiom().accept(this);
	}

	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {		
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {
		// only basic R subsumes T has been applied 
		getV_Normalised_Axioms().add(axiom);
	}

	@Override
	public void visit(OWLDisjointUnionAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}
	@Override
	public void visit(OWLClassAssertionAxiom axiom) {		
		axiom.asOWLSubClassOfAxiom().accept(this);
	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		Iterator<OWLSubClassOfAxiom> iter = axiom.asOWLSubClassOfAxioms().iterator();
		while (iter.hasNext()) {
			iter.next().accept(this);
		}
	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLSubDataPropertyOfAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLSameIndividualAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLSubPropertyChainOfAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLHasKeyAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(OWLDatatypeDefinitionAxiom axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void visit(SWRLRule axiom) {
		try {
			v_NotNormalised.add(axiom);
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
