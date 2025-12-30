function afterSuccess(response, method) {
    // console.log(`response: ${response}`);
    // console.log(`response: ${JSON.stringify(response)}`);

    // 전체조회일 경우
    if (response.detail == null && method === 'POST') {
        // console.log(`response: ${JSON.stringify(response)}`);
        let events = calendar.getEvents();
        events.forEach(function(event) {
            if (event.classNames.includes('meal-event')) {
                event.remove();
            }
        });
        // console.log(`events: ${JSON.stringify(events)}`);

        // calendar.removeAllEvents();

        let newEvents = [];
        let title = "식단 조회하기";


        $.each(response, function (index, meal) {
            newEvents.push({
                title: title,
                start: meal.mealDate,
                mealNo: meal.mealNo,
                className: 'meal-event'
            });

        });

        // 새로운 이벤트 추가
        newEvents.forEach(function(eventData) {
            calendar.addEvent(eventData);
        });
    }

    if (response.detail && method === 'POST') {
        // console.log(`상세조회 모달 세팅..`);
        // console.log(`response: ${JSON.stringify(response)}`);

        let roleNo = $('#calendar').data('role-no');
        // console.log(`roleNo: ${roleNo}`);

        // 모달의 내용 세팅
        let modalDetailTag = `<div class="modal fade fixed-width-modal" id="mealDetail"
         tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content">

                <div class="modal-header">
                    <h1 class="modal-title modal_header_title" id="mealDetailLabel">
                        ${moment(response.mealDate).format('yyyy년 M월 D일')} 식단표
                    </h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>


                <div class="modal-body modal_body">

                    <div class="modal_meals_box">

`;

        // detail 수만큼 추가
        for (let detail of response.mealDetails) {
            // console.log(`detail: ${JSON.stringify(detail)}`);

            let menuNames = detail.mealMenu.replaceAll(delimiter, `<br>`);
            // console.log(`menuNames: ${menuNames}`);

            modalDetailTag += `<div class="modal_meal_item_box">

                            <div class="modal_meal_title_box">
                                <div class="modal_meal_title">${detail.mealTypeName}</div>
                            </div>

                            
                            <div class="modal_meal_img_box">
                                <img class="modal_meal_img" src="${detail.mealPic}" alt="식단 사진" />
                            </div>

                            <div class="modal_meal_menu_box">
                                <div class="modal_meal_menu">
                                    ${menuNames}
                                </div>
                            </div>

                        </div>

                    
`;
        }

        // admin/user에 따라 버튼 다르게 표시
        modalDetailTag += `</div>

                </div>


                <div class="modal-footer modal_meal_detail_footer">
                    <div class="modal_btns_box">

                `;

        if (roleNo <= 2) {
            modalDetailTag += `<a class="btn btn-ab modal_btn_box" href="/meal/admin/modify/${response.mealNo}" alt="수정">
                            <div class="modal_btn_text">수정</div>
                        </a>


                        <div class="btn btn-danger modal_btn_box">
                            <div class="modal_btn_text" id="deleteMealBtn"
                                 data-meal-no="${response.mealNo}">
                                삭제
                            </div>
                        </div>

`
        }

        if (roleNo === 3) {
            modalDetailTag += `<div class="btn btn-ab modal_btn_box">
                            <div class="modal_btn_text" data-bs-dismiss="modal" aria-label="Close">닫기</div>
                        </div>`
        }

        modalDetailTag += `</div>
                </div>

            </div>
        </div>
    </div>`

        // 기존 html에 추가
        $('.calendar_box').after(modalDetailTag);

        // 모달 팝업
        $('#mealDetail').modal('show');

        // 이미지 로드 실패 시 placeholder로 대체
        $('.modal_meal_img').on('error', function () {
            // console.log("이미지 못읽음");
            $(this).attr('src', 'http://via.placeholder.com/330x300');
        });
    }

    if (method === 'DELETE') {
        Swal.fire({
            title: "삭제 완료",
            text: "식단표가 삭제되었습니다.",
            icon: "success",
            customClass: {
                confirmButton: 'btn-ab btn-ab-swal'
            }
        }).then((result) => {
            $('#mealDetail').find('.btn-close').click();
            // console.dir($('#mealDetail').find('.btn-close'));

            // 식단표 정보 다시 불러오기
            // console.dir(`mealDate: ${response.mealDate}`);
            let date = moment(response.mealDate);
            let startOfMonth = date.clone().startOf('month');
            let endOfMonth = date.clone().endOf('month');
            // console.log(`startOfMonth: ${startOfMonth}`);
            // console.log(`endOfMonth: ${endOfMonth}`);

            // 캘린더 시작일 구하기 (해당 주의 일요일)
            let startDate = startOfMonth.startOf('week').format('YYYY-MM-DD');

            // 캘린더 마지막일 구하기 (해당 주의 토요일)
            let endDate = endOfMonth.endOf('week');

            // 마지막 날 이후 추가적으로 한 주를 포함하도록 설정
            if (endDate.date() - endOfMonth.date() < 7) {
                endDate = endDate.add(1, 'week');
            }

            endDate = moment(endDate).format('YYYY-MM-DD');

            let url = "/meal/getByMonth";

            let param = {
                startDate : startDate,
                endDate : endDate,
                kinderNo: $('#calendar').data('kinder-no')
            }
            // console.log(`param: ${JSON.stringify(param)}`);

            commonAjax(url, 'POST', param);
        })
    }
}