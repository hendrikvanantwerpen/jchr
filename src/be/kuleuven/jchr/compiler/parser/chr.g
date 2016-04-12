// TODO deze commentaar stamt nog van een zeeeeeeer oude versie ;-)
/*
 * @author Peter Van Weert
 *
 * Wijzigingen / verbeteringen tov JCHR:
 *  o Gegenereerde klassen zullen in het juiste pakket zitten,
 *		niet langer in default (dit is niet echt concrete 
 *		syntax-verbetering) 
 *
 *	o Door gebruik te maken van een aantal lexer-constructies 
 *		uit de nieuwste antlr-java-language-definitie zijn een
 *		aantal dingen verbeterd ten opzichte van JCHR:
 *			- string- en character-literals kunnen 
 *				escaped characters bevatten (ook \" of \' dus!)
 *			- herkennen van numerische literals is er 
 *				sterk op vooruitgegaan
 *
 *	o Door het gebruiken van een grotere charVocabulary kunnen
 *		strings, commentaar, etc speciale tekens bevatten 
 *		zoals |, letters met accenten, etc
 *
 *	o Door de herdefinitie van de syntax van een regel is deze
 *		nu veel sterker gelijkend op die van de gebruikelijke.
 *		De reden dat dit niet aanvankelijk zo werd gedaan zou
 *		iets te maken kunnen hebben met het feit dat dit de 
 *		syntax-definitie wel wat bemoeilijkt, omdat een aantal
 *		syntactic predicates moeten ingevoerd om ambiguiteiten
 *		weg te werken. De "offici?le" reden om het meer java-
 *		like te maken lijkt ons niet overtuigend:
 *			- bemoeilijkt sterk het lezen van de regels
 *				als men de normale syntax gewoon is
 *			- bemoeilijkt het porten van CHR-programma's
 *
 * o Regels zonder naam krijgen in parsing-fase een naam
 *		rule_<teller>, zodat deze achteraf op net dezelfde
 *		wijze kunnen worden behandeld.
 */
header {
package be.kuleuven.jchr.compiler.parser;
}

{
import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormBuilder;
import be.kuleuven.jchr.compiler.CHRIntermediateForm.builder.ICHRIntermediateFormDirector;

import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.PROPAGATION;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.SIMPAGATION;
import static be.kuleuven.jchr.compiler.CHRIntermediateForm.rules.RuleType.SIMPLIFICATION;

import be.kuleuven.jchr.util.builder.BuilderException;

import org.kohsuke.args4j.UndefinedOptionException;
import org.kohsuke.args4j.IllegalOptionParameterException;
}

class CHRParser extends Parser;

options {
	k=2;
	exportVocab=CHR;
	
	noConstructors=true;
	classHeaderPrefix="public final";
	classHeaderSuffix="ICHRIntermediateFormDirector";
}

tokens {
	TYPECAST;
	METHODORCONSTRAINT;	
	EMPTY;
}

{
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
}

protected option throws UndefinedOptionException, IllegalOptionParameterException
{String optionName; } :
	OPTION LPAREN optionName=simpleID COMMA
		(booleanOption[optionName] | stringOption[optionName] | intOption[optionName])
	RPAREN SEMICOLON
;

protected booleanOption[String optionName] 
	throws UndefinedOptionException, IllegalOptionParameterException
:
	((ON | TRUE) { getOptions().setOption(optionName, true); })
	|
	((OFF | FALSE) { getOptions().setOption(optionName, false); })
;

protected stringOption[String optionName] 
	throws UndefinedOptionException, IllegalOptionParameterException
:
	s:STRING_LITERAL { getOptions().setOption(optionName, s.getText()); }
;

protected intOption[String optionName] 
	throws UndefinedOptionException, IllegalOptionParameterException
:
	s:NUM_INT { getOptions().setOption(optionName, Integer.valueOf(s.getText())); }
;

protected declpkg throws BuilderException
:
( PACKAGE (declarePackage) )
;

protected imports throws BuilderException
:
	{ getBuilder().beginClassDeclarations(); }
	( (declpkg)?
	  (IMPORT (importClass | importPackage) )* )
	{ getBuilder().endClassDeclarations(); }
;


