$(document).ready(function() {
    initStatus();
    changeByStatus();
    ifResultNull();

    registerEventHandlers()

    // 반에 따라 list 보여주기
    $(document).on('change', '#selectClass', function () {
        let classNo = $('#selectClass').val();
        console.log("반 변경:" + classNo);

        let param = {
            classNo: classNo
        };

        console.log("param:" + JSON.stringify(param));

        let url = "/teacher/listByClass";

        commonAjax(url, 'POST', param);

    });

    $('#send_email').on('click', function () {
        initMsg();

        let inviteName = $('#teacher_name').val().trim();
        // console.log(`inviteName: ${inviteName}`);
        let inviteEmail = $('#teacher_email').val().trim();
        // console.log(`inviteEmail: ${inviteEmail}`);

        if (inviteName === '') {
            // console.log("이름 공란");
            let msg = $('#teacher_name').siblings('.div-2').find('.msg');
            msg.show();
            $('#teacher_name').focus();
            return false;
        }

        let emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        if (inviteEmail === '' || !emailPattern.test(inviteEmail)) {
            // console.log("이메일 형식 유효성 오류")
            let msg = $('#teacher_email').siblings('.div-2').find('.msg');
            msg.show()
            $('#teacher_email').focus();
            return false;
        }

        let url = "/inviteCode/mail";

        let param = {
            inviteType : 0,
            inviteEmail : inviteEmail,
            inviteName : inviteName,
            kinderNo : $('.classList').data('kinder-no'),
        }
        console.log(`param: ${JSON.stringify(param)}`);

        Swal.fire({
            title: '전송 중...',
            text: '초대 메일을 전송하고 있습니다.',
            allowOutsideClick: false,
            didOpen: () => {
                Swal.showLoading();
            }
        });

        commonAjax(url, 'POST', param);
    });
});

