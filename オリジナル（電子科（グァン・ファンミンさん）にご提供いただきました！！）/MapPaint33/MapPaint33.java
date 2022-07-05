/* 背景の描画（背景を自由に描画） */

import java.applet.Applet;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import java.awt.*;

// ===============================================================

public class MapPaint33  extends java.applet.Applet {

    // 背景イメージ
    Image haikei[];
    int x;
    int y;
    java.awt.Image a_u1, a_u2, a_d1, a_d2, a_r1, a_r2, a_l1, a_l2;//キャラの変数
    boolean anime;
    int keyDirection = 2;//整数型でkeyEvent情報を記録する変数
    
    // -----------------------------------------------------------
    public void init() {  // 初期処理

    // 背景配列の初期化
    haikei = new Image[4];

    haikei[0] = getImage(getDocumentBase(), "map1.gif");
    haikei[1] = getImage(getDocumentBase(), "map2.gif");
    haikei[2] = getImage(getDocumentBase(), "map3.gif");
    haikei[3] = getImage(getDocumentBase(), "map4.gif");
    a_u1 = getImage(getDocumentBase(), "a_u1.gif");
    a_u2 = getImage(getDocumentBase(), "a_u2.gif");
    a_d1 = getImage(getDocumentBase(), "a_d1.gif");
    a_d2 = getImage(getDocumentBase(), "a_d2.gif");
    a_r1 = getImage(getDocumentBase(), "a_r1.gif");
    a_r2 = getImage(getDocumentBase(), "a_r2.gif");
    a_l1 = getImage(getDocumentBase(), "a_l1.gif");
    a_l2 = getImage(getDocumentBase(), "a_l2.gif");
    
    //キャラ座標の初期値
    x=64; y=64;
    
    addKeyListener(new java.awt.event.KeyListener() {
    public void keyTyped(KeyEvent e){}

    //　カーソル「上：１、下：２、右：３、左：４」とする
    public void keyPressed(KeyEvent e){
           switch(e.getKeyCode()) {
           case KeyEvent.VK_UP:
           y -= 4;//y方向に-4ずつ減らす
           keyDirection = 1;// カーソル上キーが押下されたら1を代入
           break;
           case KeyEvent.VK_DOWN:
           y += 4;//ｙ方向に+4ずつ増やす
           keyDirection = 2;// カーソル下キーが押下されたら2を代入
           break;
           case KeyEvent.VK_RIGHT:
           x += 4;//ｘ方向に+4ずつ増やす
           keyDirection = 3;// カーソル右キーがされたら3を代入
           break;
           case KeyEvent.VK_LEFT:
           x -= 4;//ｘ方向に-4ずつ減らす
           keyDirection = 4;// カーソル左キーが押下されたら4を代入
           break;
           }
                           
           //当たり判定
           if(x<16) x=x+4;
           if(x>112) x=x-4;
           if(y<16) y=y+4;
           if(y>112) y=y-4;

           repaint();
           }

        public void keyReleased(KeyEvent e){}
        });

     } // init()

     // -----------------------------------------------------------
     public void paint(Graphics g) {
     // マップ配列の初期化
     //９×９の背景描画
     int map[][] ={{2,2,2,2,2,2,2,2,2},
                   {2,1,0,1,0,1,0,1,2},
                   {2,1,3,1,0,1,0,1,2},
                   {2,1,0,1,0,1,0,1,2},
                   {2,1,0,1,0,1,0,1,2},
                   {2,1,0,1,3,1,0,1,2},
                   {2,1,0,1,0,1,0,1,2},
                   {2,3,0,1,0,1,0,1,2},
                   {2,2,2,2,2,2,2,2,2}};
                      
     for(int i=0; i<9; i++){
         for(int j=0; j<9; j++){
             int a = map[j][i];
          
         if(a == 0){
                     g.drawImage(haikei[0], i*16, j*16, this);
                    }
         else if(a == 1){
                     g.drawImage(haikei[1], i*16, j*16, this);
                    }
         else if(a == 2){
                     g.drawImage(haikei[2], i*16, j*16, this);
                    }
         else if(a == 3){
                     g.drawImage(haikei[3], i*16, j*16, this);
                    }
                }
            }


       //画像の方向を変える
       if ( anime == true ) anime = false; else  anime = true;
          if ( keyDirection == 1)//キーUP押下時、アニメを上向き画像に切り替える
             if ( anime )
                  g.drawImage(a_u1, x, y, this);
             else
                  g.drawImage(a_u2, x, y, this);
                   
          if ( keyDirection == 2)//キーDOWN押下時、アニメを下向き画像に切り替える
             if ( anime )
                  g.drawImage(a_d1, x, y, this);
             else
                  g.drawImage(a_d2, x, y, this);
          if ( keyDirection == 3)//キーRIGHT押下時、アニメを右向き画像に切り替える
             if ( anime )
                  g.drawImage(a_r1, x, y, this);
             else
                  g.drawImage(a_r2, x, y, this);
          if ( keyDirection == 4)//キーLEFT押下時、アニメを左向き画像に切り替える
             if ( anime )
                  g.drawImage(a_l1, x, y, this);
             else
                  g.drawImage(a_l2, x, y, this);
       
          }

    // -----------------------------------------------------------
    public void update(Graphics g) {  // 再作画処理
    paint( g );
    } // update()

 }//class()
