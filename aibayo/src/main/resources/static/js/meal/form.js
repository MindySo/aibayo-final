const checkedMealTypes = [];

// 체크박스 체크 되어 있을 경우에만 div 표시
function toggleDivs() {
    checkedMealTypes.length = 0;

    $('input[type="checkbox"]').each(function() {
        var checkboxId = $(this).attr('id');
        var number = checkboxId.match(/\d+/)[0];
        var divId = '#mealItemDiv' + number;
        if ($(this).is(':checked')) {
            checkedMealTypes.push($(this).val());
            $(divId).show();
        } else {
            $(divId).hide();
        }
    });

    console.log(`checkMealTypes: ${checkedMealTypes}`);
}

$(document).ready(function(){
    $('.meal_item_box_form').hide();

    // 페이지 로드 시 초기 상태 확인
    toggleDivs();

    // 체크박스 상태 변경 시
    $('input[type="checkbox"]').change(function() {
        toggleDivs();
    });

    if ($('.datepicker').val() === '') { // writeForm에서 오늘 날짜 자동으로 들어가게 설정
        // UTC와 한국 시차(+9시간) 구해서 날짜 객체 생성
        const offset = new Date().getTimezoneOffset() * 60 * 1000;
        const today = new Date(Date.now() - offset);

        $('.datepicker').val(today.toISOString().substring(0,10).replaceAll('-','.'));
        // console.log($('.datepicker').val());
    }


    // + 클릭 시 메뉴명 input 추가
    $('.menu_input_plus').on('click', function() {
        console.log("메뉴 추가");

        var parentElement = $(this).closest('.meal_menu_box_form');
        var parentId = parentElement.attr('id');

        if (parentId) {
            var newMenuInputForm = $('<div class="menu_input_form">' +
                '<input type="text" class="form-control menu_input_text" name="mealMenu1" placeholder="메뉴명을 입력해주세요.">' +
                '<div class="btn menu_input_close" title="메뉴 삭제">x</div>' +
                '</div>');
            $('#' + parentId).append(newMenuInputForm);
        } else {
            console.warn('Parent element does not have an ID');
        }
    });


    // x 클릭 시 메뉴명 input 삭제
    $(document).on('click', '.menu_input_close', function() {
        console.log("메뉴 삭제");
        $(this).closest('.menu_input_form').remove();
    });


    // 파일 등록 시 이미지 미리보기 및 형식 검증
    $('[id^=mealPic]').on('change', function(event) {
        let files = event.target.files;
        // var previewId = $(this).attr('id') + 'Preview';
        let label = $(this).closest('label');
        console.dir(label.find('.meal_pic_origin'));
        label.find('.meal_pic_origin').hide(); // 이전 미리보기 숨기기
        label.find('svg').hide(); // SVG 숨기기
        label.find('.new_meal_pic').remove(); // 기존 프리뷰 삭제

        if (files.length > 0) {
            let file = files[0];
            let reader = new FileReader();
            const maxSizeInMB = 10; // 최대 파일 크기 제한 (MB 단위)
            const maxSizeInBytes = maxSizeInMB * 1024 * 1024; // 바이트 단위로 변환

            reader.onload = function(e) {
                console.log(`file type: ${file.type}`);
                if (file.type.startsWith('image/')) {
                    // 용량 확인
                    let fileSize = file.size; // 파일 크기 (바이트 단위)
                    if (fileSize > maxSizeInBytes) {
                        Swal.fire({
                            icon: "error",
                            title: "업로드 실패",
                            text: "10MB 이하의 사진만 등록 가능합니다.",
                        });
                        event.target.value = ""; // 파일 선택 취소
                        label.find('svg').show();
                        return false;
                    }

                    label.append('<img src="' + e.target.result + '" alt="사진 미리보기" ' +
                        ' class="modal_meal_img new_meal_pic">');
                } else { // 이미지 파일이 아닐 경우
                    Swal.fire({
                        icon: "error",
                        title: "업로드 실패",
                        text: "이미지 파일만 선택할 수 있습니다.",
                    });
                    event.target.value = ""; // 파일 선택 취소
                    label.find('svg').show();
                    // label.append('<p>' + file.name + '</p>');
                }
            };

            reader.readAsDataURL(file);
        } else { // 파일을 등록하지 않았을 경우, 원래 img/svg 표시
            let url = window.location.href;
            console.log(url);

            if (url.includes('/meal/admin/write')) {
                label.find('svg').show();
            } else if (url.includes('/meal/admin/modify')) {
                // label.append('<img src="' + "http://via.placeholder.com/330x300" + '" alt="사진 미리보기" ' +
                //     ' class="modal_meal_img">');
                label.find('.new_meal_pic').remove();
                label.find('.meal_pic_origin').show();
            }

        }
    });

    // writeForm 등록 클릭
    $('#writeMealBtn').on('click', function () {
        // console.log(`등록 버튼 클릭`);

        $('#writeMealForm').submit();
    });

    $('#writeMealForm').on('submit', function (e) {
        console.log(`write 폼 제출`);
        e.preventDefault(); // 폼의 기본 제출을 막음

        const formData = new FormData();
        if (!setForm(formData)) {
            return false;
        }

        if (formData != null) {
            $.ajax({
                url: "/meal/writeOk",
                type: "POST",
                processData: false,
                contentType: false,
                enctype: "multipart/form-data",
                data: formData,
                success: function(response) {
                    // 서버로부터의 응답 처리
                    console.log(response);
                    Swal.fire({
                        title: "등록 완료",
                        text: "창을 닫으면 목록 화면으로 돌아갑니다.",
                        icon: "success",
                        customClass: {
                            confirmButton: 'btn-ab btn-ab-swal'
                        }
                    }).then((result) => {
                        window.location.href = window.location.origin + '/meal/admin/list';
                    });
                },
                error: function(err) {
                    // 에러 처리
                    console.log(err);
                }
            });
        }

    });

    $('#modifyMealBtn').on('click', function () {
        $('#modifyMealForm').submit()
    });

    $('#modifyMealForm').on('submit', function (e) {
        console.log(`modify 폼 제출`);
        e.preventDefault(); // 폼의 기본 제출을 막음

        const formData = new FormData();
        if (!setForm(formData)) {
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

                Swal.fire({
                    title: '수정 중...',
                    text: '식단표를 수정하고 있습니다.',
                    allowOutsideClick: false,
                    didOpen: () => {
                        Swal.showLoading();
                    }
                });

                $.ajax({
                    url: "/meal/modifyOk",
                    type: "PUT",
                    processData: false,
                    contentType: false,
                    enctype: "multipart/form-data",
                    data: formData,
                    success: function (response) {
                        // 서버로부터의 응답 처리
                        console.log(response);
                        Swal.close();

                        Swal.fire({
                            title: "수정 완료",
                            text: "창을 닫으면 상세 화면으로 돌아갑니다.",
                            icon: "success",
                            customClass: {
                                confirmButton: 'btn-ab btn-ab-swal'
                            }
                        }).then((result) => {
                            window.location.href = window.location.origin + '/meal/admin/list';
                        });
                    },
                    error: function (err) {
                        // 에러 처리
                        console.log(err);
                    }
                });
            }

        });
    });
});

