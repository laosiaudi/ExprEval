package parser;
import exceptions.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Stack;
import java.lang.*;
/**
 *Description:
 * <br/>Copyright(C),2013 ,Lao Si
 * <br/>This program is protected by copyright laws. 
 * <br/>Program Name: ExprEval
 * @author Lao Si
 * @version 1.0 
 */
public  class Parser{
    Token tk = new Token();
    ArrayList<String>new_expr = new ArrayList<String>();
    ArrayList<String>ss = new ArrayList<String>();
    double result;
    int count;
    boolean solve = false;
    /**
    *Initialization
    *@return no return value
    */
    public void Parser(){
        
        
        result = 0;
       
    }
    /**
    *read in expression and execute lexical analization
    *@return no return value
    *@throws EmptyExpressionException,IllegalDecimalException, IllegalIdentifierException,IllegalSymbolException
    */
    public void readin(String expr) throws EmptyExpressionException,
        IllegalDecimalException, 
        IllegalIdentifierException,
        IllegalSymbolException{

        tk.Analysize(expr.toLowerCase());
        
        new_expr = tk.getExpre();
        
        new_expr.add("$");
        ss.add("$");
    }
    /**
    *get next token to deal with
    *@return String
    */
    private String getNextToken(int index){
        return new_expr.get(index);
            
    }

    /**
    *shift: push value into stack
    *@return no return value
    */
    public void shift(String token){
        ss.add(token);
        count ++;


    }

    /**
    *do reduction according to the table
    *@return no return value
    *@throws IllegalIdentifierException,
    *        MissingOperandException,
    *       MissingOperatorException,
    *        MissingRightParenthesisException,
    *        FunctionCallException,
    *        TrinaryOperationException,
    *        TypeMismatchedException,
    *        SyntacticException,
    *        SemanticException,
    *        IllegalDecimalException,
    *        IllegalSymbolException
    */
    public void reduce(int type) throws IllegalIdentifierException,
            MissingOperandException,
            MissingOperatorException,
            MissingRightParenthesisException,
            FunctionCallException,
            TrinaryOperationException,
            TypeMismatchedException,
            SyntacticException,
            SemanticException,
            IllegalDecimalException,
            IllegalSymbolException{
        double tempresult;
        boolean tempresult2;
        switch(type){
            case 3:
                tempresult = dualop();
                ss.add(Double.toString(tempresult));
                
                break;
            case 4:
                
                ss.add(parent());
                break;
            case 5:
                tempresult = minus();
                ss.add(Double.toString(tempresult));
                break;
            case 6:
                tempresult2 = cmp();
                if (tempresult2 == true)
                    ss.add("True");
                else
                    ss.add("False");
                break;
            case 7:
                tempresult2 = rela();
                
                if (tempresult2 == true)
                    ss.add("True");
                else
                    ss.add("False");
                break;
            case 8:
                tempresult2 = not();
                
                if (tempresult2 == true)
                    ss.add("True");
                else
                    ss.add("False");
                break;
            case 9:
                tempresult = triop();
                ss.add(Double.toString(tempresult));
                break;
        }
    }

    /**
    *function with two value
    *@return double
    *@throws TypeMismatchedException,
    *DividedByZeroException,
    *MissingOperatorException,
    *MissingOperandException
    */
    public double dualop() throws TypeMismatchedException,
    DividedByZeroException,
    MissingOperatorException,
    MissingOperandException
    {
        
        int indexx = ss.size() - 1;
        String op1 = (String)ss.get(indexx);
        if (judgeOperator(indexx))
            throw new MissingOperandException();

        ss.remove(indexx--);
        String op = (String)ss.get(indexx);
        ss.remove(indexx--);
        String op2 = (String)ss.get(indexx);
        ss.remove(indexx--);
        char h1 = op1.charAt(0);
        char h2 = op2.charAt(0);
        if (h1 == '-' )
            h1 = op1.charAt(1);
        if (h2 == '-')
            h2 = op2.charAt(1);
        if ((h1 == 'f' || h1 == 't' || h1 == 'T' || h1 == 'F') || (h2=='F' || h2 == 'T' || h2 == 't' || h2 == 'f'))
            throw new TypeMismatchedException();
        if (op1.equals("-") || op1.equals("+") || op1.equals("*") || op1.equals("/"))
            throw new MissingOperandException();

        if (h1 > '9' || h1 < '0' || h2 > '9' || h2 < '0')
            throw new MissingOperandException();
        double ope1 = Double.parseDouble(op1);
        double ope2 = Double.parseDouble(op2);

        if (op.equals("+")){ 
        
            return ope1 + ope2;}
        if (op.equals("-"))
            return ope2 - ope1;
        if (op.equals("*"))
            return ope1 * ope2;
        if (op.equals("/")){
            if (ope1 == 0.0)
                throw new DividedByZeroException();
            return ope2/ope1;
        }
        
        if (op.equals("^")){
            return Math.pow(ope2,ope1);
        }
        else
            throw new MissingOperatorException();
        
    }
    
