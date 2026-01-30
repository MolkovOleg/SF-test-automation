# Руководство по использованию Appium Inspector

## 📱 Что такое Appium Inspector?

**Appium Inspector** — это графический инструмент для исследования UI мобильного приложения. Он позволяет:
- Просматривать иерархию элементов в реальном времени
- Находить различные локаторы для каждого элемента
- Тестировать локаторы до написания кода
- Делать скриншоты элементов

## 🔧 Установка Appium Inspector

### Вариант 1: Скачать готовое приложение (Рекомендуется)

1. Перейти на: https://github.com/appium/appium-inspector/releases
2. Скачать последнюю версию для macOS:
   - **`Appium-Inspector-mac-<version>.dmg`** (для Intel)
   - **`Appium-Inspector-mac-arm64-<version>.dmg`** (для Apple Silicon M1/M2/M3)
3. Открыть `.dmg` файл и перенести приложение в Applications

### Вариант 2: Установка через npm

```bash
npm install -g appium-inspector
```

## 📋 Подготовка к работе

### 1. Убедитесь что запущен Appium Server

```bash
# В отдельном терминале
appium

# Должен показать:
# [Appium] Welcome to Appium v2.x.x
# [Appium] Appium REST http interface listener started on 127.0.0.1:4723
```

### 2. Запустите эмулятор

```bash
# Проверьте, что эмулятор запущен
adb devices

# Должен показать:
# List of devices attached
# emulator-5554    device
```

### 3. Убедитесь, что приложение Wikipedia установлено

```bash
adb shell pm list packages | grep wikipedia
# Должен показать: package:org.wikipedia
```

## 🚀 Настройка Appium Inspector для Wikipedia

### Шаг 1: Запустить Appium Inspector

- Откройте приложение **Appium Inspector**
- Выберите вкладку **"Appium Server"** (по умолчанию)

### Шаг 2: Настроить Remote Host

В разделе **"Remote Host"** укажите:
- **Remote Host**: `127.0.0.1`
- **Remote Port**: `4723`
- **Remote Path**: `/` (оставить как есть)

### Шаг 3: Настроить Desired Capabilities

В разделе **"Desired Capabilities"** добавьте следующие capability (нажимая "+"):

| Capability | Value | Type |
|------------|-------|------|
| `platformName` | `Android` | text |
| `appium:deviceName` | `Android_Emulator` | text |
| `appium:automationName` | `UiAutomator2` | text |
| `appium:appPackage` | `org.wikipedia` | text |
| `appium:appActivity` | `org.wikipedia.main.MainActivity` | text |
| `appium:noReset` | `true` | boolean |
| `appium:autoGrantPermissions` | `true` | boolean |

**Примечание**: В Appium 2.x все capabilities кроме `platformName` должны иметь префикс `appium:`

### Шаг 4: Сохранить конфигурацию (опционально)

- Нажмите **"Save As..."**
- Введите имя: `Wikipedia Android Emulator`
- Нажмите **"Save"**

### Шаг 5: Запустить сессию

- Нажмите **"Start Session"**
- Подождите ~10 секунд
- Приложение Wikipedia должно запуститься на эмуляторе
- В Inspector появится UI дерево и скриншот приложения

## 🔍 Как найти локаторы для разных состояний UI

### Состояние 1: Главный экран (Explore)

#### Найти поле поиска:

1. **Метод 1: Через визуальный поиск**
   - Кликните на поле поиска на скриншоте в Inspector
   - В правой панели появятся все доступные локаторы

2. **Метод 2: Через дерево элементов**
   - Разверните дерево элементов слева
   - Найдите элемент с текстом "Search Wikipedia"

3. **Записать локаторы:**
   ```
   - resource-id: org.wikipedia:id/search_container
   - text: Search Wikipedia
   - content-desc: (если есть)
   - xpath: //android.widget.FrameLayout[@resource-id='org.wikipedia:id/search_container']
   ```

#### Найти другие элементы главной страницы:

- **Feed/Explore карточки**: ищите `recycler_view` или похожие ID
- **Кнопки навигации**: обычно внизу экрана

### Состояние 2: Активный поиск (Search Screen)

#### Как перейти в это состояние:

1. В Inspector нажмите кнопку **"Refresh"** (🔄) или **"Tap"**
2. Кликните на поле поиска на скриншоте
3. В Appium Inspector появится новое состояние экрана

#### Найти элементы поиска:

1. **Поле ввода текста:**
   - Кликните на активное поле ввода
   - Записать локаторы:
     ```
     - resource-id: org.wikipedia:id/search_src_text
     - class: android.widget.AutoCompleteTextView
     - xpath: //android.widget.AutoCompleteTextView[@resource-id='org.wikipedia:id/search_src_text']
     ```

2. **Результаты поиска:**
   - Введите текст "Java" в поле поиска в эмуляторе
   - Нажмите **"Refresh Source"** в Inspector
   - Найдите элементы результатов:
     ```
     - resource-id: org.wikipedia:id/page_list_item_title
     - class: android.widget.TextView
     - xpath: //android.widget.TextView[@resource-id='org.wikipedia:id/page_list_item_title']
     ```

3. **Кнопка закрытия поиска:**
   - Ищите элемент с content-desc: "Clear query" или "Navigate up"
   - resource-id: org.wikipedia:id/search_close_btn

### Состояние 3: Страница статьи

#### Как перейти:

1. Из поиска кликните на первый результат
2. Дождитесь загрузки статьи
3. В Inspector нажмите **"Refresh Source"**

#### Найти элементы статьи:

