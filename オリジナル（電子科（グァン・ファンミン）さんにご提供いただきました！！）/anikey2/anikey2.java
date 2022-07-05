/* カーソル操作（アニメーションあり） */

import java.awt.*;
import java.awt.event.KeyEvent;

public class anikey2 extends java.applet.Applet implements Runnable {
    
    Thread th_ = null;
    Image img_[] = new Image[2];
    int iPat_ = 0; // パターン(Pat)のインデックス(i)

    int x_ = 130;
    int y_ = 130;

    public void init() {
                setBackground(Color.gray);    
                img_[0] = getImage(getDocumentBase(), "a1.gif");
                img_[1] = getImage(getDocumentBase(), "a2.gif");

                      
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


    // アプレット開始
    public void start() {
        if(th_ == null) {
            // Runnableを実装したクラス(this)を元にスレッドを作る
            th_ = new Thread(this);
            th_.start();
         }
      }

    // スレッド処理
    public void run() {
         while (th_ == Thread.currentThread()) {
            iPat_ = 1 - iPat_;

            repaint();

            // 500ミリ秒だけ待つ
            try{
                 Thread.sleep(500);
            } catch(InterruptedException e) {}
         }
     }

    // スレッド終了
    public void stop() {
       th_ = null;
     }


    // 実際の描画
    public void paint(java.awt.Graphics g) {
       g.drawImage(img_[iPat_], x_, y_, 25, 25, this);
     }

}