import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.charset.Charset;

public final class CheckTranslations {
    private static boolean debug = false;
    private static boolean plurals = false;
    private static boolean empty = false;
    private static boolean remove = false;
    private static int checks=0;
    private static int matches =0 ;
    private static Pattern p,e;
    
    public static void main(String[] args){
        if (args.length < 1 || (args[0].equals("-d") && args.length < 2)){
            System.out.println("Não tem argumentos suficientes");
            return;
        }
        for (int i = 0; i<args.length;i++){
            switch (args[i]){
                case "-d":
                    debug = true;
                    break;
                case "-p":
                    plurals = true;
                case "-e":
                    empty = true;
                    break;
                case "-r":
                    remove = true;
                    break;
            }
        }
        if (!plurals && !empty){
            plurals = true;
            empty = true;
        }

        p = Pattern.compile("(<item quantity=\")(zero|one|two|three|few|many|other)(\"></item>|\"/>");
        e= Pattern.compile("(string[\\sa-z_\\\"=]*)((><\\/string)|\\/>){1})");

        for (int i=0;i<args.length;i++){
            if(!args[i].equals("-d")&&!args[i].equals("-p") && !args[i].equals("-e") && !args[i].equals("-r")){
                File f = new File(args[i]);
                if (f.exists() && !f.isDirectory()){
                    checkFile(f);
                } else if (f.isDirectory()){
                    checkFiles(f.listFiles());
                } else{
                    System.out.println("'" + args[i] + "' does not exist!");
                }
            }
        }
        System.out.println(checks + " arquivos estão quebrados.");
        System.out.println(matches + " linhas corrompidas detectadas.");
        if (remove) System.out.println(matches + " linhas corrompidas removidas.");
    }
    private static void checkFile(File f){
        if (f.toString().contains("values/strings.xml")){
            return;
        }
        if (debug) System.out.println("Checando " + f.toSring());
        checks++;

        List<String> lines = new ArrayList<String>();
        boolean checkFailed = false;

        try (BufferedReader br = new BufferedReader(new FileReader(f))){
            String line;
            int ln = 0;
            while ((line = br.readLine()) != null){
                ln++;
                if (plurals && p.matcher(line).find()){
                    matches++;
                    if (debug) System.out.println("    Linha "+ln+" foram "+((remove)?"removidas":"detectadas")+": '"+line+"'");
                    checkFailed=true;
                } else if(empty && e.matcher(line).find()){
                    matches++;
                    if (debug) System.out.println("   Linha "+ln+" foram "+((remove)?"removidas":"detectadas")+": '" +line+"'");
                } else{
                    if (remove) lines.add(line);
                }
            }
            br.close();
            if (remove&& checkFailed){
                Files.write(f.toPath(), lines, Charset.forName("UTF-8"));
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
}
