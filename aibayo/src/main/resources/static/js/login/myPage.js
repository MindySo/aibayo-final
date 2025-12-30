$(document).ready(function () {
    // 생활 기록이 있을 경우 처리
    if ($('.notepad_detail_content_body').find('.liferecord').length > 0) {
        let lifeRecord = $('.liferecord');
        let mood = lifeRecord.data('mood');
        let health = lifeRecord.data('health');
        let temperature = lifeRecord.data('temperature');
        let meal = lifeRecord.data('meal');
        let sleepTime = lifeRecord.data('sleepTime');
        let defecationStatus = lifeRecord.data('defecationStatus');

        console.log(`mood: ${mood}, health: ${health}, temperature: ${temperature},
                    meal: ${meal}, sleepTime: ${sleepTime}, defecationStatus: ${defecationStatus}`);

        $('#mood' + mood).attr('checked', 'true');
        $('#health' + health).attr('checked', 'true');
        $('#temperature' + temperature).attr('checked', 'true');
        $('#meal' + meal).attr('checked', 'true');
        $('#sleeptime' + sleepTime).attr('checked', 'true');
        $('#defecation_status' + defecationStatus).attr('checked', 'true');
    }

// 계정 삭제 버튼 클릭 시 확인 알림 띄우기
    $('#deleteBtn').on('click', function () {
        Swal.fire({
            title: "정말로 삭제하시겠습니까?",
            text: "계정을 삭제하면 복구할 수 없습니다.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "삭제",
            cancelButtonText: "취소"
        }).then((result) => {
            if (result.isConfirmed) {
                // AJAX 요청을 통해 서버로 삭제 요청을 보냄
                $.ajax({
                    url: "/member/deleteMember",
                    type: "POST", // POST 메서드를 사용 (DELETE 메서드도 사용 가능)
                    success: function(response) {
                        Swal.fire('삭제되었습니다!', '', 'success').then(() => {
                            // 계정 삭제 후 리디렉션 또는 추가 작업을 수행
                            window.location.href = '/member/signIn'; // 예: 로그아웃 페이지로 이동
                        });
                    },
                    error: function(xhr, status, error) {
                        console.log('Error:', error);
                        Swal.fire('오류가 발생했습니다.', xhr.responseText, 'error');
                    }
                });
            }
        });
    });

    // 저장 버튼 클릭 시 확인 알림 띄우기
    $('#saveBtn').on('click', function () {
        Swal.fire({
            title: "정보 수정",
            text: "정보를 수정하시겠습니까?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "예",
            cancelButtonText: "아니요"
        }).then((result) => {
            if (result.isConfirmed) {
                // 확인을 누르면 폼을 제출
                $('#myPageForm').submit();
            }
        });
    });

    // 비밀번호 수정 모달 관련 처리
    function closeModal() {
        let passwordModal = document.getElementById('passwordModal');
        let cancelButton = passwordModal.querySelector('.btn-secondary');
        cancelButton.addEventListener('click', function() {
            let modalInstance = bootstrap.Modal.getInstance(passwordModal);
            if (modalInstance) {
                modalInstance.hide();
            }
        });
    }

    function resetInputs(currentPassword, newPassword, confirmPassword) {
        currentPassword.value = '';
        newPassword.value = '';
        confirmPassword.value = '';
    }

    function validatePasswords(currentPassword, newPassword, confirmPassword) {
        if (currentPassword.value === newPassword.value) {
            Swal.fire({
                icon: 'warning',
                title: '경고',
                text: '이전 비밀번호와 일치합니다.',
                confirmButtonText: '확인'
            });
            resetInputs(currentPassword, newPassword, confirmPassword);
            return false;
        }

        if (newPassword.value !== confirmPassword.value) {
            Swal.fire({
                icon: 'error',
                title: '오류',
                text: '비밀번호가 일치하지 않습니다.',
                confirmButtonText: '확인'
            });
            resetInputs(currentPassword, newPassword, confirmPassword);
            return false;
        }

        return true;
    }

    function handleFormSubmit(event) {
        event.preventDefault();
        console.log('Submit event triggered');

        let passwordForm = event.target;
        let currentPassword = document.getElementById('currentPassword');
        let newPassword = document.getElementById('newPassword');
        let confirmPassword = document.getElementById('confirmPassword');

        if (validatePasswords(currentPassword, newPassword, confirmPassword)) {
            $.ajax({
                method: 'POST',
                url: '/member/passwordExist',
                contentType: 'application/json',
                data: JSON.stringify({
                    password: currentPassword.value
                }),
                success: function(response) {
                    if (response.exists) {
                        Swal.fire({
                            icon: 'success',
                            title: '성공',
                            text: '비밀번호를 수정했습니다.',
                            confirmButtonText: '확인'
                        }).then(() => {
                            passwordForm.submit();
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: '오류',
                            text: '존재하지 않는 비밀번호입니다.',
                            confirmButtonText: '확인'
                        });
                        resetInputs(currentPassword, newPassword, confirmPassword);
                    }
                },
                error: function() {
                    Swal.fire({
                        icon: 'error',
                        title: '서버 오류',
                        text: '서버 요청 중 오류가 발생했습니다.',
                        confirmButtonText: '확인'
                    });
                    resetInputs(currentPassword, newPassword, confirmPassword);
                }
            });
        }
    }

    function initPasswordForm() {
        let passwordForm = document.getElementById('passwordForm');
        passwordForm.replaceWith(passwordForm.cloneNode(true));
        passwordForm = document.getElementById('passwordForm');
        passwordForm.addEventListener('submit', handleFormSubmit);
    }

    function initialize() {
        closeModal();
        initPasswordForm();
    }

    initialize();
});

function afterSuccess(response) {
    Swal.fire({
        title: "삭제 완료",
        text: "창을 닫으면 목록 화면으로 돌아갑니다.",
        icon: "success",
        customClass: {
            confirmButton: 'btn-ab btn-ab-swal'
        }
    }).then((result) => {
        window.location.href = window.location.origin + '/notepad/admin/list';
    });
}

// function confirmDelete() {
//     if (confirm("계정을 삭제하시겠습니까?")) {
//         window.location.href = "/member/deleteMember";
//         // window.location.href = /*[[@{/member/deleteMember}]]*/ '/member/deleteMember';
//     }
// }
