/*　武器の操作 */

import java.applet.Applet;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import java.awt.*;

public class Shooting9 extends Applet implements Runnable {

    // 背景イメージ
    Image haikei[];
    int x;
    int y;
    int a, i, j;
    int tx, ty;// ナイフの座標
    java.awt.Image a_u1, a_u2, a_d1, a_d2, a_r1, a_r2, a_l1, a_l2;//キャラの変数
    Image nf_u, nf_d, nf_r, nf_l;// ナイフ画像
    Image a_r, a_l, a_nu, a_nd, a_nr, a_nl;
    boolean nflag;// ナイフの発射フラグ
    boolean anime;
    boolean keySpace;// キー押下フラグ
    Thread gameThread;
    int keyDirection = 2;//整数型でkeyEvent情報を記録する変数
    int map[][] ={{3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
                  {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
                  {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
                  {3,3,3,2,2,2,2,2,2, 2,2,2,2,2,2,2,2,2, 2,2,2,2,2,2,2,2,2, 2,2,2,2,2,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,1,0,0,0,0, 0,0,0,0,1,0,0,0,1, 0,0,1,0,0,0,0,0,0, 0,0,1,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,1,0,0,0,0, 2,0,0,0,0,0,0,0,0, 0,1,0,0,0,0,0,0,0, 1,0,0,2,0,2,3,3,3},

                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,1, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,2,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 3,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 3,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,1,0,0,1, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,1,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,1,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 3,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,3,0,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3}, 
                  {3,3,3,2,0,0,0,0,0, 0,1,0,0,0,0,0,0,0, 1,0,0,0,1,0,0,0,3, 0,0,0,2,0,2,3,3,3},

                  {3,3,3,2,0,0,1,0,0, 0,0,0,0,1,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,2,2,2,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,2,3,3,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,3, 0,0,0,0,0,0,0,0,1, 0,0,0,1,0,0,0,0,0, 1,2,3,3,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,1,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,2, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,1,0,0,1,0,0,0,0, 0,0,1,0,0,0,0,3,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,3,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,1,0, 0,0,0,0,0,0,0,0,0, 1,0,0,0,0,2,3,3,3}, 
                  {3,3,3,2,0,0,0,0,0, 3,0,1,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,3,0,0,2,3,3,3},

                  {3,3,3,2,0,0,0,0,0, 0,0,1,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,2,2,2, 0,0,0,0,0,0,0,0,3, 0,0,1,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
                  {3,3,3,2,0,0,2,3,2, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,2,0,0, 0,0,1,0,0,2,3,3,3},
                  {3,3,3,2,0,0,2,2,2, 0,1,0,0,1,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,4,3,2,3,3,3},
                  {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,3,3,3,2,3,3,3},
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
        haikei[2] = getImage(getDocumentBase(), "map11.gif");
        haikei[3] = getImage(getDocumentBase(), "map4.gif");
        haikei[4] = getImage(getDocumentBase(), "map10.gif");

        a_u1 = getImage(getDocumentBase(), "a_u1.gif");
        a_u2 = getImage(getDocumentBase(), "a_u2.gif");
        a_d1 = getImage(getDocumentBase(), "a_d1.gif");
        a_d2 = getImage(getDocumentBase(), "a_d2.gif");
        a_r1 = getImage(getDocumentBase(), "a_r1.gif");
        a_r2 = getImage(getDocumentBase(), "a_r2.gif");
        a_l1 = getImage(getDocumentBase(), "a_l1.gif");
        a_l2 = getImage(getDocumentBase(), "a_l2.gif");

        a_nu = getImage(getDocumentBase(), "a_nu.gif");
        a_nd = getImage(getDocumentBase(), "a_nd.gif");
        a_nr = getImage(getDocumentBase(), "a_nr.gif");
        a_nl = getImage(getDocumentBase(), "a_nl.gif");

        nf_u = getImage(getDocumentBase(), "nfu.gif");
        nf_d = getImage(getDocumentBase(), "nfd.gif");
        nf_r = getImage(getDocumentBase(), "nfr.gif");
        nf_l = getImage(getDocumentBase(), "nfl.gif");

        //キャラ座標の初期値
          x=18; y=18;//マップ３６×３６の中心
        // ナイフフラグをクリアする
          nflag = false;
        // キーフラグをクリア
          keySpace = false;

        /////////////////////////////////////////////////////////
        // キー入力の受け付け開始
           addKeyListener(new java.awt.event.KeyListener() {
           public void keyTyped(KeyEvent e){}

           //　カーソル「上：１、下：２、右：３、左：４」とする
           public void keyPressed(KeyEvent e){
                  switch(e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                        y -= 1;//y方向に-1ずつ減らす            
                        keyDirection = 1;// カーソル上キーが押下されたら1を代入
                        break;
                        case KeyEvent.VK_DOWN:
                        y += 1;//ｙ方向に+1ずつ増やす
                        keyDirection = 2;// カーソル下キーが押下されたら2を代入
                        break;
                        case KeyEvent.VK_RIGHT:
                        x += 1;//ｘ方向に+1ずつ増やす            
                        keyDirection = 3;// カーソル右キーがされたら3を代入
                        break;
                        case KeyEvent.VK_LEFT:
                        x -= 1;//ｘ方向に-1ずつ減らす
                        keyDirection = 4;// カーソル左キーが押下されたら4を代入
                        break;
                        case KeyEvent.VK_SPACE:
                        keySpace = true;
                        break; 
                     }

                        //当り判定
                        if(keyDirection==1 && map[y][x]==3 || keyDirection==1 && map[y][x]==2 || keyDirection==1 && map[y][x]==4) y=y+1;
                        if(keyDirection==2 && map[y][x]==3 || keyDirection==2 && map[y][x]==2 || keyDirection==2 && map[y][x]==4) y=y-1;
                        if(keyDirection==3 && map[y][x]==3 || keyDirection==3 && map[y][x]==2 || keyDirection==3 && map[y][x]==4) x=x-1;
                        if(keyDirection==4 && map[y][x]==3 || keyDirection==4 && map[y][x]==2 || keyDirection==4 && map[y][x]==4) x=x+1;

                          repaint();
               }

    // キーが離されたときの処理
    public void keyReleased(KeyEvent e) {
                   switch (e.getKeyCode()) {
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

                             if(keySpace)
                                if(!nflag)
                                    nflag=true;

                              if(nflag) nflag=false;
                              repaint();

                             Thread.sleep(500);
                           } catch (InterruptedException e) {
                      break;
                    }

                 }
             }

   // -----------------------------------------------------------
   public void paint(Graphics g) {
   // マップ配列の初期化
   //９×９の背景描画
     for(i=0; i<9; i++){
         for(j=0; j<9; j++){
             a = map[y-4+j][x-4+i];//どこを中心に描画するか
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
                 else if(a == 4){
                     g.drawImage(haikei[4], i*16, j*16, this);
                     }
                  }
              }

             ////////////////////////////////////////////////////
             //剣を出す処理
             //上向きの剣
               if(keyDirection==1)
                    if (keySpace==true) 
                        g.drawImage(nf_u, 64, 64-16, this);
                  
             //下向きの剣はキャラクタ画像処理の次（下剣へ）
             //右向きの剣
               if (keyDirection==3)
                    if (keySpace==true)
                        g.drawImage(nf_r, 64+16, 64, this);
             //左向きの剣
               if (keyDirection==4)
                    if (keySpace==true) 
                        g.drawImage(nf_l, 64-16, 64, this);


             ////////////////////////////////////////////////////////////////////////
             //キャラクタ画像の方向を変える
               if (nflag == true ) nflag = false; else  nflag = true;
                  if (keySpace==false &&  keyDirection == 1)//キーUP押下時、アニメを上向き画像に切り替える
                  if ( nflag )
                       g.drawImage(a_u1, 64, 64, this);
                  else
                       g.drawImage(a_u2, 64, 64, this);
                  if (keySpace==false &&  keyDirection == 2)//キーDOWN押下時、アニメを下向き画像に切り替える
                  if ( nflag )
                       g.drawImage(a_d1, 64, 64, this);
                  else
                       g.drawImage(a_d2, 64, 64, this);
                  if (keySpace==false &&  keyDirection == 3)//キーRIGHT押下時、アニメを右向き画像に切り替える
                  if ( nflag )
                       g.drawImage(a_r1, 64, 64, this);
                  else
                       g.drawImage(a_r2, 64, 64, this);
                  if (keySpace==false &&  keyDirection == 4)//キーLEFT押下時、アニメを左向き画像に切り替える
                  if ( nflag )
                       g.drawImage(a_l1, 64, 64, this);
                  else
                       g.drawImage(a_l2, 64, 64, this);

                  ///////////////////////////////////////////////////////////////                      
                  //剣を出したときの画像
                  if (keySpace==true &&  keyDirection == 1)
                       g.drawImage(a_nu, 64, 64, this);
                  if (keySpace==true &&  keyDirection == 2)
                       g.drawImage(a_nd, 64, 64, this);
                  if (keySpace==true &&  keyDirection == 3)
                       g.drawImage(a_nr, 64, 64, this);
                  if (keySpace==true &&  keyDirection == 4)
                       g.drawImage(a_nl, 64, 64, this);
                       

                  ///////////////////////////////////////////////////////////////
                  //（下剣）
                  //下向き時、キャラクタ上部から剣がでるために
                  //キャラ画像の次に処理をする
                  //画像が重なった時に隠れないようにするため
                  if (keyDirection==2)
                        if (keySpace==true)
                        g.drawImage(nf_d, 64, 64+16, this);


               }

   // -----------------------------------------------------------
   public void update(Graphics g) {  // 再作画処理
    paint( g );
   } // update()

}//class()

