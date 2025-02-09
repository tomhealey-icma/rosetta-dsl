package com.regnosys.rosetta.types

import com.google.inject.Inject
import com.regnosys.rosetta.rosetta.expression.RosettaConditionalExpression
import com.regnosys.rosetta.rosetta.RosettaExternalFunction
import com.regnosys.rosetta.rosetta.simple.Operation
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference

import static com.regnosys.rosetta.rosetta.expression.ExpressionPackage.Literals.*
import static com.regnosys.rosetta.rosetta.simple.SimplePackage.Literals.*
import com.regnosys.rosetta.rosetta.expression.RosettaSymbolReference

class RosettaExpectedTypeProvider {
	
	@Inject extension RosettaTypeProvider 
	
	def RType getExpectedType(EObject owner, EReference reference, int idx) {
		switch owner {
			RosettaConditionalExpression case reference == ROSETTA_CONDITIONAL_EXPRESSION__IF:
				RBuiltinType.BOOLEAN
			RosettaSymbolReference case reference == ROSETTA_SYMBOL_REFERENCE__ARGS: {
				if(idx >= 0 && owner.symbol instanceof RosettaExternalFunction) {
					val fun =  (owner.symbol as RosettaExternalFunction)
					if(idx >= fun.parameters.size) {
						null // add error type? 
					} else {
						val targetParam = fun.parameters.get(idx)
						targetParam.type.RType
					}
				}
			}
			Operation case reference == OPERATION__EXPRESSION: {
				if(owner.path === null)
					owner.assignRoot.RType
				else owner.pathAsSegmentList.last?.attribute?.RType
			}
		}
	}
	
	def RType getExpectedType(EObject owner, EReference reference) {
		owner.getExpectedType(reference, -1)
	}
}
