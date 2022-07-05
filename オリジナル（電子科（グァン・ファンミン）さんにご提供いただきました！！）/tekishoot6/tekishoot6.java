/*　パワーアップ　*/

import java.applet.Applet;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import java.awt.*;

// ===============================================================

public class tekishoot6 extends Applet implements Runnable {

    // 背景イメージ
    Image haikei[];
    int x, y;
    int a, i, j;
    int tx, ty;
    java.awt.Image a_u1, a_u2, a_d1, a_d2, a_r1, a_r2, a_l1, a_l2;//キャラの変数
    Image nf, nf_u, nf_d, nf_r, nf_l;//ホワイトソード画像
    Image bnf, bnf_u, bnf_d, bnf_r, bnf_l;//ブラックソード画像
    Image a_r, a_l, a_nu, a_nd, a_nr, a_nl;
    Image teki_1;

    //ダブルバッファリングのために用意するもの
    Graphics ct;//背景やキャラクターを描画するキャンパス
    Image buf;//キャンパスに描かれたものを一気に描画するための箱
    Dimension dim;//アプレットの大きさ取得用
    
　  boolean anime, teki1, teki2;//teki1:敵、teki2:壁
    boolean keyUp, keyDown, keyLeft, keyRight, keySpace;// キー押下フラグ
    boolean ken1flag, ken2flag;
         
         
    Thread gameThread;
    int keyDirection = 2;//整数型でkeyEvent情報を記録する変数

