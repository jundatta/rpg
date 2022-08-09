// ゲーム画面
class GameSceneDemo extends GameSceneScene {
  @Override void mousePressed() {
    gGameStack.change(new GameSceneTitle());
  }
  @Override void keyPressed() {
    // キー入力されたらタイトルに戻る
    super.keyPressed();

    gGameStack.change(new GameSceneTitle());
  }
}
