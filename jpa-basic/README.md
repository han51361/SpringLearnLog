 # ORM 표준 JPA 프로그래밍 

- entityManager 는 쓰레드간에 공유하지 않는다. 
- #### `JPA 의 모든 데이터 변경,생성은 트랜잭션 안에서 실행되어야한다. `

- command + option + v  = 변수 자동 생성 

- ### EntityManager
    -   데이터 수정시 : persist를 안해도 바뀌어서 저장된다.
    -   JPA를 통해서 데이터를 가져온다면 Commit 시점에서 다 관리를 해준다.
    -   Commit 시점 전에 데이터의 변경이 있다면 JPA 가 Update Query 를 쏜다.
    -   그 이후 커밋
    
- ### JPQL
    - JPA 를 사용하면 `엔티티 객체를 대상`으로 개발(SQL = DB table을 대상으로)
    - 문제 : 검색 쿼리
    - 검색할 때 테이블이 아닌, 엔티티 객체를 대상으로 검색
    - 모든 DB 데이터를 객체로 변환하여 검색하는 것 = 불가능
    - 결국 : 검색 조건이 포함된 SQL 이 필요
    
- ## JPA 에서 중요한 2가지
    - ### 객체와 관계형 데이터 베이스 매핑하기
    - ### 영속성 컨텍스트 
    
    
- ## 영속성 컨텍스트
    - 엔티티를 영구 저장하는 환경
    - EntityManager.persist(entity); 
        - 엔티티 매니저를 통해서 영속성 컨텍스트에 접근 
        - EntityManager (N) : 1 PersistenceContext (스프링 프레임워크)
        
    - 엔티티의 생명주기
        - 비영속(new/transient)
            - 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
            
        - 영속(managed)
            - 영속성 컨텍스트에 관리되는 상태
            - EntityManager에 persist 하는 것 
            - persist 햿다고 DB 에 저장되진 않는다. 
            
        - detached(준영속)
            - 영속성 컨텍스트에 저장되었다가 분리
        - remove
            - 삭제
    - 엔티티와 1차캐시
        - entityManger 는 데이터를 찾을 때, 1차 캐시를 먼저 조회 후, 
         데이터가 없다면, DB 조회하고 1차 캐시에 저장한다. 
            - (1 트랜잭션 안에서만 캐시 유지 )
    - 엔티티 등록
        - 트랜잭션을 지원하는 쓰기 지연
        - persist를 할 때, insert 쿼리문을 보내는 것이 아니라 insert문을 생성 후 , 보관해놓는다. 
        - commit(); 하는 순간 DB에 insert SQL을 보낸다. 
        
    - Dirty Checking(변경 감지)
         - #### `flush() : 영속성 컨텍스트의 변경내용을 DB에 반영` 
            -   변경 감지 
             
                수정된 엔티티 쓰기 지연 SQL저장소 등록 
              
                쓰기 지연 SQL 저장소 쿼리를 DB에 전송 
                              
    - 플러시 : 영속성 컨텍스트의 변경내용을 DB에 반영
        - 영속성 컨텍스트를 플러시해도 1차캐시가 변하지는 않는다. 
        
        
   - 영속성 컨텍스트를 플러시하는 방법
        - em.flush()
        - 트랜잭션 커밋
        - JPQL 쿼리 실행 
        
        
   - 준영속 상태
        - em.detach();
        - 영속 상태의 엔티티가 영속성 컨텍스트에서 분리(detached)
    
    
   - ### 객체와 테이블 매핑 
    - @Entity  
        - 기본 생성자 필수 (파라미터 없는 Public / protected 생성자)
        - final , enum, interface, inner class X
        - 저장할 필드에 final 사용 x
        
   - DB 스키마 자동 생성 - 속성(pom.xml - hibernate.hbm2ddl.auto)
        - create :   기존 테이블 삭제 후 다시 생성(Drop + create )
        - create-drop :  create 와 같으나 종료시점에 table drop
        - update : 변경분만 반영(운영 DB에 사용하면 안된다. )
        - validate : 엔티티와 테이블이 정상 매핑되었는지만 확인
        - none : 사용하지 않음 
   
   - DB 스키마 자동생성 - 주의점
        - `운영장비에는 절대 create, create-drop, update 사용하면 안된다. ` 
            - 잘못하면 데이터 다 날라간다. (* alter 와 drop 기능을 분리하는 것이 맞다.)
        - 개발 초기 : create , update
        - test 서버 : update / validate
        - 스테이징과 운영 서버 : validate/ none
        
   - DDL 생성 기능은 DDL을 자동생성할 때만 사용되고, JPA 실행 로직에는 영향을 주지 않는다. 
   
   - #### @Column
        - name : 테이블 네임 
        - insertable / updatable : 등록/변경가능 여부
        - nullable : null 값 허용 여부 (false 설정시, ddl 생성할 때 notnull 제약)
        - unique : @Table 의 uniqueConstraints와 같지만 한 칼럼에 제약할 때
        - columnDefinition : DB 칼럼정보를 직접 준다. 
        - length : 문자길이 제약조건 / String 타입에만
        - precision, scale : BigDecimal 타입에 사용
            - precision : 소수점을 포함한 전체 자릿수
            - scale : 소수의 자릿수 
   - #### @Enumerated 
        - ##### `ordinal 사용 X`
        
   - 기본 키 매핑 어노테이션
     - @Id 
        - 직접 할당이면, @Id 만 사용
        
     - @GeneratedValue용(자동생성)
        - IDENTITY : DB에 위임
        - SEQUENCE : DB 시퀀스 오브젝트 사용 (@SequenceGenerator 필요)
        - TABLE : 키 생성용 테이블 사용 (@TableGenerator 필요)
        - AUTO : 방언에 따라 자동 지정 / 기본 값 - 위에 3개 중 하나를 알아서 설정해준다. 
   
   - TABLE 전략
        : 키생성 전용 테이블을 하나 만들어 DB 시퀀스 흉내내는 전략
        - 장점 : 모든 테이블에 적용 가능
        - 단점 : 성능 
        
   - 권장하는 식별자 전략 
        - 기존 키 제약 조건 : not null, 유일, 변하면 X
        - 미래까지 이 조건을 만족하는 자연키는 찾기 어렵다. 대리키(대체키)를 사용하자. 
        - ##### `권장 : Long 형 + 대체키 + 키 생성전략 사용 `
        
   - IDENTITY 전략
        - 기본 키 생성이 DB에 들어가져야 PK 값이 지정된다. 
        - `JPA 가 1차 캐시에 들어가야 PK 를 찾는데, identity는 그렇지 못하니, 예외적으로 identity는 persist 를 호출하는 시점에, db에 insert 쿼리를 날린다. `
        - 따라서 한번에 모아서 insert 하는 것이, identity 전략에서는 불가능하다. 

  --------------------------
  
  - ## 연관관계 매핑 
  
  
   -  객체간의 관계는 단방향이 더 좋다.
   
   - mappedBy : 객체와 테이블간에 연관관계를 맺는 차이를 이해해야 한다. 
   
   객체 간 : 객체간 연관관계가 단방향이 2개이다. -> 이걸 억지로 양방향이라 하는 거다.
   
    
   테이블 간 : 테이블 간 연관관계는 FK로 설정된 칼럼을 통한 연관관계 1개!
   
   - 연관관계의 주인 은 : DB입장에서 Foreign 키를 관리할 객체를 정해야한다!  -> 이것이 주인의 개념
   - 연관관계의 주인만이 외래 키를 관리(등록, 수정)
   - 주인이 아닌 쪽은 읽기만 가능
   - 주인은 MAppedBy 속성 사용 X
   - mappedby 는 조회만 가능 (등록 수정 불가)
   
   
   >  ### 외래 키가 있는 곳을 주인으로 정하라! 