protected handler 
	throws BuilderException, UndefinedOptionException, IllegalOptionParameterException
:	
	HANDLER handler:SIMPLE_ID 
			{ getBuilder().beginHandler(handler.getText()); }

			(typeParameters)?
				
		LCURLY
			solverDeclarations 
			
			constraintDeclarations
			
			( option )*
		
			( ruleDefinitions )
		RCURLY
		
		{ getBuilder().endHandler(); }
		EOF
;

protected typeParameter throws BuilderException
{String sid; }
:
	 sid=simpleID 
	 { getBuilder().beginTypeParameter(sid); }
	 (EXTENDS upperBound (AMPERCENT upperBound)*)?
	 { getBuilder().endTypeParameter(); }
;

protected solverDeclarations throws BuilderException
:
	{ getBuilder().beginSolverDeclarations(); }
		( solverDeclaration )*
	{ getBuilder().endSolverDeclarations(); }
;

protected constraintDeclarations throws BuilderException
:
	{ getBuilder().beginConstraintDeclarations(); }
		( constraintDeclaration | constraintsDeclaration )*
	{ getBuilder().endConstraintDeclarations(); }
;

protected upperBound throws BuilderException
{ String id; }
:
	id=anID
	{ getBuilder().beginUpperBound(id); }
		( typeArguments )?
	{ getBuilder().endUpperBound(); }
;

protected typeParameters throws BuilderException:
	LT { getBuilder().beginTypeParameters(); }
		(typeParameter (COMMA typeParameter)*)?
	GT  { getBuilder().endTypeParameters(); }
;

protected simpleID returns [String result]
{ result = null; }
:
	id:SIMPLE_ID { result = id.getText(); }
;

protected composedID returns [String result]
{ result = null; }
:
	id:ID { result = id.getText(); }
;

protected anID returns [String result]
{ result = null; }
:
	result = composedID	| result = simpleID
;

protected declarePackage throws BuilderException
{ String pkg; }
:
	pkg=anID SEMICOLON
	{ getBuilder().beginEndPackage(pkg); }
;

protected importClass throws BuilderException
{ String id; }
:
	id=anID SEMICOLON
	{ getBuilder().importClass(id); }
;

protected importPackage throws BuilderException
{ String id; }
:
	id=anID DOT STAR SEMICOLON
	{ getBuilder().importPackage(id); }
;

protected solverDeclaration throws BuilderException
{ String interf, id = null; }
:	
	SOLVER interf=anID
		{ getBuilder().beginSolverDeclaration(interf); }
	( typeArguments )?
	(id=simpleID)?
		{ getBuilder().buildSolverName(id); }
	SEMICOLON
		{ getBuilder().endSolverDeclaration(); }
;

protected typeArguments throws BuilderException
:
	{ getBuilder().beginTypeArguments(); }
	LT typeArgument (COMMA typeArgument)* GT
	{ getBuilder().endTypeArguments(); }
;

protected typeArgument throws BuilderException
{ String id; }
:	
	id=anID 
	{ getBuilder().beginTypeArgument(id); }
	( typeParameters )?
	{ getBuilder().endTypeArgument(); }
;


protected constraintDeclarationBody throws BuilderException
{ String infix; }
:
	id:SIMPLE_ID { getBuilder().beginConstraintDeclaration(id.getText()); }
	LPAREN constraintArgumentList RPAREN
	( INFIX infix=infixId() { getBuilder().buildInfixIdentifier(infix); } )?
	{ getBuilder().endConstraintDeclaration(); }
;

protected constraintDeclaration throws BuilderException:
	CONSTRAINT 
		constraintDeclarationBody
	SEMICOLON 
;

protected constraintsDeclaration throws BuilderException:
	CONSTRAINTS
		constraintDeclarationBody (COMMA constraintDeclarationBody)*
	SEMICOLON
;

protected constraintArgumentList throws BuilderException:
	(constraintArgument)? (COMMA constraintArgument)*
;

protected constraintArgument throws BuilderException
{ 
	String type, name = null; 
	boolean fixed = false;
}
:
	(FIXED { fixed = true; })?
	type=anID 
	{
		getBuilder().beginConstraintArgument(); 
		getBuilder().beginConstraintArgumentType(type, fixed);
	}
	( typeArguments )?
		{ getBuilder().endConstraintArgumentType(); }
	( name=simpleID )?	
	{ 
		getBuilder().buildConstraintArgumentName(name);
		getBuilder().endConstraintArgument();
	}
