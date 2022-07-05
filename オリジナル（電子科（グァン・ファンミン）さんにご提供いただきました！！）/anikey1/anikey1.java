/* キー入力（静止画の移動）  */

import java.awt.*;
import java.awt.event.KeyEvent;

public class anikey1 extends java.applet.Applet {
    Image image_;
    int x_ = 130;
    int y_ = 130;

    public void init() {
           setBackground(Color.gray);
           image_ = getImage(getDocumentBase(), "a0.gif");
                  
           addKeyListener(new java.awt.event.KeyListener() {
           public void keyTyped(KeyEvent e){}

           public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    y_ -= 5;
                    break;
                case KeyEvent.VK_DOWN:
                    y_ += 5;
                    break;
                case KeyEvent.VK_RIGHT:
                    x_ += 5;
                    break;
                case KeyEvent.VK_LEFT:
                    x_ -= 5;
               }
                 repaint();
           }

          public void keyReleased(KeyEvent e){}
          });
      }

        public void paint(Graphics g) {
                 g.drawImage(image_, x_, y_, 25, 25, this);
             }

    }
