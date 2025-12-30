$(document).ready(function () {
    // 반추가 모달 팝업 시 처리 로직
    $('#addClassModal').on('show.bs.modal', function () {
        let selectClasses = [];

        $('#selectedClassDiv').find('.item_text').each(function () {
            // console.dir($(this));
            selectClasses.push($(this).data('class-no'));
        });

        $('input[id^="classCheck"]').each(function () {
            $(this).prop('checked', false);
        });

        for (let selectClass of selectClasses) {
            // console.log(`selectClass: ${selectClass}`);
            $('#classCheck' + selectClass).prop('checked', true);
        }
    });

    // 반추가 모달 추가 버튼 클릭 시 처리 로직
    $('#modalAddClassWrite').on('click', function () {
        $('#selectedClassDiv').find('.info_item').each(function () {
            // console.dir($(this));
            $(this).remove();
        });

        let selectClasses = [];

        $('.modal_class_item').each(function () {
            if ($(this).find('input[id^=classCheck]').is(':checked')) {
                selectClasses.push($(this).data('class-info'));
            }
        });

        for (let selectClass of selectClasses) {
            // console.log(`selectClass: ${selectClass}`);
            let classInfo = parseToJson(selectClass);
            // console.log(`after parsing: ${JSON.stringify(classInfo)}`);

            let tag = `<div class="info_item text_div">
                        <div class="item_text_box class_text_box">
                            <div class="item_text text-compact class_item"
                                 title="${classInfo.className}" 
                                 data-class-no="${classInfo.classNo}">
                                ${classInfo.className}
                            </div>
                        </div>

                        <div class="close_box">
                            <button type="button" class="btn-close remove_relation"></button>
                        </div>
                    </div>`

            $('#selectedClassDiv').append(tag);
        }

        // console.log(`selectClasses: ${selectClasses}`);
        $('#addClassModal').find('.btn-close').click();
    });

    $(document).on('click', '.remove_relation', function () {
        let target = $(this).closest('.info_item');
        // console.dir(target);
        target.remove();
    });

    // 원생 등록 시 로직
    $('#admissionKid').on('click', function () {
        // console.log(`등록 버튼 click`);
        initMsg();
        $('#kidGenderRadioBox').removeAttr('tabindex');

        let kidName = $('#kidName').val().trim();
        // console.log(`kidName: ${kidName}`);
        let kidBirth = $('#kidBirth').val().trim();
        // console.log(`kidBirth: ${kidBirth}`);
        let kidGender = $('input[id^="kidGender"]:checked').val() || '';
        // console.log(`kidGender: ${kidGender}`);
        let classNoList = [];
        $('.class_item').each(function () {
            classNoList.push($(this).data('class-no'));
        });
        // console.log(`classNoList: ${classNoList}`);

        // 빈 값 확인
        if (kidName === '') {
            // console.log(`kidName 공란`);
            $('#kidName').closest('.kid_info_box')
                         .find('.msg').show();
            $('#kidName').focus();
            return false;
        }

        if (kidGender === '') {
            // console.log(`kidGender 공란`);
            let target = $('#kidGenderRadioBox');
            // console.dir(target);
            target.closest('.kid_info_box')
                .find('.msg').show();
            target.attr('tabindex', -1);
            target.focus();
            return false;
        }

        if (kidBirth === '') {
            // console.log(`kidBirth 공란`);
            $('#kidBirth').closest('.kid_info_box')
                          .find('.msg').show();
            $('#kidBirth').focus();
            return false;
        }

        if (classNoList.length < 1) {
            // console.log(`소속 반 없음`);
            $('.classes_info_title_box').siblings('.msg').show();
            let button = $('#addClassBtn');
            button.focus();
            $('html, body').animate({
                scrollTop: button.offset().top
            }, 'smooth');
            return false;
        }

        let url = "/kid/writeOk";

        let param = {
            kinderNo : $('#admissionKid').data('kinder-no'),
            kidName : kidName,
            kidBirth : moment(kidBirth).format('YYYY-MM-DD'),
            kidGender : kidGender,
            classNoList : classNoList
        }
        // console.log(`param: ${JSON.stringify(param)}`);

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
        window.location.href = window.location.origin + '/kid/list';
    });
}

function parseToJson(dto) {
    // DTO 문자열의 시작과 끝을 잘라내기
    const trimmed = dto.slice(dto.indexOf('(') + 1,
                              dto.lastIndexOf(')'));

    // 각 속성을 쉼표로 분리
    const properties = trimmed.split(', ');

    // 객체로 변환
    const obj = properties.reduce((acc, current) => {
        const [key, value] = current.split('=');
        // 값이 'null' 문자열일 경우 JavaScript의 null로 변환
        acc[key] = value === 'null' ? null : value;
        return acc;
    }, {});

    return obj;
}