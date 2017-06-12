/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.objectTreeBuilder;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Marek Vajgl
 */
public class TreeFactory {

  private static final int MAX_DEPTH = 7;

  private static final List<Class> SPECIFIC_TYPES = new ArrayList<Class>();
  private static final List<Class> WRAPPER_TYPES = new ArrayList<Class>();
  private static final List<String> IGNORED_GETTERS = new ArrayList<String>();

  static {
    SPECIFIC_TYPES.add(String.class);
    SPECIFIC_TYPES.add(java.util.Date.class);
    SPECIFIC_TYPES.add(java.sql.Date.class);
    SPECIFIC_TYPES.add(java.util.Calendar.class);

    WRAPPER_TYPES.add(Byte.class);
    WRAPPER_TYPES.add(Short.class);
    WRAPPER_TYPES.add(Integer.class);
    WRAPPER_TYPES.add(Long.class);
    WRAPPER_TYPES.add(Float.class);
    WRAPPER_TYPES.add(Double.class);
    WRAPPER_TYPES.add(Character.class);
    WRAPPER_TYPES.add(Boolean.class);

    IGNORED_GETTERS.add("getClass");
  }

  public static TreeNode<ItemInfo> build(Object rootObject) {
    TreeNode<ItemInfo> root = buildObjectNode("root", rootObject, null, 0);

    return root;
  }

  private static TreeNode<ItemInfo> buildObjectNode(String label, Object obj, Class suggestedTypeOrNull, int depthLevel) {
    if (depthLevel >= MAX_DEPTH) {
      return new TreeNode<>(new ItemInfo("Max depth reached", "N/A", "N/A"));
    }
    TreeNode<ItemInfo> ret;
    Type t = getType(obj);
    List<TreeNode<ItemInfo>> children;
    ItemInfo ii;
    switch (t) {
      case nil:
        ret = getNullNode(label, obj, suggestedTypeOrNull);
        break;
      case primitiveOrWrapped:
        ret = getSimpleNode(label, obj);
        break;
      case enumeration:
        ret = getEnumNode(label, obj);
        break;
      case complex:
        ii = new ItemInfo(label, "", extractClassDisplayName(obj.getClass()));
        ret = new TreeNode<>(ii);
        children = decodeComplexNode(obj, depthLevel + 1);
        for (TreeNode<ItemInfo> child : children) {
          TreeNode.join(ret, child);
        }
        break;
      case iterable:
        ii = new ItemInfo(label, "", extractClassDisplayName(obj.getClass()));
        ret = new TreeNode<>(ii);
        children = decodeIterableNode(obj, depthLevel + 1);
        for (TreeNode<ItemInfo> child : children) {
          TreeNode.join(ret, child);
        }
        break;
      case array:
        ii = new ItemInfo(label, "", extractClassDisplayName(obj.getClass()));
        ret = new TreeNode<>(ii);
        children = decodeArrayNode(obj, depthLevel + 1);
        for (TreeNode<ItemInfo> child : children) {
          TreeNode.join(ret, child);
        }
        break;
      case specific:
        ret = getSpecificNode(label, obj);
        break;
      case error:
        ret = getErrorNode(label, obj);
        break;
      default:
        throw new UnsupportedOperationException("Unknown type [" + t + "].");
    }

    return ret;
  }

  private static List<Getter> getGetters(Object obj) {
    List<Getter> ret = new ArrayList();
    Class cls = obj.getClass();
    Method[] methods = cls.getMethods();
    for (Method method : methods) {
      if (method.getName().startsWith("get") == false) {
        continue; // only getters
      }
      if (method.getParameterCount() != 0) {
        continue; // without parameters
      }
      if (java.lang.reflect.Modifier.isPublic(method.getModifiers()) == false) {
        continue; // and public only
      }
      if (IGNORED_GETTERS.contains(method.getName())) {
        continue; // skip ignored methods
      }
      String name = decodePropertyName(method.getName());
      Getter getter = new Getter(name, method);
      ret.add(getter);
    }
    Collections.sort(ret);
    return ret;
  }

  private static String decodePropertyName(String name) {
    if (name.length() == 4) {
      return Character.toString(Character.toUpperCase(name.charAt(3)));
    } else {
      return Character.toString(Character.toUpperCase(name.charAt(3))) + name.substring(4);
    }
  }

  private static Type getType(Object val) {
    if (val == null) {
      return Type.nil;
    } else if (val instanceof Iterable) {
      return Type.iterable;
    } else if (val instanceof AnalysisException) {
      return Type.error;
    } else {
      Class cls = val.getClass();
      if (cls.isArray()) {
        return Type.array;
      } else if (cls.isEnum()) {
        return Type.enumeration;
      } else if (cls.isPrimitive()) {
        return Type.primitiveOrWrapped;
      } else if (WRAPPER_TYPES.contains(cls)) {
        return Type.primitiveOrWrapped;
      } else if (SPECIFIC_TYPES.contains(cls)) {
        return Type.specific;
      } else {
        return Type.complex;
      }
    }
  }

  private static TreeNode<ItemInfo> getNullNode(String label, Object val, Class suggestedType) {
    String st = "N/A";
    if (suggestedType != null) {
      st = "? " + extractClassDisplayName(suggestedType);
    }
    TreeNode<ItemInfo> ret
            = new TreeNode<>(new ItemInfo(label, "null", st));
    return ret;
  }