    /**
    *judge whether a string is a function name
    *@return String
    */
    public  String judgefunc(String str){
        String []match = {"max","min","sin","cos"};
        for (int i = 0;i < 4;i ++){
            if (str.equalsIgnoreCase(match[i]))
                return match[i];
        }
        return "";
    }


    /**
    *compare two value of two strings as double
    *@return boolean
    *@throws TypeMismatchedException
    */
    public boolean compare(String s1,String s2) throws TypeMismatchedException{
        if (s1.equalsIgnoreCase("true") || s1.equalsIgnoreCase("false") || s2.equalsIgnoreCase("true") || s2.equalsIgnoreCase("false"))
            throw new TypeMismatchedException();
        double ss1 = Double.parseDouble(s1);
        double ss2 = Double.parseDouble(s2);
        return ss1 > ss2;
    }

    /**
    *deal with parenthesis or function
    *@return String
    *@throws MissingLeftParenthesisException ,FunctionCallException,MissingOperandException,TypeMismatchedException
    */
    public String parent() throws MissingLeftParenthesisException,
        MissingRightParenthesisException,
        FunctionCallException,
        MissingOperandException,
        TypeMismatchedException{
        String rightp = (String)ss.get(ss.size() - 1);
        int mem = ss.size() - 2;//the position of last parameter
        
        int i = ss.size() - 1;
        
        String finalre = "";
        if (!rightp.equals(")"))
            throw new MissingRightParenthesisException();
        int start;
        int j;
        for (j = i-1;j >=0; j --){
            String temp = (String)ss.get(j);
            if (temp.equals("(")){    //look for the first matched left parenthesis
                start = j;//mark down the first position of the first matched left parenthesis or function
               if (start == i - 1)
                   throw new MissingOperandException();
                
                String jud = "";
                if (j >= 1 )
                    jud = judgefunc((String)ss.get(j - 1));
                if (!jud.equals("")){ //see whether called by function
                    if (jud.equalsIgnoreCase("max")){
                      if (i - j <= 2)
                          throw new MissingOperandException();
                      String re="0";
                       boolean symbol = false;
                       for (int z = start + 1;z <= mem;z ++ ){
                            int k = z - (start + 1);
                            if (k%2 == 0){
                                char first = ss.get(z).charAt(0);
                                if (first == '-')
                                    first = ss.get(z).charAt(1);
                                if (first > '9' || first < '0')
                                    throw new TypeMismatchedException();
                                if (symbol == false){
                                    re = (String)ss.get(z);
                                    symbol = true;
                                }
                                else {
                                    if (compare((String)ss.get(z),re))
                                       re = (String)ss.get(z);
                                }
                            }
                            else{
                                if (!ss.get(z).equals(","))
                                    throw new FunctionCallException();
                            }
                        }
                        finalre = re;
                        for (int kk = mem + 1;kk >= start-1;kk --)
                            ss.remove(start-1);
                        return finalre;
                    }
                
                    if (jud.equalsIgnoreCase("min")){
                       if (i - j <= 2)
                            throw new MissingOperandException();
                       String re="0";
                       boolean symbol = false;
                       for (int z = start + 1;z <= mem;z ++ ){
                            int k = z - (start + 1);
                            if (k%2 == 0){
                                char first = ss.get(z).charAt(0);
                                if (first > '9' || first < '0')
                                    throw new TypeMismatchedException();
                                if (symbol == false){
                                    re = (String)ss.get(z);
                                    symbol = true;
                                }
                                else {
                                    if (!compare((String)ss.get(z),re))
                                        re = (String)ss.get(z);
                                }
                            }
                            else{
                                if (!ss.get(z).equals(","))
                                    throw new FunctionCallException();
                            }
                        }
                        finalre =re;
                        for (int kk = mem + 1;kk >= start-1;kk --)
                            ss.remove(start-1);
                        return finalre;
                    }

                    if (jud.equalsIgnoreCase("sin")){
                       
                       if (i - j != 2)
                           throw new FunctionCallException();
                       String para = (String)ss.get(j + 1);
                       char first = para.charAt(0);
                       if (first < '0' && first > '9' )
                           throw new TypeMismatchedException();
                       finalre = Double.toString(Math.sin(Double.parseDouble(para)));
                       for (int kk = mem + 1;kk >= start - 1;kk --)
                           ss.remove(start-1);
                       return finalre;
                    }
                    
                    if (jud.equalsIgnoreCase("cos")){
                       
                       if (i - j != 2)
                           throw new FunctionCallException();
                       String para = (String)ss.get(j + 1);
                       char first = para.charAt(0);
                       if (first < '0' && first > '9' )
                           throw new TypeMismatchedException();
                       finalre = Double.toString(Math.cos(Double.parseDouble(para)));
                        
                       for (int kk = mem + 1;kk >= start - 1;kk --)
                           ss.remove(start-1);
                       return finalre;
                    }
                }else {//not called by function

                     String result = (String)ss.get(i - 1);

                     if (result.equalsIgnoreCase("true") || result.equalsIgnoreCase("false")){
                        ss.remove(i-2);
                        ss.remove(i-2);
                        ss.remove(i-2);
                        return result;
                     }
                     char first = result.charAt(0);
                     /*if(! (first >= '0' && first <= '9'))
                         throw new TypeMismatchedException();*/
                     finalre = result;
                     for (int kk = i;kk >= j;kk --){
                         ss.remove(j);
                     }
                     
                     return finalre;
                     
                }
            }
        }

        if (j == -1)
            throw new MissingLeftParenthesisException();
        return finalre;
    }

