package eng.metarJava.decoders.support;

/**
 * Internally used for decoding. Not public.
 * @author Marek Vajgl
 */
public class ReportLine {
  private final StringBuilder pre;
  private final StringBuilder post;
  
  public ReportLine(String line){
    this.pre = new StringBuilder(line);
    this.post = new StringBuilder();
  }

  @Override
  public String toString() {
    return "{line} [bef:]" + pre.toString() + " [aft:]" + post.toString();
  }
  
  public String getPre(){
    return pre.toString();
  }
  public String getPost(){
    return post.toString();
  }

  public void move(int count, boolean moveFollowingSpaces) {
    String s = pre.substring(0, count);
    pre.replace(0, count, "");
    post.append(s);
    while (pre.length() > 0 && pre.substring(0,1).equals(" ")){
      pre.replace(0, 1, "");
      post.append(" ");
    }
  }
  
}
