// 【移植】TN8001さん
// https://gist.github.com/TN8001/c3936d5c8c1c62ec399b841f98ab16ac

import java.util.Map;
import java.util.Objects;
import ptmx.*;

PGraphics pg;
HashMap<Integer, Boolean> keyMap = new HashMap<>();

PtmxEx map;

Player player;
TmxObject whiteSword, blackSword;
TmxObject breakableWall, monster, npc, goal;

final int SPACE = 32;

Message message = new Message();


void setup() {
  size(288, 288);
  frameRate(10);
  noSmooth();
  imageMode(CENTER);
  textFont(createFont("メイリオ ボールド", 24, true));
  textSize(24);
  textLeading(26);
  rectMode(CORNERS) ;

  pg = createGraphics(width/2, height/2);

  keyMap.put(LEFT, false);
  keyMap.put(RIGHT, false);
  keyMap.put(UP, false);
  keyMap.put(DOWN, false);
  keyMap.put(SPACE, false);

  initMap();
}

void initMap() {
  map = new PtmxEx(this, "16.tmx");
  map.setDrawMode(CENTER);
  map.setPositionMode("MAP");

  var p = map.getObject("プレイヤー");
  p.setVisible(false);
  player = new Player("16.png", p.getX(), p.getY());

  whiteSword = map.getObject("ホワイトソード");
  whiteSword.setInteraction(() -> player.weapon = new Weapon("16.png", 64, 32), true);

  blackSword = map.getObject("ブラックソード");
  blackSword.setInteraction(() -> player.weapon = new Weapon("16.png", 64, 48), true);

  breakableWall = map.getObject("壊せる壁");
  breakableWall.setInteraction(() -> {
    if (!whiteSword.getVisible()) breakableWall.setVisible(false);
  }
  , false);

  monster = map.getObject("モンスター");
  monster.setInteraction(() -> {
    if (!blackSword.getVisible()) goal.setVisible(true);
  }
  , true);

  npc = map.getObject("NPC");
  npc.setInteraction(() -> message.set("こんにちは、少尉。"), false);

  goal = map.getObject("ゴール");
  goal.setVisible(false);
}

void draw() {
  push();
  translate(width/2, height/2);
  scale(2);

  player.update();

  map.draw(pg, player.x, player.y);
  image(pg, 0, 0);

  player.draw();
  pop();

  message.draw();


  if (goal.getVisible()  && player.x == goal.getX() && player.y == goal.getY()) {
    fill(0, 0, 255);
    textSize(48);
    textAlign(CENTER, CENTER);
    text("ゴール", 0, 0, width, height/2);
    noLoop();
  }
}

void keyPressed() {
  if (keyMap.containsKey(keyCode)) keyMap.put(keyCode, true);
}
void keyReleased() {
  if (keyMap.containsKey(keyCode)) keyMap.put(keyCode, false);
}

enum Direction {
  DOWN(0, 16), LEFT(-16, 0), RIGHT(16, 0), UP(0, -16);

  int dx, dy;

  private Direction(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }

  int nextX(int x) {
    switch (this) {
    case LEFT:
      return x - 1;
    case RIGHT:
      return x + 1;
    }
    return x;
  }

  int nextY(int y) {
    switch (this) {
    case UP:
      return y - 1;
    case DOWN:
      return y + 1;
    }
    return y;
  }
}

class Weapon {
  PImage img;

  Weapon(String path, int x, int y) {
    img = loadImage(path).get(x, y, 64, 16);
  }

  void draw(Direction direction) {
    var sx = direction.ordinal(); // 0123
    var dx = direction.dx;
    var dy = direction.dy;
    copy(img, sx * 16, 0, 16, 16, -8 + dx, -8 + dy, 16, 16); // imageMode(CENTER) 効かない
  }
}

class Player {
  int x, y, anime;
  Direction direction = Direction.DOWN;
  PImage img;
  Weapon weapon;
  boolean isSwinging;

  Player(String path, int x, int y) {
    img = loadImage(path).get(0, 0, 48, 64);
    this.x = x;
    this.y = y;
  }

  void update() {
    anime++;

    var prevX = x;
    var prevY = y;

    // 斜めに進めないように else if
    if (keyMap.get(LEFT)) {
      x--;
      direction = Direction.LEFT;
    } else if (keyMap.get(RIGHT)) {
      x++;
      direction = Direction.RIGHT;
    } else if (keyMap.get(UP)) {
      y--;
      direction = Direction.UP;
    } else if (keyMap.get(DOWN)) {
      y++;
      direction = Direction.DOWN;
    }

    if (0 < map.getTileIndex("障害物", x, y)) {
      x = prevX;
      y = prevY;
    }

    for (var o : map.getAllObjects()) {
      if (Objects.equals("アイテム", o.getLayerName())) continue;
      if (o.getVisible() && x == o.getX() && y == o.getY()) {
        x = prevX;
        y = prevY;
        break;
      }
    }

    isSwinging = keyMap.get(SPACE);
    if (keyMap.get(SPACE)) {
      for (var o : map.getAllObjects()) {
        if (Objects.equals("アイテム", o.getLayerName())) continue;
        int nx = direction.nextX(x);
        int ny = direction.nextY(y);
        if (o.getVisible() && nx == o.getX() && ny == o.getY()) {
          o.getInteraction().run();
          if (Objects.equals("友好", o.getLayerName())) isSwinging = false;
          break;
        }
      }
    }

    for (var o : map.getObjects("アイテム")) {
      if (o.getVisible() && x == o.getX() && y == o.getY()) {
        o.getInteraction().run();
        break;
      }
    }
  }