    /**
    *judge whether a token is a operator
    *@return boolean
    */
    public boolean judgeOperator(int index){
        String []str = {"+","-","*","/",">=","<=",">","<","<>","?",":",",","(","&","|","^"};
        String op;
        boolean flag = false;
        if (index >= 0){
            op = (String)ss.get(index);
            for (int i = 0;i < 16;i ++){
                if (str[i].equals(op)){
                    flag = true;

                    break;
                }
                    
            }
        }
        
        return flag;
    }

    /**
    *deal with oppsiting the number
    *@return double
    *@throws TypeMismatchedException,MissingOperandException,MissingOperatorException
    */
    public double minus() throws TypeMismatchedException,
        MissingOperandException,
        MissingOperatorException{
            int size = ss.size();
            String op = (String)ss.get(size - 1);
            if (op.equals("--"))
                throw new MissingOperandException();
            char first = op.charAt(0);
            if (first < '0' || first > '9')
                throw new TypeMismatchedException();
            ss.remove(size - 1);
            size = ss.size();
            
           
            String operator;
            if (size > 0){
                
               operator = (String)ss.get(size - 1);
               ss.remove(size - 1);
               double temp = Double.parseDouble(op);
               temp = -temp;
              return temp;
          }
          else 
            throw new MissingOperatorException();
    }

    /**
    *deal with relation operator
    *@return boolean
    *@throws TypeMismatchedException,MissingOperandException,MissingOperatorException
    */
    public boolean cmp() throws TypeMismatchedException,
        MissingOperandException,
        MissingOperatorException{
            int size = ss.size();
            String op1 = (String)ss.get(size - 1);
            ss.remove(size - 1);
            size --;
            String op = "";
            String op2 = "";
            if (size > 0){
                op = (String)ss.get(size - 1);
                ss.remove(size - 1);
                size --;
            }
            if (size > 0){
                op2 = (String)ss.get(size - 1);
                ss.remove(size - 1);
                size --;
            }

            if (op2.equals(""))
                throw new MissingOperandException();
            char first1 = op1.charAt(0);
            char first2 = op2.charAt(0);
            if (first1 > '9' || first1 < '0' || first2 > '9' || first2 < '0')
                throw new TypeMismatchedException();

            double ope1 = Double.parseDouble(op1);
            double ope2 = Double.parseDouble(op2);
            
            if (op.equals(">"))
                 return ope2 > ope1;
            if (op.equals("<"))
                 return ope2 < ope1;
            if (op.equals(">="))
                 return ope2 >= ope1;
            if (op.equals("<="))
                 return ope2 <= ope1;
            if (op.equals("<>"))
                 return ope2 - ope1 < 0.000001;
            else
                throw new MissingOperatorException();

    }

