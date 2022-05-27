# API의 성능 최적화

<p> JPA를 사용해서 API를 개발할 때 주의점과 여러가지 성능 최적화의 방법들을 적용하였다. </p>

## 최적화 방법

### 컬렉션 조회

```
@GetMapping("/api/v2/members")
public Result membersV2() {
	List<Member>findMembers = memberService.findMembers();
	
	List<MemberDto>collect = findMembers.stream()
				.map(m -> new MemberDto(m.getName()))
				.collect(Collectors.toList());
	return new Result(collect,collect.size());
}
	
```
* 단일 조회가 아닌 컬렉션 조회이기 때문에 Result 제네릭을 만들어 반환한다.
* Entity를 직접적 반환하지 않는다.

### 1:1 조회 - 페치 조인
@ManyToOne에서는 fetch를 LAZY로 설정해주어야 한다.
따라서 현재 엔티티와 관련이 있는 엔티티들은 모두 프록시 객체로 가져오는데
이는 데이터베이스에 별도의 쿼리문을 날려서 한번 더 가져와야한다.

이걸 N + 1(연관된 엔티티 + 자기 자신) 문제 라고 한다.
이는 JPA의 문제라기보단 JPQL의 문제에 더 가깝다.

```
select m from Member m left join fetch m.team
```

이런 식으로 jpql을 작성하면 페치 조인을 적용시킬 수 있다.
