function toggleClassField() {
    const kindergartenSelect = document.getElementById('kindergarten-input');
    const classSelect = document.getElementById('class-input');

    if (kindergartenSelect.value === "") {
        classSelect.disabled = true;
    } else {
        classSelect.disabled = false;
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const kindergartenSelect = document.getElementById('kindergarten-input');
    const classSelect = document.getElementById('class-input');

    // Initialize the class select as disabled
    classSelect.disabled = true;

    // Add event listener to kindergarten select
    kindergartenSelect.addEventListener('change', toggleClassField);
});

function submitForm() {
    var formData = $("#signUpKidForm").serialize();

    $.ajax({
        type: "POST",
        url: "/member/finalSignUp",
        data: formData,
        success: function(response) {
            // 성공 시 처리
            console.log("회원가입 성공");
            window.location.href = "/member/signIn"; // 성공 페이지로 리다이렉트
        },
        error: function(error) {
            // 에러 시 처리
            console.log("회원가입 실패");
            window.location.href = "/member/signIn"; // 에러 페이지로 리다이렉트
        }
    });
}
