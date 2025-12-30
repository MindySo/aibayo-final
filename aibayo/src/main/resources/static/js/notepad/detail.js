$(document).ready(function () {
    if ($('.notepad_detail_content_body').find('.liferecord').length > 0) {
        // console.log("생활기록 존재")
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
    } else {
        // console.log("생활기록 없음");
    }

    $('#deleteBtn').on('click', function () {
        Swal.fire({
            title: "정말로 삭제하시겠습니까?",
            text: "삭제한 알림장은 복구할 수 없습니다.",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#dc3545",
            confirmButtonText: "삭제",
            cancelButtonText: "취소"

        }).then((result) => {
            if (result.isConfirmed) {
                let boardNo = $('#deleteBtn').data('boardNo');

                let param = {
                    boardNo : boardNo
                };

                console.log(`param: ${JSON.stringify(param)}`);

                let url = "/notepad/delete";

                commonAjax(url, 'DELETE', param);
            }
        })
    });
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
    })
}