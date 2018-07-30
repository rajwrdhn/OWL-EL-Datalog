import java.util.List;
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

	public ClassExpressionVisitorForNormalisationLeft(OWLDataFactory factory) {
		super(factory);
	}
	
	public OWLClassExpression getIntersectionOf(List<OWLClassExpression> ce_conjunct) {

		if (ce_conjunct.size() ==1) {
			OWLClassExpression ce = ce_conjunct.get(0);
			return (OWLClassExpression)ce;
		} else {
			return (OWLClassExpression)v_factory.getOWLObjectIntersectionOf(ce_conjunct);
		}	
	}

	@Override
	public void visit(OWLClass ce) {
		if(ce.isOWLNamedIndividual() || ce.isTopEntity() || ce.isClassExpressionLiteral() ) {
			getV_Normalised_Axioms().add(addSubClassAxiom(ce, 
					getCurrentClassExpression()));
		}
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {		

		int i = (int) ce.operands().count();
		List<OWLClassExpression> obj_intersection = ce.getOperandsAsList();
		List<OWLClassExpression> obj_intersection1 = obj_intersection.subList(0, (i+1)/2);
		List<OWLClassExpression> obj_intersection2 = obj_intersection.subList((i+1)/2, i);
		
		obj_intersection1.remove(null);
		obj_intersection2.remove(null);

		OWLClassExpression ce1 = getIntersectionOf(obj_intersection1);
		OWLClassExpression ce2 = getIntersectionOf(obj_intersection2);
		
		

		if (isNonComplementOFNamedClass(ce1) && isNonComplementOFNamedClass(ce2)) {
			getV_Normalised_Axioms().add(addAxiomOfConjunctSubClass(ce1, ce2,
					getCurrentClassExpression()));
		} else {
			OWLClassExpression new_Expr = addFreshClassName(v_counter_FreshConcept);
			v_counter_FreshConcept++;			
			
			OWLClassExpression new_Expr2 = addFreshClassName(v_counter_FreshConcept);
			v_counter_FreshConcept++;
						
			getV_Normalised_Axioms().add(addAxiomOfConjunctSubClass(new_Expr, new_Expr2, 
					getCurrentClassExpression()));
			
			getAxiomsForFurtherNorm().add(addSubClassAxiom(ce1, new_Expr));
			getAxiomsForFurtherNorm().add(addSubClassAxiom(ce2, new_Expr2));		
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

		OWLClassExpression new_Expr = addFreshClassName(v_counter_FreshConcept);
		v_counter_FreshConcept++;

		getV_Normalised_Axioms().add(addSomevaluesFromAxiomLeft(new_Expr, ce.getProperty(), 
				getCurrentClassExpression()));
		
		setCurrentClassExpression(new_Expr);
		ce.getFiller().accept(this);

		//addSubClassAxiom(ce.getFiller(), new_Expr).accept(this);
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		//throw new IllegalStateException();
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		
		// Exists R {o} subsumes A
		getV_Normalised_Axioms().add(addSubClassAxiom(ce, getCurrentClassExpression()));
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {	
		
		getV_Normalised_Axioms().add(addSubClassAxiom(ce, getCurrentClassExpression()));

/*		OWLClassExpression new_Expr = addFreshClassName(v_counter_FreshConcept);
		v_counter_FreshConcept++;

		getV_Normalised_Axioms().add(addSomevaluesFromAxiomLeft(new_Expr, ce.getProperty(), getCurrentClassExpression()));

		setCurrentClassExpression(new_Expr);
		ce.getFiller().accept(this);
		//addSubClassAxiom(ce.getFiller(), new_Expr).accept(this);
*/	}

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
		getV_Normalised_Axioms().add(addSubClassAxiom(ce, 
				getCurrentClassExpression()));
	}

	@Override
	public void visit(OWLObjectOneOf ce) {
		ce.individuals().forEach(x -> 
		getV_Normalised_Axioms().add(v_factory.getOWLClassAssertionAxiom(getCurrentClassExpression(), x)));
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