    int map[][] ={{3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
                  {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
                  {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
                  {3,3,3,2,2,2,2,2,2, 2,2,2,2,2,2,2,2,2, 2,2,2,2,2,2,2,2,2, 2,2,2,2,2,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,1,0,0,0,0,0,0, 0,0,0,0,0,1,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,1,0,0, 0,0,0,0,0,0,0,0,1, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,1, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                       
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,1, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,2,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 3,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 3,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,1,0,0,1, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,1,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,1,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 3,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,3,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3}, 
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1,0,0,0,1,0,0,0,3, 0,0,0,2,0,2,3,3,3},
                       
                  {3,3,3,2,0,0,1,0,0, 0,0,0,0,1,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,2,2,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,2,3,3,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,1, 0,0,0,1,0,0,0,0,0, 1,2,3,3,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,1,0,0,0,0,0,0, 0,0,7,0,0,0,0,0,2, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,1,0,0,1,0,0,0,0, 0,0,1,0,4,0,0,3,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,3,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,1,0, 0,0,0,0,0,0,0,0,0, 1,0,0,0,0,2,3,3,3}, 
                  {3,3,3,2,0,0,0,0,0, 3,0,1,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,3,0,0,2,3,3,3},
    
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,2,2,2, 0,0,0,0,0,0,0,0,3, 0,0,1,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,2,3,2, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,2,0,0, 0,0,1,0,0,2,3,3,3},
                  {3,3,3,2,0,0,2,2,2, 0,1,0,0,1,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,2,2,2,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,5,6,3,2,3,3,3},
                  {3,3,3,2,2,2,2,2,2, 2,2,2,2,2,2,2,2,2, 2,2,2,2,2,2,2,2,2, 2,2,2,2,2,2,3,3,3},
                  {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
                  {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3}, 
                  {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3}}; 
         
    // -----------------------------------------------------------
    public void init() {  // 初期処理

    // 背景配列の初期化
    haikei = new Image[5];

    haikei[0] = getImage(getDocumentBase(), "map1.gif");
    haikei[1] = getImage(getDocumentBase(), "map2.gif");
    haikei[2] = getImage(getDocumentBase(), "kabe1.gif");
    haikei[3] = getImage(getDocumentBase(), "map4.gif");
    haikei[4] = getImage(getDocumentBase(), "map11.gif");
                  
    //キャラクタ画像
    a_u1 = getImage(getDocumentBase(), "a_u1.gif");
    a_u2 = getImage(getDocumentBase(), "a_u2.gif");
    a_d1 = getImage(getDocumentBase(), "a_d1.gif");
    a_d2 = getImage(getDocumentBase(), "a_d2.gif");
    a_r1 = getImage(getDocumentBase(), "a_r1.gif");
    a_r2 = getImage(getDocumentBase(), "a_r2.gif");
    a_l1 = getImage(getDocumentBase(), "a_l1.gif");
    a_l2 = getImage(getDocumentBase(), "a_l2.gif");
                  
    //ソードを出すときのキャラクタ画像
    a_nu = getImage(getDocumentBase(), "a_nu.gif");
    a_nd = getImage(getDocumentBase(), "a_nd.gif");
    a_nr = getImage(getDocumentBase(), "a_nr.gif");
    a_nl = getImage(getDocumentBase(), "a_nl.gif");

    //ホワイトソード画像
    nf   = getImage(getDocumentBase(), "nf.gif");
    nf_u = getImage(getDocumentBase(), "nfu.gif");
    nf_d = getImage(getDocumentBase(), "nfd.gif");
    nf_r = getImage(getDocumentBase(), "nfr.gif");
    nf_l = getImage(getDocumentBase(), "nfl.gif");

    //ブラックソード画像
    bnf   = getImage(getDocumentBase(), "bnf.gif");
    bnf_u = getImage(getDocumentBase(), "bnfu.gif");
    bnf_d = getImage(getDocumentBase(), "bnfd.gif");
    bnf_r = getImage(getDocumentBase(), "bnfr.gif");
    bnf_l = getImage(getDocumentBase(), "bnfl.gif");

    //敵画像
    teki_1 = getImage(getDocumentBase(), "teki1.gif");
                                  
    //キャラ座標の初期値
    x=18; y=18;//マップ３６×３６の中心
    tx=64; ty=64;
                  
    teki1=teki2=true;
    
    // キーフラグをクリア
    keyUp = keyDown = keyLeft = keyRight = keySpace = false;
                  
    //剣フラグ
    ken1flag=false;//ホワイトソードをオフ
    ken2flag=false;//ブラックソードをオフ
                  

    /////////////////////////////////////////////////////////
    // キー入力の受け付け開始
    addKeyListener(new java.awt.event.KeyListener() {
    public void keyTyped(KeyEvent e){}

    //　カーソル「上：１、下：２、右：３、左：４」とする
      public void keyPressed(KeyEvent e){
                             switch(e.getKeyCode()) {
                             case KeyEvent.VK_UP:
                             keyUp=true;
                             keyDirection = 1;
                             break;
                             case KeyEvent.VK_DOWN:
                             keyDown=true;
                             keyDirection = 2;
                             break;
                             case KeyEvent.VK_RIGHT:
                             keyRight=true;
                             keyDirection = 3;
                             break;
                             case KeyEvent.VK_LEFT:
                             keyLeft=true;
                             keyDirection = 4;
                             break;
                             case KeyEvent.VK_SPACE:
                             keySpace = true;
                             break; 
                           }
                          repaint();
                   }

    // キーが離されたときの処理
     public void keyReleased(KeyEvent e) {
                            switch (e.getKeyCode()) {
                            case KeyEvent.VK_UP:
                            keyUp=false;
                            break;
                            case KeyEvent.VK_DOWN:
                            keyDown=false;
                            break;
                            case KeyEvent.VK_RIGHT: 
                            keyRight=false;
                            break;
                            case KeyEvent.VK_LEFT: 
                            keyLeft=false;
                            break;
                            case KeyEvent.VK_SPACE:
                            keySpace = false;
                            break;
                          }
                     }
                });


    } // init()

    //////////////////////////////////////////////////////////////////////
    // ゲームスレッドの開始
        public void start() {
          if(gameThread == null) {
          gameThread = new Thread(this);
          gameThread.start();
                           
          dim = getSize();
          buf = createImage(dim.width , dim.height);
       }
    }

    // ゲームスレッドの停止
    public void stop() {
      gameThread = null;
    }

    // ゲームスレッドのメイン
    public void run() {
        while (gameThread == Thread.currentThread()) {
             try {
                                
                                if(keyUp) y -=1;
                                if(keyDown) y +=1;
                                if(keyRight) x +=1;
                                if(keyLeft) x -=1;

                                          if(keySpace)
                                             if(!anime)
                                                   anime=true;
                                             
                                             if(anime) anime=false;
                           
                           
                           //////////////////////////////////////////////////////////////////
                           //ブロックと海の当り判定
                           if(keyDirection==1 && map[y][x]==3 || keyDirection==1 && map[y][x]==2) y=y+1;
                           if(keyDirection==2 && map[y][x]==3 || keyDirection==2 && map[y][x]==2) y=y-1;
                           if(keyDirection==3 && map[y][x]==3 || keyDirection==3 && map[y][x]==2) x=x-1;
                           if(keyDirection==4 && map[y][x]==3 || keyDirection==4 && map[y][x]==2) x=x+1;
                           //初期状態では敵(teki1)通過不可
                           if(keyDirection==1 && map[y][x]==4 && teki1==true) y=y+1;
                           if(keyDirection==2 && map[y][x]==4 && teki1==true) y=y-1;
                           if(keyDirection==3 && map[y][x]==4 && teki1==true) x=x-1;
                           if(keyDirection==4 && map[y][x]==4 && teki1==true) x=x+1;
                           //初期状態では壁(teki2)通過不可
                           if(keyDirection==1 && map[y][x]==5 && teki2==true) y=y+1;
                           if(keyDirection==2 && map[y][x]==5 && teki2==true) y=y-1;
                           if(keyDirection==3 && map[y][x]==5 && teki2==true) x=x-1;
                           if(keyDirection==4 && map[y][x]==5 && teki2==true) x=x+1;

                           /////////////////////////////////////////////////////////////////// 
                           //敵との当り判定
                           //敵の周りに当り判定を設ける
                           //teki1(ブラックソードを取得して、敵を倒す)
                           if(keyDirection==1 && map[y-1][x]==4 && keySpace==true && ken2flag==true) teki1=false;
                           if(keyDirection==2 && map[y+1][x]==4 && keySpace==true && ken2flag==true) teki1=false;
                           if(keyDirection==3 && map[y][x+1]==4 && keySpace==true && ken2flag==true) teki1=false;
                           if(keyDirection==4 && map[y][x-1]==4 && keySpace==true && ken2flag==true) teki1=false;
                           //壊れる壁(ホワイトソードで壁を壊す)
                           if(keyDirection==1 && map[y-1][x]==5 && keySpace==true && ken1flag==true) teki2=false;
                           if(keyDirection==2 && map[y+1][x]==5 && keySpace==true && ken1flag==true) teki2=false;
                           if(keyDirection==3 && map[y][x+1]==5 && keySpace==true && ken1flag==true) teki2=false;
                           if(keyDirection==4 && map[y][x-1]==5 && keySpace==true && ken1flag==true) teki2=false;


                           //ホワイトソードを手に入れたら、剣を切り替える
                           //map[y][x]==7は剣２の左向き（ken1Left）
                           if(keyDirection==1 && map[y][x]==7) ken1flag=true;
                           if(keyDirection==2 && map[y][x]==7) ken1flag=true;
                           if(keyDirection==3 && map[y][x]==7) ken1flag=true;
                           if(keyDirection==4 && map[y][x]==7) ken1flag=true;
                           //ブラックソードを手に入れたら、剣を切り替える
                           //map[y][x]==6は剣２の左向き（ken2Left）
                           if(keyDirection==1 && map[y][x]==6) { ken1flag=false; ken2flag=true;}
                           if(keyDirection==2 && map[y][x]==6) { ken1flag=false; ken2flag=true;}
                           if(keyDirection==3 && map[y][x]==6) { ken1flag=false; ken2flag=true;}
                           if(keyDirection==4 && map[y][x]==6) { ken1flag=false; ken2flag=true;}


                                repaint();

                           Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }

         }
     }

    // -----------------------------------------------------------
    public void update(Graphics g) {  // 再作画処理
      paint( g );
    } // update()

    public void paint(Graphics g) {
    
         if (ct == null) ct = buf.getGraphics();
         
        // マップ配列の初期化
        //９×９の背景描画
            for(i=0; i<9; i++){
             for(j=0; j<9; j++){                      
                     a = map[y-4+j][x-4+i];//どこを中心に描画するか
                 if(a == 0){
                     ct.drawImage(haikei[0], i*16, j*16, this);
                     }
                 else if(a == 1){
                     ct.drawImage(haikei[1], i*16, j*16, this);
                     }
                 else if(a == 2){
                     ct.drawImage(haikei[2], i*16, j*16, this);
                     }
                 else if(a == 3){
                     ct.drawImage(haikei[3], i*16, j*16, this);
                     }

                 //敵
                 else if(a == 4){
                        if(teki1)
                              ct.drawImage(teki_1, i*16, j*16, this);
                        else
                              ct.drawImage(haikei[0], i*16, j*16, this);
                       }
                 //壁
                 else if(a == 5){
                        if(teki2)
                              ct.drawImage(haikei[4], i*16, j*16, this);
                        else
                              ct.drawImage(haikei[0], i*16, j*16, this);
                       }
                 //ブラックソード      
                 else if(a == 6){
                        if(ken2flag==false)
                              ct.drawImage(bnf, i*16, j*16, this);
                        else
                              ct.drawImage(haikei[0], i*16, j*16, this);
                       }  
                 //ホワイトソード
                 else if(a == 7){
                        if(ken1flag==false && ken2flag==false)
                              ct.drawImage(nf, i*16, j*16, this);
                        else
                              ct.drawImage(haikei[0], i*16, j*16, this);
                       }    
         
              }
          }
              


                  ////////////////////////////////////////////////////
                  //剣を出す処理
                  
                  //ホワイトソード(ken1flag)
                  //上向きの剣
                  if(keyDirection==1)
                        if (keySpace==true && ken1flag==true) 
                        ct.drawImage(nf_u, tx, ty-16, this);
                  //下向きの剣
                  if (keyDirection==2)
                        if (keySpace==true && ken1flag==true)
                        ct.drawImage(nf_d, tx, ty+16, this);
                  //右向きの剣
                  if (keyDirection==3)
                        if (keySpace==true && ken1flag==true)
                        ct.drawImage(nf_r, tx+16, ty, this);
                  //左向きの剣
                  if (keyDirection==4)
                        if (keySpace==true && ken1flag==true) 
                        ct.drawImage(nf_l, tx-16, ty, this);
                        
                  //ブラックソード(ken2flag)
                  //上向きの剣
                  if(keyDirection==1)
                        if (keySpace==true && ken2flag==true) 
                        ct.drawImage(bnf_u, tx, ty-16, this);
                  //下向きの剣
                  if (keyDirection==2)
                        if (keySpace==true && ken2flag==true)
                        ct.drawImage(bnf_d, tx, ty+16, this);
                  //右向きの剣
                  if (keyDirection==3)
                        if (keySpace==true && ken2flag==true)
                        ct.drawImage(bnf_r, tx+16, ty, this);
                  //左向きの剣
                  if (keyDirection==4)
                        if (keySpace==true && ken2flag==true) 
                        ct.drawImage(bnf_l, tx-16, ty, this);
                        

               ////////////////////////////////////////////////////////////////////////
               //キャラクタ画像の方向を変える
               if (anime == true ) anime = false; else  anime = true;
                  if (keySpace==false &&  keyDirection == 1)
                  if ( anime )
                       ct.drawImage(a_u1, 64, 64, this);
                  else
                       ct.drawImage(a_u2, 64, 64, this);
                   
                  if (keySpace==false &&  keyDirection == 2)
                  if ( anime )
                       ct.drawImage(a_d1, 64, 64, this);
                  else
                       ct.drawImage(a_d2, 64, 64, this);
                  if (keySpace==false &&  keyDirection == 3)
                  if ( anime )
                       ct.drawImage(a_r1, 64, 64, this);
                  else
                       ct.drawImage(a_r2, 64, 64, this);
                  if (keySpace==false &&  keyDirection == 4)
                  if ( anime )
                       ct.drawImage(a_l1, 64, 64, this);
                  else
                       ct.drawImage(a_l2, 64, 64, this);

                  ///////////////////////////////////////////////////////////////
                  //剣を出したときの画像
                  if (keySpace==true &&  keyDirection == 1)
                       ct.drawImage(a_nu, 64, 64, this);
                  if (keySpace==true &&  keyDirection == 2)
                       ct.drawImage(a_nd, 64, 64, this);
                  if (keySpace==true &&  keyDirection == 3)
                       ct.drawImage(a_nr, 64, 64, this);
                  if (keySpace==true &&  keyDirection == 4)
                       ct.drawImage(a_nl, 64, 64, this);
                       
                   g.drawImage(buf , 0 , 0 ,this);

               }                


}//class()

