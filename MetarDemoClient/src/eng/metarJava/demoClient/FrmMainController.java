/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava.demoClient;

import eng.metarJava.Report;
import eng.metarJava.downloaders.NoaaGovDownloader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    if (type == Integer.class)
      return true;
    if (type == Double.class)
      return true;
    if (type == Long.class)
      return true;
    if (type == Byte.class)
      return true;
    if (type == Short.class)
      return true;
    if (type == Float.class)
      return true;
    if (type == Character.class)
      return true;
    if (type == Boolean.class)
      return true;
    return false;
  }

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

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }

  @FXML
  public void btnDownload_onAction(ActionEvent event) {
    lblState.setText("... working");
    NoaaGovDownloader d = new NoaaGovDownloader();
    String icao = txtIcao.getText();
    String ret;
    try {
      ret = d.download(icao);
      txtMetar.setText(ret);
      lblState.setText("Downloaded...");
    } catch (IOException ex) {
      lblState.setText("<<error>>:" + ex.getMessage());
    }
  }

  @FXML
  protected void btnDecode_onAction(ActionEvent event) {
    String metar = txtMetar.getText();

    eng.metarJava.decoders.IParse parser
            = new eng.metarJava.decoders.GenericFormat();

    eng.metarJava.Report r;
    r = parser.parse(metar);

    TreeItem<String> root = buildObjectTree(r, "Report");
    tvw.setRoot(root);

    lblState.setText("Decoded");
  }

  private TreeItem<String> buildObjectTree(Object o, String label) {
    TreeItem<String> ret = new TreeItem<>(getTreeItemLabel(label, o.getClass()));
    
    Class cls = o.getClass();
    List<Field> privateFields = getPrivateFields(o);
    for (Field privateField : privateFields) {
      Object privateFieldValue = getValueOfField(o, privateField);
      if (privateFieldValue == null){
        String name = privateField.getName();
        Class type = privateField.getType();
        String text = getTreeItemLabel(name, type, "<null>");
        TreeItem<String> it = new TreeItem<>(text);
        ret.getChildren().add(it);
      } else if (isPrintableField(privateField)){
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
      } else if (privateFieldValue instanceof Iterable){
        TreeItem<String> par = new TreeItem(
                getTreeItemLabel(privateField.getName(), privateFieldValue.getClass()));
        Iterable iterable = (Iterable) privateFieldValue;
        for (Object object : iterable) {
          TreeItem<String> sub = buildObjectTree(object, privateField.getType().getName());
          par.getChildren().add(sub);
        }
        ret.getChildren().add(par);
      } 
    }
    return ret;
  }
  
  private static Object getValueOfField(Object o, Field f){
    Object ret;
    try {
      f.setAccessible(true);
      ret = f.get(o);
    } catch (IllegalAccessException| IllegalArgumentException ex) {
      ret = ex.getMessage();
    }
    return ret;
  }
  
  private static boolean isPrintableField(Field field){
    if (field.getType().isPrimitive())
      return true;
    if (isPrimitiveWrapper(field.getType()))
      return true;
    if (field.getType().isEnum()){
      return true;
    }
    if (field.getType().equals(String.class))
      return true;
    return false;
  }
  
  private static String getTreeItemLabel (String name, Class type, String value){
    String ret = String.format("%s [%s] = %s", name, type.getName(), value);
    return ret;
  }
  private static String getTreeItemLabel (String name, Class type){
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

}
