# API의 성능 최적화

<p> JPA를 사용해서 API를 개발할 때 주의점과 여러가지 성능 최적화의 방법들을 적용하였다. </p>

## 최적화 방법

### 컬렉션 조회
₩₩₩

// 조회 API
@GetMapping("/api/v2/members")
public Result membersV2() {
	List<Member>findMembers = memberService.findMembers();
	
	List<MemberDto>collect = findMembers.stream()
				.map(m -> new MemberDto(m.getName()))
				.collect(Collectors.toList());
	return new Result(collect,collect.size());
	// 이렇게 클래스로 감싸야 안에 형식을 바꾸는게 유연해진다.
}
	
₩₩₩
