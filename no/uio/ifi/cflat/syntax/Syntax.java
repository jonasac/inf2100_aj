// vim: sw=2 ts=2

package no.uio.ifi.cflat.syntax;

/*
 * module Syntax
 */

/* import static no.uio.ifi.cflat.scanner.Token.eofToken; */
/* import static no.uio.ifi.cflat.scanner.Token.forToken; */
/* import static no.uio.ifi.cflat.scanner.Token.ifToken; */
/* import static no.uio.ifi.cflat.scanner.Token.leftBracketToken; */
/* import static no.uio.ifi.cflat.scanner.Token.leftCurlToken; */
/* import static no.uio.ifi.cflat.scanner.Token.leftParToken; */
/* import static no.uio.ifi.cflat.scanner.Token.nameToken; */
/* import static no.uio.ifi.cflat.scanner.Token.returnToken; */
/* import static no.uio.ifi.cflat.scanner.Token.rightCurlToken; */
/* import static no.uio.ifi.cflat.scanner.Token.rightParToken; */
/* import static no.uio.ifi.cflat.scanner.Token.semicolonToken; */
/* import static no.uio.ifi.cflat.scanner.Token.whileToken; */
/* import static no.uio.ifi.cflat.scanner.Token.intToken; */
/* import static no.uio.ifi.cflat.scanner.Token.commaToken; */
/* import static no.uio.ifi.cflat.scanner.Token.numberToken; */
/* import static no.uio.ifi.cflat.scanner.Token.addToken; */
/* import static no.uio.ifi.cflat.scanner.Token.subtractToken; */
import static no.uio.ifi.cflat.scanner.Token.*;
import no.uio.ifi.cflat.cflat.Cflat;
import no.uio.ifi.cflat.code.Code;
import no.uio.ifi.cflat.error.Error;
import no.uio.ifi.cflat.log.Log;
import no.uio.ifi.cflat.scanner.Scanner;
import no.uio.ifi.cflat.scanner.Token;
import no.uio.ifi.cflat.types.ArrayType;
import no.uio.ifi.cflat.types.Type;
import no.uio.ifi.cflat.types.Types;


/*
 * Creates a syntax tree by parsing; 
 * prints the parse tree (if requested);
 * checks it;
 * generates executable code. 
 */
public class Syntax {
  static DeclList library;
  static Program program;

  public static void init() {
    // -- Must be changed in part 1:
    System.out.println("SYNTAX: INIT");
  }

  public static void finish() {
    // -- Must be changed in part 1:
    System.out.println("SYNTAX: FINISH");
  }

  public static void checkProgram() {
    program.check(library);
  }

  public static void genCode() {
    program.genCode(null);
  }

  public static void parseProgram() {
    program = new Program();
    program.parse();
  }

  public static void printProgram() {
    program.printTree();
  }

  static void error(SyntaxUnit use, String message) {
    Error.error(use.lineNum, message);
  }
}


/*
 * Master class for all syntactic units. (This class is not mentioned in the
 * syntax diagrams.)
 */
abstract class SyntaxUnit {
  int lineNum;

  SyntaxUnit() {
    lineNum = Scanner.curLine;
  }

  abstract void check(DeclList curDecls);

  abstract void genCode(FuncDecl curFunc);

  abstract void parse();

  abstract void printTree();
}


/*
 * A <program>
 */
class Program extends SyntaxUnit {
  DeclList progDecls = new GlobalDeclList();

  @Override
    void check(DeclList curDecls) {
      progDecls.check(curDecls);

      if (!Cflat.noLink) {
        // Check that 'main' has been declared properly:
        // -- Must be changed in part 2:
      }
    }

  @Override
    void genCode(FuncDecl curFunc) {
      progDecls.genCode(null);
    }

  @Override
    void parse() {
      System.out.println("PARSER IN PROGRAM");
      Log.enterParser("<program>");

      progDecls.parse();
      if (Scanner.curToken != eofToken)
        Error.expected("A declaration");

      Log.leaveParser("</program>");
    }

  @Override
    void printTree() {
      progDecls.printTree();
    }
}


/*
 * A declaration list. (This class is not mentioned in the syntax diagrams.)
 */

abstract class DeclList extends SyntaxUnit {
  Declaration firstDecl = null;
  DeclList outerScope;

