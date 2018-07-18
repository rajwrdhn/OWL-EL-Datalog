import java.util.Iterator;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;

/*
 * Visitor Class for Axioms in the Ontology.
 */
public class AxiomVisitorForNormalisation extends Normalize implements OWLAxiomVisitor {
	//protected final List<OWLClassExpression[]> v_expressionList;
	protected static OWLClassExpression v_Right_Named_ClassExpression = null;
	protected static OWLClassExpression v_Leftt_Named_ClassExpression = null;


	public AxiomVisitorForNormalisation(OWLDataFactory factory) {
		super(factory);
	}

	@Override
	public void visit(OWLAnnotationAssertionAxiom axiom) {
		//Annotations do not change the logical meaning of the OWL Ontology.
	}

	@Override
	public void visit(OWLSubAnnotationPropertyOfAxiom arg0) {

	}

	@Override
	public void visit(OWLAnnotationPropertyDomainAxiom arg0) {
	}

	@Override
	public void visit(OWLAnnotationPropertyRangeAxiom arg0) {
	}

	@Override
	public void visit(OWLDeclarationAxiom arg0) {
		//Entity (Not Logical Axioms)
	}

	@Override
	public void visit(OWLSubClassOfAxiom axiom) {
		if (this.isNonComplementOFNamedClass(axiom.getSubClass()) 
				&& this.isNonComplementOFNamedClass(axiom.getSuperClass())) {
			//this has to be added to normalised axioms here and not in the visitors of class Expression.  
			v_Normalised_Axioms.add(axiom);					
		} else if(!axiom.getSubClass().isClassExpressionLiteral() && !axiom.getSuperClass().isClassExpressionLiteral()) {
			//C subsumes D
			OWLClassExpression new_classExpression = addFreshClassName(v_counter_Fresh_Concept);
			OWLAxiom new_Axiom_forsupcls = v_factory.getOWLSubClassOfAxiom(new_classExpression, axiom.getSuperClass());
			OWLAxiom new_Axiom_forsubcls = v_factory.getOWLSubClassOfAxiom(axiom.getSubClass(),new_classExpression);
			addAxiomToMap(v_Iterable_KeyForMap +1, new_Axiom_forsubcls);
			addAxiomToMap(v_Iterable_KeyForMap +1, new_Axiom_forsupcls);
			v_counter_Fresh_Concept++;
		} else if (this.isNonComplementOFNamedClass(axiom.getSubClass())) {
			ClassExpressionVisitorForNormalisationRight ceVisitor = new ClassExpressionVisitorForNormalisationRight(this.v_factory);
			//set the named class for use
			v_Leftt_Named_ClassExpression = axiom.getSubClass();
			axiom.getSuperClass().accept(ceVisitor);
		} else if (this.isNonComplementOFNamedClass(axiom.getSuperClass())){
			ClassExpressionVisitorForNormalisationLeft ceVisitor = new ClassExpressionVisitorForNormalisationLeft(this.v_factory);
			//set the named class for use
			v_Right_Named_ClassExpression = axiom.getSubClass();
			axiom.getSubClass().accept(ceVisitor);
		} else if (axiom.getSubClass().isOWLThing() && this.isNonComplementOFNamedClass(axiom.getSuperClass())) {
			//Top subsumes A 
			v_Normalised_Axioms.add(axiom);					
		} else if (axiom.getSuperClass().isOWLNothing() && this.isNonComplementOFNamedClass(axiom.getSubClass())) {
			//A subsumes Bot
			v_Normalised_Axioms.add(axiom);

		} else {
			//Empty Set
			System.out.println("sub------------"+axiom.getSubClass().toString()
					+"          super----------"+axiom.getSuperClass().toString());
		}
	}
	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom arg0) {
		//throw new IllegalAccessError("Annotation Negative Object Property Assertion Axiom Exception !");
		throw new IllegalArgumentException("The axiom "+arg0+" contains Negative Object property, "
				+ "which is not allowed in OWL 2 EL. ");
		//A negative ce_bool property assertion NegativeObjectPropertyAssertion( OPE a1 a2 ) 
		//states that the individual a1 is not connected by the ce_bool property expression OPE to the individual a2.
		//not in OWL 2 EL.
	}

	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom arg0) {
		throw new IllegalAccessError("Asymmetric Object Property Axiom Exception !");
	}

	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom arg0) {
		throw new IllegalAccessError("Reflexive Object Property Axiom Exception !");
	}

	@Override
	public void visit(OWLDisjointClassesAxiom axiom) {
		throw new IllegalAccessError("Disjoint Class Axiom Exception !");
	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom arg0) {
		throw new IllegalAccessError("Data Property Domain Axiom Exception !");
	}

	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {
		//TODO check?
		addAxiomToMap(v_Iterable_KeyForMap+1, axiom.asOWLSubClassOfAxiom());
	}

	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		//TODO
		Iterator<OWLSubObjectPropertyOfAxiom> iter = axiom.asSubObjectPropertyOfAxioms().iterator();
		//It can be very big !
		int i =10;
		while (iter.hasNext()) {
			v_Normalised_Axioms.add(iter.next());
			i++;
			if (i==10)
				break;
		}
	}

	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		throw new IllegalArgumentException("Negative Data Property Assertion Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLDifferentIndividualsAxiom axiom) {
		throw new IllegalArgumentException(
				"Not an OWL 2 EL axiom ! "+axiom.toString()+" Different Individuals Axiom !");
	}

	@Override
	public void visit(OWLDisjointDataPropertiesAxiom axiom) {
		throw new IllegalArgumentException(
				"Not an OWL 2 EL axiom ! "+axiom.toString()+" Disjoint data Property Axiom !");
	}

	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
		throw new IllegalArgumentException(
				"Not an OWL 2 EL axiom ! "+axiom.toString()+" Disjoint Object Property Axiom !");
	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		throw new IllegalArgumentException(	"Not an OWL 2 EL axiom ! "+axiom.toString()+" Object Property Range Axiom !");
	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {
		if(axiom.individualsInSignature().count()==2)
			v_Normalised_Axioms.add(axiom);
	}

	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException("Functional Object Property Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom) {
		//TODO only subRole(R,T) done if() 
		if (axiom.objectPropertiesInSignature().count() ==2)
			v_Normalised_Axioms.add(axiom);
	}

	@Override
	public void visit(OWLDisjointUnionAxiom axiom) {
		throw new IllegalArgumentException("Disjoint Union Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException("Symmetric Object Property Axiom" + axiom.toString());
	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom arg0) {
		throw new IllegalAccessError("Data Property Range Axiom Exception !");
	}

	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom) {
		throw new IllegalArgumentException("Functional Data Property Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
		throw new IllegalArgumentException("Equivalent Data Properties Axiom Exception !" + axiom.toString());
	}
	@Override
	public void visit(OWLClassAssertionAxiom axiom) {
		//TODO
		if (isNonComplementOFNamedClass(axiom.getClassExpression())) {
			v_Normalised_Axioms.add(v_factory.getOWLClassAssertionAxiom(axiom.getClassExpression(), axiom.getIndividual()));	
		} else {
			v_Normalised_Axioms.add(v_factory.getOWLClassAssertionAxiom(addFreshClassName(freshConceptNumber), axiom.getIndividual()));
			addAxiomToMap(v_Iterable_KeyForMap+1, addSubClassAxiom(addFreshClassName(freshConceptNumber), axiom.getClassExpression()));
			freshConceptNumber++;
		}
	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		//TODO There can be many axioms here!
		int i =10;
		Iterator<OWLSubClassOfAxiom> iter = axiom.asOWLSubClassOfAxioms().iterator();
		while (iter.hasNext()) {
			addAxiomToMap(v_Iterable_KeyForMap+1, iter.next());
			i++;
			if (i==10)
				break;
		}
	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {
		throw new IllegalArgumentException("Data Property Assertion Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException("Transitive Object Property Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException("Irreflexive Object Property Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLSubDataPropertyOfAxiom axiom) {
		throw new IllegalArgumentException("Sub Data Property Of Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		throw new IllegalArgumentException("Inverse Functional Object Property Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLSameIndividualAxiom axiom) {
		throw new IllegalArgumentException("Same Individual Axiom Exception !");
	}

	@Override
	public void visit(OWLSubPropertyChainOfAxiom axiom) {
		throw new IllegalArgumentException("Sub Property Chain Of Axiom" + axiom.toString());
	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		throw new IllegalArgumentException("Inverse Object Property Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLHasKeyAxiom axiom) {
		throw new IllegalArgumentException("Has Key Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(OWLDatatypeDefinitionAxiom axiom) {
		throw new IllegalArgumentException("Data Type Definition Axiom Exception !" + axiom.toString());
	}

	@Override
	public void visit(SWRLRule axiom) {
		throw new IllegalArgumentException("SWRL rule Exception !" + axiom.toString());
	}
}
