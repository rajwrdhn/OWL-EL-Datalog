import java.util.Iterator;

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
		// TODO Auto-generated constructor stub
	}
	
	//A and B subsumes classexpression
	
	public void insertNormalisedConjunctionAsAxiom() {
		
	}
	
	public void checkForNormalisedConjunct(OWLClassExpression sub) {
		
	}
	
	/**
	 * set the class expression to be used as super ClassExpression Normalize 
	 */
	public void setCurrentClassExpression(OWLClassExpression ce) {
		v_classExpression = ce;
	}

	@Override
	public void visit(OWLClass ce) {
/*		if(ce.isOWLNothing()) {
			v_Normalised_Axioms.add(v_factory.getOWLSubClassOfAxiom(ce, v_Right_Named_ClassExpression));
		} else {
			v_Normalised_Axioms.add(v_factory.getOWLSubClassOfAxiom(ce, v_Right_Named_ClassExpression));
		}*/
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		int size = ce.asConjunctSet().size();
		boolean v_boolcheck = false;
		Iterator<OWLClassExpression> iter = ce.asConjunctSet().iterator();
		if (size == 2) {
			while (iter.hasNext()) {
				if (iter.next().isClassExpressionLiteral()) {
					v_boolcheck = true;
				}
			}
		} else if (size>=3){
			
		}
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
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLObjectExactCardinality ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLObjectMaxCardinality ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLObjectHasSelf ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLObjectOneOf ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLDataSomeValuesFrom ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLDataAllValuesFrom ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLDataHasValue ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLDataMinCardinality ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLDataExactCardinality ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLDataMaxCardinality ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

}