  DeclList() {
    // -- Must be changed in part 1:
    System.out.println("DECLLIST: CONSTRUCTOR");
  }

  @Override
    void check(DeclList curDecls) {
      outerScope = curDecls;

      Declaration dx = firstDecl;
      while (dx != null) {
        dx.check(this);
        dx = dx.nextDecl;
      }
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
			Log.wTreeLn("DECL LIST");
    }

  void addDecl(Declaration d) {
    if (firstDecl == null) {
      firstDecl = d;
    } else {
      Declaration tmp = firstDecl;
      while (tmp.nextDecl != null) {
        tmp = tmp.nextDecl;
      }
      tmp.nextDecl = d;
    }
  }

  int dataSize() {
    Declaration dx = firstDecl;
    int res = 0;

    while (dx != null) {
      res += dx.declSize();
      dx = dx.nextDecl;
    }
    return res;
  }

  Declaration findDecl(String name, SyntaxUnit usedIn) {
    // -- Must be changed in part 2:
    return null;
  }
}


/*
 * A list of global declarations. (This class is not mentioned in the syntax
 * diagrams.)
 */
class GlobalDeclList extends DeclList {
  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      System.out.println("PARSER IN GLOBALDECLLIST");
      while (Token.isTypeName(Scanner.curToken)) {
        if (Scanner.nextToken == nameToken) {
          if (Scanner.nextNextToken == leftParToken) {
            FuncDecl fd = new FuncDecl(Scanner.nextName);
            fd.parse();
            addDecl(fd);
          } else if (Scanner.nextNextToken == leftBracketToken) {
            GlobalArrayDecl gad = new GlobalArrayDecl(Scanner.nextName);
            gad.parse();
            addDecl(gad);
          } else {
            // -- Must be changed in part 1:
            GlobalSimpleVarDecl gsv = new GlobalSimpleVarDecl(Scanner.nextName);
            gsv.parse();
            addDecl(gsv);
          }
        } else {
          Error.expected("A declaration");
        }
      }
    }
}


/*
 * A list of local declarations. (This class is not mentioned in the syntax
 * diagrams.)
 */
class LocalDeclList extends DeclList {
  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      // -- Must be changed in part 1:
      System.out.println("LOCALDECLLIST: PARSE");
      System.out.println("EXITED FROM LOCALDECLLIST");
      System.exit(1);
    }
}


/*
 * A list of parameter declarations. (This class is not mentioned in the syntax
 * diagrams.)
 */
class ParamDeclList extends DeclList {
  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      System.out.println("PARSER IN PARAMDECLLIST");
      // -- Must be changed in part 1:
      while (Scanner.curToken == intToken || Scanner.curToken == doubleToken && Scanner.nextToken == nameToken) {
        ParamDecl pd = new ParamDecl(Scanner.nextName);
        addDecl(pd);
        pd.parse(); 
        if (Scanner.curToken == commaToken) {
          Scanner.skip(commaToken);
        }
      }
    }
}


/*
 * Any kind of declaration. (This class is not mentioned in the syntax
 * diagrams.)
 */
abstract class Declaration extends SyntaxUnit {
  String name, assemblerName;
  Type type;
  boolean visible = false;
  Declaration nextDecl = null;

  Declaration(String n) {
    name = n;
  }

  abstract int declSize();

  /**
   * checkWhetherArray: Utility method to check whether this Declaration is
   * really an array. The compiler must check that a name is used properly; for
   * instance, using an array name a in "a()" or in "x=a;" is illegal. This is
   * handled in the following way:
   * <ul>
   * <li>When a name a is found in a setting which implies that should be an
   * array (i.e., in a construct like "a["), the parser will first search for
   * a's declaration d.
   * <li>The parser will call d.checkWhetherArray(this).
   * <li>Every sub-class of Declaration will implement a checkWhetherArray. If
   * the declaration is indeed an array, checkWhetherArray will do nothing, but
   * if it is not, the method will give an error message.
   * </ul>
   * Examples
   * <dl>
   * <dt>GlobalArrayDecl.checkWhetherArray(...)</dt>
   * <dd>will do nothing, as everything is all right.</dd>
   * <dt>FuncDecl.checkWhetherArray(...)</dt>
   * <dd>will give an error message.</dd>
   * </dl>
   */
  abstract void checkWhetherArray(SyntaxUnit use);

