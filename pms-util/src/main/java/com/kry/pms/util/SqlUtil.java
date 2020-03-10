package com.kry.pms.util;

public class SqlUtil {
    /****
     * 获取截取位置
     * @param selectSQL
     * @return
     */
    public static  int   getSubIndex(String  selectSQL){
        System.out.println(selectSQL.length());
        int count = 0;
        for(int i=0;i<selectSQL.length();i++){
            char  c = selectSQL.charAt(i);
            if(c =='s'||c=='S'){
                if((selectSQL.charAt(i+1)=='e'||selectSQL.charAt(i+1)=='E')&&(selectSQL.charAt(i+2)=='l'||selectSQL.charAt(i+2)=='L')&&
                        (selectSQL.charAt(i+3)=='e'||selectSQL.charAt(i+3)=='E')&&(selectSQL.charAt(i+4)=='c'||selectSQL.charAt(i+4)=='C')&&
                        (selectSQL.charAt(i+5)=='t'||selectSQL.charAt(i+5)=='T')){
                    count++;
                    i=i+5;
                }else{
                    continue;
                }
            }else if(c =='f'||c=='F'){
                if((selectSQL.charAt(i+1)=='r'||selectSQL.charAt(i+1)=='R')&&(selectSQL.charAt(i+2)=='o'||selectSQL.charAt(i+2)=='O')&&
                        (selectSQL.charAt(i+3)=='m'||selectSQL.charAt(i+3)=='M')){
                    count--;
                    i=i+3;
                    System.out.println(count);
                    if(count == 0){
                        System.out.println(i);
                        return i+1;
                    }
                }else{
                    continue;
                }
            }else {
                continue;
            }

        }
        return -1;

    }


    /**
     * 进行字符串截取
     * @param inSQL
     * @return
     */
    public static  String   getCountSQL(String inSQL){
        int  index = getSubIndex(inSQL);
        if(index != -1){
            String outSQL  = "select count(-1) from " + inSQL.substring(index);
            return outSQL;
        }else{
            System.out.println("not a  corrent sql");
            return  "not a corrent sql";
        }
    }

}
