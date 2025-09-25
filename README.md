# KMP Weather & Todo App
Kotlin Multiplatform(KMP)을 사용하여 날씨 정보 조회 기능과 할 일(Todo) 관리 기능을 제공하는 애플리케이션. 공통 코드 베이스(Shared Logic)를 통해 개발 효율성과 코드 재사용성을 극대화하고, Android/iOS 양쪽 플랫폼에 일관된 비즈니스 로직을 제공한다.
</br></br></br>

## 개요
- **공유 로직**: 비즈니스 로직(ViewModel, Repository, API/DB)은 공통 코드로 작성
- **플랫폼별 UI**:
    - **Android**: Jetpack Compose
    - **iOS**: SwiftUI

## 주요 기능
- **날씨 정보 조회**
    - 도시 이름으로 날씨 검색
    - 현재 날씨, 시간별/일별 예보 제공
- **할 일 관리**
    - 할 일 추가, 수정, 삭제, 검색
    - 카테고리별 할 일 관리

## 적용 기술 및 아키텍처
- **Kotlin Multiplatform (KMP)**: `commonMain` 소스셋에 공유 비즈니스 로직 구현
    - **ViewModel**: UI 상태 관리
        - **Android**: `koinViewModel()` 를 통해 공유 모듈의 ViewModel 주입
        - **iOS**: AndroidX ViewModel을 직접 사용할 수 없어 iOS용 Wrapper ViewModel 클래스 구현
    - **Data Layer**: API 통신(`Ktor`), 로컬 데이터베이스(`Room`) 처리
    - **DI Modules**: 의존성 주입(`Koin`)
- **MVI (Model-View-Intent)**: `orbit-mvi` 라이브러리를 사용해 UI 상태를 단방향으로 관리
- **의존성 주입**: `Koin` 을 사용해 API, DB, Repository, ViewModel 등 역할별 모듈을 분리, 결합도 낮춤

## 프로젝트 구조
```
.
├── composeApp/  # 안드로이드 앱 (Jetpack Compose)
├── iosApp/      # iOS 앱 (SwiftUI)
└── shared/      # 공유 모듈
    └── src/
        ├── androidMain/ # 안드로이드 전용 코드
        ├── iosMain/     # iOS 전용 코드
        └── commonMain/  # 공통 코드
```

## 데이터 흐름도
- **날씨 정보 조회**
```mermaid
%%{init: {'theme': 'neutral' } }%%
graph LR
    subgraph Platform UI
        A["<b>UI</b> (Compose/SwiftUI)"]
    end

    subgraph KMP Shared Logic
        B["<b>Shared ViewModel</b></br>(WeatherViewModel)"]
        C["<b>WeatherRepository</b></br>(Interface & Impl)"]
        D["<b>WeatherApi (Ktor)</b></br>(Interface & Impl)"]
    end

    subgraph External
        E["<b>OpenWeatherMap API</b>"]
    end

    %% --- Request Flow ---
    A -- "<b>1. Intent</b></br>데이터(도시 경위도, 날씨) 요청" --> B
    B -- "<b>2. 데이터 요청</b></br>getCoordinates()</br>getCurWeather()</br>getThreeHoursForecast()" --> C
    C -- "<b>3. API 함수 호출</b></br>getCoordinates()</br>getCurWeather()</br>getThreeHoursForecast()" --> D
    D -- "<b>4. HTTP GET 요청" --> E

    %% --- Response Flow ---
    E -- "<b>5. JSON 응답" --> D
    D -- "<b>6. 데이터 파싱</b></br>Resource 객체로 변환" --> C
    C -- "<b>7. Flow 스트림 반환" --> B
    B -- "<b>8. 상태 업데이트" --> A

```

- **할 일 관리**
```mermaid
%%{init: {'theme': 'neutral' } }%%
graph LR
    subgraph Platform UI
        A["<b>UI</b> (Compose/SwiftUI)"] 
    end

    subgraph KMP Shared Logic 
        B["<b>Shared ViewModel</b></br>(Todo/CategoryViewModel)"]
        C["<b>Repository</b></br>(Todo/Category Interface & Impl)"]
        D["<b>Dao</b></br>(Room)"]
    end

    subgraph Local Storage
        E["<b>DB</b>"]
    end

    %% --- Request Flow ---
    A -- "<b>1. Intent</b></br>데이터(할 일, 카테고리)</br>추가/수정/삭제" --> B
    B -- "<b>2. DB 작업 요청</b></b></br>insertTodo()</br>..." --> C
    C -- "<b>3. 쿼리 함수 호출</b></br>insert()</br>..." --> D
    D -- "<b>4. 쿼리 실행</b>" --> E

    %% --- Response Flow ---
    E -- "<b>5. 데이터 반환</b>" --> D
    D -- "<b>6. 데이터 모델 반환</b>" --> C
    C -- "<b>7. 목록 조회 및 반환</b></br>List&lt;TodoWithCategory&gt;" --> B
    B -- "<b>8. 상태 업데이트</b>" --> A

```