  /**
   * checkWhetherFunction: Utility method to check whether this Declaration is
   * really a function.
   * 
   * @param nParamsUsed
   *          Number of parameters used in the actual call. (The method will
   *          give an error message if the function was used with too many or
   *          too few parameters.)
   * @param use
   *          From where is the check performed?
   * @see checkWhetherArray
   */
  abstract void checkWhetherFunction(int nParamsUsed, SyntaxUnit use);

  /**
   * checkWhetherSimpleVar: Utility method to check whether this Declaration is
   * really a simple variable.
   * 
   * @see checkWhetherArray
   */
  abstract void checkWhetherSimpleVar(SyntaxUnit use);
}


/*
 * A <var decl>
 */
abstract class VarDecl extends Declaration {
  VarDecl(String n) {
    super(n);
  }

  @Override
    int declSize() {
      return type.size();
    }

  @Override
    void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
      Syntax.error(use, name + " is a variable and no function!");
    }

  @Override
    void printTree() {
      Log.wTree(type.typeName() + " " + name);
      Log.wTreeLn(";");
    }

  // -- Must be changed in part 1+2:
}


/*
 * A global array declaration
 */
class GlobalArrayDecl extends VarDecl {
  GlobalArrayDecl(String n) {
    super(n);
    assemblerName = (Cflat.underscoredGlobals() ? "_" : "") + n;
  }

  @Override
    void check(DeclList curDecls) {
      visible = true;
      if (((ArrayType)type).nElems < 0)
        Syntax.error(this, "Arrays cannot have negative size!");
    }

  @Override
    void checkWhetherArray(SyntaxUnit use) {
      /* OK */
    }

  @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
      Syntax.error(use, name + " is an array and no simple variable!");
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      Log.enterParser("<var decl>");

      // -- Must be changed in part 1:

      System.out.println("EXITED FROM GLOBALARRAYDECL");
      System.exit(1);

      Log.leaveParser("</var decl>");
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
      System.out.println("GLOBALARRAYDECL: PRINTTREE");
    }
}


/*
 * A global simple variable declaration
 */
class GlobalSimpleVarDecl extends VarDecl {
  GlobalSimpleVarDecl(String n) {
    super(n);
    assemblerName = (Cflat.underscoredGlobals() ? "_" : "") + n;
  }

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherArray(SyntaxUnit use) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
      /* OK */
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      System.out.println("PARSER IN GLOBAL SIMPLE VARDECL");
      Log.enterParser("<var decl>");
      // -- Must be changed in part 1:
      Scanner.skip(intToken);
      Scanner.skip(nameToken);
      Scanner.skip(semicolonToken);
      Log.leaveParser("</var decl>");
    }
}


/*
 * A local array declaration
 */
class LocalArrayDecl extends VarDecl {
  LocalArrayDecl(String n) {
    super(n);
  }

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherArray(SyntaxUnit use) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      Log.enterParser("<var decl>");

      // -- Must be changed in part 1:
      System.out.println("EXITED FROM LOCALARRAYDECL");
      System.exit(1);

      Log.leaveParser("</var decl>");
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
      System.out.println("LOCAL ARRAY DECL: PRINT TREE");
    }

}


/*
 * A local simple variable declaration
 */
class LocalSimpleVarDecl extends VarDecl {
  LocalSimpleVarDecl(String n) {
    super(n);
  }

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherArray(SyntaxUnit use) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      Log.enterParser("<var decl>");

      // -- Must be changed in part 1:
      System.out.println("EXITED FROM LOCALSIMPLECARDECL");
      System.exit(1);
      Log.leaveParser("</var decl>");
    }
}


/*
 * A <param decl>
 */
class ParamDecl extends VarDecl {
  int paramNum = 0;

  ParamDecl(String n) {
    super(n);
  }

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherArray(SyntaxUnit use) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      System.out.println("PARSER IN PARAMDECL");
      Log.enterParser("<param decl>");

      // -- Must be changed in part 1:
      if (Token.isTypeName(Scanner.curToken)){
        type = Types.getType(Scanner.curToken);
        Scanner.skip(Scanner.curToken);
        name = Scanner.curName;
        Scanner.skip(nameToken);
      } else {
        Error.expected(" A param declaration");
      }
      Log.leaveParser("</param decl>");
    }
}