;

protected ruleDefinitions throws BuilderException:
	RULES 
		LCURLY { getBuilder().beginRules(); }
			( variableDeclarations | rule )*
		RCURLY	{ getBuilder().endRules(); }
;

protected variableDeclarations throws BuilderException
{ String type = null; boolean fixed = false; }
:	
	VARIABLE (FIXED { fixed = true; })?
	type=anID
	{ 
		getBuilder().beginVariableDeclarations(); 
		getBuilder().beginVariableType(type, fixed);
	}
	(typeArguments)?
		{ getBuilder().endVariableType(); }
	variableDeclaration (COMMA variableDeclaration)*
	SEMICOLON
		{ getBuilder().endVariableDeclarations(); }
;

protected variableDeclaration throws BuilderException
{ String id; }
:
	id = simpleID
	{ getBuilder().declareVariable(id); }
;

protected rule throws BuilderException
:
	{ getBuilder().beginRule(); }
	( 
	(SIMPLE_ID AT)	/* ruleDeclaration */
		=> 	id:SIMPLE_ID AT ruleDefinition[id.getText()]
		|	ruleDefinition[null]	// builder will generate name
	)
	( pragmas )?
	ruleEnd
	{ getBuilder().endRule(); }
;

//protected ruledef[String name]:
//	(constraintList MINUS) => simpagationDef
//	| (constraintList SIMP)	=> simplificationDef
//	| propagationDef
//;

protected ruleDefinition[String name] throws BuilderException:
	(occurrenceList MINUS) => simpagationDef[name]
		| (occurrenceList SIMP) => simplificationDef[name]
		| propagationDef[name]
		
	{ getBuilder().endRuleDefinition(); }
;

//protected ruleDefinition[String name] throws BuilderException
//{ byte type = getRuleType(); }
//:
//	{ getBuilder().beginRuleDefinition(name, type); }
//	(
//		{ type == PROPAGATION }? propagationDef
//			| { type == SIMPLIFICATION }? simplificationDef
//			| { type == SIMPAGATION }? simpagationDef
//	)
//	{ getBuilder().endRuleDefinition(); }
//;

protected pragmas throws BuilderException:
	PRAGMA 
	{ getBuilder().beginPragmas(); }
	pragma (COMMA pragma)*
	{ getBuilder().endPragmas(); }
;

protected pragma throws BuilderException:
	passivePragma
;

protected passivePragma throws BuilderException:
	PASSIVE LPAREN passivePragmaId ( COMMA passivePragmaId )* RPAREN	
;

protected passivePragmaId throws BuilderException
{ String id; }
:
	id=simpleID { getBuilder().addPassivePragma(id); }
;

protected simpagationDef[String name] throws BuilderException:	
	{ 
		getBuilder().beginRuleDefinition(name, SIMPAGATION);
		getBuilder().beginHead(); 
	}
		keptOccurrences MINUS removedOccurrences
	{ 	getBuilder().endHead(); }
	
	SIMP guardNbody
;

protected simplificationDef[String name] throws BuilderException:
	{ 
		getBuilder().beginRuleDefinition(name, SIMPLIFICATION);
		getBuilder().beginHead(); 
	}
		removedOccurrences
	{ getBuilder().endHead(); }
	
	SIMP guardNbody
;

protected propagationDef[String name] throws BuilderException:
	{ 
		getBuilder().beginRuleDefinition(name, PROPAGATION);
		getBuilder().beginHead(); 
	}
		keptOccurrences
	{ getBuilder().endHead(); }
	
	PROP guardNbody
;

protected keptOccurrences throws BuilderException:
	{ getBuilder().beginKeptOccurrences(); }
		occurrenceList
	{ getBuilder().endKeptOccurrences(); }
;

protected removedOccurrences throws BuilderException:
	{ getBuilder().beginRemovedOccurrences(); }
		occurrenceList
	{ getBuilder().endRemovedOccurrences(); }
;

protected fieldAccessConjunct throws BuilderException
{ String id; }
:
	id=composedID { getBuilder().addFieldAccessConjunct(id); }