1. **Заголовок статьи:**
   ```
   - class: android.widget.TextView (первый крупный TextView)
   - xpath: //android.view.View[содержит текст]
   ```

2. **Кнопка "Назад":**
   ```
   - content-desc: Navigate up
   - class: android.widget.ImageButton
   - xpath: //android.widget.ImageButton[@content-desc='Navigate up']
   ```

3. **Контент статьи:**
   ```
   - class: android.webkit.WebView
   - id: org.wikipedia:id/page_web_view
   ```

## 📝 Лучшие практики поиска локаторов

### Приоритет локаторов (от лучшего к худшему):

1. **Resource ID** (если уникальный)
   ```java
   @AndroidFindBy(id = "org.wikipedia:id/search_container")
   ```

2. **Accessibility ID (content-desc)**
   ```java
   @AndroidFindBy(accessibility = "Navigate up")
   ```

3. **Text** (если стабильный, не переводится)
   ```java
   @AndroidFindBy(xpath = "//android.widget.TextView[@text='Search Wikipedia']")
   ```

4. **Class + другие атрибуты**
   ```java
   @AndroidFindBy(xpath = "//android.widget.AutoCompleteTextView[@resource-id='org.wikipedia:id/search_src_text']")
   ```

5. **XPath** (использовать как последний вариант)
   ```java
   @AndroidFindBy(xpath = "//android.widget.FrameLayout/android.widget.LinearLayout/...")
   ```

### ⚠️ Чего избегать:

❌ Избегать абсолютных XPath:
```java
// ПЛОХО
@AndroidFindBy(xpath = "/hierarchy/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/...")
```

❌ Избегать индексов без необходимости:
```java
// ПЛОХО (если есть resource-id)
@AndroidFindBy(xpath = "(//android.widget.TextView)[5]")
```

✅ Использовать относительные локаторы:
```java
// ХОРОШО
@AndroidFindBy(xpath = "//android.widget.TextView[@resource-id='org.wikipedia:id/page_list_item_title']")
```

## 🛠️ Тестирование локаторов в Inspector

### Использование "Search for element"

1. В правой панели Inspector найдите раздел **"Search for element"**
2. Выберите стратегию локатора (id, xpath, accessibility id и т.д.)
3. Введите свой локатор
4. Нажмите **"Search"**
5. Inspector подсветит найденные элементы

#### Примеры для тестирования:

**Поиск по ID:**
```
Selector: id
Locator: org.wikipedia:id/search_container
```

**Поиск по XPath:**
```
Selector: xpath
Locator: //android.widget.TextView[@resource-id='org.wikipedia:id/page_list_item_title']
```

**Поиск по Accessibility ID:**
```
Selector: accessibility id
Locator: Navigate up
```

## 📸 Создание скриншотов для документации

1. Найдите нужный элемент в Inspector
2. Кликните правой кнопкой на элементе в дереве
3. Выберите **"Screenshot Element"** или **"Copy Attributes"**

## 🔄 Обновление исходного кода после исследования

После исследования локаторов в Inspector, обновите ваши Page Objects:

### Пример обновления WikipediaSearchPage:

```java
// Надежные локаторы, найденные через Inspector

// Главный экран - поле поиска
@AndroidFindBy(id = "org.wikipedia:id/search_container")
private WebElement searchContainer;

// Альтернативный поиск по тексту
@AndroidFindBy(xpath = "//android.widget.TextView[@text='Search Wikipedia']")
private WebElement searchPlaceholder;

// Активный поиск - поле ввода
@AndroidFindBy(id = "org.wikipedia:id/search_src_text")
private WebElement searchInput;

// Результаты поиска - список
@AndroidFindBy(id = "org.wikipedia:id/page_list_item_title")
private List<WebElement> searchResults;

// Кнопка очистки/закрытия
@AndroidFindBy(accessibility = "Clear query")
private WebElement clearButton;
```

## 🐛 Troubleshooting

### Inspector не подключается:

1. Проверьте, что Appium Server запущен:
   ```bash
   curl http://127.0.0.1:4723/status
   ```

2. Проверьте capabilities - они должны соответствовать вашему эмулятору

3. Проверьте логи Appium Server в терминале

### Не видно элементов в дереве:

1. Нажмите **"Refresh Source"** (⟳)
2. Убедитесь что приложение открыто на эмуляторе
3. Попробуйте снова запустить сессию

### Эмулятор не реагирует:

1. Взаимодействуйте напрямую с эмулятором
2. После изменений нажимайте **"Refresh Source"** в Inspector

## 📚 Дополнительные ресурсы

- [Appium Inspector GitHub](https://github.com/appium/appium-inspector)
- [Appium Documentation](https://appium.io/docs/en/latest/)
- [Android UI Selector](https://developer.android.com/reference/androidx/test/uiautomator/UiSelector)

## ✅ Checklist: Поиск локаторов для Wikipedia

- [ ] Установить и настроить Appium Inspector
- [ ] Запустить сессию с Wikipedia
- [ ] Найти локаторы для главного экрана (Explore):
  - [ ] Поле поиска
  - [ ] Feed/карточки
  - [ ] Навигация
- [ ] Найти локаторы для экрана поиска:
  - [ ] Поле ввода
  - [ ] Список результатов
  - [ ] Кнопка очистки
- [ ] Найти локаторы для страницы статьи:
  - [ ] Заголовок
  - [ ] Контент
  - [ ] Кнопка "Назад"
- [ ] Протестировать каждый локатор через "Search for element"
- [ ] Обновить Page Objects с новыми локаторами
- [ ] Запустить тесты для проверки

---

**Следующий шаг**: После исследования локаторов обновите файлы Page Objects с более надежными селекторами! 🚀
