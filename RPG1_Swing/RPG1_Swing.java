// [【プログラム】Javaでゲームを作る（前編）！Javaで画像の表示、カーソル操作、マップ画像の描画について解説！初級者向けです。☆プログラミング☆ゲーム - YouTube](https://www.youtube.com/watch?v=Cy5IqYeqiIo)
// [電子科 - YouTube](https://www.youtube.com/channel/UCqnaytXaX-aO3qTw2jKM4XQ/about)

// 【移植】TN8000さん
// https://gist.github.com/TN8001/ca1e2fc531b659fda2ab8287ed2ee254


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class RPG1_Swing extends JPanel implements ActionListener {
    public static void main(String[] args) { new RPG1_Swing(); }

    // 背景イメージ
    private Image[] haikei;
    private int x = 18, y = 18;
    private Image a_u1, a_u2, a_d1, a_d2, a_r1, a_r2, a_l1, a_l2; // キャラの変数
    private Image nf, nf_u, nf_d, nf_r, nf_l; // ホワイトソード画像
    private Image bnf, bnf_u, bnf_d, bnf_r, bnf_l; // ブラックソード画像
    private Image a_nu, a_nd, a_nr, a_nl;
    private Image teki_1;
    private Image charact1; // 新しいキャラクター

    private boolean anime, teki1 = true, teki2 = true; // teki1:敵、teki2:壁
    private boolean keyUp, keyDown, keyLeft, keyRight, keySpace; // キー押下フラグ
    private boolean A; // A key
    private boolean ken1flag, ken2flag;

    private boolean goal; // ゴールのフラグ

    private int keyDirection = 2; // 整数型でkeyEvent情報を記録する変数

    // region private final int[][] map = { { } }
    private final int[][] map = { { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
        { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
        { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
        { 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 3, 3, 3 },

        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 0, 2, 0, 2, 3, 3, 3 },

        { 3, 3, 3, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 3, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 2, 3, 3, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 9, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 4, 0, 0, 3, 0, 0, 0, 0, 0, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 3, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 2, 3, 3, 3 },

        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 2, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1, 0, 0, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 2, 2, 2, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 6, 3, 2, 3, 3, 3 },
        { 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3 },
        { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
        { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 },
        { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 } };
//    private final int[][] map ={{3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
//        {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
//        {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
//        {3,3,3,2,2,2,2,2,2, 2,2,2,2,2,2,2,2,2, 2,2,2,2,2,2,2,2,2, 2,2,2,2,2,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1,0,0,0,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,1,0,0,0,0,0,0, 0,0,0,0,0,1,0,0,0, 0,0,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,1,0,0, 0,0,0,0,0,0,0,0,1, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,1, 0,0,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
//
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,1, 0,0,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,2,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 3,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 3,0,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,1,0,0,1, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,1,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,1,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 3,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,3,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,2,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 1,0,0,0,1,0,0,0,3, 0,0,0,2,0,2,3,3,3},
//
//        {3,3,3,2,0,0,1,0,0, 0,0,0,0,1,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,2,2,2,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 8,0,0,0,0,0,0,0,0, 0,2,3,3,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,1, 0,0,0,1,0,0,0,0,0, 1,2,3,3,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,1,0,0,0,9,0,0, 0,0,7,0,0,0,0,0,2, 0,0,0,0,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,1,0,0,1,0,0,0,0, 0,0,1,0,4,0,0,3,0, 0,0,0,0,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,3,0, 0,0,0,0,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,1,0, 0,0,0,0,0,0,0,0,0, 1,0,0,0,0,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 3,0,1,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,3,0,0,2,3,3,3},
//
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
//        {3,3,3,2,0,0,2,2,2, 0,0,0,0,0,0,0,0,3, 0,0,1,0,0,0,0,0,0, 0,0,0,0,0,2,3,3,3},
//        {3,3,3,2,0,0,2,3,2, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,2,0,0, 0,0,1,0,0,2,3,3,3},
//        {3,3,3,2,0,0,2,2,2, 0,1,0,0,1,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,2,2,2,2,3,3,3},
//        {3,3,3,2,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0,0, 0,0,5,6,3,2,3,3,3},
//        {3,3,3,2,2,2,2,2,2, 2,2,2,2,2,2,2,2,2, 2,2,2,2,2,2,2,2,2, 2,2,2,2,2,2,3,3,3},
//        {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
//        {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3},
//        {3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3, 3,3,3,3,3,3,3,3,3}};
// endregion

    public RPG1_Swing() {
        init();

        setPreferredSize(new Dimension(288, 288));

        var frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        setFocusable(true);
        requestFocusInWindow();

        new Timer(100, this).start();
    }

    public void init() {
        haikei = new Image[]{
            getImage(getDocumentBase(), "mapA.gif"),
            getImage(getDocumentBase(), "map2.gif"),
            getImage(getDocumentBase(), "kabe1.gif"),
            getImage(getDocumentBase(), "map4.gif"),
            getImage(getDocumentBase(), "map11.gif"),
            getImage(getDocumentBase(), "goal.gif"),
        };

        a_u1 = getImage(getDocumentBase(), "a_u1.gif");
        a_u2 = getImage(getDocumentBase(), "a_u2.gif");
        a_d1 = getImage(getDocumentBase(), "a_d1.gif");
        a_d2 = getImage(getDocumentBase(), "a_d2.gif");
        a_r1 = getImage(getDocumentBase(), "a_r1.gif");
        a_r2 = getImage(getDocumentBase(), "a_r2.gif");
        a_l1 = getImage(getDocumentBase(), "a_l1.gif");
        a_l2 = getImage(getDocumentBase(), "a_l2.gif");

        // ソードを出すときのキャラクタ画像
        a_nu = getImage(getDocumentBase(), "a_nu.gif");
        a_nd = getImage(getDocumentBase(), "a_nd.gif");
        a_nr = getImage(getDocumentBase(), "a_nr.gif");
        a_nl = getImage(getDocumentBase(), "a_nl.gif");

        // ホワイトソード画像
        nf = getImage(getDocumentBase(), "nf.gif");
        nf_u = getImage(getDocumentBase(), "nfu.gif");
        nf_d = getImage(getDocumentBase(), "nfd.gif");
        nf_r = getImage(getDocumentBase(), "nfr.gif");
        nf_l = getImage(getDocumentBase(), "nfl.gif");

        // ブラックソード画像
        bnf = getImage(getDocumentBase(), "bnf.gif");
        bnf_u = getImage(getDocumentBase(), "bnfu.gif");
        bnf_d = getImage(getDocumentBase(), "bnfd.gif");
        bnf_r = getImage(getDocumentBase(), "bnfr.gif");
        bnf_l = getImage(getDocumentBase(), "bnfl.gif");

        // 敵画像
        teki_1 = getImage(getDocumentBase(), "teki1.gif");

        // キャラクター
        charact1 = getImage(getDocumentBase(), "charact1.gif");

        /////////////////////////////////////////////////////////
        // キー入力の受け付け開始
        addKeyListener(new KeyAdapter() {
            // カーソル「上：１、下：２、右：３、左：４」とする
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        keyUp = true;
                        keyDirection = 1;
                    }
                    case KeyEvent.VK_DOWN -> {
                        keyDown = true;
                        keyDirection = 2;
                    }
                    case KeyEvent.VK_RIGHT -> {
                        keyRight = true;
                        keyDirection = 3;
                    }
                    case KeyEvent.VK_LEFT -> {
                        keyLeft = true;
                        keyDirection = 4;
                    }
                    case KeyEvent.VK_SPACE -> keySpace = true;
                    case KeyEvent.VK_A -> A = true;
                }
//                repaint();
            }

            // キーが離されたときの処理
            @Override public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> keyUp = false;
                    case KeyEvent.VK_DOWN -> keyDown = false;
                    case KeyEvent.VK_RIGHT -> keyRight = false;
                    case KeyEvent.VK_LEFT -> keyLeft = false;
                    case KeyEvent.VK_SPACE -> keySpace = false;
                    case KeyEvent.VK_A -> A = false;
                }
            }
        });
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        var ct = (Graphics2D) g;
        ct.scale(2, 2);

        for (var i = 0; i < 9; i++) {
            for (var j = 0; j < 9; j++) {
                ct.drawImage(haikei[0], i * 16, j * 16, this);

                var a = map[y - 4 + j][x - 4 + i];
                switch (a) {
                    case 0 -> ct.drawImage(haikei[0], i * 16, j * 16, this);
                    case 1 -> ct.drawImage(haikei[1], i * 16, j * 16, this);
                    case 2 -> ct.drawImage(haikei[2], i * 16, j * 16, this);
                    case 3 -> ct.drawImage(haikei[3], i * 16, j * 16, this);
                    case 4 -> { if (teki1) ct.drawImage(teki_1, i * 16, j * 16, this); }
                    case 5 -> { if (teki2) ct.drawImage(haikei[4], i * 16, j * 16, this); }
                    case 6 -> { if (!ken2flag) ct.drawImage(bnf, i * 16, j * 16, this); }
                    case 7 -> { if (!ken1flag && !ken2flag) ct.drawImage(nf, i * 16, j * 16, this); }
                    case 8 -> { if (goal) ct.drawImage(haikei[5], i * 16, j * 16, this); }
                    case 9 -> ct.drawImage(charact1, i * 16, j * 16, this);
                    default -> throw new IllegalStateException();
                }
            }
        }

        anime = !anime;

        var tx = 64;
        var ty = 64;
        switch (keyDirection) {
            case 1 -> {
                if (keySpace) {
                    ct.drawImage(a_nu, tx, ty, this);
                    if (ken1flag) ct.drawImage(nf_u, tx, ty - 16, this);
                    if (ken2flag) ct.drawImage(bnf_u, tx, ty - 16, this);
                } else {
                    ct.drawImage(anime ? a_u1 : a_u2, tx, ty, this);
                }
            }
            case 2 -> {
                if (keySpace) {
                    ct.drawImage(a_nd, tx, ty, this);
                    if (ken1flag) ct.drawImage(nf_d, tx, ty + 16, this);
                    if (ken2flag) ct.drawImage(bnf_d, tx, ty + 16, this);
                } else {
                    ct.drawImage(anime ? a_d1 : a_d2, tx, ty, this);
                }
            }
            case 3 -> {
                if (keySpace) {
                    ct.drawImage(a_nr, tx, ty, this);
                    if (ken1flag) ct.drawImage(nf_r, tx + 16, ty, this);
                    if (ken2flag) ct.drawImage(bnf_r, tx + 16, ty, this);
                } else {
                    ct.drawImage(anime ? a_r1 : a_r2, tx, ty, this);
                }
            }
            case 4 -> {
                if (keySpace) {
                    ct.drawImage(a_nl, tx, ty, this);
                    if (ken1flag) ct.drawImage(nf_l, tx - 16, ty, this);
                    if (ken2flag) ct.drawImage(bnf_l, tx - 16, ty, this);
                } else {
                    ct.drawImage(anime ? a_l1 : a_l2, tx, ty, this);
                }
            }
        }

        if (map[y][x] == 8 && goal) {
            ct.setColor(Color.blue);
            ct.drawString("ゴール", 55, 50);
        }
        if (getFront() == 9 && A) {
            ct.setColor(Color.blue);
            ct.drawString("こんにちは、少尉。", 25, 50);
        }
    }


    @Override public void actionPerformed(ActionEvent e) {
        var xx = x;
        var yy = y;

        if (keyUp) y--;
        if (keyDown) y++;
        if (keyRight) x++;
        if (keyLeft) x--;

        if (map[y][x] == 2 || map[y][x] == 3) { x = xx; y = yy; }
        if (map[y][x] == 4 && teki1) { x = xx; y = yy; }
        if (map[y][x] == 5 && teki2) { x = xx; y = yy; }

        if (keySpace) {
            if (getFront() == 5 && ken1flag) teki2 = false;
            if (getFront() == 4 && ken2flag) { teki1 = false; goal = true; }
        }

        if (map[y][x] == 7) ken1flag = true;
        if (map[y][x] == 6) { ken1flag = false; ken2flag = true; }
        if (map[y][x] == 9) { x = xx; y = yy; }

        repaint();
    }


    private int getFront() {
        return switch (keyDirection) {
            case 1 -> map[y - 1][x];
            case 2 -> map[y + 1][x];
            case 3 -> map[y][x + 1];
            case 4 -> map[y][x - 1];
            default -> throw new IllegalStateException();
        };
    }

    private Image getImage(String dir, String name) {
        try {
            return ImageIO.read(new File(dir, name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getDocumentBase() { return "."; }
}