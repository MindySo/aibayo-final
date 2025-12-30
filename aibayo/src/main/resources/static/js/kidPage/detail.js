$(document).ready(function () {
    $('#modifyKidName').on('click', function () {
        initMsg();

        let kidName = $('#kidName').val().trim();
        let kidBirth = $('#kidBirth').val().trim();

        if (kidName === '') {
            $('#kidName').closest('.kid_info_box')
                .find('.msg').show();
            $('#kidName').focus();
            return false;
        }

        if (kidBirth === '') {
            $('#kidBirth').closest('.kid_info_box')
                .find('.msg').show();
            $('#kidBirth').focus();
            return false;
        }

        function modifyInfo(result) {
            // console.log("원생명 변경 함수")
            let param = {
                kidNo : $('#kidProfile').data('kid-no'),
                kidName : kidName
            }

            // console.log(`param: ${JSON.stringify(param)}`);

            let url = "/kid/modifyOk"

            commonAjax(url, 'PUT', param);
        }

       alertConfirmModify(modifyInfo);
    });

    $('#modifyKidGender').on('click', function () {
        function modifyInfo(result) {
            // console.log("원생성별 변경 함수");
            let param = {
                kidNo : $('#kidProfile').data('kid-no'),
                kidGender : $('input[id^="kidGender"]:checked').val()
            }

            let url = "/kid/modifyOk";

            commonAjax(url, 'PUT', param);
        }

        alertConfirmModify(modifyInfo);
    });

   $('#modifyKidBirth').on('click', function () {
       function modifyInfo(result) {
           // console.log("원생생일 변경 함수")
           let param = {
               kidNo : $('#kidProfile').data('kid-no'),
               kidBirth : moment($('#kidBirth').val()).format('YYYY-MM-DD')
           }

           // console.log(`param: ${JSON.stringify(param)}`);

           let url = "/kid/modifyOk"

           commonAjax(url, 'PUT', param);
       }

       alertConfirmModify(modifyInfo);
   });

    $('#dischargeKid').on('click', function () {
        Swal.fire({
            title: "정말로 퇴소시키겠습니까?",
            text: "퇴소 후 취소할 수 없습니다.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "퇴소",
            cancelButtonText: "취소"

        }).then((result) => {
            if (result.isConfirmed) {
                let url = "/kid/deleteOk"

                // 모든 관계 제거
                $('.info_item').each(function () {
                    let param = {
                        acceptNo : $(this).data('accept-no')
                    }
                    // console.log(`remove_relation param: ${JSON.stringify(param)}`);

                    commonAjax(url, 'DELETE', param);
                });

                let param = {
                    kidNo : $('#kidProfile').data('kid-no')
                }
                // console.log(`kidNo param: ${JSON.stringify(param)}`)

                commonAjax(url, 'DELETE', param);

            }
        });
    });

    $(document).on('click', '.remove_relation', function () {
        Swal.fire({
            title: "정말로 삭제하시겠습니까?",
            text: "삭제 후 복구할 수 없습니다.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "삭제",
            cancelButtonText: "취소"

        }).then((result) => {
            if (result.isConfirmed) {
                let $this = $(this);

                // console.log("accept_log 삭제");
                let param = {
                    acceptNo : $this.closest('.info_item').data('accept-no')
                }

                let url = "/kid/deleteOk"

                commonAjax(url, 'DELETE', param);

                // item이 하나만 남을 경우 삭제 버튼 비표시
                let itemBox = $this.closest('.info_content_box');
                // console.dir(itemBox);

                let itemCount = itemBox.find('.info_item').length - 1;
                // console.log(`itemCount: ${itemCount}`);

                $this.closest('.info_item').remove();

                if (itemCount === 1) {
                    let singleItem = itemBox.find('.info_item');
                    singleItem.find('.close_box').remove();
                }
            }
        });
    });


    $('#sendEmailBtn').on('click', function () {
        initMsg();

        let inviteName = $('#parentName').val().trim();
        // console.log(`inviteName: ${inviteName}`);
        let inviteEmail = $('#parentEmail').val().trim();
        // console.log(`inviteEmail: ${inviteEmail}`);

        if (inviteName === '') {
            // console.log("이름 공란");
            let msg = $('#parentName').siblings('.msg_box').find('.msg');
            msg.show();
            $('#parentName').focus();
            return false;
        }

        let emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        if (inviteEmail === '' || !emailPattern.test(inviteEmail)) {
            // console.log("이메일 형식 유효성 오류")
            let msg = $('#parentEmail').siblings('.msg_box').find('.msg');
            msg.show()
            $('#parentEmail').focus();
            return false;
        }

        let url = "/inviteCode/mail";

        let param = {
            inviteType : 1,
            inviteEmail : inviteEmail,
            inviteName : inviteName,
            kinderNo : $('#kidProfile').data('kinder-no'),
            kidNo : $('#kidProfile').data('kid-no'),
            url: window.location.origin
        }
        // console.log(`param: ${JSON.stringify(param)}`);

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


   $('#addClassModal').on('show.bs.modal', function (e) {
       let selectedClasses = [];

        $('.class_item').each(function () {
            selectedClasses.push($(this).data('class-no'));
        });

        // console.log(`소속 반: ${selectedClasses}`);

        for (let classNo of selectedClasses) {
            $('#classCheck' + classNo).prop('checked', true);
            $('#classCheck' + classNo).prop('disabled', true);
        }

   });

   $('#addClassModal').on('hidden.bs.modal', function () {
       $('.modal_class_item').each(function () {
          let modalCheck = $(this).find('input[id^="classCheck"]');

           if (modalCheck.is(':checked') && !modalCheck.is(':disabled')) {
               modalCheck.prop('checked', false);
           }
       });
   });

    $('#addParentModal').on('hidden.bs.modal', function () {
        $('.modal_parent_input').each(function () {
            $(this).val('');
        });
    });


   $('#modalAddClassBtn').on('click', function () {
       function modifyInfo(result) {
           // console.log("반추가 모달 추가 버튼 클릭");

           let modifyClassList = [];

           $('.modal_class_item').each(function () {
               let modalCheck = $(this).find('input[id^="classCheck"]');

               if (modalCheck.is(':checked') && !modalCheck.is(':disabled')) {
                   modifyClassList.push(modalCheck.val())
               }
           });

           let url = "/kid/modifyOk"

           for (let classNo of modifyClassList) {
               let param = {
                   kidNo : $('#kidProfile').data('kid-no'),
                   classNo : classNo
               }

               // console.log(`param: ${JSON.stringify(param)}`);

               commonAjax(url, 'POST', param);
           }
       }

       alertConfirmModify(modifyInfo);
   });

    if ($('#kidName').is(':disabled')) {
        let kidName = $('#kidName').val();
        let kidGender = $('#kidName').data('kid-gender');

        if (kidGender === 1) {
            $('#kidName').val(`${kidName}(남아)`);
        } else if (kidGender === 2) {
            $('#kidName').val(`${kidName}(여아)`);
        }

    }

});

function initMsg() {
    $('.msg').each(function () {
        $(this).hide();
    })
}

function afterSuccess(response, method) {
    // console.log(`response: ${response}`);

    if (method === 'PUT') {
        if (response.kidName != null || response.kidBirth != null ||
            response.kidGender != null) {
            $('#kidName').val(response.kidName);
            $('#kidGender' + response.kidGender).prop('checked', true);
            $('#kidBirth').val(moment(response.kidBirth).format('YYYY.MM.DD'));
        }
    }

    if (method === 'POST') {
        // response 형식에 따라 작업 분기

        if ($(response).find('#kidProfile').length > 0) {
            $('#parentKidDiv').replaceWith($(response).find('#parentKidDiv'));
            $('#classKidDiv').replaceWith($(response).find('#classKidDiv'));

            $('#closeParentModal').click();
            $('#closeClassModal').click();
        }

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
                $('#closeParentModal').click();
            })
        }
    }

    if (method === 'DELETE' && response.dischargeFlag === '1') {
        Swal.fire({
            title: "퇴소 완료",
            text: "창을 닫으면 목록 화면으로 돌아갑니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {
            window.location.href = window.location.origin + '/kid/list';
        })
    }
}

function alertConfirmModify(modifyInfo) {
    Swal.fire({
        title: "정말로 수정하시겠습니까?",
        showCancelButton: true,
        confirmButtonText: "네",
        cancelButtonText: "아니오",
        customClass: {
            confirmButton: 'btn-ab btn-ab-swal'
        }
    }).then((result) => {
        if (result.isConfirmed) {
             modifyInfo(result);
        }
    });
}
