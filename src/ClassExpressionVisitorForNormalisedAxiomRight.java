import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
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
import org.semanticweb.vlog4j.core.model.api.Constant;
import org.semanticweb.vlog4j.core.model.api.Predicate;
import org.semanticweb.vlog4j.core.model.implementation.Expressions;


public class ClassExpressionVisitorForNormalisedAxiomRight extends VisitNormalisedAxioms implements OWLClassExpressionVisitor {
	protected OWLClassExpression sub_class_of_axiom; 

	public ClassExpressionVisitorForNormalisedAxiomRight(OWLClassExpression subClassExprOfAxm, Set<OWLAxiom> normalisedaxms) {
		super(normalisedaxms);
		sub_class_of_axiom = subClassExprOfAxm;
	}
	@Override
	public void visit(OWLClass ce) {
		if(ce.isBottomEntity()) {
			String predicatename = ce.toString() + sub_class_of_axiom.toString();
			addBotEDB(predicatename);
		} else if (ce.isOWLNamedIndividual()) {
			String predicatename = sub_class_of_axiom.toString() + ce.toString();
			Predicate predicate = Expressions.makePredicate(predicatename, 2);
			
			Constant c1 = Expressions.makeConstant(ce.toString());
			Constant c2 = Expressions.makeConstant(sub_class_of_axiom.toString());
			
			addClassNamesEDB(sub_class_of_axiom.toString());
			addNominalsEDB(ce.toString());
			addToSubClassEDB(Expressions.makePredicate(predicatename+"rule", 2));
			
			addToDoubleConstantFacts(predicate, c2, c1);
		} else {
			String predicatename = sub_class_of_axiom.toString() + ce.toString();
			Predicate predicate = Expressions.makePredicate(predicatename, 2);
			
			Constant c1 = Expressions.makeConstant(ce.toString());
			Constant c2 = Expressions.makeConstant(sub_class_of_axiom.toString());
			
			addClassNamesEDB(sub_class_of_axiom.toString());
			addClassNamesEDB(ce.toString());			
			addToSubClassEDB(Expressions.makePredicate(predicatename+"rule", 2));
			
			addToDoubleConstantFacts(predicate, c2, c1);
		}
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		//should not be present after normalisation
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
		
		String predicatename = sub_class_of_axiom.toString() + ce.toString();
		Predicate predicate = Expressions.makePredicate(predicatename, 4);
		
		Constant c1 = Expressions.makeConstant(ce.getFiller().toString());
		Constant c2 = Expressions.makeConstant(sub_class_of_axiom.toString());
		Constant c3 = Expressions.makeConstant(ce.getProperty().toString());
		Constant c4 = Expressions.makeConstant("aux"+auxnum);
		auxnum++;
		
		addClassNamesEDB(sub_class_of_axiom.toString());
		addClassNamesEDB(ce.getFiller().toString());	
		addrolesEDB(c3.getName());
		
		addToSupExEDB(Expressions.makePredicate(predicatename+"rule", 2));
		
		addToFourConstantFacts(predicate, c2, c3, c1, c4);
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		//Normalised
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		//Normalised
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
		String predicatename = sub_class_of_axiom.toString() + ce.getProperty().toString();
		Predicate predicate = Expressions.makePredicate(predicatename, 2);
		
		Constant c1 = Expressions.makeConstant(ce.getProperty().toString());
		Constant c2 = Expressions.makeConstant(sub_class_of_axiom.toString());
		
		addClassNamesEDB(sub_class_of_axiom.toString());
		addrolesEDB(predicatename);
		addToSupExEDB(Expressions.makePredicate(predicatename+"rule", 2));
		
		addToDoubleConstantFacts(predicate, c2, c1);
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
		//Normalised
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