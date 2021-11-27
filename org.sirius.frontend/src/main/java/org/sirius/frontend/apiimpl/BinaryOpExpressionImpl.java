package org.sirius.frontend.apiimpl;

import org.sirius.frontend.api.BinaryOpExpression;
import org.sirius.frontend.api.Expression;
import org.sirius.frontend.api.Type;
import org.sirius.frontend.api.Visitor;
import org.sirius.frontend.api.BinaryOpExpression.Operator;

public record BinaryOpExpressionImpl(Expression left, Expression right, Operator operator) implements BinaryOpExpression {

//	@Override
//	public Expression getLeft() {
//		return left;
//	}
//
//	@Override
//	public Expression getRight() {
//		return right;
//	}

	@Override
	public Type getType() {
		Type leftType = left.getType();
		Type rightType = right.getType();
		
		if(leftType == Type.integerType && rightType == Type.integerType ) {
			return Type.integerType;
		}
		
		throw new UnsupportedOperationException("Partial support of getType() in AstBinaryOpExpression yet (only integer are supported)");
	}

	@Override
	public void visitMe(Visitor visitor) {
		visitor.start(this);
		left().visitMe(visitor);
		right().visitMe(visitor);
		visitor.end(this);
	}
	@Override
	public String toString() {
		return left + " " + operator + " " + right;
	}

//	@Override
//	public Operator getOperator() {
//		return operator;
//	}
}