> 외래키가 있는 곳이 -> many 



 - 양방향 연관관계 주의점
    - 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정해줘야한다. 
    - 연관관계 편의 메소드를 생성하자. -> Member 의 changeTeam 메소드 확인 ! / Team의 addMember
    - 양방향 매핑시 무한루프 조심 
        -   ex) toString() / lombok /JSON 
    - 단방향 매핑만으로도 이미 연관관계 매핑 완료 : 양방향 매핑은 최대한 자제하라
    - JPQL에서 역방향으로 탐색할 일이 많다. 
    - 단방향 매핑을 잘하고 양방향은 필요할 때 추가해도 됨
    
    
  
  - ## Controller 단에서는 entity를 절대 반환하지 말라
  
  - 단방향 / 양방향
  
    * 테이블
        - 외래 키 하나로 양쪽 조인 가능
        - 방향이라는 개념이 없음
        
    * 객체
        - 참조용 필드가 있는 쪽으로만 참조 가능
        - 한쪽만 참조하는 단방향
        - 양쪽이 서로 참조하면 양방향 
        
        
  - 다대일 양방향 vs 다대일 단방향 
  
  - ##다대일 양방향 
    - 외래 키가 있는 쪽 : 연관관계 주인
    - 양쪽을 서로 참조하도록 개발 
    
  
  
  - #### 일대다 단방향 (실무에선 거의 안씀)
    - 일쪽에 외래키를 할 순 없음 무조건 다쪽에서 
    - member에서 외래키를 가지고 있기 때문에
    - one to many 는 연관관계에서 변경할 떄, update 쿼리가 많이 나가야한다. (성능상 단점) 
   
    - ex) team.getMembers().add(member); -> team만 만졋는데 엮여있는 member 에 update 쿼리가 날라감 
    - 1 : 연관관계 주인 / N 쪽에 외래키가 있음
        - #### 객체와 테이블의 차이 때문에 반대편 테이블의 외래 키를 관리하는 특이한 구조
    - @JoinColumn을 꼭 사용해야 함 / (else JoinTable을 사용하기 때문에 성능 하락)
       
    - ### 결론 : ~~일대다 단방향매핑~~ 하지말고 다대일 양방향 매핑 사용
    
    - 일대다 양방향 정리(공식적으로 존재 X) 
        - 읽기 전용 필드를 사용하여 양방향처럼 사용하는 방법 
        - @JoinColumn(name = "~~~",insertable = false , updatable = false)
        
        
  - #### 일대일 관계 
    - 주 테이블 혹은 대상 테이블 중 외래 키 선택 능 
    - ` 외래 키에 디비 유니크 제약조건 추가 `
    - 다대일 단방향 매핑과 유사 
    - 외래 키가 있는 곳이 주인 / 반대는 mappedBy
    
    - #### 일대일 : 대상 테이블에 외래 키가 잇는 경우(단방향) -> 단방향 관계는 JPA가 지원 X / 양방향은 지원 
    
  
  - #### N : M 관계
    - 관계형 DB는 정규화되 테이블 2개로 다대다 관계를 표현할 수 없음
    - 연결 테이블을 추가해서 일대다 , 다대일 관계로 풀어야한다. (ex- 카트 / 아이템 )
    - 편리해보이지만 실무에선 사용 X
    - 연결 테이블이 단순히 연결만 하고 끝나지 않는다. 
    - ex) 주문 시간 , 수량 같은 데이터가 들어올 수 있다. 
    
  
  - #### 다대다 관계 극복 
  - @ManyToMany -> @OneToMany + @ManyToOne
  
  
  - ### 상속관계 매핑 @Inheritance
    - 슈퍼타입 서브타임 논리 모델 => 실제 물리 모델로 구현하는 방법 
    - RDB 는 상속관계가 없다
    - but 슈퍼타입 /서브타입 관계라는 논리 모델링 기업이 객체 상속과 유사 
  - #### 구현 방법
    - 1. Join으로 구현 (이게 정석) - 주 테이블 밑에 서브 테이블에서 필요한 별도의 칼럼만을 가지고 있음 & PK로 테이블 조인 구분 
        - 장점 :  테이블 정규화/ 외래키 참조 무결성 제약조건 활용 가능 / 저장공간 효율화 
        - 단점 : 조회시 조인 많다.(성능저하) / 조회 쿼리 복잡 / 데이터 저장시 insert sql 2번 호출 
    - 2. 단일 테이블 전략 (strategy = > single_table)
         - 필요 칼럼 다 떄려박아 -> 성능 제일 좋음 (join 안해도 되니까 )
         -  얘는 discriminatorColumn 어노테이션 안넣어도 자동으로 dtype 칼럼 생성됨 
         - 장점 : 조인 필요 없음 조회 쿼리 단순
         - 단점 : 자식 엔티티가 매핑한 칼럼 모두 null 허용 /  단일 테이블 저장이므로 테이블이 커지면 조회성능 느려질 가능성 존재 
           
    - 3. ~~구현 클래스마다 테이블 전략~~ strategy = inheritance.type = table_per_class -> 이거 쓰지마 
            -  각 테이블마다 공통된 칼럼을 다 가짐 
            -  상위 테이블에 대한 칼럼들을 하위 테이블의 칼럼에 다 추가 따라서 중복 칼럼 발생 
            -  단점 :  조회시, jpa 는 데이터가 있는지 없는지 다 찾아봐야해서 union all 로 쿼리문을 만들어서 조회하기 때문에 비효율적인 동작을 할 수 있다.
            -  but 확실히 데이터를 명시하게 되면 조회할 때 편하고 데이터 추가할 때는 편하다. 
            
    - #### @MappedSuperclass
        - 공통된 매핑 정보가 필요할 떄 사용 (ex . id, name이 또 필요할 때 )
        - 상속관계 매핑이 아니다.
        - 엔티티 X , 테이블과 매핑 X
        - 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공한다. 
        - 조회, 검색 불가 (em.find(BaseEntity) X)
        - 직접 생성해서 사용할일이 없으므로 추상 클래스로 만들도록 하자. 
            
    