  private static TreeNode<ItemInfo> getSimpleNode(String label, Object val) {
    ItemInfo ii = new ItemInfo(label, val.toString(), extractClassDisplayName(val.getClass()));
    TreeNode<ItemInfo> ret
            = new TreeNode<>(ii);
    return ret;
  }

  private static TreeNode<ItemInfo> getEnumNode(String label, Object val) {
    ItemInfo ii = new ItemInfo(label, val.toString(), extractClassDisplayName(val.getClass()));
    TreeNode<ItemInfo> ret
            = new TreeNode<>(ii);
    return ret;
  }

  private static String extractClassDisplayName(Class cls) {
    return cls.getName();
  }

  private static List<TreeNode<ItemInfo>> decodeComplexNode(Object obj, int depthLevel) {
    List<TreeNode<ItemInfo>> ret = new ArrayList();
    List<Getter> getters = getGetters(obj);

    for (Getter g : getters) {
      Object val = g.getValue(obj);
      TreeNode<ItemInfo> child = buildObjectNode(g.getDisplayName(), val, g.method.getReturnType(), depthLevel);
      ret.add(child);
    }

    return ret;
  }

  private static List<TreeNode<ItemInfo>> decodeIterableNode(Object obj, int depthLevel) {
    List<TreeNode<ItemInfo>> ret = new ArrayList<>();
    Iterable iterable = (Iterable) obj;
    int index = 0;

    for (Object item : iterable) {
      TreeNode<ItemInfo> child = buildObjectNode("[" + index + ".]", item, null, depthLevel);
      ret.add(child);
      index++;
    }
    return ret;
  }

  private static List<TreeNode<ItemInfo>> decodeArrayNode(Object obj, int depthLevel) {
    List<TreeNode<ItemInfo>> ret = new ArrayList<>();

    int length = Array.getLength(obj);
    for (int index = 0; index < length; index++) {
      Object item = Array.get(obj, index);
      TreeNode<ItemInfo> child = buildObjectNode("[" + index + ".]", item, null, depthLevel);
      ret.add(child);
    }

    return ret;
  }

  private static TreeNode<ItemInfo> getSpecificNode(String label, Object obj) {
    TreeNode<ItemInfo> ret;
    Method method = null;
    try {
      Method[] ms = TreeFactory.class.getDeclaredMethods();
      for (Method m : ms) {
        if (m.getName().equals("getSpecificToString") && m.getParameterCount() == 1 && m.getParameterTypes()[0] == obj.getClass()) {
          method = m;
          break;
        }
      }
      if (method == null) {
        throw new NoSuchMethodException("No method [decodeSpecific] for parameter " + obj.getClass().getName() + ".");
      }

      method.setAccessible(true);
      Object val = method.invoke(null, obj);
      String str;
      if (val == null) {
        str = "null";
      } else {
        str = val.toString();
      }

      ret = new TreeNode<ItemInfo>(new ItemInfo(
              label, str, extractClassDisplayName(obj.getClass())));

    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      ret = new TreeNode<ItemInfo>(new ItemInfo(label, "Error: " + ex.getMessage(), extractClassDisplayName(obj.getClass())));
    }

    return ret;
  }

  @SuppressWarnings("unused")
  private static String getSpecificToString(java.util.Date date) {
    return date.toString();
  }

  private static String getSpecificToString(String string) {
    return string;
  }

  private static TreeNode<ItemInfo> getErrorNode(String label, Object obj) {
    AnalysisException ae = (AnalysisException) obj;
    
    StringBuilder sb = new StringBuilder();
    Throwable t = ae;
    while (t != null){
      sb.append("[").append(ae.getClass().getName()).append("]:").append(t.getMessage()).append(" -> ");
      t = t.getCause();
    }
    
    TreeNode<ItemInfo> ret = 
            new TreeNode<>(
            new ItemInfo("Error", sb.toString(), extractClassDisplayName(obj.getClass())));
    return ret;
  }

}

class Depth {

  public final int max;
  public int current;

  public Depth(int max) {
    this.max = max;
    this.current = 0;
  }
}

class Getter implements Comparable<Getter> {

  public final String propertyName;
  public final Method method;
  private final DisplayLabel displayLabel;

  public Getter(String propertyName, Method method) {
    this.propertyName = propertyName;
    this.method = method;

    if (method.isAnnotationPresent(DisplayLabel.class)) {
      this.displayLabel = method.getAnnotation(DisplayLabel.class);
    } else {
      this.displayLabel = null;
    }
  }

  public Object getValue(Object source) {
    Object ret;
    try {
      ret = method.invoke(source);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      ret = new AnalysisException(propertyName, ex);
    }
    return ret;
  }

  public String getDisplayName() {
    String ret;
    if (this.displayLabel != null) {
      ret = this.displayLabel.label();
    } else {
      ret = this.propertyName;
    }
    return ret;
  }

  @Override
  public int compareTo(Getter o) {
    int a;
    int b;
    if (this.displayLabel != null) {
      a = this.displayLabel.orderIndex();
    } else {
      a = 0;
    }
    if (o.displayLabel != null) {
      b = o.displayLabel.orderIndex();
    } else {
      b = 0;
    }
    int ret = Integer.compare(a, b);
    if (ret == 0) {
      ret = this.getDisplayName().compareTo(o.getDisplayName());
    }
    return ret;
  }

}

enum Type {
  nil,
  primitiveOrWrapped,
  specific,
  enumeration,
  iterable,
  array,
  complex,
  error
}