/*
 * A <func decl>
 */
class FuncDecl extends Declaration {
  // -- Must be changed in part 1+2:
  ParamDeclList pdl;
  LocalDeclList ldl;
  StatmList sl = new StatmList();

  FuncDecl(String n) {
    // Used for user functions:

    super(n);
    assemblerName = (Cflat.underscoredGlobals() ? "_" : "") + n;
    // -- Must be changed in part 1:
    type = Types.getType(Scanner.curToken);
  }

  @Override
    int declSize() {
      return 0;
    }

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherArray(SyntaxUnit use) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
      // -- Must be changed in part 2:
    }

  @Override
    void checkWhetherSimpleVar(SyntaxUnit use) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      Code.genInstr("", ".globl", assemblerName, "");
      Code.genInstr(assemblerName, "pushl", "%ebp", "Start function " + name);
      Code.genInstr("", "movl", "%esp,%ebp", "");
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      // -- Must be changed in part 1:
      System.out.println("PARSER IN FUNCDECL");
      Log.enterParser("<func decl>");
      Scanner.skip(intToken);
      Scanner.skip(nameToken);
      Scanner.skip(leftParToken);
      if (Scanner.curToken == intToken) {
        pdl = new ParamDeclList();
        pdl.parse();
      }
      Scanner.skip(rightParToken);
      Log.enterParser("<func body>");
      Scanner.skip(leftCurlToken);
      if (Scanner.curToken == intToken) {
        ldl = new LocalDeclList();
        ldl.parse();
      }
      sl.parse();
      Scanner.skip(rightCurlToken);
      Log.leaveParser("</func body>");
      Log.leaveParser("</func decl>");
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
      System.out.println("FUNC DECL: PRINT TREE");
    }
}


/*
 * A <statm list>.
 */
class StatmList extends SyntaxUnit {
  // -- Must be changed in part 1:
  Statement head = null;
  Statement tail = null;

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      System.out.println("PARSER IN STATMLIST");
      Log.enterParser("<statm list>");

      Statement lastStatm = null;
      while (Scanner.curToken != rightCurlToken) {
        Log.enterParser("<statement>");
        // -- Must be changed in part 1:
        if (head == null) {
          head = Statement.makeNewStatement();
          head.parse();
          tail = head;
        } else {
          tail.nextStatm = Statement.makeNewStatement();
          tail = tail.nextStatm;
          tail.parse();
        }
        Log.leaveParser("</statement>");

      }
        Log.leaveParser("</statm list>");
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
      System.out.println("STATM LIST: PRINT TREE");
    }
}


/*
 * A <statement>.
 */
abstract class Statement extends SyntaxUnit {
  Statement nextStatm = null;

  static Statement makeNewStatement() {
    if (Scanner.curToken == nameToken && Scanner.nextToken == leftParToken) {
      // -- Must be changed in part 1:
      return new CallStatm();
    } else if (Scanner.curToken == nameToken) {
      // -- Must be changed in part 1:
      return new AssignStatm();
    } else if (Scanner.curToken == forToken) {
      // -- Must be changed in part 1:
      return new ForStatm();
    } else if (Scanner.curToken == ifToken) {
      return new IfStatm();
    } else if (Scanner.curToken == returnToken) {
      // -- Must be changed in part 1:
    } else if (Scanner.curToken == whileToken) {
      return new WhileStatm();
    } else if (Scanner.curToken == semicolonToken) {
      return new EmptyStatm();
    } else {
      Error.expected("A statement");
    }
    return null; // Just to keep the Java compiler happy. :-)
  }
}


/*
 * An <empty statm>.
 */
class EmptyStatm extends Statement {
  // -- Must be changed in part 1+2:

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      // -- Must be changed in part 1:
      System.out.println("EMPTY STATM: PARSE");
      System.out.println("EXITED FROM EMPTY STATM");
      System.exit(1);
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
      System.out.println("EMPTY STATM: PRINT TREE");
    }
}


/*
 * A <for-statm>.
 */
// -- Must be changed in part 1+2:
class ForStatm extends Statement {
  @Override
    void check(DeclList curDecls) {
    }

  @Override
    void genCode(FuncDecl curFunc) {
    }

