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
import org.semanticweb.vlog4j.core.model.implementation.Expressions;

public class ClassExpressionVisitorForNormalisationRight extends AxiomVisitorForNormalisation implements OWLClassExpressionVisitor{

	public ClassExpressionVisitorForNormalisationRight(OWLDataFactory factory) {
		super(factory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void visit(OWLClass ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		setCurrentClassExpression(v_Leftt_Named_ClassExpression);
		for (OWLClassExpression ce1 : ce.asConjunctSet()) {
			if(isNonComplementOFNamedClass(ce1)) {
				v_Normalised_Axioms.add(addSubClassAxiom(getCurrentClassExpression(), ce1));				
			} else {
				addAxiomToMap(v_Iterable_KeyForMap+1, addSubClassAxiom(getCurrentClassExpression(), addFreshClassName(freshConceptNumber)));
			}
		}
		v_Leftt_Named_ClassExpression = addFreshClassName(freshConceptNumber);
		//NOW IT WILL GO
	}

	@Override
	public void visit(OWLObjectUnionOf ce) {
		throw new IllegalArgumentException("OWLObjectUnionOf not OWL 2 EL ! " + ce.toString()  );
	}

	@Override
	public void visit(OWLObjectComplementOf ce) {
		throw new IllegalArgumentException("OWLObjectComplementOf not OWL 2 EL ! " + ce.toString()  );
	}

	@Override
	public void visit(OWLObjectSomeValuesFrom ce) {
		setCurrentClassExpression(v_Leftt_Named_ClassExpression);
		v_Normalised_Axioms.add(addSomevaluesFromAxiom(getCurrentClassExpression(), ce.getProperty(), addFreshClassName(freshConceptNumber)));
		addAxiomToMap(v_Iterable_KeyForMap+1, addSubClassAxiom(addFreshClassName(freshConceptNumber), ce));
		v_Leftt_Named_ClassExpression = addFreshClassName(freshConceptNumber);
		setCurrentClassExpression(addFreshClassName(freshConceptNumber));
		freshConceptNumber++;
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		throw new IllegalStateException("OWLObjectAllValuesFrom " + ce.toString());
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		//TODO Cross Check? ce is an individual 
		addAxiomToMap(v_Iterable_KeyForMap+1, v_factory.getOWLClassAssertionAxiom(getCurrentClassExpression(), ce.getFiller()));	
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		setCurrentClassExpression(v_Leftt_Named_ClassExpression);
		v_Normalised_Axioms.add(addSomevaluesFromAxiom(getCurrentClassExpression(), ce.getProperty(), addFreshClassName(freshConceptNumber)));
		addAxiomToMap(v_Iterable_KeyForMap+1, addSubClassAxiom(addFreshClassName(freshConceptNumber), ce.getFiller()));
		v_Leftt_Named_ClassExpression = addFreshClassName(freshConceptNumber);
		setCurrentClassExpression(addFreshClassName(freshConceptNumber));
		freshConceptNumber++;
	}

	@Override
	public void visit(OWLObjectExactCardinality ce) {
		throw new IllegalStateException("OWLObjectExactCardinality " + ce.toString());
	}

	@Override
	public void visit(OWLObjectMaxCardinality ce) {
		throw new IllegalStateException("OWLObjectMaxCardinality " + ce.toString());
	}

	@Override
	public void visit(OWLObjectHasSelf ce) {		
		v_Normalised_Axioms.add(addSubClassAxiom(getCurrentClassExpression(), v_factory.getOWLObjectHasSelf(ce.getProperty())));
	}

	@Override
	public void visit(OWLObjectOneOf ce) {
		throw new IllegalStateException("OWLObjectOneOf " + ce.toString());
	}

	@Override
	public void visit(OWLDataSomeValuesFrom ce) {
		throw new IllegalStateException("OWLDataSomeValuesFrom " + ce.toString());
	}

	@Override
	public void visit(OWLDataAllValuesFrom ce) {
		throw new IllegalStateException("OWLDataAllValuesFrom " + ce.toString());
	}

	@Override
	public void visit(OWLDataHasValue ce) {
		throw new IllegalStateException("OWLDataHasValue " + ce.toString());
	}

	@Override
	public void visit(OWLDataMinCardinality ce) {
		throw new IllegalStateException("OWLDataMinCardinality " + ce.toString());
	}

	@Override
	public void visit(OWLDataExactCardinality ce) {
		throw new IllegalStateException("OWLDataExactCardinality " + ce.toString());
	}

	@Override
	public void visit(OWLDataMaxCardinality ce) {
		throw new IllegalStateException("OWLDataMaxCardinality " + ce.toString());
	}
}
