package models;

import java.util.HashMap;
import java.util.Map;

public class StaticMethods {
    public static String transliteration(String str){
        Map<String, String> gost = new HashMap<String, String>();
        gost.put("А", "A");
        gost.put("Б", "B");
        gost.put("В", "V");
        gost.put("Г", "G");
        gost.put("Д", "D");
        gost.put("Е", "E");
        gost.put("Ё", "JO");
        gost.put("Ж", "ZH");
        gost.put("З", "Z");
        gost.put("И", "I");
        gost.put("Й", "JJ");
        gost.put("К", "K");
        gost.put("Л", "L");
        gost.put("М", "M");
        gost.put("Н", "N");
        gost.put("О", "O");
        gost.put("П", "P");
        gost.put("Р", "R");
        gost.put("С", "S");
        gost.put("Т", "T");
        gost.put("У", "U");
        gost.put("Ф", "F");
        gost.put("Х", "KH");
        gost.put("Ц", "C");
        gost.put("Ч", "CH");
        gost.put("Ш", "SH");
        gost.put("Щ", "SHH");
        gost.put("Ъ", "'");
        gost.put("Ы", "Y");
        gost.put("Ь", "");
        gost.put("Э", "EH");
        gost.put("Ю", "YU");
        gost.put("Я", "YA");
        gost.put("а", "a");
        gost.put("б", "b");
        gost.put("в", "v");
        gost.put("г", "g");
        gost.put("д", "d");
        gost.put("е", "e");
        gost.put("ё", "jo");
        gost.put("ж", "zh");
        gost.put("з", "z");
        gost.put("и", "i");
        gost.put("й", "jj");
        gost.put("к", "k");
        gost.put("л", "l");
        gost.put("м", "m");
        gost.put("н", "n");
        gost.put("о", "o");
        gost.put("п", "p");
        gost.put("р", "r");
        gost.put("с", "s");
        gost.put("т", "t");
        gost.put("у", "u");
        gost.put("ф", "f");
        gost.put("х", "kh");
        gost.put("ц", "c");
        gost.put("ч", "ch");
        gost.put("ш", "sh");
        gost.put("щ", "shh");
        gost.put("ъ", "");
        gost.put("ы", "y");
        gost.put("ь", "");
        gost.put("э", "eh");
        gost.put("ю", "yu");
        gost.put("я", "ya");
        gost.put(" ", "_");
        gost.put(".", "_");
        gost.put(",", "");
        gost.put("\"", "");
        gost.put("<", "");
        gost.put(">", "");
        gost.put("\\", "");
        gost.put("/", "");
        char[] chars = str.toCharArray();
        System.out.println(chars);

        for(int i=0; i<gost.size(); i++)
            str = str.replace(gost.keySet().toArray()[i].toString(), gost.get(gost.keySet().toArray()[i]));

        return str;
    }
}
