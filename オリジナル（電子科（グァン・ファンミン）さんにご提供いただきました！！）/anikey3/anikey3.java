/* カーソル操作（アニメーションの画像切替） */

import java.applet.Applet;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.image.*;

public class anikey3 extends java.applet.Applet {

         java.awt.Image a_u1, a_u2, a_d1, a_d2, a_r1, a_r2, a_l1, a_l2;//キャラの変数
         boolean anime;
　　　　 int x = 130;
         int y = 130;         
         int keyDirection = 2;//整数型でkeyEvent情報を記録する変数


         public void init() {
                setBackground(Color.gray);    
                a_u1 = getImage(getDocumentBase(), "a_u1.gif");
                a_u2 = getImage(getDocumentBase(), "a_u2.gif");
                a_d1 = getImage(getDocumentBase(), "a_d1.gif");
                a_d2 = getImage(getDocumentBase(), "a_d2.gif");
                a_r1 = getImage(getDocumentBase(), "a_r1.gif");
                a_r2 = getImage(getDocumentBase(), "a_r2.gif");
                a_l1 = getImage(getDocumentBase(), "a_l1.gif");
                a_l2 = getImage(getDocumentBase(), "a_l2.gif");

         addKeyListener(new java.awt.event.KeyListener() {
         public void keyTyped(KeyEvent e){}

         //　カーソル「上：１、下：２、右：３、左：４」とする
         public void keyPressed(KeyEvent e){
                switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                y -= 5;//y方向に-5ずつ減らす            
                keyDirection = 1;// カーソル上キーが押下されたら1を代入
                break;
                case KeyEvent.VK_DOWN:
                y += 5;//ｙ方向に+5ずつ増やす
                keyDirection = 2;// カーソル下キーが押下されたら2を代入
                break;
                case KeyEvent.VK_RIGHT:
                x += 5;//ｘ方向に+5ずつ増やす            
                keyDirection = 3;// カーソル右キーがされたら3を代入
                break;
                case KeyEvent.VK_LEFT:
                x -= 5;//ｘ方向に-5ずつ減らす
                keyDirection = 4;// カーソル左キーが押下されたら4を代入
                break;
                
                }
                repaint();
                }

           public void keyReleased(KeyEvent e){}

           });

         }
         

            public void paint(java.awt.Graphics g){
            if ( anime == true ) anime = false; else  anime = true;
               if ( keyDirection == 1)//キーUP押下時、アニメを上向き画像に切り替える
                 if ( anime )
                      g.drawImage(a_u1, x, y, 25, 25, this);
                 else
                      g.drawImage(a_u2, x, y, 25, 25, this);
                   
               if ( keyDirection == 2)//キーDOWN押下時、アニメを下向き画像に切り替える
                 if ( anime )
                      g.drawImage(a_d1, x, y, 25, 25, this);
                 else
                      g.drawImage(a_d2, x, y, 25, 25, this);
               if ( keyDirection == 3)//キーRIGHT押下時、アニメを右向き画像に切り替える
                 if ( anime )
                      g.drawImage(a_r1, x, y, 25, 25, this);
                 else
                      g.drawImage(a_r2, x, y, 25, 25, this);
               if ( keyDirection == 4)//キーLEFT押下時、アニメを左向き画像に切り替える
                 if ( anime )
                      g.drawImage(a_l1, x, y, 25, 25, this);
                 else
                      g.drawImage(a_l2, x, y, 25, 25, this);

         }
    }

