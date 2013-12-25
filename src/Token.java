package parser;
import exceptions.*;
import java.util.ArrayList;
import java.io.*;
/**
 *Description:
 * <br/>Copyright(C),2013 ,Lao Si
 * <br/>This program is protected by copyright laws. 
 * <br/>Program Name: ExprEval
 * @author Lao Si
 * @version 1.0 
 */
public class Token{
    
    String expr;
    ArrayList<String> new_expr = new ArrayList<String>();
    String []opeator = {",","+","-","*","/","<","<=",">=",">","=","<>","&","|","!","(",")","?",":"};
    int index = 0;

    /**
    *Initialization
    *@return no return value
    */
    public void Token(){
        expr = "";
    }

    /**
    *return the expression after analization
    *@return no return value
    */
    public ArrayList<String> getExpre(){
        return new_expr;
    }
    
    /**
    *deal with blank
    *@return no return value
    */
    void isBlank(String input){
        while (input.charAt(index) == ' '){
            index ++;
        }   
    }

    /**
    *make digits together
    *@return String
    */
    String isDigit(String input){
        StringBuffer temp =  new StringBuffer();
        int num = 0;
        while (index < input.length() && input.charAt(index) <= '9'&& input.charAt(index) >= '0'){
            temp.append(input.charAt(index));
            index ++;
        }
        return temp.toString();
    }

    /**
    *search for a bool
    *@return String
    */
    String isBool(String input){
        char []c = new char[4];
        
        StringBuffer temp = new StringBuffer();
        for (int i = 0;i < 4;i ++){
            c[i] = input.charAt(index + i);
            temp.append(c[i]);
        }
        String boolvar = temp.toString();
        if (boolvar.equals("true") || boolvar.equals("false") || boolvar.equals("True") || boolvar.equals("False"))
            return boolvar;
        else
            return "";
    }

    /**
    *search for a function
    *@return String
    */
    String isFunc(String input){
        char []c = new char[3];

        StringBuffer temp =  new StringBuffer();
        for (int i = 0;i < 3;i ++){
            c[i] = input.charAt(index ++);
            temp.append(c[i]);
        }
        String funvar = temp.toString();
        if (funvar.equals("min") || funvar.equals("max"))
            return funvar;
        else
            return "";

    }

    /**
    *search for a operator
    *@return boolean
    */
    boolean isOperator(char c){
        StringBuffer ss = new StringBuffer();
        ss.append(c);
        String flag = ss.toString();
        for (int i = 0;i < 18;i ++){
            if (opeator[i].equals(flag))
                return true;
        }
        return false;
    }

    /**
    *get a operator
    *@return String
    */
    String getOpeator(char c,String input){
        if (c == '<' && input.charAt(index + 1) != '='){
            index ++;
            return "<";
        }
         
        if (c == '>' && input.charAt(index + 1) != '='){
            index ++;
            return ">";
        }
        if (c == '<' && input.charAt(index+1) == '='){
             index = index + 2;
              return "<=";
        }
        if (c == '>' && input.charAt(index+1) == '='){
            index = index + 2;
            return ">=";
        }
        if (c == '<' && input.charAt(index+1) == '>'){
            index = index + 2;
            return "<>";
        }
        StringBuffer ss = new StringBuffer();
        ss.append(c);
        index ++;
        return ss.toString();

    }

    /**
    *search for a sin or cos
    *@return String
    */
    String isSinOrCos(String input){
        
        char []c = new char[3];

        StringBuffer temp = new StringBuffer();
        for (int i = 0;i < 3;i ++){
            c[i] = input.charAt(index ++);
            temp.append(c[i]);
        }
        String funvar = temp.toString();
        if (funvar.equals("sin") || funvar.equals("cos"))
            return funvar;
        else
            return "";
    }

