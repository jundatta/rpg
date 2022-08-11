// ゲーム画面
class GameScenePlay extends GameSceneScene {
  @Override void keyPressed() {
    // ESCキーが入力されたら中断する
    if (key == ESC) {
      key = 0;  // プログラムが終了しないように書き換えた
      gGameStack.push(new GameScenePause());
      return;
    }
    super.keyPressed();
  }
}