  @Override
    void parse() {
      System.out.println("HELLO FORM FOR STATEMENT");
      System.out.println("EXITED FROM FORSTATM");
      System.exit(1);
    }

  @Override
    void printTree() {
    }
}

class CallStatm extends Statement {
  FunctionCall fc = new FunctionCall();
  @Override
    void check(DeclList curDecls) {
    }

  @Override
    void genCode(FuncDecl curFunc) {
    }

  @Override
    void parse() {
      System.out.println("PARSER IN CALL STATM");
      Log.enterParser("<call-statm>");
      fc.parse();
      Scanner.skip(semicolonToken);
      Log.leaveParser("</call-statm>");
    }

  @Override
    void printTree() {
    }
}
class AssignStatm extends Statement {
  Assignment ass = null;
  @Override
    void check(DeclList curDecls) {
    }

  @Override
    void genCode(FuncDecl curFunc) {
    }

  @Override
    void parse() {
      System.out.println("PARSER IN ASSIGNSTATM");
      Log.enterParser("<assign-statm>");
      ass = new Assignment();
      ass.parse();
      Scanner.skip(semicolonToken);
      Log.leaveParser("</assign-statm>"); 
    }

  @Override
    void printTree() {
    }
}

class Assignment extends SyntaxUnit {
  Variable var = null;
  Expression expr = null;

  void check(DeclList curDecls) {
  }

  void genCode(FuncDecl curFunc){
  }
  void parse() {
    System.out.println("PARSER IN ASSIGNMENT");
    Log.enterParser("<assignment>");
    var = new Variable();
    var.parse();
    Scanner.skip(assignToken);
    expr = new Expression();
    expr.parse();
    Log.leaveParser("</assignment>");
  }
  void printTree() {
  }
}

/*
 * An <if-statm>.
 */
class IfStatm extends Statement {
  // -- Must be changed in part 1+2:
  Expression ifTest = null;
  StatmList ifPart = null;
  StatmList elsePart = null;

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      // -- Must be changed in part 1:
      System.out.println("PARSER IN IFSTATM");
      Scanner.skip(ifToken);
      Scanner.skip(leftParToken);
      ifTest = new Expression();
      ifTest.parse();
      Scanner.skip(rightParToken);
      Scanner.skip(leftCurlToken);
      ifPart = new StatmList();
      ifPart.parse();
      Scanner.skip(rightCurlToken);
      if (Scanner.curToken == elseToken) {
        Log.enterParser("<else-part>");
        Scanner.readNext();
        Scanner.skip(rightCurlToken);
        elsePart = new StatmList();
        elsePart.parse();
        Scanner.skip(rightCurlToken);
        Log.leaveParser("</else-part>");
      }
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
      System.out.println("IF STATM: PRINT TREE");
    }
}


/*
 * A <return-statm>.
 */
// -- Must be changed in part 1+2:
class ReturnStatm extends Statement {
  @Override
    void check(DeclList curDecls) {
    }

  @Override
    void genCode(FuncDecl curFunc) {
    }

  @Override
    void parse() {
      System.out.println("EXITED FROM RETURN STATM");
      System.exit(1);
    }

  @Override
    void printTree() {
    }
}

/*
 * A <while-statm>.
 */
class WhileStatm extends Statement {
  Expression test = new Expression();
  StatmList body = new StatmList();

  @Override
    void check(DeclList curDecls) {
      test.check(curDecls);
      body.check(curDecls);
    }

  @Override
    void genCode(FuncDecl curFunc) {
      String testLabel = Code.getLocalLabel(), endLabel = Code.getLocalLabel();

      Code.genInstr(testLabel, "", "", "Start while-statement");
      test.genCode(curFunc);
      test.valType.genJumpIfZero(endLabel);
      body.genCode(curFunc);
      Code.genInstr("", "jmp", testLabel, "");
      Code.genInstr(endLabel, "", "", "End while-statement");
    }

  @Override
    void parse() {
      Log.enterParser("<while-statm>");
      System.out.println("PARSER IN WHITESTATM");
      Scanner.readNext();
      Scanner.skip(leftParToken);
      test.parse();
      Scanner.skip(rightParToken);
      Scanner.skip(leftCurlToken);
      body.parse();
      Scanner.skip(rightCurlToken);

      Log.leaveParser("</while-statm>");
    }