;

protected identifiedConjunct throws BuilderException
{ String id; }
:
	id=anID { getBuilder().beginIdentifiedConjunct(id); }
		LPAREN arglist RPAREN
	{ getBuilder().endIdentifiedConjunct(); }
;

protected userDefinedConstraint throws BuilderException
{ String id; }
:
	id=simpleID { getBuilder().beginUserDefinedConstraint(id); }
		LPAREN arglist RPAREN
	{ getBuilder().endUserDefinedConstraint(); }
;

protected guardNbody throws BuilderException:
	(
	(conjunctList VERTLINE) 
		=> 	{ getBuilder().beginGuard(); }
				conjunctList VERTLINE 
			{ getBuilder().endGuard(); }
		|
	)
	{ getBuilder().beginBody(); }
		conjunctList
	{ getBuilder().endBody(); }
;

protected occurrenceList throws BuilderException:
	occurrence (and occurrence)*
;

protected occurrence throws BuilderException
{String id; }
:
	(
		( argument infixId )
			=> infixConstraint
			|  userDefinedConstraint
	)
	(
		NUMBER_SIGN id=simpleID
		{ getBuilder().buildOccurrenceId(id); }
	)?
;

protected conjunctList throws BuilderException:
	conjunct (and conjunct)*
;

protected conjunct throws BuilderException:
	( argument infixId )
		=> ( infixConstraint )
		| (
			identifiedConjunct
			| TRUE
			| ( FALSE | FAIL ){ getBuilder().addFailureConjunct(); }
		)
;

protected infixId returns [String result] throws BuilderException
{ result = null; }
:
	EQ 		{ result = "=" ; }
	| EQEQ 	{ result = "=="; }
	| LT  	{ result = "<" ; }
	| GT  	{ result = ">" ; }
	| LEQ 	{ result = "<="; }
	| QEL 	{ result = "<="; }
	| GEQ 	{ result = ">="; }
	| NEQ 	{ result = "!="; }
	| ud:INFIX_ID { result = ud.getText(); }
;

protected infixConstraint throws BuilderException
{ String infix; }
:	
	{ getBuilder().beginInfixConstraint(); }
		argument
		infix=infixId() { getBuilder().buildInfix(infix); }
		argument
	{ getBuilder().endInfixConstraint(); }
;

protected methodInvocationConjunct throws BuilderException:
	id:ID { getBuilder().beginMethodInvocationConjunct(id.getText()); }
		LPAREN arglist RPAREN
	{ getBuilder().endMethodInvocationConjunct(); }
;

// We laten zowel prolog- als java-like syntax toe bij de
// and en bij het einde van een regel. Dit laat de gebruiker
// toe om zelf te kiezen wat hem/haar het beste ligt.
// Ook hybride dingen zijn toegelaten, waar ik persoonlijk
// wel een fan van ben, bvb:
//		leq(X, Y), leq(X1, Y1) <=> X == X1 && Y == Y1 | true.
// ...
protected and:	AND | COMMA	;
protected ruleEnd:	SEMICOLON | DOT ;

protected arglist throws BuilderException:
	{ getBuilder().beginArguments(); }
	(argument)? (COMMA! argument)*
	{ getBuilder().endArguments(); }
;

protected argument throws BuilderException:
	identifiedArgument | constantArgument | methodInvocationArgument | fieldAccessArgument
;


protected fieldAccessArgument throws BuilderException:
	id:ID { getBuilder().addFieldAccessArgument(id.getText()); }
;


protected identifiedArgument throws BuilderException:
	id:SIMPLE_ID	{ getBuilder().addIdentifiedArgument(id.getText()); }
;

protected constantArgument throws BuilderException:
	s:STRING_LITERAL 	{ getBuilder().addConstantArgument(s.getText()); }
		| c:CHAR_LITERAL{ getBuilder().addConstantArgument(c.getText().charAt(0)); }
		| i:NUM_INT 	{ getBuilder().addConstantArgument(Integer.valueOf(i.getText()).intValue()); }
		| fl:NUM_FLOAT 	{ getBuilder().addConstantArgument(Float.valueOf(fl.getText()).floatValue()); }
		| l:NUM_LONG 	{ getBuilder().addConstantArgument(Long.valueOf(l.getText()).longValue()); }
		| d:NUM_DOUBLE 	{ getBuilder().addConstantArgument(Double.valueOf(d.getText()).doubleValue()); }
		| TRUE	 		{ getBuilder().addConstantArgument(true); }
		| FALSE			{ getBuilder().addConstantArgument(false); }
		| NULL			{ getBuilder().addConstantArgument(); }
