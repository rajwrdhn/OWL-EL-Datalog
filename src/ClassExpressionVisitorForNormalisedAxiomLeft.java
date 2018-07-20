import java.util.Iterator;
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

public class ClassExpressionVisitorForNormalisedAxiomLeft extends VisitNormalisedAxioms implements OWLClassExpressionVisitor {

	protected OWLClassExpression super_class_of_axiom; 
	public ClassExpressionVisitorForNormalisedAxiomLeft(OWLClassExpression superClassExprOfAxm, Set<OWLAxiom> normalisedaxms) {
		super(normalisedaxms);
		super_class_of_axiom = superClassExprOfAxm;
	}
	@Override
	public void visit(OWLClass ce) {
		// TODO 
		if(ce.isTopEntity()) {
			String predicatename = ce.toString() + super_class_of_axiom.toString();
			addTopEDB(predicatename);
		} else if (ce.isOWLNamedIndividual()) {
			String predicatename = super_class_of_axiom.toString() + ce.toString();
			Predicate predicate = Expressions.makePredicate(predicatename, 2);
			
			Constant c1 = Expressions.makeConstant(ce.toString());
			Constant c2 = Expressions.makeConstant(super_class_of_axiom.toString());
			
			addClassNamesEDB(super_class_of_axiom.toString());
			addNominalsEDB(ce.toString());
			addToSubClassEDB(predicate);
			
			addToDoubleConstantFacts(predicate, c1, c2);

		} else {
			String predicatename = super_class_of_axiom.toString() + ce.toString();
			Predicate predicateEDB = Expressions.makePredicate(predicatename+"EDB", 2);
			Predicate predicateIDB = Expressions.makePredicate(predicatename+"IDB", 2);
			
			Constant c1 = Expressions.makeConstant(ce.toString());
			Constant c2 = Expressions.makeConstant(super_class_of_axiom.toString());
			
			addClassNamesEDB(super_class_of_axiom.toString());
			addClassNamesEDB(ce.toString());			
			addToSubClassEDB(predicateEDB);
			addToSubClassEDB(predicateIDB);
			
			addToDoubleConstantFacts(predicateEDB, c1, c2);
		}
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		Iterator<OWLClassExpression> iter = ce.asConjunctSet().iterator();
		OWLClassExpression[] clsindi = new OWLClassExpression[2];
		int i = 0 ;
		
		while(iter.hasNext()) {
			clsindi[i] = iter.next();
			i++;
		}
		
		String predicatename = ce.toString()+super_class_of_axiom.toString();
		Predicate predicate = Expressions.makePredicate(predicatename, 3);
		
		Constant c1 = Expressions.makeConstant(clsindi[0].toString());
		Constant c2 = Expressions.makeConstant(clsindi[1].toString());
		Constant c3 = Expressions.makeConstant(super_class_of_axiom.toString());
		
		addClassNamesEDB(c1.getName());
		addClassNamesEDB(c2.getName());
		addClassNamesEDB(c3.getName());
		
		addToThreeConstantFacts(predicate, c1, c2, c3);
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
		String predicatename = super_class_of_axiom.toString() + ce.toString();
		Predicate predicate = Expressions.makePredicate(predicatename, 3);
		
		Constant c1 = Expressions.makeConstant(ce.getFiller().toString());
		Constant c2 = Expressions.makeConstant(super_class_of_axiom.toString());
		Constant c3 = Expressions.makeConstant(ce.getProperty().toString());
		
		addClassNamesEDB(super_class_of_axiom.toString());
		addClassNamesEDB(ce.getFiller().toString());	
		addrolesEDB(c3.getName());
		
		addToSubExEDB(predicate);
		
		addToThreeConstantFacts(predicate, c3, c1, c2);
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		//normalised
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		//normalised
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
		String predicatename = super_class_of_axiom.toString() + ce.getProperty().toString();
		Predicate predicate = Expressions.makePredicate(predicatename, 2);
		
		Constant c1 = Expressions.makeConstant(ce.getProperty().toString());
		Constant c2 = Expressions.makeConstant(super_class_of_axiom.toString());
		
		addClassNamesEDB(super_class_of_axiom.toString());
		addrolesEDB(c1.getName());
		addToSupExEDB(predicate);
		
		addToDoubleConstantFacts(predicate, c1, c2);
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