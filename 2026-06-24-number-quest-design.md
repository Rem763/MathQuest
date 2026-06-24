# 数字闯关 — 设计文档

**日期**: 2026-06-24
**技术栈**: JavaFX (Scene + FXML + CSS)，Maven 构建，MySQL 数据库
**架构方案**: 纯 Scene 切换

---

## 一、场景架构 & 导航

5 个 Scene，由 SceneManager 统一管理：

```
主菜单 (TitleScene)
  ↓ 新游戏 / 继续游戏
地图选关 (MapScene)
  ↓ 选关          → 商店 (ShopScene)
战斗 (BattleScene)
  ↓ 战斗结束
结算 (ResultScene)
  ↓ 下一关 / 返回地图
```

SceneManager 持有 Stage 引用，对外暴露 `switchTo(String sceneName, Object... data)`。每个 Scene 实现 `ManagedScene` 接口，提供 `onEnter(data)` 和 `onLeave()` 回调。

---

## 二、数据模型

### Player
- name: String — 昵称
- hp / maxHp: int — 生命值
- attack: int — 基础攻击力
- defense: int — 防御力
- coins: int — 金币
- items: List<ItemStack> — 持有的道具及数量
- stars: Map<Int, Int> — 每关星级（levelId → 1~3）
- unlockedGrade: int — 已解锁年级 1~3
- unlockedLevelId: int — 已解锁到的关卡ID

### Enemy
- name: String — 怪物名
- hp / maxHp: int
- attack: int
- image: String — 资源路径
- difficulty: int — 1~5，决定题目范围
- dropCoins: int — 死亡掉落金币

### Question
- text: String — "12 + 7 = ?"
- answer: int — 正确答案
- options: int[4] — 含正确答案的4个选项
- difficulty: int — 难度 1~5
- type: Operation — ADD / SUB / MUL / DIV

### Item / ItemStack
- name: String, type: ItemType (RESTORE / ATTACK / DEFENSE / ASSIST)
- effect: int — 效果值
- price: int — 价格
- ItemStack: item + count

### Level
- id: int, name: String
- grade: int — 1~3
- enemies: List<Enemy>
- starThresholds: int[2] — 3星/2星 HP百分比门槛
- isBoss: boolean

### 数据库设计

MySQL 存储玩家动态数据。游戏静态配置（关卡、怪物、道具）仍用 JSON 文件。

**数据库**: `number_quest`

```sql
-- 玩家表（单行记录）
CREATE TABLE player (
    id INT PRIMARY KEY DEFAULT 1,
    name VARCHAR(50),
    hp INT, max_hp INT,
    attack INT, defense INT,
    coins INT,
    unlocked_grade INT DEFAULT 1,
    unlocked_level_id INT DEFAULT 1,
    CHECK (id = 1)
);

-- 玩家道具背包
CREATE TABLE player_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(50),
    count INT
);

-- 关卡星级记录
CREATE TABLE player_stars (
    level_id INT PRIMARY KEY,
    stars INT  -- 1~3
);
```

连接池使用 HikariCP，DAO 层封装 CRUD。启动时从 MySQL 加载玩家存档，关键操作（战斗结束、购买道具、通关）即时写入。

---

## 三、战斗系统

### 回合流程
```
玩家回合 → 4选1答题（3秒限时）
  → 答对: 玩家攻击怪物（基础攻击 + 道具加成）
  → 答错 / 超时: 怪物攻击玩家
  → 道具 / 技能: 可穿插在答题前使用
循环直到一方 HP=0
```

### 题目生成
QuestionGenerator 根据 difficulty 生成题目：
- 1: 10以内加减
- 2: 20以内加减
- 3: 100以内加减
- 4: 乘法口诀
- 5: 三年级混合运算

### 技能系统
每答对3题攒 1 技能点，最多存 3 点：
| 技能 | 消耗 | 效果 |
|------|------|------|
| 暴击 | 1点 | 本次攻击 ×2 |
| 回血 | 1点 | 恢复 20% HP |
| 必对 | 2点 | 本题直接算对 |

### 星级评定
- 3星: 结束 HP ≥ 80% → 大量金币 + 解锁下一关
- 2星: 结束 HP ≥ 50% → 中等金币
- 1星: 通关 → 少量金币
- 0星: HP归零 → 闯关失败

BOSS 关打通额外解锁下一年级。

---

## 四、地图系统

丛林探险主题世界地图，分三个年级区域，每区域 6~8 普通关 + 1 BOSS关。

关卡节点三态：锁定（灰色+锁图标）、已解锁（彩色+光效）、已完成（彩色+星级标记）。

年级标签页切换，高年级需打通前一 BOSS 关解锁。

---

## 五、商店系统

| 道具 | 效果 | 价格 |
|------|------|------|
| 回复药水 | 恢复 30% HP | 50金币 |
| 大回复药水 | 恢复 60% HP | 100金币 |
| 攻击卷轴 | 本场攻击 +5 | 80金币 |
| 护盾石 | 抵消一次伤害 | 120金币 |
| 提示羽毛 | 排除2个错误选项 | 30金币 |

购买流程：商店列表 → 点击 → 确认 → 扣金币 → 入背包。战斗中可带 3 种道具。

---

## 六、项目结构

```
number-quest/
├── src/main/java/com/quest/
│   ├── App.java                 — 入口
│   ├── SceneManager.java        — Scene切换
│   ├── data/
│   │   ├── model/              — Player, Enemy, Level, Item, Question
│   │   ├── dao/                — PlayerDao, ItemDao, StarDao
│   │   └── DatabaseManager.java — HikariCP连接池管理
│   ├── scene/
│   │   ├── TitleScene.java     — 主菜单
│   │   ├── MapScene.java       — 地图选关
│   │   ├── BattleScene.java    — 战斗
│   │   ├── ShopScene.java      — 商店
│   │   └── ResultScene.java    — 结算
│   ├── logic/
│   │   ├── BattleEngine.java   — 战斗回合逻辑
│   │   ├── QuestionGenerator.java — 出题
│   │   ├── StarCalculator.java — 星级评定
│   │   └── ShopService.java    — 购买/背包
│   └── config/
│       ├── LevelData.java
│       ├── EnemyData.java
│       └── ItemData.java
├── src/main/resources/
│   ├── fxml/                   — 5个FXML布局
│   ├── css/style.css
│   ├── images/                 — 素材
│   └── data/
│       ├── levels.json
│       ├── enemies.json
│       └── items.json
├── pom.xml
└── README.md
```

---

## 附录：不纳入首版的功能

以下功能留待后续迭代：
- 音效和背景音乐
- 角色换装/形象定制
- 排行榜
- 成就徽章系统
- 关卡编辑器
