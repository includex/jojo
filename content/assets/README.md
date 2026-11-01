# 실행 자산

이 디렉토리가 일반 빌드의 자산 원본이다. 임충전 지도·초상화·스프라이트와 공통 UI·효과·음원을 보관한다. 조조전 캠페인의 지도·인물 자산은 실행 패키지에서 제거했다. `:core:packageContentAssets`가 `core/build/generated/content-assets`로 복사하며 외부 복원 프로젝트를 사용하지 않는다.

- PNG/JPG/음원/JSON은 바이트 그대로 복사한다.
- `.rgba.gz`는 무손실 압축한 프레임버퍼다. 빌드에서 `.rgba`로 해제한다.
- 동일한 상대 경로의 `.rgba`와 `.rgba.gz`를 함께 두면 충돌로 거부한다.
- maps/data의 게임 테이블은 여기 두지 않고 콘텐츠 YAML에서 생성한다.
- atlas·manifest·텍스처 키의 연결을 유지하여 파일을 교체한다. 저장소에 새 파일을 넣을 때 함께 Git에 추가한다.
- 원본 복원/이미지 추출 도구는 선택적 수입 도구이며 일반 빌드의 일부가 아니다.

자세한 절차는 [게임 패키지 가이드](../../docs/game-package-guide.html)를 참고한다.

현재 실행 콘텐츠와 공통 자산의 구분은 [패키지 경계](../../docs/content-package.html)를 참고한다. 임충전 원본 그림과 생성 매핑은 `../examples/linchong/assets/`에서 관리한다.
