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
	
	protected final boolean boolSub;
	protected final boolean boolSuper;
	
	public ClassExpressionVisitorForNormalisationRight(OWLDataFactory factory) {
		super(factory);
		boolSub = true;
		boolSuper = true;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void visit(OWLClass ce) {
		// TODO Auto-generated method stub
		OWLClassExpressionVisitor.super.visit(ce);
	}
	
	@Override
	public void visit(OWLObjectIntersectionOf ce) {
		//Done in normalizesubClassExpressionAxiom method above
		Iterator<OWLClassExpression> iter =ce.asConjunctSet().iterator();
		while(iter.hasNext() ) {
			
		}
		if (boolSuper) {
			normalizesubClassExpressionAxiom(getCurrentClassExpression(), ce);
		}else if (boolSub) {
			normalizesubClassExpressionAxiom(ce, getCurrentClassExpression());
		}
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
		if(boolSuper) {
			if (!ce.getFiller().isClassExpressionLiteral()) {
				//A subsumes Exists.R. C
				if (ce.getFiller() instanceof OWLObjectIntersectionOf) {
					k_axioms.add(v_factory.getOWLSubClassOfAxiom(addFreshClassName(freshConceptNumber),ce.getFiller()));
				}
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
						v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber))));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
						v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber))).toString(), 4),
						Expressions.makeConstant(getCurrentClassExpression().toString()),
						Expressions.makeConstant(ce.getProperty().toString()),
						Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
						Expressions.makeConstant("aux"+auxnum)							
						));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString(), 1), Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate("aux"+auxnum, 1), Expressions.makeConstant("aux"+auxnum)));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), Expressions.makeConstant(ce.getProperty().toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), Expressions.makeConstant(getCurrentClassExpression().toString())));
				supExEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
						v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber))).toString()+"rule", 4));
				clsEDB.add(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString()+"rule", 1));
				clsEDB.add(Expressions.makePredicate("aux"+auxnum+"rule", 1));
				rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
				clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
				setCurrentClassExpression(addFreshClassName(freshConceptNumber));
				auxnum++;
				freshConceptNumber++;
				
			} else {
				//A subsumes Exists.R. B
				if (ce.getFiller() instanceof OWLObjectComplementOf) {
					throw new IllegalArgumentException("OWLObjectComplementOf not OWL 2 EL ! " + ce.toString()  );
				}else {
					n_axioms.add(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), ce.getFiller())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), ce.getFiller())).toString(), 4),
							Expressions.makeConstant(getCurrentClassExpression().toString()),
							Expressions.makeConstant(ce.getProperty().toString()),
							Expressions.makeConstant(ce.getFiller().toString()),
							Expressions.makeConstant("aux"+auxnum)							
							));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getFiller().toString(), 1), Expressions.makeConstant(ce.getFiller().toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate("aux"+auxnum, 1), Expressions.makeConstant("aux"+auxnum)));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), Expressions.makeConstant(ce.getProperty().toString())));
					setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), Expressions.makeConstant(getCurrentClassExpression().toString())));
					supExEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), 
							v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), ce.getFiller())).toString().toString()+"rule", 4));
					clsEDB.add(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate("aux"+auxnum+"rule", 1));
					rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
					clsEDB.add(Expressions.makePredicate(ce.getFiller().toString()+"rule", 1));
					auxnum++;
				}
			}
		} else if (boolSub) {
			if(!ce.getFiller().isClassExpressionLiteral()) {
				// Exists.R.C subsumes A
				//C subsumes X
				if (ce.getFiller() instanceof OWLObjectIntersectionOf) {
					k_axioms.add(v_factory.getOWLSubClassOfAxiom(ce.getFiller(), addFreshClassName(freshConceptNumber)));
				} 						
				//Exists.R.X subsumes A
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(
						v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber)), 
						getCurrentClassExpression()));
				//subEx(R,X,A) where X-> new concept
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(
						v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber)), 
						getCurrentClassExpression()).toString(), 3), 
						Expressions.makeConstant(ce.getProperty().toString()),
						Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString()),
						Expressions.makeConstant(getCurrentClassExpression().toString())
						));
				//role
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString(), 1), Expressions.makeConstant(addFreshClassName(freshConceptNumber).toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), Expressions.makeConstant(getCurrentClassExpression().toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), Expressions.makeConstant(ce.getProperty().toString())));	
				subExEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(
						v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), addFreshClassName(freshConceptNumber)), 
						getCurrentClassExpression()).toString()+"rule", 3));
				clsEDB.add(Expressions.makePredicate(addFreshClassName(freshConceptNumber).toString()+"rule", 1));
				rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
				clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
				setCurrentClassExpression(addFreshClassName(freshConceptNumber));
				freshConceptNumber++;
			} else {
				n_axioms.add(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()));
				//Exists.R.B subsumes A
				//subEx(R,B,A)
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()).toString(), 3), Expressions.makeConstant(ce.getProperty().toString()), 
						Expressions.makeConstant(ce.getFiller().toString()),
						Expressions.makeConstant(getCurrentClassExpression().toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getFiller().toString(), 1), Expressions.makeConstant(ce.getFiller().toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), Expressions.makeConstant(getCurrentClassExpression().toString())));
				setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), Expressions.makeConstant(ce.getProperty().toString())));
				subExEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()).toString()+"rule", 3));
				clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
				rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
				clsEDB.add(Expressions.makePredicate(ce.getFiller().toString()+"rule", 1));
			}
		}
	}

	@Override
	public void visit(OWLObjectAllValuesFrom ce) {
		//throw new IllegalStateException("OWLObjectAllValuesFrom " + ce.toString());
	}

	@Override
	public void visit(OWLObjectHasValue ce) {
		k_axioms.add(v_factory.getOWLSubClassOfAxiom(ce.asSomeValuesFrom(), 
				getCurrentClassExpression()
				));
	}

	@Override
	public void visit(OWLObjectMinCardinality ce) {
		//OWLSubClassOfAxiom sub = new OWLSubClassOfAxiomImpl(subClass, superClass, annotations);
		k_axioms.add(v_factory.getOWLSubClassOfAxiom(v_factory.getOWLObjectSomeValuesFrom(ce.getProperty(), (OWLClassExpression) ce.getFiller()), 
				getCurrentClassExpression()
				));
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
		if(boolSuper) {
			//A subsumes Exists Self R
			n_axioms.add(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), ce));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), ce).toString(), 2),
					Expressions.makeConstant(getCurrentClassExpression().toString()),
					Expressions.makeConstant(ce.getProperty().toString())						
					));

			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), 
					Expressions.makeConstant(ce.getProperty().toString())
					));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), 
					Expressions.makeConstant(getCurrentClassExpression().toString())
					));
			rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
			clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
			supSelfEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(getCurrentClassExpression(), ce).toString()+"rule", 2));
		} else if (boolSub) {
			//Exists Self R subsumes A
			n_axioms.add(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()).toString(), 2),
					Expressions.makeConstant(ce.getProperty().toString()),
					Expressions.makeConstant(getCurrentClassExpression().toString())						
					));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(ce.getProperty().toString(), 1), 
					Expressions.makeConstant(ce.getProperty().toString())
					));
			setFacts.add(Expressions.makeAtom(Expressions.makePredicate(getCurrentClassExpression().toString(), 1), 
					Expressions.makeConstant(getCurrentClassExpression().toString())
					));
			rolEDB.add(Expressions.makePredicate(ce.getProperty().toString()+"rule", 1));
			clsEDB.add(Expressions.makePredicate(getCurrentClassExpression().toString()+"rule", 1));
			subSelfEDB.add(Expressions.makePredicate(v_factory.getOWLSubClassOfAxiom(ce, getCurrentClassExpression()).toString()+"rule", 2));			
		}
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