function registerEventHandlers() {
    $(document).on('click', '#waitAccept', function () {
        Swal.fire({
            title: "정말로 승인하시겠습니까?",
            showCancelButton: true,
            confirmButtonText: "네",
            cancelButtonText: "아니오",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {
            if (result.isConfirmed) {
                let target = $(this).closest('.profile_wait');
                let url = "/teacher/modifyOk";

                let param = {
                    id: parseInt(target.attr('id')),
                    teacherKinderAcceptNo: target.data('teacher-kinder-accept-no')
                }
                console.log(`param : ${JSON.stringify(param)}`);

                target.remove();

                commonAjax(url, 'PUT', param);
            }
        });
    });

    $(document).on('click', '#waitReject', function () {
        Swal.fire({
            title: "정말로 삭제하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "네",
            cancelButtonText: "아니오"
        }).then((result) => {
            if (result.isConfirmed) {
                let target = $(this).closest('.profile_wait');

                let url = "/teacher/deleteOk";

                let param = {
                    id: parseInt(target.attr('id')),
                    kinderAcceptNo: target.data('teacher-kinder-accept-no')
                }
                console.log(`param : ${JSON.stringify(param)}`);

                target.remove();

                commonAjax(url, 'DELETE', param);
            }
        });
    });


    // 초대중 처리
    $(document).on('click', '#inviteDelete', function () {
        Swal.fire({
            title: "정말로 삭제하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "네",
            cancelButtonText: "아니오"
        }).then((result) => {
            if (result.isConfirmed) {
                let target = $(this).closest('.profile_invite');

                let url = "/inviteCode/deleteOk";

                let param = {
                    inviteId: target.data('invite-id'),
                    acceptNo: target.data('invite-code-accept-no')
                }
                console.log(`param : ${JSON.stringify(param)}`);

                target.closest('.profile_invite').remove();

                commonAjax(url, 'DELETE', param);
            }
        });
    });

    $(document).on('click', '#inviteResend', function () {
        Swal.fire({
            title: "초대 메일을 재전송 하시겠습니까?",
            showCancelButton: true,
            confirmButtonText: "네",
            cancelButtonText: "아니오",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {
            if (result.isConfirmed) {
                let target = $(this).closest('.profile_invite');

                let url = "/inviteCode/resendMail";

                let param = {
                    inviteId: target.data('invite-id'),
                    acceptNo: target.data('invite-code-accept-no')
                }
                console.log(`param : ${JSON.stringify(param)}`);

                Swal.fire({
                    title: '전송 중...',
                    text: '초대 메일을 전송하고 있습니다.',
                    allowOutsideClick: false,
                    didOpen: () => {
                        Swal.showLoading();
                    }
                });

                commonAjax(url, 'PUT', param);
            }
        });
    });
}

function initMsg() {
    $('.msg').each(function () {
        $(this).hide();
    })
}

function initStatus() {
    $('.row-selected-teacher').hide();

    // 초기 로드 시 체크된 라디오 버튼에 해당하는 div를 표시
    var initialSelectedValue = $('input[name="teacherStatusRd"]:checked').val();
    if (initialSelectedValue) {
        $('#teacherStatus' + initialSelectedValue + 'Div').show();
    }
}

function changeByStatus(){
    $('input[name="teacherStatusRd"]').on('change', function() {
        var selectedValue = $('input[name="teacherStatusRd"]:checked').val();

        // 모든 상태 div 숨기기
        $('.row-selected-teacher').hide();

        // 선택된 값에 따라 해당 div 보이기
        $('#teacherStatus' + selectedValue + 'Div').show();

        // ifResultNull();
    });
}


// 새롭게 받은 데이터를 이용하여 프로필 리스트 갱신
function updateProfileLists(response) {
    $('#teacherStatus1Div .carousel').replaceWith($(response).find('#teacherStatus1Div .carousel'));
    $('#teacherStatus0Div .carousel_wait').replaceWith($(response).find('#teacherStatus0Div .carousel_wait'));
    $('#teacherStatus2Div .carousel_invite').replaceWith($(response).find('#teacherStatus2Div .carousel_invite'));

    // 이벤트 핸들러 재등록
    registerEventHandlers();
}

function updateStatusCounts() {
    // 승인 완료 교사 수 업데이트
    let acceptedCount = $('#teacherStatus1Div .profile').length;
    $('label[for="teacherStatus1"]').text(`승인 완료 ${acceptedCount}명`);
    $('#teacherStatus1Div .n-2').text(`${acceptedCount}`);

    // 승인 대기 교사 수 업데이트
    let waitingCount = $('#teacherStatus0Div .profile_wait').length;
    $('label[for="teacherStatus0"]').text(`승인 대기 ${waitingCount}명`);
    $('#teacherStatus0Div .n-2').text(`${waitingCount}`);

    // 초대 중 교사 수 업데이트
    let invitedCount = $('#teacherStatus2Div .profile_invite').length;
    $('label[for="teacherStatus2"]').text(`초대 중 ${invitedCount}명`);
    $('#teacherStatus2Div .n-2').text(`${invitedCount}`);
}

function ifResultNull(){

    // 모든 상태 div 숨기기
    $('.ifResultNull').hide();

    // 선택된 값에 따라 해당 div 보이기

    // console.log($('#teacherStatus1Div .profile').find().prevObject.length);
    // console.log($('#teacherStatus0Div .profile').find().prevObject.length);
    // console.log($('#teacherStatus2Div .profile').find().prevObject.length);

    if($('#teacherStatus1Div .profile').find().prevObject.length == 0){
        $('#teacherStatus1Div .ifResultNull').show();
    }
    if($('#teacherStatus0Div .profile_wait').find().prevObject.length == 0){
        $('#teacherStatus0Div .ifResultNull').show();
    }
    if($('#teacherStatus2Div .profile_invite').find().prevObject.length == 0){
        $('#teacherStatus2Div .ifResultNull').show();
    }
}





function afterSuccess(response, method) {

    console.log('AJAX Response:', JSON.stringify(response, null, 2));
    console.log('AJAX Response keys:', Object.keys(response));

    // classNo 유지
    let classNo = $('#selectClass').val();

    if (method === 'POST') {

        if (response.inviteEmail != null) {
            Swal.close();

            Swal.fire({
                title: "초대 완료",
                text: "초대 메일이 성공적으로 전송되었습니다.",
                icon: "success",
                customClass: {
                    confirmButton: 'btn-ab btn-ab-swal'
                }
            }).then((result) => {
                $('.button-x').click();
            })

            // 업데이트된 데이터를 사용하여 갱신
            updateProfileLists(response);
            updateStatusCounts();
        }
    }

    // 승인대기 상태변경
    if (response.teacherKinderAcceptNo != null) {
        updateProfileLists(response);
        updateStatusCounts();
    }

    // 초대중 업데이트
    if (response.inviteId != null) {
        // console.log(`inviteId: ${response.inviteId}`);
        if (method === 'PUT') { // 초대메일 재전송 했을 경우에만
            Swal.close();

            Swal.fire({
                title: "전송 완료",
                text: "초대 메일이 성공적으로 전송되었습니다.",
                icon: "success",
                customClass: {
                    confirmButton: 'btn-ab btn-ab-swal'
                }
            });
            updateProfileLists(response);
            updateStatusCounts();
        }
    }

    // dropdown - 선택된 옵션에 selected 속성 추가
    $('#selectClass option').each(function() {
        if ($(this).val() === classNo) {
            $(this).prop('selected', true);
        } else {
            $(this).prop('selected', false);
        }
    });

    initStatus();
    ifResultNull();
    changeByStatus();

    registerEventHandlers();
}
