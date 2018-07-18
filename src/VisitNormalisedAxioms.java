import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
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

public class VisitNormalisedAxioms extends DatalogTranslation implements OWLAxiomVisitor{
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
