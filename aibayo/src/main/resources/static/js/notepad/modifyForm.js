// 라디오 체크용
const elementsToCheck = ['mood', 'health', 'temperature', 'meal', 'sleeptime', 'defecation_status'];
const dataNames = ['mood', 'health', 'temperature', 'meal', 'sleep-time', 'defecation-status'];

$(document).ready(function() {
    let date = moment($('.datepicker').data('date')).format('YYYY.MM.DD');
    $('.datepicker').val(date);

    $('.summernote').summernote({
        height: 300,
        lang: 'ko-KR' // 한국어 설정
    });

    $('.summernote').summernote('code', $('.summernote').data('board-contents'));

    for (let i = 0; i < elementsToCheck.length; i++) {
        let element = elementsToCheck[i];
        let dataName = dataNames[i];
        let checkNum = $('.' + element + '-title').data(dataName)

        console.log(`${element}-title: ${checkNum}`);

        $('#' + element + checkNum).prop('checked', 'true');
    }

    // 수정 시 내용 없을 경우 포커스
    $('#modifyBtn').on('click', function () {
        initMsg();

        let contentEmpty = $('.summernote').summernote('isEmpty');

        if (contentEmpty) {
            console.log("내용 공란");
            let msg = $('.summernote').closest('.item_group').find('.msg');
            msg.show();
            $('.summernote').summernote('focus');

            return false;
        }

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
                let modifyForm = $('#modifyForm');

                setData(modifyForm);

                let param = {
                    boardNo : modifyForm.data('board-no'),
                    boardContents : modifyForm.data('board-contents'),
                    notepadNo : modifyForm.data('notepad-no'),
                    notepadDate : modifyForm.data('notepad-date'),
                    mood : modifyForm.data('mood'),
                    health : modifyForm.data('health'),
                    temperature : modifyForm.data('temperature'),
                    meal : modifyForm.data('meal'),
                    sleepTime : modifyForm.data('sleep-time'),
                    defecationStatus : modifyForm.data('defecation-status')
                }

                console.log('param: ' + JSON.stringify(param));

                let url = '/notepad/modifyOk';

                commonAjax(url, 'PUT', param);
            }

        });

    });
});

function afterSuccess(response) {
    Swal.fire({
        title: "수정 완료",
        text: "창을 닫으면 상세 화면으로 돌아갑니다.",
        icon: "success",
        customClass: {
            confirmButton: 'btn-ab btn-ab-swal'
        }
    }).then((result) => {
        let notepadNo = $('#modifyForm').data('notepad-no');
        window.location.href = window.location.origin + '/notepad/admin/' + notepadNo;
    })
}

function setBoardContent(selector) {
    selector.data('board-contents', $('.summernote').summernote('code'));
}