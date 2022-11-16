package tutorial.lab10;

import java.awt.event.*;
import java.lang.reflect.*;

/**
 * @version 1.00 2004-08-17
 * @author Cay Horstmann
 */
public class ActionListenerInstaller
{
   /**
    * Processes all ActionListenerFor annotations in the given object.
    * @param obj an object whose methods may have ActionListenerFor annotations
    */
   public static void processAnnotations(Object obj)
   {
      try
      {
         Class<?> cl = obj.getClass(); // ButtonFrame
         // For each method in ButtonFrame that are annotated with @ActionListenerFor,
         // add this method as a listener to the corresponding source button
         for (Method m : cl.getDeclaredMethods())
         {
            // @ActionListenerFor has RetentionPolicy.RUNTIME,
            // therefore we could access this annotation through reflection
            ActionListenerFor a = m.getAnnotation(ActionListenerFor.class);
            if (a != null)
            {
               Field f = cl.getDeclaredField(a.source());
               f.setAccessible(true);
               // add the method as an action listener to the button
               // (specified by "source" in @ActionListenerFor)
               addListener(f.get(obj), obj, m);
            }
         }
      }
      catch (ReflectiveOperationException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Adds an action listener that calls a given method.
    * @param source the event source to which an action listener is added
    * @param param the implicit parameter of the method that the listener calls
    * @param m the method that the listener calls
    */
   public static void addListener(Object source, final Object param, final Method m)
         throws ReflectiveOperationException
   {
      var handler = new InvocationHandler()
         {
            public Object invoke(Object proxy, Method mm, Object[] args) throws Throwable
            {
               // We could use mm to differentiate the method being called and specify the behavior accordingly
               // In this example, the only method we are calling is actionPerformed in the ActionListener interface
               // For simplicity, we directly use the m & param argument without checking mm
               return m.invoke(param);
            }
         };

      // A proxy object which implements the ActionListener interface
      // Yet, the behavior of overridden methods in the interface cannot be statically handled,
      // but can be dynamically delegated to handler
      Object listener = Proxy.newProxyInstance(null,
         new Class[] { ActionListener.class }, handler);

      // (event) source is the button
      // this means "button.addActionListener(listener)"
      // the listener behavior (actionPerformed()) is delegated to proxy object's invoke()
      Method adder = source.getClass().getMethod("addActionListener", ActionListener.class);
      adder.invoke(source, listener);
   }
}
