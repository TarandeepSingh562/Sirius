package org.sirius.frontend.core.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sirius.common.core.QName;
import org.sirius.common.error.AccumulatingReporter;
import org.sirius.common.error.Reporter;
import org.sirius.common.error.ShellReporter;
import org.sirius.frontend.api.Expression;
import org.sirius.frontend.ast.Annotation;
import org.sirius.frontend.ast.AstExpression;
import org.sirius.frontend.ast.AstIntegerConstantExpression;
import org.sirius.frontend.ast.AstInterfaceDeclaration;
import org.sirius.frontend.ast.AstMemberValueDeclaration;
import org.sirius.frontend.ast.AstType;
import org.sirius.frontend.ast.SimpleReferenceExpression;
import org.sirius.frontend.ast.SimpleType;
import org.sirius.frontend.ast.TypeParameter;
import org.sirius.frontend.ast.Variance;
import org.sirius.frontend.core.parser.TypeParameterParser.TypeParameterVisitor;
import org.sirius.frontend.parser.Sirius;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

public class MemberValueDeclarationParserTest {

	private Reporter reporter ;
	
	@BeforeEach
	public void setup() {
		this.reporter = new AccumulatingReporter(new ShellReporter());
	}
	@AfterEach
	public void tearDown() {
		assert(this.reporter.ok());
	}

	@Test
	@DisplayName("Simple member values")
	public void simpleMemberValue() {
		parseMemberValue("Integer i;",      ((AstMemberValueDeclaration memberValue) -> {
			assertThat(memberValue.getNameString(), equalTo("i") );
			assertThat(memberValue.getApiInitialValue().isPresent(), equalTo(false) );
			assertThat(memberValue.getType(), instanceOf(SimpleType.class));
			assertThat(((SimpleType)memberValue.getType()).getNameString(), equalTo("Integer"));
			}) );
		parseMemberValue("Integer i = 42;",      ((AstMemberValueDeclaration memberValue) -> {
			assertThat(memberValue.getApiInitialValue().isPresent(), equalTo(true) );
			AstExpression initExpr = memberValue.getInitialValue().get();
			assertThat(initExpr, instanceOf(AstIntegerConstantExpression.class));
			assertThat(((AstIntegerConstantExpression)initExpr).getValue(), equalTo(42));
			}) );
	}

	private AstMemberValueDeclaration parseMemberValue(String inputText, Consumer<AstMemberValueDeclaration> verify) {
//		Sirius parser = ParserUtil.createParser(reporter, inputText);
		ParserUtil.ParserFactory parserFactory = ParserUtil.createParserFactory(reporter, inputText);
		Sirius parser = parserFactory.create();

		ParseTree tree = parser.memberValueDeclaration();
		
		MemberValueDeclarationParser.MemberValueVisitor visitor = new MemberValueDeclarationParser.MemberValueVisitor(reporter, parserFactory.tokenStream());
		AstMemberValueDeclaration memberValue = visitor.visit(tree);
		
		verify.accept(memberValue);
		return memberValue;
	}

	@Test
	@DisplayName("Parsing annotations on member values")
	public void memberValue_annotations() {
		parseMemberValue("anno0 anno1 Integer i;",      ((AstMemberValueDeclaration memberValue) -> {
			assertThat(memberValue.getNameString(), equalTo("i") );
			assertThat(memberValue.getApiInitialValue().isPresent(), equalTo(false) );
			assertThat(memberValue.getType(), instanceOf(SimpleType.class));
			assertThat(((SimpleType)memberValue.getType()).getNameString(), equalTo("Integer"));

			assertThat(memberValue.getAnnotations().stream()
					.map((Annotation anno) -> anno.getName()
					.getText()) 
					.toArray(), 
					is(new String[] {"anno0", "anno1"} ));

		}) );
	}

}