    /**
    *deal with "&" and "|"
    *@return double
    *@throws TypeMismatchedException,MissingOperandException,MissingOperatorException
    */
    public boolean rela() throws TypeMismatchedException,
        MissingOperandException,
        MissingOperatorException{
        int size = ss.size();
        String op1 = (String)(String)ss.get(size - 1);
        ss.remove(size - 1);
        size --;
        String op = "";
        String op2 = "";
        if (size > 0){
            op = (String)(String)ss.get(size - 1);
            ss.remove(size - 1);
            size --;
        }
        if (size > 0){
            op2 = (String)(String)ss.get(size - 1);
            ss.remove(size-1);
            size --;
        }

        if (op2.equals(""))
            throw new MissingOperandException();
        boolean f1 = boolvalue(op1);
        boolean f2 = boolvalue(op2);
        if (f1 == false || f2 == false)
            throw new TypeMismatchedException();
        boolean ff1 = false,ff2 = false;
        if (op1.equalsIgnoreCase("true"))
            ff1 = true;
        if (op1.equalsIgnoreCase("false"))
            ff1 = false;
        if (op2.equalsIgnoreCase("true"))
            ff2 = true;
        if (op2.equalsIgnoreCase("false"))
            ff2 = false;

        if (op.equals("&"))
            return ff1 && ff2;
        if (op.equals("|"))
            return ff1 || ff2;
        else
            throw new MissingOperatorException();
            
        

    }

    /**
    *deal with "!"
    *@return boolean
    *@throws TypeMismatchedException,MissingOperandException,MissingOperatorException
    */
    public boolean not() throws TypeMismatchedException,
        MissingOperandException,
        MissingOperatorException{
            int size = ss.size();
            String op = (String)(String)ss.get(size - 1);
            ss.remove(size - 1);
            size --;
            if (!(op.equalsIgnoreCase("true") || op.equalsIgnoreCase("false")))
                throw new TypeMismatchedException();
            if (size <= 0 || !ss.get(size-1).equals("!"))
                throw new MissingOperatorException();
            if (op.equalsIgnoreCase("true"))
                return false;
            else
                return true;
    }

    /**
    *deal with "?:"
    *@return double
    *@throws TypeMismatchedException,MissingOperandException,MissingOperatorException，TrinaryOperationException
    */
    public double triop() throws TypeMismatchedException,
        MissingOperatorException,
        MissingOperandException,
        TrinaryOperationException{
            int size = ss.size();
            String choose1 = (String)(String)ss.get(size - 1);
            ss.remove(size - 1);
            size --;
            String op1 = (String)(String)ss.get(size - 1);
            if (!op1.equals(":"))
                throw new TrinaryOperationException();
            ss.remove(size - 1);
            size --;
            if (size <= 0)
                throw new TrinaryOperationException();
            String choose2 = (String)(String)ss.get(size - 1);
            ss.remove(size - 1);
            size --;
            if (size <= 0)
                throw new TrinaryOperationException();
            String op2 = (String)(String)ss.get(size - 1);
            ss.remove(size - 1);
            size --;
            if (size <= 0)
                throw new TrinaryOperationException();

            String condition = (String)(String)ss.get(size - 1);
            ss.remove(size - 1);
            size --;
            //see whether the parameters are in valid format
            char fir1 = choose1.charAt(0);
            char fir2 = choose2.charAt(0);
            if (fir1 < '0' || fir1 > '9' || fir2 < '0' || fir2 > '9')
                throw new TypeMismatchedException();
           
            if (!(condition.equalsIgnoreCase("true") || condition.equalsIgnoreCase("false")))
                throw new TypeMismatchedException();
            double ch1 = Double.parseDouble(choose1);
            double ch2 = Double.parseDouble(choose2);

            if (condition.equalsIgnoreCase("true"))
                return ch2;
            else
                return ch1;
    }