function setForm(formData) {
    initMsg();

    // console.log(`checkRequired() : ${checkRequired()}`)
    if (!checkRequired()) { return false; }
    // console.log(`checkedMealTypes : ${checkedMealTypes}`);
    // console.log(`length: ${checkedMealTypes.length}`);


    let mealDate = moment($('#mealDate').val().trim()).format('YYYY-MM-DD');
    // console.log(`mealDate: ${mealDate}`);


    let mealDetails = [];
    if (!setDetails(mealDetails)) { return false; }
    // console.log(`mealDetails: ${JSON.stringify(mealDetails)}`);



    // 데이터 세팅
    let param = {
        mealNo : $('#modifyMealForm').data('meal-no'),
        kinderNo : $('#writeMealForm').data('kinder-no'),
        mealDate : mealDate,
        mealDetails : mealDetails
    }
    console.log(`param: ${JSON.stringify(param)}`);

    // 파일 전송
    let fileInput = $('.meal_input_file');
    for (let i = 0; i < fileInput.length; i++) {
        if (fileInput[i].files.length > 0) {
            for (let j = 0; j < fileInput[i].files.length; j++) {
                formData.append('files', fileInput[i].files[j]);
            }
        }
    }

    // 폼에 dto 저장
    formData.append("mealDto",
        new Blob([JSON.stringify(param)], {type: "application/json"}));

    return true;

}

function checkRequired() {
    if (checkedMealTypes.length < 1) {
        $('.meal_time_checkbox').siblings('.msg').show();
        return false;
    }

    if ($('#mealDate').val().trim() === '') {
        $('.meal_datepicker').siblings('.msg').show();
        return false;
    }

    return true;
}

function setDetails(mealDetails) {

    for (let num of checkedMealTypes) { // 상세 빈 값 확인

        // console.dir($('#mealPic'+ num));
        let mealPic = $('#mealPic' + num);
        // console.dir(mealPic);

        if (!checkMealPic(mealPic)) { return false; }

        let filePath = mealPic.val();
        let fileName = filePath.substring(filePath.lastIndexOf('\\') + 1);

        let menuBox = $('#meal_menu_box_form' + num);
        let target = menuBox.find('.menu_input_text');
        let menu = Array.from(target)
            .map(target => target.value.trim())
            .filter(value => value !== '')
            .join('::');
        // console.log(`menu: ${menu}`);

        if (!checkMealMenu(menuBox, menu)) { return false; }

        let mealDetailNo = $('#mealItemDiv' + num).find('.meal_detail_no').val();

        let mealDetail = {
            mealDetailNo : mealDetailNo,
            mealType : num,
            mealMenu : menu,
            mealPicOriginalName : fileName
        }

        mealDetails.push(mealDetail);
    }

    return true;
}

function checkMealPic(mealPic) {
    console.log(mealPic.siblings('img').attr('src'));
    if (mealPic.val().trim() === ''
        && mealPic.siblings('img').attr('src') === '') { // 사진 빈 값 확인
        // console.log(`사진 미등록`);

        let picBox = mealPic.closest('.modal_meal_img_box');
        picBox.find('.msg').show();
        // console.log(`scrollTop: ${mealPic.offset().top}`);
        // console.log(`${JSON.stringify(mealPic.offset())}`);
        // console.log(`${JSON.stringify(picBox.offset())}`);

        picBox[0].scrollIntoView({ behavior: 'smooth' });

        return false;
    }

    return true;
}

function checkMealMenu(menuBox, menu) {
    if (menu.replaceAll('::', '') === '') {
        menuBox.find('.msg').show();
        menuBox.find('input').first().focus();
        return false;
    }

    return true;
}
