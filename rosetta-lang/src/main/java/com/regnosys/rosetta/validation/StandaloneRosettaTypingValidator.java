package com.regnosys.rosetta.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.EValidatorRegistrar;

import com.google.inject.Inject;
import com.regnosys.rosetta.rosetta.RosettaCardinality;
import com.regnosys.rosetta.rosetta.expression.ChoiceOperation;
import com.regnosys.rosetta.rosetta.expression.RosettaOnlyElement;
import com.regnosys.rosetta.rosetta.simple.Attribute;
import com.regnosys.rosetta.types.RListType;
import com.regnosys.rosetta.types.TypeFactory;
import com.regnosys.rosetta.types.TypeSystem;
import com.regnosys.rosetta.types.TypeValidationUtil;
import com.regnosys.rosetta.typing.validation.RosettaTypingValidator;

import static com.regnosys.rosetta.rosetta.expression.ExpressionPackage.Literals;

public class StandaloneRosettaTypingValidator extends RosettaTypingValidator {
	@Inject
	private TypeSystem ts;
	
	@Inject
	private TypeFactory tf;
	
	@Inject
	private TypeValidationUtil tu;
	
	@Override
	protected List<EPackage> getEPackages() {
		List<EPackage> result = new ArrayList<EPackage>();
		result.add(EPackage.Registry.INSTANCE.getEPackage("http://www.rosetta-model.com/Rosetta"));
		result.add(EPackage.Registry.INSTANCE.getEPackage("http://www.rosetta-model.com/RosettaSimple"));
		result.add(EPackage.Registry.INSTANCE.getEPackage("http://www.rosetta-model.com/RosettaExpression"));
		return result;
	}
	
	@Override
	public void register(EValidatorRegistrar registrar) {
	}
	
	/**
	 * Xsemantics does not allow raising warnings. See https://github.com/eclipse/xsemantics/issues/149.
	 */
	@Check
	public void checkOnlyElement(RosettaOnlyElement e) {
		RListType t = ts.inferType(e.getArgument());
		if (t != null) {
			RosettaCardinality minimalConstraint = tf.createConstraint(1, 2);
			if (!minimalConstraint.isSubconstraintOf(t.getConstraint())) {
				warning(tu.notLooserConstraintMessage(minimalConstraint, t), e, Literals.ROSETTA_UNARY_OPERATION__ARGUMENT);
			}
		}
	}
	
	/**
	 * Xsemantics does not allow raising errors on a specific index of a multi-valued feature.
	 * See https://github.com/eclipse/xsemantics/issues/64.
	 */
	@Check
	public void checkChoiceOperationHasNoDuplicateAttributes(ChoiceOperation e) {
		for (var i = 1; i < e.getAttributes().size(); i++) {
			Attribute attr = e.getAttributes().get(i);
			for (var j = 0; j < i; j++) {
				if (attr.equals(e.getAttributes().get(j))) {
					error("Duplicate attribute.", e, Literals.CHOICE_OPERATION__ATTRIBUTES, i);
				}
			}
		}
	}
}