    /**
    *judge whether a string is bool value
    *@return boolean
    */
    public boolean boolvalue(String s){
        if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))
            return true;
        else
            return false;
    }

    /**
    *end the syntatic analization
    *@return no return value
    */
    public void over(){
        result = Double.parseDouble((String)ss.get(ss.size() - 1));
        solve = true;
    }

    /**
    *judge whether a string is number
    *@return boolean
    */
    public boolean isNum(String str){
        if (str.charAt(0) == '-'){
            if (str.length() >= 2 && str.charAt(1) >= '0' && str.charAt(1) <= '9')
                return true;
            else
                return false;
        }
        else if (str.charAt(0) >= '0' && str.charAt(0) <= '9')
            return true;
        else
            return false;
    }

    /**
    *main process dealing with parsing
    *@return double
    *@throws IllegalIdentifierException,
    *        MissingOperandException,
    *        MissingOperatorException,
    *        MissingRightParenthesisException,
    *        FunctionCallException,
    *        TrinaryOperationException,
    *        TypeMismatchedException,
    *        SyntacticException,
    *        SemanticException,
    *        IllegalDecimalException,
    *        IllegalSymbolException
    */
    
    public double parsing()throws
            IllegalIdentifierException,
            MissingOperandException,
            MissingOperatorException,
            MissingRightParenthesisException,
            FunctionCallException,
            TrinaryOperationException,
            TypeMismatchedException,
            SyntacticException,
            SemanticException,
            IllegalDecimalException,
            IllegalSymbolException
    {
        for (count = 0;count < new_expr.size() ;){

            int index = ss.size() - 1;
            String token = getNextToken(count);
            

            String top = (String)ss.get(index);
            
            int state;
            if (isNum(top) && isNum(token))
                throw new MissingOperatorException();

            
            boolean f1 = boolvalue(top);
            
            int tempindex = index; 
            if (isNum(top)){
                 
                int num;
                if (token.equals("-")) {//judge whether - is unary or binary
                   
                    if (judgeOperator(tempindex)){
                        num = OPP.getState("digit","--");
                        token = "--";
                    }
                    else
                        num = OPP.getState("digit","+-");
                }else
                    num = OPP.getState("digit",token);
                if (num == -3)
                    throw new MissingOperatorException();
                index --;

            }
            if (f1){

                int num;
                if (token.equals('-')) {//judge whether - is unary or binary
                    if (judgeOperator(tempindex))
                        num = OPP.getState(top,"-");
                    else
                        num = OPP.getState(top,"+-");
                }else
                    num = OPP.getState(top,token);
                if (num == -3)
                    throw new MissingOperatorException();
                
                index --;
               
            }
            
            top = (String)ss.get(index);
            
            if (token.equals("-")) {//judge whether - is unary or binary
                if (judgeOperator(tempindex)){
                    
                    state = OPP.getState(top,"--");
                    token = "--";

                }
                else
                    state = OPP.getState(top,"+-");
            }
            else if (isNum(token)) //数字转类型
                state = OPP.getState(top,"digit"); 
            else    
                state = OPP.getState(top,token);
            
            switch(state){
                case 0:
                    shift(token);
                    break;
                case 1:
                    over();
                    break;
                case 3:
                    reduce(3);
                    break;
                case 4:
                    reduce(4);
                    break;
                case 5:
                    reduce(5);
                    break;
                case 6:
                    reduce(6);
                    break;
                case 7:
                    reduce(7);
                    break;
                case 8:
                    reduce(8);
                    break;
                case 9:
                    reduce(9);
                    break;
                case -3:
                    throw new MissingOperatorException();
                case -2:
                    throw new MissingRightParenthesisException();
                case -6:
                    throw new TypeMismatchedException();
                case -5:
                    throw new TrinaryOperationException();
                case -7:
                    throw new SyntacticException();
                case -4:
                    throw new FunctionCallException();
            }
            if (solve)
                break;
        }

        if (solve)
            return result;
        else{
            return 0;
        }

    } 
}

