package org.sirius.frontend.api;

import java.util.Optional;

public interface TopLevelValue extends AbstractValue {

	Optional<Expression> getInitialValue();

	default void visitMe(Visitor visitor) {
		visitor.start(this);
		visitor.end(this);
	}

}
