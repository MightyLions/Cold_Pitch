# <span style="color:orange">🍑 Cold_Pitch

<br>

## <span style="color:lightblue">🎟️ 프로젝트 주제

### <span style="color:lightgreen"> "초기 스타트업과 소비자 간의 상호작용을 촉진하는 플랫폼"

<br>

## <span style="color:lightblue">⌛️ 프로젝트 요약

| 분류 |           내용            | X |     스택     |  버전   |   
|----|:-----------------------:|---|:----------:|:-----:|
| 주제 | 스타트업 - 소비자 상호작용 촉진 플랫폼  | X |    JAVA    |  11   |
| 인원 |            6            | X | SpringBoot | 2.7.11 |
| 기간 | 2023.05.01 ~ 2023.07.21 | X |   MySQL    | latest      |

<br>



## <span style="color:lightblue">📘 프로젝트 배경

- 넓은 범위의 디지털 기기의 보급으로 정보의 양과 확산 속도가 급격하게 증가하여 이를 소비자들이 적극적으로 활용하고 있음.
- 이에 따라, 제품이나 서비스의 품질, 브랜드 이미지와 마케팅 전략 등에 소비자들의 평가와 선택이 큰 영향을 미치고 있음.

![img_6.png](readmeImage/survey.png)

<br>

## <span style="color:lightblue">🔍 예상 기대효과
#### 경제적 효과
 1. 스타트업의 안정적 시장 진입과 성장
 2. 플랫폼 사용자 유치를 통한 스타트업 수익 창출
    
#### 기술적 효과
 1. 디지털 신기술의 활용
      
#### 사회적 효과
 1. 스타트업 커뮤니티 활성화
 2. 국내외 스타트업과의 교류와 유입
 3. 스타트업 생태계 확장에 따른 고용 창출


<br>

## <span style="color:lightblue">💻 개발내용 및 목표
<details>
<summary><h3>서비스 다이어그램</h3></summary>
 
![img_3.png](readmeImage/service-diagram.png)

</details>
<details>
<summary><h3>시퀀스 다이어그램</h3></summary>
 
![img_5.png](readmeImage/sequence-diagram.png)

</details>
<details>
<summary><h3>ER 다이어그램</h3></summary>

![img_2.png](readmeImage/erdiagram.png)

</details>

<br>

## <span style="color:lightblue">🛠️ 기술 스택 및 의존성

통합 개발 환경 : <img src="https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white">  
개발 언어 : <img src="https://img.shields.io/badge/JAVA-11-FFFFFF?style=for-the-badge&logo=openjdk&logoColor=FFFFFF"><br>
개발 프레임 워크: <img src="https://img.shields.io/badge/SpringBoot-3.1.1-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=6DB33F">   <img src="https://img.shields.io/badge/JUnit5-FFFFFF?style=for-the-badge&logo=JUnit5&logoColor=6DB33F"><br>
데이터베이스 : <img src="https://img.shields.io/badge/mysql-003B57?style=for-the-badge&logo=Mysql&logoColor=white"><br>
배포 도구 : <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <img src="https://img.shields.io/badge/githubactions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white">    <br>
협업 도구 : <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white">
<img src="https://img.shields.io/badge/Notion -000000?style=for-the-badge&logo=Notion&logoColor=white">
<img src="https://img.shields.io/badge/Drive -4285F4?style=for-the-badge&logo=GoogleDrive&logoColor=white"><br>

<br>

## <span style="color:lightblue">☁️ 프로젝트 아키텍쳐
![img_1.png](readmeImage/architecture.png)

<br>

## <span style="color:lightblue">🗃️ 프로젝트 구조

<details>
 <summary>프로젝트 구조 확인하기</summary>

