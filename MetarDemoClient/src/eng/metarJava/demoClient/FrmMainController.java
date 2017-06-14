/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava.demoClient;

import eng.metarJava.Report;
import eng.metarJava.downloaders.NoaaGovDownloader;
import eng.objectTreeBuilder.ItemInfo;
import eng.objectTreeBuilder.TreeNode;
import java.awt.datatransfer.StringSelection;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * FXML Controller class
 *
 * @author Marek Vajgl
 */
public class FrmMainController implements Initializable {

  private static boolean isPrimitiveWrapper(Class<?> type) {
    if (type == Integer.class) {
      return true;
    }
    if (type == Double.class) {
      return true;
    }
    if (type == Long.class) {
      return true;
    }
    if (type == Byte.class) {
      return true;
    }
    if (type == Short.class) {
      return true;
    }
    if (type == Float.class) {
      return true;
    }
    if (type == Character.class) {
      return true;
    }
    if (type == Boolean.class) {
      return true;
    }
    return false;
  }

  @FXML
  private TabPane tabPane;
  @FXML
  private Button btnDownload;
  @FXML
  private TextField txtIcao;
  @FXML
  private TextField txtMetar;
  @FXML
  private Button btnDecode;
  @FXML
  private Label lblState;
  @FXML
  private TreeView tvw;
  @FXML
  private TreeView tvwG;
  @FXML
  private TextArea txtError;
  @FXML
  private Tab tabError;
  @FXML
  private ComboBox cmbFormatters;
  @FXML
  private TextField txtEncoded;
  @FXML
  private TextField txtPropertyOutFileName;

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  @FXML
  public void btnDownload_onAction(ActionEvent event) {
   clearException();
    lblState.setText("... working");
    NoaaGovDownloader d = new NoaaGovDownloader();
    String icao = txtIcao.getText();
    String ret;
    try {
      ret = d.download(icao);
      txtMetar.setText(ret);
      lblState.setText("Downloaded...");
    } catch (IOException ex) {
      reportException(ex);
      lblState.setText("<<error>>:" + ex.getMessage());
    }
  }

  @FXML
  public void btnEncode_onAction() {
    clearException();
    if (lastReport == null) {
      txtEncoded.setText("Decode something first...");
    } else {
      eng.metarJava.decoders.Formatter fmt
              = new eng.metarJava.decoders.EUFormatter();

      String txt;

      try {
        txt = fmt.format(lastReport);
        txtEncoded.setText(txt);

        updateColors();

        lblState.setText("Encoded");
      } catch (Exception ex) {
        reportException(ex);
      }
    }
  }

  eng.metarJava.Report lastReport = null;

  @FXML
  protected void btnDecode_onAction(ActionEvent event) {
clearException();
    try {
      String metar = txtMetar.getText();

      eng.metarJava.decoders.Parser parser
              = new eng.metarJava.decoders.GenericParser();

      eng.metarJava.Report r;
      r = parser.parse(metar);

      TreeItem<String> root = buildObjectTree(r, "Report");
      tvw.setRoot(root);

//      root = buildGObjectTree(r, "Report");
//      tvwG.setRoot(root);
      fillPropertyTree(r);

      lblState.setText("Decoded");
      this.lastReport = r;
    } catch (Exception ex) {
      reportException(ex);
    }
  }

