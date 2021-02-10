package org.sirius.frontend.api;

import java.util.List;

import org.sirius.common.core.QName;

public interface ClassOrInterface extends ClassType {

	List<MemberValue> getMemberValues();
	List<AbstractFunction> getFunctions();
	
	/** This class/interface qualified name */
	QName getQName();

	
	default void visitContent(Visitor visitor) {
		
		for(MemberValue mv: getMemberValues()) {
			mv.visitMe(visitor);
		}
		
		for(AbstractFunction fct: getFunctions()) {
			fct.visitMe(visitor);
		}
	}
	
}
