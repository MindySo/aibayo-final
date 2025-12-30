$(document).ready(function() {
    initStatus();

    changeByStatus();

    $('#selectClass').on('change', function () {
        let classNo = $('#selectClass').val();
        // console.log(`반 변경: ${classNo}`);

        let param = {
            classNo : classNo
        };

        // console.log(`param: ${JSON.stringify(param)}`);

        let url = "/kid/searchByClass"

        commonAjax(url, 'POST', param);
    });


    // 학부모 승인요청 처리
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
                let target = $(this).closest('.kid_item_btns');

                let url = "/kid/modifyOk";

                let param = {
                    parentKidAcceptNo : target.data('parent-kid-accept-no'),
                    kidNo : target.data('kid-no')
                }
                // console.log(`param : ${JSON.stringify(param)}`);

                target.closest('.kid_item_parent').remove();

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
                let target = $(this).closest('.kid_item_btns');

                let url = "/kid/deleteOk";

                let param = {
                    parentKidAcceptNo : target.data('parent-kid-accept-no')
                }
                // console.log(`param : ${JSON.stringify(param)}`);

                target.closest('.kid_item_parent').remove();

                commonAjax(url, 'DELETE', param);
            }
        });
    });


    // 초대중 처리
    $(document).on('click', '#inviteDelete', function () {
        Swal.fire({
            title: "정말로 취소하시겠습니까?",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "네",
            cancelButtonText: "아니오"
        }).then((result) => {
            if (result.isConfirmed) {
                let target = $(this).closest('.kid_item_btns');

                let url = "/inviteCode/deleteOk";

                let param = {
                    inviteId: target.data('invite-id'),
                    acceptNo : target.data('invite-code-accept-no')
                }
                // console.log(`param : ${JSON.stringify(param)}`);

                target.closest('.kid_item_parent').remove();

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
                let target = $(this).closest('.kid_item_btns');

                let url = "/inviteCode/resendMail";

                let param = {
                    kinderNo: $('#kidsStatus2Div').data('kinder-no'),
                    inviteId: target.data('invite-id'),
                    acceptNo : target.data('invite-code-accept-no')
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
});

function afterSuccess(response, method) {

    // 반 별 조회
    if ($(response).find('.kid_status_box').length > 0) {
        $('.kid_status_box').replaceWith($(response).find('.kid_status_box'));
        $('#kidsStatus1Div').replaceWith($(response).find('#kidsStatus1Div'));
        $('#kidsStatus0Div').replaceWith($(response).find('#kidsStatus0Div'));
        $('#kidsStatus2Div').replaceWith($(response).find('#kidsStatus2Div'));

        initStatus();

        changeByStatus();
    }

    // 승인대기 상태변경
    if (response.parentKidAcceptNo != null) {
        let count = $('#kidsStatus0Div').find('.kid_item_parent').length;

        let countBanner = `승인 대기 ${count}명`;
        let countBox = `승인 대기 중인 학부모 ${count}명`

        let targetBanner = $('label[for="kidsStatus0"]');
        let targetBox = $('#kidsStatus0Div').find('.count_text');

        targetBanner.text(countBanner);
        targetBox.text(countBox);
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
            })
        }

        let count = $('#kidsStatus2Div').find('.kid_item_parent').length;

        let countBanner = `초대 중 ${count}명`;
        let countBox = `초대 중인 학부모 ${count}명`

        let targetBanner = $('label[for="kidsStatus2"]');
        let targetBox = $('#kidsStatus2Div').find('.count_text');

        targetBanner.text(countBanner);
        targetBox.text(countBox);
    }
}

function initStatus() {
    $('.kids_list_box').hide();

    // 초기 로드 시 체크된 라디오 버튼에 해당하는 div를 표시
    var initialSelectedValue = $('input[name="kidsStatusRd"]:checked').val();
    if (initialSelectedValue) {
        $('#kidsStatus' + initialSelectedValue + 'Div').show();
    }
}

function changeByStatus() {
    $('input[name="kidsStatusRd"]').on('change', function() {
        var selectedValue = $('input[name="kidsStatusRd"]:checked').val();

        // 모든 상태 div 숨기기
        $('.kids_list_box').hide();

        // 선택된 값에 따라 해당 div 보이기
        $('#kidsStatus' + selectedValue + 'Div').show();
    });
}