;

protected methodInvocationArgument throws BuilderException:
	id:ID { getBuilder().beginMethodInvocationArgument(id.getText()); }
		LPAREN arglist RPAREN
	{ getBuilder().endMethodInvocationArgument(); }
;

protected constructorInvocationArgument throws BuilderException:
	NEW id:ID { getBuilder().beginConstructorInvocationArgument(id.getText()); }
		( typeArguments )?
		LPAREN arglist RPAREN
	{ getBuilder().endConstructorInvocationArgument(); }
;


class CHRLexer extends Lexer;

options {
	testLiterals=false;
	k=3;
	exportVocab=CHR;	
	charVocabulary='\u0003'..'\u7FFE';
}

tokens {
	HANDLER		= "handler";
	SOLVER		= "solver";
	RULES		= "rules";
	VARIABLE	= "variable";
	CONSTRAINT	= "constraint";		// cfr JaCK
	CONSTRAINTS = "constraints"; 	// cfr Prolog-versies
	IMPORT		= "import";
	TRUE		= "true";
	FALSE		= "false";
	NULL		= "null";
	FAIL		= "fail";
	EXTENDS		= "extends";
	INFIX		= "infix";
	OPTION		= "option";
	ON			= "on";
	OFF			= "off";
	NEW			= "new";
	PACKAGE     = "package";
	PRAGMA		= "pragma";
	PASSIVE		= "passive";
	
	ELLIPSIS;
}


SIMP: 		"<=>";
PROP: 		"==>";
FIXED:		"+";
MINUS: 		"\\";
AND: 		"&&";
SEMICOLON: 	';';
COMMA: 		',';
LPAREN:		'(';
RPAREN:		')';
LCURLY:		'{';
RCURLY:		'}';
AT:			'@';
VERTLINE:	'|';
AMPERCENT:	'&';
NUMBER_SIGN:'#';

DOT : '.';
STAR: '*';

// operatoren:
EQ:		 	"=";
EQEQ:		"==";
LT:			"<";
LEQ:		"<=";
QEL:		"=<";
GT:			">";
GEQ:		">=";
NEQ:		"!=";

// Whitespace -- ignored
WS
:	(	' '
		|	'\t'
		|	'\f'
			// handle newlines
		|	(	options {generateAmbigWarnings=false;}
			:	"\r\n"  // Evil DOS
			|	'\r'    // Macintosh
			|	'\n'    // Unix (the right way)
			)
			{ newline(); }
		)+
		{ _ttype = Token.SKIP; }
	;

// Single-line comments
SL_COMMENT
	:	"//"
		(~('\n'|'\r'))* ('\n'|'\r'('\n')?)?
		{$setType(Token.SKIP); newline();}
	;

// multiple-line comments
ML_COMMENT
	:	"/*"
		(	/*	'\r' '\n' can be matched in one alternative or by matching
				'\r' in one iteration and '\n' in another.  I am trying to
				handle any flavor of newline that comes in, but the language
				that allows both "\r\n" and "\r" and "\n" to all be valid
				newline is ambiguous.  Consequently, the resulting grammar
				must be ambiguous.  I'm shutting this warning off.
			 */
			options {
				generateAmbigWarnings=false;
			}
		:
			{ LA(2)!='/' }? '*'
		|	'\r' '\n'		{newline();}
		|	'\r'			{newline();}
		|	'\n'			{newline();}
		|	~('*'|'\n'|'\r')
		)*
		"*/"
		{$setType(Token.SKIP);}
	;

// an identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
ID
	options {
		testLiterals=true;
		paraphrase = "an identifer";
	}
	:	(SIMPLE_ID '.' SIMPLE_ID)
			=> SIMPLE_ID ('.' SIMPLE_ID)+
			| SIMPLE_ID { _ttype = SIMPLE_ID; }
	;
	
