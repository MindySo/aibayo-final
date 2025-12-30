// 라디오 체크용
const elementsToCheck = ['mood', 'health', 'temperature', 'meal', 'sleeptime', 'defecation_status'];
const dataNames = ['mood', 'health', 'temperature', 'meal', 'sleep-time', 'defecation-status'];


$(document).ready(function() {
    $('.summernote_create').summernote({
        height: 300,
        lang: 'ko-KR' // 한국어 설정
    });

    // 오늘 날짜 자동으로 들어가게 설정
    // UTC와 한국 시차(+9시간) 구해서 날짜 객체 생성
    const offset = new Date().getTimezoneOffset() * 60 * 1000;
    const today = new Date(Date.now() - offset);

    $(".datepicker").val(today.toISOString().substring(0,10).replaceAll('-','.'));
    console.log($(".datepicker").val());

    /* 기본으로 반선택/원생선택 select  숨기기 */
    $("#selectClass").hide();
    $("#selectKid").hide();
    $("#liferecord").hide();

    /* 알림 범위 select 선택 결과에 따라 하위 select 표시 */
    $("#selectScope").change(function() {
        var value= $(this).val();
        $("#selectClass").hide();
        $("#selectKid").hide();
        $("#liferecord").hide();
        $("#liferecord input[type='radio']").prop("required", false);
        $('#selectKid').val('0');
        $('#selectClass').val('0');

        // console.log("value: " + value);

        if (value == "1") {
            $("#selectClass").show();
            $("#liferecord").hide();
            $("#liferecord input[type='radio']").prop("required", false);
        } else if (value == "2") { // 원생이 선택될 경우, 생활기록이 같이 표시되도록 설정
            $("#selectKid").show();
            $("#liferecord").show();
            $("#liferecord input[type='radio']").prop("required", true);
        }
    });



    // 등록 시 작성한 값이 누락될 경우 alert 표시 후 포커스
    $('#writeBtn').on('click', function () {
        initMsg();

        let datepicker = $('.datepicker');

        let selectScope = $('#selectScope');
        let selectClass = $('#selectClass');
        let selectKid = $('#selectKid');

        let summernote = $('.summernote_create');
        let contentEmpty = summernote.summernote('isEmpty');

        if (datepicker.val() === '') {
            console.log("날짜 공란");
            let msg = datepicker.siblings('.msg');
            console.log("tooltip: ", msg.text());
            msg.show();
            datepicker.focus();
            return false;
        }

        if (selectScope.val() === '0' || (selectClass.val() === '0' && selectKid.val() === '0')) {
            console.log("수신자 공란");
            // let msg = $('.sender').siblings('.msg');
            let msg = selectScope.closest('.item_group').find('.msg');
            console.log("tooltip: ", msg.text());
            msg.show();
            selectScope.focus();
            return false;
        }

        if (contentEmpty) {
            console.log("내용 공란");
            let msg = summernote.closest('.item_group').find('.msg');
            console.log("tooltip: ", msg.text());
            msg.show();
            summernote.summernote('focus');
            return false;
        }

        // console.log(summernote.summernote('code'));

        let liferecord = $('.liferecord');

        if (liferecord.is(':visible')) {

            for (const selector of elementsToCheck) {
                let classSelector = '.' + selector;

                if (!isChecked($(classSelector))) {
                    $(classSelector).closest('.liferecord_item_box').find('.msg').show();
                    $(classSelector).first().focus();
                    return;
                }
            }
        }

        let writeFrm = $('#writeFrm');

        setData(writeFrm);

        let param = {
            boardType : writeFrm.data('board-type'),
            writer : writeFrm.data('writer'),
            boardTitle : writeFrm.data('board-title'),
            boardContents : writeFrm.data('board-contents'),
            boardKinderNo : writeFrm.data('board-kinder-no'),
            hasLifeRecord : writeFrm.data('has-life-record'),
            notepadDate : writeFrm.data('notepad-date'),
            mood : writeFrm.data('mood'),
            health : writeFrm.data('health'),
            temperature : writeFrm.data('temperature'),
            meal : writeFrm.data('meal'),
            sleepTime : writeFrm.data('sleep-time'),
            defecationStatus : writeFrm.data('defecation-status'),
            classNo : writeFrm.data('class-no'),
            kidNo : writeFrm.data('kid-no')
        }
        console.log('param: ' + JSON.stringify(param));

        let url = '/notepad/writeOk';

        commonAjax(url, 'POST', param);
    });
});

function afterSuccess(response) {
    Swal.fire({
        title: "등록 완료",
        text: "창을 닫으면 목록 화면으로 돌아갑니다.",
        icon: "success",
        customClass: {
            confirmButton: 'btn-ab btn-ab-swal'
        }
    }).then((result) => {
        window.location.href = window.location.origin + '/notepad/admin/list';
    });
}

function setTitle(selector) {
    let selectScope = $('#selectScope').val();
    let title = ' 알림장';
    let classNo = $('#selectClass').val();
    let kidNo = $('#selectKid').val();


    if (selectScope === '1') {
        selector.data('class-no', classNo);
        selector.data('kid-no', '');
        selector.data('board-title', $('#class' + classNo).text() + title);
    } else if (selectScope === '2') {
        title = '의' + title;
        selector.data('kid-no', kidNo);
        selector.data('class-no', '');
        selector.data('board-title', $('#kid' + kidNo).text() + title);
    }
}

function setBoardContent(selector) {
    selector.data('board-contents', $('.summernote_create').summernote('code'));
}
