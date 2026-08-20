# hy-springboot-app

빌드와 실행 테스트를 위한 간단한 Spring Boot 웹 애플리케이션입니다.

## 1. 필요한 환경

- Java 17 이상
- Maven 3.8 이상
- 인터넷 연결
  - 첫 빌드 시 Maven이 Spring Boot 의존성을 자동으로 다운로드합니다.

## 2. Java와 Maven 설치

### Ubuntu / Debian

터미널에서 다음 명령을 실행합니다.

```bash
sudo apt-get update
sudo apt-get install -y openjdk-17-jdk maven
```

설치가 완료되었는지 확인합니다.

```bash
java -version
mvn -version
```

Java 버전은 17 이상이어야 합니다. Maven 출력에도 Java 17이 표시되는지 확인하세요.

`sudo` 권한이 없으면 시스템 관리자에게 Java 17 JDK와 Maven 설치를 요청해야 합니다.

### macOS

Homebrew가 설치되어 있다면 다음 명령을 실행합니다.

```bash
brew install openjdk@17 maven
```

설치 후 버전을 확인합니다.

```bash
java -version
mvn -version
```

### Windows

1. Java 17 JDK를 설치합니다. 예: [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=17)
2. Apache Maven을 설치합니다: [Maven 설치 안내](https://maven.apache.org/install.html)
3. Java의 `bin` 디렉터리와 Maven의 `bin` 디렉터리를 `PATH` 환경 변수에 추가합니다.
4. 새 터미널에서 다음 명령으로 확인합니다.

```powershell
java -version
mvn -version
```

## 3. 프로젝트 빌드 및 테스트

프로젝트 디렉터리로 이동합니다.

```bash
cd /home/ubuntu/dev/hy-springboot-app
```

컴파일과 테스트를 실행합니다.

```bash
mvn clean test
```

테스트가 정상적으로 완료되면 `BUILD SUCCESS`가 출력됩니다.

## 4. 애플리케이션 버전 설정

`/` 응답에는 `message`와 함께 애플리케이션 `version`이 반환됩니다. 버전을 설정하지 않으면 기본값은 `dev`입니다.

### 환경 변수로 설정

Linux/macOS에서는 애플리케이션 실행 전에 `APP_VERSION`을 설정합니다.

```bash
export APP_VERSION=1.0.0
mvn spring-boot:run
```

JAR 파일을 실행할 때도 같은 방식으로 설정할 수 있습니다.

```bash
APP_VERSION=1.0.0 java -jar target/hy-springboot-app-0.0.1-SNAPSHOT.jar
```

Windows PowerShell에서는 다음과 같이 설정합니다.

```powershell
$env:APP_VERSION = "1.0.0"
mvn spring-boot:run
```

### application.properties로 설정

`src/main/resources/application.properties` 파일을 만들고 다음 내용을 추가합니다.

```properties
app.version=1.0.0
```

`app.version` 프로퍼티가 설정되어 있으면 `APP_VERSION` 환경 변수보다 우선 사용됩니다.

### `java -jar` 실행 시 직접 주입

JAR 실행 명령 뒤에 Spring Boot 명령줄 프로퍼티를 전달할 수 있습니다.

```bash
java -jar target/hy-springboot-app-0.0.1-SNAPSHOT.jar --app.version=1.0.0
```

또는 JVM 시스템 프로퍼티를 `-jar` 앞에 전달할 수도 있습니다.

```bash
java -Dapp.version=1.0.0 -jar target/hy-springboot-app-0.0.1-SNAPSHOT.jar
```

Windows PowerShell에서도 같은 방식으로 실행할 수 있습니다.

```powershell
java -jar target\hy-springboot-app-0.0.1-SNAPSHOT.jar --app.version=1.0.0
java -Dapp.version=1.0.0 -jar target\hy-springboot-app-0.0.1-SNAPSHOT.jar
```

`--app.version` 또는 `-Dapp.version`을 사용하면 `APP_VERSION` 환경 변수보다 우선 적용됩니다. 아무 값도 전달하지 않으면 버전은 `dev`가 됩니다.

## 5. 애플리케이션 실행

### Maven으로 실행

```bash
mvn spring-boot:run
```

기본적으로 애플리케이션은 `http://localhost:8080`에서 실행됩니다.

### JAR 파일로 실행

먼저 실행 가능한 JAR 파일을 생성합니다.

```bash
mvn clean package
```

그 다음 JAR 파일을 실행합니다.

```bash
java -jar target/hy-springboot-app-0.0.1-SNAPSHOT.jar
```

## 6. 요청 로그 확인

애플리케이션으로 HTTP 요청이 들어오면 요청 메서드와 경로가 콘솔에 출력됩니다.

예를 들어 다음 요청을 보내면:

```bash
curl http://localhost:8080/health
```

애플리케이션을 실행한 터미널에 다음과 비슷한 로그가 표시됩니다.

```text
INFO ... RequestLoggingFilter : Incoming request: GET /health
```

## 7. 엔드포인트 확인

애플리케이션이 실행된 상태에서 다른 터미널을 열고 다음 명령을 실행합니다.

```bash
curl http://localhost:8080/
curl http://localhost:8080/health
curl http://localhost:8080/timecheck
```

예상 응답은 다음과 같습니다.

### `/`

버전을 설정하지 않으면 `version` 값은 `dev`입니다.

```json
{"message":"Hello, Spring Boot!","version":"1.0.0"}
```

### `/health`

```json
{"status":"UP"}
```

### `/timecheck`

현재 실행 시각이 ISO-8601 형식으로 반환됩니다.

```json
{"time":"2026-08-20T11:22:36.512+09:00"}
```

실제 반환 시간은 요청한 시점에 따라 달라집니다.

## 8. 애플리케이션 종료

실행 중인 터미널에서 `Ctrl+C`를 누르면 애플리케이션을 종료할 수 있습니다.