protected SIMPLE_ID
	options {
		testLiterals=true;
		paraphrase = "an identifer";
	}

	:	( 'a'..'z' | 'A'..'Z' | '_' | '$' ) 
		( 'a'..'z' | 'A'..'Z' | '_' | '$' | '0'..'9')*
	;
	
INFIX_ID:	
	'?'! (~'?')* '?'!
;

// character literals
CHAR_LITERAL
	options {
		paraphrase = "a character";
	}
	:	'\''! ( ESC | ~('\''|'\n'|'\r'|'\\') ) '\''!
	;

// string literals
STRING_LITERAL
	options {
		paraphrase = "a string";
	}
	: '"'! (ESC|~('"'|'\\'|'\n'|'\r'))* '"'!
	;
	
// escape sequence -- note that this is protected; it can only be called
//   from another lexer rule -- it will not ever directly return a token to
//   the parser
// There are various ambiguities hushed in this rule.  The optional
// '0'...'9' digit matches should be matched here rather than letting
// them go back to STRING_LITERAL to be matched.  ANTLR does the
// right thing by matching immediately; hence, it's ok to shut off
// the FOLLOW ambig warnings.
protected ESC:
	'\\'
		(	'n'
		|	'r'
		|	't'
		|	'b'
		|	'f'
		|	'"'
		|	'\''
		|	'\\'
		|	('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
		|	'0'..'3'
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	'0'..'7'
				(
					options {
						warnWhenFollowAmbig = false;
					}
				:	'0'..'7'
				)?
			)?
		|	'4'..'7'
			(
				options {
					warnWhenFollowAmbig = false;
				}
			:	'0'..'7'
			)?
		)
	;
	
// hexadecimal digit (again, note it's protected!)
protected HEX_DIGIT:
	('0'..'9'|'A'..'F'|'a'..'f')
;

// a numeric literal
NUM_INT
	options {
		paraphrase = "a numerical";	
	}
	{boolean isDecimal=false; Token t=null;}
	:	'.' {_ttype = DOT;}
		(	'.' '.' {_ttype = ELLIPSIS;}	/* Dit laten we maar staan, hoewel nergens gebruikt */
		|	(	('0'..'9')+ (EXPONENT)? (f1:FLOAT_SUFFIX {t=f1;})?
				{
				if (t != null && t.getText().toUpperCase().indexOf('F')>=0) {
					_ttype = NUM_FLOAT;
				}
				else {
					_ttype = NUM_DOUBLE; // assume double
				}
				}
			)?
		)

	|	(	'0' {isDecimal = true;} // special case for just '0'
			(	('x'|'X')
				(											// hex
					// the 'e'|'E' and float suffix stuff look
					// like hex digits, hence the (...)+ doesn't
					// know when to stop: ambig.  ANTLR resolves
					// it correctly by matching immediately.  It
					// is therefor ok to hush warning.
					options {
						warnWhenFollowAmbig=false;
					}
				:	HEX_DIGIT
				)+

			|	//float or double with leading zero
				(('0'..'9')+ ('.'|EXPONENT|FLOAT_SUFFIX)) => ('0'..'9')+

			|	('0'..'7')+									// octal
			)?
		|	('1'..'9') ('0'..'9')*  {isDecimal=true;}		// non-zero decimal
		)
		(	('l'|'L')! { _ttype = NUM_LONG; }

		// only check to see if it's a float if looks like decimal so far
		|	{isDecimal}?
			(	'.' ('0'..'9')* (EXPONENT)? (f2:FLOAT_SUFFIX {t=f2;})?
			|	EXPONENT (f3:FLOAT_SUFFIX {t=f3;})?
			|	f4:FLOAT_SUFFIX {t=f4;}
			)
			{
			if (t != null && t.getText().toUpperCase() .indexOf('F') >= 0) {
				_ttype = NUM_FLOAT;
			}
			else {
				_ttype = NUM_DOUBLE; // assume double
			}
			}
		)?
	;


// a couple protected methods to assist in matching floating point numbers
protected EXPONENT:
	('e'|'E') ('+'|'-')? ('0'..'9')+
;
	
protected FLOAT_SUFFIX:
	'f'|'F'|'d'|'D'
;