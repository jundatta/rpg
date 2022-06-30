// 【作者】グァン・ファンミン（電子科）さん
// 【作品名】RPG1？
// Javaでゲームを作る（前編）！
//   https://www.youtube.com/watch?v=Cy5IqYeqiIo
// Javaでゲームを作る（中編）！
//   https://www.youtube.com/watch?v=XnaTvzn7UPU
// Javaでゲームを作る（後編）！
//   https://www.youtube.com/watch?v=CPtEV8aygXI
// 【移植】PC-8001（TN8001）様


// [【プログラム】Javaでゲームを作る（後編）！Javaで「簡単なRPG」を作るためのプログラム方法について解説！初級者向けです。☆プログラミング☆ゲーム☆java - YouTube](https://www.youtube.com/watch?v=CPtEV8aygXI)


// [キャラクターチップ１ - ぴぽや倉庫](https://pipoya.net/sozai/assets/charachip/character-chip-1/)
// キャラチップ.zip
//    pipo-charachip018a.png
//    pipo-charachip019e.png
//    pipo-charachip029d.png
//
// [マップチップ　32×32～ - ぴぽや倉庫](https://pipoya.net/sozai/assets/map-chip_tileset32/)
// ウディタ2_32x32mapchip_20210215.zip
//    [Base]BaseChip_pipo.png
//    [A]Water2_pipo.png


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class RPG1 extends Frame implements Runnable {
    public static void main(String[] args) { new RPG1(); }


    Image haikei[];
    int x, y;
    int a, i, j;
    int tx, ty;
    Image a_u1, a_u2, a_d1, a_d2, a_r1, a_r2, a_l1, a_l2;
    Image nf, nf_u, nf_d, nf_r, nf_l;
    Image bnf, bnf_u, bnf_d, bnf_r, bnf_l;
    Image a_r, a_l, a_nu, a_nd, a_nr, a_nl;
    Image teki_1;
    Image charact1;

    Graphics ct;
    Image buf;

    boolean anime, teki1, teki2;
    boolean keyUp, keyDown, keyLeft, keyRight, keySpace;
    boolean A;
    boolean ken1flag, ken2flag;

    boolean goal;

    Thread gameThread;
    int keyDirection = 2;

    int map[][] = {
        {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
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
        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1,0,0,0,0,1,0,0,3, 0,0,0,2,0,2,3,3,3},

        {3,3,3,2,0,0,1,0,0, 0,0,0,0,1,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,2,2,2,0,2,3,3,3},
        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 8,0,0,0,0,0,0,0,0, 0,2,3,3,0,2,3,3,3},
        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,1, 0,0,0,1,0,0,0,0,0, 1,2,3,3,0,2,3,3,3},
        {3,3,3,2,0,0,0,0,0, 0,0,1,0,0,0,9,0,0, 0,0,7,0,0,0,0,0,2, 0,0,0,0,0,2,3,3,3},
        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
        {3,3,3,2,0,0,0,0,0, 0,1,0,0,1,0,0,0,0, 0,0,1,0,4,0,0,3,0, 0,0,0,0,0,2,3,3,3},
        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
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
        {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
    };


    public RPG1() {
        init();

        addWindowListener(new WindowAdapter() {
            @Override public void windowOpened(WindowEvent e) {
                start();
            }

            @Override public void windowClosing(WindowEvent e) {
                stop();
                System.exit(0);
            }
        });

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        var i = getInsets();
        setSize(288 + i.left + i.right, 288 + i.top + i.bottom);
    }

    public void init() {
        haikei = new Image[6];
        haikei[0] = getImage("map1.gif");
        haikei[1] = getImage("map2.gif");
        haikei[2] = getImage("kabe1.gif");
        haikei[3] = getImage("map4.gif");
        haikei[4] = getImage("map11.gif");
        haikei[5] = getImage("goal.gif");

        a_u1 = getImage("a_u1.gif");
        a_u2 = getImage("a_u2.gif");
        a_d1 = getImage("a_d1.gif");
        a_d2 = getImage("a_d2.gif");
        a_r1 = getImage("a_r1.gif");
        a_r2 = getImage("a_r2.gif");
        a_l1 = getImage("a_l1.gif");
        a_l2 = getImage("a_l2.gif");

        a_nu = getImage("a_nu.gif");
        a_nd = getImage("a_nd.gif");
        a_nr = getImage("a_nr.gif");
        a_nl = getImage("a_nl.gif");

        nf = getImage("nf.gif");
        nf_u = getImage("nfu.gif");
        nf_d = getImage("nfd.gif");
        nf_r = getImage("nfr.gif");
        nf_l = getImage("nfl.gif");

        bnf = getImage("bnf.gif");
        bnf_u = getImage("bnfu.gif");
        bnf_d = getImage("bnfd.gif");
        bnf_r = getImage("bnfr.gif");
        bnf_l = getImage("bnfl.gif");

        teki_1 = getImage("teki1.gif");

        charact1 = getImage("charact1.gif");

        x = 18;
        y = 18;
        tx = 128;
        ty = 128;

        teki1 = teki2 = true;

        keyUp = keyDown = keyLeft = keyRight = keySpace = false;

        ken1flag = false;
        ken2flag = false;

        goal = false;

        addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) { }

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        keyUp = true;
                        keyDirection = 1;
                        break;
                    case KeyEvent.VK_DOWN:
                        keyDown = true;
                        keyDirection = 2;
                        break;
                    case KeyEvent.VK_RIGHT:
                        keyRight = true;
                        keyDirection = 3;
                        break;
                    case KeyEvent.VK_LEFT:
                        keyLeft = true;
                        keyDirection = 4;
                        break;
                    case KeyEvent.VK_SPACE:
                        keySpace = true;
                        break;
                    case KeyEvent.VK_A:
                        A = true;
                        break;
                }
                repaint();
            }

            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        keyUp = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        keyDown = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        keyRight = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        keyLeft = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        keySpace = false;
                        break;
                    case KeyEvent.VK_A:
                        A = false;
                        break;
                }
            }
        });
    }

    public void start() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();

            buf = createImage(288, 288);
        }
    }

    public void stop() {
        gameThread = null;
    }

    public void run() {
        while (gameThread == Thread.currentThread()) {
            try {
                if (keyUp) y -= 1;
                if (keyDown) y += 1;
                if (keyRight) x += 1;
                if (keyLeft) x -= 1;
                if (keySpace)
                    if (!anime) anime = true;
                if (anime) anime = false;


                if (keyDirection == 1 && map[y][x] == 3 || keyDirection == 1 && map[y][x] == 2) y = y + 1;
                if (keyDirection == 2 && map[y][x] == 3 || keyDirection == 2 && map[y][x] == 2) y = y - 1;
                if (keyDirection == 3 && map[y][x] == 3 || keyDirection == 3 && map[y][x] == 2) x = x - 1;
                if (keyDirection == 4 && map[y][x] == 3 || keyDirection == 4 && map[y][x] == 2) x = x + 1;

                if (keyDirection == 1 && map[y][x] == 4 && teki1 == true) y = y + 1;
                if (keyDirection == 2 && map[y][x] == 4 && teki1 == true) y = y - 1;
                if (keyDirection == 3 && map[y][x] == 4 && teki1 == true) x = x - 1;
                if (keyDirection == 4 && map[y][x] == 4 && teki1 == true) x = x + 1;

                if (keyDirection == 1 && map[y][x] == 5 && teki2 == true) y = y + 1;
                if (keyDirection == 2 && map[y][x] == 5 && teki2 == true) y = y - 1;
                if (keyDirection == 3 && map[y][x] == 5 && teki2 == true) x = x - 1;
                if (keyDirection == 4 && map[y][x] == 5 && teki2 == true) x = x + 1;


                if (keyDirection == 1 && map[y - 1][x] == 4 && keySpace == true && ken2flag == true) { teki1 = false; goal = true; }
                if (keyDirection == 2 && map[y + 1][x] == 4 && keySpace == true && ken2flag == true) { teki1 = false; goal = true; }
                if (keyDirection == 3 && map[y][x + 1] == 4 && keySpace == true && ken2flag == true) { teki1 = false; goal = true; }
                if (keyDirection == 4 && map[y][x - 1] == 4 && keySpace == true && ken2flag == true) { teki1 = false; goal = true; }

                if (keyDirection == 1 && map[y - 1][x] == 5 && keySpace == true && ken1flag == true) teki2 = false;
                if (keyDirection == 2 && map[y + 1][x] == 5 && keySpace == true && ken1flag == true) teki2 = false;
                if (keyDirection == 3 && map[y][x + 1] == 5 && keySpace == true && ken1flag == true) teki2 = false;
                if (keyDirection == 4 && map[y][x - 1] == 5 && keySpace == true && ken1flag == true) teki2 = false;


                if (keyDirection == 1 && map[y][x] == 7) ken1flag = true;
                if (keyDirection == 2 && map[y][x] == 7) ken1flag = true;
                if (keyDirection == 3 && map[y][x] == 7) ken1flag = true;
                if (keyDirection == 4 && map[y][x] == 7) ken1flag = true;

                if (keyDirection == 1 && map[y][x] == 6) { ken1flag = false; ken2flag = true; }
                if (keyDirection == 2 && map[y][x] == 6) { ken1flag = false; ken2flag = true; }
                if (keyDirection == 3 && map[y][x] == 6) { ken1flag = false; ken2flag = true; }
                if (keyDirection == 4 && map[y][x] == 6) { ken1flag = false; ken2flag = true; }


                if (keyDirection == 1 && map[y][x] == 9) y = y + 1;
                if (keyDirection == 2 && map[y][x] == 9) y = y - 1;
                if (keyDirection == 3 && map[y][x] == 9) x = x - 1;
                if (keyDirection == 4 && map[y][x] == 9) x = x + 1;


                repaint();

                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        if (ct == null) ct = buf.getGraphics();

        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                a = map[y - 4 + j][x - 4 + i];
                if (a == 0) {
                    ct.drawImage(haikei[0], i * 32, j * 32, this);
                } else if (a == 1) {
                    ct.drawImage(haikei[1], i * 32, j * 32, this);
                } else if (a == 2) {
                    ct.drawImage(haikei[2], i * 32, j * 32, this);
                } else if (a == 3) {
                    ct.drawImage(haikei[3], i * 32, j * 32, this);
                } //
                else if (a == 4) {
                    if (teki1) ct.drawImage(teki_1, i * 32, j * 32, this);
                    else ct.drawImage(haikei[0], i * 32, j * 32, this);
                } //
                else if (a == 5) {
                    if (teki2) ct.drawImage(haikei[4], i * 32, j * 32, this);
                    else ct.drawImage(haikei[0], i * 32, j * 32, this);
                } //
                else if (a == 6) {
                    if (ken2flag == false) ct.drawImage(bnf, i * 32, j * 32, this);
                    else ct.drawImage(haikei[0], i * 32, j * 32, this);
                } //
                else if (a == 7) {
                    if (ken1flag == false && ken2flag == false)
                        ct.drawImage(nf, i * 32, j * 32, this);
                    else ct.drawImage(haikei[0], i * 32, j * 32, this);
                } //
                else if (a == 8) {
                    if (goal == false) ct.drawImage(haikei[0], i * 32, j * 32, this);
                    else ct.drawImage(haikei[5], i * 32, j * 32, this);
                } //
                else if (a == 9) {
                    ct.drawImage(charact1, i * 32, j * 32, this);
                }
            }
        }


        if (keyDirection == 1) {
            if (keySpace == true && ken1flag == true)
                ct.drawImage(nf_u, tx, ty - 32, this);
        }
        if (keyDirection == 2) {
            if (keySpace == true && ken1flag == true)
                ct.drawImage(nf_d, tx, ty + 32, this);
        }
        if (keyDirection == 3) {
            if (keySpace == true && ken1flag == true)
                ct.drawImage(nf_r, tx + 32, ty, this);
        }
        if (keyDirection == 4) {
            if (keySpace == true && ken1flag == true)
                ct.drawImage(nf_l, tx - 32, ty, this);
        }

        if (keyDirection == 1) {
            if (keySpace == true && ken2flag == true)
                ct.drawImage(bnf_u, tx, ty - 32, this);
        }
        if (keyDirection == 2) {
            if (keySpace == true && ken2flag == true)
                ct.drawImage(bnf_d, tx, ty + 32, this);
        }
        if (keyDirection == 3) {
            if (keySpace == true && ken2flag == true)
                ct.drawImage(bnf_r, tx + 32, ty, this);
        }
        if (keyDirection == 4) {
            if (keySpace == true && ken2flag == true)
                ct.drawImage(bnf_l, tx - 32, ty, this);
        }


        if (anime == true) anime = false;
        else anime = true;

        if (keySpace == false && keyDirection == 1)
            if (anime) ct.drawImage(a_u1, 128, 128, this);
            else ct.drawImage(a_u2, 128, 128, this);
        if (keySpace == false && keyDirection == 2)
            if (anime) ct.drawImage(a_d1, 128, 128, this);
            else ct.drawImage(a_d2, 128, 128, this);
        if (keySpace == false && keyDirection == 3)
            if (anime) ct.drawImage(a_r1, 128, 128, this);
            else ct.drawImage(a_r2, 128, 128, this);
        if (keySpace == false && keyDirection == 4)
            if (anime) ct.drawImage(a_l1, 128, 128, this);
            else ct.drawImage(a_l2, 128, 128, this);


        if (keySpace == true && keyDirection == 1)
            ct.drawImage(a_nu, 128, 128, this);
        if (keySpace == true && keyDirection == 2)
            ct.drawImage(a_nd, 128, 128, this);
        if (keySpace == true && keyDirection == 3)
            ct.drawImage(a_nr, 128, 128, this);
        if (keySpace == true && keyDirection == 4)
            ct.drawImage(a_nl, 128, 128, this);


        if (keyDirection == 1 && map[y][x] == 8 && goal == true) {
            ct.setColor(Color.blue);
            ct.drawString("ゴール", 110, 100);
        }
        if (keyDirection == 2 && map[y][x] == 8 && goal == true) {
            ct.setColor(Color.blue);
            ct.drawString("ゴール", 110, 100);
        }
        if (keyDirection == 3 && map[y][x] == 8 && goal == true) {
            ct.setColor(Color.blue);
            ct.drawString("ゴール", 110, 100);
        }
        if (keyDirection == 4 && map[y][x] == 8 && goal == true) {
            ct.setColor(Color.blue);
            ct.drawString("ゴール", 110, 100);
        }


        if (keyDirection == 1 && map[y - 1][x] == 9 && A == true) {
            ct.setColor(Color.blue);
            ct.drawString("こんにちは、少尉。", 50, 100);
        }
        if (keyDirection == 2 && map[y + 1][x] == 9 && A == true) {
            ct.setColor(Color.blue);
            ct.drawString("こんにちは、少尉。", 50, 100);
        }
        if (keyDirection == 3 && map[y][x + 1] == 9 && A == true) {
            ct.setColor(Color.blue);
            ct.drawString("こんにちは、少尉。", 50, 100);
        }
        if (keyDirection == 4 && map[y][x - 1] == 9 && A == true) {
            ct.setColor(Color.blue);
            ct.drawString("こんにちは、少尉。", 50, 100);
        }

        var i = getInsets();
        g.drawImage(buf, i.left, i.top, this);
    }

    private Image get(String s, int x, int y) {
        try {
            var image = ImageIO.read(new File(s));
            return image.getSubimage(x, y, 32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Image getImage(String path) {
//        return getToolkit().getImage(path);

        return switch (path) {
            case "map1.gif" -> get("[Base]BaseChip_pipo.png", 64, 0);
            case "map2.gif" -> get("[Base]BaseChip_pipo.png", 0, 0);
            case "kabe1.gif" -> get("[Base]BaseChip_pipo.png", 32, 1152);
            case "map4.gif" -> get("[A]Water2_pipo.png", 0, 128);
            case "map11.gif" -> get("[Base]BaseChip_pipo.png", 96, 4448);
            case "goal.gif" -> get("[Base]BaseChip_pipo.png", 128, 4384);

            case "a_u1.gif" -> get("pipo-charachip018a.png", 0, 96);
            case "a_u2.gif" -> get("pipo-charachip018a.png", 64, 96);
            case "a_d1.gif" -> get("pipo-charachip018a.png", 0, 0);
            case "a_d2.gif" -> get("pipo-charachip018a.png", 64, 0);
            case "a_r1.gif" -> get("pipo-charachip018a.png", 0, 64);
            case "a_r2.gif" -> get("pipo-charachip018a.png", 64, 64);
            case "a_l1.gif" -> get("pipo-charachip018a.png", 0, 32);
            case "a_l2.gif" -> get("pipo-charachip018a.png", 64, 32);

            case "a_nu.gif" -> get("pipo-charachip018a.png", 32, 96);
            case "a_nd.gif" -> get("pipo-charachip018a.png", 32, 0);
            case "a_nr.gif" -> get("pipo-charachip018a.png", 32, 64);
            case "a_nl.gif" -> get("pipo-charachip018a.png", 32, 32);

            case "nf.gif" -> get("[Base]BaseChip_pipo.png", 224, 736);
            case "nfu.gif" -> get("[Base]BaseChip_pipo.png", 224, 736);
            case "nfd.gif" -> get("[Base]BaseChip_pipo.png", 224, 736);
            case "nfr.gif" -> get("[Base]BaseChip_pipo.png", 224, 736);
            case "nfl.gif" -> get("[Base]BaseChip_pipo.png", 224, 736);

            case "bnf.gif" -> get("[Base]BaseChip_pipo.png", 224, 800);
            case "bnfu.gif" -> get("[Base]BaseChip_pipo.png", 224, 800);
            case "bnfd.gif" -> get("[Base]BaseChip_pipo.png", 224, 800);
            case "bnfr.gif" -> get("[Base]BaseChip_pipo.png", 224, 800);
            case "bnfl.gif" -> get("[Base]BaseChip_pipo.png", 224, 800);

            case "teki1.gif" -> get("pipo-charachip019e.png", 0, 0);
            case "charact1.gif" -> get("pipo-charachip029d.png", 32, 0);
            default -> throw new IllegalArgumentException();
        };
    }
}