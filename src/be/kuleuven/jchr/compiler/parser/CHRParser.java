// $ANTLR : "chr.g" -> "CHRParser.java"$

package be.kuleuven.jchr.compiler.parser;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormDirector;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.PROPAGATION;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.SIMPAGATION;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.SIMPLIFICATION;

import be.kuleuven.jchr.util.builder.BuilderException;

import org.kohsuke.args4j.UndefinedOptionException;
import org.kohsuke.args4j.IllegalOptionParameterException;

public final class CHRParser extends antlr.LLkParser       implements CHRTokenTypes
, ICHRIntermediateFormDirector {

	public CHRParser(TokenBuffer tokenBuf, ICHRIntermediateFormBuilder<?> builder, Options opts) {
		super(tokenBuf, 1);
		tokenNames = _tokenNames;
		init(builder, opts);
	}
	
	public CHRParser(TokenStream tokenStr, ICHRIntermediateFormBuilder<?> builder, Options opts) {
		super(tokenStr, 1);
		tokenNames = _tokenNames;
		init(builder, opts);
	}
	
	public void init(ICHRIntermediateFormBuilder<?> builder, Options opts) {
		setBuilder(builder);
		setOptions(opts);
	}
	
	private Options opts;
	
	protected void setOptions(Options opts) {
		this.opts = opts;	
	}
	
	protected Options getOptions() {
		return opts;	
	}
	
	private ICHRIntermediateFormBuilder<?> builder;
	
	public void setBuilder(ICHRIntermediateFormBuilder<?> builder) {
		this.builder = builder;
	}
	
	public ICHRIntermediateFormBuilder<?> getBuilder() {
		return builder;	
	}	
	
	public final void construct() throws BuilderException {
		if (getBuilder() == null) {
			throw new BuilderException("Builder not set");
		}
		
		getBuilder().init();
		
		try {
			imports();			
			handler();
		} catch (RecognitionException re) {
			abort(re);
		} catch (TokenStreamException tse) {
		 	abort(tse);
		} catch (UndefinedOptionException uoe) {
			abort(uoe);
		} catch (IllegalOptionParameterException iope) {
			abort(iope);
		}
				
		getBuilder().finish();
	}
	
	protected void abort(Exception reason) throws BuilderException {
		getBuilder().abort();
		throw new BuilderException(reason);
	}
			
	/**
	 * Bepaalt het type van een regel. Dit is duidelijk een hack,
	 * en we kunnen perfect zonder, maar kom, heb't nu gemaakt
	 * en ik laat het alvast voorlopig staan...
	 *
	 * @see ruledef[String name]
	 * @note Kan zijn dat het niet meer werkt in latere versies
	 *  van ANTLR, en dan zal de alternatieve versie van ruledef
	 *  moeten worden gebruikt (staat in commentaar), die iets
	 *  minder effici?nt is (moet telkens 2 keer backtracken)
	 *	en zoals ze er nu staat ook minder robuust...
     */	
/*	protected final byte getRuleType() 
	throws RecognitionException, TokenStreamException, BuilderException {
		final int mark = mark();
		int type = -1;
		
		inputState.guessing++;
		try {
			occurrenceList();
			type = LA(1);			
			}
		catch (RecognitionException re) {
			re.printStackTrace(System.err);
		}
		rewind(mark);
		inputState.guessing--;

		switch(type) {
			case MINUS:	return SIMPAGATION;
			case PROP:	return PROPAGATION;
			case SIMP:	return SIMPLIFICATION;
			default:
				throw new BuilderException("Unable to determine rule-type");
		}
	}*/

protected CHRParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public CHRParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected CHRParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public CHRParser(TokenStream lexer) {
  this(lexer,2);
}

public CHRParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	protected final void option() throws RecognitionException, TokenStreamException, UndefinedOptionException,IllegalOptionParameterException {
		
		String optionName;
		
		try {      // for error handling
			match(OPTION);
			match(LPAREN);
			optionName=simpleID();
			match(COMMA);
			{
			switch ( LA(1)) {
			case ON:
			case TRUE:
			case OFF:
			case FALSE:
			{
				booleanOption(optionName);
				break;
			}
			case STRING_LITERAL:
			{
				stringOption(optionName);
				break;
			}
			case NUM_INT:
			{
				intOption(optionName);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RPAREN);
			match(SEMICOLON);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final String  simpleID() throws RecognitionException, TokenStreamException {
		String result;
		
		Token  id = null;
		result = null;
		
		try {      // for error handling
			id = LT(1);
			match(SIMPLE_ID);
			if ( inputState.guessing==0 ) {
				result = id.getText();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_1);
			} else {
			  throw ex;
			}
		}
		return result;
	}
	
	protected final void booleanOption(
		String optionName
	) throws RecognitionException, TokenStreamException, UndefinedOptionException,IllegalOptionParameterException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case ON:
			case TRUE:
			{
				{
				{
				switch ( LA(1)) {
				case ON:
				{
					match(ON);
					break;
				}
				case TRUE:
				{
					match(TRUE);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					getOptions().setOption(optionName, true);
				}
				}
				break;
			}
			case OFF:
			case FALSE:
			{
				{
				{
				switch ( LA(1)) {
				case OFF:
				{
					match(OFF);
					break;
				}
				case FALSE:
				{
					match(FALSE);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				if ( inputState.guessing==0 ) {
					getOptions().setOption(optionName, false);
				}
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void stringOption(
		String optionName
	) throws RecognitionException, TokenStreamException, UndefinedOptionException,IllegalOptionParameterException {
		
		Token  s = null;
		
		try {      // for error handling
			s = LT(1);
			match(STRING_LITERAL);
			if ( inputState.guessing==0 ) {
				getOptions().setOption(optionName, s.getText());
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void intOption(
		String optionName
	) throws RecognitionException, TokenStreamException, UndefinedOptionException,IllegalOptionParameterException {
		
		Token  s = null;
		
		try {      // for error handling
			s = LT(1);
			match(NUM_INT);
			if ( inputState.guessing==0 ) {
				getOptions().setOption(optionName, Integer.valueOf(s.getText()));
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void declpkg() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			{
			match(PACKAGE);
			{
			declarePackage();
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void declarePackage() throws RecognitionException, TokenStreamException, BuilderException {
		
		String pkg;
		
		try {      // for error handling
			pkg=anID();
			match(SEMICOLON);
			if ( inputState.guessing==0 ) {
				getBuilder().beginEndPackage(pkg);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void imports() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				getBuilder().beginClassDeclarations();
			}
			{
			{
			switch ( LA(1)) {
			case PACKAGE:
			{
				declpkg();
				break;
			}
			case EOF:
			case IMPORT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop264:
			do {
				if ((LA(1)==IMPORT)) {
					match(IMPORT);
					{
					if ((LA(1)==SIMPLE_ID||LA(1)==ID) && (LA(2)==SEMICOLON)) {
						importClass();
					}
					else if ((LA(1)==SIMPLE_ID||LA(1)==ID) && (LA(2)==DOT)) {
						importPackage();
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
				}
				else {
					break _loop264;
				}
				
			} while (true);
			}
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endClassDeclarations();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void importClass() throws RecognitionException, TokenStreamException, BuilderException {
		
		String id;
		
		try {      // for error handling
			id=anID();
			match(SEMICOLON);
			if ( inputState.guessing==0 ) {
				getBuilder().importClass(id);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void importPackage() throws RecognitionException, TokenStreamException, BuilderException {
		
		String id;
		
		try {      // for error handling
			id=anID();
			match(DOT);
			match(STAR);
			match(SEMICOLON);
			if ( inputState.guessing==0 ) {
				getBuilder().importPackage(id);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_3);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void handler() throws RecognitionException, TokenStreamException, BuilderException,UndefinedOptionException,IllegalOptionParameterException {
		
		Token  handler = null;
		
		try {      // for error handling
			match(HANDLER);
			handler = LT(1);
			match(SIMPLE_ID);
			if ( inputState.guessing==0 ) {
				getBuilder().beginHandler(handler.getText());
			}
			{
			switch ( LA(1)) {
			case LT:
			{
				typeParameters();
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(LCURLY);
			solverDeclarations();
			constraintDeclarations();
			{
			_loop268:
			do {
				if ((LA(1)==OPTION)) {
					option();
				}
				else {
					break _loop268;
				}
				
			} while (true);
			}
			{
			ruleDefinitions();
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				getBuilder().endHandler();
			}
			match(Token.EOF_TYPE);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void typeParameters() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			match(LT);
			if ( inputState.guessing==0 ) {
				getBuilder().beginTypeParameters();
			}
			{
			switch ( LA(1)) {
			case SIMPLE_ID:
			{
				typeParameter();
				{
				_loop285:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						typeParameter();
					}
					else {
						break _loop285;
					}
					
				} while (true);
				}
				break;
			}
			case GT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(GT);
			if ( inputState.guessing==0 ) {
				getBuilder().endTypeParameters();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_5);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void solverDeclarations() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				getBuilder().beginSolverDeclarations();
			}
			{
			_loop276:
			do {
				if ((LA(1)==SOLVER)) {
					solverDeclaration();
				}
				else {
					break _loop276;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endSolverDeclarations();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_6);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void constraintDeclarations() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				getBuilder().beginConstraintDeclarations();
			}
			{
			_loop279:
			do {
				switch ( LA(1)) {
				case CONSTRAINT:
				{
					constraintDeclaration();
					break;
				}
				case CONSTRAINTS:
				{
					constraintsDeclaration();
					break;
				}
				default:
				{
					break _loop279;
				}
				}
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endConstraintDeclarations();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_0);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void ruleDefinitions() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			match(RULES);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				getBuilder().beginRules();
			}
			{
			_loop317:
			do {
				switch ( LA(1)) {
				case VARIABLE:
				{
					variableDeclarations();
					break;
				}
				case TRUE:
				case FALSE:
				case STRING_LITERAL:
				case NUM_INT:
				case SIMPLE_ID:
				case ID:
				case CHAR_LITERAL:
				case NUM_FLOAT:
				case NUM_LONG:
				case NUM_DOUBLE:
				case NULL:
				{
					rule();
					break;
				}
				default:
				{
					break _loop317;
				}
				}
			} while (true);
			}
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				getBuilder().endRules();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_7);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void typeParameter() throws RecognitionException, TokenStreamException, BuilderException {
		
		String sid;
		
		try {      // for error handling
			sid=simpleID();
			if ( inputState.guessing==0 ) {
				getBuilder().beginTypeParameter(sid);
			}
			{
			switch ( LA(1)) {
			case EXTENDS:
			{
				match(EXTENDS);
				upperBound();
				{
				_loop273:
				do {
					if ((LA(1)==AMPERCENT)) {
						match(AMPERCENT);
						upperBound();
					}
					else {
						break _loop273;
					}
					
				} while (true);
				}
				break;
			}
			case COMMA:
			case GT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endTypeParameter();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void upperBound() throws RecognitionException, TokenStreamException, BuilderException {
		
		String id;
		
		try {      // for error handling
			id=anID();
			if ( inputState.guessing==0 ) {
				getBuilder().beginUpperBound(id);
			}
			{
			switch ( LA(1)) {
			case LT:
			{
				typeArguments();
				break;
			}
			case COMMA:
			case AMPERCENT:
			case GT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endUpperBound();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_9);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void solverDeclaration() throws RecognitionException, TokenStreamException, BuilderException {
		
		String interf, id = null;
		
		try {      // for error handling
			match(SOLVER);
			interf=anID();
			if ( inputState.guessing==0 ) {
				getBuilder().beginSolverDeclaration(interf);
			}
			{
			switch ( LA(1)) {
			case LT:
			{
				typeArguments();
				break;
			}
			case SEMICOLON:
			case SIMPLE_ID:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case SIMPLE_ID:
			{
				id=simpleID();
				break;
			}
			case SEMICOLON:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				getBuilder().buildSolverName(id);
			}
			match(SEMICOLON);
			if ( inputState.guessing==0 ) {
				getBuilder().endSolverDeclaration();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_10);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void constraintDeclaration() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			match(CONSTRAINT);
			constraintDeclarationBody();
			match(SEMICOLON);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_6);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void constraintsDeclaration() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			match(CONSTRAINTS);
			constraintDeclarationBody();
			{
			_loop306:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					constraintDeclarationBody();
				}
				else {
					break _loop306;
				}
				
			} while (true);
			}
			match(SEMICOLON);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_6);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final String  anID() throws RecognitionException, TokenStreamException {
		String result;
		
		result = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ID:
			{
				result=composedID();
				break;
			}
			case SIMPLE_ID:
			{
				result=simpleID();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_11);
			} else {
			  throw ex;
			}
		}
		return result;
	}
	
	protected final void typeArguments() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				getBuilder().beginTypeArguments();
			}
			match(LT);
			typeArgument();
			{
			_loop297:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					typeArgument();
				}
				else {
					break _loop297;
				}
				
			} while (true);
			}
			match(GT);
			if ( inputState.guessing==0 ) {
				getBuilder().endTypeArguments();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_12);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final String  composedID() throws RecognitionException, TokenStreamException {
		String result;
		
		Token  id = null;
		result = null;
		
		try {      // for error handling
			id = LT(1);
			match(ID);
			if ( inputState.guessing==0 ) {
				result = id.getText();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		return result;
	}
	
	protected final void typeArgument() throws RecognitionException, TokenStreamException, BuilderException {
		
		String id;
		
		try {      // for error handling
			id=anID();
			if ( inputState.guessing==0 ) {
				getBuilder().beginTypeArgument(id);
			}
			{
			switch ( LA(1)) {
			case LT:
			{
				typeParameters();
				break;
			}
			case COMMA:
			case GT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endTypeArgument();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_8);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void constraintDeclarationBody() throws RecognitionException, TokenStreamException, BuilderException {
		
		Token  id = null;
		String infix;
		
		try {      // for error handling
			id = LT(1);
			match(SIMPLE_ID);
			if ( inputState.guessing==0 ) {
				getBuilder().beginConstraintDeclaration(id.getText());
			}
			match(LPAREN);
			constraintArgumentList();
			match(RPAREN);
			{
			switch ( LA(1)) {
			case INFIX:
			{
				match(INFIX);
				infix=infixId();
				{
				}
				if ( inputState.guessing==0 ) {
					getBuilder().buildInfixIdentifier(infix);
				}
				break;
			}
			case COMMA:
			case SEMICOLON:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endConstraintDeclaration();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void constraintArgumentList() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case SIMPLE_ID:
			case ID:
			case FIXED:
			{
				constraintArgument();
				break;
			}
			case COMMA:
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop310:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					constraintArgument();
				}
				else {
					break _loop310;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final String  infixId() throws RecognitionException, TokenStreamException, BuilderException {
		String result;
		
		Token  ud = null;
		result = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case EQ:
			{
				match(EQ);
				if ( inputState.guessing==0 ) {
					result = "=" ;
				}
				break;
			}
			case EQEQ:
			{
				match(EQEQ);
				if ( inputState.guessing==0 ) {
					result = "==";
				}
				break;
			}
			case LT:
			{
				match(LT);
				if ( inputState.guessing==0 ) {
					result = "<" ;
				}
				break;
			}
			case GT:
			{
				match(GT);
				if ( inputState.guessing==0 ) {
					result = ">" ;
				}
				break;
			}
			case LEQ:
			{
				match(LEQ);
				if ( inputState.guessing==0 ) {
					result = "<=";
				}
				break;
			}
			case QEL:
			{
				match(QEL);
				if ( inputState.guessing==0 ) {
					result = "<=";
				}
				break;
			}
			case GEQ:
			{
				match(GEQ);
				if ( inputState.guessing==0 ) {
					result = ">=";
				}
				break;
			}
			case NEQ:
			{
				match(NEQ);
				if ( inputState.guessing==0 ) {
					result = "!=";
				}
				break;
			}
			case INFIX_ID:
			{
				ud = LT(1);
				match(INFIX_ID);
				if ( inputState.guessing==0 ) {
					result = ud.getText();
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_15);
			} else {
			  throw ex;
			}
		}
		return result;
	}
	
	protected final void constraintArgument() throws RecognitionException, TokenStreamException, BuilderException {
		
		
			String type, name = null; 
			boolean fixed = false;
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case FIXED:
			{
				match(FIXED);
				if ( inputState.guessing==0 ) {
					fixed = true;
				}
				break;
			}
			case SIMPLE_ID:
			case ID:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			type=anID();
			if ( inputState.guessing==0 ) {
				
						getBuilder().beginConstraintArgument(); 
						getBuilder().beginConstraintArgumentType(type, fixed);
					
			}
			{
			switch ( LA(1)) {
			case LT:
			{
				typeArguments();
				break;
			}
			case COMMA:
			case RPAREN:
			case SIMPLE_ID:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endConstraintArgumentType();
			}
			{
			switch ( LA(1)) {
			case SIMPLE_ID:
			{
				name=simpleID();
				break;
			}
			case COMMA:
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
						getBuilder().buildConstraintArgumentName(name);
						getBuilder().endConstraintArgument();
					
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_16);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void variableDeclarations() throws RecognitionException, TokenStreamException, BuilderException {
		
		String type = null; boolean fixed = false;
		
		try {      // for error handling
			match(VARIABLE);
			{
			switch ( LA(1)) {
			case FIXED:
			{
				match(FIXED);
				if ( inputState.guessing==0 ) {
					fixed = true;
				}
				break;
			}
			case SIMPLE_ID:
			case ID:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			type=anID();
			if ( inputState.guessing==0 ) {
				
						getBuilder().beginVariableDeclarations(); 
						getBuilder().beginVariableType(type, fixed);
					
			}
			{
			switch ( LA(1)) {
			case LT:
			{
				typeArguments();
				break;
			}
			case SIMPLE_ID:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endVariableType();
			}
			variableDeclaration();
			{
			_loop322:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					variableDeclaration();
				}
				else {
					break _loop322;
				}
				
			} while (true);
			}
			match(SEMICOLON);
			if ( inputState.guessing==0 ) {
				getBuilder().endVariableDeclarations();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_17);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void rule() throws RecognitionException, TokenStreamException, BuilderException {
		
		Token  id = null;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				getBuilder().beginRule();
			}
			{
			boolean synPredMatched327 = false;
			if (((LA(1)==SIMPLE_ID) && (LA(2)==AT))) {
				int _m327 = mark();
				synPredMatched327 = true;
				inputState.guessing++;
				try {
					{
					match(SIMPLE_ID);
					match(AT);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched327 = false;
				}
				rewind(_m327);
inputState.guessing--;
			}
			if ( synPredMatched327 ) {
				id = LT(1);
				match(SIMPLE_ID);
				match(AT);
				ruleDefinition(id.getText());
			}
			else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2)))) {
				ruleDefinition(null);
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			switch ( LA(1)) {
			case PRAGMA:
			{
				pragmas();
				break;
			}
			case SEMICOLON:
			case DOT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			ruleEnd();
			if ( inputState.guessing==0 ) {
				getBuilder().endRule();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_17);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void variableDeclaration() throws RecognitionException, TokenStreamException, BuilderException {
		
		String id;
		
		try {      // for error handling
			id=simpleID();
			if ( inputState.guessing==0 ) {
				getBuilder().declareVariable(id);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_14);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void ruleDefinition(
		String name
	) throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			boolean synPredMatched331 = false;
			if (((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2))))) {
				int _m331 = mark();
				synPredMatched331 = true;
				inputState.guessing++;
				try {
					{
					occurrenceList();
					match(MINUS);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched331 = false;
				}
				rewind(_m331);
inputState.guessing--;
			}
			if ( synPredMatched331 ) {
				simpagationDef(name);
			}
			else {
				boolean synPredMatched333 = false;
				if (((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2))))) {
					int _m333 = mark();
					synPredMatched333 = true;
					inputState.guessing++;
					try {
						{
						occurrenceList();
						match(SIMP);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched333 = false;
					}
					rewind(_m333);
inputState.guessing--;
				}
				if ( synPredMatched333 ) {
					simplificationDef(name);
				}
				else if ((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2)))) {
					propagationDef(name);
					if ( inputState.guessing==0 ) {
						getBuilder().endRuleDefinition();
					}
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
			}
			catch (RecognitionException ex) {
				if (inputState.guessing==0) {
					reportError(ex);
					recover(ex,_tokenSet_20);
				} else {
				  throw ex;
				}
			}
		}
		
	protected final void pragmas() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			match(PRAGMA);
			if ( inputState.guessing==0 ) {
				getBuilder().beginPragmas();
			}
			pragma();
			{
			_loop336:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					pragma();
				}
				else {
					break _loop336;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endPragmas();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_21);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void ruleEnd() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case SEMICOLON:
			{
				match(SEMICOLON);
				break;
			}
			case DOT:
			{
				match(DOT);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_17);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void occurrenceList() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			occurrence();
			{
			_loop356:
			do {
				if ((LA(1)==COMMA||LA(1)==AND)) {
					and();
					occurrence();
				}
				else {
					break _loop356;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_22);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void simpagationDef(
		String name
	) throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				
						getBuilder().beginRuleDefinition(name, SIMPAGATION);
						getBuilder().beginHead(); 
					
			}
			keptOccurrences();
			match(MINUS);
			removedOccurrences();
			if ( inputState.guessing==0 ) {
					getBuilder().endHead();
			}
			match(SIMP);
			guardNbody();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_20);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void simplificationDef(
		String name
	) throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				
						getBuilder().beginRuleDefinition(name, SIMPLIFICATION);
						getBuilder().beginHead(); 
					
			}
			removedOccurrences();
			if ( inputState.guessing==0 ) {
				getBuilder().endHead();
			}
			match(SIMP);
			guardNbody();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_20);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void propagationDef(
		String name
	) throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				
						getBuilder().beginRuleDefinition(name, PROPAGATION);
						getBuilder().beginHead(); 
					
			}
			keptOccurrences();
			if ( inputState.guessing==0 ) {
				getBuilder().endHead();
			}
			match(PROP);
			guardNbody();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_20);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void pragma() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			passivePragma();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_23);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void passivePragma() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			match(PASSIVE);
			match(LPAREN);
			passivePragmaId();
			{
			_loop340:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					passivePragmaId();
				}
				else {
					break _loop340;
				}
				
			} while (true);
			}
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_23);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void passivePragmaId() throws RecognitionException, TokenStreamException, BuilderException {
		
		String id;
		
		try {      // for error handling
			id=simpleID();
			if ( inputState.guessing==0 ) {
				getBuilder().addPassivePragma(id);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_16);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void keptOccurrences() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				getBuilder().beginKeptOccurrences();
			}
			occurrenceList();
			if ( inputState.guessing==0 ) {
				getBuilder().endKeptOccurrences();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_24);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void removedOccurrences() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				getBuilder().beginRemovedOccurrences();
			}
			occurrenceList();
			if ( inputState.guessing==0 ) {
				getBuilder().endRemovedOccurrences();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_25);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void guardNbody() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			{
			boolean synPredMatched353 = false;
			if (((_tokenSet_26.member(LA(1))) && (_tokenSet_27.member(LA(2))))) {
				int _m353 = mark();
				synPredMatched353 = true;
				inputState.guessing++;
				try {
					{
					conjunctList();
					match(VERTLINE);
					}
				}
				catch (RecognitionException pe) {
					synPredMatched353 = false;
				}
				rewind(_m353);
inputState.guessing--;
			}
			if ( synPredMatched353 ) {
				if ( inputState.guessing==0 ) {
					getBuilder().beginGuard();
				}
				conjunctList();
				match(VERTLINE);
				if ( inputState.guessing==0 ) {
					getBuilder().endGuard();
				}
			}
			else if ((_tokenSet_26.member(LA(1))) && (_tokenSet_28.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				getBuilder().beginBody();
			}
			conjunctList();
			if ( inputState.guessing==0 ) {
				getBuilder().endBody();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_20);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void fieldAccessConjunct() throws RecognitionException, TokenStreamException, BuilderException {
		
		String id;
		
		try {      // for error handling
			id=composedID();
			if ( inputState.guessing==0 ) {
				getBuilder().addFieldAccessConjunct(id);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void identifiedConjunct() throws RecognitionException, TokenStreamException, BuilderException {
		
		String id;
		
		try {      // for error handling
			id=anID();
			if ( inputState.guessing==0 ) {
				getBuilder().beginIdentifiedConjunct(id);
			}
			match(LPAREN);
			arglist();
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				getBuilder().endIdentifiedConjunct();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_29);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void arglist() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				getBuilder().beginArguments();
			}
			{
			switch ( LA(1)) {
			case TRUE:
			case FALSE:
			case STRING_LITERAL:
			case NUM_INT:
			case SIMPLE_ID:
			case ID:
			case CHAR_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			case NULL:
			{
				argument();
				break;
			}
			case COMMA:
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop380:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					argument();
				}
				else {
					break _loop380;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				getBuilder().endArguments();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_2);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void userDefinedConstraint() throws RecognitionException, TokenStreamException, BuilderException {
		
		String id;
		
		try {      // for error handling
			id=simpleID();
			if ( inputState.guessing==0 ) {
				getBuilder().beginUserDefinedConstraint(id);
			}
			match(LPAREN);
			arglist();
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				getBuilder().endUserDefinedConstraint();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_30);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void conjunctList() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			conjunct();
			{
			_loop364:
			do {
				if ((LA(1)==COMMA||LA(1)==AND)) {
					and();
					conjunct();
				}
				else {
					break _loop364;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_31);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void occurrence() throws RecognitionException, TokenStreamException, BuilderException {
		
		String id;
		
		try {      // for error handling
			{
			boolean synPredMatched360 = false;
			if (((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2))))) {
				int _m360 = mark();
				synPredMatched360 = true;
				inputState.guessing++;
				try {
					{
					argument();
					infixId();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched360 = false;
				}
				rewind(_m360);
inputState.guessing--;
			}
			if ( synPredMatched360 ) {
				infixConstraint();
			}
			else if ((LA(1)==SIMPLE_ID) && (LA(2)==LPAREN)) {
				userDefinedConstraint();
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			switch ( LA(1)) {
			case NUMBER_SIGN:
			{
				match(NUMBER_SIGN);
				id=simpleID();
				if ( inputState.guessing==0 ) {
					getBuilder().buildOccurrenceId(id);
				}
				break;
			}
			case COMMA:
			case MINUS:
			case SIMP:
			case PROP:
			case AND:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_32);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void and() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case AND:
			{
				match(AND);
				break;
			}
			case COMMA:
			{
				match(COMMA);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_26);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void argument() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case SIMPLE_ID:
			{
				identifiedArgument();
				break;
			}
			case TRUE:
			case FALSE:
			case STRING_LITERAL:
			case NUM_INT:
			case CHAR_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			case NULL:
			{
				constantArgument();
				break;
			}
			default:
				if ((LA(1)==ID) && (LA(2)==LPAREN)) {
					methodInvocationArgument();
				}
				else if ((LA(1)==ID) && (_tokenSet_33.member(LA(2)))) {
					fieldAccessArgument();
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_33);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void infixConstraint() throws RecognitionException, TokenStreamException, BuilderException {
		
		String infix;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				getBuilder().beginInfixConstraint();
			}
			argument();
			infix=infixId();
			{
			}
			if ( inputState.guessing==0 ) {
				getBuilder().buildInfix(infix);
			}
			argument();
			if ( inputState.guessing==0 ) {
				getBuilder().endInfixConstraint();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_34);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void conjunct() throws RecognitionException, TokenStreamException, BuilderException {
		
		
		try {      // for error handling
			boolean synPredMatched367 = false;
			if (((_tokenSet_18.member(LA(1))) && (_tokenSet_19.member(LA(2))))) {
				int _m367 = mark();
				synPredMatched367 = true;
				inputState.guessing++;
				try {
					{
					argument();
					infixId();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched367 = false;
				}
				rewind(_m367);
inputState.guessing--;
			}
			if ( synPredMatched367 ) {
				{
				infixConstraint();
				}
			}
			else if ((_tokenSet_35.member(LA(1))) && (_tokenSet_36.member(LA(2)))) {
				{
				switch ( LA(1)) {
				case SIMPLE_ID:
				case ID:
				{
					identifiedConjunct();
					break;
				}
				case TRUE:
				{
					match(TRUE);
					break;
				}
				case FALSE:
				case FAIL:
				{
					{
					switch ( LA(1)) {
					case FALSE:
					{
						match(FALSE);
						break;
					}
					case FAIL:
					{
						match(FAIL);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					if ( inputState.guessing==0 ) {
						getBuilder().addFailureConjunct();
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_29);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void methodInvocationConjunct() throws RecognitionException, TokenStreamException, BuilderException {
		
		Token  id = null;
		
		try {      // for error handling
			id = LT(1);
			match(ID);
			if ( inputState.guessing==0 ) {
				getBuilder().beginMethodInvocationConjunct(id.getText());
			}
			match(LPAREN);
			arglist();
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				getBuilder().endMethodInvocationConjunct();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void identifiedArgument() throws RecognitionException, TokenStreamException, BuilderException {
		
		Token  id = null;
		
		try {      // for error handling
			id = LT(1);
			match(SIMPLE_ID);
			if ( inputState.guessing==0 ) {
				getBuilder().addIdentifiedArgument(id.getText());
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_33);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void constantArgument() throws RecognitionException, TokenStreamException, BuilderException {
		
		Token  s = null;
		Token  c = null;
		Token  i = null;
		Token  fl = null;
		Token  l = null;
		Token  d = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING_LITERAL:
			{
				s = LT(1);
				match(STRING_LITERAL);
				if ( inputState.guessing==0 ) {
					getBuilder().addConstantArgument(s.getText());
				}
				break;
			}
			case CHAR_LITERAL:
			{
				c = LT(1);
				match(CHAR_LITERAL);
				if ( inputState.guessing==0 ) {
					getBuilder().addConstantArgument(c.getText().charAt(0));
				}
				break;
			}
			case NUM_INT:
			{
				i = LT(1);
				match(NUM_INT);
				if ( inputState.guessing==0 ) {
					getBuilder().addConstantArgument(Integer.valueOf(i.getText()).intValue());
				}
				break;
			}
			case NUM_FLOAT:
			{
				fl = LT(1);
				match(NUM_FLOAT);
				if ( inputState.guessing==0 ) {
					getBuilder().addConstantArgument(Float.valueOf(fl.getText()).floatValue());
				}
				break;
			}
			case NUM_LONG:
			{
				l = LT(1);
				match(NUM_LONG);
				if ( inputState.guessing==0 ) {
					getBuilder().addConstantArgument(Long.valueOf(l.getText()).longValue());
				}
				break;
			}
			case NUM_DOUBLE:
			{
				d = LT(1);
				match(NUM_DOUBLE);
				if ( inputState.guessing==0 ) {
					getBuilder().addConstantArgument(Double.valueOf(d.getText()).doubleValue());
				}
				break;
			}
			case TRUE:
			{
				match(TRUE);
				if ( inputState.guessing==0 ) {
					getBuilder().addConstantArgument(true);
				}
				break;
			}
			case FALSE:
			{
				match(FALSE);
				if ( inputState.guessing==0 ) {
					getBuilder().addConstantArgument(false);
				}
				break;
			}
			case NULL:
			{
				match(NULL);
				if ( inputState.guessing==0 ) {
					getBuilder().addConstantArgument();
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_33);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void methodInvocationArgument() throws RecognitionException, TokenStreamException, BuilderException {
		
		Token  id = null;
		
		try {      // for error handling
			id = LT(1);
			match(ID);
			if ( inputState.guessing==0 ) {
				getBuilder().beginMethodInvocationArgument(id.getText());
			}
			match(LPAREN);
			arglist();
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				getBuilder().endMethodInvocationArgument();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_33);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void fieldAccessArgument() throws RecognitionException, TokenStreamException, BuilderException {
		
		Token  id = null;
		
		try {      // for error handling
			id = LT(1);
			match(ID);
			if ( inputState.guessing==0 ) {
				getBuilder().addFieldAccessArgument(id.getText());
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_33);
			} else {
			  throw ex;
			}
		}
	}
	
	protected final void constructorInvocationArgument() throws RecognitionException, TokenStreamException, BuilderException {
		
		Token  id = null;
		
		try {      // for error handling
			match(NEW);
			id = LT(1);
			match(ID);
			if ( inputState.guessing==0 ) {
				getBuilder().beginConstructorInvocationArgument(id.getText());
			}
			{
			switch ( LA(1)) {
			case LT:
			{
				typeArguments();
				break;
			}
			case LPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(LPAREN);
			arglist();
			match(RPAREN);
			if ( inputState.guessing==0 ) {
				getBuilder().endConstructorInvocationArgument();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				recover(ex,_tokenSet_4);
			} else {
			  throw ex;
			}
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"TYPECAST",
		"METHODORCONSTRAINT",
		"EMPTY",
		"\"option\"",
		"LPAREN",
		"COMMA",
		"RPAREN",
		"SEMICOLON",
		"\"on\"",
		"\"true\"",
		"\"off\"",
		"\"false\"",
		"a string",
		"a numerical",
		"\"package\"",
		"\"import\"",
		"\"handler\"",
		"an identifer",
		"LCURLY",
		"RCURLY",
		"\"extends\"",
		"AMPERCENT",
		"LT",
		"GT",
		"an identifer",
		"DOT",
		"STAR",
		"\"solver\"",
		"\"infix\"",
		"\"constraint\"",
		"\"constraints\"",
		"FIXED",
		"\"rules\"",
		"\"variable\"",
		"AT",
		"MINUS",
		"SIMP",
		"\"pragma\"",
		"\"passive\"",
		"PROP",
		"VERTLINE",
		"NUMBER_SIGN",
		"\"fail\"",
		"EQ",
		"EQEQ",
		"LEQ",
		"QEL",
		"GEQ",
		"NEQ",
		"INFIX_ID",
		"AND",
		"a character",
		"NUM_FLOAT",
		"NUM_LONG",
		"NUM_DOUBLE",
		"\"null\"",
		"\"new\"",
		"ELLIPSIS",
		"WS",
		"SL_COMMENT",
		"ML_COMMENT",
		"ESC",
		"HEX_DIGIT",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 68719476864L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 18024844660576000L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 1024L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 524290L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 138412544L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 94489280640L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 8388608L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 134218240L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 167772672L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 96636764288L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 773852928L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 169873152L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 773852930L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 2560L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 1116892707858655744L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 1536L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 1116892845305995264L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 1116892707858653184L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 17873661222453504L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 2199560128512L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 536872960L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 10445360463872L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 536873472L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = { 9345848836096L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { 1099511627776L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = { 1116963076602830848L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 35905651917980416L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = { 35890259292064512L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { 18034190255655424L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { 18060028242035200L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { 19791746172928L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { 18024843869946368L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = { 35953481210662400L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { 18079819988208128L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = { 70369014751232L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	private static final long[] mk_tokenSet_36() {
		long[] data = { 18034190255655680L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
	
	}
