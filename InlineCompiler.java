import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.util.Scanner;

public interface InlineCompiler 
{   
    public static void main(StringBuilder newcode)
    {        
        File fileToEdit = new File("UserCode.java");
        //Save Old Code
        StringBuilder oldcode = SaveOldCode(fileToEdit);
        
        //Begin editing the file        
        if (fileToEdit.getAbsoluteFile().getParentFile().exists() || fileToEdit.getAbsoluteFile().getParentFile().mkdirs()) 
        {
            try {
                Writer writer = null;
                try {
                    writer = new FileWriter(fileToEdit);
                    writer.write(newcode.toString());
                    writer.flush();
                } finally {
                    try {
                        writer.close();
                    } catch (Exception e) {
                    }
                }

                //Compilation Requirements
                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                List<String> optionList = new ArrayList<String>();
                optionList.add("-classpath");
                optionList.add(System.getProperty("java.class.path") + ";dist/InlineCompiler.jar");
                                
                Iterable<? extends JavaFileObject> compilationUnit
                        = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(fileToEdit));
                JavaCompiler.CompilationTask task = compiler.getTask(
                    null, 
                    fileManager, 
                    diagnostics, 
                    optionList, 
                    null, 
                    compilationUnit);
                    
                if (task.call()) {
                    /** Load and execute *************************************************************************************************/
                    // Create a new class loader, pointing to the package root
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("").toURI().toURL()});
                    // Load the class from the classloader by name....
                    Class<?> newClass = classLoader.loadClass("UserCode");
                    // Create a new instance...
                    Object obj = newClass.newInstance();
                    /************************************************************************************************* Load and execute **/
                } else {
                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        System.out.format("Error on line %d in %s%n",
                                diagnostic.getLineNumber(),
                                diagnostic.getSource().toUri());
                    }
                }
                fileManager.close();
            } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException exp) {
                exp.printStackTrace();
            }
        }
    }
    
    public static StringBuilder SaveOldCode(File fileToEdit)
    {
        StringBuilder oldcode = new StringBuilder(64);//Reallocate memory to flush, let java deal with GC
        try
        {
            Scanner scanner = new Scanner(fileToEdit);
            while(scanner.hasNextLine())
            {
                oldcode.append(scanner.nextLine());
            }
        }
        catch(java.io.FileNotFoundException fnfe)
        {
            System.out.println(fnfe + " does not exist");
        }
        return oldcode;
    }
}