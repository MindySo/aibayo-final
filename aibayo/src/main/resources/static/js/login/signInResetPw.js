document.addEventListener("DOMContentLoaded", function() {
    document.querySelector("form").addEventListener("submit", function(event) {
        var password1 = document.getElementById("password1").value;
        var password2 = document.getElementById("password2").value;

        if (password1 !== password2) {
            event.preventDefault(); // 폼 제출 방지
            alert("비밀번호가 일치하지 않습니다");
            document.getElementById("password1").value = ""; // password1 리셋
            document.getElementById("password2").value = ""; // password2 리셋
        }
    });
});