  @Override
    void printTree() {
      Log.wTree("while (");
      test.printTree();
      Log.wTreeLn(") {");
      Log.indentTree();
      body.printTree();
      Log.outdentTree();
      Log.wTreeLn("}");
    }
}


// -- Must be changed in part 1+2:

/*
 * An <expression list>.
 */

class ExprList extends SyntaxUnit {
  Expression firstExpr = null;

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      Expression lastExpr = null;

      System.out.println("PARSER ENTERING EXPRLIST");
      System.out.println("CURRENT TOKEN IS : " + Scanner.curToken);
      Log.enterParser("<expr list>");

      // -- Must be changed in part 1:
      if (Scanner.curToken != rightParToken) {
        if (firstExpr == null) {
          firstExpr = new Expression();
          firstExpr.parse();
          lastExpr = firstExpr;
        } else {
          System.out.println("GOT HERE!");
          firstExpr.nextExpr = new Expression();
          lastExpr = firstExpr.nextExpr;
          lastExpr.parse();
          while (Scanner.curToken == commaToken) {
            firstExpr.nextExpr = new Expression();
            lastExpr = firstExpr.nextExpr;
            lastExpr.parse();
          }
        }
      } else {
        Scanner.readNext();
      }
      System.out.println(Scanner.curToken);
      System.out.println(Scanner.nextNextToken);
      System.out.println("PARSER LEAVING EXPRLIST");
      System.out.println("EXPRLIST CURRENT TOKEN: " + Scanner.curToken);
      System.out.println("EXPRLIST CURRENT NAME: " + Scanner.curName);
      System.out.println("EXPRLIST NEXT TOKEN: " + Scanner.nextToken);
      System.out.println("EXPRELIST NEXT NAME: " + Scanner.nextName);
      Log.leaveParser("</expr list>");
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
    }
  // -- Must be changed in part 1:
}


/*
 * An <expression>
 */
class Expression extends Operand {
  Expression nextExpr = null;
  Term firstTerm = new Term(), secondTerm = null;
  Operator relOp = null;
  boolean innerExpr = false;

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      System.out.println("HELLO FROM EXPRESSION");
      Log.enterParser("<expression>");
      firstTerm.parse();
      if (Token.isRelOperator(Scanner.curToken)) {
        System.out.println("GOT HERE");
        relOp = new RelOperator();
        relOp.parse();
        secondTerm = new Term();
        secondTerm.parse();
      }
      Log.leaveParser("</expression>");
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
    }
}


/*
 * A <term>
 */
class Term extends SyntaxUnit {
  // -- Must be changed in part 1+2:
  Factor firstFactor;
  TermOperator firstTop;
  Type valType;

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      // -- Must be changed in part 1:
      System.out.println("PARSER IN TERM");
      Factor lastFactor = null;
      Operator lastOp = null;
      Log.enterParser("<term>");
      firstFactor = new Factor();
      lastFactor = firstFactor;
      firstFactor.parse();
      while (Token.isTermOperator(Scanner.curToken)) {
        if (lastOp == null) {
          firstTop = new TermOperator(); 
          lastOp = firstTop;
        } else {
          lastOp.nextOp = new TermOperator();
          lastOp = lastOp.nextOp;
        }
        lastOp.parse();
        lastFactor.nextFactor = new Factor();
        lastFactor = lastFactor.nextFactor;
        lastFactor.parse();
      }
      Log.leaveParser("</term>");
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1+2:
    }
}

class Factor extends SyntaxUnit {
  Factor nextFactor;
  Operator firstFo;
  Operand firstOperand;
  @Override
    void check(DeclList curDecls) {
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // TODO
    }

