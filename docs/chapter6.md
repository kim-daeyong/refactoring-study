# 메소드 정리

### 메소드 추출

* 메소드가 길거나, 주석을 달아야 코드 해석이 가능할때 사용
    - 직관적인 메소드 이름과 함께 잘게 쪼게 분리한다.
    - 분리에서 중요한건 메서드 명과 메소드 내용이 중요

* 방법
    - 목적에 부합하는 이름의 새 메서드를 생성 (원리가 아니라 기능을 나타내는 이름)
    - 기존 메서드에서 빼낸 코드를 새로 생성한 메서드로 복사
    - 빼낸 코드에서 기존 메서드의 모든 지역변수 참조를 찾아 새 메서드의 지역변수나 매개변수로 수정
    - 빼낸 코드 안에서만 사용하는 임시 변수가 있다면 새로 선언
    - 추출 코드에 의해 변경되는 지역변수가 있는지 파악
    - 빼낸 코드에서 읽어들인 지역변수를 대상 메서드에 배개변수로 전달

    ```java
        void methodA(int a) {
            otherMethod();

            // 세부 정보 출력
            System.out.println("name: " + _name);
            System.out.println("amount: " + amount);
        }

        // 변경

        void methodA(int a) {
            otherMethod();
            printDetail(a);
        }

        void printDetail(int a) {
            System.out.println("name: " + _name);
            System.out.println("amount: " + a);
        }
    ```

### 메소드 내용 직접 삽입

* 메소드 명으로 그 로직이 설명될 간단한 기능이라면 메소드를 없앤다.

* 방법
    - 메서드가 재정의되어 있지 않은지 확인
    - 메서드 호출 하는 부분을 모두 찾자
    - 각 호출 부분을 메서드 내용으로 교체
    - 테스트
    - 메서드 정의 삭제
    - 복잡한 상황에선 이 기법을 자제한다.

    ```java
    int getRating() {
        return (moreThanFiveLateDelivers()) ? 2 : 1;
    }

    boolean moreThanFiveLateDelivers() {
        return _numberOfLateDelivers > 5;
    }

    //변경

    int getRating() {
        return (_numberOfLateDelivers > 5) ? 2 : 1;

    ```

### 임시변수 내용 직접 삽입

* 간단한 수식을 받는 임시변수로 인해 리팩토링이 힘들 시 그 임시변수를 참조하는 부분을 수식으로 변경 

* 방법
    - 대입문의 우변에 문제가 없는지 확인하고 문제가 없다면 임시변수를 final로 선언하고 컴파일
    - 임시변수를 참조하는 부분을 찾아서 대입문 우변의 수식으로 바꾼다.
    - 컴파일, 테스트
    - 임시변수 선언과 대입문을 삭제
    - 컴파일, 테스트
    ```java
        double basePrice = anOrder.basePrice();
        return (basePrice > 1000)
        
        ///변경

        return (anOrder.basePrice() > 1000)
    ```

### 임시변수를 메서드로 변환

* 임시변수를 메소드로 추출, 임시변수 호출부분을 메소드호출로 변환

* 방법
    - 값이 한 번만 대입되는 임시변수를 찾는다.
        - 값이 여러 번 대입되는 임시변수가 있으면 임시변수 분리 기법을 고려
    - 그 임시변수를 final로 선언
    - 컴파일
    - 대입문 우변을 빼내어 메서드로 만든다.
        - 처음엔 메서드를 private으로 선언, 다른 곳에서 호출된다면 접근 제한을 완화
        - 추출 메서드에 문제가 없는지, 상태가 변경되는지 확인. 만약 객체 변경 등의 상태가 변경된다면 상태변경 메서드와 값 반환 메서드를 분리기법 실시
    - 컴파일, 테스트
    - 임시변수를 대상으로 임시변수 내용 직접 삽입 기법을 실시

    ```java
        double basePrice = _quantity * _itemPrice;
        if (basePrice > 1000) {
            return basePrice * 0.95;
        } else {
            return basePrice * 0.95;
        }
        
        //변경

        if (basePrice() > 1000) {
            return basePrice() * 0.95;
        } else {
            return basePrice() * 0.95;
        }

        private double basePrice() {
            return _quantity * _itemPrice
        }
    ```

* 참고
    - 임시변수들 추출 전 final을 이용하여 정말 한번만 사용되는지 확인해보자

### 직관적인 임시변수 사용

* 사용된 수식이 복잡할땐 그 수식을 결과나 용도에 부합하는 직관적인 임시변수에 대입한다.

* 방법
    - 임시변수를 final로 선언하고, 복잡한 수식에서 한 부분의 결과를 임시변수에 대입
    - 수식에서 한 부분의 결과를 임시변수 값으로 교체
    - 컴파일, 테스트
    - 다른 부분을 대상으로 반복


    ```java
        if ((platform.toUpperCase().indexOf("MAC") > -1) &&
                browser.toUpperCase().indexOf("IE") > -1) &&
                wasInitialized() && resize > 0 ) {

        }

        //변경

        final boolean isMacOs = platform.toUpperCase().indexOf("MAC") > -1;
        final boolean isIEBrowser = browser.toUpperCase().indexOf("IE") > -1;
        final boolean wasResized = resize > 0;

        if (isMacOs && 
                isIEBrowser && 
                wasInitialized() &&
                wasResized) {

        }
    ```

