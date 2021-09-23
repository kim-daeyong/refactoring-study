package chapter3;

public class Chapter3 {
    public static void main(String[] args) {

    }

    //중복코드
    public void methodA() {
        duplicateMethod();
    }
    
    public void methodB() {
        duplicateMethod();
    }
    
    public void duplicateMethod() {
        // 중복 로직 구현
    }

    //장황한 메서드
    public void method(int a, int b, int c, int d) {
        
    }

    class ParameterObject {
        int a;
        int b;
        int c;
        int d;
    }
    
    public void changeMethod(ParameterObject parameterObject) {
        
    }
    /*
    int totalResult = 0;

    // 임시변수 사용
    for (CustomObject customObj : customObjects) {
            int tempResult = customObj.getResult();
            if (customObj.getCode() == CustomCode.Event) {
            totalResult += (tempResult * 0.7);
        } else {
            totalResult += (tempResult * 1.2);
        }
    }

    // 임시변수 사용하지 않음
    for (CustomObject customObj : customObjects) {
            if (customObj.getCode() == CustomCode.Event) {
            totalResult += (customObj.getResult() * 0.7);
        } else {
            totalResult += (customObj.getResult() * 1.2);
        }
    }
    */

    //방대한 클라스

    //과다한 매개변수

    //수정의 산발

    //기능의 산재

    //잘못된 소속

    //데이터 뭉치

    //강박적 기본 타입 사용


    //switch 문
    // https://www.slipp.net/questions/566 참조
    
    //평행 상속 계측

    //직무유기 클라스

    //막연한 범용 코드

    //임시 필드

    //메세지 체인

    //과잉 중개 메서드

    //지나친 관여

    //인터페이스가 다른 대용 클라스

    //미흡한 라이브러리 클라스

    //데이터 클라스

    //방치된 상속물

    //불필요한 주석
}
