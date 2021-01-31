<h1>쇼핑몰 API</h1>
<h2>1. 프로젝트 설명</h2>
쇼핑몰 구축에 필요한 API 를 제공한다. 
클라이언트 프로그램에서 사용하는 결제 ~ 구매 완료 , 포인트 누적 등의 기능을 담고있다.

<h2>2. 프로젝트 실행</h3>
해당 프로젝트는 maven 빌드 툴을 사용한다. 

몇몇 설정의 이유로 패키징 이후에 정상 사용이 가능하다.
패키징명령어는 프로젝트 루트 패스에서 `mvn package` 명령을 통해 진행할 수 있다.

 
<h2>3. 프로젝트 문서</h3>

실행 이후 `http://localhost:15080/docs/index.html` 경로로 이동할 경우 해당 프로젝트의 API 
문서를 볼 수 있다.


<h2>4. 프로젝트 설정</h3>
해당 프로젝트는 4개의 써드파티 API 설정을 가지고 있으며 각 설정을 맞춰야만 
정상 사용 가능한 기능이 존재한다.

1. SNS 로그인

    - kakao 로그인, 회원가입 기능을 제공한다.
       
    - yml 파일의 kjs-mall.login.kakao 의 설정을 맞춰야 한다.
    
    - client_id와 client_secret 에 사용자가 실제로 발급 받은 키를 설정한다.

2. 결제 연동

    - nice_pay를 이용한 결제를 제공한다.
    
    - kakao, naver, 가상계좌, 카드결제 기능을 제공하며, 해당 기능을 갖춘 계정을 나이스 페이에서 발급받아야 한다.
    
    - yml 파일의 kjs-mall.payment.nicePay 의 설정을 맞춰야 한다.
    
    - merchant-id, merchant-key 에 사용자가 실제로 발급 받은 키를 설정한다.
 
3. sms 발송 연동

    - purrio 를 이용한 sms 발송을 제공한다.
    
    - yml 파일의 kjs-mall.sms.purrio 의 설정을 맞춰야 한다.
    
    - user_id, sender_phone  에 사용자가 실제로 발급 받은 키를 설정한다.

4. 파일업로드 

    - 자체적으로 개발한 파일 서버를 이용한다.
    
    - multipart-file 형식의 file을 파라미터로 입력받고
    `src/main/java/me/kjs/mall/ConnectService/FileUploadResponse.class` 
    형식으로 입력받을 수 있는 api url을 제공할 경우 사용 가능하다.
    
    - yml 파일의 kjs-mall.file-upload 의 설정을 맞추면 사용 가능하다.