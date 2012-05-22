package com.cortexity.fsm;

import java.io.*;

import junit.framework.*;

import org.mozilla.javascript.*;

public abstract class JavaScriptTestCase extends TestCase {

   protected Context cx;

   protected Scriptable scope;
   
   protected StringBuffer buffer;

   public JavaScriptTestCase(String name) {
      super(name);
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      this.buffer = new StringBuffer(128);
      this.cx = Context.enter();
      this.scope = cx.initStandardObjects();
      putJavaObjectInJs("test", this);
      putJavaObjectInJs("out", System.out);
      doSetUp();
   }

   @Override
   protected void tearDown() throws Exception {
      Context.exit();
      doTearDown();
      super.tearDown();
   }

   protected abstract void doSetUp() throws Exception;

   protected abstract void doTearDown() throws Exception;

   protected void addLine(String jsCode) {
      buffer.append(jsCode).append(";\n");
   }
   
   /**
    * Loads a file relative to your test class.
    * 
    * @param fileName
    * @return result of the load. Often times you don't care much about this
    */
   protected Object executeScriptFile(String fileName) {
      File scriptFile = new File(fileName);
      assertTrue("can't find " + scriptFile.getAbsolutePath(), scriptFile.exists());
      assertTrue("can't read " +scriptFile.getAbsolutePath(), scriptFile.canRead());
      try {
         FileReader fr = new FileReader(scriptFile);
         char[] buff = new char[(int) scriptFile.length()];
         fr.read(buff);
         String script = new String(buff);
         return executeJSCode(script, fileName);
      } catch (Exception e) {
         StringWriter stringWriter = new StringWriter(64);
         PrintWriter writer = new PrintWriter(stringWriter);
         e.printStackTrace(writer);
         writer.flush();
         throw new AssertionFailedError("Error while loading '" + scriptFile.getAbsolutePath()
               + "':\n" + stringWriter.toString());
      }
   }
   
   protected Object executeJSCode() {
      return this.executeJSCode(buffer.toString(), getClass().getName() + ':' + getName());
   }

   protected Object executeJSCode(String codeSnippet, String source) {
      return cx.evaluateString(scope, codeSnippet, source, 1, null);
   }

   protected void putJavaObjectInJs(String jsSymbolNameofJavaObject, Object javaObject) {
      Object wrappedObject = Context.javaToJS(javaObject, scope);
      ScriptableObject.putProperty(scope, jsSymbolNameofJavaObject, wrappedObject);
   }

   protected String invokeJsFunction(String functionName, Object[] functionArgs) {
      Object fObj = this.scope.get(functionName, this.scope);
      if (!(fObj instanceof Function)) {
         throw new AssertionFailedError(functionName + " is undefined or not a function.");
      }
      Function f = (Function) fObj;
      Object result = f.call(this.cx, this.scope, this.scope, functionArgs);
      return Context.toString(result);
   }

   /**
    * gets Files matching pattern
    * 
    * @param fileEndsWithPattern
    * @param dir
    * @return String[]
    */
   protected static String[] getFiles(final String fileEndsWithPattern, File dir) {
      String[] files = dir.list(new FilenameFilter() {

         /**
          * return file if it's a Directory or if it ends in fileEndsWithPattern
          * 
          * @param dirParameter
          * @param file
          * @return
          */
         public boolean accept(File dirParameter, String file) {
            File mightBeDir = new File(String.valueOf(dirParameter.getAbsoluteFile()) + '/' + file);
            return ((file.endsWith(fileEndsWithPattern) && mightBeDir.canRead()) || mightBeDir
                  .isDirectory());
         }

      });
      return files;
   }

   /**
    * This is non-recursive i.e. in that directories will not be followed. It will only work if your
    * JavaScript code doesn't depend on the order in which it is loaded into the interpreter, which
    * is rarely a real-world situation because JavaScript is so brain-dead.
    * 
    * @param dirPath
    */
   protected void addJavascriptFromDir(String dirPath) {
      File dir = new File(dirPath);
      assertTrue(dir.getAbsolutePath(), dir.isDirectory());
      assertTrue(dir.getAbsolutePath(), dir.canRead());
      String[] files = getFiles(".js", dir);
      assertNotNull(dir.getAbsolutePath(), files);
      assertTrue(dir.getAbsolutePath(), files.length > 0);
      for (int i = 0; i < files.length; i++) {
         File mightBeDir = new File(dir.getAbsolutePath() + '/' + files[i]);
         if (mightBeDir.isDirectory()) {
            continue;
         }
         executeScriptFile(mightBeDir.getAbsolutePath());
      }
   }
}
