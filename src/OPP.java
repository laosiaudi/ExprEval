package parser;
/**
 *Description:
 * <br/>Copyright(C),2013 ,Lao Si
 * <br/>This program is protected by copyright laws. 
 * <br/>Program Name: ExprEval
 * @author Lao Si
 * @version 1.0 
 */
public class OPP{
    /**
    *Opp table
    */
    static int table[][] = {
          /*      (   )   func   --   ^  /* +- relation  !   &   |   ?   :   ,  digit  boolean  $*/
        /*(*/{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -5, 0, 0, 0, -2},
        /*)*/ {-3, 4, -3, -3, 4, 4, 4, 4, -3, 4, 4, 4, 4, 4, -3, -3, 4},
        /*Function*/ {0, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4},
        /*-*/ {0, 5, 0, 0, 5, 5, 5, 5, -3, 5, 5, 5, 5, 5, 0, -6, 5},
        /*^*/ {0, 3, 0, 0, 0, 3, 3, 3, -3, 3, 3, 3, 3, 3, 0, -6, 3},
        /*m/d*/ {0, 3, 0, 0, 0, 3, 3, 3, -3, 3, 3, 3, 3, 3, 0, -6, 3},
        /*p/m*/ {0, 3, 0, 0, 0, 0, 3, 3, -3, 3, 3, 3, 3, 3, 0, -6, 3},
        /*relation*/ {0, 6, 0, 0, 0, 0, 0, 6, 6, 6, 6, 6, 6, 6, 0, -6, 6},
        /*!*/ {0, 8, -3, 0, 0, 0, 0, 0, 0, 8, 8, 8, 8, -6, -6, 0, 8},
        /*&*/ {0, 7, 0, 0, 0, 0, 0, 0, 7, 7, 7, 7, 7, 7, 0, 0, 7},
        /*|*/ {0, 7, 0, 0, 0, 0, 0, 0, 7, 0, 7, 7, 7, 7, 0, 0, 7},
        /*?*/ {0, -5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -5, 0, 0, -5},
        /*:*/ {0, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -5, 0, 0, 9},
        /*,*/ {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -5, 0, 0, 0, -4},
        /*digit*/ {-3, 2, -3, -3, 2, 2, 2, 2, -3, 2, 2, 2, 2, 2, -3, -3, 2},
        /*boolean*/ {-3, 2, -3, -3, -6, -6, -6, -6, -3, 2, 2, 2, -6, -6, -3, -3, 2},
        /*$*/ {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -7, 0, 0, 1}

        
    };
    static String op[]={"(",")","func","--","^","/*","+-","relation","!","&","|","?",":",",","digit","boolean","$"};
    /**
    *return the type
    *@return String
    */
    static String getType(String str){
        if (str.equals("sin") || str.equals("cos") || str.equals("max") || str.equals("min"))
            return "func";
        if (str.equals(">=") || str.equals("<=") || str.equals(">") || str.equals("<") || str.equals("<>"))
            return "relation";
        if (str.equals("*") || str.equals("/"))
            return "/*";
        if (str.equals("True") || str.equals("False") || str.equals("true") || str.equals("false"))
            return "boolean";
        if (str.equals("+"))
            return "+-";
        if (str.equals("--"))
            return "--";
        if (str.equals("-"))
            return "+-";
        return str;
    }

    /**
    *return the state
    *@return int
    */
    static int getState(String top, String next){
        int line = -1;
        int col = -1;
        String new_top = getType(top);
        String new_next = getType(next);
        for (int i = 0;i < 17;i ++){
            if (op[i].equals(new_top))
                line = i;
            if (op[i].equals(new_next))
                col = i;
        }
        return table[line][col];
    }
}
