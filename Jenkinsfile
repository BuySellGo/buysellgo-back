// 아래 서비스 리스트는 변경될 수 있음.
//
// 1.회원서비스(user-srevice)
// 2.상품서비스(product-service)
// 3.장바구니서비스(cart-service)
// 4.주문서비스(order-service)
// 5.리뷰서비스(review-service)
// 6.Q&A서비스(qna-service)
// 7.고객센터서비스(helpdesk-service) <
// 8.검색서비스(search-service)
// 9.결제서비스(payment-service)
// 10.배송서비스(delivery-service) <
// 11.프로모션(promotion-service) <
// 12.채팅서비스(chat-service)
// 13.통계서비스(statistics-service) <

// 필요한 변수를 선언할 수 있다. (내가 직접 선언하는 변수, 젠킨스 환경변수를 끌고 올 수 있음)
def ecrLoginHelper="docker-credential-ecr-login" // ECR credential helper 이름

// 젠킨스의 선언형 파이프라인 정의부 시작 (그루비 언어)
pipeline {
    agent any // 어느 젠킨스 서버에서도 실행이 가능
    environment {
        REGION = "ap-northeast-2"
        ECR_URL = "216989126830.dkr.ecr.ap-northeast-2.amazonaws.com"
        SERVICE_DIRS = "config-service,gateway-service,user-service,order-service,product-service,cart-service,review-service,qna-service,helpdesk-service,search-service,payment-service,delivery-service,promotion-service,chat-service,statistics-service"
        K8S_REPO_URL = "https://github.com/BuySellGo/buysellgo-back.git"
        K8S_REPO_CRED = "github-k8s-repo-token"
    }
    stages {
        stage('Pull Codes from Github'){ // 스테이지 제목 (맘대로 써도 됨.)
            steps{
                checkout scm // 젠킨스와 연결된 소스 컨트롤 매니저(git 등)에서 코드를 가져오는 명령어
            }
        }
        //--------------------------------------------
        stage('Detect Changes') {
            steps {
                script {
                    // 변경된 파일 감지
                    def changedFiles = sh(script: "git diff --name-only HEAD~1 HEAD", returnStdout: true)
                        .trim()
                        .split('\n') // 변경된 파일을 줄 단위로 분리

                    // 변경된 파일 출력
                    // user-service/src/main/resources/application.yml
                    // user-service/src/main/java/com/buysellgo/userservice/controller/UserController.java
                    // ordering-service/src/main/resources/application.yml
                    echo "Changed files: ${changedFiles}"

                    def changedServices = []
                    def serviceDirs = env.SERVICE_DIRS.split(",")

                    serviceDirs.each { service ->
                        if (changedFiles.any { it.startsWith(service + "/") }) {
                            changedServices.add(service)
                        }
                    }

                    env.CHANGED_SERVICES = changedServices.join(",")
                    if (env.CHANGED_SERVICES == "") {
                        echo "No changes detected in service directories. Skipping build and deployment."
                        // 성공 상태로 파이프라인 종료
                        currentBuild.result = 'SUCCESS' // 성공으로 표시
                    }
                }
            }
        }
        //--------------------------------------------

//         stage('Build Codes by Gradle') {
//             steps {
//                 script {
//                     def serviceDirs = env.SERVICE_DIRS.split(",")
//                     serviceDirs.each { service ->
//                         sh """
//                         echo "Building ${service}"
//                         cd ${service}
//                         chmod +x ./gradlew
//                         ./gradlew clean build -x test
//                         ls -al ./build/libs
//                         cd ..
//                         """
//                     }
//                 }
//             }
//         }
        stage('Build Changed Services') {
            // 이 스테이지는 빌드되어야 할 서비스가 존재한다면 실행되는 스테이지.
            // 이전 스테이지에서 세팅한 CHANGED_SERVICES라는 환경변수가 비어있지 않아야만 실행.
            when {
                expression { env.CHANGED_SERVICES != "" } // 변경된 서비스가 있을 때만 실행
            }
            steps {
                script {
                    def changedServices = env.CHANGED_SERVICES.split(",")
                    changedServices.each { service ->
                        sh """
                        echo "Building ${service}..."
                        cd ${service}
                        ./gradlew clean build -x test
                        ls -al ./build/libs
                        cd ..
                        """
                    }
                }
            }
        }
        //-----------------------------------------------
//         stage('Build Docker Image & Push to AWS ECR') {
//             steps {
//                  script {
//                     // withAWS를 통해 리전과 계정의 access, secret 키를 가져옴.
//                     withAWS(region: "${REGION}", credentials: "aws-key") {
//                         def serviceDirs = env.SERVICE_DIRS.split(",")
//                         serviceDirs.each { service ->
//                             // AWS에 접속해서 ECR을 사용해야 하는데, 젠킨스에는 aws-cli를 설치하지 않았어요.
//                             // aws-cli 없이도 접속을 할 수 있게 도와주는 라이브러리 설치.
//                             // helper가 대신 aws에 로그인을 진행. 그리고 그 인증 정보를 json으로 만들어서
//                             // docker에게 세팅해줌. -> docker가 ECR에 push가 가능해짐.
//                             sh """
//                                 curl -O https://amazon-ecr-credential-helper-releases.s3.us-east-2.amazonaws.com/0.4.0/linux-amd64/${ecrLoginHelper}
//                                 chmod +x ${ecrLoginHelper}
//                                 mv ${ecrLoginHelper} /usr/local/bin/
//
//                                 echo '{"credHelpers": {"${ECR_URL}": "ecr-login"}}' > ~/.docker/config.json
//
//                                 # Docker 이미지 빌드(서비스 이름으로)
//                                 docker build -t ${service}:latest ${service}
//
//                                 # ECR 레포지토리로 태깅
//                                 docker tag ${service}:latest ${ECR_URL}/${service}:latest
//
//                                 # ECR 로 푸시
//                                 docker push ${ECR_URL}/${service}:latest
//                             """
//                         }
//                     }
//                 }
//             }
//         }
        stage('Build Docker Image & Push to AWS ECR') {
           when {
               expression { env.CHANGED_SERVICES != "" } // 변경된 서비스가 있을 때만 실행
           }
            steps {
                script {
                    withAWS(region: "${REGION}", credentials: "aws-key") {
                        def changedServices = env.CHANGED_SERVICES.split(",")
                        changedServices.each { service ->
                            // 여기서 원하는 버전을 정하거나, 커밋 태그를 붙여보자.
                            def newTag = "1.0.2"
                            sh """
                            curl -O https://amazon-ecr-credential-helper-releases.s3.us-east-2.amazonaws.com/0.4.0/linux-amd64/${ecrLoginHelper}
                            chmod +x ${ecrLoginHelper}
                            mv ${ecrLoginHelper} /usr/local/bin/

                            echo '{"credHelpers": {"${ECR_URL}": "ecr-login"}}' > ~/.docker/config.json

                            docker build -t ${service}:${newTag} ${service}
                            docker tag ${service}:${newTag} ${ECR_URL}/${service}:${newTag}
                            docker push ${ECR_URL}/${service}:${newTag}
                            """
                        }
                    }
                }
            }
        }
        //-------------------------------------------------------------------------

       stage('Update k8s Repo') {
           when {
               expression { env.CHANGED_SERVICES != "" } // 변경된 서비스가 있을 때만 실행
           }

           steps {
               script {
                   // 1. k8s 레포지토리를 클론하자.
                   // 기존 git 스텝은 토큰 방식에서는 잘 말을 듣지 않아서
                   // withCredentials를 이용해서 credential 정보를 가져옴. 가져온 정보를 GIT_USERNAME과 GIT_PASSWORD라는 이름의 환경변수로 설정.
                   withCredentials([usernamePassword(credentialsId: "${K8S_REPO_CRED}", usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                           // git clone 실시. 기존 디렉토리 경로가 스프링 프로젝트로 되어있기에 cd .. 으로 한 단계 위로 나가서 클론하기
                           sh '''
                               cd ..
                               ls -a
                               git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/BuySellGo/buysellgo-k8s.git
                           '''

                           def changedServices = env.CHANGED_SERVICES.split(",")
                           changedServices.each { service ->
                               def newTag = "1.0.2" // 이미지 빌드할 때 사용한 태그를 동일하게 사용. (태그 값은 직접 지정)

                               // umbrella-chart/charts/<service>/values.yaml 파일 내의 image 태그 교체.
                               // sed: 스트림 편집기(stream editor), 텍스트 파일을 수정하는 데 사용.
                               // s#^ -> 라인의 시작을 의미. image: -> 텍스트 image:을 찾아라, .* -> image: 다음에 오는 모든 문자
                               // 새로운 태그를 붙인 ecr 경로로 수정을 진행해라
                               sh """
                                   cd /var/jenkins_home/workspace/buysellgo-k8s
                                   ls -a
                                   echo "Updating ${service} image tag in k8s repo...."
                                   sed -i 's#^image: .*#image: ${ECR_URL}/${service}:${newTag}#' ./umbrella-chart/charts/${service}/values.yaml
                               """
                           }


                       // 변경사항 commit & push
                       // cd를 이용해서 orderservice-k8s 이동해 준다. (sh가 실시될 때마다 기본 경로가 스프링 프로젝트 내부로 잡힘.)
                       // 설정 할 것 하고 나서 commit 후 push를 진행.
                       // 혹시라도 변경사항이 없으면 commit과 push를 진행하지 않게 조건부 설정 (안하면 push에서 에러가 발생.)
                       // push까지 완료되었다면 다음 clone을 위해서 clone했던 폴더를 삭제. (clone 시 경로가 존재하면 에러가 발생)
                       sh """
                           cd /var/jenkins_home/workspace/buysellgo-k8s
                           git config user.name "username"
                           git config user.email "email@email.com"
                           git remote -v
                           git add .

                           if git diff --cached --quiet; then
                               echo "nothing to commit. skip push."
                           else
                               git commit -m "Update images for changed services ${env.BUILD_ID}"
                               git push origin main
                               echo "push successfully complete."
                           fi

                           cd ..
                           rm -rf buysellgo-k8s
                           ls -a
                       """

                   }
               }
           }
       }

        //----------------------------------------------------------------
    }
}