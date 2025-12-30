document.addEventListener("DOMContentLoaded", function() {
    // 초기 로드 시 인증번호 입력 필드 비활성화
    document.getElementById("certification-number").disabled = true;

    // 인증번호 보내기 버튼 클릭 시
    document.getElementById("certification-number-submit").addEventListener("click", function(event) {
        event.preventDefault(); // 폼 제출 방지
        document.getElementById("certification-number").disabled = false; // 인증번호 입력 필드 활성화
    });
});