  @Override
    void parse() {
      System.out.println("PARSER IN FACTOR");
      System.out.println("CURRENT TOKEN IS : " + Scanner.curToken);
      System.out.println("NEXT TOKEN IS : " + Scanner.nextToken);
      System.out.println("NEXTNEXT TOKEN IS : " + Scanner.nextNextToken);
      Operator lastFo = null;
      Operand lastOperand = null;
      Log.enterParser("<factor>");
      do {
        Log.enterParser("<operand>");
        if (Scanner.curToken == numberToken) {
          System.out.println("PARSER IN FACTOR/NUMBER");
          if (firstOperand == null) {
            firstOperand = new Number();
            lastOperand = firstOperand;
          } else {
            lastOperand.nextOperand = new Number();
            lastOperand = lastOperand.nextOperand;
          }
          lastOperand.parse();
        } else if (Scanner.curToken == nameToken && Scanner.nextToken == leftParToken) {
          System.out.println("PARSER IN FACTOR/FUNCTIONCALL");
          if (firstOperand == null) {
            firstOperand = new FunctionCall();
            lastOperand = firstOperand;
          } else {
            lastOperand.nextOperand = new FunctionCall();
            lastOperand = lastOperand.nextOperand;
          }
          lastOperand.parse();
        } else if (Scanner.curToken == nameToken) {
          System.out.println("PARSER IN FACTOR/VARIABLE");
          if (firstOperand == null) {
            firstOperand = new Variable();
            lastOperand = firstOperand;
          } else {
            lastOperand.nextOperand = new Variable();
            lastOperand = lastOperand.nextOperand;
          }
          lastOperand.parse();
        } else if (Scanner.curToken == leftParToken) {
          System.out.println("PARSER IN FACTOR/EXPRESSION");
          Scanner.skip(leftParToken);
          if (firstOperand == null) {
            firstOperand = new Expression();
            lastOperand = firstOperand;
          } else {
            lastOperand.nextOperand = new Expression();
            lastOperand = lastOperand.nextOperand;
          }
          lastOperand.parse();
        } else {
          System.out.println("WE ARE HERE LOLOLOLOL");
          Error.expected("An operand");
        }
        Log.leaveParser("</operand>");
        if (Token.isFactorOperator(Scanner.curToken)) {
          if (firstFo == null) {
            firstFo = new FactOperator();
            lastFo = firstFo;
          } else {
            lastFo.nextOp = new FactOperator();
            lastFo = lastFo.nextOp;
          }
          lastFo.parse();
        } else {
          Log.leaveParser("</factor>");
          return;
        }
      } while (true);
    }


  @Override
    void printTree() {
      // TODO: here
    }
}
// -- Must be changed in part 1+2:

/*
 * An <operator>
 */
abstract class Operator extends SyntaxUnit {
  Operator nextOp = null;
  Type opType;
  Token opToken;

  @Override
    void check(DeclList curDecls) {
    }
}


class FactOperator extends Operator {
  @Override
    void check(DeclList curDecls) {
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // TODO
    }

  @Override
    void parse() {
      System.out.println("PARSER IN FACT OPERATOR");
      Log.enterParser("<fact operator>");
      if (Token.isFactorOperator(Scanner.curToken)) {
        opToken = Scanner.curToken;
        Scanner.skip(Scanner.curToken);
      } else {
        Error.expected("An factor operator");
      }
      Log.leaveParser("</fact operator>");
    }

  @Override
    void printTree() {
      // TODO
    }
}
class TermOperator extends Operator {
  @Override
    void check(DeclList curDecls) {
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // TODO
    }

  @Override
    void parse() {
      System.out.println("PARSER IN TERM OPERATOR");
      Log.enterParser("<term>");
      if (Token.isTermOperator(Scanner.curToken)) {
        Scanner.skip(Scanner.curToken);
      } else {
        Error.expected("An term operator");
      }
      Log.leaveParser("</term operator>");
    }

  @Override
    void printTree() {
      // TODO
    }
}
// -- Must be changed in part 1+2:
/*
 * A relational operator (==, !=, <, <=, > or >=).
 */

class RelOperator extends Operator {
  @Override
    void genCode(FuncDecl curFunc) {
      if (opType == Types.doubleType) {
        Code.genInstr("", "fldl", "(%esp)", "");
        Code.genInstr("", "addl", "$8,%esp", "");
        Code.genInstr("", "fsubp", "", "");
        Code.genInstr("", "fstps", Code.tmpLabel, "");
        Code.genInstr("", "cmpl", "$0," + Code.tmpLabel, "");
      } else {
        Code.genInstr("", "popl", "%ecx", "");
        Code.genInstr("", "cmpl", "%eax,%ecx", "");
      }
      Code.genInstr("", "movl", "$0,%eax", "");
      switch (opToken) {
        case equalToken:
          Code.genInstr("", "sete", "%al", "Test ==");
          break;
        case notEqualToken:
          Code.genInstr("", "setne", "%al", "Test !=");
          break;
        case lessToken:
          Code.genInstr("", "setl", "%al", "Test <");
          break;
        case lessEqualToken:
          Code.genInstr("", "setle", "%al", "Test <=");
          break;
        case greaterToken:
          Code.genInstr("", "setg", "%al", "Test >");
          break;
        case greaterEqualToken:
          Code.genInstr("", "setge", "%al", "Test >=");
          break;
      }
    }