~~~
├── ColdPitchApplication.java
├── aop
│   └── LoggingAspect.java
├── config
│   ├── Auditing
│   │   ├── AuditorAwareImpl.java
│   │   └── JpaAuditingConfig.java
│   ├── cloud
│   │   └── S3Config.java
│   ├── file
│   │   └── FileConfig.java
│   ├── okHttp
│   │   └── OkHttpClientConfig.java
│   ├── querydsl
│   │   └── QuerydslConfiguration.java
│   ├── security
│   │   ├── CorsConfig.java
│   │   ├── JwtConfig.java
│   │   └── SecurityConfig.java
│   └── swagger
│       └── SwaggerConfig.java
├── core
│   ├── factory
│   │   └── YamlLoadFactory.java
│   └── manager
│       ├── AbstractFileManager.java
│       └── FileManager.java
├── domain
│   ├── apicontroller
│   │   ├── CommentApiController.java
│   │   ├── CompanyRegistrationController.java
│   │   ├── FileApiController.java
│   │   ├── HelloApiController.java
│   │   ├── PostApiController.java
│   │   ├── SolutionApiController.java
│   │   ├── TagApiController.java
│   │   ├── UserApiController.java
│   │   ├── UserAuthApiController.java
│   │   └── UserTagController.java
│   ├── entity
│   │   ├── BaseEntity.java
│   │   ├── Comment.java
│   │   ├── CompanyRegistration.java
│   │   ├── Dislike.java
│   │   ├── File.java
│   │   ├── FileStatus.java
│   │   ├── Hello.java
│   │   ├── Like.java
│   │   ├── Post.java
│   │   ├── Solution.java
│   │   ├── Tag.java
│   │   ├── User.java
│   │   ├── UserTag.java
│   │   ├── comment
│   │   │   ├── CommentRequestType.java
│   │   │   └── CommentState.java
│   │   ├── dto
│   │   │   ├── comment
│   │   │   │   ├── CommentRequestDto.java
│   │   │   │   └── CommentResponseDto.java
│   │   │   ├── companyRegistraion
│   │   │   │   ├── CompanyRegistrationDto.java
│   │   │   │   └── CompanyRegistrationValidationDto.java
│   │   │   ├── file
│   │   │   │   ├── FileUploadRequest.java
│   │   │   │   └── FileUploadResponse.java
│   │   │   ├── jwt
│   │   │   │   ├── RefreshToken.java
│   │   │   │   ├── TokenDto.java
│   │   │   │   └── TokenRequestDto.java
│   │   │   ├── post
│   │   │   │   ├── PostRequestDto.java
│   │   │   │   └── PostResponseDto.java
│   │   │   ├── solution
│   │   │   │   ├── SolutionRequestDto.java
│   │   │   │   └── SolutionResponseDto.java
│   │   │   ├── tag
│   │   │   │   └── TagRequestDto.java
│   │   │   ├── user
│   │   │   │   ├── CompanyRequestDto.java
│   │   │   │   ├── CompanyResponseDto.java
│   │   │   │   ├── LoginDto.java
│   │   │   │   ├── UserRequestDto.java
│   │   │   │   └── UserResponseDto.java
│   │   │   └── usertag
│   │   │       ├── TagRequestDto.java
│   │   │       └── TagResponseDto.java
│   │   ├── post
│   │   │   ├── Category.java
│   │   │   ├── LikeState.java
│   │   │   └── PostState.java
│   │   ├── solution
│   │   │   └── SolutionState.java
│   │   ├── tag
│   │   │   └── TagName.java
│   │   └── user
│   │       ├── CurState.java
│   │       └── UserType.java
│   ├── repository
│   │   ├── CommentRepository.java
│   │   ├── CommentRepositoryCustom.java
│   │   ├── CommentRepositoryImpl.java
│   │   ├── CompanyRegistrationRepository.java
│   │   ├── DislikeRepository.java
│   │   ├── FileJPARepository.java
│   │   ├── FileQueryRepository.java
│   │   ├── FileRepository.java
│   │   ├── FileRepositoryCustom.java
│   │   ├── HelloRepository.java
│   │   ├── LikeRepository.java
│   │   ├── PostRepository.java
│   │   ├── PostRepositoryCustom.java
│   │   ├── PostRepositoryImpl.java
│   │   ├── RefreshTokenRepository.java
│   │   ├── SolutionRepository.java
│   │   ├── SolutionRepositoryCustom.java
│   │   ├── SolutionRepositoryImpl.java
│   │   ├── TagRepository.java
│   │   ├── UserRepository.java
│   │   ├── UserRepositoryCustom.java
│   │   ├── UserRepositoryCustomImpl.java
│   │   ├── UserTagRepository.java
│   │   └── support
│   │       └── Querydsl4RepositorySupport.java
│   └── service
│       ├── CommentService.java
│       ├── CompanyRegistrationService.java
│       ├── CompanyRegistrationValidator.java
│       ├── CustomUserDetailService.java
│       ├── FileService.java
│       ├── HelloService.java
│       ├── PostService.java
│       ├── RefreshTokenService.java
│       ├── SolutionService.java
│       ├── TagService.java
│       ├── UserService.java
│       └── UserTagService.java
├── exception
│   ├── CustomException.java
│   ├── CustomSecurityException.java
│   ├── ExceptionHandleFilter.java
│   └── handler
│       ├── ErrorCode.java
│       ├── ErrorResponse.java
│       └── GlobalExceptionHandler.java
├── jwt
│   ├── JwtFilter.java
│   ├── TokenProvider.java
│   └── exception
│       ├── JwtAccessDeniedHandler.java
│       └── JwtAuthenticationEntryPoint.java
└── utils
    ├── RandomUtil.java
    ├── SecurityUtil.java
    └── ServerUtil.java
