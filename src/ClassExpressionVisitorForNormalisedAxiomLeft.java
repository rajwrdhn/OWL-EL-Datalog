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

public class ClassExpressionVisitorForNormalisedAxiomLeft extends VisitNormalisedAxioms implements OWLClassExpressionVisitor {

	protected OWLClassExpression super_class_of_axiom; 

	public ClassExpressionVisitorForNormalisedAxiomLeft(OWLClassExpression superClassExprOfAxm, Set<OWLAxiom> normalisedaxms) {
		super(normalisedaxms);
		super_class_of_axiom = superClassExprOfAxm;
	}

	@Override
	public void visit(OWLClass ce) {
		if (ce.isOWLNamedIndividual()) {
			Constant c1 = getConstant(ce.toString());
			Constant c2 = getConstant(super_class_of_axiom.toString());
			
			toDoubleConstantFacts(v_subClassEDB, c1, c2);
			toSingleConstantFacts(v_nomEDB, c1);
			toSingleConstantFacts(v_clsEDB, c2);			
		} else if (ce.isOWLThing()) {
			//Constant c1 = getConstant(ce.toString());
			Constant c2 = getConstant(super_class_of_axiom.toString());
			toSingleConstantFacts(v_topEDB, c2);
			toSingleConstantFacts(v_clsEDB, c2);
		} else {
			Constant c1 = getConstant(ce.toString());
			Constant c2 = getConstant(super_class_of_axiom.toString());
			toDoubleConstantFacts(v_subClassEDB,c1,c2);
			toSingleConstantFacts(v_clsEDB, c1);
			toSingleConstantFacts(v_clsEDB, c2);
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

		Constant c1 = getConstant(clsindi[0].toString());
		Constant c2 = getConstant(clsindi[1].toString());
		Constant c3 = getConstant(super_class_of_axiom.toString());
		
		toThreeConstantFacts(v_subConjEDB, c1, c2, c3);
		toSingleConstantFacts(v_clsEDB, c1);
		toSingleConstantFacts(v_clsEDB, c2);
		toSingleConstantFacts(v_clsEDB, c3);
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

		Constant c1 = getConstant(ce.getFiller().toString());
		Constant c2 = getConstant(super_class_of_axiom.toString());
		Constant c3 = getConstant(ce.getProperty().toString());
		
		toSingleConstantFacts(v_clsEDB, c1);
		toThreeConstantFacts(v_subExEDB, c3, c1, c2);
		toSingleConstantFacts(v_clsEDB, c2);
		toSingleConstantFacts(v_rolEDB, c3);
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		Constant c1 = getConstant(ce.getFiller().toString());
		Constant c2 = getConstant(super_class_of_axiom.toString());
		Constant c3 = getConstant(ce.getProperty().toString());
		
		toSingleConstantFacts(v_nomEDB, c1);
		toThreeConstantFacts(v_subExEDB, c3, c1, c2);
		toSingleConstantFacts(v_clsEDB, c2);
		toSingleConstantFacts(v_rolEDB, c3);
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		Constant c1 = getConstant(ce.getFiller().toString());
		Constant c2 = getConstant(super_class_of_axiom.toString());
		Constant c3 = getConstant(ce.getProperty().toString());
		
		toSingleConstantFacts(v_nomEDB, c1);
		toThreeConstantFacts(v_subExEDB, c3, c1, c2);
		toSingleConstantFacts(v_clsEDB, c2);
		toSingleConstantFacts(v_rolEDB, c3);
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
		
		Constant c1 = getConstant(ce.getProperty().toString());
		Constant c2 = getConstant(super_class_of_axiom.toString());
		
		toDoubleConstantFacts(v_subSelfEDB, c1, c2);
		toSingleConstantFacts(v_rolEDB, c1);
		toSingleConstantFacts(v_clsEDB, c2);
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