  private TreeItem<String> buildGObjectTree(Object o, String label) {

    TreeItem<String> ret = new TreeItem<>(getTreeItemLabel(label, o.getClass()));

    try {
      List<PropertyDescriptor> pds = getGetMethods(o);
      for (PropertyDescriptor pd : pds) {
        Method pMethod = pd.getReadMethod();
        String pName = pd.getDisplayName();
        Object pValue = getValueOfProperty(o, pd);
        Class pType = pMethod.getReturnType();

        if (pValue == null) {
          String text = getTreeItemLabel(pName, pType, "<null>");
          TreeItem<String> it = new TreeItem<>(text);
          ret.getChildren().add(it);
        } else if (isPrintableField(pd)) {
          String text = getTreeItemLabel(pName, pType, pValue);
          TreeItem<String> it = new TreeItem<>(text);
          ret.getChildren().add(it);
        } else if (pd.getReadMethod().getReturnType().getName().startsWith("eng.metarJava")) {
          TreeItem<String> local = buildGObjectTree(pValue, pd.getDisplayName());
          ret.getChildren().add(local);
        } else if (pValue instanceof Iterable) {
          TreeItem<String> par = new TreeItem("???");
          Iterable iterable = (Iterable) pValue;
          for (Object object : iterable) {
            TreeItem<String> sub = buildGObjectTree(object, pd.getReadMethod().getReturnType().getName());
            par.getChildren().add(sub);
          }
          par.setValue(getTreeItemLabel(
                  pd.getDisplayName() + " - " + par.getChildren().size()
                  + " items", pValue.getClass()));
          ret.getChildren().add(par);
        }
      }
    } catch (Throwable t) {
      throw new RuntimeException("Failed to extract value of object " + o.getClass().getName() + " with label " + label + ".", t);
    }
    return ret;
  }

  private List<PropertyDescriptor> getGetMethods(Object o) {
    PropertyDescriptor[] tmp;
    try {
      tmp = Introspector.getBeanInfo(o.getClass()).getPropertyDescriptors();
    } catch (IntrospectionException ex) {
      return new ArrayList();
    }
    List<PropertyDescriptor> ret = Arrays.asList(tmp);
    return ret;
  }

  private TreeItem<String> buildObjectTree(Object o, String label) {
    TreeItem<String> ret = new TreeItem<>(getTreeItemLabel(label, o.getClass()));

    Class cls = o.getClass();
    List<Field> privateFields = getPrivateFields(o);
    for (Field privateField : privateFields) {
      Object privateFieldValue = getValueOfField(o, privateField);
      if (privateFieldValue == null) {
        String name = privateField.getName();
        Class type = privateField.getType();
        String text = getTreeItemLabel(name, type, "<null>");
        TreeItem<String> it = new TreeItem<>(text);
        ret.getChildren().add(it);
      } else if (isPrintableField(privateField)) {
        String name = privateField.getName();
        Class type = privateField.getType();
        String value = getValueOfField(o, privateField).toString();
        String text = getTreeItemLabel(name, type, value);
        TreeItem<String> it = new TreeItem<>(text);
        ret.getChildren().add(it);
      } else if (privateField.getType().getName().startsWith("eng.metarJava")) {
        Object val = getValueOfField(o, privateField);
        TreeItem<String> local = buildObjectTree(val, privateField.getName());
        ret.getChildren().add(local);
      } else if (privateFieldValue instanceof Iterable) {
        TreeItem<String> par = new TreeItem("???");
        Iterable iterable = (Iterable) privateFieldValue;
        for (Object object : iterable) {
          TreeItem<String> sub = buildObjectTree(object, privateField.getType().getName());
          par.getChildren().add(sub);
        }
        par.setValue(getTreeItemLabel(
                privateField.getName() + " - " + par.getChildren().size()
                + " items", privateFieldValue.getClass()));
        ret.getChildren().add(par);
      }
    }
    return ret;
  }

  private static Object getValueOfField(Object o, Field f) {
    Object ret;
    try {
      f.setAccessible(true);
      ret = f.get(o);
    } catch (IllegalAccessException | IllegalArgumentException ex) {
      ret = ex.getMessage();
    }
    return ret;
  }

  private static boolean isPrintableField(Field field) {
    if (field.getType().isPrimitive()) {
      return true;
    }
    if (isPrimitiveWrapper(field.getType())) {
      return true;
    }
    if (field.getType().isEnum()) {
      return true;
    }
    if (field.getType().equals(String.class)) {
      return true;
    }
    return false;
  }

  private static boolean isPrintableField(PropertyDescriptor pd) {
    if (pd.getReadMethod().getReturnType().isPrimitive()) {
      return true;
    }
    if (isPrimitiveWrapper(pd.getReadMethod().getReturnType())) {
      return true;
    }
    if (pd.getReadMethod().getReturnType().isEnum()) {
      return true;
    }
    if (pd.getReadMethod().getReturnType().equals(String.class)) {
      return true;
    }
    return false;
  }

