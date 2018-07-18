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
import org.semanticweb.vlog4j.core.model.implementation.Expressions;


public class ClassExpressionVisitorForNormalisedAxiomRight extends DatalogTranslation implements OWLClassExpressionVisitor {
	protected OWLClassExpression sub_class_of_axiom; 
	public ClassExpressionVisitorForNormalisedAxiomRight(OWLClassExpression subClassExprOfAxm) {
		// TODO Auto-generated constructor stub
		sub_class_of_axiom = subClassExprOfAxm;
	}
	@Override
	public void visit(OWLClass ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
		if(ce.isBottomEntity()) {
			v_s_botEDB.add(Expressions.makePredicate(sub_class_of_axiom.toString(), 1));
			//v_s_Facts.add(Expressions.makeAtom(predicate, terms))
		} else if (ce.isOWLNamedIndividual()) {
			//v_s_subClassEDB.add(Expressions.makePredicate(name, arity));
			//v_s_Facts.add(Expressions.makeAtom(predicate, terms))
		} else {
			v_s_clsEDB.add(Expressions.makePredicate(sub_class_of_axiom.toString(), 1));
			v_s_clsEDB.add(Expressions.makePredicate(ce.toString(), 1));
			v_s_subClassEDB.add(Expressions.makePredicate(sub_class_of_axiom.toString()+ce.toString(), 2));
		}
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
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
		// setfacts 
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
		//setfacts
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