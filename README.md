# tank-software-design

Запуск: `./gradlew run --args=file` или `./gradlew run --args=random`.
Проверки: `./gradlew test`. На Windows вместо `./gradlew` — `.\gradlew.bat`.
Управление: стрелки/WASD — движение, пробел — выстрел, L — полоски здоровья.

## Состояния танка

| Остаток от начального здоровья | Состояние | Скорость | Стрельба |
| --- | --- | --- | --- |
| Больше 70% | Лёгкий | Обычная | Разрешена |
| От 15% до 70% включительно | Средний | Вдвое ниже | Разрешена |
| Меньше 15% | Тяжёлый | Втрое ниже | Запрещена |

`Tank` делегирует обновление движения и стрельбу своему `TankState`.
`HealthTankState` хранит три неизменяемых объекта состояния в enum и выбирает
переход после урона. Скорость меняется и посреди движения, без сброса прогресса.
Пороги вычисляются относительно `maxHealth`, поэтому работают и при старте с 80 жизнями.
При нулевом начальном здоровье выбирается тяжёлое состояние. Как в шестой домашке,
танк удаляется при отрицательном здоровье.

Контракт `TankState` открыт для новых реализаций: будущий бустер можно реализовать
отдельным классом в `model` и включить через `Tank.changeState`, не меняя код
делегирования в танке. Очередной урон снова выбирает состояние по здоровью;
длительность и приоритет будущих бустеров пока не заданы.
Это применение [шаблона «Состояние»](https://refactoring.guru/ru/design-patterns/state).

## Слои и границы

Ниже — разбор текущего кода по ответственности, а не по именам пакетов.
В частности, `TextLevelSource` находится в `model`, но выполняет роль адаптера.

| Слой | Классы и интерфейсы | Ответственность |
| --- | --- | --- |
| Domain | `Tank`, `Bullet`, `Tree`, `GameObject`, `Obstacle` | Объекты игры, урон и столкновения |
| Domain | `GameField`, `GameWorld`, `CellAvailability`, `Direction` | Логический мир, занятость клеток, направления |
| Domain | `Level`, `RandomTankSpawner` | Данные уровня и выбор свободных стартовых клеток |
| Domain | `TankState`, `HealthTankState`, `GameFieldListener` | Поведение танка и контракт событий мира |
| Application | `GameLoop`, `Action`, `MoveAction`, `ShootAction`, `ControlStrategy`, `RandomActionStrategy`, `LevelSource` | Цикл команд, игровые действия, AI, контракты источников команд и уровня |
| Infrastructure / adapters | `KeyboardHandler`, `TextLevelSource`, `RandomLevelSource` | Клавиатура и конкретные способы получения уровня |
| Infrastructure / presentation | `GameRenderer`, `EntityRenderer`, `TankRenderer`, `TreeRenderer`, `BulletRenderer`, `HealthBarRenderer`, `HealthBarVisibility`, `TileMovement`, `GdxGameUtils` | Отрисовка, её настройки и вспомогательный код libGDX |
| Infrastructure / composition | `TankGame`, `GameDesktopLauncher` | Запуск libGDX, сборка зависимостей и связь с ресурсами |

Порты и адаптеры отмечены Javadoc в исходниках.

| Порт | Кто использует и реализует |
| --- | --- |
| `Action` — входной контракт команды | `MoveAction` и `ShootAction` выполняют сценарии. Клавиатура и AI передают команды в `GameLoop`; команда L вызывает переключение состояния представления. |
| `ControlStrategy` — порт источника команд | `GameLoop` опрашивает его на первом шаге цикла. `KeyboardHandler` адаптирует внешний ввод, `RandomActionStrategy` реализует внутреннюю политику AI. |
| `LevelSource` — выходной порт получения уровня | Сейчас вызывается при сборке в `TankGame`. `TextLevelSource` и `RandomLevelSource` — сменные адаптеры. |
| `GameFieldListener` — выходной порт событий | `GameField` сообщает о добавлении/удалении объектов. `GameRenderer` адаптирует эти события для графики, включая начальное заполнение уровня. |

`GameObject`, `Obstacle`, `GameWorld`, `CellAvailability` и `TankState` — внутренние
контракты предметной области. Наличие интерфейса само по себе не делает его
архитектурным портом. `EntityRenderer` — контракт внутри графического адаптера:
он использует `Batch` и `TileMovement`, поэтому его нельзя переносить в ядро.
Чтение файла и клавиатуры уже передаются адаптерам через `Supplier<String>` и
`IntPredicate`; привязки к `Gdx.files` и `Gdx.input` находятся в точках сборки.

## Что ещё отделить для Ports & Adapters

В [Layered Architecture](https://www.oreilly.com/library/view/software-architecture-patterns/9781491971437/ch01.html)
код группируется по обязанностям слоёв. Для Ports & Adapters также важно, чтобы
зависимости деталей направлялись к ядру, как описано у
[Марка Симана](https://blog.ploeh.dk/2013/12/03/layers-onions-ports-adapters-its-all-the-same/)
и в [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).
В текущем проекте остаются следующие границы для дальнейшего выделения:

- `TankGame` совмещает сборку, создание игровой сессии и callback-и libGDX.
  Сценарий создания мира можно вынести в application-сервис с `LevelSource`,
  а запуск/обновление сессии оформить входным портом. `TankGame` останется
  адаптером жизненного цикла, сборка зависимостей — во внешнем слое.
- В domain используются `GridPoint2` и `MathUtils` из libGDX. Для независимости
  от фреймворка нужны собственный тип координат и локальные числовые операции;
  преобразование в графические координаты останется в адаптере.
- Пакеты смешивают слои: адаптеры загрузки лежат в `model`, а AI — в `input`.
  Их можно разнести по `domain`, `application`, `infrastructure`, сохранив
  интерфейсы портов в ядре.

Отдельный выходной порт отрисовки сейчас не требуется `GameLoop`: графику вызывает
внешний `TankGame`, а логика сообщает изменения через `GameFieldListener`.
Если управление отрисовкой перейдёт в application, ему понадобится контракт
представления без типов libGDX. Эти изменения границ здесь описаны как следующие
шаги; восьмая домашка не требует полного переноса проекта на новую архитектуру.
