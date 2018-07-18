import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;

public class ClassExpressionVisitorForNormalisedAxiomLeft extends DatalogTranslation implements OWLClassExpressionVisitor {

	protected OWLClassExpression super_class_of_axiom; 
	public ClassExpressionVisitorForNormalisedAxiomLeft(OWLClassExpression subClassExprOfAxm) {
		super_class_of_axiom = subClassExprOfAxm;
	}
	@Override
	public void visit(OWLClass ce) {
		// TODO 
		if(ce.isTopEntity()) {

		} else if (ce.isOWLNamedIndividual()) {

		} else {

		}
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		// TODO 
	}

	@Override
	public void visit(OWLObjectUnionOf ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectComplementOf ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectSomeValuesFrom ce) {
		// TODO 
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectHasValue ce) {

	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {

	}

	@Override
	public void visit(OWLObjectExactCardinality ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectMaxCardinality ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectHasSelf ce) {
		//TODO set facts
	}

	@Override
	public void visit(OWLObjectOneOf ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataSomeValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataAllValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataHasValue ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataMinCardinality ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataExactCardinality ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLDataMaxCardinality ce) {
		throw new IllegalStateException();
	}	
}