~~~

</details>

## <span style="color:lightblue">😉 오류처리
<details>
<summary> 오류 처리 문서</summary>
HTTP 상태 코드


| 상태  |         코드          |               	메시지               |
|-----|:-------------------:|:--------------------------------:|
| 204 |  해당 컨텐츠를 찾을 수 없습니다  |    	요청한 컨텐츠를 찾을 수 없을 때 반환됩니다.    |
| 301 |    URI가 변경되었습니다	    |      요청한 URI가 변경되었음을 나타냅니다.      |
| 400 |     잘못된 요청입니다	      |      클라이언트의 잘못된 요청을 나타냅니다.       |
| 403 |    접근 권한이 없습니다	     |       접근 권한이 없는 경우 반환됩니다.        |
| 404 | 해당 컨텐츠가 존재하지 않습니다	  |    요청한 컨텐츠가 존재하지 않을 때 반환됩니다.     |
| 405 |    잘못된 접근 방식입니다     | 	요청에 사용된 HTTP 메서드가 잘못되었음을 나타냅니다. |
| 408 |  접근 요청시간이 만료되었습니다   |      	요청 시간이 만료되었을 때 반환됩니다.      |
| 500 | 내부적인 서버 에러가 발생했습니다	 | 서버 내부에서 예기치 않은 에러가 발생한 경우 반환됩니다. |
| 501 |   구현되지 않은 기능입니다	    |    요청한 기능이 구현되지 않은 경우 반환됩니다.     |
| 401 | JWT 서명이 존재하지 않습니다	  |       JWT 서명이 없을 때 반환됩니다.        |
| 403 |     권한이 맞지 않습니다     |       	권한이 맞지 않을 때 반환됩니다.        |


</details>

## <span style="color:lightblue">🧐 비즈니스 로직
<details>
<summary>초기 비즈니스 모델</summary>
 
![스크린샷 2023-07-13 오전 10 25 48](https://github.com/MightyLions/Cold_Pitch/assets/81156109/7a7a862a-4469-4415-abaf-df47d4e4558b)


</details>

<details>
<summary>메인 비즈니스 모델</summary>
 
![스크린샷 2023-07-13 오전 10 25 57](https://github.com/MightyLions/Cold_Pitch/assets/81156109/2c04703d-93b7-495d-97fa-4653bb9e1e6f)

</details>

<details>
<summary>확장 비즈니스 모델</summary>
 
![스크린샷 2023-07-13 오전 10 26 08](https://github.com/MightyLions/Cold_Pitch/assets/81156109/57c59e35-b4d9-41f8-a032-83009cc3384c)


</details>

<br>

## <span style="color:lightblue">👥 팀원 소개

<div>
<table>
  <tbody>
    <tr>
        <td align="center"> 팀장 </td>
        <td align="center"> 팀원 </td>
        <td align="center"> 팀원 </td>
        <td align="center"> 팀원 </td>
        <td align="center"> 팀원 </td>
        <td align="center"> 팀원 </td>
    </tr>
    <tr>
        <td><img src="" width="100px;" alt=""/><br> </td>
        <td><img src="" width="100px;" alt=""/><br> </td>
        <td><img src="" width="100px;" alt=""/><br> </td>
        <td><img src="" width="100px;" alt=""/><br> </td>
        <td><img src="" width="100px;" alt=""/><br> </td>
        <td><img src="" width="100px;" alt=""/><br> </td>
    </tr>
    <tr>
      <td align="center"><a href="https://github.com/sangdob"> 박상도 </a></td>
      <td align="center"><a href="https://github.com/mon0mon"> 이민기 </a></td>
      <td align="center"><a href="https://github.com/dyKim"> 김도영 </a></td>
      <td align="center"><a href="https://github.com/dlrjs2360"> 이희건 </a></td>
      <td align="center"><a href="https://github.com/juhee77"> 박주희 </a></td>
      <td align="center"><a href="https://github.com/bear31eun"> 이정은 </a></td>
    </tr>

  </tbody>
</table>
</div>

## <span style="color:lightblue">🗂️ 버전기록


<br> 