### 임시변수 분리

* 루프변수나 누적용 임시변수가 아닌 임시변수에 여러 번 값이 대입될 땐 각 대입마다 다른 임시변수를 사용
    - 값이 두 번 이상 대입되는건 그 변수가 메서드 안에서 여러번 사용된다는 것을 의미한다.
    - 여러 용도로 사용되는 변수는 각 용도별로 다른 변수로 분리한다.

* 방법 
    - 선언문과 첫번째 대입문에 있는 임시변수 이름을 변경
    - 이름을 바꾼 새 임시변수를 final로 선언
    - 그 임시변수의 참조 부분을 두번째 대입문으로 수정
    - 컴파일, 테스트
    - 각 대입문마다 차례로 선언문에서 임시변수 이름을 변경, 대입문까지 참조를 수정하며 위 과정 반복

    ```java
        double temp = 2 * (height + width);
        System.out.println(temp);
        temp = height * width;
        System.out.println(temp);
        
        //변경

        final double perimeter = 2 * (height + width);
        System.out.println(perimeter);
        final doublie area = height * width;
        System.out.println(area);
    ```

### 매개변수로의 값 대입 제거

* 매개변수로 값을 대입하는 코드가 있을땐 매개변수 대신 임시변수를 사용
    - 매개변수로의 값 대입이란 매개변수로 전달받은 foo의 값을 다른 객체 참조로 변경
        ```
            void method(Object foo) {
                foo.method1();
                foo = anotherObject; // 하면 안됨
            }
        ```

* 방법
    - 매개변수 대신 사용할 임시변수를 선언
    - 매개변수로 값을 대입하는 코드 뒤에 나오는 매개변수 참조를 전부 임시변수로 수정
    - 매개변수로의 값 대입을 임시변수로의 값 대입으로 수정
    - 컴파일, 테스트

    ```java
        int discount (int inputVal, int quantity, int yearToDate) {

            if (inputVal > 50) inputVal -= 2;
        }
        
        // 변경

        int discount (int inputVal, int quantity, int yearToDate) {
            int result = inputVal;

            if (inputVal > 50) result -= 2;
        }
    ```

* 자바에서의 값을 통한 전달
    - 자바는 값을 통한 전달 사용

### 메서드를 메서드 객체로 전환

* 지역변수 때문에 메서드 추출이 힘들다면 그 메서드 자체를 객체로 전환해서 모든 지역변수를 객체의 필드로 만들고 메서드를 쪼갠다.

* 방법

    - 전환할 메서드의 이름과 같은 새 클래스를 생성
    - 그 클래스에 원본 메서드가 들어있던 객체를 나타내는 final 필드 추가, 각 임시변수와 매개변수에 해당하는 속성 추가
    - 새 클래스에 원본 객체와 각 매개변수를 받는 생성자 메서드 작성
    - 새 클래스에 compute라는 이름의 메서드 작성
    - 원본 메서드 내용을 compute 메서드 안에 복사, 원본 객체에 있는 메서드를 호출할땐 원본 객체를 나타내는 필드 사용
    - 컴파일
    - 원본 메서드를 개 객체 생성과, compute 메서드 호출을 담당하는 메서드로 변경

    ```java
        class Order...
            double price() {
                double primaryBasePrice;
                double secondaryBasePrice;
                double tertiaryBasePrice;

                // 계산 코드 ;

                ...
            }

        // 변경

        class Order ...

            double price() {
                return new PriceCalculator(this).compute()
            }
        

        class PriceCalculator...

            final double primaryBasePrice;
            final double secondaryBasePrice;
            final double tertiaryBasePrice;

            double compute() {
                // 계산
            }
        
    ```

### 알고리즘 전환

* 알고리즘을 더 분명한 것으로 교체해야 할땐 해당 메서드의 내용을 새 알고리즘으로 변경

* 방법
    - 교체할 간결한 알고리즘 준비, 컴파일
    - 새 알고리즘을 실행하면서 여러 번의 테스트
    - 각 테스트 결과가 다르다면 기존 알고리즘으로 테스트와 디버깅을 실시
        - 기존, 새 알고리즘을 대상으로 각 테스트 케이스를 실행하고 두 결과를 비교 후 문제 수정

    ```java
        String foundPerson(String[] people) {
            for (int i = 0; i < people.length; i++>) {
                if (people[i].equals ("Don")) {
                    return "Don";
                }

                if (people[i].equals ("John")) {
                    return "John";
                }

                if (people[i].equals ("Kent")) {
                    return "Kent";
                }
            }
            return "";
        }

        //변경

        List candidates = Arrays.asList(new String[] {"Don", "John", "Kent"})
        for (int i = 0; i < people.length; i++>) {
            if (candidates.contains(people[i])) 
                return people[i];
        }
        return "";

    ```