package skhu.gdsc.securitypractice.domain;

import javax.swing.Spring;

public enum Authority {
    ROLE_USER, ROLE_ADMIN
    /*
    * Authorization(권한) 설정
    * Spring security 에서 제공하는 ROLE
    *
    * ROLE_USER: 회원 권한
    * ROLE_ADMIN: 관리자 권환
    * */
}

/*
* Enum 열거 타입
* Enumeration은 "열거, 목록, 일람표"
* 요소,멤버라 불리는 명명된 값의 집합을 이루는 자료형 = 상수 데이터들의 집합
* 정해져 있는 한정된 데이터 묶음 ex) 요일, 계절, 주사위 숫자
* 상수를 단순히 정수로 치부하지말고 객체 지향적으로 객체화해서 관리하자는 취지
* 자바에서는 독립된 특수한 클래스
* 참고 자료: https://inpa.tistory.com/entry/JAVA-☕-열거형Enum-타입-문법-활용-정리 [Inpa Dev 👨‍💻:티스토리]
* */