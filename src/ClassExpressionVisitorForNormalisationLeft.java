import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
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

/**
 * Visiting the sub class expression of subclassAxioms 
 * Super class expression is a named class!
 * @author raj
 *
 */
public class ClassExpressionVisitorForNormalisationLeft extends AxiomVisitorForNormalisation implements OWLClassExpressionVisitor {

	protected static OWLClassExpression v_classExpression = null;  
	public ClassExpressionVisitorForNormalisationLeft(OWLDataFactory factory) {
		super(factory);
	}

	@Override
	public void visit(OWLClass ce) {
		if (ce.isOWLNamedIndividual()) {
			v_Normalised_Axioms.add(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()));
		}else if(ce.isOWLNothing()) {
			v_Normalised_Axioms.add(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()));
		} else {
			v_Normalised_Axioms.add(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()));
		}
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		//done in axiom visitor
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
		if (isNonComplementOFNamedClass(ce)) {
			v_Normalised_Axioms.add(addSubClassAxiom(ce, v_Right_Named_ClassExpression));
		} else {
			
			OWLClassExpression new_Expr = addFreshClassName(v_counter_FreshConcept);			
			
			if (ce.getFiller() instanceof OWLObjectIntersectionOf) {
				v_For_FurtherNormalisation.add(addSubClassAxiom(ce.getFiller(), new_Expr));
				v_Normalised_Axioms.add(addSomevaluesFromAxiom(new_Expr, ce.getProperty(), v_Right_Named_ClassExpression));
			
			} else {
				setCurrentClassExpression(new_Expr);
			}
		}
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		v_Normalised_Axioms.add(addSubClassAxiom(ce, getCurrentClassExpression()));
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		ce.getProperty();
		ce.getCardinality();
		ce.getFiller();
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