  @Override
    void parse() {
      System.out.println("PARSER IN REL OPERATOR");
      Log.enterParser("<rel operator>");

      opToken = Scanner.curToken;
      Scanner.readNext();

      Log.leaveParser("</rel operator>");
    }

  @Override
    void printTree() {
      String op = "?";
      switch (opToken) {
        case equalToken:
          op = "==";
          break;
        case notEqualToken:
          op = "!=";
          break;
        case lessToken:
          op = "<";
          break;
        case lessEqualToken:
          op = "<=";
          break;
        case greaterToken:
          op = ">";
          break;
        case greaterEqualToken:
          op = ">=";
          break;
      }
      Log.wTree(" " + op + " ");
    }
}


/*
 * An <operand>
 */
abstract class Operand extends SyntaxUnit {
  Operand nextOperand = null;
  Type valType;
}

/*
 * A <function call>.
 */
class FunctionCall extends Operand {
  // -- Must be changed in part 1+2:
  ExprList el = new ExprList();
  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      // -- Must be changed in part 1:
      System.out.println("PARSER IN FUNCTIONCALL");
      Log.enterParser("<function call>");
      Scanner.skip(nameToken);
      Scanner.skip(leftParToken);
      System.out.println("FUNCTIONCALL CURRENT TOKEN: " + Scanner.curToken);
      el.parse();
      System.out.println("FUNCTIONCALL 2 CURRENT TOKEN: " + Scanner.curToken);
      System.out.println("OMGOMGOMGLEAVING FUNCTIONCALL");
      Scanner.skip(rightParToken);
      System.out.println("NOT GETTING HERE");
      Log.leaveParser("</function call>");
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
			System.out.println("OST OST OST");
			Log.wTreeLn("function call");
    }
  // -- Must be changed in part 1+2:
}

/*
 * A <number>.
 */
class Number extends Operand {
  int numVal;

  @Override
    void check(DeclList curDecls) {
      // -- Must be changed in part 2:
    }

  @Override
    void genCode(FuncDecl curFunc) {
      Code.genInstr("", "movl", "$" + numVal + ",%eax", "" + numVal);
    }

  @Override
    void parse() {
      // -- Must be changed in part 1:
      Log.enterParser("<number>");
      System.out.println("PARSER IN NUMBER");
      numVal = Scanner.curNum;
      Scanner.skip(numberToken);
      Log.leaveParser("</number>");
    }

  @Override
    void printTree() {
      Log.wTree("" + numVal);
    }
}


/*
 * A <variable>.
 */

class Variable extends Operand {
  String varName;
  VarDecl declRef = null;
  Expression index = null;

  @Override
    void check(DeclList curDecls) {
      Declaration d = curDecls.findDecl(varName, this);
      if (index == null) {
        d.checkWhetherSimpleVar(this);
        valType = d.type;
      } else {
        d.checkWhetherArray(this);
        index.check(curDecls);
        index.valType.checkType(lineNum, Types.intType, "Array index");
        valType = ((ArrayType)d.type).elemType;
      }
      declRef = (VarDecl)d;
    }

  @Override
    void genCode(FuncDecl curFunc) {
      // -- Must be changed in part 2:
    }

  @Override
    void parse() {
      Log.enterParser("<variable>");
      System.out.println("PARSER IN VARIABLE");
      System.exit(1);
      // -- Must be changed in part 1:
      varName = Scanner.curName;
      System.out.println("PARSER IN VARIABLE");
      Scanner.skip(nameToken);
      if (Scanner.curToken == leftBracketToken) {
        Scanner.skip(leftBracketToken);
        index = new Expression();
        index.parse();
        Scanner.skip(rightBracketToken);
      }
      Log.leaveParser("</variable>");
    }

  @Override
    void printTree() {
      // -- Must be changed in part 1:
    }
}