    /**
    *search for a fraction
    *@return boolean
    */
    boolean findFraction(String input){
       int temp = index + 1;
       if (input.charAt(temp) == '+' || input.charAt(temp) == '-')
           temp ++;
       char test = input.charAt(temp);
       char begin = test;
       while (test <= '9' && test >= '0'){
            temp ++;
            if (temp >= input.length())
                break;
            test = input.charAt(temp);
       }
       if (begin == '+' || begin == '-' || begin == '.' || test == '.')
           return false;
       return true;

            
    }
    
    /**
    *main process for lexical analizition
    *@return String
    */
    public void Analysize(String input)throws 
        EmptyExpressionException,
        IllegalDecimalException, 
        IllegalIdentifierException,
        IllegalSymbolException {
        int size = input.length();
        
        if (size == 0){
            throw new EmptyExpressionException();
        }
        if (input.equals("   "))
            throw new EmptyExpressionException();
        input = input.replaceAll(" ","");
        size = input.length();
        char sym;
        while (index < size){
                 
            sym = input.charAt(index);
            if (sym == ' ')
                isBlank(input);
            else if (sym <= '9' && sym >= '0'){
                new_expr.add(isDigit(input));
                
            }
            else if (sym == 't' || sym == 'f' || sym == 'T' || sym == 'F'){
                String temp = isBool(input);
                if (!temp.equals(""))
                    new_expr.add(temp);
                else
                    throw new IllegalIdentifierException();
                index = index + 4;
            }
            else if (sym == '.'){
                if (index == 0 || index == size - 1)
                    throw new IllegalDecimalException();
                char pre,after;
                pre = input.charAt(index - 1);
                after = input.charAt(index + 1);
                boolean prev = false;
                boolean aft = false;
                if (pre <= '9' && pre >= '0')
                    prev = true;
                if (after <= '9' && after >= '0')
                    aft = true;
                if (prev && aft){
                    String temp = ".";
                    String new_frac = new_expr.get(new_expr.size()-1);
                    index ++;
                    String new_num = isDigit(input);
                    new_expr.set(new_expr.size()-1,new_frac+"."+new_num);
                }
                else
                    throw new IllegalDecimalException();

                //index ++;

            }
            else if (sym == '^'){
                
                if (index == 0 || index == size - 1)
                    throw new IllegalDecimalException();
                char pre,after;
                pre = input.charAt(index - 1);
                after = input.charAt(index + 1);
                boolean prev = false;
                boolean aft = false;
                if (pre <= '9' && pre >= '0' || pre == ')' )
                    prev = true;
                if (after <= '9' && after >= '0' || after == '(' )
                    aft = true;
                if (prev && aft){
                    String temp = "^";
                    new_expr.add(temp);
                }
                else
                    throw new IllegalDecimalException();
                index ++;
            }
            else if (sym == 'm'){
                String temp = isFunc(input);
                if (temp != "")
                    new_expr.add(temp);
                else
                    throw new IllegalIdentifierException();
            }
            else if(isOperator(sym)){
                String temp = getOpeator(sym,input);
                new_expr.add(temp);
                
            }
            else if (sym == 's' || sym == 'c'){
                String temp = isSinOrCos(input);
                if (temp != "")
                    new_expr.add(temp);
                else
                    throw new IllegalDecimalException();
            }
            else if(sym == 'E' || sym == 'e'){

                if (index == 0)
                    throw new IllegalIdentifierException();
               if (input.charAt(index - 1) > '9' || input.charAt(index - 1) < '0')
                   throw new IllegalDecimalException();
               char after = input.charAt(index + 1);
               if (!(after == '+' || after == '-' || after >= '0' && after <= '9' ))
                    throw new IllegalDecimalException();
               new_expr.add("*");
               new_expr.add("10");
               new_expr.add("^");
               StringBuffer af = new StringBuffer();
               int temp = index;
               if (after == '+')
                   temp = index + 1;
               /*else{
                   af.append(after);
                   new_expr.add(af.toString());
               }*/
               boolean flag = findFraction(input);
               if (flag == false)
                   throw new IllegalDecimalException();
               
               index = temp + 1;

            }
            else{

                throw new IllegalSymbolException();
            }
        }
        

    }
}