  void draw() {
    var sx = anime % 2;
    if (keyMap.get(SPACE)) sx = 2;
    var sy = direction.ordinal(); // 0123
    copy(img, sx * 16, sy * 16, 16, 16, -8, -8, 16, 16); // imageMode(CENTER) 効かない

    if (isSwinging && weapon != null) weapon.draw(direction);
  }
}


class Message {
  String message;
  int index;

  void set(String message) {
    if (!Objects.equals(this.message, message)) index = 0;
    this.message = message;
  }

  void draw() {
    if (message == null || message.isEmpty()) return;

    noFill();
    stroke(50);
    strokeWeight(3);
    rect(20, 20, width - 20, height/2 - 34, 8);

    fill(50);
    var i = message.length() < index ? message.length() : index;
    text(message.substring(0, i), 24, 22, width - 23, height/2 - 32);

    index += frameCount % 2 == 0 ? 1 : 0;
    if (message.length() * 3 < index) {
      message = "";
      index = 0;
    }
  }
}

// 汎用マップエディタ（海外ではデファクトスタンダードだと思う）
// [Tiled | Flexible level editor](https://www.mapeditor.org/)

// tmx（Tiledのファイル形式）を読むライブラリ（ライブラリをインポートの中にいます）
// [linux-man/ptmx: Use Tiled maps on your Processing sketch.](https://github.com/linux-man/ptmx)

// 微妙に気が利かないのでちょい拡張
static class PtmxEx extends Ptmx {
  private IntDict layerDict = new IntDict();
  private ArrayList<TmxObject> allObjects = new ArrayList<>();
  private HashMap<String, ArrayList<TmxObject>> objectsDict = new HashMap<>();


  public PtmxEx(PApplet p, String filename) {
    super(p, filename);

    var i = -1;
    while (true) {
      i++;
      if (getType(i) == null) break;

      layerDict.add(getName(i), i);
      if (!getType(i).equals("objectgroup")) continue;

      var list = objectsDict.get(getName(i));
      if (list == null) {
        list = new ArrayList<TmxObject>();
        objectsDict.put(getName(i), list);
      }
      for (var d : getObjects(i)) {
        var o = new TmxObject(getName(i), d, getTileSize());
        allObjects.add(o);
        list.add(o);
      }
    }
  }

  public int getTileIndex(String layerName, int x, int y) { // レイヤ名がかぶっていたら知らんｗ
    return getTileIndex(layerDict.get(layerName), x, y);
  }

  public TmxObject getObject(String name) { // オブジェクト名がかぶっていたら知らんｗ
    for (var o : allObjects) {
      if (Objects.equals(o.getName(), name)) {
        return o;
      }
    }
    return null;
  }

  public Iterable<TmxObject> getObjects(String layerName) {
    return objectsDict.get(layerName);
  }

  public Iterable<TmxObject> getAllObjects() {
    return allObjects;
  }
}

static class TmxObject {
  private StringDict dict;
  private PVector tileSize;
  private String layerName;
  private Runnable interaction = () -> {
  };

  TmxObject(String layerName, StringDict dict, PVector tileSize) {
    this.layerName = layerName;
    this.dict = dict;
    this.tileSize = tileSize;
  }

  public String getName() {
    return dict.get("name");
  }

  public String getLayerName() {
    return layerName;
  }

  public boolean getVisible() {
    return !"0".equals(dict.get("visible"));
  }
  public void setVisible(boolean v) {
    if (v) dict.remove("visible");
    else dict.set("visible", "0");
  }

  public int getX() {
    return int(int(dict.get("x")) / tileSize.x);
  }
  public void setX(int x) {
    dict.set("x", Integer.toString(int(x * tileSize.x)));
  }

  public int getY() {
    return int(int(dict.get("y")) / tileSize.y - 1); // 左下座標
  }
  public void setY(int y) {
    dict.set("y", Integer.toString(int(++y * tileSize.y))); // 左下座標
  }

  public Runnable getInteraction() {
    return interaction;
  }
  public void setInteraction(Runnable interaction, boolean remove) {
    if (remove) {
      this.interaction = () -> {
        interaction.run();
        setVisible(false);
      };
    } else this.interaction = interaction;
  }
}
