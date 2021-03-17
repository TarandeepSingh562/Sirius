package org.sirius.backend.jvm;

import java.util.stream.Collectors;

import org.sirius.common.error.Reporter;
import org.sirius.frontend.api.AbstractFunction;
import org.sirius.frontend.api.ArrayType;
import org.sirius.frontend.api.ClassType;
import org.sirius.frontend.api.FunctionFormalArgument;
import org.sirius.frontend.api.Scope;
import org.sirius.frontend.api.Type;
import org.sirius.frontend.api.VoidType;

/** Tools for creation or JVM descriptors (types, methods...)
 * 
 * @author jpragey
 *
 */
public class DescriptorFactory {
	private Reporter reporter;

	public DescriptorFactory(Reporter reporter) {
		super();
		this.reporter = reporter;
	}
			
	
	public String fieldDescriptor(Type type) {
		if(type instanceof ClassType) {
			ClassType classType = (ClassType)type;
			String descriptor = classType.getQName().getStringElements().stream().collect(Collectors.joining("/", "L", ";"));
			return descriptor;
			
		} else if(type instanceof VoidType) {
			return "V";
		} else if(type instanceof ArrayType) {
			ArrayType arrayType = (ArrayType)type;
			return "[" + fieldDescriptor(arrayType.getElementType());
		} else {
			reporter.error("JVM backend: internal error creating fieldDescriptor, type " + type + ":" + type.getClass() + " has no mapping to JVM type descriptor.");
			return "";
		}
	}
	
	public String methodDescriptor(AbstractFunction function/*, Scope scope*/) {
		Type returnType = function .getReturnType();
		String descr = function.getArguments().stream()
			.map((FunctionFormalArgument arg) -> fieldDescriptor(arg.getType()) )
			.collect(Collectors.joining("", "(", ")"))
			+ (returnType instanceof VoidType ? "V" : fieldDescriptor(returnType))
			;
		
		return descr;
	}
	

}
