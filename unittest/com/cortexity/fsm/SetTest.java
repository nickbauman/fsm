package com.cortexity.fsm;

import org.mozilla.javascript.*;

public class SetTest extends JavaScriptTestCase {

   public SetTest(String name) {
      super(name);
   }

   public void testSet() throws Exception {
      addLine("var set = new Set()");
      addLine("test.assertEquals(0, set.size())");
      addLine("test.assertTrue(set.isEmpty())");
      addLine("set.add('foo')");
      addLine("test.assertEquals(1, set.size())");
      addLine("test.assertTrue(set.contains('foo'))");
      addLine("test.assertFalse(set.contains('bar'))");
      addLine("var anArray = Array()");
      addLine("anArray.push('foz')");
      addLine("anArray.push('bar')");
      addLine("anArray.push('baz')");
      addLine("set = new Set(anArray)");
      addLine("test.assertEquals(3, set.size())");
      addLine("test.assertTrue(set.contains('foz'))");
      addLine("test.assertTrue(set.contains('bar'))");
      addLine("test.assertTrue(set.contains('baz'))");
      addLine("test.assertFalse(set.contains('dkkd'))");
      addLine("set2 = new Set(set)");
      addLine("test.assertEquals(3, set2.size())");
      addLine("var object = new Object();");
      addLine("set3 = new Set(object)");
      try {
         executeJSCode();
         fail();
      } catch (JavaScriptException e) {
         assertEquals("Cannot coerce Object into Set (jssrc/Set.js#11)", e.getMessage());
      }
   }

   @Override
   protected void doSetUp() throws Exception {
      executeScriptFile("jssrc/Set.js");
   }

   @Override
   protected void doTearDown() throws Exception {
   }
}
