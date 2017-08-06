/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eng.metarJava.downloaders.support;

/**
 *
 * @author Marek Vajgl
 */
public interface AsyncResult<T> {
  
  public enum eState{
    Success,
    Exception
  }
  
  public void finished(eState state, T value, Throwable exception);
}
