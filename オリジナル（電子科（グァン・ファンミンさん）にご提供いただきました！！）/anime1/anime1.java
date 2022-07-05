/* 画像の表示  */

import java.awt.*;

public class anime1 extends java.applet.Applet {
    java.awt.Image image_;

    public void init() {
           setBackground(Color.gray);
           image_ = getImage(getDocumentBase(), "a0.gif");
          }

    public void paint(java.awt.Graphics g){
           g.drawImage(image_, 35, 35, 25, 25, this);
          }
}