  private static String getTreeItemLabel(String name, Class type, Object value) {
    String ret = String.format("%s [%s] = %s", name, type.getName(), value);
    return ret;
  }

  private static String getTreeItemLabel(String name, Class type) {
    String ret = String.format("%s [%s]", name, type.getName());
    return ret;
  }

  private List<Field> getPrivateFields(Object o) {
    List<Field> privateFields = new ArrayList<>();
    Field[] allFields = o.getClass().getDeclaredFields();
    for (Field field : allFields) {
      if (Modifier.isPrivate(field.getModifiers())) {
        privateFields.add(field);
      }
    }
    return privateFields;
  }

  private Object getValueOfProperty(Object o, PropertyDescriptor pd) {
    Object ret;
    try {
      ret = pd.getReadMethod().invoke(o);
    } catch (NullPointerException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      ret = "<<error>> " + ex.getMessage();
    }
    return ret;
  }

  private void clearException(){
    txtError.setText("");
  }
  
  private void reportException(Throwable ex) {
    int indent = 0;
    StringBuilder sb = new StringBuilder();
    StackTraceElement[] stack = new StackTraceElement[0];

    while (ex != null) {
      for (int i = 0; i < indent; i++) {
        sb.append(" ");
      }
      sb.append("[").append(ex.getClass().getName()).append("] :: ");
      sb.append(ex.getMessage()).append("\r\n");
      indent++;
      stack = ex.getStackTrace();

      ex = ex.getCause();
    }

    sb.append("\r\n\r\n\r\n");
    for (StackTraceElement ste : stack) {
      sb
              .append(ste.getClassName())
              .append(".")
              .append(ste.getMethodName())
              .append(" ( at ")
              .append(ste.getFileName())
              .append("::")
              .append(ste.getLineNumber())
              .append(")")
              .append("\r\n");
    }

    txtError.setText(sb.toString());
    
    tabPane.getSelectionModel().select(2);
  }

  private void updateColors() {
    String a = txtMetar.getText();
    String b = txtEncoded.getText();

    if (a.equals(b)) {
      txtEncoded.setStyle("-fx-text-fill: green;");
    } else {
      txtEncoded.setStyle("-fx-text-fill: red;");
    }
  }
  private boolean isUserSpecificInitialzed = false;

  private void fillPropertyTree(Report r) {

    if (isUserSpecificInitialzed == false) {
      isUserSpecificInitialzed = true;
      eng.objectTreeBuilder.TreePropertyFactory.registerSpecificTypeDecoders(UserSpecificFormatters.class);
    }

    TreeNode<ItemInfo> root = eng.objectTreeBuilder.TreePropertyFactory.build(r);
    TreeItem<ItemInfo> fxRoot = convertTreeNodeToTreeItem(root);
    tvwG.setRoot(fxRoot);
  }

  private TreeItem<ItemInfo> convertTreeNodeToTreeItem(TreeNode<ItemInfo> node) {
    TreeItem<ItemInfo> ret = new TreeItem<>(node.getValue());

    for (TreeNode<ItemInfo> child : node.getChildren()) {
      TreeItem<ItemInfo> fxChild = convertTreeNodeToTreeItem(child);
      ret.getChildren().add(fxChild);
    }
    return ret;
  }

  @FXML
  private void btnSavePropertiesToFile_onAction() {
    clearException();
    TreeItem<ItemInfo> root = tvwG.getRoot();
    StringBuilder sb = new StringBuilder();
    appendItemToStringBuilder(root, sb, 0);
    
    String fileName = txtPropertyOutFileName.getText();

    try {
      java.nio.file.Files.write(
              Paths.get(fileName),
              sb.toString().getBytes());
      lblState.setText("... saved as text");
    } catch (IOException ex) {
      reportException(ex);
    }
  }

  private void appendItemToStringBuilder(TreeItem<ItemInfo> root, StringBuilder sb, int i) {
    for (int j = 0; j < i; j++) {
      sb.append("  ");
    }
    sb.append(root.getValue().toString());
    sb.append("\r\n");
    for (TreeItem<ItemInfo> child : root.getChildren()) {
      appendItemToStringBuilder(child, sb, i + 1);
    }
  }
}
